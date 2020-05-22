package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_Point;
import wblut.geom.WB_PointList;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEC_Waterman extends HEC_Creator {
	/**  */
	private double R;
	/**  */
	private int root;
	/**  */
	private int c;
	/**  */
	private final static WB_Point[] centers = new WB_Point[] { new WB_Point(), new WB_Point(0.5, 0.5, 0.0),
			new WB_Point(1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0), new WB_Point(1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0),
			new WB_Point(0.5, 0.5, 0.5), new WB_Point(0.0, 0.0, 0.5), new WB_Point(1.0, 0.0, 0.0) };

	/**
	 *
	 */
	public HEC_Waterman() {
		super();
		R = 1.0;
		root = 2;
		c = 0;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Waterman setOuterRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param R
	 * @return
	 */
	public HEC_Waterman setRadius(final double R) {
		this.R = R;
		return this;
	}

	/**
	 *
	 *
	 * @param root
	 * @return
	 */
	public HEC_Waterman setRoot(final int root) {
		this.root = root;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEC_Waterman setCenter(final int c) {
		this.c = c;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		if (root < 1) {
			return new HE_Mesh();
		}
		if (WB_Epsilon.isZero(R)) {
			return new HE_Mesh();
		}
		final WB_Point center = centers[c];
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
		final double scale = (float) (R / radius);
		final int IR = (int) (radius + 1);
		final List<WB_Point> points = new WB_PointList();
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
		final HEC_Creator creator = new HEC_ConvexHull().setPoints(points);
		return creator.createBase();
	}
}
