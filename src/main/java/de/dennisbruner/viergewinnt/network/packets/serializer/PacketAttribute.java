package de.dennisbruner.viergewinnt.network.packets.serializer;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface PacketAttribute {
	// Dieses Attribut soll nur Variablen markieren,
	// die in Bytes umgewandelt werden sollen (oder auch umgekehrt)
}
