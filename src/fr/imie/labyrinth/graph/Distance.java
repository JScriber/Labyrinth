package fr.imie.labyrinth.graph;

public class Distance {
	private int horizontal;
	private int vertical;
	
	public Distance(Coordinates start, Coordinates end) {
		// Can return negative number
		this.horizontal = -1*(start.getX() - end.getX());
		this.vertical = start.getY() - end.getY();
	}
	

	public int horizontal() {
		return this.horizontal;
	}
	public int vertical() {
		return this.vertical;
	}
}
