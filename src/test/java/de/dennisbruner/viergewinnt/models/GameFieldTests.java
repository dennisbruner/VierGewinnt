package de.dennisbruner.viergewinnt.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameFieldTests
{
	GameField field;
	
	@BeforeEach
	void setUp() throws Exception
	{
		field = new GameField(7, 7);
	}

	@AfterEach
	void tearDown() throws Exception
	{
		field = null;
	}

	@Test
	void Should_WinHorizontally()
	{
		field.addToColumn(0, PlayerColor.RED);
		field.addToColumn(1, PlayerColor.RED);
		field.addToColumn(2, PlayerColor.RED);
		field.addToColumn(3, PlayerColor.RED);
		
		assertTrue(field.hasWonHorizontally(PlayerColor.RED), "Spieler sollte horizontal gewinnen");
	}
	
	@Test
	void ShouldNot_WinHorizontally()
	{
		field.addToColumn(0, PlayerColor.RED);
		field.addToColumn(1, PlayerColor.RED);
		field.addToColumn(2, PlayerColor.RED);
		field.addToColumn(4, PlayerColor.RED);
		
		assertFalse(field.hasWonHorizontally(PlayerColor.RED), "Spieler sollte nicht horizontal gewinnen");
	}
	
	@Test
	void Should_WinVertically()
	{
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		
		assertTrue(field.hasWonVertically(PlayerColor.YELLOW), "Spieler sollte vertikal gewinnen");
	}
	
	@Test
	void ShouldNot_WinVertically()
	{
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.RED);
		field.addToColumn(0, PlayerColor.YELLOW);
		
		assertFalse(field.hasWonVertically(PlayerColor.YELLOW), "Spieler sollte nicht vertikal gewinnen");
	}

	@Test
	void Should_WinDiagonallyDriftLeft()
	{
		
		field.addToColumn(0, PlayerColor.RED);
		
		field.addToColumn(1, PlayerColor.YELLOW);
		field.addToColumn(1, PlayerColor.RED);
		
		field.addToColumn(2, PlayerColor.YELLOW);
		field.addToColumn(2, PlayerColor.YELLOW);
		field.addToColumn(2, PlayerColor.RED);
		
		field.addToColumn(3, PlayerColor.YELLOW);
		field.addToColumn(3, PlayerColor.YELLOW);
		field.addToColumn(3, PlayerColor.YELLOW);
		field.addToColumn(3, PlayerColor.RED);
		
		assertTrue(field.hasWonDiagonally(PlayerColor.RED, -1), "Spieler sollte nach links diagonal gewinnen");
	}
	
	@Test
	void Should_WinDiagonallyDriftRight()
	{
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.YELLOW);
		field.addToColumn(0, PlayerColor.RED);
		
		field.addToColumn(1, PlayerColor.YELLOW);
		field.addToColumn(1, PlayerColor.YELLOW);
		field.addToColumn(1, PlayerColor.RED);
		
		field.addToColumn(2, PlayerColor.YELLOW);
		field.addToColumn(2, PlayerColor.RED);
		
		field.addToColumn(3, PlayerColor.RED);
		
		assertTrue(field.hasWonDiagonally(PlayerColor.RED, 1), "Spieler sollte nach rechts diagonal gewinnen");
	}
}
