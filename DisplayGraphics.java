import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.List;
import java.util.LinkedList;
import model.*;

public class DisplayGraphics extends JPanel{
	private Labyrinth lab;
	private int offset;
	private double scale;

	private Vector routeFrom;
	private Vector routeTo;
	private Vector selected;
	private List<Vector> selectedReach;

	private Storable stuff;

	public DisplayGraphics(Labyrinth laby, int offs, int sc) {
		lab = laby;
		offset = offs;
		scale = sc;

		routeFrom = null;
		routeTo = null;
		selected = null;
		selectedReach = new LinkedList<>();

		stuff = new Storable(lab, new Vector(12, 12));
	}

	private int labPosToPx(double p){
		return offset + (int)((p + 2/scale) * scale);
	}

	private double pxToLabPos(int px){
		return ((int)(px - offset)) / scale;
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

	private void drawLab(Graphics2D g){
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

	private double calculateCorridorWidth(){
		List<double[]> nodePoly = lab.getNodePoly(new Vector(0,0));
		double dx = nodePoly.get(0)[0] - nodePoly.get(1)[0];
		double dy = nodePoly.get(0)[1] - nodePoly.get(1)[1];
		return Math.sqrt(dx*dx + dy*dy);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		setBackground(Color.BLACK);
		g2.setStroke(new BasicStroke((float)(scale * calculateCorridorWidth()+ 1)));
		g2.setColor(Color.WHITE);
		for (Room r : lab.getRooms()) {
			drawRoom(g2, r);
		}
		drawLab(g2);

		g2.setStroke(new BasicStroke((float)(scale/20)));
		if (routeTo != null && routeFrom != null) {
			g2.setColor(Color.GREEN);
			List<Vector> route = lab.findRoute(routeFrom, routeTo);
			int[] xpos = route.stream().mapToInt(v -> labPosToPx(lab.xPosition(v))).toArray();
			int[] ypos = route.stream().mapToInt(v -> labPosToPx(lab.yPosition(v))).toArray();
			g2.drawPolyline(xpos, ypos, route.size());
		}
		if(selected != null) {
			g2.setColor(Color.DARK_GRAY);
			drawCell(g2, selected);
			g2.setColor(Color.GRAY);
			selectedReach.stream().forEach(c -> drawCell(g2, c));
		}
		g2.setColor(Color.RED);
		int x = labPosToPx(stuff.getXPos());
		int y = labPosToPx(stuff.getYPos());
		g2.fillOval(x-3, y-3, 6, 6);
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
			selectedReach = lab.inReachOf(pxToLabPos(x), pxToLabPos(y));
			repaint();
		}
		double dx = pxToLabPos(x) - stuff.getXPos();
		double dy = pxToLabPos(y) - stuff.getYPos();
		stuff.setPosition(stuff.getXPos() + dx/10, stuff.getYPos() + dy/10);
	}

	public static void main(String[] args) {
		Labyrinth lab = new HexaLab(20, 20, 0.3, new ConvexRoomFinder(3));
		lab.changeNTimes(1000);
		lab.coverWithRooms();
		DisplayGraphics m = new DisplayGraphics(lab, 60, 30);
		JFrame frame = new JFrame();
		frame.add(m);
		frame.setSize(900,800);
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
