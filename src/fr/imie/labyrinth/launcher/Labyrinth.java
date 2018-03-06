package fr.imie.labyrinth.launcher;

import java.util.ArrayList;
import java.util.Random;

import fr.imie.labyrinth.graph.Coordinates;
import fr.imie.labyrinth.graph.Distance;

public class Labyrinth {
	private int width;
	private int height;
	
	private Cell[][] cells;
	
	
	public Labyrinth(int width, int height) {
		this.width = width;
		this.height = height;
		// Creating an empty labyrinth
		this.cells = new Cell[width][height];
		
		// Filling it with walls
		this.defaultFilling();
	}
	
	
	// Return random coordinates within the labyrinth
	private Coordinates randomPoint() {
		int x = Coordinates.randomBetween(1, this.width-1);
		int y = Coordinates.randomBetween(1, this.height-1);
		
		return new Coordinates(x, y);
	}
	
	
	// Modify the cells
	private void setCell(Coordinates coordinates, Cell cell) {
		this.cells[coordinates.getX()][coordinates.getY()] = cell;
	}
	private Cell getCell(Coordinates coordinates) {
		return cells[coordinates.getX()][coordinates.getY()];
	}
	
	
	// Fills with wall only
	private void defaultFilling() {
		int i, j;
		//Wall wall = new Wall();
		
		for(i = 0; i < this.width; i++) {
			for(j = 0; j < this.height; j++) {
				//this.setCell(new Coordinates(i, j), wall);
			}
		}
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
