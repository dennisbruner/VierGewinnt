package de.dennisbruner.viergewinnt.controller;

import java.util.ArrayList;

/**
 * Jeder Controller soll von dieser Klasse erben.
 * Diese Klasse bringt "Boilerplate"-Code mit um
 * PropertyChangedListeners hinzuzufügen und
 * Änderungen im Code dem View mitzuteilen.
 */
public abstract class AbstractController
{
	private ArrayList<PropertyChangedListener> listeners;
	
	public AbstractController()
	{
		listeners = new ArrayList<PropertyChangedListener>();
	}
	
	public void addPropertyChangedListener(PropertyChangedListener listener)
	{
		listeners.add(listener);
	}
	
	public void removePropertyChangedListener(PropertyChangedListener listener)
	{
		listeners.remove(listener);
	}
	
	/**
	 * Diese Funktion soll vom Controller aufgerufen werden,
	 * wenn sich eine Variable ändert.
	 * Zum Beispiel: Der aktuelle Spieler ändert sich -> Name anzeigen
	 * 
	 * @param propertyName Frei wählbarer Variablenname
	 * @param value        Optionaler Wert der mitgegeben werden kann
	 */
	protected void onPropertyChanged(String propertyName, Object value)
	{
		for (PropertyChangedListener l : listeners)
		{
			l.propertyChanged(propertyName, value);
		}
	}
}
