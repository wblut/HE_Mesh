/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 * Dodecahedron.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_Waterman extends HEC_Creator {
	/** Outer Radius. */
	private double R;

	private int root;
	private int c;
	private final static WB_Point[] centers = new WB_Point[] { new WB_Point(), new WB_Point(0.5, 0.5, 0.0),
			new WB_Point(1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0), new WB_Point(1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0),
			new WB_Point(0.5, 0.5, 0.5), new WB_Point(0.0, 0.0, 0.5), new WB_Point(1.0, 0.0, 0.0) };

	/**
	 * Instantiates a new dodecahedron.
	 *
	 */
	public HEC_Waterman() {
		super();
		R = 1.0;
		root = 2;
		c = 0;
	}

	/**
	 * Set radius circumscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Waterman setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 * Set radius circumscribed sphere.
	 *
	 * @param R
	 *            radius
	 * @return self
	 */
	public HEC_Waterman setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_Waterman setRoot(final int root) {
		this.root = root;
		return this;
	}

	public HEC_Waterman setCenter(final int c) {
		this.c = c;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		if (root < 1) {
			return new HE_Mesh();
		}
		if (WB_Epsilon.isZero(R)) {
			return new HE_Mesh();
		}

		WB_Point center = centers[c];
		double radius2;
		double radius;
		switch (c) {
		case 0:
			radius2 = 2 * root;
			radius = Math.sqrt(radius2);
			break;
		case 1:
			radius = 2 + 4 * root;
			radius = 0.5 * Math.sqrt(radius);
			break;
		case 2:
			radius = 6 * (root + 1);
			radius = Math.sqrt(radius) / 3.0;
			break;
		case 3:
			radius = 3 + 6 * root;
			radius = Math.sqrt(radius) / 3.0;
			break;
		case 4:
			radius = 3 + 8 * (root - 1);
			radius = 0.5 * Math.sqrt(radius);
			break;
		case 5:
			radius = 1 + 4 * root;
			radius = 0.5 * Math.sqrt(radius);
			break;
		case 6:
			radius = 1 + 2 * (root - 1);
			radius = Math.sqrt(radius);
			break;

		default:
			radius = 2 * root;
			radius = Math.sqrt(radius);
			break;
		}
		radius2 = (radius + WB_Epsilon.EPSILON) * (radius + WB_Epsilon.EPSILON);
		double scale = (float) (R / radius);
		int IR = (int) (radius + 1);

		List<WB_Point> points = new FastList<WB_Point>();
		double R2x, R2y, R2;
		for (int i = -IR; i <= IR; i++) {
			R2x = (i - center.xd()) * (i - center.xd());
			if (R2x > radius2) {
				continue;
			}
			for (int j = -IR; j <= IR; j++) {
				R2y = R2x + (j - center.yd()) * (j - center.yd());
				if (R2y > radius2) {
					continue;
				}
				for (int k = -IR; k <= IR; k++) {
					if ((i + j + k) % 2 == 0) {
						R2 = R2y + (k - center.zd()) * (k - center.zd());
						if (R2 <= radius2 && R2 > radius2 - 400) {
							points.add(new WB_Point(i, j, k).mulSelf(scale));
						}
					}
				}
			}
		}
		HEC_Creator creator = new HEC_ConvexHull().setPoints(points);
		return creator.createBase();
	}
}
