package de.dennisbruner.viergewinnt.controller;

import java.util.ArrayList;

/**
 * Jeder Controller soll von dieser Klasse erben.
 * Diese Klasse bringt "Boilerplate"-Code mit um
 * PropertyChangedListeners hinzuzuf�gen und
 * �nderungen im Code dem View mitzuteilen.
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
	 * wenn sich eine Variable �ndert.
	 * Zum Beispiel: Der aktuelle Spieler �ndert sich -> Name anzeigen
	 * 
	 * @param propertyName Frei w�hlbarer Variablenname
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
