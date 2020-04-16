package wblut.hemesh;

import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

public class HEC_Antiprism extends HEC_Creator {
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	public HEC_Antiprism() {
		super();
		parameters.set("facets", 5);
		parameters.set("height", 100.0);
		parameters.set("radius", 100.0);
		parameters.set("topcap", true);
		parameters.set("bottomcap", true);
	}

	public HEC_Antiprism(final int n, final double r, final double d) {
		this();
		parameters.set("facets", n);
		parameters.set("height", d);
		parameters.set("radius", r);
		parameters.set("topcap", true);
		parameters.set("bottomcap", true);
	}

	public HEC_Antiprism setFacets(final int n) {
		parameters.set("facets", n);
		return this;
	}

	public HEC_Antiprism setHeight(final double d) {
		parameters.set("height", d);
		return this;
	}

	public HEC_Antiprism setRadius(final double r) {
		parameters.set("radius", r);
		return this;
	}

	public HEC_Antiprism setTopCap(final boolean b) {
		parameters.set("topcap", b);
		return this;
	}

	public HEC_Antiprism setBottomCap(final boolean b) {
		parameters.set("bottomcap", b);
		return this;
	}

	int getFacets() {
		return parameters.get("facets", 0);
	}

	double getHeight() {
		return parameters.get("height", 0.0);
	}

	double getRadius() {
		return parameters.get("radius", 0.0);
	}

	boolean getTopCap() {
		return parameters.get("topcap", true);
	}

	boolean getBottomCap() {
		return parameters.get("bottomcap", true);
	}

	@Override
	protected HE_Mesh createBase() {
		final int facets = getFacets();
		final double radius = getRadius();
		final double height = getHeight();
		final boolean topCap = getTopCap();
		final boolean bottomCap = getBottomCap();
		if (facets < 3 || WB_Epsilon.isZero(radius)) {
			return null;
		}
		final WB_Point[] ppoints = new WB_Point[facets];
		for (int i = 0; i < facets; i++) {
			final double x = radius * Math.cos(Math.PI * 2.0 / facets * i);
			final double y = radius * Math.sin(Math.PI * 2.0 / facets * i);
			ppoints[i] = new WB_Point(x, y, 0.0);
		}
		final WB_Polygon polygon = gf.createSimplePolygon(ppoints);
		final WB_Vector norm = polygon.getPlane().getNormal();
		final int n = polygon.getNumberOfPoints();
		final boolean surf = WB_Epsilon.isZero(height);
		final WB_Coord[] points = new WB_Coord[surf ? n : 2 * n];
		for (int i = 0; i < n; i++) {
			points[i] = polygon.getPoint(i);
		}
		if (!surf) {
			for (int i = 0; i < n; i++) {
				points[n + i] = new WB_Point(points[i]).addMulSelf(height, norm).rotateAboutAxis2PSelf(Math.PI / facets,
						0, 0, 0, 0, 0, 1);
			}
		}
		int[][] faces;
		if (surf) {
			faces = new int[1][n];
			for (int i = 0; i < n; i++) {
				faces[0][i] = i;
			}
		} else {
			faces = new int[2 * n + (topCap ? 1 : 0) + (bottomCap ? 1 : 0)][];
			if (bottomCap || topCap) {
				faces[2 * n] = new int[n];
			}
			if (bottomCap && topCap) {
				faces[2 * n + 1] = new int[n];
			}
			for (int i = 0; i < n; i++) {
				if (bottomCap) {
					faces[2 * n][i] = i;
				}
				if (topCap) {
					faces[2 * n + (bottomCap ? 1 : 0)][i] = 2 * n - 1 - i;
				}
				faces[2 * i] = new int[3];
				faces[2 * i][0] = i;
				faces[2 * i][1] = n + i;
				faces[2 * i][2] = (i + 1) % n;
				faces[2 * i + 1] = new int[3];
				faces[2 * i + 1][0] = i + n;
				faces[2 * i + 1][2] = (i + 1) % n;
				faces[2 * i + 1][1] = n + (i + 1) % n;
			}
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setFaces(faces).setCheckDuplicateVertices(false);
		return HE_MeshOp.flipFaces(fl.createBase());
	}
}
