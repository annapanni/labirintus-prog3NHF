package model;

import java.util.List;
import java.util.ArrayList;


public class Light implements java.io.Serializable{
	private Storable origin;
	private double radius = Double.POSITIVE_INFINITY;
	private double flicker = 0.2;
	private double dimFrom = 0.3;
	private int rayNum = 100;
	private double step = 0.05;
	private ModelColor color = ModelColor.NONE;

	public double getRadius(){return radius;}
	public void setRadius(double rad){radius = rad;}
	public Storable getOrigin(){return origin;}
	public ModelColor getColor(){return color;}
	public double getDimFrom(){return dimFrom;}
	public double getFlicker(){return flicker;}

	public Light(Storable og){
		origin = og;
	}
	public Light(Storable og, double rad, double df, double fl){
		this(og, rad, df, fl, ModelColor.NONE);
	}
	public Light(Storable og, double rad, double df, double fl, ModelColor col){
		origin = og;
		radius = rad;
		color = col;
		dimFrom = df;
		flicker = fl;
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
