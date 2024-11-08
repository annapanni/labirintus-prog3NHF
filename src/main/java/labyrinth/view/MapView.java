package labyrinth.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import labyrinth.model.*;

public class MapView {
	private LabState labState;
	private int padding = 10;
	private int xoffset;
	private int yoffset;
	private double scale;
	private int w;
	private int h;

	public int getWidth() {return w;}
	public int getHeight() {return h;}

	private static Random rand = new Random();

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

	private int fuzz(){
		return rand.nextInt(3) - 1;
	}

	private void drawRoom(Graphics2D g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> xlabPosToPx(labState.getLab().xPosition(v)) + fuzz()).toArray();
		int[] ypos = border.stream().mapToInt(v -> ylabPosToPx(labState.getLab().yPosition(v)) + fuzz()).toArray();
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
				int sections = 3;
				int[] xpos = new int[sections+1];
				int[] ypos = new int[sections+1];
				xpos[0] = xlabPosToPx(labState.getLab().xPosition(idx));
				ypos[0] = ylabPosToPx(labState.getLab().yPosition(idx));
				xpos[sections] = xlabPosToPx(labState.getLab().xPosition(idx.plus(labState.getLab().getDir(idx))));
				ypos[sections] = ylabPosToPx(labState.getLab().yPosition(idx.plus(labState.getLab().getDir(idx))));
				for (int i=1; i<sections; i++) {
					xpos[i] = xpos[0] + i * (xpos[sections] - xpos[0]) / sections + fuzz();
					ypos[i] = ypos[0] + i * (ypos[sections] - ypos[0]) / sections + fuzz();
				}
				g.drawPolyline(xpos, ypos, sections + 1);
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
		int width = (int)g.getDeviceConfiguration().getBounds().getWidth();
		int height = (int)g.getDeviceConfiguration().getBounds().getHeight();
		g.setColor(new Color(240, 230, 140));
		g.fillRect(0, 0, width, height);
		g.setStroke(new BasicStroke(2.0f));
		g.setColor(Color.BLACK);
		for (Room r : labState.getLab().getRooms()) {
			drawRoom(g, r);
		}
		drawCorridors(g);
	}

	public BufferedImage getImage(boolean rotate) {
		int rotation = rotate ? rand.nextInt(5) : 0;
		BufferedImage unRotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		paint((Graphics2D)unRotated.getGraphics());
		int neww = rotation % 2 == 0 ?  w : h;
		int newh = rotation % 2 == 0 ? h : w;
		BufferedImage img = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.translate((neww-w)/2, (newh-h)/2);
    g.rotate(Math.PI/2 * rotation, w/2.0, h/2.0);
		g.drawRenderedImage(unRotated, null);
    g.dispose();
		return img;
	}

}
