package de.dennisbruner.viergewinnt.network.packets.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dennisbruner.viergewinnt.network.packets.AbstractPacket;

public class PacketSerializer {
	
	// Bytes, die immer am Anfang vorhanden sind.
	// Damit kann ich abfragen, ob das überhaupt ein
	// relevantes Paket für mich ist.
	public static final byte[] MAGIC_BYTES = new byte[] { (byte) 0x13, (byte) 0x37, (byte) 0xBE, (byte) 0xEF };
	
	// Aktuelle Version des Protokolls.
	// Wenn diese nicht übereinstimmt werden Pakete verworfen.
	public static final byte VERSION = 1;
	
	// Maximale Länge der Nutzdaten
	// (in dem Fall 32767 Bytes weils kein unsigned in Java gibt *würg*)
	public static final int MAX_DATA_LENGTH = Short.MAX_VALUE;
	
	// Feste Headerlänge
	public static final int HEADER_LENGTH = MAGIC_BYTES.length + Byte.BYTES + Byte.BYTES + Short.BYTES;
	
	// Mögliche Pakete mit IDs verbinden
	private static final Map<Byte, Class<?>> PACKET_CLASS_MAP = new HashMap<Byte, Class<?>>();
	static {
		// TODO: declare packets
	}
	
	public byte[] serialize(AbstractPacket packet) throws IllegalArgumentException, IllegalAccessException {
		Class<?> c = packet.getClass();
		
		// Paketbuffer erstellen
		ByteBuffer buf = ByteBuffer.allocate(HEADER_LENGTH + MAX_DATA_LENGTH);
		int count = 0;
		
		// Zuerst werden die Magic Bytes geschrieben
		buf.put(MAGIC_BYTES);
		count += MAGIC_BYTES.length;
		
		// Dann die Protokollversion
		buf.put(VERSION);
		count += Byte.BYTES;
		
		// Anschließend die Paket ID
		buf.put(packet.getPacketID());
		count += Byte.BYTES;
		
		// Da ich noch nicht weiß wie groß die Nutzdaten werden,
		// erstelle ich hier noch seperat einen ByteBuffer.
		//
		// Könnt ich auch als Funktion auslagern aber neeeeeeeee
		{
			ByteBuffer databuf = ByteBuffer.allocate(MAX_DATA_LENGTH);
			int datacount = 0;
			
			// Variablen mit dem PacketAttribute
			List<Field> fields = new LinkedList<Field>();
			for (Field f : c.getDeclaredFields()) {
				// Da Variablen auch private sein können, muss ich den
				// Zugriff explizit erlauben.
				f.setAccessible(true);
				// Wenn die Variable so ein PacketAttribute aufweist,
				// dann zur Liste hinzufügen.
				if (f.getDeclaredAnnotation(PacketAttribute.class) != null) {
					fields.add(f);
				}
			}

			// Jede einzelne Variable in Bytes umwandeln
			for (Field f : fields) {
				// Wert auslesen
				Object value = f.get(packet);
				
				// Je nach Datentyp wird der Wert anders in Bytes umgewandelt
				if (f.getType() == byte.class) {
					databuf.put((byte) value);
					datacount += Byte.BYTES;
				} else if (f.getType() == char.class) {
					databuf.putChar((char) value);
					datacount += Character.BYTES;
				} else if (f.getType() == short.class) {
					databuf.putShort((short) value);
					datacount += Short.BYTES;
				} else if (f.getType() == int.class) {
					databuf.putInt((int) value);
					datacount += Integer.BYTES;
				} else if (f.getType() == long.class) {
					databuf.putLong((long) value);
					datacount += Long.BYTES;
				} else if (f.getType() == float.class) {
					databuf.putFloat((float) value);
					datacount += Float.BYTES;
				} else if (f.getType() == double.class) {
					databuf.putDouble((double) value);
					datacount += Double.BYTES;
				} else if (f.getType() == boolean.class) {
					databuf.put((byte) ((boolean) value ? 0x01 : 0x00));
					datacount += Byte.BYTES;
				} else if (f.getType() == String.class) {
					// Ein String ist ein spezieller Datentyp.
					//
					// Da seine Länge variabel ist, muss ich bevor ich
					// den String in den Buffer schreibe die Länge des
					// kommenden Strings "ankündigen" um später beim
					// Auslesen zu wissen wieviele Bytes ich überhaupt
					// lesen darf/muss.
					String str = (String) value;
					
					// Erst den String in UTF-8 kodieren
					ByteBuffer strbuf = StandardCharsets.UTF_8.encode(str);
					byte[] strbytes = new byte[strbuf.remaining()];
					strbuf.get(strbytes);
					
					// Dann die Länge der Bytes schreiben
					databuf.putInt(strbytes.length);
					datacount += Integer.BYTES;

					// Anschließend den tatsächlichen String hinzufügen
					databuf.put(strbytes);
					datacount += strbytes.length;
				}
			}
			
			// Größe des Buffers festlegen
			databuf.position(0);
			databuf.limit(datacount);
			
			byte[] data = new byte[databuf.remaining()];
			databuf.get(data);
			
			// Länge zum Short umwandeln und hinzufügen
			buf.putShort((short) data.length);
			count += Short.BYTES;
			
			// Nutzdaten hinzufügen
			buf.put(data);
			count += data.length;
		}

		// Größe des Buffers festlegen
		buf.position(0);
		buf.limit(count);
		
		// Buffer als Byte array zurückgeben
		byte[] data = new byte[buf.remaining()];
		buf.get(data);
		
		return data;
	}
	
	public AbstractPacket deserialize(byte[] bytes) throws PacketTooSmallException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
		// Prüfe, ob das Paket überhaupt groß genug ist.
		// (Der Header sollte wenigstens vollständig da sein)
		if (bytes.length < HEADER_LENGTH)
			throw new PacketTooSmallException();
		
		// Buffer aus den Bytes erstellen
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		
		// Magic bytes auslesen und prüfen
		{
			byte[] magicBytes = new byte[MAGIC_BYTES.length];
			buf.get(magicBytes);
			
			// null zurückgeben, wenn magic bytes nicht übereinstimmen
			for (int i = 0; i < MAGIC_BYTES.length; i++)
				if (magicBytes[i] != MAGIC_BYTES[i])
					return null;
		}
		
		// Version byte auslesen und prüfen
		{
			byte version = buf.get();
			
			// Ebenfalls null zurückgeben, wenn die Version nicht übereinstimmt
			if (version != VERSION)
				return null;
		}
		
		// Paket ID auslesen
		Class<?> packetClass;
		{
			byte packetID = buf.get();
			packetClass = PACKET_CLASS_MAP.get(packetID);
		}
		
		// Länge der Nutzdaten
		int datalen = (int) buf.getShort();
		buf.limit(buf.position() + datalen);
		
		// Neue Paketinstanz erstellen, die wir befüllen.
		// Dafür muss es einen Konstruktor ohne Parameter geben.
		AbstractPacket packet = (AbstractPacket) packetClass.getConstructor().newInstance();
		
		// Variablen mit dem PacketAttribute
		List<Field> fields = new LinkedList<Field>();
		for (Field f : packetClass.getDeclaredFields()) {
			// Da Variablen auch private sein können, muss ich den
			// Zugriff explizit erlauben.
			f.setAccessible(true);
			// Wenn die Variable so ein PacketAttribute aufweist,
			// dann zur Liste hinzufügen.
			if (f.getDeclaredAnnotation(PacketAttribute.class) != null) {
				fields.add(f);
			}
		}
					
		// Jede einzelne Variable mit Werten füllen
		for (Field f : fields) {
			if (f.getType() == byte.class) {
				byte value = buf.get();
				f.set(packet, value);
			} else if (f.getType() == char.class) {
				char value = buf.getChar();
				f.set(packet, value);
			} else if (f.getType() == short.class) {
				short value = buf.getShort();
				f.set(packet, value);
			} else if (f.getType() == int.class) {
				int value = buf.getInt();
				f.set(packet, value);
			} else if (f.getType() == long.class) {
				long value = buf.getLong();
				f.set(packet, value);
			} else if (f.getType() == float.class) {
				float value = buf.getFloat();
				f.set(packet, value);
			} else if (f.getType() == double.class) {
				double value = buf.getDouble();
				f.set(packet, value);
			} else if (f.getType() == boolean.class) {
				boolean value = buf.get() == 0x01;
				f.set(packet, value);
			} else if (f.getType() == String.class) {
				// Länge des Strings
				int strlen = buf.getInt();
				
				// String bytes
				byte[] strbytes = new byte[strlen];
				buf.get(strbytes);
				
				// String von UTF-8 dekodieren
				String value = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(strbytes)).toString();
				f.set(packet, value);
			}
		}
		
		return packet;
	}
	
}
