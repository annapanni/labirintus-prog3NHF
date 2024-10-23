import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import model.*;

public class DisplayGraphics extends JPanel{
	private Labyrinth lab;
	private int offset;
	private int corridorWidth;
	private int scale;
	private Color color;

	private Vector routeFrom;
	private Vector routeTo;
	private Vector selected;

	public DisplayGraphics(Labyrinth laby, int offs, int cw, int sc, Color col) {
		lab = laby;
		offset = offs;
		corridorWidth = cw;
		scale = sc;
		color = col;

		routeFrom = null;
		routeTo = null;
		selected = null;
	}

	private int labPosToPx(double p){
		return offset + (int)(p*scale);
	}

	private double pxToLabPos(int px){
		return (double)(px - offset + (double)scale/2) / scale;
	}

	private void drawRoom(Graphics g, Room r){
		List<Vector> border = r.getBorderPoly();
		int[] xpos = border.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
		int[] ypos = border.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
	}

	private void drawLab(Graphics g){
		for (int y=0; y < lab.getHeight(); y++){
			for (int x=0; x < lab.getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (! lab.inBound(idx)) {
					continue;
				}
				List<double[]> xyLabPos = lab.getNodePoly(idx);
				int[] xpos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[0])).toArray();
				int[] ypos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[1])).toArray();
        g.fillPolygon(xpos, ypos, xpos.length);
				int centerX = labPosToPx(lab.xPosition(idx));
				int centerY = labPosToPx(lab.yPosition(idx));
				int endX = labPosToPx(lab.xPosition(idx.plus(lab.getDir(idx))));
				int endY = labPosToPx(lab.yPosition(idx.plus(lab.getDir(idx))));
        g.drawLine(centerX, centerY, endX, endY);
			}
		}
	}

	private void handleClick(int x, int y){
		Vector vclick = lab.posToVec(pxToLabPos(x), pxToLabPos(y));
		if (! lab.inBound(vclick)) {return;}
		if (routeTo != null || routeFrom == null) {
			routeFrom = vclick;
			routeTo = null;
		} else {
			routeTo = vclick;
		}
		repaint();
	}

	private void handleMouseMove(int x, int y) {
		Vector vm = lab.posToVec(pxToLabPos(x), pxToLabPos(y));
		if (lab.inBound(vm) && selected != vm){
			selected = vm;
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		setBackground(Color.BLACK);
		setForeground(color);
		for (Room r : lab.getRooms()) {
			drawRoom(g, r);
		}
		drawLab(g);

		if (routeTo != null && routeFrom != null) {
			g.setColor(Color.RED);
			List<Vector> route = lab.findRoute(routeFrom, routeTo);
			int[] xpos = route.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
			int[] ypos = route.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
			g.drawPolyline(xpos, ypos, route.size());
		}
		if(selected != null) {
			g.setColor(Color.BLUE);
			List<double[]> xyLabPos = lab.getNodePoly(selected);
			int[] xpos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[0])).toArray();
			int[] ypos = xyLabPos.stream().mapToInt(p -> labPosToPx(p[1])).toArray();
			g.fillPolygon(xpos, ypos, xpos.length);
		}
	}

	public static void main(String[] args) {
		Labyrinth lab = new HexaLab(20, 20, new ConvexRoomFinder(3));
		lab.changeNTimes(500);
		lab.coverWithRooms();
		DisplayGraphics m = new DisplayGraphics(lab, 10, 15, 30, Color.WHITE);
		JFrame frame = new JFrame();
		frame.add(m);
		frame.setSize(600,600);
		frame.setVisible(true);


		frame.addWindowListener(new WindowAdapter() {
    	public void windowClosing(WindowEvent windowEvent){
          System.exit(0);
       }
    });

		m.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) {}
      public void mouseEntered(MouseEvent e) {}

      public void mousePressed(MouseEvent e) {
				m.handleClick(e.getX(), e.getY());
			}
		});

		m.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {}
      public void mouseMoved(MouseEvent e) {
				m.handleMouseMove(e.getX(), e.getY());
			}
		});

	}
}
