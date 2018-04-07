package fr.imie.labyrinth.solver;

import fr.imie.labyrinth.launcher.Cell;
import fr.imie.labyrinth.launcher.Maze;
import fr.imie.labyrinth.launcher.Wall;
import fr.imie.labyrinth.symbol.Symbol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import fr.imie.labyrinth.exceptions.IsNotMazeException;

public class Solve {

    // Gets the maze as a String
    private static String getStringMaze(String fileName) throws OutOfMemoryError, IOException {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);

        // Gets the pieces of the maze
        List<String> piecesOfMaze = Files.readAllLines(filePath);
        String textMaze = "";
        // Iterates over the pieces in order to group them together
        for (int i = 0; i < piecesOfMaze.size(); i++) {
            String pieceOfMaze = piecesOfMaze.get(i);
            textMaze = textMaze.concat(pieceOfMaze);
            if(i != piecesOfMaze.size() - 1){
                textMaze = textMaze.concat("\n");
            }
        }
        return textMaze;
    }

    // Breaks the wall if the symbol is equal
    private static void wallBreaker(String symbol, Wall cellWall){

        // Defines what is a broken wall
        String brokenWall = Symbol.LANE.toString();

        if(symbol.equals(brokenWall)) {
            cellWall.breaks();
        }
    }

    // Gets the maze from a file
    public static Maze getMazeFromFile(String fileName) throws IsNotMazeException, OutOfMemoryError, IOException{
        String textMaze = getStringMaze(fileName);

        return getMaze(textMaze);
    }

    // Gets the maze as an array of Cells
    public static Maze getMaze(String textMaze) throws IsNotMazeException, OutOfMemoryError {
        int width, height, realWidth, realHeight;
        Cell maze[][];

        Dimension mazeDim = findWidthHeight(textMaze);
        width = mazeDim.getWidth();
        height = mazeDim.getHeight();

        // Gets the real number of cells out of the text
        realWidth = Math.round(width/2);
        realHeight = Math.round(height/2);

        // Prepares the new maze
        maze = new Cell[realWidth][realHeight];

        // Wipes out the useless and irritating \n
        textMaze = getMazeWithoutBackspaces(textMaze);

        // Tests if it contains only handled characters
        if(!containsHandledCharacters(textMaze)){
            throw new IsNotMazeException("A not handled character has been found!");
        }

        // Iterates over the maze (two by two)
        int counter = 0;

        int mazeX = 0;
        int mazeY = 0;

        for (int i = width+1; i < textMaze.length(); i += 2) {
            String symbol = textMaze.charAt(i)+"";
            // Note that symbol is either a _ or a S/G

            Cell addedCell = new Cell(mazeX, mazeY);

            // Find the walls
            String testedSymbol;

            // Left one
            testedSymbol = textMaze.charAt(i-1)+"";
            wallBreaker(testedSymbol, addedCell.getLeft());

            // Right one
            testedSymbol = textMaze.charAt(i+1)+"";
            wallBreaker(testedSymbol, addedCell.getRight());

            // Top one
            if(mazeY > 0){
                testedSymbol = textMaze.charAt(i-width)+"";
                wallBreaker(testedSymbol, addedCell.getTop());
            }

            // Bottom one
            if(mazeY < realHeight){
                testedSymbol = textMaze.charAt(i+width)+"";
                wallBreaker(testedSymbol, addedCell.getBottom());
            }

            // Define as start or goal
            if(symbol.equals(Symbol.START.toString())){
                addedCell.defineAsStart();
            }
            if(symbol.equals(Symbol.END.toString())){
                addedCell.defineAsGoal();
            }

            // Adds the cell
            maze[mazeX][mazeY] = addedCell;

            counter++;
            // Moves inside the maze horizontally
            mazeX++;

            if(counter%realWidth == 0){
                System.out.println();

                // Moves inside the maze vertically
                mazeY++;
                mazeX = 0;

                // Adds one to not get the first wall
                i++;

                // A row to skip the bottom walls
                i += width;
            }
        }
        return new Maze(maze);
    }

    // Returns the textMaze without the \n
    private static String getMazeWithoutBackspaces(String maze){
        return maze.replaceAll("\n", "");
    }

    // Find out the size of the maze
    private static Dimension findWidthHeight(String maze) throws IsNotMazeException {
        String character = "";
        String notRectangleMessage = "Not rectangle maze.";

        int counter = 0, width, height;

        for (int i = 0; i < maze.length() ; i++) {
            character = maze.charAt(i)+"";

            if(character.equals("\n")){
                break;
            }
            counter++;
        }
        if(counter == maze.length()){
            throw new IsNotMazeException("A single line is not a maze.");
        }

        width = counter;
        // Calculates the theoric height from the width
        height = (getMazeWithoutBackspaces(maze).length())/width;

        //Security test
        int loop = 0;
        for (int i = width; i < maze.length(); i += width+1) {
            character = maze.charAt(i)+"";

            if(!character.equals("\n")){
                throw new IsNotMazeException(notRectangleMessage);
            }
            loop++;
        }

        // Height test
        if(loop != height){
            throw new IsNotMazeException(notRectangleMessage);
        }

        // Further and ultimate testing (maths only)
        int finalSize = (height*width)+height-1;
        if(finalSize != maze.length()-1){
            throw new IsNotMazeException(notRectangleMessage);
        }

        return new Dimension(width, height);
    }

    // Security test
    private static boolean containsHandledCharacters(String testedString){
        String[] authorizedValue = {
                Symbol.END.toString(),
                Symbol.START.toString(),
                Symbol.LANE.toString(),
                Symbol.WALL.toString()
        };

        for (int i = 0; i < testedString.length(); i++) {
            String character = testedString.charAt(i)+"";
            int counter = 0;

            for (String value: authorizedValue) {
                if(character.equals(value)) {
                    counter++;
                }
            }
            if(counter != 1){
                return false;
            }
        }
        return true;
    }
}
