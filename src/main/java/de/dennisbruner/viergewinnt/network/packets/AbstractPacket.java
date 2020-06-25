package de.dennisbruner.viergewinnt.network.packets;

public abstract class AbstractPacket {

	private final byte packetID;
	
	public AbstractPacket(byte packetID) {
		this.packetID = packetID;
	}
	
	public byte getPacketID() {
		return packetID;
	}
	
}
