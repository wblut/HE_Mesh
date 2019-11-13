/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/

 * I , Frederik Vanhoutte, have waived all copyright and related or neighboring
 * rights.
 *
 * This work is published from Belgium. (http://creativecommons.org/publicdomain/zero/1.0/)
 *
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Geodesic;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_Network;
import wblut.geom.WB_Point;
import wblut.geom.WB_SimpleMesh;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation3D;

/**
 *
 */
public class HET_MeshGraph {
	/**
	 *
	 */
	private final Node[] nodes;
	/**
	 *
	 */
	private int lastSource;

	/**
	 *
	 *
	 * @param mesh
	 */
	public HET_MeshGraph(final WB_SimpleMesh mesh) {
		nodes = new Node[mesh.getNumberOfVertices()];
		for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
			nodes[i] = new Node(i, mesh.getVertex(i));
		}
		final int[][] meshedges = mesh.getEdgesAsInt();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < meshedges.length; i++) {
			if (meshedges[i][0] != meshedges[i][1]) {
				p0 = mesh.getVertex(meshedges[i][0]);
				p1 = mesh.getVertex(meshedges[i][1]);
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i][0]];
				v1 = nodes[meshedges[i][1]];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			}
		}
		lastSource = -1;
	}



	/**
	 *
	 *
	 * @param mesh
	 */
	public HET_MeshGraph(final HE_Mesh mesh) {
		this(mesh, 0.0);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param offset
	 */
	public HET_MeshGraph(final HE_Mesh mesh, final double offset) {

		nodes = new Node[mesh.getNumberOfVertices()];
		if (offset == 0.0) {
			for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
				nodes[i] = new Node(i, new WB_Point(mesh.getVertex(i)));
			}
		} else {
			for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
				nodes[i] = new Node(i,
						new WB_Point(mesh.getVertex(i)).addMulSelf(offset, mesh.getVertexNormal(i)));
			}

		}
		final int[][] meshedges = mesh.getEdgesAsInt();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < meshedges.length; i++) {
			if (meshedges[i][0] != meshedges[i][1]) {
				p0 = mesh.getVertex(meshedges[i][0]);
				p1 = mesh.getVertex(meshedges[i][1]);
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i][0]];
				v1 = nodes[meshedges[i][1]];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshGraph(final List<? extends WB_Coord> points, final WB_Triangulation3D triangulation) {
		nodes = new Node[points.size()];
		for (int i = 0; i < points.size(); i++) {
			nodes[i] = new Node(i, points.get(i));
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points.get(meshedges[i]);
				p1 = points.get(meshedges[i + 1]);
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshGraph(final WB_Coord[] points, final WB_Triangulation3D triangulation) {
		nodes = new Node[points.length];
		for (int i = 0; i < points.length; i++) {
			nodes[i] = new Node(i, points[i]);
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points[meshedges[i]];
				p1 = points[meshedges[i + 1]];
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshGraph(final WB_Coord[] points, final WB_Triangulation2D triangulation) {
		nodes = new Node[points.length];
		for (int i = 0; i < points.length; i++) {
			nodes[i] = new Node(i, points[i]);
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points[meshedges[i]];
				p1 = points[meshedges[i + 1]];
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			}
		}
		lastSource = -1;
	}

	
	public HET_MeshGraph(final WB_Network network) {
		nodes = new Node[network.getNumberOfNodes()];
		for (int i = 0; i < network.getNumberOfNodes(); i++) {
			nodes[i] = new Node(i, network.getNode(i));
		}
		final List<WB_Network.Connection>connections =network.getConnections();
		WB_Coord p0;
		WB_Coord p1;
		Node v0;
		Node v1;
		double d;
		for (int i = 0; i < connections.size(); i ++) {
			
				p0 = network.getNode(connections.get(i).getStartIndex());
				p1 = network.getNode(connections.get(i).getEndIndex());
				d = WB_CoordOp.getDistance3D(p0, p1);
				v0 = nodes[connections.get(i).getStartIndex()];
				v1 = nodes[connections.get(i).getEndIndex()];
				v0.neighbors.add(new Edge(v1, d));
				v1.neighbors.add(new Edge(v0, d));
			
		}
		lastSource = -1;
	}

	
	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public int getNodeIndex(final int i) {
		return nodes[i].index;
	}

	/**
	 *
	 *
	 * @param i
	 */
	public void computePathsToVertex(final int i) {
		final Node source = nodes[i];
		for (int j = 0; j < nodes.length; j++) {
			nodes[j].reset();
		}
		source.distanceToSource = 0.;
		final PriorityQueue<Node> vertexQueue = new PriorityQueue<Node>();
		vertexQueue.add(source);
		while (!vertexQueue.isEmpty()) {
			final Node u = vertexQueue.poll();
			// Visit each edge exiting u
			for (final Edge e : u.neighbors) {
				final Node v = e.target;
				final double weight = e.weight;
				final double distanceThroughU = u.distanceToSource + weight;
				if (distanceThroughU < v.distanceToSource) {
					vertexQueue.remove(v);
					v.distanceToSource = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
		lastSource = i;
	}

	/**
	 *
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public int[] getShortestPathBetweenVertices(final int source, final int target) {
		if (source != lastSource) {
			computePathsToVertex(source);
		}
		if (source == target) {
			return new int[] { source };
		}
		final List<Node> path = new ArrayList<Node>();
		for (Node vertex = nodes[target]; vertex != null; vertex = vertex.previous) {
			path.add(vertex);
		}
		Collections.reverse(path);
		final int[] result = new int[path.size()];
		for (int i = 0; i < path.size(); i++) {
			result[i] = path.get(i).index;
		}
		return result;
	}

	public double getShortestDistanceBetweenVertices(final int source, final int target) {
		if (source != lastSource) {
			computePathsToVertex(source);
		}
		if (source == target) {
			return 0.0;
		}
		return nodes[target].distanceToSource;

	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	public WB_Network getNetwork(final int i) {
		final WB_Network frame = new WB_Network();
		computePathsToVertex(i);
		for (final Node v : nodes) {
			frame.addNode(v.x, v.y, v.z, 0);
		}
		for (final Node v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			for (int j = 0; j < path.length - 1; j++) {
				frame.getNode(path[j])
						.setValue(Math.max(frame.getNode(path[j]).getValue(), 1.0 - j * 1.0 / path.length));
				frame.addConnection(path[j], path[j + 1]);
			}
			frame.getNode(path[path.length - 1])
					.setValue(Math.max(frame.getNode(path[path.length - 1]).getValue(), 1.0 / path.length));
		}
		return frame;
	}

	/**
	 *
	 *
	 * @param i
	 * @param maxnodes
	 * @return
	 */
	public WB_Network getNetwork(final int i, final int maxnodes) {
		final WB_Network frame = new WB_Network();
		computePathsToVertex(i);
		for (final Node v : nodes) {
			frame.addNode(v.x, v.y, v.z, 0);
		}
		for (final Node v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length);
			for (int j = 0; j < nodes - 1; j++) {
				frame.getNode(path[j]).setValue(Math.max(frame.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes));
				frame.addConnection(path[j], path[j + 1]);
			}
			frame.getNode(path[nodes - 1]).setValue(Math.max(frame.getNode(path[nodes - 1]).getValue(), 1.0 / nodes));
		}
		return frame;
	}

	public WB_Network getNetwork(final int i, final int maxnodes, final double offset) {
		final WB_Network frame = new WB_Network();
		computePathsToVertex(i);
		for (final Node v : nodes) {
			frame.addNode(v.x, v.y, v.z, 0);
		}
		for (final Node v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length);
			for (int j = 0; j < nodes - 1; j++) {
				frame.getNode(path[j])
						.setValue(Math.max(frame.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes + offset));
				frame.addConnection(path[j], path[j + 1]);
			}
			frame.getNode(path[nodes - 1])
					.setValue(Math.max(frame.getNode(path[nodes - 1]).getValue(), 1.0 / nodes + offset));
		}
		return frame;
	}

	/**
	 *
	 *
	 * @param i
	 * @param maxnodes
	 * @param cuttail
	 * @return
	 */
	public WB_Network getNetwork(final int i, final int maxnodes, final int cuttail) {
		final WB_Network frame = new WB_Network();
		computePathsToVertex(i);
		for (final Node v : nodes) {
			frame.addNode(v.x, v.y, v.z, 0);
		}
		for (final Node v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length - cuttail);
			if (nodes <= 1) {
				continue;
			}
			for (int j = 0; j < nodes - 1; j++) {
				frame.getNode(path[j]).setValue(Math.max(frame.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes));
				frame.addConnection(path[j], path[j + 1]);
			}
			frame.getNode(path[nodes - 1]).setValue(Math.max(frame.getNode(path[nodes - 1]).getValue(), 1.0 / nodes));
		}
		return frame;
	}

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final WB_Geodesic geo = new WB_Geodesic(1.0, 2, 0, WB_Geodesic.Type.ICOSAHEDRON);
		HET_MeshGraph graph = new HET_MeshGraph(geo.create());
		for (final Node v : graph.nodes) {
			final int[] path = graph.getShortestPathBetweenVertices(5, v.index);
			System.out.println("Distance to " + v + ": " + v.distanceToSource);
			System.out.print("Path: ");
			for (int i = 0; i < path.length - 1; i++) {
				System.out.print(path[i] + "->");
			}
			System.out.println(path[path.length - 1] + ".");
		}
		final HE_Mesh mesh = new HE_Mesh(geo.create());
		mesh.smooth();
		graph = new HET_MeshGraph(mesh);
		for (final Node v : graph.nodes) {
			final int[] path = graph.getShortestPathBetweenVertices(0, v.index);
			System.out.println("Distance to " + v + ": " + v.distanceToSource);
			System.out.print("Path: ");
			for (int i = 0; i < path.length - 1; i++) {
				System.out.print(path[i] + "->");
			}
			System.out.println(path[path.length - 1] + ".");
		}
		for (final Node v : graph.nodes) {
			final int[] path = graph.getShortestPathBetweenVertices(5, v.index);
			System.out.println("Distance to " + v + ": " + v.distanceToSource);
			System.out.print("Path: ");
			for (int i = 0; i < path.length - 1; i++) {
				System.out.print(path[i] + "->");
			}
			System.out.println(path[path.length - 1] + ".");
		}
	}

	/**
	 *
	 */
	public class Node implements Comparable<Node> {
		/**
		 *
		 */
		public final int index;
		/**
		 *
		 */
		public List<Edge> neighbors;
		/**
		 *
		 */
		public double distanceToSource = Double.POSITIVE_INFINITY;
		/**
		 *
		 */
		public Node previous;
		/**
		 *
		 */
		public double x, y, z;

		/**
		 *
		 *
		 * @param id
		 * @param pos
		 */
		public Node(final int id, final WB_Coord pos) {
			index = id;
			neighbors = new FastList<Edge>();
			x = pos.xd();
			y = pos.yd();
			z = pos.zd();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Vertex " + index;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(final Node other) {
			return Double.compare(distanceToSource, other.distanceToSource);
		}

		/**
		 *
		 */
		public void reset() {
			distanceToSource = Double.POSITIVE_INFINITY;
			previous = null;
		}

	}

	/**
	 *
	 */
	public class Edge {
		/**
		 *
		 */
		public final Node target;
		/**
		 *
		 */
		public final double weight;

		/**
		 *
		 *
		 * @param target
		 * @param weight
		 */
		public Edge(final Node target, final double weight) {
			this.target = target;
			this.weight = weight;
		}
	}

	public static List<HET_MeshGraph> getAllGraphs(final HE_Mesh mesh) {
		HE_MeshCollection meshes = new HEMC_Explode().setMesh(mesh).create();
		List<HET_MeshGraph> graphs = new ArrayList<HET_MeshGraph>();
		for (int i = 0; i < meshes.getNumberOfMeshes(); i++) {
			graphs.add(new HET_MeshGraph(meshes.getMesh(i)));
		}
		return graphs;

	}
}
