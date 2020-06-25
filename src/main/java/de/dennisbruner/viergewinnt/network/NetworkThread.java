package de.dennisbruner.viergewinnt.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.dennisbruner.viergewinnt.network.packets.AbstractPacket;
import de.dennisbruner.viergewinnt.network.packets.serializer.PacketSerializer;

public class NetworkThread implements Runnable {

	public static final int SOCKET_PORT = 4824;
	
	private PacketSerializer serializer;
	private DatagramSocket socket;
	private AbstractQueue<Message> queue;
	
	public NetworkThread() {
		serializer = new PacketSerializer();
		queue = new ConcurrentLinkedQueue<Message>();
	}
	
	@Override
	public void run() {
		// Socket erstellen
		try {
			socket = new DatagramSocket(SOCKET_PORT, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		// Unendliche Schleife
		while (true) {
			// Auf ein Datagram warten
			byte[] received = new byte[PacketSerializer.HEADER_LENGTH + PacketSerializer.MAX_DATA_LENGTH];
			DatagramPacket p = new DatagramPacket(received, received.length);
			try {
				socket.receive(p);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			// Datagram in ein AbstractPacket umwandeln
			if (p != null) {
				try {
					AbstractPacket ap = serializer.deserialize(p.getData());
					
					Message m = new Message();
					m.address = p.getSocketAddress();
					m.packet = ap;
					
					// In die Queue pushen
					queue.add(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void broadcast(AbstractPacket p) {
		try {
			byte[] data = serializer.serialize(p);
			DatagramPacket dp = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.178.255", SOCKET_PORT));
			socket.send(dp);			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send(AbstractPacket p, SocketAddress addr) {
		try {
			byte[] data = serializer.serialize(p);
			DatagramPacket dp = new DatagramPacket(data, data.length, addr);
			socket.send(dp);			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public AbstractQueue<Message> getQueue() {
		return queue;
	}

}
