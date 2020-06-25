package de.dennisbruner.viergewinnt.models;

import de.dennisbruner.viergewinnt.controller.GameController;

public abstract class Player
{
	private String name;
	private PlayerColor color;
	
	public Player(String name, PlayerColor color)
	{
		this.name = name;
		this.color = color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public PlayerColor getColor()
	{
		return color;
	}
	
	public abstract void doNextMove(GameController game);
}
