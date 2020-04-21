package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import wblut.geom.WB_List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Geodesic;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Network;
import wblut.geom.WB_Network.Connection;
import wblut.geom.WB_Point;
import wblut.geom.WB_SimpleMesh;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation3D;

public class HET_MeshNetwork {
	private final MeshNode[] nodes;
	private int lastSource;

	public HET_MeshNetwork(final WB_SimpleMesh mesh) {
		nodes = new MeshNode[mesh.getNumberOfVertices()];
		for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
			nodes[i] = new MeshNode(i, mesh.getVertex(i));
		}
		final int[][] meshedges = mesh.getEdgesAsInt();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (final int[] meshedge : meshedges) {
			if (meshedge[0] != meshedge[1]) {
				p0 = mesh.getVertex(meshedge[0]);
				p1 = mesh.getVertex(meshedge[1]);
				d = WB_GeometryOp3D.getDistance3D(p0, p1);
				v0 = nodes[meshedge[0]];
				v1 = nodes[meshedge[1]];
				v0.neighbors.add(new MeshConnection(v1, d));
				v1.neighbors.add(new MeshConnection(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshNetwork(final HE_Mesh mesh) {
		this(mesh, 0.0);
	}

	public HET_MeshNetwork(final HE_Mesh mesh, final double offset) {
		nodes = new MeshNode[mesh.getNumberOfVertices()];
		if (offset == 0.0) {
			for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
				nodes[i] = new MeshNode(i, new WB_Point(mesh.getVertex(i)));
			}
		} else {
			for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
				nodes[i] = new MeshNode(i, new WB_Point(mesh.getVertex(i)).addMulSelf(offset, mesh.getVertexNormal(i)));
			}
		}
		final int[][] meshedges = mesh.getEdgesAsInt();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (final int[] meshedge : meshedges) {
			if (meshedge[0] != meshedge[1]) {
				p0 = mesh.getVertex(meshedge[0]);
				p1 = mesh.getVertex(meshedge[1]);
				d = WB_GeometryOp3D.getDistance3D(p0, p1);
				v0 = nodes[meshedge[0]];
				v1 = nodes[meshedge[1]];
				v0.neighbors.add(new MeshConnection(v1, d));
				v1.neighbors.add(new MeshConnection(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshNetwork(final List<? extends WB_Coord> points, final WB_Triangulation3D triangulation) {
		nodes = new MeshNode[points.size()];
		for (int i = 0; i < points.size(); i++) {
			nodes[i] = new MeshNode(i, points.get(i));
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points.get(meshedges[i]);
				p1 = points.get(meshedges[i + 1]);
				d = WB_GeometryOp3D.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new MeshConnection(v1, d));
				v1.neighbors.add(new MeshConnection(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshNetwork(final WB_Coord[] points, final WB_Triangulation3D triangulation) {
		nodes = new MeshNode[points.length];
		for (int i = 0; i < points.length; i++) {
			nodes[i] = new MeshNode(i, points[i]);
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points[meshedges[i]];
				p1 = points[meshedges[i + 1]];
				d = WB_GeometryOp3D.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new MeshConnection(v1, d));
				v1.neighbors.add(new MeshConnection(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshNetwork(final WB_Coord[] points, final WB_Triangulation2D triangulation) {
		nodes = new MeshNode[points.length];
		for (int i = 0; i < points.length; i++) {
			nodes[i] = new MeshNode(i, points[i]);
		}
		final int[] meshedges = triangulation.getEdges();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (int i = 0; i < meshedges.length; i += 2) {
			if (meshedges[i] != meshedges[i + 1]) {
				p0 = points[meshedges[i]];
				p1 = points[meshedges[i + 1]];
				d = WB_GeometryOp3D.getDistance3D(p0, p1);
				v0 = nodes[meshedges[i]];
				v1 = nodes[meshedges[i + 1]];
				v0.neighbors.add(new MeshConnection(v1, d));
				v1.neighbors.add(new MeshConnection(v0, d));
			}
		}
		lastSource = -1;
	}

	public HET_MeshNetwork(final WB_Network network) {
		nodes = new MeshNode[network.getNumberOfNodes()];
		for (int i = 0; i < network.getNumberOfNodes(); i++) {
			nodes[i] = new MeshNode(i, network.getNode(i));
		}
		final List<WB_Network.Connection> connections = network.getConnections();
		WB_Coord p0;
		WB_Coord p1;
		MeshNode v0;
		MeshNode v1;
		double d;
		for (final Connection connection : connections) {
			p0 = network.getNode(connection.getStartIndex());
			p1 = network.getNode(connection.getEndIndex());
			d = WB_GeometryOp3D.getDistance3D(p0, p1);
			v0 = nodes[connection.getStartIndex()];
			v1 = nodes[connection.getEndIndex()];
			v0.neighbors.add(new MeshConnection(v1, d));
			v1.neighbors.add(new MeshConnection(v0, d));
		}
		lastSource = -1;
	}

	public int getNodeIndex(final int i) {
		return nodes[i].index;
	}

	public void computePathsToVertex(final int i) {
		final MeshNode source = nodes[i];
		for (final MeshNode node : nodes) {
			node.reset();
		}
		source.distanceToSource = 0.;
		final PriorityQueue<MeshNode> vertexQueue = new PriorityQueue<>();
		vertexQueue.add(source);
		while (!vertexQueue.isEmpty()) {
			final MeshNode u = vertexQueue.poll();
			// Visit each edge exiting u
			for (final MeshConnection e : u.neighbors) {
				final MeshNode v = e.target;
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

	public int[] getShortestPathBetweenVertices(final int source, final int target) {
		if (source != lastSource) {
			computePathsToVertex(source);
		}
		if (source == target) {
			return new int[] { source };
		}
		final List<MeshNode> path = new ArrayList<>();
		for (MeshNode vertex = nodes[target]; vertex != null; vertex = vertex.previous) {
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

	public WB_Network getNetwork(final int i) {
		final WB_Network network = new WB_Network();
		computePathsToVertex(i);
		for (final MeshNode v : nodes) {
			network.addNode(v.x, v.y, v.z, 0);
		}
		for (final MeshNode v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			for (int j = 0; j < path.length - 1; j++) {
				network.getNode(path[j])
						.setValue(Math.max(network.getNode(path[j]).getValue(), 1.0 - j * 1.0 / path.length));
				network.addConnection(path[j], path[j + 1]);
			}
			network.getNode(path[path.length - 1])
					.setValue(Math.max(network.getNode(path[path.length - 1]).getValue(), 1.0 / path.length));
		}
		return network;
	}

	public WB_Network getNetwork(final int i, final int maxnodes) {
		final WB_Network network = new WB_Network();
		computePathsToVertex(i);
		for (final MeshNode v : nodes) {
			network.addNode(v.x, v.y, v.z, 0);
		}
		for (final MeshNode v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length);
			for (int j = 0; j < nodes - 1; j++) {
				network.getNode(path[j]).setValue(Math.max(network.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes));
				network.addConnection(path[j], path[j + 1]);
			}
			network.getNode(path[nodes - 1])
					.setValue(Math.max(network.getNode(path[nodes - 1]).getValue(), 1.0 / nodes));
		}
		return network;
	}

	public WB_Network getNetwork(final int i, final int maxnodes, final double offset) {
		final WB_Network network = new WB_Network();
		computePathsToVertex(i);
		for (final MeshNode v : nodes) {
			network.addNode(v.x, v.y, v.z, 0);
		}
		for (final MeshNode v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length);
			for (int j = 0; j < nodes - 1; j++) {
				network.getNode(path[j])
						.setValue(Math.max(network.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes + offset));
				network.addConnection(path[j], path[j + 1]);
			}
			network.getNode(path[nodes - 1])
					.setValue(Math.max(network.getNode(path[nodes - 1]).getValue(), 1.0 / nodes + offset));
		}
		return network;
	}

	public WB_Network getNetwork(final int i, final int maxnodes, final int cuttail) {
		final WB_Network network = new WB_Network();
		computePathsToVertex(i);
		for (final MeshNode v : nodes) {
			network.addNode(v.x, v.y, v.z, 0);
		}
		for (final MeshNode v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length - cuttail);
			if (nodes <= 1) {
				continue;
			}
			for (int j = 0; j < nodes - 1; j++) {
				network.getNode(path[j]).setValue(Math.max(network.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes));
				network.addConnection(path[j], path[j + 1]);
			}
			network.getNode(path[nodes - 1])
					.setValue(Math.max(network.getNode(path[nodes - 1]).getValue(), 1.0 / nodes));
		}
		return network;
	}

	public WB_Network getNetwork(final int i, final int maxnodes, final int start, final int cuttail) {
		final WB_Network network = new WB_Network();
		computePathsToVertex(i);
		for (final MeshNode v : nodes) {
			network.addNode(v.x, v.y, v.z, 0);
		}
		for (final MeshNode v : nodes) {
			final int[] path = getShortestPathBetweenVertices(i, v.index);
			final int nodes = Math.min(maxnodes, path.length - cuttail);
			if (nodes <= 1) {
				continue;
			}
			for (int j = start; j < nodes - 1; j++) {
				network.getNode(path[j]).setValue(Math.max(network.getNode(path[j]).getValue(), 1.0 - j * 1.0 / nodes));
				network.addConnection(path[j], path[j + 1]);
			}
			network.getNode(path[nodes - 1])
					.setValue(Math.max(network.getNode(path[nodes - 1]).getValue(), 1.0 / nodes));
		}
		return network;
	}

	public static void main(final String[] args) {
		final WB_Geodesic geo = new WB_Geodesic(1.0, 2, 0, WB_Geodesic.Type.ICOSAHEDRON);
		HET_MeshNetwork graph = new HET_MeshNetwork(geo.create());
		for (final MeshNode v : graph.nodes) {
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
		graph = new HET_MeshNetwork(mesh);
		for (final MeshNode v : graph.nodes) {
			final int[] path = graph.getShortestPathBetweenVertices(0, v.index);
			System.out.println("Distance to " + v + ": " + v.distanceToSource);
			System.out.print("Path: ");
			for (int i = 0; i < path.length - 1; i++) {
				System.out.print(path[i] + "->");
			}
			System.out.println(path[path.length - 1] + ".");
		}
		for (final MeshNode v : graph.nodes) {
			final int[] path = graph.getShortestPathBetweenVertices(5, v.index);
			System.out.println("Distance to " + v + ": " + v.distanceToSource);
			System.out.print("Path: ");
			for (int i = 0; i < path.length - 1; i++) {
				System.out.print(path[i] + "->");
			}
			System.out.println(path[path.length - 1] + ".");
		}
	}

	public class MeshNode implements Comparable<MeshNode> {
		public final int index;
		public List<MeshConnection> neighbors;
		public double distanceToSource = Double.POSITIVE_INFINITY;
		public MeshNode previous;
		public double x, y, z;

		public MeshNode(final int id, final WB_Coord pos) {
			index = id;
			neighbors = new WB_List<>();
			x = pos.xd();
			y = pos.yd();
			z = pos.zd();
		}

		@Override
		public String toString() {
			return "Vertex " + index;
		}

		@Override
		public int compareTo(final MeshNode other) {
			return Double.compare(distanceToSource, other.distanceToSource);
		}

		public void reset() {
			distanceToSource = Double.POSITIVE_INFINITY;
			previous = null;
		}
	}

	public class MeshConnection {
		public final MeshNode target;
		public final double weight;

		public MeshConnection(final MeshNode target, final double weight) {
			this.target = target;
			this.weight = weight;
		}
	}

	public static List<HET_MeshNetwork> getAllNetworks(final HE_Mesh mesh) {
		final HE_MeshCollection meshes = new HEMC_Explode().setMesh(mesh).create();
		final List<HET_MeshNetwork> graphs = new ArrayList<>();
		for (int i = 0; i < meshes.getNumberOfMeshes(); i++) {
			graphs.add(new HET_MeshNetwork(meshes.getMesh(i)));
		}
		return graphs;
	}
}
