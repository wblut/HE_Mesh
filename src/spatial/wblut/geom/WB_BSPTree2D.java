/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;

/**
 * WB_BSPTree2D.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_BSPTree2D {
	/** Root node */
	private WB_BSPNode2D		root;
	private WB_PolygonSplitter	ps;

	/**
	 * Instantiates a new 2D BSPTree
	 */
	public WB_BSPTree2D() {
		ps = new WB_PolygonSplitter();
		root = null;
	}

	private void build(final WB_BSPNode2D tree, final List<WB_Segment> segs) {
		WB_Segment cseg = null;
		final Iterator<WB_Segment> S2DItr = segs.iterator();
		if (S2DItr.hasNext()) {
			cseg = S2DItr.next();
		}
		tree.partition = new WB_Line(cseg.getOrigin(), cseg.getDirection());
		final FastList<WB_Segment> _segs = new FastList<WB_Segment>();
		_segs.add(cseg);
		final FastList<WB_Segment> pos_list = new FastList<WB_Segment>();
		final FastList<WB_Segment> neg_list = new FastList<WB_Segment>();
		WB_Segment seg = null;
		while (S2DItr.hasNext()) {
			seg = S2DItr.next();
			final WB_Classification result = WB_GeometryOp2D
					.classifySegmentToLine2D(seg, tree.partition);
			if (result == WB_Classification.FRONT) {
				pos_list.add(seg);
			} else if (result == WB_Classification.BACK) {
				neg_list.add(seg);
			} else if (result == WB_Classification.CROSSING) { /* spanning */
				final WB_Segment[] split_seg = splitSegment2D(seg,
						tree.partition);
				if (split_seg != null) {
					pos_list.add(split_seg[0]);
					neg_list.add(split_seg[1]);
				} else {
				}
			} else if (result == WB_Classification.ON) {
				_segs.add(seg);
			}
		}
		if (!pos_list.isEmpty()) {
			tree.pos = new WB_BSPNode2D();
			build(tree.pos, pos_list);
		}
		if (!neg_list.isEmpty()) {
			tree.neg = new WB_BSPNode2D();
			build(tree.neg, neg_list);
		}
		if (tree.segments != null) {
			tree.segments.clear();
		}
		tree.segments.addAll(_segs);
	}

	/**
	 * Builds the.
	 *
	 * @param segments
	 *            the segments
	 */
	public void build(final List<WB_Segment> segments) {
		if (root == null) {
			root = new WB_BSPNode2D();
		}
		build(root, segments);
	}

	/**
	 * Builds the.
	 *
	 * @param poly
	 *            the poly
	 */
	public void build(final WB_Polygon poly) {
		if (root == null) {
			root = new WB_BSPNode2D();
		}
		build(root, poly.toSegments());
	}

	/**
	 * Point location.
	 *
	 * @param p
	 *            the p
	 * @return the int
	 */
	public int pointLocation(final WB_Coord p) {
		return pointLocation(root, p);
	}

	/**
	 * Point location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the int
	 */
	public int pointLocation(final double x, final double y) {
		return pointLocation(root, new WB_Point(x, y));
	}

	/**
	 * Point location.
	 *
	 * @param node
	 *            the node
	 * @param p
	 *            the p
	 * @return the int
	 */
	private int pointLocation(final WB_BSPNode2D node, final WB_Coord p) {
		final WB_Classification type = WB_GeometryOp2D.classifyPointToLine2D(p,
				node.partition);
		if (type == WB_Classification.FRONT) {
			if (node.pos != null) {
				return pointLocation(node.pos, p);
			} else {
				return 1;
			}
		} else if (type == WB_Classification.BACK) {
			if (node.neg != null) {
				return pointLocation(node.neg, p);
			} else {
				return -1;
			}
		} else {
			for (int i = 0; i < node.segments.size(); i++) {
				if (WB_Epsilon.isZero(WB_GeometryOp2D.getDistance2D(p,
						node.segments.get(i)))) {
					return 0;
				}
			}
			if (node.pos != null) {
				return pointLocation(node.pos, p);
			} else if (node.neg != null) {
				return pointLocation(node.neg, p);
			} else {
				return 0;
			}
		}
	}

	/**
	 * Partition segment.
	 *
	 * @param S
	 *            the s
	 * @param pos
	 *            the pos
	 * @param neg
	 *            the neg
	 * @param coSame
	 *            the co same
	 * @param coDiff
	 *            the co diff
	 */
	public void partitionSegment(final WB_Segment S, final List<WB_Segment> pos,
			final List<WB_Segment> neg, final List<WB_Segment> coSame,
			final List<WB_Segment> coDiff) {
		partitionSegment(root, S, pos, neg, coSame, coDiff);
	}

	private void partitionSegment(final WB_BSPNode2D node, final WB_Segment S,
			final List<WB_Segment> pos, final List<WB_Segment> neg,
			final List<WB_Segment> coSame, final List<WB_Segment> coDiff) {
		final WB_Classification type = WB_GeometryOp2D
				.classifySegmentToLine2D(S, node.partition);
		if (type == WB_Classification.CROSSING) {
			final WB_Segment[] split = splitSegment2D(S, node.partition);
			if (split != null) {
				getSegmentPosPartition(node, split[0], pos, neg, coSame,
						coDiff);
				getSegmentNegPartition(node, split[1], pos, neg, coSame,
						coDiff);
			}
		} else if (type == WB_Classification.FRONT) {
			getSegmentPosPartition(node, S, pos, neg, coSame, coDiff);
		} else if (type == WB_Classification.BACK) {
			getSegmentNegPartition(node, S, pos, neg, coSame, coDiff);
		} else if (type == WB_Classification.ON) {
			partitionCoincidentSegments(node, S, pos, neg, coSame, coDiff);
		}
	}

	private void partitionCoincidentSegments(final WB_BSPNode2D node,
			final WB_Segment S, final List<WB_Segment> pos,
			final List<WB_Segment> neg, final List<WB_Segment> coSame,
			final List<WB_Segment> coDiff) {
		FastList<WB_Segment> partSegments = new FastList<WB_Segment>();
		partSegments.add(S);
		WB_Segment thisS, otherS;
		final WB_Line L = node.partition;
		for (int i = 0; i < node.segments.size(); i++) {
			final FastList<WB_Segment> newpartSegments = new FastList<WB_Segment>();
			otherS = node.segments.get(i);
			final double v0 = L.getT(otherS.getOrigin());
			final double v1 = L.getT(otherS.getEndpoint());
			for (int j = 0; j < partSegments.size(); j++) {
				thisS = partSegments.get(j);
				final double u0 = L.getT(thisS.getOrigin());
				final double u1 = L.getT(thisS.getEndpoint());
				double[] intersection;
				if (u0 < u1) {
					intersection = intervalIntersection(u0, u1,
							Math.min(v0, v1), Math.max(v0, v1));
					if (intersection[0] == 2) {
						final WB_Point pi = L.getPoint(intersection[1]);
						final WB_Point pj = L.getPoint(intersection[2]);
						if (u0 < intersection[1]) {
							newpartSegments
									.add(new WB_Segment(thisS.getOrigin(), pi));
						}
						coSame.add(new WB_Segment(pi, pj));
						if (u1 > intersection[2]) {
							newpartSegments.add(
									new WB_Segment(pj, thisS.getEndpoint()));
						}
					} else {// this segment doesn't coincide with an edge
						newpartSegments.add(thisS);
					}
				} else {
					intersection = intervalIntersection(u1, u0,
							Math.min(v0, v1), Math.max(v0, v1));
					if (intersection[0] == 2) {
						final WB_Point pi = L.getPoint(intersection[1]);
						final WB_Point pj = L.getPoint(intersection[2]);
						if (u1 < intersection[1]) {
							newpartSegments.add(
									new WB_Segment(pi, thisS.getEndpoint()));
						}
						coDiff.add(new WB_Segment(pj, pi));
						if (u0 > intersection[2]) {
							newpartSegments
									.add(new WB_Segment(thisS.getOrigin(), pj));
						}
					} else {
						newpartSegments.add(thisS);
					}
				}
			}
			partSegments = newpartSegments;
		}
		for (int i = 0; i < partSegments.size(); i++) {
			getSegmentPosPartition(node, partSegments.get(i), pos, neg, coSame,
					coDiff);
			getSegmentNegPartition(node, partSegments.get(i), pos, neg, coSame,
					coDiff);
		}
	}

	private void getSegmentPosPartition(final WB_BSPNode2D node,
			final WB_Segment S, final List<WB_Segment> pos,
			final List<WB_Segment> neg, final List<WB_Segment> coSame,
			final List<WB_Segment> coDiff) {
		if (node.pos != null) {
			partitionSegment(node.pos, S, pos, neg, coSame, coDiff);
		} else {
			pos.add(S);
		}
	}

	private void getSegmentNegPartition(final WB_BSPNode2D node,
			final WB_Segment S, final List<WB_Segment> pos,
			final List<WB_Segment> neg, final List<WB_Segment> coSame,
			final List<WB_Segment> coDiff) {
		if (node.neg != null) {
			partitionSegment(node.neg, S, pos, neg, coSame, coDiff);
		} else {
			neg.add(S);
		}
	}

	/**
	 * To segments.
	 *
	 * @return the array list
	 */
	public ArrayList<WB_Segment> toSegments() {
		final ArrayList<WB_Segment> segments = new ArrayList<WB_Segment>();
		addSegments(root, segments);
		return segments;
	}

	/**
	 * Adds the segments.
	 *
	 * @param node
	 *            the node
	 * @param segments
	 *            the segments
	 */
	private void addSegments(final WB_BSPNode2D node,
			final ArrayList<WB_Segment> segments) {
		segments.addAll(node.segments);
		if (node.pos != null) {
			addSegments(node.pos, segments);
		}
		if (node.neg != null) {
			addSegments(node.neg, segments);
		}
	}

	public WB_BSPTree2D negate() {
		final WB_BSPTree2D negTree = new WB_BSPTree2D();
		negTree.root = negate(root);
		return negTree;
	}

	private WB_BSPNode2D negate(final WB_BSPNode2D node) {
		final WB_BSPNode2D negNode = new WB_BSPNode2D();
		negNode.partition = new WB_Line(node.partition.getOrigin(),
				WB_Vector.mul(node.partition.getDirection(), -1));
		for (int i = 0; i < node.segments.size(); i++) {
			final WB_Segment seg = node.segments.get(i);
			negNode.segments
					.add(new WB_Segment(seg.getEndpoint(), seg.getOrigin()));
		}
		if (node.pos != null) {
			negNode.neg = negate(node.pos);
		}
		if (node.neg != null) {
			negNode.pos = negate(node.neg);
		}
		return negNode;
	}

	/**
	 * Partition polygon.
	 *
	 * @param P
	 *            polygon
	 * @param pos
	 *            List<WB_Polygon> to hold positive, inside fragments
	 * @param neg
	 *            List<WB_Polygon> to hold negative, outside fragments
	 */
	public void partitionPolygon(final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg) {
		partitionPolygon(root, P, pos, neg);
	}

	private void partitionPolygon(final WB_BSPNode2D node, final WB_Polygon P,
			final List<WB_Polygon> pos, final List<WB_Polygon> neg) {
		if (P.getNumberOfPoints() > 2) {
			final WB_Classification type = WB_GeometryOp
					.classifyPolygonToLine2D(P, node.partition);
			if (type == WB_Classification.CROSSING) {
				List<WB_Polygon> splits = ps.splitSimplePolygon2D(P,
						node.partition);
				for (WB_Polygon split : splits) {
					if (WB_GeometryOp.classifyPolygonToLine2D(split,
							node.partition) == WB_Classification.FRONT) {
						getPolygonPosPartition(node, split, pos, neg);
					} else if (WB_GeometryOp.classifyPolygonToLine2D(split,
							node.partition) == WB_Classification.BACK) {
						getPolygonNegPartition(node, split, pos, neg);
					}
				}
			} else if (type == WB_Classification.FRONT) {
				getPolygonPosPartition(node, P, pos, neg);
			} else if (type == WB_Classification.BACK) {
				getPolygonNegPartition(node, P, pos, neg);
			}
		}
	}

	private void getPolygonPosPartition(final WB_BSPNode2D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg) {
		if (node.pos != null) {
			partitionPolygon(node.pos, P, pos, neg);
		} else {
			pos.add(P);
		}
	}

	private void getPolygonNegPartition(final WB_BSPNode2D node,
			final WB_Polygon P, final List<WB_Polygon> pos,
			final List<WB_Polygon> neg) {
		if (node.neg != null) {
			partitionPolygon(node.neg, P, pos, neg);
		} else {
			neg.add(P);
		}
	}

	private WB_Segment[] splitSegment2D(final WB_Segment S, final WB_Line L) {
		final WB_Segment[] result = new WB_Segment[2];
		final WB_IntersectionResult ir2D = closestPoint2D(S, L);
		if (!ir2D.intersection) {
			return null;
		}
		if (ir2D.dimension == 0) {
			if (WB_GeometryOp2D.classifyPointToLine2D(S.getOrigin(),
					L) == WB_Classification.FRONT) {
				result[0] = new WB_Segment(S.getOrigin(),
						(WB_Coord) ir2D.object);
				result[1] = new WB_Segment((WB_Coord) ir2D.object,
						S.getEndpoint());
			} else if (WB_GeometryOp2D.classifyPointToLine2D(S.getOrigin(),
					L) == WB_Classification.BACK) {
				result[1] = new WB_Segment(S.getOrigin(),
						(WB_Coord) ir2D.object);
				result[0] = new WB_Segment((WB_Coord) ir2D.object,
						S.getEndpoint());
			}
		}
		return result;
	}

	private WB_IntersectionResult closestPoint2D(final WB_Segment S,
			final WB_Line L) {
		final WB_IntersectionResult i = closestPoint2D(L,
				new WB_Line(S.getOrigin(), S.getDirection()));
		if (i.dimension == 0) {
			return i;
		}
		if (i.t2 <= WB_Epsilon.EPSILON) {
			i.t2 = 0;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(),
					new WB_Point(S.getOrigin()));
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		if (i.t2 >= S.getLength() - WB_Epsilon.EPSILON) {
			i.t2 = 1;
			i.object = new WB_Segment(((WB_Segment) i.object).getOrigin(),
					new WB_Point(S.getEndpoint()));
			i.sqDist = ((WB_Segment) i.object).getLength();
			i.sqDist *= i.sqDist;
			i.intersection = false;
		}
		return i;
	}

	/**
	 * Interval intersection.
	 *
	 * @param u0
	 *            the u0
	 * @param u1
	 *            the u1
	 * @param v0
	 *            the v0
	 * @param v1
	 *            the v1
	 * @return the double[]
	 */
	private double[] intervalIntersection(final double u0, final double u1,
			final double v0, final double v1) {
		if ((u0 >= u1) || (v0 >= v1)) {
			throw new IllegalArgumentException(
					"Interval degenerate or reversed.");
		}
		final double[] result = new double[3];
		if ((u1 < v0) || (u0 > v1)) {
			return result;
		}
		if (u1 > v0) {
			if (u0 < v1) {
				result[0] = 2;
				if (u0 < v0) {
					result[1] = v0;
				} else {
					result[1] = u0;
				}
				if (u1 > v1) {
					result[2] = v1;
				} else {
					result[2] = u1;
				}
			} else {
				result[0] = 1;
				result[1] = u0;
			}
		} else {
			result[0] = 1;
			result[1] = u1;
		}
		return result;
	}

	private WB_IntersectionResult closestPoint2D(final WB_Line L1,
			final WB_Line L2) {
		final double a = WB_Vector.dot2D(L1.getDirection(), L1.getDirection());
		final double b = WB_Vector.dot2D(L1.getDirection(), L2.getDirection());
		final WB_Point r = WB_Point.sub(L1.getOrigin(), L2.getOrigin());
		final double c = WB_Vector.dot2D(L1.getDirection(), r);
		final double e = WB_Vector.dot2D(L2.getDirection(), L2.getDirection());
		final double f = WB_Vector.dot2D(L2.getDirection(), r);
		double denom = a * e - b * b;
		if (WB_Epsilon.isZero(denom)) {
			final double t2 = r.dot(L1.getDirection());
			final WB_Point p2 = new WB_Point(L2.getPoint(t2));
			final double d2 = p2.getSqDistance2D(L1.getOrigin());
			final WB_IntersectionResult i = new WB_IntersectionResult();
			i.intersection = false;
			i.t1 = 0;
			i.t2 = t2;
			i.dimension = 1;
			i.object = new WB_Segment(L1.getOrigin(), p2);
			i.sqDist = d2;
			return i;
		}
		denom = 1.0 / denom;
		final double t1 = (b * f - c * e) * denom;
		final double t2 = (a * f - b * c) * denom;
		final WB_Point p1 = new WB_Point(L1.getPoint(t1));
		final WB_Point p2 = new WB_Point(L2.getPoint(t2));
		final double d2 = p1.getSqDistance2D(p2);
		final WB_IntersectionResult i = new WB_IntersectionResult();
		i.intersection = true;
		i.t1 = t1;
		i.t2 = t2;
		i.dimension = 0;
		i.object = p1;
		i.sqDist = d2;
		return i;
	}
}
