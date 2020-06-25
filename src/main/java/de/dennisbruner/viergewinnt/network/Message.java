package de.dennisbruner.viergewinnt.network;

import java.net.SocketAddress;

import de.dennisbruner.viergewinnt.network.packets.AbstractPacket;

public class Message {

	public SocketAddress address;
	
	public AbstractPacket packet;

}
