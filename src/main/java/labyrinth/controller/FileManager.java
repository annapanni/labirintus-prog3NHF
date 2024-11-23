package labyrinth.controller;

import java.io.*;
import java.util.Arrays;

import labyrinth.model.LabState;

/**
 * Utility class providing static methods relating to file mangement.
 */
public class FileManager {
	private FileManager(){}
	/**
	 * Saves the state a labyrinth as [labstate.name].laby in the working directory by serializing it
	 * @param ls - LabState to be saved
	 */
	public static void save(LabState ls) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ls.getName() + ".laby"));
		out.writeObject(ls);
		out.close();
	}

	/**
	 * Loads the state a labyrinth from a given file by deserializing it
	 * @param fname - name of the file in which the labyrinth was saved
	 */
	public static LabState load(String fname) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fname));
		LabState labState = (LabState)in.readObject();
		in.close();
		return labState;
	}
	/**
	 * Returns all filenames ending with .laby in the working directory.
	 */
	public static String[] getLabys() {
		File dir = new File(".");
		String[] contents = dir.list();
		return Arrays.asList(contents).stream().filter(s -> s.matches(".*\\.laby")).toArray(String[]::new);
	}

}
