/**
 * Base on Paul Bourke's CONREC routine: http://paulbourke.net/papers/conrec/
 *
 */
package wblut.hemesh;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Segment;
import wblut.math.WB_ScalarParameter;

/**
 * @author FVH
 *
 */
public class HET_Contours {
	public static List<List<WB_Coord>> contoursAsPaths(final HE_Mesh mesh,
			final WB_Plane P) {
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Mesh slmesh = lmesh.get();
		HEM_SliceSurface ss = new HEM_SliceSurface().setPlane(P);
		slmesh.modify(ss);
		List<HE_Path> paths = ss.getPaths();
		List<List<WB_Coord>> contours = new FastList<List<WB_Coord>>();
		for (HE_Path path : paths) {
			List<WB_Coord> contour = new FastList<WB_Coord>();
			contour.addAll(path.getPathVertices());
			if (path.isLoop()) {
				contour.add(path.getPathVertices().get(0));
			}
			contours.add(contour);
		}
		return contours;
	}

	public static List<List<WB_Coord>> contoursAsPaths(final HE_Mesh mesh,
			final WB_Plane P, final double min, final double max,
			final double step) {
		double start = Math.min(min, max);
		double end = Math.max(min, max);
		double inc = Math.abs(step);
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		List<List<WB_Coord>> contours = new FastList<List<WB_Coord>>();
		for (double offset = start; offset <= end; offset += inc) {
			WB_Plane offsetP = new WB_Plane(
					P.getOrigin().addMul(offset, P.getNormal()), P.getNormal());
			HE_Mesh slmesh = lmesh.get();
			HEM_SliceSurface ss = new HEM_SliceSurface().setPlane(offsetP);
			slmesh.modify(ss);
			List<HE_Path> paths = ss.getPaths();
			for (HE_Path path : paths) {
				List<WB_Coord> contour = new FastList<WB_Coord>();
				for (WB_Coord p : path.getPathVertices()) {
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

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final WB_Plane P) {
		double A, B, C, D;
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		A = P.getNormal().xd();
		B = P.getNormal().yd();
		C = P.getNormal().zd();
		D = -P.d();
		WB_Coord p, q, r;
		WB_Point p1, p2;
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		HE_FaceIterator fItr = lmesh.fItr();
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
			/* Are all the vertices on the same side */
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			/* Is p0 the only point on a side by itself */
			if (Math.signum(side[0]) != Math.signum(side[1])
					&& Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd()
						- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd())
								/ (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd())
								/ (side[2] - side[0]));
				p2.set(p.xd()
						- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd())
								/ (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd())
								/ (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p1 the only point on a side by itself */
			if (Math.signum(side[1]) != Math.signum(side[0])
					&& Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd()
						- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd())
								/ (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd())
								/ (side[2] - side[1]));
				p2.set(q.xd()
						- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd())
								/ (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd())
								/ (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p2 the only point on a side by itself */
			if (Math.signum(side[2]) != Math.signum(side[0])
					&& Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd()
						- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd())
								/ (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd())
								/ (side[0] - side[2]));
				p2.set(r.xd()
						- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd())
								/ (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd())
								/ (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final WB_Plane P, final double min, final double max,
			final double step) {
		double A, B, C, D;
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		double start = Math.min(min, max);
		double end = Math.max(min, max);
		double inc = Math.abs(step);
		WB_Coord p, q, r;
		WB_Point p1, p2;
		A = P.getNormal().xd();
		B = P.getNormal().yd();
		C = P.getNormal().zd();
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		for (double offset = start; offset <= end; offset += inc) {
			WB_Plane offsetP = new WB_Plane(
					P.getOrigin().addMul(offset, P.getNormal()), P.getNormal());
			D = -offsetP.d();
			HE_Face f;
			HE_FaceIterator fItr = lmesh.fItr();
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
				/* Are all the vertices on the same side */
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				/* Is p0 the only point on a side by itself */
				if (Math.signum(side[0]) != Math.signum(side[1])
						&& Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd()
							- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd())
									/ (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd())
									/ (side[2] - side[0]));
					p2.set(p.xd()
							- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd())
									/ (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd())
									/ (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p1 the only point on a side by itself */
				if (Math.signum(side[1]) != Math.signum(side[0])
						&& Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd()
							- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd())
									/ (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd())
									/ (side[2] - side[1]));
					p2.set(q.xd()
							- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd())
									/ (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd())
									/ (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p2 the only point on a side by itself */
				if (Math.signum(side[2]) != Math.signum(side[0])
						&& Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd()
							- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd())
									/ (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd())
									/ (side[0] - side[2]));
					p2.set(r.xd()
							- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd())
									/ (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd())
									/ (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final WB_ScalarParameter function, final double level) {
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		WB_Coord p, q, r;
		WB_Point p1, p2;
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		HE_FaceIterator fItr = lmesh.fItr();
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
			/* Are all the vertices on the same side */
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			/* Is p0 the only point on a side by itself */
			if (Math.signum(side[0]) != Math.signum(side[1])
					&& Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd()
						- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd())
								/ (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd())
								/ (side[2] - side[0]));
				p2.set(p.xd()
						- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd())
								/ (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd())
								/ (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p1 the only point on a side by itself */
			if (Math.signum(side[1]) != Math.signum(side[0])
					&& Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd()
						- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd())
								/ (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd())
								/ (side[2] - side[1]));
				p2.set(q.xd()
						- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd())
								/ (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd())
								/ (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p2 the only point on a side by itself */
			if (Math.signum(side[2]) != Math.signum(side[0])
					&& Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd()
						- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd())
								/ (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd())
								/ (side[0] - side[2]));
				p2.set(r.xd()
						- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd())
								/ (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd())
								/ (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final WB_ScalarParameter function, final double min,
			final double max, final double step) {
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		double start = Math.min(min, max);
		double end = Math.max(min, max);
		double inc = Math.abs(step);
		WB_Coord p, q, r;
		WB_Point p1, p2;
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		for (double level = start; level <= end; level += inc) {
			HE_Face f;
			HE_FaceIterator fItr = lmesh.fItr();
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
				/* Are all the vertices on the same side */
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				/* Is p0 the only point on a side by itself */
				if (Math.signum(side[0]) != Math.signum(side[1])
						&& Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd()
							- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd())
									/ (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd())
									/ (side[2] - side[0]));
					p2.set(p.xd()
							- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd())
									/ (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd())
									/ (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p1 the only point on a side by itself */
				if (Math.signum(side[1]) != Math.signum(side[0])
						&& Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd()
							- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd())
									/ (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd())
									/ (side[2] - side[1]));
					p2.set(q.xd()
							- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd())
									/ (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd())
									/ (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p2 the only point on a side by itself */
				if (Math.signum(side[2]) != Math.signum(side[0])
						&& Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd()
							- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd())
									/ (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd())
									/ (side[0] - side[2]));
					p2.set(r.xd()
							- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd())
									/ (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd())
									/ (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final double[] vertexValues, final double level) {
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		HE_Face f;
		HE_FaceIterator fItr = lmesh.fItr();
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
			/* Are all the vertices on the same side */
			if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
				continue;
			}
			if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
				continue;
			}
			/* Is p0 the only point on a side by itself */
			if (Math.signum(side[0]) != Math.signum(side[1])
					&& Math.signum(side[0]) != Math.signum(side[2])) {
				p1.set(p.xd()
						- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
						p.yd() - side[0] * (r.yd() - p.yd())
								/ (side[2] - side[0]),
						p.zd() - side[0] * (r.zd() - p.zd())
								/ (side[2] - side[0]));
				p2.set(p.xd()
						- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
						p.yd() - side[0] * (q.yd() - p.yd())
								/ (side[1] - side[0]),
						p.zd() - side[0] * (q.zd() - p.zd())
								/ (side[1] - side[0]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p1 the only point on a side by itself */
			if (Math.signum(side[1]) != Math.signum(side[0])
					&& Math.signum(side[1]) != Math.signum(side[2])) {
				p1.set(q.xd()
						- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
						q.yd() - side[1] * (r.yd() - q.yd())
								/ (side[2] - side[1]),
						q.zd() - side[1] * (r.zd() - q.zd())
								/ (side[2] - side[1]));
				p2.set(q.xd()
						- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
						q.yd() - side[1] * (p.yd() - q.yd())
								/ (side[0] - side[1]),
						q.zd() - side[1] * (p.zd() - q.zd())
								/ (side[0] - side[1]));
				segments.add(new WB_Segment(p1, p2));
			}
			/* Is p2 the only point on a side by itself */
			if (Math.signum(side[2]) != Math.signum(side[0])
					&& Math.signum(side[2]) != Math.signum(side[1])) {
				p1.set(r.xd()
						- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
						r.yd() - side[2] * (p.yd() - r.yd())
								/ (side[0] - side[2]),
						r.zd() - side[2] * (p.zd() - r.zd())
								/ (side[0] - side[2]));
				p2.set(r.xd()
						- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
						r.yd() - side[2] * (q.yd() - r.yd())
								/ (side[1] - side[2]),
						r.zd() - side[2] * (q.zd() - r.zd())
								/ (side[1] - side[2]));
				segments.add(new WB_Segment(p1, p2));
			}
		}
		return segments;
	}

	public static List<WB_Segment> contours(final HE_Mesh mesh,
			final double[] vertexValues, final double min, final double max,
			final double step) {
		double[] side = new double[3];
		List<WB_Segment> segments = new FastList<WB_Segment>();
		double start = Math.min(min, max);
		double end = Math.max(min, max);
		double inc = Math.abs(step);
		HE_Vertex p, q, r;
		WB_Point p1, p2;
		HE_Mesh lmesh = mesh;
		HE_MeshOp.triangulate(lmesh);
		for (double level = start; level <= end; level += inc) {
			HE_Face f;
			HE_FaceIterator fItr = lmesh.fItr();
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
				/* Are all the vertices on the same side */
				if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) {
					continue;
				}
				if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) {
					continue;
				}
				/* Is p0 the only point on a side by itself */
				if (Math.signum(side[0]) != Math.signum(side[1])
						&& Math.signum(side[0]) != Math.signum(side[2])) {
					p1.set(p.xd()
							- side[0] * (r.xd() - p.xd()) / (side[2] - side[0]),
							p.yd() - side[0] * (r.yd() - p.yd())
									/ (side[2] - side[0]),
							p.zd() - side[0] * (r.zd() - p.zd())
									/ (side[2] - side[0]));
					p2.set(p.xd()
							- side[0] * (q.xd() - p.xd()) / (side[1] - side[0]),
							p.yd() - side[0] * (q.yd() - p.yd())
									/ (side[1] - side[0]),
							p.zd() - side[0] * (q.zd() - p.zd())
									/ (side[1] - side[0]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p1 the only point on a side by itself */
				if (Math.signum(side[1]) != Math.signum(side[0])
						&& Math.signum(side[1]) != Math.signum(side[2])) {
					p1.set(q.xd()
							- side[1] * (r.xd() - q.xd()) / (side[2] - side[1]),
							q.yd() - side[1] * (r.yd() - q.yd())
									/ (side[2] - side[1]),
							q.zd() - side[1] * (r.zd() - q.zd())
									/ (side[2] - side[1]));
					p2.set(q.xd()
							- side[1] * (p.xd() - q.xd()) / (side[0] - side[1]),
							q.yd() - side[1] * (p.yd() - q.yd())
									/ (side[0] - side[1]),
							q.zd() - side[1] * (p.zd() - q.zd())
									/ (side[0] - side[1]));
					segments.add(new WB_Segment(p1, p2));
				}
				/* Is p2 the only point on a side by itself */
				if (Math.signum(side[2]) != Math.signum(side[0])
						&& Math.signum(side[2]) != Math.signum(side[1])) {
					p1.set(r.xd()
							- side[2] * (p.xd() - r.xd()) / (side[0] - side[2]),
							r.yd() - side[2] * (p.yd() - r.yd())
									/ (side[0] - side[2]),
							r.zd() - side[2] * (p.zd() - r.zd())
									/ (side[0] - side[2]));
					p2.set(r.xd()
							- side[2] * (q.xd() - r.xd()) / (side[1] - side[2]),
							r.yd() - side[2] * (q.yd() - r.yd())
									/ (side[1] - side[2]),
							r.zd() - side[2] * (q.zd() - r.zd())
									/ (side[1] - side[2]));
					segments.add(new WB_Segment(p1, p2));
				}
			}
		}
		return segments;
	}
}
