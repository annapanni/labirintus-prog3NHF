package labyrinth.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.util.stream.IntStream;

import labyrinth.model.*;

public class LabView extends JPanel {
	private LabState labState;
	private int xoffset;
	private int yoffset;
	private double scale;
	private double visiblityOverride;
	private transient Paint floorPaint;
	private transient BufferedImage mapImage;
	private boolean drawMap;

	public void setVisiblityOverride(double vo) {visiblityOverride = vo;}

	private static boolean failedToLoad = false;
	public static boolean failedToLoadTexture() {return failedToLoad;}
	private static BufferedImage charImage;
	private static BufferedImage keyImage;
	private static BufferedImage brazierImage;
	private static BufferedImage exitImage;
	private static BufferedImage fireflyImage;
	private static BufferedImage tilesImage;
	private static BufferedImage mapIconImage;

	static {
		try {
			String resourcePath = "src/main/resources/";
			charImage = ImageIO.read(new File(resourcePath + "wizard.png"));
			keyImage = ImageIO.read(new File(resourcePath + "key.png"));
			brazierImage = ImageIO.read(new File(resourcePath + "brazier.png"));
			exitImage = ImageIO.read(new File(resourcePath + "exit.png"));
			fireflyImage = ImageIO.read(new File(resourcePath + "firefly.png"));
			tilesImage = ImageIO.read(new File(resourcePath + "tiles.jpg"));
			mapIconImage = ImageIO.read(new File(resourcePath + "map.png"));
		} catch (IOException e) {
			failedToLoad = true;
		}
	}

	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					yoffset += 10; break;
				case KeyEvent.VK_DOWN:
					yoffset -= 10; break;
				case KeyEvent.VK_LEFT:
					xoffset += 10; break;
				case KeyEvent.VK_RIGHT:
					xoffset -= 10; break;
				case KeyEvent.VK_C:
					center(); break;
				case KeyEvent.VK_M:
					drawMap = !drawMap; break;
				default:
			}
			if (! failedToLoad) {
				floorPaint = new TexturePaint(tilesImage, new Rectangle(xoffset, yoffset, (int)scale, (int)scale));
			}
    }
	}

	public LabView(LabState laby, int sc, double vo) {
		scale = sc;
		visiblityOverride = vo;
		setLabState(laby);
		setPreferredSize(new Dimension(800, 600));
		center();
		addKeyListener(new KeyHandler());
		addMouseWheelListener(e -> {
			double os = scale;
			scale = scale * (1 - 0.05*e.getWheelRotation());
			double plx = labState.getPlayer().getXPos();
			double ply = labState.getPlayer().getYPos();
			xoffset += (int)(plx * os - plx * scale);
			yoffset += (int)(ply * os - ply * scale);
			if (! failedToLoad) floorPaint = new TexturePaint(tilesImage, new Rectangle(xoffset, yoffset, (int)scale, (int)scale));
		});
		if (! failedToLoad) {
			tilesImage.getGraphics().translate(xoffset, yoffset);
			floorPaint = new TexturePaint(tilesImage, new Rectangle(xoffset, yoffset, (int)scale, (int)scale));
		}
	}

	public LabView(LabState laby, int sc) {
		this(laby, sc, -1);
	}

	public int xlabPosToPx(double p){
		return xoffset + (int)((p + 2/scale) * scale);
	}

	public int ylabPosToPx(double p){
		return yoffset + (int)((p + 2/scale) * scale);
	}

	public double xpxToLabPos(int px){
		return (px - xoffset) / scale;
	}

	public double ypxToLabPos(int px){
		return (px - yoffset) / scale;
	}

	public void setLabState(LabState ls) {
		labState = ls;
		center();
		mapImage = (new MapView(labState, 10)).getImage(true);
	}

	private void center(){
		xoffset = 0; yoffset = 0;
		int px = xlabPosToPx(labState.getPlayer().getXPos());
		int py = ylabPosToPx(labState.getPlayer().getYPos());
		int screenWidth = (int)getSize().getWidth();
		int screenHeight = (int)getSize().getHeight();
		xoffset = screenWidth/2 - px;
		yoffset = screenHeight/2 - py;
	}

	private double calculateCorridorWidth(){
		List<double[]> nodePoly = labState.getLab().getNodePoly(new Vector(0,0));
		double dx = nodePoly.get(0)[0] - nodePoly.get(1)[0];
		double dy = nodePoly.get(0)[1] - nodePoly.get(1)[1];
		return Math.sqrt(dx*dx + dy*dy);
	}

	private Color decodeColor(ModelColor c) {
		switch (c) {
			case YELLOW:
				return Color.YELLOW;
			case RED:
				return Color.RED;
			case WHITE:
				return Color.WHITE;
			case BLACK:
				return Color.BLACK;
			case FIREFLY:
				return new Color(210, 240, 30);
			case ORANGE:
				return new Color(255, 180, 20, 255);
			case BLUE:
				return Color.BLUE;
			default:
				return new Color(0,0,0,0);
			}
	}

	private Color withMaxOpacity(Color c, int opacity) {
		int op = opacity > c.getAlpha() ? c.getAlpha() : opacity;
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), op);
	}

	private void drawCell(Graphics2D g, Vector idx) {
		List<double[]> xyLabPos = labState.getLab().getNodePoly(idx);
		int[] xpos = xyLabPos.stream().mapToInt(p -> xlabPosToPx(p[0])).toArray();
		int[] ypos = xyLabPos.stream().mapToInt(p -> ylabPosToPx(p[1])).toArray();
		g.fillPolygon(xpos, ypos, xpos.length);
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
				drawCell(g, idx);
				int centerX = xlabPosToPx(labState.getLab().xPosition(idx));
				int centerY = ylabPosToPx(labState.getLab().yPosition(idx));
				int endX = xlabPosToPx(labState.getLab().xPosition(idx.plus(labState.getLab().getDir(idx))));
				int endY = ylabPosToPx(labState.getLab().yPosition(idx.plus(labState.getLab().getDir(idx))));
				g.drawLine(centerX, centerY, endX, endY);
			}
		}
	}

	private void drawImageAt(Graphics2D g, Image img, int x, int y, double sizeModifier) {
		int h = img.getHeight(null);
		int w = img.getWidth(null);
		double sc = scale *(1 - labState.getLab().getPadding()) / (h > w ? h : w);
		int nh = (int)(h*sc*sizeModifier);
		int nw = (int)(w*sc*sizeModifier);
		g.drawImage(img, x - nw/2, y - nh/2, nw, nh, null);
	}

	private void drawObject(Graphics2D g, Storable obj){
		int x = xlabPosToPx(obj.getXPos());
		int y = ylabPosToPx(obj.getYPos());
		if (failedToLoad) {
			switch (obj.getSprite()) {
				case ModelSprite.CHARACTER:
					g.setColor(Color.RED);
					g.fillOval(x-3, y-3, 6, 6); break;
				case ModelSprite.KEY:
					g.setColor(Color.BLACK);
					g.fillOval(x-3, y-3, 6, 6); break;
				case ModelSprite.BRAZIER:
					g.setColor(Color.RED);
					g.fillOval(x-4, y-4, 8, 8); break;
				case ModelSprite.EXIT:
					g.setColor(Color.BLACK);
					g.fillRect(x-4, y-4, 8, 8); break;
				default:
					g.setColor(Color.BLUE);
					g.fillOval(x-3, y-3, 6, 6); break;
			}
		} else {
			switch (obj.getSprite()) {
				case ModelSprite.CHARACTER:
					drawImageAt(g, charImage, x, y, 1); break;
				case ModelSprite.KEY:
					drawImageAt(g, keyImage, x, y, 0.7); break;
				case ModelSprite.BRAZIER:
					drawImageAt(g, brazierImage, x, y, 1); break;
				case ModelSprite.EXIT:
					drawImageAt(g, exitImage, x, y, 0.6); break;
				case ModelSprite.FIREFLY:
					drawImageAt(g, fireflyImage, x, y, 0.5); break;
				case ModelSprite.MAP:
					drawImageAt(g, mapIconImage, x, y, 0.6); break;
				default:
					g.setColor(Color.BLUE);
					g.fillOval(x-3, y-3, 6, 6); break;
			}
		}
	}

	private Area getLightArea(Light l, double rad) {
		List<double[]> lightPoly = l.getLightPoly();
		int[] xpos = lightPoly.stream().mapToInt(c -> xlabPosToPx(c[0])).toArray();
		int[] ypos = lightPoly.stream().mapToInt(c -> ylabPosToPx(c[1])).toArray();
		Area light = new Area(new Polygon(xpos, ypos, xpos.length));
		if (rad != Double.POSITIVE_INFINITY) {
			Storable og = l.getOrigin();
			Area range = new Area(new Ellipse2D.Double(xlabPosToPx(og.getXPos()) - rad, ylabPosToPx(og.getYPos()) - rad, rad*2, rad*2));
			light.intersect(range);
		}
		return light;
	}

	private boolean circleInBound(int w, int h, int cx, int cy, double r){
		return cx + r > 0 && cy + r > 0 && cx - r < w && cy - r < h;
	}

	private void drawLightColors(Graphics2D g, List<Light> lights) {
		int w = (int)getSize().getWidth();
		int h = (int)getSize().getHeight();
		for (Light light : lights){
			int cx = xlabPosToPx(light.getOrigin().getXPos());
			int cy = ylabPosToPx(light.getOrigin().getYPos());
			double r = light.getRadius() * scale;
			if (circleInBound(w, h, cx, cy, r)) {
				Area lArea = getLightArea(light, light.getRadius()*scale);
				Color col = decodeColor(light.getColor());
				Paint p = new RadialGradientPaint(cx, cy, (float)r, new float[]{(float)light.getDimFrom(), 1f},
					new Color[]{withMaxOpacity(col, 150), withMaxOpacity(col, 0)});
				g.setPaint(p);
				g.fill(lArea);
			}
		}
	}

	private BufferedImage darknessImage(List<Light> lights, int w, int h) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		for (Light light : lights) {
			int cx = xlabPosToPx(light.getOrigin().getXPos());
			int cy = ylabPosToPx(light.getOrigin().getYPos());
			double r = light.getRadius() * scale;
			if (circleInBound(w, h, cx, cy, r)) {
				Area lArea = getLightArea(light, light.getRadius()*scale);
				Paint p = new RadialGradientPaint(cx, cy, (float)r, new float[]{(float)light.getDimFrom(), 1f},
					new Color[]{new Color(255, 255, 255, 255), new Color(255, 255, 255, 0)});
				g.setPaint(p);
				g.fill(lArea);
			}
		}

		IntStream.range(0, image.getWidth()).parallel().forEach( x ->
			IntStream.range(0, image.getHeight()).forEach( y -> {
				int col = image.getRGB(x, y);
				int newAlpha = (256 - col & 0xff) << 24;
				int newCol = (col & 0xff000000) + newAlpha;
				image.setRGB(x, y, newCol);
			})
		);
		return image;
	}

	private void drawStatus(Graphics2D g) {
		g.setColor(Color.WHITE);
		int screenWidth = (int)getSize().getWidth();
		String ffStatus = (labState.getFireflyNum() - labState.getUsedFireflyNum()) + "/" + labState.getFireflyNum();
		long kNum = labState.getKeys().count();
		String keyStatus = (kNum - labState.getUncollectedKeyNum()) + "/" + kNum;
		int padd1 = g.getFontMetrics().stringWidth(keyStatus) + g.getFontMetrics().stringWidth(keyStatus) + 45;
		int padd2 = g.getFontMetrics().stringWidth(keyStatus) + 5;
		g.drawString(keyStatus, screenWidth - padd1, 20);
		g.drawString(ffStatus, screenWidth - padd2, 20);
		if (! failedToLoad) {
			double kw = 30.0 / keyImage.getWidth();
			g.drawImage(keyImage, screenWidth - padd1 - 30, 0, (int)(keyImage.getWidth() * kw), (int)(keyImage.getHeight() * kw), null);
			double fw = 30.0 / fireflyImage.getWidth();
			g.drawImage(fireflyImage, screenWidth - padd2 - 30, 0, (int)(fireflyImage.getWidth() * fw), (int)(fireflyImage.getHeight() * fw), null);
		}
	}

	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D)g1;
		int screenWidth = (int)getSize().getWidth();
		int screenHeight = (int)getSize().getHeight();
		setBackground(Color.BLACK);
		g.setStroke(new BasicStroke((float)(scale * calculateCorridorWidth()+ 1)));
		if (failedToLoad) g.setColor(Color.WHITE);
		else g.setPaint(floorPaint);
		for (Room r : labState.getLab().getRooms()) {
			drawRoom(g, r);
		}
		drawCorridors(g);
		List<Light> lights = labState.getObjects().stream().map(Storable::getLight).filter(l->l!=null).toList();
		drawLightColors(g, lights);
		for (Storable obj : labState.getObjects()){
			drawObject(g, obj);
		}
		g.setColor(new Color(0, 0, 0, 255));
		double op = visiblityOverride >= 0 ? visiblityOverride : labState.getdarknessOpacity();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)op));
		BufferedImage darkness = darknessImage(lights, screenWidth, screenHeight);
		g.drawImage(darkness, 0, 0, null);
		Light sRange =  labState.getLineOfSight();
		if (sRange != null && visiblityOverride < 0) {
			Area sightDarkness = new Area(new Rectangle(screenWidth, screenHeight));
			sightDarkness.subtract(getLightArea(sRange, Double.POSITIVE_INFINITY));
			g.fill(sightDarkness);
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if (labState.getMapCollected() && drawMap) {
			g.drawImage(mapImage, 5, 5, null);
		}
		drawStatus(g);
	}

}
