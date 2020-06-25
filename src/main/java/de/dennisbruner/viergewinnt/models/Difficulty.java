package de.dennisbruner.viergewinnt.models;

public enum Difficulty {
	EASY("Einfach (Level 1)"),
	NORMAL("Normal (Level 2)"),
	HARD("Schwer (Level 3)");
	
	private String text;
	
	Difficulty(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString()
	{
		return getText();
	}
	
	public static Difficulty fromText(String text) {
		switch (text) {
		case "Einfach (Level 1)":
			return EASY;
		case "Normal (Level 2)":
			return NORMAL;
		case "Schwer (Level 3)":
			return HARD;
		}
		return NORMAL;
	}
}
