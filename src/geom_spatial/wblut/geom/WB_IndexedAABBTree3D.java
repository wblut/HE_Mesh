package wblut.geom;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.core.WB_ProgressReporter.WB_ProgressTracker;

public class WB_IndexedAABBTree3D {
	private WB_IndexedAABBNode3D root;
	private final int maxLevel;
	private int depth;
	private final int maxNumberOfFaces;
	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	public WB_IndexedAABBTree3D(final int[] tetrahedra, final WB_CoordCollection points, final int mnof) {
		final List<WB_IndexedTetrahedron> mesh = new FastList<>();
		for (int i = 0; i < tetrahedra.length; i += 4) {
			mesh.add(new WB_IndexedTetrahedron(i, tetrahedra, points));
		}
		maxLevel = 2 * (int) Math.ceil(Math.log(mesh.size()) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(mesh);
	}

	public WB_IndexedAABBTree3D(final WB_Triangulation3D triangulation, final WB_CoordCollection points,
			final int mnof) {
		final List<WB_IndexedTetrahedron> mesh = new FastList<>();
		for (int i = 0; i < triangulation.getTetrahedra().length; i += 4) {
			mesh.add(new WB_IndexedTetrahedron(i, triangulation.getTetrahedra(), points));
		}
		maxLevel = 2 * (int) Math.ceil(Math.log(mesh.size()) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(mesh);
	}

	public WB_IndexedAABBTree3D(final WB_AlphaTriangulation3D triangulation, final double alpha, final int mnof) {
		final List<WB_IndexedTetrahedron> mesh = new FastList<>();
		final int[] alphatri = triangulation.getAlphaTriangles(alpha);
		for (int i = 0; i < alphatri.length; i += 4) {
			mesh.add(new WB_IndexedTetrahedron(i, alphatri,
					WB_CoordCollection.getCollection(triangulation.getPoints())));
		}
		maxLevel = 2 * (int) Math.ceil(Math.log(mesh.size()) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(mesh);
	}

	public WB_IndexedAABBTree3D(final Collection<? extends WB_IndexedTetrahedron> mesh, final int mnof) {
		maxLevel = 2 * (int) Math.ceil(Math.log(mesh.size()) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(mesh);
	}

	private void buildTree(final Collection<? extends WB_IndexedTetrahedron> mesh) {
		tracker.setStartStatus(this,
				"Starting WB_AABBTree construction. Max. number of faces per node: " + maxNumberOfFaces);
		root = new WB_IndexedAABBNode3D();
		final List<WB_IndexedTetrahedron> faces = new FastList<>();
		faces.addAll(mesh);
		buildNode(root, faces, mesh, 0);
		tracker.setStopStatus(this, "Exiting WB_AABBTree construction.");
	}

	private void buildNode(final WB_IndexedAABBNode3D node, final List<WB_IndexedTetrahedron> faces,
			final Collection<? extends WB_IndexedTetrahedron> mesh, final int level) {
		tracker.setDuringStatus(this, "Splitting WB_AABBNode level " + level + " with " + faces.size() + " faces.");
		node.level = level;
		node.aabb = new WB_AABB();
		for (final WB_IndexedTetrahedron f : faces) {
			node.aabb.expandToInclude(f.getAABB());
		}
		if (level == maxLevel || faces.size() <= maxNumberOfFaces) {
			node.faces.addAll(faces);
			node.isLeaf = true;
			depth = Math.max(depth, node.level);
			return;
		}
		final List<WB_IndexedTetrahedron> subsetA = new FastList<>();
		final List<WB_IndexedTetrahedron> subsetB = new FastList<>();
		double sah = Double.POSITIVE_INFINITY;
		for (int i = 0; i < 2; i++) {
			final WB_TetrahedronSort fs = new WB_TetrahedronSort.WB_AABBSortMax1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}
		for (int i = 0; i < 2; i++) {
			final WB_TetrahedronSort fs = new WB_TetrahedronSort.WB_AABBSortCenter1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}
		for (int i = 0; i < 2; i++) {
			final WB_TetrahedronSort fs = new WB_TetrahedronSort.WB_TetrahedronSortCenter1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}
		final List<WB_IndexedTetrahedron> childA = new FastList<>();
		final List<WB_IndexedTetrahedron> childB = new FastList<>();
		if (subsetA.size() < subsetB.size()) {
			childA.addAll(subsetB);
			childB.addAll(subsetA);
		} else {
			childA.addAll(subsetA);
			childB.addAll(subsetB);
		}
		node.isLeaf = true;
		if (childB.size() > 0) {
			node.childB = new WB_IndexedAABBNode3D();
			buildNode(node.childB, childB, mesh, level + 1);
			node.isLeaf = false;
		}
		if (childA.size() > 0) {
			node.childA = new WB_IndexedAABBNode3D();
			buildNode(node.childA, childA, mesh, level + 1);
			node.isLeaf = false;
		}
	}

	private double findOptimalSubset(double bestSah, final WB_IndexedAABBNode3D node,
			final List<WB_IndexedTetrahedron> childA, final List<WB_IndexedTetrahedron> childB,
			final List<WB_IndexedTetrahedron> faces) {
		final int items = faces.size();
		final double invdenom = 1 / node.getAABB().getArea();
		final double[] surfaceAreaA = new double[items - 1];
		final double[] surfaceAreaB = new double[items - 1];
		final WB_AABB aabbA = new WB_AABB();
		for (int i = 0; i < items - 1; i++) {
			aabbA.expandToInclude(faces.get(i).getAABB());
			surfaceAreaA[i] = aabbA.getArea();
		}
		final WB_AABB aabbB = new WB_AABB();
		for (int i = items - 2; i >= 0; i--) {
			aabbB.expandToInclude(faces.get(i + 1).getAABB());
			surfaceAreaB[i] = aabbB.getArea();
		}
		int sizeA = -1;
		for (int i = 0, numA = 1; i < items - 1; i++, numA++) {
			final double currentSah = getSAH(invdenom, surfaceAreaA[i], numA, surfaceAreaB[i], items - numA);
			if (currentSah < bestSah) {
				bestSah = currentSah;
				sizeA = numA;
			}
		}
		if (sizeA >= 0) {
			childA.clear();
			childB.clear();
			for (int i = 0; i < sizeA; i++) {
				childA.add(faces.get(i));
			}
			for (int i = sizeA; i < faces.size(); i++) {
				childB.add(faces.get(i));
			}
		}
		return bestSah;
	}

	static private double getSAH(final double denom, final double surfaceAreaA, final int numA,
			final double surfaceAreaB, final int numB) {
		return (surfaceAreaA * numA + surfaceAreaB * numB) * denom;
	}

	public WB_IndexedAABBNode3D getRoot() {
		return root;
	}

	public int getDepth() {
		return depth;
	}

	public WB_Coord getClosestPoint(final WB_Coord p) {
		final PriorityQueue<Entry> entries = new PriorityQueue<>(new EntryOrder());
		double closest2 = Double.POSITIVE_INFINITY;
		closest2 = addNode(p, root, entries, closest2);
		Entry top = entries.poll();
		while (top.type == 0) {
			if (top.node.getChildA() != null) {
				closest2 = addNode(p, top.node.getChildA(), entries, closest2);
			}
			if (top.node.getChildB() != null) {
				closest2 = addNode(p, top.node.getChildB(), entries, closest2);
			}
			top = entries.poll();
		}
		return top.point;
	}

	public int[] getClosestFace(final WB_Coord p) {
		final PriorityQueue<Entry> entries = new PriorityQueue<>(new EntryOrder());
		double closest2 = Double.POSITIVE_INFINITY;
		closest2 = addNode(p, root, entries, closest2);
		Entry top = entries.poll();
		while (top.type == 0) {
			if (top.node.getChildA() != null) {
				closest2 = addNode(p, top.node.getChildA(), entries, closest2);
			}
			if (top.node.getChildB() != null) {
				closest2 = addNode(p, top.node.getChildB(), entries, closest2);
			}
			top = entries.poll();
		}
		return top.face;
	}

	public Entry getClosestEntry(final WB_Coord p) {
		final PriorityQueue<Entry> entries = new PriorityQueue<>(new EntryOrder());
		double closest2 = Double.POSITIVE_INFINITY;
		closest2 = addNode(p, root, entries, closest2);
		Entry top = entries.poll();
		while (top.type == 0) {
			if (top.node.getChildA() != null) {
				closest2 = addNode(p, top.node.getChildA(), entries, closest2);
			}
			if (top.node.getChildB() != null) {
				closest2 = addNode(p, top.node.getChildB(), entries, closest2);
			}
			top = entries.poll();
		}
		return top;
	}

	private double addNode(final WB_Coord p, final WB_IndexedAABBNode3D node, final PriorityQueue<Entry> entries,
			double closest2) {
		final double d2 = WB_GeometryOp3D.getSqDistance3D(p, node.aabb);
		if (d2 <= closest2) {
			entries.add(new Entry(null, d2, 0, null, node));
			if (node.isLeaf()) {
				for (final WB_IndexedTetrahedron f : node.faces) {
					final WB_Coord q = WB_GeometryOp3D.getClosestPoint3D(p, f);
					final double fd2 = WB_GeometryOp3D.getSqDistance3D(p, q);
					if (fd2 < closest2) {
						entries.add(new Entry(q, fd2, 1, f, node));
						closest2 = fd2;
					}
				}
			}
		}
		return closest2;
	}

	private class EntryOrder implements Comparator<Entry> {
		@Override
		public int compare(final Entry arg0, final Entry arg1) {
			return Double.compare(arg0.d2, arg1.d2);
		}
	}

	private class Entry {
		WB_Coord point;
		double d2;
		int type; // 0=aabb, 1=face
		WB_IndexedAABBNode3D node;
		int[] face;

		Entry(final WB_Coord p, final double d2, final int type, final WB_IndexedTetrahedron f,
				final WB_IndexedAABBNode3D node) {
			this.point = p;
			this.d2 = d2;
			this.type = type;
			this.face = f == null ? null : new int[] { f.i1(), f.i2(), f.i3(), f.i4() };
			this.node = node;
		}
	}

	public void expandBy(final double d) {
		root.expandBy(d);
	}

	public class WB_IndexedAABBNode3D {
		protected int level;
		protected WB_AABB aabb = null;
		protected WB_IndexedAABBNode3D childA = null;
		protected WB_IndexedAABBNode3D childB = null;
		protected List<WB_IndexedTetrahedron> faces;
		protected boolean isLeaf;

		public WB_IndexedAABBNode3D() {
			level = -1;
			faces = new FastList<>();
		}

		public WB_AABB getAABB() {
			return aabb;
		}

		public int getLevel() {
			return level;
		}

		public boolean isLeaf() {
			return isLeaf;
		}

		public List<WB_IndexedTetrahedron> getFaces() {
			return faces;
		}

		public WB_IndexedAABBNode3D getChildA() {
			return childA;
		}

		public WB_IndexedAABBNode3D getChildB() {
			return childB;
		}

		public void expandBy(final double d) {
			aabb.expandBy(d);
			if (childB != null) {
				childB.expandBy(d);
			}
			if (childA != null) {
				childA.expandBy(d);
			}
		}
	}
}
