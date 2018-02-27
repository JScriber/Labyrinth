package fr.imie.labyrinth.graph;

public class Distance {
	private int horizontal;
	private int vertical;
	private boolean endFirstX;
	private boolean endFirstY;
	
	public Distance(Coordinates start, Coordinates end) {
		// Detects if the end is before the start reading from up to bottom, left to right.
		this.horizontal = (start.getX() - end.getX());
		if(this.horizontal < 0) {
			this.endFirstX = true;
		}
		this.vertical = (start.getY() - end.getY());
		if(this.vertical < 0) {
			this.endFirstY = true;
		}
	}

	public boolean endIsBeforeStartX() {
		return this.endFirstX;
	}
	public boolean endIsBeforeStartY() {
		return this.endFirstY;
	}

	public int horizontal() {
		return this.horizontal*-1;
	}
	public int vertical() {
		return this.vertical;
	}

	public int absHorizontal() {
		return Math.abs(this.horizontal);
	}
	public int absVertical() {
		return Math.abs(this.vertical);
	}
}
