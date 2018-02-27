package fr.imie.labyrinth.launcher;

public class Labyrinth {
	private int width;
	private int height;
	private Coordinates start;
	private Coordinates goal;
	
	private Cell[][] cells;
	
	
	public Labyrinth(int width, int height) {
		this.width = width;
		this.height = height;
		// Creating an empty labyrinth
		this.cells = new Cell[width][height];
		
		// Filling it with walls
		this.fillWithWalls();
		
		// Add random start and goal
		this.addStartPoint();
		this.addGoalPoint();
	}
	
	
	// Return random coordinates within the labyrinth
	private Coordinates randomPoint() {
		int x = Coordinates.randomBetween(1, this.width-1);
		int y = Coordinates.randomBetween(1, this.height-1);
		
		return new Coordinates(x, y);
	}
	
	// Adds the start point of the game
	private void addStartPoint() {
		Coordinates randomPlace = randomPoint();
		// Adds it to the instance
		this.start = randomPlace;
		
		// Adds it to the table
		this.setCell(randomPlace, new Start());
	}
	
	// Adds the goal point of the game
	private void addGoalPoint() {
		Coordinates randomPlace;
		// Makes sure we aren't overwriting the Start point.
		// (it wouldn't make sense...)
		do {
			randomPlace = randomPoint();
		} while(this.getCell(randomPlace) instanceof Start);
		
		// Adds it to the instance
		this.goal = randomPlace;
		
		// Adds it to the table
		this.setCell(randomPlace, new Goal());
	}
	
	
	// Modify the cells
	private void setCell(Coordinates coordinates, Cell cell) {
		this.cells[coordinates.getX()][coordinates.getY()] = cell;
	}
	private Cell getCell(Coordinates coordinates) {
		return cells[coordinates.getX()][coordinates.getY()];
	}
	
	
	// Fills with wall only
	private void fillWithWalls() {
		int i, j;
		Wall wall = new Wall();
		
		for(i = 0; i < this.width; i++) {
			for(j = 0; j < this.height; j++) {
				this.setCell(new Coordinates(i, j), wall);
			}
		}
	}
	
	@Override
	public String toString() {
		String labyrinth = "";
		int i, j;
		// Returns the layrinth
		for(i = 0; i < this.width; i++) {
			for(j = 0; j < this.height; j++) {
				Cell targetedCell = this.getCell(new Coordinates(i, j));
				labyrinth = labyrinth.concat(targetedCell.getSymbol());
			}
			labyrinth = labyrinth.concat("\n");
		}
		
		return labyrinth;
	}
}