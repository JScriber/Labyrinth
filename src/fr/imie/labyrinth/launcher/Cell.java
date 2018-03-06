package fr.imie.labyrinth.launcher;

import fr.imie.labyrinth.graph.Coordinates;

public class Cell {
	private Coordinates coordinates;

	private boolean start;
	private boolean end;
	private Wall leftWall, rightWall, topWall, bottomWall;
	private boolean used;
	
	public Cell() {
		
	}
	
	public Cell(Coordinates coordinates, Wall leftWall, Wall rightWall, Wall topWall, Wall bottomWall) {
		this.coordinates = coordinates;
		// Gives the walls
		this.leftWall = leftWall;
		this.rightWall = rightWall;
		this.topWall = topWall;
		this.bottomWall = bottomWall;
		
		this.start = false;
		this.end = false;
		
		// Set the activation of the cell to false
		this.used = false;
	}
	
	// Methods to wipe out walls
	public void breakBottomWall() {
		this.bottomWall.breaks();
	}
	public void breakTopWall() {
		this.topWall.breaks();
	}
	public void breakLeftWall() {
		this.leftWall.breaks();
	}
	public void breakRightWall() {
		this.rightWall.breaks();
	}
	
	// Getters
	public Wall getLeftWall() {
		return leftWall;
	}
	public Wall getRightWall() {
		return rightWall;
	}
	public Wall getTopWall() {
		return topWall;
	}
	public Wall getBottomWall() {
		return bottomWall;
	}
	
	// Checks if it has protected walls
	public boolean hasProtectedWalls() {
		if(leftWall.isProtected() || rightWall.isProtected() || topWall.isProtected() || bottomWall.isProtected()) {
			return true;
		}else {
			return false;
		}
	}

	// Returns if activated
	public boolean isUsed() {
		return this.used;
	}
	// Sets the cell as used
	public void setAsUsed() {
		this.used = true;
	}
	
	// Returns coordinates
	public int getX() {
		return this.coordinates.getX();
	}
	public int getY() {
		return this.coordinates.getY();
	}	
	
	// Start cell
	public void defineAsStart() {
		this.start = true;
	}
	public boolean isTheStart() {
		return this.start;
	}
	
	// End cell
	public void defineAsEnd() {
		this.end = true;
	}
	public boolean isTheEnd() {
		return this.end;
	}
	
	
	
}
