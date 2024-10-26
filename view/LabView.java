package view;

import java.awt.*;
import java.awt.geom.Area;
import java.util.List;

import model.*;

public class LabView {
	private Labyrinth lab;
	private int offset;
	private double scale;
	private Vector routeFrom;
	private Vector routeTo;
	private Storable stuff;
	public Firefly fly;

	public LabView(Labyrinth laby, int offs, int sc) {
		lab = laby;
		offset = offs;
		scale = sc;
		routeFrom = null;
		routeTo = null;
		stuff = new Storable(lab, new Vector(12, 12));
	}

	private double calculateCorridorWidth(){
		List<double[]> nodePoly = lab.getNodePoly(new Vector(0,0));
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
		List<double[]> xyLabPos = lab.getNodePoly(idx);
		int[] xpos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[0])).toArray();
		int[] ypos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[1])).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
	}

	private void drawRoom(Graphics2D g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
		int[] ypos = border.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
		g.drawPolygon(xpos, ypos, xpos.length);
	}

	private void drawCorridors(Graphics2D g){
		for (int y=0; y < lab.getHeight(); y++){
			for (int x=0; x < lab.getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (! lab.inBound(idx)) {
					continue;
				}
				drawCell(g, idx);
				int centerX = labPosToPx(lab.xPosition(idx));
				int centerY = labPosToPx(lab.yPosition(idx));
				int endX = labPosToPx(lab.xPosition(idx.plus(lab.getDir(idx))));
				int endY = labPosToPx(lab.yPosition(idx.plus(lab.getDir(idx))));
				g.drawLine(centerX, centerY, endX, endY);
			}
		}
	}

	private void drawObject(Graphics g, Storable obj, Color col){
		g.setColor(col);
		int x = labPosToPx(obj.getXPos());
		int y = labPosToPx(obj.getYPos());
		g.fillOval(x-3, y-3, 6, 6);
	}

	public void drawAll(Graphics2D g) {
		g.setStroke(new BasicStroke((float)(scale * calculateCorridorWidth()+ 1)));
		g.setColor(Color.WHITE);
		for (Room r : lab.getRooms()) {
			drawRoom(g, r);
		}
		drawCorridors(g);

		g.setStroke(new BasicStroke((float)(scale/20)));
		if (routeTo != null && routeFrom != null) {
			g.setColor(Color.GREEN);
			List<Vector> route = lab.findRoute(routeFrom, routeTo);
			int[] xpos = route.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
			int[] ypos = route.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
			//g2.drawPolyline(xpos, ypos, route.size());
		}

		drawObject(g, stuff, Color.RED);
		if (fly != null) {
			drawObject(g, fly, Color.BLUE);
		}

		List<double[]> darkPoly = Darkness.darknessFrom(lab, stuff.getXPos(), stuff.getYPos());
		int[] xpos = darkPoly.stream().mapToInt(c -> labPosToPx(c[0])).toArray();
		int[] ypos = darkPoly.stream().mapToInt(c -> labPosToPx(c[1])).toArray();
		Area darkness = new Area(new Rectangle(1000, 1000));
		darkness.subtract(new Area(new Polygon(xpos, ypos, xpos.length)));
		g.setColor(new Color(0, 0, 0, 150));
		g.fill(darkness);
	}





	public void handleClick(int x, int y){
		Vector vclick = lab.posToVec(pxToLabPos(x), pxToLabPos(y));
		if (! lab.inBound(vclick)) {return;}
		if (routeTo != null || routeFrom == null) {
			routeFrom = vclick;
			routeTo = null;
		} else {
			routeTo = vclick;
			fly = new Firefly(lab, routeFrom, routeTo, 0.01);
		}
	}

 	public void handleMouseMove(int x, int y) {
		Vector vm = lab.posToVec(pxToLabPos(x), pxToLabPos(y));
		double dx = pxToLabPos(x) - stuff.getXPos();
		double dy = pxToLabPos(y) - stuff.getYPos();
		stuff.setPosition(stuff.getXPos() + dx/10, stuff.getYPos() + dy/10);
		if (fly != null) {
			boolean ended = ! fly.stepOne();
			if (ended) fly = null;
		}
	}

}
