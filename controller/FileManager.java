package controller;

import java.io.*;
import java.util.Arrays;

import model.LabState;

public class FileManager {
	public static void save(LabState ls) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ls.getName() + ".laby"));
			out.writeObject(ls);
			out.close();
		} catch(IOException ex) {
			System.out.println(ex);
		}
	}

	public static LabState load(String fname){
		LabState labState = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fname));
			labState = (LabState)in.readObject();
			in.close();
		} catch(IOException ex) {
			System.out.println(ex);
		} catch(ClassNotFoundException ex) {
			System.out.println(ex);
		}
		return labState;
	}

	public static String[] getLabys() {
    File dir = new File(".");
    String[] contents = dir.list();
		return Arrays.asList(contents).stream().filter(s -> s.matches(".*\\.laby")).toArray(String[]::new);
	}

}
