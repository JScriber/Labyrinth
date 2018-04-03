package fr.imie.labyrinth.launcher;

import fr.imie.labyrinth.symbol.Symbol;

import java.util.ArrayList;
import java.util.Random;

public class Maze {
	private Cell[][] maze;
	private int width, height;
	private ArrayList<Cell> ariane;

	public Maze(int width, int height){
		this.maze = new Cell[width][height];

		this.width = width;
		this.height = height;

		this.fillWithCells();

		// Starts the processing
		this.ariane = new ArrayList<>();

		this.process(getRandomCell());
		this.addStartGoal(0.75F);
	}

	// Constructor for solving
	public Maze(Cell[][] maze){
		this.maze = maze;

		this.width = maze.length;
		this.height = maze[0].length;

		// Used for the solving
		this.ariane = new ArrayList<>();
	}

	// Initializing functions
	private void fillWithCells(){
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				maze[i][j] = new Cell(i, j);
			}
		}
	}
	private Cell getRandomCell(){
		int x, y;
		Random random = new Random();
		x = random.nextInt(this.width);
		y = random.nextInt(this.height);

		return this.maze[x][y];
	}

	// Constructions functions
	private void process(Cell currentCell){
		currentCell.setAsVisited();
		// Get the neighboors
		ArrayList<Cell> neighboors = getNeighboors(currentCell);
		if(!mazeIsFull()){
			if(neighboors.isEmpty()) {
				// Get back into the ariane string
				Cell lastCell = ariane.get(ariane.size() - 1);
				ariane.remove(ariane.size() - 1);

				process(lastCell);
			}else{
				// Picks the next cell
				int randomIndex = new Random().nextInt(neighboors.size());
				Cell nextCell = neighboors.get(randomIndex);

                /*
                System.out.println(currentCell.getX()+" - "+currentCell.getY()+" has "+neighboors.size()+" neighboors and "+nbrOfWalls(currentCell)+" walls");
                System.out.println("go to : "+nextCell.getX()+" - "+nextCell.getY());
                */

				// Adds the last cell to the ariane string
				ariane.add(currentCell);

				// Removes the wall
				wallBreaker(currentCell, nextCell);

				process(nextCell);
			}
		}else{
			System.out.println("Labyrinth generated");
		}
	}

	private ArrayList<Cell> getNeighboors(Cell middleCell){
		int x = middleCell.getX();
		int y = middleCell.getY();

		ArrayList<Cell> neighboors = new ArrayList<>();
		Cell testedCell;

		// Search for left
		if(x != 0){
			testedCell = this.maze[x-1][y];
			if(!testedCell.isVisited()){
				neighboors.add(testedCell);
			}
		}
		// Search for right
		if(x != this.width-1){
			testedCell = this.maze[x+1][y];
			if(!testedCell.isVisited()){
				neighboors.add(testedCell);
			}
		}

		// Search for top
		if(y != 0){
			testedCell = this.maze[x][y-1];
			if(!testedCell.isVisited()){
				neighboors.add(testedCell);
			}
		}
		// Search for bottom
		if(y != this.height-1){
			testedCell = this.maze[x][y+1];
			if(!testedCell.isVisited()){
				neighboors.add(testedCell);
			}
		}
		return neighboors;
	}
	private boolean mazeIsFull(){
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if(!this.maze[i][j].isVisited()){
					return false;
				}
			}
		}
		return true;
	}
	private void wallBreaker(Cell from, Cell to){
		int diffX = from.getX() - to.getX();
		int diffY = from.getY() - to.getY();

		// Go down
		if(diffY == -1){
			from.getBottom().breaks();
			to.getTop().breaks();
			return ;
		}

		// Go up
		if(diffY == 1){
			from.getTop().breaks();
			to.getBottom().breaks();
			return ;
		}

		// Go left
		if(diffX == 1){
			from.getLeft().breaks();
			to.getRight().breaks();
			return ;
		}

		// Go right
		if(diffX == -1){
			from.getRight().breaks();
			to.getLeft().breaks();
			return ;
		}
	}

	// Add start en goal point
	private void addStartGoal(float spaceRatio){
		int sX = 0, sY = 0;
		int spaceBetweenX, spaceBetweenY;
		Random random = new Random();

		// Calculate space ratio
		spaceBetweenX = Math.round(this.width*spaceRatio);
		spaceBetweenY = Math.round(this.height*spaceRatio);

		do{
			// Divided by 3 to ease the process
			sX = random.nextInt(this.width/3);
			sY = random.nextInt(this.height/3);
		} while((sX+spaceBetweenX >= this.width) || (sY+spaceBetweenY >= this.height));

		this.maze[sX][sY].defineAsStart();
		this.maze[sX+spaceBetweenX][(sY+spaceBetweenY)].defineAsGoal();
	}


	// Only used for tests
	private int nbrOfWalls(Cell tested){
		int nbr = 0;
		if(!tested.getLeft().isBroken()){
			nbr++;
		}
		if(!tested.getRight().isBroken()){
			nbr++;
		}
		if(!tested.getTop().isBroken()){
			nbr++;
		}
		if(!tested.getBottom().isBroken()){
			nbr++;
		}
		return nbr;
	}


	// The following methods are chiefly used to solve the maze

	public Cell getStartPoint(){
		Cell testedCell;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				testedCell = this.maze[i][j];
				if(testedCell.isStartPoint()){
					return testedCell;
				}
			}
		}
		return null;
	}
	// Returns the contiguous cells (walls are considered)
	private ArrayList<Cell> getContiguousCells(Cell mainCell){
		int x = mainCell.getX();
		int y = mainCell.getY();

		// Gets the neighboors
		ArrayList<Cell> neighboors = this.getNeighboors(mainCell);

		// Debug
		System.out.println("Before : ");
		for (int i = 0; i < neighboors.size(); i++) {
			System.out.println(i+") "+neighboors.get(i).getX()+" "+neighboors.get(i).getY());
		}

		// Tests the adjacency
		Cell treatedCell;
		int posX, posY;
		for (int i = 0; i < neighboors.size(); i++) {
			treatedCell = neighboors.get(i);

			posX = -1 * (x - treatedCell.getX());
			posY = -1 * (y - treatedCell.getY());

			if(posX == 1){
				if(!mainCell.getRight().isBroken()){
					neighboors.remove(i);
				}
			}else{
				if(posX == -1){
					if(!mainCell.getLeft().isBroken()){
						neighboors.remove(i);
					}
				}
			}

			if(posY == 1){
				if(!mainCell.getBottom().isBroken()){
					neighboors.remove(i);
				}
			}else{
				if(posY == -1){
					if(!mainCell.getTop().isBroken()){
						neighboors.remove(i);
					}
				}
			}
		}
		// Only returns contiguous cells
		return neighboors;
	}

	// Solves the maze
	public void solve(Cell currentCell){
		System.out.println("-----");
		System.out.println("Tested : "+currentCell.getX()+" "+currentCell.getY());
		currentCell.setAsVisited();

		currentCell.setQuickPath(true);

		// Get the neighboors
		ArrayList<Cell> contiguousCells = getContiguousCells(currentCell);
		if(!currentCell.isGoalPoint()){
			if(contiguousCells.isEmpty()) {
				currentCell.setQuickPath(false);

				// Get back into the ariane string
				Cell lastCell = ariane.get(ariane.size() - 1);
				ariane.remove(ariane.size() - 1);

				solve(lastCell);
			}else{
				// Debug

				System.out.println("After : ");
				for (int i = 0; i < contiguousCells.size(); i++) {
					System.out.println(i+") "+contiguousCells.get(i).getX()+" "+contiguousCells.get(i).getY());
				}


				// Picks the next cell
				int randomIndex = new Random().nextInt(contiguousCells.size());
				Cell nextCell = contiguousCells.get(randomIndex);

				// Adds the last cell to the ariane string
				ariane.add(currentCell);

				solve(nextCell);
			}
		}else{
			System.out.println("Goal achieved");
		}
	}

	// Displaying functions
	@Override
	public String toString() {
		// Show the number of wall for

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                System.out.println(j+" "+i+" has "+nbrOfWalls(maze[j][i])+" walls");
            }
            System.out.println();
        }
        System.out.println();


		// Get the associated strings
		String wall = Symbol.WALL.toString();
		String lane = Symbol.LANE.toString();
		String startPoint = Symbol.START.toString();
		String endPoint = Symbol.END.toString();
		String quickPath = Symbol.QUICK.toString();

		String render = "";
		String backspace = "\n";

		for (int i = 0; i < this.height; i++) {
			// Top bar
			for (int j = 0; j < this.width; j++) {
				Cell displayedCell = maze[j][i];

				render = render.concat(wall);
				if(displayedCell.getTop().isBroken()){
					if(displayedCell.isMemberOfQuickPath()){

						//render = render.concat(quickPath);

						if(i != 0){
							if(maze[j][i-1].isMemberOfQuickPath()){
								render = render.concat(quickPath);
							}else{
								render = render.concat(lane);
							}
						}
					}else{
						render = render.concat(lane);
					}
				}else{
					render = render.concat(wall);
				}
				// Right add
				if(j == this.width - 1){
					render = render.concat(wall);
				}
			}
			render = render.concat(backspace);

			// Left bar
			for (int j = 0; j < this.width; j++) {
				Cell displayedCell = maze[j][i];
				if(displayedCell.getLeft().isBroken()){
					// By default the left-left wall are untouched
					if(j != 0){
						if(displayedCell.isMemberOfQuickPath() && maze[j-1][i].isMemberOfQuickPath()){
							render = render.concat(quickPath);
						}else{
							render = render.concat(lane);
						}
					}
				}else{
					render = render.concat(wall);
				}
				if(displayedCell.isStartPoint()){
					render = render.concat(startPoint);
				}else{
					if(displayedCell.isGoalPoint()){
						render = render.concat(endPoint);
					}else{
						if(displayedCell.isMemberOfQuickPath()){
							render = render.concat(quickPath);
						}else{
							render = render.concat(lane);
						}
					}
				}
				// Right add
				if(j == this.width - 1){
					render = render.concat(wall);
				}
			}
			render = render.concat(backspace);

			// Bottom bar
			if(i == this.height-1){
				for (int j = 0; j < this.width; j++) {
					render = render.concat(wall).concat(wall);
					// Right add
					if(j == this.width - 1){
						render = render.concat(wall);
					}
				}
				render = render.concat(backspace);
			}
		}
		return render;
	}
}
