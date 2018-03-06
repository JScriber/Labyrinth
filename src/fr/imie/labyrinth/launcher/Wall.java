package fr.imie.labyrinth.launcher;

public class Wall {
	private boolean isProtected;
	private boolean isBroken;

	public Wall(boolean isProtected) {
		// Sets the protective lock
		this.isBroken = false;
		this.isProtected = isProtected;
	}
	
	
	// Getters
	public boolean isProtected() {
		return isProtected;
	}
	public boolean isBroken() {
		return isBroken;
	}
	
	// Setters
	public void breaks() {
		this.isBroken = true;
	}
}
