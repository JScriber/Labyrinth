package fr.imie.labyrinth.launcher;

import fr.imie.labyrinth.graph.Coordinates;

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
	
	// Checks if the other one hasn't the same coordinates
	public boolean hasDifferentCoordinates(Coordinates other) {
		if(other.getX() == this.coordinates.getX() &&
			other.getY() == this.coordinates.getY()) {
			return false;
		}else {
			return true;
		}
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
	
	public void move(int width, int height) {
		// Moves the tracer to its new coordinates (the old one belongs to the previous tracer or start point)
		int newX = this.coordinates.getX() + this.moveX;
		int newY = this.coordinates.getY() + this.moveY;
		
		// Makes sure we aren't getting out of the labyrinth
		if(newX > width-2) {
			newX = width-2;
		}
		if(newY > height-2) {
			newY = height-2;
		}
		if(newX < 1) {
			newX = 1;
		}
		if(newY < 1) {
			newY = 1;
		}
		
		// Applying the coordonates
		this.coordinates.setX(newX);
		this.coordinates.setY(newY);
	}
	
	// Access the moveX / moveY from the outside
	public int getMoveX() {
		return this.moveX;
	}
	public int getMoveY() {
		return this.moveY;
	}
	
}
