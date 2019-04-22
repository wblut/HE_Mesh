/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.core.WB_ProgressReporter.WB_ProgressTracker;

public class WB_AABBTree2D {

	private WB_AABBNode2D root;

	private final int maxLevel;
	private int depth;

	private final int maxNumberOfFaces;

	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	/**
	 *
	 *
	 * @param mesh
	 * @param mnof
	 */
	public WB_AABBTree2D(final Collection<? extends WB_Triangle> mesh, final int mnof) {
		maxLevel = 2 * (int) Math.ceil(Math.log(mesh.size()) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(mesh);
	}

	public WB_AABBTree2D(final int[] triangles, final WB_CoordCollection points, final int mnof) {
		maxLevel = 2 * (int) Math.ceil(Math.log(triangles.length) / Math.log(2.0));
		maxNumberOfFaces = Math.max(1, mnof);
		depth = 0;
		buildTree(triangles, points);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	private void buildTree(final Collection<? extends WB_Triangle> mesh) {

		tracker.setStartStatus(this,
				"Starting WB_AABBTree2D construction. Max. number of faces per node: " + maxNumberOfFaces);

		root = new WB_AABBNode2D();
		final List<WB_Triangle> faces = new FastList<WB_Triangle>();
		faces.addAll(mesh);
		buildNode(root, faces, mesh, 0);
		tracker.setStopStatus(this, "Exiting WB_AABBTree construction.");
	}

	private void buildTree(final int[] triangles, final WB_CoordCollection points) {

		tracker.setStartStatus(this,
				"Starting WB_AABBTree2D construction. Max. number of faces per node: " + maxNumberOfFaces);

		root = new WB_AABBNode2D();
		final List<WB_Triangle> faces = new FastList<WB_Triangle>();
		List<WB_Triangle> mesh = new FastList<WB_Triangle>();
		for (int i = 0; i < triangles.length; i += 3) {
			mesh.add(new WB_Triangle(points.get(triangles[i]), points.get(triangles[i + 1]),
					points.get(triangles[i + 2])));
		}
		faces.addAll(mesh);
		buildNode(root, faces, mesh, 0);
		tracker.setStopStatus(this, "Exiting WB_AABBTree construction.");
	}

	/**
	 *
	 *
	 * @param node
	 * @param faces
	 * @param mesh
	 * @param level
	 */
	private void buildNode(final WB_AABBNode2D node, final List<WB_Triangle> faces,
			final Collection<? extends WB_Triangle> mesh, final int level) {
		tracker.setDuringStatus(this, "Splitting WB_AABBNode level " + level + " with " + faces.size() + " faces.");
		node.level = level;
		node.aabb = new WB_AABB2D();
		for (WB_Triangle f : faces) {
			node.aabb.expandToInclude(f.getAABB2D());
		}
		if (level == maxLevel || faces.size() <= maxNumberOfFaces) {
			node.faces.addAll(faces);
			node.isLeaf = true;
			depth = Math.max(depth, node.level);
			return;
		}

		List<WB_Triangle> subsetA = new FastList<WB_Triangle>();
		List<WB_Triangle> subsetB = new FastList<WB_Triangle>();

		double sah = Double.POSITIVE_INFINITY;

		for (int i = 0; i < 2; i++) {
			WB_TriangleSort fs = new WB_TriangleSort.WB_AABBSortMax1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}

		for (int i = 0; i < 2; i++) {
			WB_TriangleSort fs = new WB_TriangleSort.WB_AABBSortCenter1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}

		for (int i = 0; i < 2; i++) {
			WB_TriangleSort fs = new WB_TriangleSort.WB_TriangleSortCenter1D(i);
			Collections.sort(faces, fs);
			sah = findOptimalSubset(sah, node, subsetA, subsetB, faces);
		}

		List<WB_Triangle> childA = new FastList<WB_Triangle>();
		List<WB_Triangle> childB = new FastList<WB_Triangle>();

		if (subsetA.size() < subsetB.size()) {
			childA.addAll(subsetB);
			childB.addAll(subsetA);
		} else {
			childA.addAll(subsetA);
			childB.addAll(subsetB);
		}

		node.isLeaf = true;
		if (childB.size() > 0) {
			node.childB = new WB_AABBNode2D();
			buildNode(node.childB, childB, mesh, level + 1);

			node.isLeaf = false;
		}
		if (childA.size() > 0) {
			node.childA = new WB_AABBNode2D();
			buildNode(node.childA, childA, mesh, level + 1);

			node.isLeaf = false;
		}

	}

	private double findOptimalSubset(double bestSah, final WB_AABBNode2D node, final List<WB_Triangle> childA,
			final List<WB_Triangle> childB, final List<WB_Triangle> faces) {
		int items = faces.size();

		double invdenom = 1 / node.getAABB().getArea();
		double[] surfaceAreaA = new double[items - 1];
		double[] surfaceAreaB = new double[items - 1];

		WB_AABB aabbA = new WB_AABB();
		for (int i = 0; i < items - 1; i++) {
			aabbA.expandToInclude(faces.get(i).getAABB());
			surfaceAreaA[i] = aabbA.getArea();
		}
		WB_AABB aabbB = new WB_AABB();
		for (int i = items - 2; i >= 0; i--) {
			aabbB.expandToInclude(faces.get(i + 1).getAABB());
			surfaceAreaB[i] = aabbB.getArea();
		}

		int sizeA = -1;
		for (int i = 0, numA = 1; i < items - 1; i++, numA++) {
			double currentSah = getSAH(invdenom, surfaceAreaA[i], numA, surfaceAreaB[i], items - numA);
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

	/**
	 * Surface area heuristic
	 *
	 * @param denom
	 * @param surfaceAreaA
	 * @param numA
	 * @param surfaceAreaB
	 * @param numB
	 * @return
	 */
	static private double getSAH(final double denom, final double surfaceAreaA, final int numA,
			final double surfaceAreaB, final int numB) {
		return (surfaceAreaA * numA + surfaceAreaB * numB) * denom;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABBNode2D getRoot() {
		return root;
	}

	public int getDepth() {
		return depth;
	}

	public WB_Coord getClosestPoint(final WB_Coord p) {
		PriorityQueue<Entry> entries = new PriorityQueue<Entry>(new EntryOrder());
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

	public WB_Triangle getClosestFace(final WB_Coord p) {
		PriorityQueue<Entry> entries = new PriorityQueue<Entry>(new EntryOrder());
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
		PriorityQueue<Entry> entries = new PriorityQueue<Entry>(new EntryOrder());
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

	private double addNode(final WB_Coord p, final WB_AABBNode2D node, final PriorityQueue<Entry> entries,
			double closest2) {
		double d2 = WB_CoordOp2D.getSqDistance2D(p, node.aabb);
		if (d2 <= closest2) {
			entries.add(new Entry(null, d2, 0, null, node));
			if (node.isLeaf()) {
				for (WB_Triangle f : node.faces) {
					WB_Coord q = WB_GeometryOp2D.getClosestPoint2D(p, f);
					double fd2 = WB_CoordOp2D.getSqDistance2D(p, q);
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

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final Entry arg0, final Entry arg1) {
			return Double.compare(arg0.d2, arg1.d2);
		}

	}

	private class Entry {
		WB_Coord point;
		double d2;
		int type; // 0=aabb, 1=face
		WB_AABBNode2D node;
		WB_Triangle face;

		Entry(final WB_Coord p, final double d2, final int type, final WB_Triangle f, final WB_AABBNode2D node) {
			this.point = p;
			this.d2 = d2;
			this.type = type;
			this.face = f;
			this.node = node;
		}

	}

	public void expandBy(final double d) {
		root.expandBy(d);
	}

	public class WB_AABBNode2D {

		protected int level;

		protected WB_AABB2D aabb = null;

		protected WB_AABBNode2D childA = null;

		protected WB_AABBNode2D childB = null;

		protected List<WB_Triangle> faces;

		protected boolean isLeaf;

		/**
		 *
		 */
		public WB_AABBNode2D() {
			level = -1;
			faces = new FastList<WB_Triangle>();
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_AABB2D getAABB() {
			return aabb;
		}

		/**
		 *
		 *
		 * @return
		 */
		public int getLevel() {
			return level;
		}

		/**
		 *
		 *
		 * @return
		 */
		public boolean isLeaf() {
			return isLeaf;
		}

		/**
		 *
		 *
		 * @return
		 */
		public List<WB_Triangle> getFaces() {
			return faces;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_AABBNode2D getChildA() {
			return childA;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_AABBNode2D getChildB() {
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
