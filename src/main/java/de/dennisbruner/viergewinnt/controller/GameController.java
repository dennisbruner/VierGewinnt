package de.dennisbruner.viergewinnt.controller;

import java.util.Random;

import de.dennisbruner.viergewinnt.models.GameField;
import de.dennisbruner.viergewinnt.models.GameState;
import de.dennisbruner.viergewinnt.models.Player;
import de.dennisbruner.viergewinnt.models.PlayerColor;

public abstract class GameController extends AbstractController
{
	private GameField gameField;
	private GameState gameState;
	
	private Player[] players;
	private int currentPlayer = 0;
	
	public GameController(GameField gameField, Player player1, Player player2)
	{
		this.gameField = gameField;
		this.players = new Player[] { player1, player2 };
		
		// Random current player
		currentPlayer = new Random().nextInt(players.length);
		onPropertyChanged("currentPlayer", getCurrentPlayer());
		
		OnNextMove(getCurrentPlayer());
	}
	
	/**
	 * Gibt das Spielfeld zur�ck
	 * 
	 * @return Spielfeld
	 */
	public GameField getGameField()
	{
		return gameField;
	}
	
	/**
	 * Gibt den aktuellen Spielstatus zur�ck
	 *
	 * @return Aktueller Spielstatus
	 */
	public GameState getGameState()
	{
		return gameState;
	}
	
	/**
	 * Gibt den Spieler zur�ck, der aktuell im Zug ist
	 * 
	 * @return Aktueller Spieler
	 */
	public Player getCurrentPlayer()
	{
		return players[currentPlayer];
	}
	
	/**
	 * Gibt eine Liste von teilnehmenden Spielern zur�ck
	 * 
	 * @return Liste von Spielern
	 */
	public Player[] getPlayers()
	{
		return players;
	}
	
	/**
	 * Diese Methode wird von einer Oberfl�che oder von einer KI aufgerufen,
	 * wenn der n�chste Spielzug f�r den aktuellen Spieler gemacht werden soll.
	 * 
	 * @param column Ausgew�hlte Spalte
	 * @return       Gibt -1 zur�ck, wenn der Spielzug fehlerhaft ist.
	 *               Gibt 0 zur�ck, wenn die Spalte voll ist.
	 *               Gibt 1 zur�ck, wenn der Spielzug erfolgreich war.
	 */
	public int turn(int column)
	{
		int result = getGameField().addToColumn(column, getCurrentPlayer().getColor());
		if (result != 1)
			return result;
		onPropertyChanged("gameField", getGameField());
		
		// Pr�fen ob jemand gewonnen hat
		PlayerColor winningColor = getGameField().getWinningColor();
		if (winningColor != null)
		{
			// Setze game state
			gameState = winningColor == PlayerColor.EMPTY
					? GameState.DRAW
					: GameState.WON;
			onPropertyChanged("gameState", gameState);
			
			// Spieler mit dieser Farbe rausfinden
			Player winningPlayer = null;
			for (Player p : players)
			{
				if (p.getColor() == winningColor)
				{
					winningPlayer = p;
				}
			}
			
			// Eventfunktion aufrufen
			OnGameDone(winningPlayer);
			
			return 1;
		}
		
		// N�chster Spieler
		currentPlayer = (currentPlayer + 1) % players.length;
		onPropertyChanged("currentPlayer", getCurrentPlayer());
		
		// N�chsten Zug ank�ndigen
		OnNextMove(getCurrentPlayer());
		
		return 1;
	}
	
	/**
	 * Gibt den Spieler zur�ck der gewonnen hat
	 * 
	 * @return Spieler der gewonnen hat (null falls keiner)
	 */
	public Player getWinningPlayer()
	{
		PlayerColor winningColor = getGameField().getWinningColor();
		if (winningColor == null || winningColor == PlayerColor.EMPTY)
			return null;
		
		for (int i = 0; i < players.length; i++)
		{
			if (players[i].getColor() == winningColor)
			{
				return players[i];
			}
		}
		
		return null;
	}
	
	/**
	 * Diese Funktion wird von einer ableitenden Klasse implementiert.
	 * 
	 * Sie wird aufgerufen, wenn ein Spieler einen Zug gemacht hat und der
	 * n�chste Spieler jetzt seinen Zug machen soll.
	 * 
	 * @param nextPlayer Spieler, der jetzt am Zug ist
	 */
	public abstract void OnNextMove(Player nextPlayer);
	
	/**
	 * Diese Funktion wird von einer ableitenden Klasse implementiert.
	 * 
	 * Sie wird aufgerufen, wenn nach einem Zug ein Spieler gewonnen hat oder
	 * wenn das Spiel unentschieden endet.
	 * 
	 * @param winningPlayer Spieler, der gewonnen hat (null falls unentschieden)
	 */
	public abstract void OnGameDone(Player winningPlayer);
}
