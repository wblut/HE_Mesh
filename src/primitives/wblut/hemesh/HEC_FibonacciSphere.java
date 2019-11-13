/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
// http://extremelearning.com.au/evenly-distributing-points-on-a-sphere/
package wblut.hemesh;

import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

/**
 * Dodecahedron.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FibonacciSphere extends HEC_Creator {
	/** Outer Radius. */
	private double				R;
	private int					N;
	//private static final double	PHI	= 0.5 * (1.0 + Math.sqrt(5.0));

	/**
	 * Instantiates a new dodecahedron.
	 *
	 */
	public HEC_FibonacciSphere() {
		super();
		R = 1.0;
		N = 100;
	}

	public HEC_FibonacciSphere setRadius(final double R) {
		this.R = R;
		return this;
	}

	public HEC_FibonacciSphere setN(final int N) {
		this.N = N;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		if (N < 4) {
			return new HE_Mesh();
		}
		if (WB_Epsilon.isZero(R)) {
			return new HE_Mesh();
		}
		WB_Point[] points = new WB_Point[N];
		for (int i = 0; i < N; i++) {
			points[i] = new WB_Point(Math.acos(1 - 2 * (i + 0.5) / N),
					Math.PI * (1.0 + Math.sqrt(5.0)) * (i + 0.5));
		}
		for (int i = 0; i < N; i++) {
			points[i] = gf.createPointFromSpherical(R, points[i].xd(),
					points[i].yd());
		}
		HEC_Creator creator = new HEC_ConvexHull().setPoints(points);
		return creator.createBase();
	}
}
