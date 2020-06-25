package de.dennisbruner.viergewinnt.controller;

import de.dennisbruner.viergewinnt.models.GameField;
import de.dennisbruner.viergewinnt.models.Player;

public class LocalMultiplayerGameController extends GameController {
	
	public LocalMultiplayerGameController(GameField gameField, Player player1, Player player2) {
		super(gameField, player1, player2);
	}

	@Override
	public void OnNextMove(Player nextPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGameDone(Player winningPlayer) {
		// TODO Auto-generated method stub
		
	}
	
}
