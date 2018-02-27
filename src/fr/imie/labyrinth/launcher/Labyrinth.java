package fr.imie.labyrinth.launcher;

import java.util.ArrayList;
import java.util.Random;

import fr.imie.labyrinth.graph.Coordinates;
import fr.imie.labyrinth.graph.Distance;

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
		
		// Add the paths
		this.addPaths();
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
		Distance distance;
		int distanceRatio = 4;
		
		
		// Makes sure the distance between the points is great
		do {
			// Makes sure we aren't overwriting the Start point.
			// (it wouldn't make sense...)
			do {
				randomPlace = randomPoint();
			} while(this.getCell(randomPlace) instanceof Start);
			
			// Adds it to the instance
			this.goal = randomPlace;

			distance = new Distance(this.start, this.goal);
		}while(distance.absHorizontal() < this.width/distanceRatio
				 || distance.absVertical() < this.height/distanceRatio);
		
		
		// Adds it to the table
		this.setCell(randomPlace, new Goal());
	}
	
	
	// Modify the cells
	private void setCell(Coordinates coordinates, Cell cell) {
		this.cells[coordinates.getX()][coordinates.getY()] = cell;
	}
	// Overload for special Tracer cells
	private void setCell(Tracer cell) {
		Coordinates coordinates = cell.getCoordinates();
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
	
	// Magnetism 
	private double determineXMagnetism(int y, Distance distance) {
		double fraction = (double)distance.horizontal() / (double)distance.vertical();
		return Math.round(fraction*y);
	}
	
	private double determineYMagnetism(int x, Distance distance) {
		double fraction = (double)distance.vertical() / (double)distance.horizontal();
		return Math.round(fraction*x);
	}
	
	// Change the direction
	private void changeDirection(Distance distance, Tracer tracer) {
		Random random = new Random();
		if(random.nextBoolean()) {
			if(distance.endIsBeforeStartY()) {
				tracer.headFor(Direction.DOWN);
			}else {
				tracer.headFor(Direction.UP);
			}
		}else {
			if(!distance.endIsBeforeStartX()) {
				tracer.headFor(Direction.LEFT);
			}else {
				tracer.headFor(Direction.RIGHT);
			}
		}
	}
	
	// Create paths inside the labyrinth
	private void addPaths() {
		// Contains all the branches
		ArrayList<Tracer> branches = new ArrayList<Tracer>();
		
		// Add the first Tracer which coordinates are the same as the start point
		Tracer primaryTracer = new Tracer(this.start.clone());
		branches.add(primaryTracer);

		Distance distance = new Distance(this.start, this.goal);
		
		
		// Search the first direction to take
		Random random = new Random();
		if(random.nextBoolean()) {
			if(distance.endIsBeforeStartY()) {
				primaryTracer.headFor(Direction.DOWN);
			}else {
				primaryTracer.headFor(Direction.UP);
			}
		}else {
			if(!distance.endIsBeforeStartX()) {
				primaryTracer.headFor(Direction.LEFT);
			}else {
				primaryTracer.headFor(Direction.RIGHT);
			}
		}
		
		
		for (int i = 0; i < 50; i++) {
			primaryTracer.move(this.width, this.height);
			
			// Just in case
			if(primaryTracer.hasDifferentCoordinates(this.start)) {
				// Adding it to the table
				this.setCell(primaryTracer);
			}
			changeDirection(distance, primaryTracer);
		}
		
		/*
		for (int i = 1; i < distance.absHorizontal(); i++) {
		 
			System.out.println(determineYMagnetism(i, distance));
		}
		*/
		
		
		System.out.println(primaryTracer.getMoveX()+" : "+primaryTracer.getMoveY());
		
		System.out.println("Distance(X:"+distance.horizontal()+" Y:"+distance.vertical()+")");
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
				labyrinth = labyrinth.concat(targetedCell.getSymbol());
			}
			labyrinth = labyrinth.concat("\n");
		}
		
		return labyrinth;
	}
}
