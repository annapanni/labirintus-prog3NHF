package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import model.*;

public class MapView {
	private LabState labState;
	private int padding = 10;
	private int xoffset;
	private int yoffset;
	private double scale;
	private int w, h;

	public int getWidth() {return w;}
	public int getHeight() {return h;}

	public MapView(LabState laby, int sc) {
		labState = laby;
		scale = sc;
		setSize();
	}

	private void setSize() {
		List<Double> xs = new ArrayList<>();
		List<Double> ys = new ArrayList<>();
		for (int y=0; y < labState.getLab().getHeight(); y++){
			for (int x=0; x < labState.getLab().getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (labState.getLab().inBound(idx)){
					xs.add(labState.getLab().xPosition(idx));
					ys.add(labState.getLab().yPosition(idx));
				}
			}
		}
		double minx = Collections.min(xs);
		double maxx = Collections.max(xs);
		double miny = Collections.min(ys);
		double maxy = Collections.max(ys);
		xoffset = padding - (int)(minx * scale);
		yoffset = padding - (int)(miny * scale);
		w = (int)((maxx - minx) * scale) + 2*padding;
		h = (int)((maxy - miny) * scale) + 2*padding;
	}

	public void setLabState(LabState ls) {
		labState = ls;
		setSize();
	}

	private void drawRoom(Graphics2D g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> xlabPosToPx(labState.getLab().xPosition(v))).toArray();
		int[] ypos = border.stream().mapToInt(v -> ylabPosToPx(labState.getLab().yPosition(v))).toArray();
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
				int centerX = xlabPosToPx(labState.getLab().xPosition(idx));
				int centerY = ylabPosToPx(labState.getLab().yPosition(idx));
				int endX = xlabPosToPx(labState.getLab().xPosition(idx.plus(labState.getLab().getDir(idx))));
				int endY = ylabPosToPx(labState.getLab().yPosition(idx.plus(labState.getLab().getDir(idx))));
				g.drawLine(centerX, centerY, endX, endY);
			}
		}
	}

	public int xlabPosToPx(double p){
		return xoffset + (int)((p + 2/scale) * scale);
	}

	public int ylabPosToPx(double p){
		return yoffset + (int)((p + 2/scale) * scale);
	}

	public void paint(Graphics2D g) {
		int screenWidth = (int)g.getDeviceConfiguration().getBounds().getWidth();
		int screenHeight = (int)g.getDeviceConfiguration().getBounds().getHeight();
		g.setColor(new Color(240, 230, 140));
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setStroke(new BasicStroke(2.0f));
		g.setColor(Color.BLACK);
		for (Room r : labState.getLab().getRooms()) {
			drawRoom(g, r);
		}
		drawCorridors(g);
	}

	public BufferedImage getImage(int rotation) {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();
		int neww = rotation % 2 == 0 ?  w : h;
		int newh = rotation % 2 == 0 ? h : w;
		paint(g);
		g.translate((neww-w)/2, (newh-h)/2);
    g.rotate(Math.PI/2 * rotation, w/2, h/2);
    g.dispose();
		return img;
	}

}
