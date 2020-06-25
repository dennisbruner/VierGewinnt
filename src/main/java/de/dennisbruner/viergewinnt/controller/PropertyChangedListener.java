package de.dennisbruner.viergewinnt.controller;

/**
 * Diese Klasse soll von der View implementiert werden,
 * um �nderungen im Controller sofort mitzubekommen.
 */
public interface PropertyChangedListener
{
	void propertyChanged(String propertyName, Object value);
}
