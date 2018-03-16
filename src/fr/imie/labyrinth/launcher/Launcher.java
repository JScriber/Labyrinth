package fr.imie.labyrinth.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.*;

import fr.imie.labyrinth.exceptions.MissingArgumentsException;
import fr.imie.labyrinth.exceptions.TooHighNumberException;

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

            int len;
            Maze maze = new Maze(width, height);
            String mazeString = maze.toString();
            byte byteCode[] = mazeString.getBytes();

            for (int j = 0; j < byteCode.length; j++) {
                out.write(byteCode[j]);
            }
            out.closeEntry();
		}
		out.close();
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
	public static void checkParameters(String [] args) throws MissingArgumentsException, TooHighNumberException {
		int numberOfArguments = args.length;
		
		if(numberOfArguments != 0) {
			final String complexity = args[0].toLowerCase();
			// Safety
			int maxLimit = 500;
			String limitReached = "You reached the limit of "+maxLimit+".\nThis is a safety as your memory can be quickly overloaded.";
			
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
								if(width > maxLimit || height > maxLimit || numberOfLabyrinth > maxLimit) {
									throw new TooHighNumberException(limitReached);
								}
							
								try
                                {
                                    multipleLabyrinth(numberOfLabyrinth, width, height, args[4]);
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
	
				default:
					throw new MissingArgumentsException("Invalid kind of labyrinth.");
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
		}
	}
}
