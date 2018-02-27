package fr.imie.labyrinth.launcher;

public class Tracer extends Cell {
	// Only cell where the coordinates really matter
	private Coordinates coordinates;
	
	public Tracer(Coordinates coordinates) {
		super("_");
		this.coordinates = coordinates;
	}

	public Coordinates getCoordinates() {
		return this.coordinates;
	}
	
	// Shortcuts (for cleaner code)
	public int getX() {
		return this.coordinates.getX();
	}
	public int getY() {
		return this.coordinates.getY();
	}
}
