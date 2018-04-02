package fr.imie.labyrinth.launcher;

public class Cell {
	private boolean isVisited;

	private int x, y;
	private boolean start, goal, quickestPath;
	private Wall left, right, top, bottom;

	public Cell(int x, int y){
		// Sets the position
		this.x = x;
		this.y = y;

		this.isVisited = false;

		// Adds the walls
		this.left = new Wall();
		this.top = new Wall();
		this.right = new Wall();
		this.bottom = new Wall();

		// Status of the cell
		this.start = false;
		this.goal = false;

		// Says it is a part of the quickest path
		this.quickestPath = false;
	}

	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}

	public boolean isVisited(){
		return this.isVisited;
	}

	public void setAsVisited(){
		this.isVisited = true;
	}

	// Getters for walls
	public Wall getLeft(){
		return this.left;
	}
	public Wall getRight(){
		return this.right;
	}
	public Wall getTop(){
		return this.top;
	}
	public Wall getBottom(){
		return this.bottom;
	}

	// Setters for start and goal status
	public void defineAsStart(){
		this.start = true;
	}
	public void defineAsGoal(){
		this.goal = true;
	}
	public void setQuickPath(boolean status){
		this.quickestPath = status;
	}

	// Getters for status
	public boolean isStartPoint(){
		return this.start;
	}
	public boolean isGoalPoint(){
		return this.goal;
	}
	public boolean isMemberOfQuickPath(){ return this.quickestPath; }
}
