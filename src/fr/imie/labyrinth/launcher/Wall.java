package fr.imie.labyrinth.launcher;


public class Wall {
	private boolean isBroken;

	public Wall(){
		this.isBroken = false;
	}

	public boolean isBroken(){
		return this.isBroken;
	}
	public void breaks(){
		this.isBroken = true;
	}
}
