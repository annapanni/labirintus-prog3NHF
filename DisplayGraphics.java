import java.awt.*;
import javax.swing.JFrame;

import java.util.List;
import model.*;

public class DisplayGraphics extends Canvas{
	private Labyrinth lab;
	private int offset;
	private int corridorWidth;
	private int scale;
	private Color color;

	public DisplayGraphics(Labyrinth laby, int offs, int cw, int sc, Color col) {
		lab = laby;
		offset = offs;
		corridorWidth = cw;
		scale = sc;
		color = col;
	}

	private int labPosToPx(double p){
		return offset + (int)(p*scale);
	}

	private void drawRoom(Graphics g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
		int[] ypos = border.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
	}

	public void paint(Graphics g) {
		setBackground(Color.BLACK);
		setForeground(color);
		for (Room r : lab.getRooms()) {
			drawRoom(g, r);
		}

		for (int y=0; y < lab.getHeight(); y++){
			for (int x=0; x < lab.getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (! lab.inBound(idx)) {
					continue;
				}
				int centerX = labPosToPx(lab.xPosition(idx));
				int centerY = labPosToPx(lab.yPosition(idx));
        		g.fillOval(centerX - corridorWidth/4, centerY - corridorWidth/4, corridorWidth/2, corridorWidth/2);
				int endX = labPosToPx(lab.xPosition(idx.plus(lab.getDir(idx))));
				int endY = labPosToPx(lab.yPosition(idx.plus(lab.getDir(idx))));
        		g.drawLine(centerX, centerY, endX, endY);
			}
		}

		g.setColor(Color.RED);
		List<Vector> route = lab.findRoute(new Vector(5, 5), new Vector(10, 15));
		int[] xpos = route.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
		int[] ypos = route.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
		g.drawPolyline(xpos, ypos, route.size());
	}

	public static void main(String[] args) {
		Labyrinth lab = new RectLab(20, 20, new ConvexRoomFinder(3));
		lab.changeNTimes(500);
		lab.coverWithRooms();
		DisplayGraphics m = new DisplayGraphics(lab, 10, 15, 20, Color.WHITE);
		JFrame frame = new JFrame();
		frame.add(m);
		frame.setSize(600,600);
		frame.setVisible(true);
	}
}
