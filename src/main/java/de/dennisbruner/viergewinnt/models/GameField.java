package de.dennisbruner.viergewinnt.models;

public class GameField
{
	private int width;
	private int height;
	
	private PlayerColor[][] field;
	
	public GameField(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		// Init field
		field = new PlayerColor[getWidth()][getHeight()];
		clear();
	}
	
	/**
	 * Gibt die Anzahl der Spalten zurück
	 * 
	 * @return Anzahl der Spalten
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Gibt die Anzahl der Zeilen zurück
	 * 
	 * @return Anzahl der Zeilen
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Gibt das komplette Spielfeld als rohes zweidimensionales Array zurück
	 * 
	 * @return Zweidimensionales Array mit Spielerfarben
	 */
	public PlayerColor[][] getField()
	{
		return field;
	}
	
	/**
	 * Gibt die Spielerfarbe auf der gewünschten Position zurück
	 * 
	 * @param x Spalte
	 * @param y Zeile
	 * @return  Spielerfarbe oder EMPTY falls leer
	 */
	public PlayerColor getColorAt(int x, int y)
	{
		return field[x][y];
	}
	
	/**
	 * Leert das Feld von Spielsteinen
	 */
	public void clear()
	{
		for (int i = 0; i < getWidth(); i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				field[i][j] = PlayerColor.EMPTY;
			}
		}
	}
	
	/**
	 * Fügt einen Spielstein in die Spalte ein
	 * 
	 * @param column Spalte
	 * @param color  Spielerfarbe
	 * @return       Gibt einen negativen Wert zurück wenn die Parameter falsch sind.
	 *               1 wenn der Spielzug erfolgreich war, 0 wenn Spalte voll.
	 */
	public int addToColumn(int column, PlayerColor color)
	{
		if (column >= getWidth() || column < 0)
			return -1;
		if (color == PlayerColor.EMPTY)
			return -1;

		for (int i = getHeight() - 1; i >= 0; i--)
		{
			if (field[column][i] == PlayerColor.EMPTY)
			{
				field[column][i] = color;
				return 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * Entfernt den obersten Spielstein von der Spalte
	 * 
	 * @param column Ausgewählte Spalte
	 * @return       true wenn erfolgreich, false wenn nicht
	 */
	public boolean removeFromColumn(int column)
	{
		for (int i = 0; i < getHeight(); i++)
		{
			if (field[column][i] != PlayerColor.EMPTY)
			{
				field[column][i] = PlayerColor.EMPTY;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Berechnet ob ein Spieler gewonnen hat oder nicht
	 * 
	 * @return Spielerfarbe die gewonnen hat
	 *         EMPTY falls untenschieden
	 *         null wenn das Spiel noch nicht vorbei ist
	 */
	public PlayerColor getWinningColor()
	{
		// Roter Spieler hat gewonnen
		if (hasColorWon(PlayerColor.RED))
		{
			return PlayerColor.RED;
		}
		// Gelber Spieler hat gewonnen
		else if (hasColorWon(PlayerColor.YELLOW))
		{
			return PlayerColor.YELLOW;
		}
		// Alle Spalten sind voll und keiner hat gewonnen (unentschieden)
		else
		{
			int fullColumns = 0;
			for (int i = 0; i < getWidth(); i++)
			{
				if (field[i][0] != PlayerColor.EMPTY)
				{
					fullColumns++;
				}
			}
			
			if (fullColumns == getWidth())
			{
				return PlayerColor.EMPTY;
			}
		}
		
		// Spiel ist noch nicht vorbei
		return null;
	}
	
	public boolean hasColorWon(PlayerColor color)
	{
		return hasWonVertically(color)
			|| hasWonHorizontally(color)
			|| hasWonDiagonally(color, 1)   // Drift nach rechts
			|| hasWonDiagonally(color, -1); // Drift nach links
	}
	
	boolean hasWonVertically(PlayerColor color)
	{
		for (int i = 0; i < getWidth(); i++)
		{
			int count = 0;
			for (int j = 0; j < getHeight(); j++)
			{
				if (field[i][j] == color)
				{
					count++;
				}
				else
				{
					count = 0;
				}
				
				if (count >= 4)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	boolean hasWonHorizontally(PlayerColor color)
	{
		for (int i = 0; i < getHeight(); i++)
		{
			int count = 0;
			for (int j = 0; j < getWidth(); j++)
			{
				if (field[j][i] == color)
				{
					count++;
				}
				else
				{
					count = 0;
				}
				
				if (count >= 4)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	boolean hasWonDiagonally(PlayerColor color, int drift)
	{
		// Top side
		for (int cols = 0; cols < getWidth(); cols++)
		{
			int count = 0;
			
			int i = cols;
			int j = 0;
			do
			{
				if (field[i][j] == color)
					count++;
				else
					count = 0;
				
				if (count >= 4)
					return true;
				
				i += drift;
				j++;
			}
			while ((i >= 0 && i < getWidth()) && (j >= 0 && j < getHeight()));
		}
		
		// Bottom side
		for (int cols = 0; cols < getWidth(); cols++)
		{
			int count = 0;
			
			int i = cols;
			int j = getHeight() - 1;
			do
			{
				if (field[i][j] == color)
					count++;
				else
					count = 0;
				
				if (count >= 4)
					return true;
				
				i -= drift;
				j--;
			}
			while ((i >= 0 && i < getWidth()) && (j >= 0 && j < getHeight()));
		}
		
		return false;
	}
}
