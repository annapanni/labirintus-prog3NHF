package controller;

import java.io.*;
import java.util.Arrays;

import model.LabState;

public class FileManager {
	private FileManager(){}
	public static void save(LabState ls) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ls.getName() + ".laby"));
		out.writeObject(ls);
		out.close();
	}

	public static LabState load(String fname) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fname));
		LabState labState = (LabState)in.readObject();
		in.close();
		return labState;
	}

	public static String[] getLabys() {
    File dir = new File(".");
    String[] contents = dir.list();
		return Arrays.asList(contents).stream().filter(s -> s.matches(".*\\.laby")).toArray(String[]::new);
	}

}
