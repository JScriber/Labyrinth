package fr.imie.labyrinth.graph;

public class Distance {
	private int horizontal;
	private int vertical;
	
	public Distance(Coordinates start, Coordinates end) {
		// Need to handle negative values (i have to say how the points are)
		this.horizontal = Math.abs((start.getX() - end.getX()));
		this.vertical = Math.abs(start.getY() - end.getY());
	}
	

	public int horizontal() {
		return this.horizontal;
	}
	public int vertical() {
		return this.vertical;
	}
}
