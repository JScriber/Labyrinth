package fr.imie.labyrinth.launcher;

public class Labyrinth {
	private int width;
	private int height;
	
	private Cell[][] cells;
	
	public Labyrinth(int width, int height) {
		this.width = width;
		this.height = height;
		// Creating an empty labyrinth
		this.cells = new Cell[height][width];
		
		// Filling it with walls
		this.fillWithWalls();
	}
	
	// Modify the cells
	private void setCell(int x, int y, Cell cell) {
		this.cells[x][y] = cell;
	}
	private Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	// Fills with wall only
	private void fillWithWalls() {
		int i, j;
		Wall wall = new Wall();
		
		for(i = 0; i < this.height; i++) {
			for(j = 0; j < this.width; j++) {
				this.setCell(i, j, wall);
			}
		}
	}
	
	@Override
	public String toString() {
		String labyrinth = "";
		int i, j;
		// Returns the layrinth
		for(i = 0; i < this.height; i++) {
			for(j = 0; j < this.width; j++) {
				Cell targetedCell = this.getCell(i, j);
				labyrinth = labyrinth.concat(targetedCell.getGlyph());
			}
			labyrinth = labyrinth.concat("\n");
		}
		
		return labyrinth;
	}
}
