package fr.imie.labyrinth.launcher;

import java.util.ArrayList;
import java.util.Random;

import fr.imie.labyrinth.graph.Coordinates;
import fr.imie.labyrinth.graph.Distance;

public class Labyrinth {
	private int width;
	private int height;

	private ArrayList<Cell> arianeString = null;
	
	private Cell[][] cells;
	
	
	public Labyrinth(int width, int height) {
		this.arianeString = new ArrayList<Cell>();
		this.width = width;
		this.height = height;
		// Creating an empty labyrinth
		this.cells = new Cell[width][height];
		
		// Fills with walls
		this.defaultFilling();
		
		// Traces the labyrinth
		this.traces();
		
		
		
		System.out.println(neighboorsFinder(this.getCell(new Coordinates(0, 0))));
	}
	
	
	// Return random coordinates within the labyrinth
	private Coordinates randomPoint() {
		int x = Coordinates.randomBetween(1, this.width-1);
		int y = Coordinates.randomBetween(1, this.height-1);
		
		return new Coordinates(x, y);
	}
	
	
	// Modify the cells
	private void setCell(Cell cell) {
		this.cells[cell.getX()][cell.getY()] = cell;
	}
	private Cell getCell(Coordinates coordinates) {
		return cells[coordinates.getX()][coordinates.getY()];
	}
	private Cell getCell(int x, int y) {
		return cells[x][y];
	}
	
	
	// Fills with wall only (builds the first map)
	private void defaultFilling() {
		int i, j;
		boolean left, right, up, down;
		
		for(i = 0; i < this.width; i++) {
			for(j = 0; j < this.height; j++) {
				left = false;
				right = false;
				up = false;
				down = false;
				
				// Makes sure the walls are well protected
				if(i == 0) {
					left = true;
				}else if(i == this.width) {
					right = true;
				}
				
				if(j == 0) {
					up = true;
				}else if(j == this.height) {
					down = true;
				}
				
				// Builds and adds a new cell
				this.setCell(new Cell(
						new Coordinates(i, j),
						new Wall(left),
						new Wall(up),
						new Wall(right),
						new Wall(down)
				));
			}
		}
	}
	
	// Checks and lists the neighboors
	private ArrayList<Cell> neighboorsFinder(Cell cell) {
		int x = cell.getX();
		int y = cell.getY();
		ArrayList<Cell>options = new ArrayList<Cell>();
		
		Cell nextCell = null;
		// Checks if the current cell isn't in a corner
		if(!cell.getLeftWall().isProtected()) {
			// Check if the upper cell is active
			nextCell = this.getCell(x-1, y);
			if(!nextCell.isUsed()) {
				options.add(nextCell);
			}
		}

		if(!cell.getTopWall().isProtected()) {
		}
		

		if(!cell.getRightWall().isProtected()) {
		}
		
		
		if(!cell.getBottomWall().isProtected()) {
		}
		
		
		System.out.println(options.size());
		return options;
	}
	
	
	// Breaks the walls between two cells
	private void wallBreaker(Cell origin, Cell ultimate) {
		int xDiference = origin.getX() - ultimate.getX();
		int yDiference = origin.getY() - ultimate.getY();
		
		// Breaks horizontally
		switch(xDiference){
			case -1:
				// Ultimate is at the right
				origin.breakRightWall();
				ultimate.breakLeftWall();
			break;
			case 1:
				// Ultimate is at the left
				origin.breakLeftWall();
				ultimate.breakRightWall();
			break;
		}
		// Breaks vertically
		switch(yDiference){
			case 1:
				// Ultimate is above
				origin.breakTopWall();
				ultimate.breakBottomWall();
			break;
			case -1:
				// Ultimate is beneath
				origin.breakBottomWall();
				ultimate.breakTopWall();
			break;
		}
	}
	
	
	private void weave(Cell testedCell) {
		// Shortcut
		ArrayList<Cell> ariane = this.arianeString;
		
		ariane.add(testedCell);
		testedCell.setAsUsed();
		
		ArrayList<Cell> choices = neighboorsFinder(testedCell);
		
		// Checks if there are cells around
		if(choices.isEmpty() || choices.size() == 0) {
			// Removes the last one we added
			ariane.remove(ariane.size() - 1);
			
			// Go back with the previous cell
			weave(ariane.get(ariane.size() - 1));
		}else {
			Random random = new Random();
			int finalChoice = random.nextInt(choices.size());
			Cell nextCell = choices.get(finalChoice);
			
			// Breaks the wall
			wallBreaker(testedCell, nextCell);
			
			// Continue to weave with the next chosen one
			weave(nextCell);
		}
	}
	
	
	// Traces the labyrinth
	private void traces() {
		// Initiates with random point
		Coordinates randomPoint = randomPoint();
		Cell beginning = this.getCell(randomPoint);
		
		// Weave will eventually return the final point
		this.weave(beginning);
	}
	
	
	
	// Used to print into the file 
	@Override
	public String toString() {
		String labyrinth = "";
		int i, j;
		// Returns the layrinth
		for(i = 0; i < this.height; i++) {
			for(j = 0; j < this.width; j++) {
				Cell targetedCell = this.getCell(new Coordinates(j, i));
				//labyrinth = labyrinth.concat(targetedCell.getSymbol());
			}
			labyrinth = labyrinth.concat("\n");
		}
		
		return labyrinth;
	}
}
