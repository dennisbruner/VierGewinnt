package de.dennisbruner.viergewinnt.models;

import de.dennisbruner.viergewinnt.controller.GameController;

public class HumanPlayer extends Player
{
	public HumanPlayer(String name, PlayerColor color) {
		super(name, color);
	}

	@Override
	public void doNextMove(GameController game) {
		// Nichts tun, da der nächste Spielzug per Buttonklick ausgeführt wird
	}
}
