package fr.imie.labyrinth.launcher;

public class Cell {
	private String symbol = "";
	
	public Cell(String glyph) {
		this.symbol = glyph;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
}
