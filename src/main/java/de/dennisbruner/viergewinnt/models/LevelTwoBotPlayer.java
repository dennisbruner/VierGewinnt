package de.dennisbruner.viergewinnt.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import de.dennisbruner.viergewinnt.controller.GameController;

public class LevelTwoBotPlayer extends Player
{
	public static final String[] BOT_NAMES = new String[] {
			"[BOT] Richard",
			"[BOT] Heinrich",
			"[BOT] Dieter",
			"[BOT] Ursula",
			"[BOT] Angela",
			"[BOT] Selma",
	};

	public LevelTwoBotPlayer(PlayerColor color)
	{
		super(BOT_NAMES[new Random().nextInt(BOT_NAMES.length)], color);
	}

	@Override
	public void doNextMove(GameController game)
	{
		// Dreiviertel-Sekunde warten, bis der Spielzug ausgeführt wird
		Timer timer = new Timer(750, null);
		
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				
				// Simulator erstellen
				GameFieldSimulator simulator = new GameFieldSimulator(game.getGameField());
				
				// Spielerfarben
				PlayerColor myColor = getColor();
				PlayerColor otherColor = myColor == PlayerColor.RED ? PlayerColor.YELLOW : PlayerColor.RED;
				
				// Züge testen
				for (int i = 0; i < game.getGameField().getWidth(); i++)
				{
					// Eigene Farbe testen (Gewinnen)
					simulator.addToColumn(i, myColor);
					PlayerColor winningColor = game.getGameField().getWinningColor();
					simulator.reset();
					if (winningColor == myColor)
					{
						game.turn(i);
						return;
					}
					
					// Andere Farbe testen (Gewinn verhindern)
					simulator.addToColumn(i, otherColor);
					winningColor = game.getGameField().getWinningColor();
					simulator.reset();
					if (winningColor == otherColor)
					{
						game.turn(i);
						return;
					}
				}
				
				// Zufällige Spalte wählen
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
