package fr.imie.labyrinth.launcher;

import java.io.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.*;

import fr.imie.labyrinth.exceptions.IsNotArchiveException;
import fr.imie.labyrinth.exceptions.IsNotMazeException;
import fr.imie.labyrinth.exceptions.MissingArgumentsException;
import fr.imie.labyrinth.exceptions.TooHighNumberException;
import fr.imie.labyrinth.exceptions.NegativeNumberException;
import fr.imie.labyrinth.solver.Solve;

/*
Args
>>> simple 3 3 resolv.laby
>>> multiple 3 3 3 labyrinth.zip

>>> solver resolv.laby
>>> solver labyrinth.zip
*/

public class Launcher {
	//  Simple labyrinth part
	public static void simpleLabyrinth(int width, int height, String file) {
		Maze laby = new Maze(width, height);

		// Create a file and put it into
		try{
		    PrintWriter writer = new PrintWriter(file, "UTF-8");
		    writer.println(laby);
		    writer.close();
		} catch(IOException e) {
			System.out.println("Not able to save the labyrinth.");
		}
		// Displays it into the console as well
		System.out.println(laby);
		
	}
	
	
	// Multiple labyrinth part
	public static void multipleLabyrinth(int numberOfLabyrinth, int width, int height, String archive) throws Exception {
		// Create the archive
        String archiveName = "labyrinth.zip";

        ZipOutputStream out = new ZipOutputStream(
                new FileOutputStream(System.getProperty("user.dir")+"/"+archiveName)
        );


		// Will basically call labyrinth several times
		String fileName;
		for(int i = 1; i <= numberOfLabyrinth; i++) {
			fileName = "labyrinth"+i+".laby";

            ZipEntry entry = new ZipEntry(fileName);
            out.putNextEntry(entry);

            Maze maze = new Maze(width, height);
            String mazeString = maze.toString();
            byte byteCode[] = mazeString.getBytes();

            for (int j = 0; j < byteCode.length; j++) {
                out.write(byteCode[j]);
            }
            out.closeEntry();
		}
		out.close();

		// Final message to inform the user
		System.out.println(numberOfLabyrinth+" labyrinths have been created inside "+archiveName+".");
	}


	// Solver for files
	public static void solveFile(String fileName){
		try{
			Maze receivedMaze = Solve.getMazeFromFile(fileName);
			receivedMaze.solve(receivedMaze.getStartPoint());

			System.out.println(receivedMaze);

			// Enters into the solver
		} catch (OutOfMemoryError e) {
			System.out.println(fileName+" is too big.");
		} catch (IsNotMazeException e){
			System.out.println(e.getError());
			System.out.println(fileName+" doesn't contain a maze.");
		} catch (IOException e){
			System.out.println("Could't access to "+fileName);
		}
	}
	// Solver for archive
	// Snippet/convertor from stackoverflow
	public static String convertInputStream(InputStream stream){
		Scanner s = new Scanner(stream).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";

		return result;
	}
	public static void solveArchive(String fileName){
		try
		{
			ZipFile zipFile = new ZipFile(System.getProperty("user.dir")+"/"+fileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			int numberOfLabyrinth = 1;

			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				InputStream stream = zipFile.getInputStream(entry);

				String textMaze = convertInputStream(stream);
				try {
					Maze receivedMaze = Solve.getMaze(textMaze);
					receivedMaze.solve(receivedMaze.getStartPoint());

					System.out.println("Maze "+numberOfLabyrinth+":");
					System.out.println(receivedMaze);
				}catch(IsNotMazeException e){
					System.out.println("Maze number "+numberOfLabyrinth+" doesn't contain a maze.");
				}
				System.out.println();

				stream.close();
				// Used for the user
				numberOfLabyrinth++;
			}
		} catch (IOException e) {
			System.out.println("Couldn't get " + fileName);
		}
	}

	
	// Checks if the supposed numbered values are really numbers.
	public static boolean checkIfInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	// Tests at multiple levels if the given parameters suit our needs
	public static void checkParameters(String [] args) throws MissingArgumentsException, IsNotArchiveException, TooHighNumberException, NegativeNumberException {
		int numberOfArguments = args.length;
		
		if(numberOfArguments != 0) {
			final String complexity = args[0].toLowerCase();
			// Safety
			int maxLimit = 500;
			String limitReached = "You reached the limit of "+maxLimit+".\nThis is a safety as your memory can be quickly overloaded.";
			String negativeWarning = "You gave a number inferior or equal to zero.";
			
			// Checks if everything is given by the user
			switch (complexity) {
				case "simple":
					if(numberOfArguments != 4) {
						throw new MissingArgumentsException("You must follow : simple <width> <height> <file>");
					}
					// Checks if the two following values are numbers
					if(checkIfInteger(args[1])) {
						if(checkIfInteger(args[2])) {
							int width = Integer.parseInt(args[1]);
							int height = Integer.parseInt(args[2]);
							if(width > maxLimit || height > maxLimit) {
								throw new TooHighNumberException(limitReached);
							}
							if(width <= 0 || height <= 0) {
								throw new NegativeNumberException(negativeWarning);
							}
							
							simpleLabyrinth(width, height, args[3]);
						}else {
							throw new MissingArgumentsException("Given height is not a number.");
						}
					}else {
						throw new MissingArgumentsException("Given width is not a number.");
					}
				break;
				case "multiple":
					if(numberOfArguments != 5) {
						throw new MissingArgumentsException("You must follow : multiple <numberOfLabyrinth> <width> <height> <archive>");
					}
					// Checks if the three following values are numbers
					if(checkIfInteger(args[1])) {
						if(checkIfInteger(args[2])) {
							if(checkIfInteger(args[3])) {
								int numberOfLabyrinth = Integer.parseInt(args[1]);
								int width = Integer.parseInt(args[2]);
								int height = Integer.parseInt(args[3]);
								// Tests the maximum limit
								if(width > maxLimit || height > maxLimit || numberOfLabyrinth > maxLimit) {
									throw new TooHighNumberException(limitReached);
								}
								// Tests the minimum limit
								if(width <= 0 || height <= 0 || numberOfLabyrinth <= 0) {
									throw new NegativeNumberException(negativeWarning);
								}
								
								// The archive must be a zip file
								String archiveName = args[4];
								if(!archiveName.contains(".zip")) {
									String errorMessage = "This program only handles .zip archives.\nTherefore you must ";
									if(archiveName.contains(".")) {
										errorMessage = errorMessage.concat("change your extension.");
									}else {
										errorMessage = errorMessage.concat("specify a .zip extension at the end of "+archiveName);
									}
									throw new IsNotArchiveException(errorMessage);
								}
							
								try
                                {
                                    multipleLabyrinth(numberOfLabyrinth, width, height, archiveName);
                                } catch (Exception e){
                                    System.out.println("Couldn't create an archive.");
                                }
							}else {
								throw new MissingArgumentsException("Given height is not a number.");
							}
						}else {
							throw new MissingArgumentsException("Given width is not a number.");
						}
					}else {
						throw new MissingArgumentsException("Given number of labyrinth is not a number.");
					}
				break;
				case "solver" :
					if(numberOfArguments != 2) {
						throw new MissingArgumentsException("You must follow : solver <file or archive>");
					}
					String givenFile = args[1];
					if(givenFile.contains(".zip")){
						solveArchive(givenFile);
					}else{
						solveFile(givenFile);
					}
				break;
	
				default:
					throw new MissingArgumentsException("Invalid parameters.");
			}
		}else {
			throw new MissingArgumentsException("You didn't give parameters.");
		}
	}
	
	// Main method
	public static void main(String[] args) {
		try {
			checkParameters(args);
		} catch (MissingArgumentsException e) {
			// Displays what's wrong
			System.out.println(e.getError());
		} catch (TooHighNumberException e) {
			System.out.println(e.getError());
		} catch (NegativeNumberException e) {
			System.out.println(e.getError());
		} catch (IsNotArchiveException e) {
			System.out.println(e.getError());
		}
	}
}
