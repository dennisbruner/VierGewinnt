package de.dennisbruner.viergewinnt.models;

import java.util.Stack;

public class GameFieldSimulator
{
	private GameField field;
	private Stack<Integer> turns;
	
	public GameFieldSimulator(GameField field)
	{
		this.field = field;
		this.turns = new Stack<Integer>();
	}
	
	public GameField getGameField()
	{
		return field;
	}
	
	public int addToColumn(int column, PlayerColor color)
	{
		// Zug tätigen
		int result = field.addToColumn(column, color);
		if (result != 1)
			return result;
		
		// Zug zum stack hinzufügen
		turns.add(column);
		
		return 1;
	}
	
	public void undo()
	{
		if (!turns.empty())
		{
			int column = (Integer)turns.pop();
			field.removeFromColumn(column);
		}
	}
	
	public void reset()
	{
		while (!turns.empty())
		{
			int column = (Integer)turns.pop();
			field.removeFromColumn(column);
		}
	}
}
