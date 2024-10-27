package model;

import java.util.List;
import java.util.ArrayList;


public class Light {
	private Storable origin;
	private double radius = Double.POSITIVE_INFINITY;
	private double dimFrom = 0.3;
	private int rayNum = 100;
	private double step = 0.1;
	private ModelColor color = ModelColor.NONE;

	public double getRadius(){return radius;}
	public Storable getOrigin(){return origin;}
	public ModelColor getColor(){return color;}
	public double getDimFrom(){return dimFrom;}

	public Light(Storable og){
		origin = og;
	}
	public Light(Storable og, double rad, double df){
		origin = og;
		radius = rad;
		dimFrom = df;
	}
	public Light(Storable og, double rad, double df, ModelColor col){
		origin = og;
		radius = rad;
		color = col;
		dimFrom = df;
	}

	public List<double[]> getLightPoly() {
		double x = origin.getXPos();
		double y = origin.getYPos();
		List<double[]> lightPoly = new ArrayList<>();
		Storable ray = new Storable(origin.getLab(), new Vector(0,0));
		for (int i = 0; i < rayNum; i++) {
			double dx = x;
			double dy = y;
			while (ray.isValidPosition(dx, dy)) {
				dx += Math.cos((double)i * 2 * Math.PI / rayNum) * step;
				dy += Math.sin((double)i * 2 * Math.PI / rayNum) * step;
			}
			lightPoly.add(new double[]{dx, dy});
		}
		return lightPoly;
	}

}
