package fr.imie.labyrinth.launcher;

import java.util.ArrayList;
import java.util.Random;

import fr.imie.labyrinth.graph.Coordinates;

public class Labyrinth {
	private int width;
	private int height;

	private Cell lastCell = null;
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
		lastCell.defineAsEnd();
		
		//System.out.println(neighboorsFinder(this.getCell(new Coordinates(0, 0))));
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
		try {
			return cells[x][y];
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("What you want isn't into the table");
			return null;
		}
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
				}else if(i == this.width-1) {
					right = true;
				}
				
				if(j == 0) {
					up = true;
				}else if(j == this.height-1) {
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
	
	// Checks if all cells are filled
	private boolean labyrinthIsFull() {
		int i, j;
		
		for(i = 0; i < this.width; i++) {
			for(j = 0; j < this.height; j++) {
				// If encounters an unused variable
				if(!this.getCell(i, j).isUsed()) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Checks and lists the neighboors
	private ArrayList<Cell> neighboorsFinder(Cell cell) {
		int x = cell.getX();
		int y = cell.getY();
		ArrayList<Cell>options = new ArrayList<Cell>();
		
		Cell nextCell = null;
		
		// Does all the tests
		// Checks if the current cell isn't in a corner/border
		if(!cell.getLeftWall().isProtected()) {
			if(x - 1 >= 0) {
				// Check if the left cell is active
				nextCell = this.getCell(x-1, y);
				if(!nextCell.isUsed()) {
					options.add(nextCell);
				}
			}
		}
		if(!cell.getTopWall().isProtected()) {
			if(y - 1 >= 0) {
				nextCell = this.getCell(x, y-1);
				if(!nextCell.isUsed()) {
					options.add(nextCell);
				}
			}
		}
		if(!cell.getRightWall().isProtected()) {
			if(x + 1 < this.width) {
				nextCell = this.getCell(x+1, y);
				if(!nextCell.isUsed()) {
					options.add(nextCell);
				}
			}
		}
		if(!cell.getBottomWall().isProtected()) {
			if(y + 1 < this.height) {
				nextCell = this.getCell(x, y+1);
				if(!nextCell.isUsed()) {
					options.add(nextCell);
				}
			}
		}
		
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
				//System.out.println("Goes right");
			break;
			case 1:
				// Ultimate is at the left
				origin.breakLeftWall();
				ultimate.breakRightWall();
				//System.out.println("Goes left");
			break;
		}
		// Breaks vertically
		switch(yDiference){
			case 1:
				// Ultimate is above
				origin.breakTopWall();
				ultimate.breakBottomWall();
				//System.out.println("Goes up");
			break;
			case -1:
				// Ultimate is beneath
				origin.breakBottomWall();
				ultimate.breakTopWall();
				//System.out.println("Goes down");
			break;
		}
	}
	
	
	private void weave(Cell testedCell) {
		// Shortcut
		ArrayList<Cell> ariane = this.arianeString;
		
		ariane.add(testedCell);
		testedCell.setAsUsed();
		
		ArrayList<Cell> choices = neighboorsFinder(testedCell);
		
		// Checks if cells are still empty
		if(!this.labyrinthIsFull()) {
			// Checks if there are cells around
			if(choices.isEmpty() || choices.size() == 0) {
				try {
					// Removes the last two one we added
					arianeBack();
					Cell usedCell = ariane.get(this.arianeString.size() - 1);
					arianeBack();
					
					// Go back with the previous cell
					weave(usedCell);
				} catch(Exception e) {
					return ;
				}
			}else {
				Random random = new Random();
				int finalChoice = random.nextInt(choices.size());
				Cell nextCell = choices.get(finalChoice);
				
				// Breaks the wall
				wallBreaker(testedCell, nextCell);
				
				// Sets the cell as the last one
				this.lastCell = testedCell;
				
				/*
				System.out.println("New lap : ");
				System.out.println(testedCell.getX()+" / "+testedCell.getY());
				System.out.println(nextCell.getX()+" / "+nextCell.getY());
				*/
				
				weave(nextCell);
			}
		}else {
			System.out.println("Labyrinth is full");
			return ;
		}
	}
	
	
	// Traces the labyrinth
	private void traces() {
		// Initiates with random point
		Coordinates randomPoint = randomPoint();
		Cell beginning = this.getCell(randomPoint);
		beginning.defineAsStart();
		
		// Weave will eventually return the final point
		this.weave(beginning);
	}
	
	// Goes back on the trail
	private void arianeBack() {
		try{
			this.arianeString.remove(this.arianeString.size() - 1);
		}catch(Exception e) {
			System.out.println("Cannot remove an unexisting cell");
		}
	}
	
	// Used to print into the file 
	@Override
	public String toString() {
		String nothingness = "  ";
		String wall = "██";
		String start = "S•";
		String end = "E•";
		
		String labyrinth = "";
		String symbol = "";
		Cell targetedCell = null;
		int i, j;
		
		// Returns the layrinth
		for(i = 0; i < this.height; i++) {

			/*for(j = 0; j < this.width; j++) {
				
			}*/

			/*labyrinth = labyrinth.concat(wall);
			labyrinth = labyrinth.concat("\n");*/
			
			for(j = 0; j < this.width; j++) {
				targetedCell = this.getCell(new Coordinates(j, i));

				if(targetedCell.isTheStart()) {
					nothingness = start;
				}
				if(targetedCell.isTheEnd()) {
					nothingness = end;
				}
				
				if(targetedCell.getRightWall().isBroken()) {
					if(targetedCell.isTheStart() || targetedCell.isTheEnd()) {
						symbol = nothingness.concat("  ");
					}else {
						symbol = nothingness.concat(nothingness);
					}
				}else {
					symbol = nothingness.concat(wall);
				}
				
				if(targetedCell.isTheStart() || targetedCell.isTheEnd()) {
					nothingness = "  ";
				}
				
				labyrinth = labyrinth.concat(symbol);
			}
			labyrinth = labyrinth.concat("\n").concat(wall);
			for(j = 0; j < this.width; j++) {
				targetedCell = this.getCell(new Coordinates(j, i));
				if(targetedCell.getBottomWall().isBroken()) {
					symbol = nothingness.concat(wall);
				}else {
					symbol = wall.concat(wall);
				}
				labyrinth = labyrinth.concat(symbol);
				
			}
			if(i != this.height-1) {
				labyrinth = labyrinth.concat("\n").concat(wall);
			}else {
				labyrinth = labyrinth.concat("\n");
			}
		}
		
		return labyrinth;
	}
}
