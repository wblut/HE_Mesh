/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.hemesh.HEC_Geodesic;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Frame {

	private static WB_GeometryFactory gf = new WB_GeometryFactory();

	private List<Strut> struts;

	private List<Node> nodes;

	/**
	 *
	 */
	public WB_Frame() {
		struts = new FastList<Strut>();
		nodes = new FastList<Node>();
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final WB_Coord[] points, final WB_IndexedSegment[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment strut : struts) {
			addStrut(strut.i1(), strut.i2());
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final WB_Coord[] points, final Collection<WB_IndexedSegment> struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment strut : struts) {
			addStrut(strut.i1(), strut.i2());
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public void add(final WB_Coord[] points, final Collection<WB_IndexedSegment> struts) {
		if (struts == null) {
			this.struts = new FastList<Strut>();
		}
		if (nodes == null) {
			nodes = new FastList<Node>();
		}
		final int nodeoffset = nodes.size();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment strut : struts) {
			addStrut(strut.i1() + nodeoffset, strut.i2() + nodeoffset);
		}
	}

	/**
	 *
	 *
	 * @param frame
	 */
	public void add(final WB_Frame frame) {
		if (struts == null) {
			this.struts = new FastList<Strut>();
		}
		if (nodes == null) {
			nodes = new FastList<Node>();
		}
		final int nodeoffset = nodes.size();
		for (final Node node : frame.nodes) {
			addNode(node, node.getValue());
		}
		for (final WB_IndexedSegment strut : frame.getIndexedSegments()) {
			addStrut(strut.i1() + nodeoffset, strut.i2() + nodeoffset);
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final Collection<? extends WB_Coord> points, final Collection<WB_IndexedSegment> struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment strut : struts) {
			addStrut(strut.i1(), strut.i2());
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final WB_Coord[] points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}
	}

	public WB_Frame(final WB_Coord[] points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final Collection<? extends WB_Coord> points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}
	}

	public WB_Frame(final Collection<? extends WB_Coord> points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	public WB_Frame(final WB_CoordCollection points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		WB_Coord point;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	public WB_Frame(final WB_CoordCollection points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		WB_Coord point;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final double[][] points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}

	}

	public WB_Frame(final double[][] points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}

		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final float[][] points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}

	}

	public WB_Frame(final float[][] points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}

		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	/**
	 *
	 *
	 * @param points
	 * @param struts
	 */
	public WB_Frame(final int[][] points, final int[][] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] strut : struts) {
			addStrut(strut[0], strut[1]);
		}

	}

	public WB_Frame(final int[][] points, final int[] struts) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}

		for (int i = 0; i < struts.length; i += 2) {
			addStrut(struts[i], struts[i + 1]);
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Frame(final WB_Coord[] points) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Frame(final Collection<? extends WB_Coord> points) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Frame(final double[][] points) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Frame(final float[][] points) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	/**
	 *
	 *
	 * @param points
	 */
	public WB_Frame(final int[][] points) {
		this.struts = new FastList<Strut>();
		nodes = new FastList<Node>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param v
	 * @return
	 */
	public int addNode(final double x, final double y, final double z, final double v) {
		final int n = nodes.size();
		nodes.add(new Node(new WB_Point(x, y, z), n, v));
		return n;
	}

	/**
	 *
	 *
	 * @param pos
	 * @param v
	 * @return
	 */
	public int addNode(final WB_Coord pos, final double v) {
		final int n = nodes.size();
		nodes.add(new Node(pos, n, v));
		return n;
	}

	/**
	 *
	 *
	 * @param node
	 */
	public void removeNode(final Node node) {
		for (final Strut strut : node.getStruts()) {
			removeStrut(strut);
		}
		nodes.remove(node);
	}

	/**
	 *
	 *
	 * @param pos
	 * @return
	 */
	public int addNodes(final Collection<WB_Coord> pos) {
		int n = nodes.size();
		final Iterator<WB_Coord> pItr = pos.iterator();
		while (pItr.hasNext()) {
			nodes.add(new Node(pItr.next(), n, 1));
			n++;
		}
		return n;
	}

	/**
	 *
	 *
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean addStrut(final int i, final int j) {
		if (i == j) {
			throw new IllegalArgumentException("Strut can't connect a node to itself: " + i + " " + j + ".");
		}
		final int nn = nodes.size();
		if (i < 0 || j < 0 || i >= nn || j >= nn) {
			throw new IllegalArgumentException("Strut indices outside node range.");
		}
		final int n = struts.size();
		Strut strut;
		if (i <= j) {
			strut = new Strut(nodes.get(i), nodes.get(j), n);
		} else {
			strut = new Strut(nodes.get(j), nodes.get(i), n);
		}
		if (!nodes.get(i).addStrut(strut)) {
			return false;
		} else if (!nodes.get(j).addStrut(strut)) {
			return false;
		} else {
			struts.add(strut);
		}
		return true;
	}

	/**
	 *
	 *
	 * @param strut
	 */
	public void removeStrut(final Strut strut) {
		nodes.get(strut.getStartIndex()).removeStrut(strut);
		nodes.get(strut.getEndIndex()).removeStrut(strut);
		struts.remove(strut);
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<Strut> getStruts() {
		final List<Strut> result = new ArrayList<Strut>();
		result.addAll(struts);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Segment> getSegments() {
		final List<WB_Segment> result = new ArrayList<WB_Segment>();
		for (final Strut strut : struts) {
			result.add(strut.toSegment());
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_IndexedSegment> getIndexedSegments() {
		final List<WB_Point> apoints = getPoints();
		WB_Point[] ipoints = new WB_Point[apoints.size()];
		ipoints = apoints.toArray(ipoints);
		final List<WB_IndexedSegment> result = new ArrayList<WB_IndexedSegment>();
		for (final Strut strut : struts) {
			result.add(new WB_IndexedSegment(strut.getStartIndex(), strut.getEndIndex(), ipoints));
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfStruts() {
		return struts.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<Node> getNodes() {
		final List<Node> result = new ArrayList<Node>();
		result.addAll(nodes);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Point> getPoints() {
		final List<WB_Point> result = new ArrayList<WB_Point>();
		result.addAll(nodes);
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Point[] getPointsAsArray() {
		final List<WB_Point> result = new ArrayList<WB_Point>();
		result.addAll(nodes);
		final List<WB_Point> apoints = getPoints();
		final WB_Point[] ipoints = new WB_Point[apoints.size()];
		return apoints.toArray(ipoints);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfNodes() {
		return nodes.size();
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public Node getNode(final int i) {
		if (i < 0 || i >= nodes.size()) {
			throw new IllegalArgumentException("Index outside of node range.");
		}
		return nodes.get(i);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public Strut getStrut(final int i) {
		if (i < 0 || i >= struts.size()) {
			throw new IllegalArgumentException("Index outside of strut range.");
		}
		return struts.get(i);
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public double getDistanceToStrut(final WB_Coord p) {
		double d = Double.POSITIVE_INFINITY;
		for (int i = 0; i < struts.size(); i++) {
			final Strut strut = struts.get(i);
			final WB_Segment S = new WB_Segment(strut.start(), strut.end());
			d = Math.min(d, WB_GeometryOp3D.getDistance3D(p, S));
		}
		return d;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public int getClosestNodeOnStrut(final WB_Coord p) {
		double mind = Double.POSITIVE_INFINITY;
		int q = -1;
		for (int i = 0; i < nodes.size(); i++) {
			final double d = WB_CoordOp3D.getSqDistance3D(p, nodes.get(i));
			if (d < mind) {
				mind = d;
				q = i;
			}
		}
		return q;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public WB_Coord getClosestPointOnStrut(final WB_Coord p) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Coord q = new WB_Point(p);
		for (int i = 0; i < struts.size(); i++) {
			final Strut strut = struts.get(i);
			final WB_Segment S = new WB_Segment(strut.start(), strut.end());
			final double d = WB_GeometryOp3D.getDistance3D(p, S);
			if (d < mind) {
				mind = d;
				q = WB_GeometryOp3D.getClosestPoint3D(S, p);
			}
		}
		return q;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double getDistanceToStrut(final double x, final double y, final double z) {
		double d = Double.POSITIVE_INFINITY;
		for (int i = 0; i < struts.size(); i++) {
			final Strut strut = struts.get(i);
			final WB_Segment S = new WB_Segment(strut.start(), strut.end());
			d = Math.min(d, WB_GeometryOp3D.getDistance3D(new WB_Point(x, y, z), S));
		}
		return d;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public WB_Coord getClosestPointOnStrut(final double x, final double y, final double z) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Coord q = new WB_Point(x, y, z);
		for (int i = 0; i < struts.size(); i++) {
			final Strut strut = struts.get(i);
			final WB_Segment S = new WB_Segment(strut.start(), strut.end());
			final double d = WB_GeometryOp3D.getDistance3D(new WB_Point(x, y, z), S);
			if (d < mind) {
				mind = d;
				q = WB_GeometryOp3D.getClosestPoint3D(S, new WB_Point(x, y, z));
			}
		}
		return q;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Frame smoothBiNodes() {
		final WB_Point[] newPos = new WB_Point[nodes.size()];
		int id = 0;
		for (final Node node : nodes) {
			if (node.getOrder() == 2) {
				newPos[id] = node.getNeighbor(0).add(node.getNeighbor(1));
				newPos[id].mulSelf(0.5);
				newPos[id].addSelf(node);
				newPos[id].mulSelf(0.5);
			}
			id++;
		}
		id = 0;
		for (final Node node : nodes) {
			if (node.getOrder() == 2) {
				node.set(newPos[id]);
			}
			id++;
		}
		return this;
	}

	public WB_Frame smoothBiNodes(final int r) {
		for (int i = 0; i < r; i++) {
			smoothBiNodes();
		}
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Frame smoothNodes() {
		final WB_Point[] newPos = new WB_Point[nodes.size()];
		int id = 0;
		for (final Node node : nodes) {
			if (node.getOrder() > 1) {
				newPos[id] = new WB_Point();
				final List<Node> ns = node.getNeighbors();
				for (final Node n : ns) {
					newPos[id].addSelf(n);
				}
				newPos[id].mulSelf(1.0 / ns.size());
				newPos[id].addSelf(node);
				newPos[id].mulSelf(0.5);
				id++;
			}
		}
		id = 0;
		for (final Node node : nodes) {
			if (node.getOrder() > 1) {
				node.set(newPos[id]);
				id++;
			}
		}
		return this;
	}

	public WB_Frame smoothNodes(final int r) {
		for (int i = 0; i < r; i++) {
			smoothNodes();
		}
		return this;
	}

	public WB_Frame refine(final double threshold) {
		WB_Frame result = refineOnePass(threshold);
		if (result.getNumberOfNodes() == this.getNumberOfNodes()) {
			return result;
		}
		int n = 0;
		do {
			n = result.getNumberOfNodes();
			result = result.refine(threshold);
		} while (n != result.getNumberOfNodes());

		return result;
	}

	/**
	 *
	 *
	 * @param threshold
	 * @return
	 */
	public WB_Frame refineOnePass(final double threshold) {

		final WB_Frame result = new WB_Frame();
		for (final Node node : nodes) {
			result.addNode(node, node.getValue());
		}
		for (final Strut strut : struts) {
			if (strut.getLength() > threshold) {
				final WB_Point start = strut.start();
				final WB_Point end = strut.end();
				final WB_Point mid = gf.createInterpolatedPoint(start, end, 0.5);
				result.addNode(mid, 0.5 * (strut.start().getValue() + strut.end().getValue()));
			}
		}
		final int n = getNumberOfNodes();
		int id = 0;
		for (final Strut strut : struts) {
			if (strut.getLength() > threshold) {
				final int start = strut.getStartIndex();
				final int end = strut.getEndIndex();
				result.addStrut(start, n + id);
				result.addStrut(n + id, end);
				id++;
			} else {
				final int start = strut.getStartIndex();
				final int end = strut.getEndIndex();
				result.addStrut(start, end);
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param n
	 * @param r
	 * @param d
	 * @param l
	 * @param rr
	 * @param dr
	 * @return
	 */
	public List<WB_Point> toPointCloud(final int n, final double r, final double d, final int l, final double rr,
			final double dr) {
		final List<WB_Point> points = new FastList<WB_Point>();
		double sl, dsl;
		int divs;
		WB_Plane P;
		WB_Vector u, localu, v;
		WB_Point p;
		final WB_RandomOnSphere rnd = new WB_RandomOnSphere();
		final double da = 2.0 * Math.PI / n;
		for (final Strut strut : struts) {
			sl = strut.getLength() - 2 * rr;
			if (sl > 0) {
				divs = (int) WB_Math.max(1, Math.round(sl / d));
				dsl = sl / divs;
				P = strut.toPlane();
				u = P.getU().mul(r);
				v = strut.toNormVector().copy();
				strut.start().addMul(rr, v);
				v.mulSelf(dsl);
				for (int i = 0; i <= divs; i++) {
					for (int j = 0; j < n; j++) {
						p = strut.start().addMul(i, v);
						localu = u.copy();
						localu.rotateAboutAxisSelf(j * da, new WB_Point(), P.getNormal());
						p.addSelf(localu);
						p.addSelf(rnd.nextVector().mulSelf(dr));
						points.add(p);
					}
				}
			}
		}
		for (final Node node : nodes) {
			final HE_Mesh ball = new HE_Mesh(new HEC_Geodesic().setRadius(rr).setB(l).setC(0).setCenter(node));
			for (final WB_Coord q : ball.getVerticesAsCoord()) {
				points.add(new WB_Point(q).addSelf(rnd.nextVector().mulSelf(dr)));
			}
		}
		return points;
	}

	public class Node extends WB_Point {

		private final List<Strut> struts;

		protected final int index;

		protected double value;

		/**
		 *
		 *
		 * @param pos
		 * @param id
		 * @param v
		 */
		public Node(final WB_Coord pos, final int id, final double v) {
			super(pos);
			index = id;
			struts = new FastList<Strut>();
			value = v == 0 ? 10 * WB_Epsilon.EPSILON : v;
		}

		/**
		 *
		 *
		 * @param strut
		 * @return
		 */
		public boolean addStrut(final Strut strut) {
			if (strut.start() != this && strut.end() != this) {
				return false;
			}
			for (int i = 0; i < struts.size(); i++) {
				if (struts.get(i).start() == strut.start() && struts.get(i).end() == strut.end()) {
					return false;
				}
			}
			struts.add(strut);
			return true;
		}

		/**
		 *
		 *
		 * @param strut
		 * @return
		 */
		public boolean removeStrut(final Strut strut) {
			if (strut.start() != this && strut.end() != this) {
				return false;
			}
			struts.remove(strut);
			return true;
		}

		/**
		 *
		 *
		 * @return
		 */
		public List<Strut> getStruts() {
			final List<Strut> result = new ArrayList<Strut>();
			result.addAll(struts);
			return result;
		}

		/**
		 *
		 *
		 * @return
		 */
		public List<Node> getNeighbors() {
			final List<Node> result = new ArrayList<Node>();
			for (int i = 0; i < struts.size(); i++) {
				if (struts.get(i).start() == this) {
					result.add(struts.get(i).end());
				} else {
					result.add(struts.get(i).start());
				}
			}
			return result;
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getIndex() {
			return index;
		}

		/**
		 *
		 *
		 * @return
		 */
		public double findSmallestSpan() {
			double minAngle = Double.MAX_VALUE;
			for (int i = 0; i < getOrder(); i++) {
				minAngle = Math.min(minAngle, findSmallestSpanAroundStrut(i));
			}
			return minAngle;
		}

		/**
		 *
		 *
		 * @param strut
		 * @return
		 */
		public double findSmallestSpanAroundStrut(final Strut strut) {
			return findSmallestSpanAroundStrut(struts.indexOf(strut));
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		public double findSmallestSpanAroundStrut(final int i) {
			final int n = struts.size();
			if (i < 0 || i >= n) {
				throw new IllegalArgumentException("Index beyond strut range.");
			}
			final List<Node> nnodes = getNeighbors();
			if (n == 1) {
				return 2 * Math.PI;
			} else if (n == 2) {
				final WB_Vector u = nnodes.get(0).subToVector3D(this);
				final WB_Vector w = nnodes.get(1).subToVector3D(this);
				u.normalizeSelf();
				w.normalizeSelf();
				final double udw = WB_Math.clamp(u.dot(w), -1, 1);
				if (udw < WB_Epsilon.EPSILON - 1) {
					return Math.PI;
				} else {
					return Math.acos(udw);
				}
			} else {
				double minAngle = Double.MAX_VALUE;
				final WB_Vector u = nnodes.get(i).subToVector3D(this);
				u.normalizeSelf();
				for (int j = 0; j < n; j++) {
					if (i != j) {
						final WB_Vector w = nnodes.get(j).subToVector3D(this);
						w.normalizeSelf();
						final double a = Math.acos(u.dot(w));
						minAngle = WB_Math.min(minAngle, a);
					}
				}
				return minAngle;
			}
		}

		/**
		 *
		 *
		 * @return
		 */
		public double findShortestStrut() {
			double minLength = Double.MAX_VALUE;
			for (int i = 0; i < struts.size(); i++) {
				minLength = Math.min(minLength, struts.get(i).getSqLength());
			}
			return Math.sqrt(minLength);
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getOrder() {
			return struts.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getValue() {
			return value;
		}

		/**
		 *
		 *
		 * @param v
		 */
		public void setValue(final double v) {
			value = v == 0 ? 10 * WB_Epsilon.EPSILON : v;
		}

		/**
		 *
		 *
		 * @param index
		 * @return
		 */
		public Strut getStrut(final int index) {
			if (index < 0 || index >= struts.size()) {
				throw new IllegalArgumentException("Index outside of strut range.");
			}
			return struts.get(index);
		}

		/**
		 *
		 *
		 * @param index
		 */
		public void removeStrut(final int index) {
			if (index < 0 || index >= struts.size()) {
				throw new IllegalArgumentException("Index outside of strut range.");
			}
			struts.remove(index);
		}

		/**
		 *
		 *
		 * @param index
		 * @return
		 */
		public Node getNeighbor(final int index) {
			if (index < 0 || index >= struts.size()) {
				throw new IllegalArgumentException("Index outside of strut range.");
			}
			if (struts.get(index).start() == this) {
				return struts.get(index).end();
			}
			return struts.get(index).start();
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Point toPoint() {
			return new WB_Point(xd(), yd(), zd());
		}
	}

	public class Strut {

		private final Node start;

		private final Node end;

		private final int index;

		private double radiuss;

		private double radiuse;

		private double offsets;

		private double offsete;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 */
		public Strut(final Node s, final Node e, final int id) {
			start = s;
			end = e;
			index = id;
		}

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param r
		 */
		public Strut(final Node s, final Node e, final int id, final double r) {
			start = s;
			end = e;
			index = id;
			radiuss = radiuse = r;
		}

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param rs
		 * @param re
		 */
		public Strut(final Node s, final Node e, final int id, final double rs, final double re) {
			start = s;
			end = e;
			index = id;
			radiuss = rs;
			radiuse = re;
		}

		/**
		 *
		 *
		 * @return
		 */
		public Node start() {
			return start;
		}

		/**
		 *
		 *
		 * @return
		 */
		public Node end() {
			return end;
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getStartIndex() {
			return start.getIndex();
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getEndIndex() {
			return end.getIndex();
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getIndex() {
			return index;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Vector toVector() {
			return end().subToVector3D(start());
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Vector toNormVector() {
			final WB_Vector v = end().subToVector3D(start());
			v.normalizeSelf();
			return v;
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getSqLength() {
			return WB_CoordOp3D.getSqDistance3D(end(), start());
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getLength() {
			return WB_CoordOp3D.getDistance3D(end(), start());
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getRadiusStart() {
			return radiuss;
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getRadiusEnd() {
			return radiuse;
		}

		/**
		 *
		 *
		 * @param r
		 */
		public void setRadiusStart(final double r) {
			radiuss = r;
		}

		/**
		 *
		 *
		 * @param r
		 */
		public void setRadiusEnd(final double r) {
			radiuse = r;
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getOffsetStart() {
			return offsets;
		}

		/**
		 *
		 *
		 * @return
		 */
		public double getOffsetEnd() {
			return offsete;
		}

		/**
		 *
		 *
		 * @param o
		 */
		public void setOffsetStart(final double o) {
			offsets = o;
		}

		/**
		 *
		 *
		 * @param o
		 */
		public void setOffsetEnd(final double o) {
			offsete = o;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Point getCenter() {
			return end().add(start()).mulSelf(0.5);
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Segment toSegment() {
			return new WB_Segment(start, end);
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Plane toPlane() {
			return new WB_Plane(start().toPoint(), toVector());
		}
	}

}
