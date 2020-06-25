package de.dennisbruner.viergewinnt.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import de.dennisbruner.viergewinnt.controller.GameController;

public class LevelThreeBotPlayer extends Player
{
	private static int MAX_LAYERS = 3;
	private static int STARTING_WEIGHT = 10000000;
	
	public static final String[] BOT_NAMES = new String[] {
			"[BOT] WELTENZERSTÖRER",
			"[BOT] MENSCHENVERNICHTER",
			"[BOT] Der Weihnachtsmann",
			"[BOT] Shrek",
	};

	public LevelThreeBotPlayer(PlayerColor color)
	{
		super(BOT_NAMES[new Random().nextInt(BOT_NAMES.length)], color);
	}

	@Override
	public void doNextMove(GameController game) {
		// Dreiviertel-Sekunde warten, bis der Spielzug ausgeführt wird
		Timer timer = new Timer(750, null);
		
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				
				// Züge vorausberechnen
				int[] weights = new int[game.getGameField().getWidth()];
				for (int i = 0; i < game.getGameField().getWidth(); i++)
				{
					GameFieldSimulator simulator = new GameFieldSimulator(game.getGameField());
					int result = simulateTurn(simulator, getColor(), i, STARTING_WEIGHT, 0);
					weights[i] = result;
					simulator.reset();
				}
				
				// Wenn alle gewichte 0 sind => Zufall
				int zeroWeights = 0;
				System.out.print("Weights: ");
				for (int i = 0; i < weights.length; i++)
				{
					System.out.printf("%d ", weights[i]);
					if (weights[i] == 0)
					{
						zeroWeights++;
					}
				}
				System.out.println();
				
				if (zeroWeights == weights.length)
				{
					while (true)
					{
						// Solange eine zufällige Spalte auswählen, bis der Spielzug erfolgreich war
						int column = new Random().nextInt(game.getGameField().getWidth());
						if (game.turn(column) == 1)
							break;
					}
				}
				else
				{
					// Besten Zug auswählen
					int column = 0;
					for (int i = 0; i < weights.length; i++)
					{
						if (weights[i] > weights[column])
						{
							column = i;
						}
					}
					
					System.out.printf("Column %d: %d\n", column, weights[column]);
					
					// Zug ausführen
					game.turn(column);
				}
			}}
		);
		
		// Start timer
		timer.start();
	}
	
	private int simulateTurn(GameFieldSimulator simulator, PlayerColor color, int column, int weight, int layer)
	{
		if (!(layer < MAX_LAYERS))
			return 0;
		
		// Simulate
		if (simulator.addToColumn(column, color) != 1)
			return 0;
		PlayerColor winningColor = simulator.getGameField().getWinningColor();
		PlayerColor enemyColor = getColor() == PlayerColor.RED ? PlayerColor.YELLOW : PlayerColor.RED;
		// Wenn man selbst gewinnt
		if (winningColor == getColor())
		{
			System.out.printf("Win on layer %d column %d\n", layer, column);
			return weight;
		}
		// Wenn der Gegner im nächsten Zug gewinnen würde
		else if (winningColor == enemyColor)
		{
			return weight;
		}
		// Wenn keiner Gewinnen würde, dann gehe in die nächste Ebene
		else
		{
			PlayerColor oppositeColor = color == PlayerColor.RED ? PlayerColor.YELLOW : PlayerColor.RED;
			
			int cumulatedWeight = 0;
			for (int i = 0; i < simulator.getGameField().getWidth(); i++)
			{
				int resultWeight = simulateTurn(simulator, oppositeColor, i, weight / 100, layer + 1);
				cumulatedWeight += resultWeight;
				simulator.undo();
			}
			
			System.out.printf("Cumulated weight: %d\n", cumulatedWeight);
			
			return cumulatedWeight;
		}
	}
}
