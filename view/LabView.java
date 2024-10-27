package view;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Iterator;

import model.*;

public class LabView {
	private LabState labState;
	private int offset;
	private double scale;
	private Vector routeFrom;

	public LabView(LabState laby, int offs, int sc) {
		labState = laby;
		offset = offs;
		scale = sc;
	}

	private double calculateCorridorWidth(){
		List<double[]> nodePoly = labState.getLab().getNodePoly(new Vector(0,0));
		double dx = nodePoly.get(0)[0] - nodePoly.get(1)[0];
		double dy = nodePoly.get(0)[1] - nodePoly.get(1)[1];
		return Math.sqrt(dx*dx + dy*dy);
	}

	private int labPosToPx(double p){
		return offset + (int)((p + 2/scale) * scale);
	}

	private double pxToLabPos(int px){
		return (px - offset) / scale;
	}

	private void drawCell(Graphics2D g, Vector idx) {
		List<double[]> xyLabPos = labState.getLab().getNodePoly(idx);
		int[] xpos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[0])).toArray();
		int[] ypos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[1])).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
	}

	private void drawRoom(Graphics2D g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> labPosToPx(labState.getLab().xPosition(v))).toArray();
		int[] ypos = border.stream().mapToInt(v -> labPosToPx(labState.getLab().yPosition(v))).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
		g.drawPolygon(xpos, ypos, xpos.length);
	}

	private void drawCorridors(Graphics2D g){
		for (int y=0; y < labState.getLab().getHeight(); y++){
			for (int x=0; x < labState.getLab().getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (! labState.getLab().inBound(idx)) {
					continue;
				}
				drawCell(g, idx);
				int centerX = labPosToPx(labState.getLab().xPosition(idx));
				int centerY = labPosToPx(labState.getLab().yPosition(idx));
				int endX = labPosToPx(labState.getLab().xPosition(idx.plus(labState.getLab().getDir(idx))));
				int endY = labPosToPx(labState.getLab().yPosition(idx.plus(labState.getLab().getDir(idx))));
				g.drawLine(centerX, centerY, endX, endY);
			}
		}
	}

	private void drawObject(Graphics2D g, Storable obj, Color col){
		g.setColor(col);
		int x = labPosToPx(obj.getXPos());
		int y = labPosToPx(obj.getYPos());
		g.fillOval(x-3, y-3, 6, 6);
	}

	private Area getLightArea(Light l, double rad) {
		List<double[]> lightPoly = l.getLightPoly();
		int[] xpos = lightPoly.stream().mapToInt(c -> labPosToPx(c[0])).toArray();
		int[] ypos = lightPoly.stream().mapToInt(c -> labPosToPx(c[1])).toArray();
		Area light = new Area(new Polygon(xpos, ypos, xpos.length));
		if (rad != Double.POSITIVE_INFINITY) {
			Storable og = l.getOrigin();
			Area range = new Area(new Ellipse2D.Double(labPosToPx(og.getXPos()) - rad, labPosToPx(og.getYPos()) - rad, rad*2, rad*2));
			light.intersect(range);
		}
		return light;
	}

	private void drawLightColors(Graphics2D g, List<Light> lights) {
		for (Light light : lights){
			if (light.getColor() != null && light.getColor().equals("yellow")){ //TODO decode color
				Area lArea = getLightArea(light, light.getRadius()*scale);
				int cx = labPosToPx(light.getOrigin().getXPos());
				int cy = labPosToPx(light.getOrigin().getYPos());
				double r = light.getRadius() * scale;
				Paint p = new RadialGradientPaint(cx, cy, (float)r, new float[]{(float)light.getDimFrom(), 1f},
					new Color[]{new Color(250, 240, 130, 150), new Color(250, 240, 130, 0)});
				g.setPaint(p);
				g.fill(lArea);
			}
		}
	}

	private BufferedImage darknessImage(List<Light> lights) {
		BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB); //TODO fixed size
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		for (Light light : lights) {
			Area lArea = getLightArea(light, light.getRadius()*scale);
			int cx = labPosToPx(light.getOrigin().getXPos());
			int cy = labPosToPx(light.getOrigin().getYPos());
			double r = light.getRadius() * scale;
			Paint p = new RadialGradientPaint(cx, cy, (float)r, new float[]{(float)light.getDimFrom(), 1f},
				new Color[]{new Color(255, 255, 255, 255), new Color(255, 255, 255, 0)});
			g.setPaint(p);
			g.fill(lArea);
		}
		for (int x=0; x < image.getWidth(); x++){
			for (int y=0; y < image.getHeight(); y++){
				int col = image.getRGB(x, y);
				int newAlpha = (256 - col & 0xff) << 24;
				int newCol = (col & 0xff000000) + newAlpha;
				image.setRGB(x, y, newCol);
			}
		}
		return image;
	}

	public void drawAll(Graphics2D g) {
		g.setStroke(new BasicStroke((float)(scale * calculateCorridorWidth()+ 1)));
		g.setColor(Color.WHITE);
		for (Room r : labState.getLab().getRooms()) {
			drawRoom(g, r);
		}
		drawCorridors(g);

		for (Storable obj : labState.getObjects()){
			Color col = obj.equals(labState.getPlayer()) ? Color.RED : Color.BLUE;
			drawObject(g, obj,col);
		}
		List<Light> lights = labState.getObjects().stream().map(Storable::getLight).filter(l->l!=null).toList();
		drawLightColors(g, lights);
		BufferedImage darkness = darknessImage(lights);
		g.setColor(new Color(0, 0, 0, 255));
		Light sRange =  labState.getSightRange();
		if (sRange != null) {
			g.drawImage(darkness, 0, 0, null);
			Area sightDarkness = new Area(new Rectangle(1000, 1000)); //fixed size bad bad TODO
			sightDarkness.subtract(getLightArea(sRange, Double.POSITIVE_INFINITY));
			g.fill(sightDarkness);
		} else {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g.drawImage(darkness, 0, 0, null);
		}
	}





	public void handleClick(int x, int y){
		Vector vclick = labState.getLab().posToVec(pxToLabPos(x), pxToLabPos(y));
		if (! labState.getLab().inBound(vclick)) {return;}
		if (routeFrom == null) {
			routeFrom = vclick;
		} else {
			Storable fly = new Firefly(labState.getLab(), routeFrom, vclick, 0.01);
			Light li = new Light(fly, 0.8, 0.3, "yellow");
			fly.setLight(li);
			labState.getObjects().add(fly);
			routeFrom = null;
		}
	}

 	public void handleMouseMove(int x, int y) {
		double dx = pxToLabPos(x) - labState.getPlayer().getXPos();
		double dy = pxToLabPos(y) - labState.getPlayer().getYPos();
		labState.getPlayer().setPosition(labState.getPlayer().getXPos() + dx/10, labState.getPlayer().getYPos() + dy/10);
		Iterator<Storable> it = labState.getObjects().iterator();
		while (it.hasNext()) {
			Storable obj = it.next();
			if (obj instanceof Moving) {
				Moving mov = (Moving)obj;
				boolean done = mov.step();
				if (done) {

					it.remove();
				}
			}
		}
	}

}
