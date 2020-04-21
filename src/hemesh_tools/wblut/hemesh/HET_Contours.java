package wblut.hemesh;

import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_List;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.math.WB_ScalarParameter;

public class HET_Contours {
	public static List<List<WB_Coord>> planarSlices(final HE_Mesh mesh, final WB_Plane P) {
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		final HE_Mesh slmesh = lmesh.get();
		final HEM_SliceSurface ss = new HEM_SliceSurface().setPlane(P);
		slmesh.modify(ss);
		final List<HE_Path> paths = ss.getPaths();
		final List<List<WB_Coord>> contours = new WB_List<>();
		for (final HE_Path path : paths) {
			final List<WB_Coord> contour = new WB_CoordList();
			contour.addAll(path.getPathVertices());
			if (path.isLoop()) {
				contour.add(path.getPathVertices().get(0));
			}
			contours.add(contour);
		}
		return contours;
	}

	public static List<List<WB_Coord>> planarSlices(final HE_Mesh mesh, final WB_Plane P, final double min,
			final double max, final double step) {
		final double start = Math.min(min, max);
		final double end = Math.max(min, max);
		final double inc = Math.abs(step);
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		final List<List<WB_Coord>> contours = new WB_List<>();
		for (double offset = start; offset <= end; offset += inc) {
			final WB_Plane offsetP = new WB_Plane(P.getOrigin().addMul(offset, P.getNormal()), P.getNormal());
			HE_Mesh slmesh = lmesh.get();
			final HEM_SliceSurface ss = new HEM_SliceSurface().setPlane(offsetP);
			slmesh.modify(ss);
			List<HE_Path> paths = ss.getPaths();
			for (final HE_Path path : paths) {
				final List<WB_Coord> contour = new WB_CoordList();
				for (final WB_Coord p : path.getPathVertices()) {
					contour.add(new WB_Point(p));
				}
				if (path.isLoop()) {
					contour.add(new WB_Point(path.getPathVertices().get(0)));
				}
				contours.add(contour);
			}
			paths = null;
			slmesh = null;
		}
		return contours;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final WB_Plane P) {
		double A, B, C, D;
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		A = P.getNormal().xd();
		B = P.getNormal().yd();
		C = P.getNormal().zd();
		D = -P.d();
		WB_Coord p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		final WB_AABBTree3D tree = new WB_AABBTree3D(lmesh, 1);
		final Iterator<HE_Face> fItr = HE_MeshOp.getPotentialIntersectedFaces(tree, P).iterator();
		while (fItr.hasNext()) {
			f = fItr.next();
			p = f.getHalfedge().getVertex();
			q = f.getHalfedge().getEndVertex();
			r = f.getHalfedge().getNextInFace().getEndVertex();
			p1 = new WB_Point();
			p2 = new WB_Point();
			side[0] = A * p.xd() + B * p.yd() + C * p.zd() + D;
			side[1] = A * q.xd() + B * q.yd() + C * q.zd() + D;
			side[2] = A * r.xd() + B * r.yd() + C * r.zd() + D;
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
				p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
				p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
				p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final WB_Plane P, final double min, final double max,
			final double step) {
		double A, B, C, D;
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		final double start = Math.min(min, max);
		final double end = Math.max(min, max);
		final double inc = Math.abs(step);
		WB_Coord p, q, r;
		WB_Point p1, p2;
		A = P.getNormal().xd();
		B = P.getNormal().yd();
		C = P.getNormal().zd();
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		final WB_AABBTree3D tree = new WB_AABBTree3D(lmesh, 1);
		for (double offset = start; offset <= end; offset += inc) {
			final WB_Plane offsetP = new WB_Plane(P.getOrigin().addMul(offset, P.getNormal()), P.getNormal());
			D = -offsetP.d();
			HE_Face f;
			final Iterator<HE_Face> fItr = HE_MeshOp.getPotentialIntersectedFaces(tree, offsetP).iterator();
			while (fItr.hasNext()) {
				f = fItr.next();
				p = f.getHalfedge().getVertex();
				q = f.getHalfedge().getEndVertex();
				r = f.getHalfedge().getNextInFace().getEndVertex();
				p1 = new WB_Point();
				p2 = new WB_Point();
				side[0] = A * p.xd() + B * p.yd() + C * p.zd() + D;
				side[1] = A * q.xd() + B * q.yd() + C * q.zd() + D;
				side[2] = A * r.xd() + B * r.yd() + C * r.zd() + D;
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
					p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
					p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
					p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final WB_ScalarParameter function, final double level) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		WB_Coord p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		final HE_FaceIterator fItr = lmesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			p = f.getHalfedge().getVertex();
			q = f.getHalfedge().getEndVertex();
			r = f.getHalfedge().getNextInFace().getEndVertex();
			p1 = new WB_Point();
			p2 = new WB_Point();
			side[0] = function.evaluate(p.xd(), p.yd(), p.zd()) - level;
			side[1] = function.evaluate(q.xd(), q.yd(), q.zd()) - level;
			side[2] = function.evaluate(r.xd(), r.yd(), r.zd()) - level;
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
				p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
				p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
				p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final WB_ScalarParameter function, final double min,
			final double max, final double step) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		final double start = Math.min(min, max);
		final double end = Math.max(min, max);
		final double inc = Math.abs(step);
		WB_Coord p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		for (double level = start; level <= end; level += inc) {
			HE_Face f;
			final HE_FaceIterator fItr = lmesh.fItr();
			while (fItr.hasNext()) {
				f = fItr.next();
				p = f.getHalfedge().getVertex();
				q = f.getHalfedge().getEndVertex();
				r = f.getHalfedge().getNextInFace().getEndVertex();
				p1 = new WB_Point();
				p2 = new WB_Point();
				side[0] = function.evaluate(p.xd(), p.yd(), p.zd()) - level;
				side[1] = function.evaluate(q.xd(), q.yd(), q.zd()) - level;
				side[2] = function.evaluate(r.xd(), r.yd(), r.zd()) - level;
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
					p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
					p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
					p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final double[] vertexValues, final double level) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		final HE_FaceIterator fItr = lmesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			p = f.getHalfedge().getVertex();
			q = f.getHalfedge().getEndVertex();
			r = f.getHalfedge().getNextInFace().getEndVertex();
			p1 = new WB_Point();
			p2 = new WB_Point();
			side[0] = vertexValues[mesh.getIndex(p)] - level;
			side[1] = vertexValues[mesh.getIndex(q)] - level;
			side[2] = vertexValues[mesh.getIndex(r)] - level;
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
				p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
				p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
				p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final double[] vertexValues, final double min,
			final double max, final double step) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		final double start = Math.min(min, max);
		final double end = Math.max(min, max);
		final double inc = Math.abs(step);
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		for (double level = start; level <= end; level += inc) {
			HE_Face f;
			final HE_FaceIterator fItr = lmesh.fItr();
			while (fItr.hasNext()) {
				f = fItr.next();
				p = f.getHalfedge().getVertex();
				q = f.getHalfedge().getEndVertex();
				r = f.getHalfedge().getNextInFace().getEndVertex();
				p1 = new WB_Point();
				p2 = new WB_Point();
				side[0] = vertexValues[mesh.getIndex(p)] - level;
				side[1] = vertexValues[mesh.getIndex(q)] - level;
				side[2] = vertexValues[mesh.getIndex(r)] - level;
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
					p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
					p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
					p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final String attribute, final double level) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		final boolean dblAtt = mesh.getDoubleAttribute(attribute) != null;
		final boolean fltAtt = mesh.getFloatAttribute(attribute) != null;
		final boolean intAtt = mesh.getIntegerAttribute(attribute) != null;
		if (!dblAtt && !fltAtt && !intAtt) {
			return segments;
		}
		final HE_FaceIterator fItr = lmesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			p = f.getHalfedge().getVertex();
			q = f.getHalfedge().getEndVertex();
			r = f.getHalfedge().getNextInFace().getEndVertex();
			p1 = new WB_Point();
			p2 = new WB_Point();
			if (dblAtt) {
				side[0] = mesh.getDoubleAttributeForElement(p, attribute) - level;
				side[1] = mesh.getDoubleAttributeForElement(q, attribute) - level;
				side[2] = mesh.getDoubleAttributeForElement(r, attribute) - level;
			} else if (fltAtt) {
				side[0] = mesh.getFloatAttributeForElement(p, attribute) - level;
				side[1] = mesh.getFloatAttributeForElement(q, attribute) - level;
				side[2] = mesh.getFloatAttributeForElement(r, attribute) - level;
			} else {
				side[0] = mesh.getIntegerAttributeForElement(p, attribute) - level;
				side[1] = mesh.getIntegerAttributeForElement(q, attribute) - level;
				side[2] = mesh.getIntegerAttributeForElement(r, attribute) - level;
			}
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
				p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
				p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
				p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh, final String attribute, final double min,
			final double max, final double step) {
		final double[] side = new double[3];
		final List<WB_Segment> segments = new WB_List<>();
		final double start = Math.min(min, max);
		final double end = Math.max(min, max);
		final double inc = Math.abs(step);
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		final HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		final boolean dblAtt = mesh.getDoubleAttribute(attribute) != null;
		final boolean fltAtt = mesh.getFloatAttribute(attribute) != null;
		final boolean intAtt = mesh.getIntegerAttribute(attribute) != null;
		if (!dblAtt && !fltAtt && !intAtt) {
			return segments;
		}
		for (double level = start; level <= end; level += inc) {
			HE_Face f;
			final HE_FaceIterator fItr = lmesh.fItr();
			while (fItr.hasNext()) {
				f = fItr.next();
				p = f.getHalfedge().getVertex();
				q = f.getHalfedge().getEndVertex();
				r = f.getHalfedge().getNextInFace().getEndVertex();
				p1 = new WB_Point();
				p2 = new WB_Point();
				if (dblAtt) {
					side[0] = mesh.getDoubleAttributeForElement(p, attribute) - level;
					side[1] = mesh.getDoubleAttributeForElement(q, attribute) - level;
					side[2] = mesh.getDoubleAttributeForElement(r, attribute) - level;
				} else if (fltAtt) {
					side[0] = mesh.getFloatAttributeForElement(p, attribute) - level;
					side[1] = mesh.getFloatAttributeForElement(q, attribute) - level;
					side[2] = mesh.getFloatAttributeForElement(r, attribute) - level;
				} else {
					side[0] = mesh.getIntegerAttributeForElement(p, attribute) - level;
					side[1] = mesh.getIntegerAttributeForElement(q, attribute) - level;
					side[2] = mesh.getIntegerAttributeForElement(r, attribute) - level;
				}
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				if (Math.signum(side[0]) != Math.signum(side[1]) && Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd() - side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd()) / (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd()) / (side[2] - side[0]));
					p2.set(p.xd() - side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd()) / (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd()) / (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[1]) != Math.signum(side[0]) && Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd() - side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd()) / (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd()) / (side[2] - side[1]));
					p2.set(q.xd() - side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd()) / (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd()) / (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				if (Math.signum(side[2]) != Math.signum(side[0]) && Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd() - side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd()) / (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd()) / (side[0] - side[2]));
					p2.set(r.xd() - side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd()) / (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd()) / (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static float[] toRawFloat(final List<WB_Segment> segments) {
		final float[] coords = new float[segments.size() * 6];
		WB_Segment seg;
		for (int i = 0, j = 0; i < segments.size(); i++) {
			seg = segments.get(i);
			coords[j++] = seg.getOrigin().xf();
			coords[j++] = seg.getOrigin().yf();
			coords[j++] = seg.getOrigin().zf();
			coords[j++] = seg.getEndpoint().xf();
			coords[j++] = seg.getEndpoint().yf();
			coords[j++] = seg.getEndpoint().zf();
		}
		return coords;
	}

	public static double[] toRawDouble(final List<WB_Segment> segments) {
		final double[] coords = new double[segments.size() * 6];
		WB_Segment seg;
		for (int i = 0, j = 0; i < segments.size(); i++) {
			seg = segments.get(i);
			coords[j++] = seg.getOrigin().xf();
			coords[j++] = seg.getOrigin().yf();
			coords[j++] = seg.getOrigin().zf();
			coords[j++] = seg.getEndpoint().xf();
			coords[j++] = seg.getEndpoint().yf();
			coords[j++] = seg.getEndpoint().zf();
		}
		return coords;
	}
}
