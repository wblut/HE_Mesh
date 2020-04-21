package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import wblut.hemesh.HEC_Geodesic;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

public class WB_Network {
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	private List<Connection> connections;
	private List<Node> nodes;

	public WB_Network() {
		connections = new WB_List<>();
		nodes = new WB_List<>();
	}

	public WB_Network(final WB_Coord[] points, final WB_IndexedSegment[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addConnection(connection.i1(), connection.i2());
		}
	}

	public WB_Network(final WB_Coord[] points, final Collection<WB_IndexedSegment> connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addConnection(connection.i1(), connection.i2());
		}
	}

	public void add(final WB_Coord[] points, final Collection<WB_IndexedSegment> connections) {
		if (connections == null) {
			this.connections = new WB_List<>();
		}
		if (nodes == null) {
			nodes = new WB_List<>();
		}
		final int nodeoffset = nodes.size();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addConnection(connection.i1() + nodeoffset, connection.i2() + nodeoffset);
		}
	}

	public void add(final WB_Network frame) {
		if (connections == null) {
			this.connections = new WB_List<>();
		}
		if (nodes == null) {
			nodes = new WB_List<>();
		}
		final int nodeoffset = nodes.size();
		for (final Node node : frame.nodes) {
			addNode(node, node.getValue());
		}
		for (final WB_IndexedSegment connection : frame.getIndexedSegments()) {
			addConnection(connection.i1() + nodeoffset, connection.i2() + nodeoffset);
		}
	}

	public WB_Network(final Collection<? extends WB_Coord> points, final Collection<WB_IndexedSegment> connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addConnection(connection.i1(), connection.i2());
		}
	}

	public WB_Network(final WB_Coord[] points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final WB_Coord[] points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final Collection<? extends WB_Coord> points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final Collection<? extends WB_Coord> points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final WB_CoordCollection points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		WB_Coord point;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final WB_CoordCollection points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		WB_Coord point;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final double[][] points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final double[][] points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final float[][] points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final float[][] points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final int[][] points, final int[][] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addConnection(connection[0], connection[1]);
		}
	}

	public WB_Network(final int[][] points, final int[] connections) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (int i = 0; i < connections.length; i += 2) {
			addConnection(connections[i], connections[i + 1]);
		}
	}

	public WB_Network(final WB_Coord[] points) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
	}

	public WB_Network(final Collection<? extends WB_Coord> points) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final WB_Coord point : points) {
			addNode(point.xd(), point.yd(), point.zd(), 1);
		}
	}

	public WB_Network(final double[][] points) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	public WB_Network(final float[][] points) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	public WB_Network(final int[][] points) {
		this.connections = new WB_List<>();
		nodes = new WB_List<>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	public int addNode(final double x, final double y, final double z, final double v) {
		final int n = nodes.size();
		nodes.add(new Node(new WB_Point(x, y, z), n, v));
		return n;
	}

	public int addNode(final WB_Coord pos, final double v) {
		final int n = nodes.size();
		nodes.add(new Node(pos, n, v));
		return n;
	}

	public void removeNode(final Node node) {
		for (final Connection connection : node.getConnections()) {
			removeConnection(connection);
		}
		nodes.remove(node);
	}

	public int addNodes(final Collection<WB_Coord> pos) {
		int n = nodes.size();
		final Iterator<WB_Coord> pItr = pos.iterator();
		while (pItr.hasNext()) {
			nodes.add(new Node(pItr.next(), n, 1));
			n++;
		}
		return n;
	}

	public boolean addConnection(final int i, final int j) {
		if (i == j) {
			throw new IllegalArgumentException("Connection can't connect a node to itself: " + i + " " + j + ".");
		}
		final int nn = nodes.size();
		if (i < 0 || j < 0 || i >= nn || j >= nn) {
			throw new IllegalArgumentException("Connection indices outside node range.");
		}
		final int n = connections.size();
		Connection connection;
		if (i <= j) {
			connection = new Connection(nodes.get(i), nodes.get(j), n);
		} else {
			connection = new Connection(nodes.get(j), nodes.get(i), n);
		}
		if (!nodes.get(i).addConnection(connection)) {
			return false;
		} else if (!nodes.get(j).addConnection(connection)) {
			return false;
		} else {
			connections.add(connection);
		}
		return true;
	}

	public void removeConnection(final Connection connection) {
		nodes.get(connection.getStartIndex()).removeConnection(connection);
		nodes.get(connection.getEndIndex()).removeConnection(connection);
		connections.remove(connection);
	}

	public List<Connection> getConnections() {
		final List<Connection> result = new ArrayList<>();
		result.addAll(connections);
		return result;
	}

	public List<WB_Segment> getSegments() {
		final List<WB_Segment> result = new ArrayList<>();
		for (final Connection connection : connections) {
			result.add(connection.toSegment());
		}
		return result;
	}

	public List<WB_IndexedSegment> getIndexedSegments() {
		final List<WB_Point> apoints = getPoints();
		WB_Point[] ipoints = new WB_Point[apoints.size()];
		ipoints = apoints.toArray(ipoints);
		final List<WB_IndexedSegment> result = new ArrayList<>();
		for (final Connection connection : connections) {
			result.add(new WB_IndexedSegment(connection.getStartIndex(), connection.getEndIndex(), ipoints));
		}
		return result;
	}

	public int getNumberOfConnections() {
		return connections.size();
	}

	public List<Node> getNodes() {
		final List<Node> result = new ArrayList<>();
		result.addAll(nodes);
		return result;
	}

	public List<WB_Point> getPoints() {
		final List<WB_Point> result = new ArrayList<>();
		result.addAll(nodes);
		return result;
	}

	public WB_Point[] getPointsAsArray() {
		final List<WB_Point> result = new ArrayList<>();
		result.addAll(nodes);
		final List<WB_Point> apoints = getPoints();
		final WB_Point[] ipoints = new WB_Point[apoints.size()];
		return apoints.toArray(ipoints);
	}

	public int getNumberOfNodes() {
		return nodes.size();
	}

	public Node getNode(final int i) {
		if (i < 0 || i >= nodes.size()) {
			throw new IllegalArgumentException("Index outside of node range.");
		}
		return nodes.get(i);
	}

	public Connection getConnection(final int i) {
		if (i < 0 || i >= connections.size()) {
			throw new IllegalArgumentException("Index outside of connection range.");
		}
		return connections.get(i);
	}

	public double getDistanceToConnection(final WB_Coord p) {
		double d = Double.POSITIVE_INFINITY;
		for (final Connection connection : connections) {
			final WB_Segment S = new WB_Segment(connection.start(), connection.end());
			d = Math.min(d, WB_GeometryOp3D.getDistance3D(p, S));
		}
		return d;
	}

	public int getClosestNodeOnConnection(final WB_Coord p) {
		double mind = Double.POSITIVE_INFINITY;
		int q = -1;
		for (int i = 0; i < nodes.size(); i++) {
			final double d = WB_GeometryOp3D.getSqDistance3D(p, nodes.get(i));
			if (d < mind) {
				mind = d;
				q = i;
			}
		}
		return q;
	}

	public WB_Coord getClosestPointOnConnection(final WB_Coord p) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Coord q = new WB_Point(p);
		for (final Connection connection : connections) {
			final WB_Segment S = new WB_Segment(connection.start(), connection.end());
			final double d = WB_GeometryOp3D.getDistance3D(p, S);
			if (d < mind) {
				mind = d;
				q = WB_GeometryOp3D.getClosestPoint3D(S, p);
			}
		}
		return q;
	}

	public double getDistanceToConnection(final double x, final double y, final double z) {
		double d = Double.POSITIVE_INFINITY;
		for (final Connection connection : connections) {
			final WB_Segment S = new WB_Segment(connection.start(), connection.end());
			d = Math.min(d, WB_GeometryOp3D.getDistance3D(new WB_Point(x, y, z), S));
		}
		return d;
	}

	public WB_Coord getClosestPointOnConnection(final double x, final double y, final double z) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Coord q = new WB_Point(x, y, z);
		for (final Connection connection : connections) {
			final WB_Segment S = new WB_Segment(connection.start(), connection.end());
			final double d = WB_GeometryOp3D.getDistance3D(new WB_Point(x, y, z), S);
			if (d < mind) {
				mind = d;
				q = WB_GeometryOp3D.getClosestPoint3D(S, new WB_Point(x, y, z));
			}
		}
		return q;
	}

	public WB_Network smoothBiNodes() {
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

	public WB_Network smoothBiNodes(final int r) {
		for (int i = 0; i < r; i++) {
			smoothBiNodes();
		}
		return this;
	}

	public WB_Network smoothNodes() {
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

	public WB_Network smoothNodes(final int r) {
		for (int i = 0; i < r; i++) {
			smoothNodes();
		}
		return this;
	}

	public WB_Network refine(final double threshold) {
		WB_Network result = refineOnePass(threshold);
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

	public WB_Network refineOnePass(final double threshold) {
		final WB_Network result = new WB_Network();
		for (final Node node : nodes) {
			result.addNode(node, node.getValue());
		}
		for (final Connection connection : connections) {
			if (connection.getLength() > threshold) {
				final WB_Point start = connection.start();
				final WB_Point end = connection.end();
				final WB_Point mid = gf.createInterpolatedPoint(start, end, 0.5);
				result.addNode(mid, 0.5 * (connection.start().getValue() + connection.end().getValue()));
			}
		}
		final int n = getNumberOfNodes();
		int id = 0;
		for (final Connection connection : connections) {
			if (connection.getLength() > threshold) {
				final int start = connection.getStartIndex();
				final int end = connection.getEndIndex();
				result.addConnection(start, n + id);
				result.addConnection(n + id, end);
				id++;
			} else {
				final int start = connection.getStartIndex();
				final int end = connection.getEndIndex();
				result.addConnection(start, end);
			}
		}
		return result;
	}

	public WB_Network clipConnections(final WB_AABB aabb) {
		final List<Connection> toRemove = new WB_List<>();
		for (final Connection c : connections) {
			if (!aabb.contains(c.start()) || !aabb.contains(c.end())) {
				toRemove.add(c);
			}
		}
		for (final Connection c : toRemove) {
			removeConnection(c);
		}
		return this;
	}

	public WB_Network clipConnections(final WB_Coord center, final double d) {
		final double d2 = d * d;
		final List<Connection> toRemove = new WB_List<>();
		for (final Connection c : connections) {
			if (WB_Vector.getSqDistance3D(center, c.start()) > d2 || WB_Vector.getSqDistance3D(center, c.end()) > d2) {
				toRemove.add(c);
			}
		}
		for (final Connection c : toRemove) {
			removeConnection(c);
		}
		return this;
	}

	public List<WB_Point> toPointCloud(final int facets, final double connectionRadius, final double segmentLength,
			final int nodeDetail, final double nodeRadius, final double noise) {
		final List<WB_Point> points = new WB_PointList();
		double sl, dsl;
		int divs;
		WB_Plane P;
		WB_Vector u, localu, v;
		WB_Point p;
		final WB_RandomOnSphere rnd = new WB_RandomOnSphere();
		final double da = 2.0 * Math.PI / facets;
		for (final Connection connection : connections) {
			sl = connection.getLength() - 2 * nodeRadius;
			if (sl > 0) {
				divs = (int) WB_Math.max(1, Math.round(sl / segmentLength));
				dsl = sl / divs;
				P = connection.toPlane();
				u = P.getU().mul(connectionRadius);
				v = connection.toNormVector().copy();
				connection.start().addMul(nodeRadius, v);
				v.mulSelf(dsl);
				for (int i = 0; i <= divs; i++) {
					for (int j = 0; j < facets; j++) {
						p = connection.start().addMul(i, v);
						localu = u.copy();
						localu.rotateAboutAxisSelf(j * da + ((i % 2 == 0) ? 0.0 : 0.5 * da), new WB_Point(),
								P.getNormal());
						p.addSelf(localu);
						p.addSelf(rnd.nextVector().mulSelf(noise));
						points.add(p);
					}
				}
			}
		}
		for (final Node node : nodes) {
			final HE_Mesh ball = new HE_Mesh(
					new HEC_Geodesic().setRadius(nodeRadius).setB(nodeDetail).setC(0).setCenter(node));
			for (final WB_Coord q : ball.getVerticesAsCoord()) {
				points.add(new WB_Point(q).addSelf(rnd.nextVector().mulSelf(noise)));
			}
		}
		return points;
	}

	public class Node extends WB_Point {
		private final List<Connection> connections;
		protected final int index;
		protected double value;

		public Node(final WB_Coord pos, final int id, final double v) {
			super(pos);
			index = id;
			connections = new WB_List<>();
			value = v == 0 ? 10 * WB_Epsilon.EPSILON : v;
		}

		public boolean addConnection(final Connection connection) {
			if (connection.start() != this && connection.end() != this) {
				return false;
			}
			for (final Connection connection2 : connections) {
				if (connection2.start() == connection.start() && connection2.end() == connection.end()) {
					return false;
				}
			}
			connections.add(connection);
			return true;
		}

		public boolean removeConnection(final Connection connection) {
			if (connection.start() != this && connection.end() != this) {
				return false;
			}
			connections.remove(connection);
			return true;
		}

		public List<Connection> getConnections() {
			final List<Connection> result = new ArrayList<>();
			result.addAll(connections);
			return result;
		}

		public List<Node> getNeighbors() {
			final List<Node> result = new ArrayList<>();
			for (final Connection connection : connections) {
				if (connection.start() == this) {
					result.add(connection.end());
				} else {
					result.add(connection.start());
				}
			}
			return result;
		}

		public int getIndex() {
			return index;
		}

		public double findSmallestSpan() {
			double minAngle = Double.MAX_VALUE;
			for (int i = 0; i < getOrder(); i++) {
				minAngle = Math.min(minAngle, findSmallestSpanAroundConnection(i));
			}
			return minAngle;
		}

		public double findSmallestSpanAroundConnection(final Connection connection) {
			return findSmallestSpanAroundConnection(connections.indexOf(connection));
		}

		public double findSmallestSpanAroundConnection(final int i) {
			final int n = connections.size();
			if (i < 0 || i >= n) {
				throw new IllegalArgumentException("Index beyond connection range.");
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

		public double findShortestConnection() {
			double minLength = Double.MAX_VALUE;
			for (final Connection connection : connections) {
				minLength = Math.min(minLength, connection.getSqLength());
			}
			return Math.sqrt(minLength);
		}

		public int getOrder() {
			return connections.size();
		}

		public double getValue() {
			return value;
		}

		public void setValue(final double v) {
			value = v == 0 ? 10 * WB_Epsilon.EPSILON : v;
		}

		public Connection getConnection(final int index) {
			if (index < 0 || index >= connections.size()) {
				throw new IllegalArgumentException("Index outside of connection range.");
			}
			return connections.get(index);
		}

		public void removeConnection(final int index) {
			if (index < 0 || index >= connections.size()) {
				throw new IllegalArgumentException("Index outside of connection range.");
			}
			connections.remove(index);
		}

		public Node getNeighbor(final int index) {
			if (index < 0 || index >= connections.size()) {
				throw new IllegalArgumentException("Index outside of connection range.");
			}
			if (connections.get(index).start() == this) {
				return connections.get(index).end();
			}
			return connections.get(index).start();
		}

		public WB_Point toPoint() {
			return new WB_Point(xd(), yd(), zd());
		}
	}

	public class Connection {
		private final Node start;
		private final Node end;
		private final int index;
		private double radiuss;
		private double radiuse;
		private double offsets;
		private double offsete;

		public Connection(final Node s, final Node e, final int id) {
			start = s;
			end = e;
			index = id;
		}

		public Connection(final Node s, final Node e, final int id, final double r) {
			start = s;
			end = e;
			index = id;
			radiuss = radiuse = r;
		}

		public Connection(final Node s, final Node e, final int id, final double rs, final double re) {
			start = s;
			end = e;
			index = id;
			radiuss = rs;
			radiuse = re;
		}

		public Node start() {
			return start;
		}

		public Node end() {
			return end;
		}

		public int getStartIndex() {
			return start.getIndex();
		}

		public int getEndIndex() {
			return end.getIndex();
		}

		public int getIndex() {
			return index;
		}

		public WB_Vector toVector() {
			return end().subToVector3D(start());
		}

		public WB_Vector toNormVector() {
			final WB_Vector v = end().subToVector3D(start());
			v.normalizeSelf();
			return v;
		}

		public double getSqLength() {
			return WB_GeometryOp3D.getSqDistance3D(end(), start());
		}

		public double getLength() {
			return WB_GeometryOp3D.getDistance3D(end(), start());
		}

		public double getRadiusStart() {
			return radiuss;
		}

		public double getRadiusEnd() {
			return radiuse;
		}

		public void setRadiusStart(final double r) {
			radiuss = r;
		}

		public void setRadiusEnd(final double r) {
			radiuse = r;
		}

		public double getOffsetStart() {
			return offsets;
		}

		public double getOffsetEnd() {
			return offsete;
		}

		public void setOffsetStart(final double o) {
			offsets = o;
		}

		public void setOffsetEnd(final double o) {
			offsete = o;
		}

		public WB_Point getCenter() {
			return end().add(start()).mulSelf(0.5);
		}

		public WB_Segment toSegment() {
			return new WB_Segment(start, end);
		}

		public WB_Plane toPlane() {
			return new WB_Plane(start().toPoint(), toVector());
		}
	}
}
