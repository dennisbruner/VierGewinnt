package de.dennisbruner.viergewinnt.controller;

import de.dennisbruner.viergewinnt.models.GameField;
import de.dennisbruner.viergewinnt.models.Player;

public class BotGameController extends GameController 
{

	public BotGameController(GameField gameField, Player player1, Player player2) {
		super(gameField, player1, player2);
	}

	@Override
	public void OnNextMove(Player nextPlayer) {
		nextPlayer.doNextMove(this);
	}

	@Override
	public void OnGameDone(Player winningPlayer) {
		// TODO Auto-generated method stub
		
	}

}
