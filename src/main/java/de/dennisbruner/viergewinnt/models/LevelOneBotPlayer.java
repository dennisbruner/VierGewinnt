package de.dennisbruner.viergewinnt.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import de.dennisbruner.viergewinnt.controller.GameController;

public class LevelOneBotPlayer extends Player
{
	public static final String[] BOT_NAMES = new String[] {
			"[BOT] Schnuffelpuff",
			"[BOT] Hasipups"
	};
	
	public LevelOneBotPlayer(PlayerColor color) {		
		super(BOT_NAMES[new Random().nextInt(BOT_NAMES.length)], color);
	}

	@Override
	public void doNextMove(GameController game) {
		// Dreiviertel-Sekunde warten, bis der Spielzug ausgeführt wird
		Timer timer = new Timer(750, null);
		
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Timer wieder beenden (soll nur einmal ausgeführt werden)
				timer.stop();
				
				// Eigentlicher Spielzug
				while (true)
				{
					// Solange eine zufällige Spalte auswählen, bis der Spielzug erfolgreich war
					int column = new Random().nextInt(game.getGameField().getWidth());
					if (game.turn(column) == 1)
						break;
				}
			}}
		);
		
		// Timer starten
		timer.start();
	}
}
