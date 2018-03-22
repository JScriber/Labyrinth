package fr.imie.labyrinth.solver;

import fr.imie.labyrinth.launcher.Cell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    // Gets the maze as an array of Cells
    public static Cell[][] getMaze(String fileName) throws OutOfMemoryError, IOException{
        int width, height, realWidth, realHeight;
        Cell maze[][];

        // Gets the maze (text)
        String textMaze = getStringMaze(fileName);

        // @Debug
        System.out.println(textMaze);
        System.out.println("----");

        Dimension mazeDim = findWidthHeight(textMaze);
        width = mazeDim.getWidth();
        height = mazeDim.getHeight();

        // Gets the real number of cells out of the text
        realWidth = Math.round(width/2);
        realHeight = Math.round(height/2);

        System.out.println(realWidth+" "+realHeight);

        // Prepares the new maze
        maze = new Cell[realWidth][realHeight];

        // Wipes out the useless and irritating \n
        textMaze = textMaze.replaceAll("\n", "");

        // Iterates over the maze (two by two)
        int counter = 0;
        for (int i = width+1; i < textMaze.length(); i += 2) {
            if(i%width != 0){
                System.out.print(textMaze.charAt(i)+" ");
            }else{
                i += 2;
                System.out.println();
            }

        }
        return maze;
    }

    // Find out the size of the maze
    public static Dimension findWidthHeight(String maze){
        String character = "";
        int counter = 0, width, height;

        for (int i = 0; i < maze.length() ; i++) {
            character = maze.charAt(i)+"";

            if(character.equals("\n")){
                break;
            }
            counter++;
        }
        width = counter;
        height = (maze.length()-1)/counter;

        return new Dimension(width, height);
    }


    public static void main(String[] args) {
        // Needs to handle the input

        // Gets the maze
        try{
            Cell maze[][] = getMaze("resolv.laby");

            // Enters into the solver
        } catch (OutOfMemoryError e){
            System.out.println("The given file is too big.");
        } catch (IOException e){
            System.out.println("Could't access to the file.");
        }
    }
}
