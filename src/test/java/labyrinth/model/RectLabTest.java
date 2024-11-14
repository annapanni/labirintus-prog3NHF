package labyrinth.model;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class RectLabTest {
	RectLab laby;

	@BeforeEach
	public void setup() {
		laby = new RectLab(10, 8, 0.3, null);
	}

	@Test
	void neighbourTest() {
		Vector midPoint = new Vector(5, 5);
		List<Vector> all5 = laby.getAllNeighbours(midPoint);
		assertEquals(List.of(new Vector(4, 5), new Vector(4, 4), new Vector(5, 4),
			new Vector(6, 4), new Vector(6, 5), new Vector(6, 6),  new Vector(5, 6), new Vector(4, 6)),
			all5);
		List<Vector> valid5 = laby.getValidNeighbours(midPoint);
		assertEquals(List.of(new Vector(4, 5), new Vector(5, 4),
			new Vector(6, 5),  new Vector(5, 6)), valid5);
		List<Vector> allCorner = laby.getAllNeighbours(laby.lastValidIdx());
		assertEquals(8, allCorner.size());
		List<Vector> validCorner = laby.getValidNeighbours(laby.lastValidIdx());
		assertEquals(2, validCorner.size());
		Vector parent5 = midPoint.plus(laby.getDir(midPoint));
		assertTrue(laby.getChildren(parent5).contains(midPoint));
	}

	@Test
	void positionTests() {
		Vector i1 = new Vector(2, 3);
		Vector i2 = new Vector(8, 5);
		assertEquals(40, laby.getDist2Between(i1, i2));
		double x1 = laby.xPosition(i1);
		assertEquals(2, x1);
		double y1 = laby.yPosition(i1);
		assertEquals(3, y1);
		double x2 = laby.xPosition(i2);
		assertEquals(8, x2);
		double y2 = laby.yPosition(i2);
		assertEquals(5, y2);
		assertEquals(laby.posToVec(x1, y1), i1);
		assertEquals(laby.posToVec(x2, y2), i2);
	}

	@Test
	void directionTest() {
		assertEquals(new Vector(0,0), laby.getDir(laby.getRoot()));
		Vector ndir = new Vector(5, 5);
		Vector idx = new Vector(4, 3);
		laby.setDir(idx, ndir);
		assertEquals(ndir, laby.getDir(idx));
	}

	@Test
	void boundaryTest() {
		assertTrue(laby.inBound(new Vector(4, 4)));
		assertFalse(laby.inBound(new Vector(-1, 0)));
		assertFalse(laby.onBound(new Vector(6, 6)));
		assertTrue(laby.onBound(new Vector(0, 4)));
		assertTrue(laby.onBound(new Vector(9, 7)));
		assertTrue(laby.onBound(new Vector(9, 3)));
		assertTrue(laby.onBound(new Vector(0, 0)));
		assertEquals(new Vector(9, 7), laby.lastValidIdx());
	}

	@Test
	void paddingTest(){
		assertEquals(0.3, laby.getPadding());
		Vector idx = new Vector(4, 4);
		List<double[]> poly = laby.getNodePoly(idx);
		double dx = poly.get(0)[0] - laby.xPosition(idx);
		double dy = poly.get(0)[1] - laby.yPosition(idx);
		double distFromCenter = Math.sqrt(dx*dx + dy*dy);
		assertEquals(1 - laby.getPadding(), Math.sqrt(2) * distFromCenter, 0.00001);
	}


}
