package model;

import java.util.List;
import java.util.ArrayList;


public class Light implements Moving {
	private Storable origin;
	private double radius = Double.POSITIVE_INFINITY;
	private double ogRadius = Double.POSITIVE_INFINITY;
	private double flicker = 0.2;
	private double dimFrom = 0.3;
	private int rayNum = 100;
	private double step = 0.1;
	private ModelColor color = ModelColor.NONE;

	public double getRadius(){return radius;}
	public void setRadius(double rad){radius = rad;}
	public Storable getOrigin(){return origin;}
	public ModelColor getColor(){return color;}
	public double getDimFrom(){return dimFrom;}

	public Light(Storable og){
		origin = og;
	}
	public Light(Storable og, double rad, double df, double fl){
		this(og, rad, df, fl, ModelColor.NONE);
	}
	public Light(Storable og, double rad, double df, double fl, ModelColor col){
		origin = og;
		radius = rad;
		ogRadius = rad;
		color = col;
		dimFrom = df;
		flicker = fl;
	}

	public boolean step(int dTime){
		if (Storable.rand.nextDouble(0.0, 1.0) < flicker * dTime/30.0) {
			double newRad = radius + radius * Storable.rand.nextDouble(-flicker/4, flicker/4);
			if (newRad < ogRadius * (1 + flicker) && ogRadius * (1 - flicker) < newRad) {
				setRadius(newRad);
			}
		}
		return false;
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
