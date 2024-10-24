//maybe in disp package

package model;

import java.util.List;
import java.util.ArrayList;


public class Darkness {
	static int rayNum = 100;
	static double step = 0.05;
	public static List<double[]> darknessFrom(Labyrinth lab, double x, double y) {
		List<double[]> darkPoly = new ArrayList<>();
		Storable ray = new Storable(lab, new Vector(0,0));
		for (int i = 0; i < rayNum; i++) {
			double dx = x;
			double dy = y;
			while (ray.isValidPosition(dx, dy)) {
				dx += Math.cos((double)i * 2 * Math.PI / rayNum) * step;
				dy += Math.sin((double)i * 2 * Math.PI / rayNum) * step;
			}
			darkPoly.add(new double[]{dx, dy});
		}
		return darkPoly;
	}


}
