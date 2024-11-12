package labyrinth.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a light source in the labyrinth, illuminating an area around an origin point with specific
 * properties such as radius, color, and flickering.
 */
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
	/** Returns a number in [0;1], denoting where the light is starint to dim from*/
	public double getDimFrom(){return dimFrom;}
	/** Returns the constant that describes the flickering rate of this light (the higher the constant the more it flickers) */
	public double getFlicker(){return flicker;}

	/**Constructs a light with a specified origin and default properties.*/
	public Light(Storable og){
		origin = og;
	}
	/**
     * Constructs a light with specified origin, radius, dimming distance, and flicker intensity.
     * @param og  the origin of the light
     * @param rad the radius of the light
     * @param df  the distance from the origin at which the light begins to dim
     * @param fl  the flicker intensity of the light: non negative number, the higher the more intensive is the flickering
     * @param col  the color of the light
	 */
	public Light(Storable og, double rad, double df, double fl, ModelColor col){
		origin = og;
		radius = rad;
		color = col;
		dimFrom = df;
		flicker = fl;
	}
	/** 
	 * Computes the polygonal shape representing the area that could be illuminated by the light
	 * if it has any infinite radius.
	*/
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
