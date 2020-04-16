package wblut.geom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import wblut.math.WB_Epsilon;

public class WB_MutableCoordinate2DTest {
	private WB_MutableCoordinate2D p, q;
	private final double eps = WB_Epsilon.EPSILON;

	@Before
	public void setUp() throws Exception {
		p = new WB_MutableCoordinate2D(2, 8);
		q = new WB_MutableCoordinate2D(-4, 12);
	}

	@Test
	public void testGetd() {
		assertEquals(2, p.getd(0), eps);
		assertEquals(8, p.getd(1), eps);
		boolean isNan = Double.isNaN(p.getd(2));
		assertTrue(isNan);
		isNan = Double.isNaN(p.getd(3));
		assertTrue(isNan);
		isNan = Double.isNaN(p.getd(-1));
		assertTrue(isNan);
		isNan = Double.isNaN(p.getd(4));
		assertTrue(isNan);
	}

	@Test
	public void testGetf() {
		assertEquals(2, p.getf(0), eps);
		assertEquals(8, p.getf(1), eps);
		boolean isNan = Float.isNaN(p.getf(2));
		assertTrue(isNan);
		isNan = Float.isNaN(p.getf(3));
		assertTrue(isNan);
		isNan = Float.isNaN(p.getf(-1));
		assertTrue(isNan);
		isNan = Float.isNaN(p.getf(4));
		assertTrue(isNan);
	}

	@Test
	public void testXd() {
		assertEquals(2, p.xd(), eps);
	}

	@Test
	public void testYd() {
		assertEquals(8, p.yd(), eps);
	}

	@Test
	public void testZd() {
		assertEquals(0, p.zd(), eps);
	}

	@Test
	public void testWd() {
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testXf() {
		assertEquals(2, p.xf(), eps);
	}

	@Test
	public void testYf() {
		assertEquals(8, p.yf(), eps);
	}

	@Test
	public void testZf() {
		assertEquals(0, p.zf(), eps);
	}

	@Test
	public void testWf() {
		assertEquals(0, p.wf(), eps);
	}

	@Test
	public void testSetX() {
		p.setX(4.0);
		assertEquals(4, p.xd(), eps);
	}

	@Test
	public void testSetY() {
		p.setY(4.0);
		assertEquals(4, p.yd(), eps);
	}

	@Test
	public void testSetZ() {
		p.setZ(4.0);
		assertEquals(0, p.zd(), eps);
	}

	@Test
	public void testSetW() {
		p.setW(4.0);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testSetCoord() {
		p.setCoord(0, 3.0);
		assertEquals(3, p.xd(), eps);
		p.setCoord(1, 9.0);
		assertEquals(9, p.yd(), eps);
		p.setCoord(2, 4.0);
		assertEquals(0, p.zd(), eps);
		p.setCoord(3, 4.0);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testSetWB_Coord() {
		p.set(q);
		assertEquals(-4, p.xd(), eps);
		assertEquals(12, p.yd(), eps);
		assertEquals(0, p.zd(), eps);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testSetDoubleDouble() {
		p.set(-4, 5);
		assertEquals(-4, p.xd(), eps);
		assertEquals(5, p.yd(), eps);
		assertEquals(0, p.zd(), eps);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testSetDoubleDoubleDouble() {
		p.set(-4, 5, 7);
		assertEquals(-4, p.xd(), eps);
		assertEquals(5, p.yd(), eps);
		assertEquals(0, p.zd(), eps);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testSetDoubleDoubleDoubleDouble() {
		p.set(-4, 5, 7, 9);
		assertEquals(-4, p.xd(), eps);
		assertEquals(5, p.yd(), eps);
		assertEquals(0, p.zd(), eps);
		assertEquals(0, p.wd(), eps);
	}

	@Test
	public void testCompareTo() {
		assertEquals(1, p.compareTo(q));
		assertEquals(-1, q.compareTo(p));
		assertEquals(0, p.compareTo(p));
		q.set(p);
		assertEquals(0, p.compareTo(q));
	}

	@Test
	public void testCompareToY1st() {
		assertEquals(-1, p.compareToY1st(q));
		assertEquals(1, q.compareToY1st(p));
		assertEquals(0, p.compareToY1st(p));
		q.set(p);
		assertEquals(0, p.compareToY1st(q));
	}

	@Test
	public void testEqualsObject() {
		assertTrue(p.equals(p));
		assertFalse(p.equals(null));
		assertFalse(p.equals(q));
		q.set(p);
		assertTrue(p.equals(q));
		assertTrue(p.equals(new WB_MutableCoordinate3D(2, 8, 0)));
		assertFalse(p.equals(new WB_MutableCoordinate3D(2, 8, 7)));
	}
}
