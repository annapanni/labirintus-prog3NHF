import java.awt.*;
import javax.swing.JFrame;

import model.*;

public class DisplayGraphics extends Canvas{
	public Labyrinth lab;
	public int offset;
	public int corridorWidth;
	public int scale;
	public Color color;

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

  public void paint(Graphics g) {
		setBackground(Color.BLACK);
		setForeground(color);

		for (int y=0; y < lab.getHeight(); y++){
			for (int x=0; x < lab.getWidth(); x++){
				Vector idx = new Vector(x, y);
				if (! lab.inBound(idx)) {
					continue;
				}
				int centerX = labPosToPx(lab.xPosition(idx));
				int centerY = labPosToPx(lab.yPosition(idx));
        g.fillOval(centerX, centerY, corridorWidth/2, corridorWidth/2);
				int endX = labPosToPx(lab.xPosition(idx.plus(lab.getDir(idx))));
				int endY = labPosToPx(lab.yPosition(idx.plus(lab.getDir(idx))));
        g.drawLine(centerX, centerY, endX, endY);
			}
		}
  }

  public static void main(String[] args) {
		Labyrinth lab = new HexaLab(20, 20);
		lab.changeNTimes(500);
    DisplayGraphics m = new DisplayGraphics(lab, 10, 15, 20, Color.WHITE);
    JFrame frame = new JFrame();
    frame.add(m);
    frame.setSize(600,600);
    frame.setVisible(true);
  }
}
