/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.geom.WB_JTS;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Math;

public interface HET_InfoFace<E extends Object> {
	/**
	 *
	 *
	 * @param face
	 * @return
	 */
	public E retrieve(final HE_Face face);

	public static class HET_FaceNormal implements HET_InfoFace<WB_Vector> {
		/*
		 * (non-Javadoc)
		 * @see wblut.hemesh.HET_FaceInfo#retrieve(wblut.hemesh.HE_Face)
		 */
		@Override
		public WB_Vector retrieve(final HE_Face face) {
			if (face.getHalfedge() == null) {
				return null;
			}
			// calculate normal with Newell's method
			HE_Halfedge he = face.getHalfedge();
			final WB_Vector normal = new WB_Vector();
			HE_Vertex p0;
			HE_Vertex p1;
			do {
				p0 = he.getVertex();
				p1 = he.getNextInFace().getVertex();
				normal.addSelf((p0.yd() - p1.yd()) * (p0.zd() + p1.zd()),
						(p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
						(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			normal.normalizeSelf();
			return normal;
		}
	}

	public static class HET_FaceCenter implements HET_InfoFace<WB_Point> {
		/*
		 * (non-Javadoc)
		 * @see wblut.hemesh.HET_FaceInfo#retrieve(wblut.hemesh.HE_Face)
		 */
		@Override
		public WB_Point retrieve(final HE_Face face) {
			if (face.getHalfedge() == null) {
				return null;
			}
			HE_Halfedge he = face.getHalfedge();
			final WB_Point center = new WB_Point();
			int c = 0;
			do {
				center.addSelf(he.getVertex());
				c++;
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			center.divSelf(c);
			return center;
		}
	}

	public static class HET_FaceArea implements HET_InfoFace<Double> {
		/*
		 * (non-Javadoc)
		 * @see wblut.hemesh.HET_FaceInfo#retrieve(wblut.hemesh.HE_Face)
		 */
		@Override
		public Double retrieve(final HE_Face face) {
			if (face.getHalfedge() == null) {
				return 0.0;
			}
			HE_Halfedge he = face.getHalfedge();
			final WB_Vector n = new WB_Vector();
			HE_Vertex p0;
			HE_Vertex p1;
			do {
				p0 = he.getVertex();
				p1 = he.getNextInFace().getVertex();
				n.addSelf((p0.yd() - p1.yd()) * (p0.zd() + p1.zd()),
						(p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
						(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			n.normalizeSelf();
			if (WB_Vector.getLength3D(n) < 0.5) {
				return 0.0;
			}
			final double x = WB_Math.fastAbs(n.xd());
			final double y = WB_Math.fastAbs(n.yd());
			final double z = WB_Math.fastAbs(n.zd());
			double area = 0;
			int coord = 3;
			if (x >= y && x >= z) {
				coord = 1;
			} else if (y >= x && y >= z) {
				coord = 2;
			}
			he = face.getHalfedge();
			do {
				switch (coord) {
					case 1:
						area += he.getVertex().yd()
								* (he.getNextInFace().getVertex().zd()
										- he.getPrevInFace().getVertex().zd());
						break;
					case 2:
						area += he.getVertex().xd()
								* (he.getNextInFace().getVertex().zd()
										- he.getPrevInFace().getVertex().zd());
						break;
					case 3:
						area += he.getVertex().xd()
								* (he.getNextInFace().getVertex().yd()
										- he.getPrevInFace().getVertex().yd());
						break;
				}
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			switch (coord) {
				case 1:
					area *= 0.5 / x;
					break;
				case 2:
					area *= 0.5 / y;
					break;
				case 3:
					area *= 0.5 / z;
			}
			return WB_Math.fastAbs(area);
		}
	}

	public static class HET_FaceTriangles implements HET_InfoFace<int[]> {
		/*
		 * (non-Javadoc)
		 * @see wblut.hemesh.HET_FaceInfo#retrieve(wblut.hemesh.HE_Face)
		 */
		@Override
		public int[] retrieve(final HE_Face f) {
			int[] triangles;
			final int fo = f.getFaceDegree();
			if (fo < 3) {
				return new int[] { 0, 0, 0 };
			} else if (fo == 3) {
				return new int[] { 0, 1, 2 };
			} else if (f.isDegenerate()) {
				triangles = new int[3 * (fo - 2)];
				for (int i = 0; i < fo - 2; i++) {
					triangles[3 * i] = 0;
					triangles[3 * i + 1] = i + 1;
					triangles[3 * i + 2] = i + 2;
				}
			} else if (fo == 4) {
				final WB_Point[] points = new WB_Point[4];
				int i = 0;
				HE_Halfedge he = f.getHalfedge();
				do {
					points[i] = new WB_Point(he.getVertex().xd(),
							he.getVertex().yd(), he.getVertex().zd());
					he = he.getNextInFace();
					i++;
				} while (he != f.getHalfedge());
				return WB_JTS.PolygonTriangulatorJTS.triangulateQuad(points[0],
						points[1], points[2], points[3]);
			} else {
				triangles = new WB_JTS.PolygonTriangulatorJTS()
						.triangulatePolygon2D(HE_MeshOp.getPolygon(f), true)
						.getTriangles();
			}
			return triangles;
		}
	}
}