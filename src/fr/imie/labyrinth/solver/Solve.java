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
    public static String getStringMaze(String fileName) throws OutOfMemoryError, IOException {
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
    public static void wallBreaker(String symbol, Wall cellWall){

        // Defines what is a broken wall
        String brokenWall = Symbol.LANE.toString();

        if(symbol.equals(brokenWall)) {
            cellWall.breaks();
        }
    }

    // Gets the maze as an array of Cells
    public static Maze getMaze(String fileName) throws IsNotMazeException, OutOfMemoryError, IOException{
        int width, height, realWidth, realHeight;
        Cell maze[][];

        // Gets the maze (text)
        String textMaze = getStringMaze(fileName);

        Dimension mazeDim = findWidthHeight(textMaze);
        width = mazeDim.getWidth();
        height = mazeDim.getHeight();

        // Gets the real number of cells out of the text
        realWidth = Math.round(width/2);
        realHeight = Math.round(height/2);

        // Prepares the new maze
        maze = new Cell[realWidth][realHeight];

        // Wipes out the useless and irritating \n
        textMaze = textMaze.replaceAll("\n", "");

        // Tests if it contains only handled characters
        if(!containsHandledCharacters(textMaze)){
            throw new IsNotMazeException("An unhandled character has been found!");
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

    // Find out the size of the maze
    public static Dimension findWidthHeight(String maze) throws IsNotMazeException {
        String character = "";
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
        height = (maze.length()-1)/counter;

        //Security test
        int loop = 0;
        for (int i = width; i < maze.length(); i += width+1) {
            character = maze.charAt(i)+"";

            if(!character.equals("\n")){
                throw new IsNotMazeException("Not rectangle maze.");
            }
            loop++;
        }
        // Height test
        if(loop != height){
            throw new IsNotMazeException("Not rectangle maze.");
        }

        return new Dimension(width, height);
    }

    // Security test
    public static boolean containsHandledCharacters(String testedString){
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


    // Main will have to be moved inside the launcher :)
    // The input will have to be handled

    public static void main(String[] args) {

        // Gets the maze
        try{
            Maze receivedMaze = getMaze("resolv.laby");

            System.out.println(receivedMaze);


            // Enters into the solver
        } catch (OutOfMemoryError e) {
            System.out.println("The given file is too big.");
        } catch (IsNotMazeException e){
            System.out.println("The given file doesn't contain a maze.");
        } catch (IOException e){
            System.out.println("Could't access to the file.");
        }
    }

}
