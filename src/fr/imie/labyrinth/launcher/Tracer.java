package fr.imie.labyrinth.launcher;

public class Tracer extends Cell {
	// Only cell where the coordinates really matter
	private Coordinates coordinates;
	private int moveX = 0;
	private int moveY = 0;
	
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
	
	// Change the direction
	public void headFor(Direction choice) {
		switch(choice) {
			case LEFT :
				this.moveX = -1;
				this.moveY = 0;
			break;
			case RIGHT : 
				this.moveX = 1;
				this.moveY = 0;
			break;
			case UP :
				this.moveX = 0;
				this.moveY = 1;
			break;
			case DOWN : 
				this.moveX = 0;
				this.moveY = -1;
			break;
		}
	}
	
	// Access the moveX / moveY from the outside
	public int getMoveX() {
		return this.moveX;
	}
	public int getMoveY() {
		return this.moveY;
	}
	
}
