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
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_QuadtreeInteger {
	protected WB_AABB2D box;
	protected WB_Coord extent;

	protected WB_Coord min, max;
	protected double minNodeSize = 4.0;

	protected WB_QuadtreeInteger parent;
	protected WB_QuadtreeInteger[] nodes;
	protected int numNodes;
	protected List<WB_QuadtreeIntegerEntry> entries;
	protected double size, hsize;
	protected WB_Coord center;
	private int level = 0;
	private boolean autoPrune = false;

	private WB_QuadtreeInteger(final WB_QuadtreeInteger p, final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = p;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
		if (parent != null) {
			level = parent.level + 1;
			minNodeSize = parent.minNodeSize;
		}
	}

	public WB_QuadtreeInteger(final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	public void addPoint(final WB_Coord p, final int value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new FastList<WB_QuadtreeIntegerEntry>();
				}
				entries.add(new WB_QuadtreeIntegerEntry(p, value));

				return;
			} else {
				if (nodes == null) {
					nodes = new WB_QuadtreeInteger[4];
				}
				int quadrant = getQuadrant(p);

				if (nodes[quadrant] == null) {
					WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((quadrant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(quadrant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize));

					nodes[quadrant] = new WB_QuadtreeInteger(this, newCenter, hsize);
					numNodes++;
				}
				nodes[quadrant].addPoint(p, value);
				return;
			}
		}
		return;
	}

	public void addAll(final Collection<? extends WB_Coord> points) {
		int i = 0;
		for (WB_Coord point : points) {
			addPoint(point, i++);
		}
	}

	public void clear() {
		numNodes = 0;
		nodes = null;
		entries = null;
	}

	public int getLevel() {
		return level;
	}

	public WB_QuadtreeInteger getNode(final WB_Coord p) {
		if (box.contains(p)) {
			if (numNodes > 0) {
				int quadrant = getQuadrant(p);
				if (nodes[quadrant] != null) {
					return nodes[quadrant].getNode(p);
				}
			} else if (entries != null) {
				return this;
			}
		}
		return null;
	}

	public double getMinNodeSize() {
		return minNodeSize;
	}

	public int getNumNodes() {
		return numNodes;
	}

	protected final int getQuadrant(final WB_Coord p) {

		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0);
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_QuadtreeInteger getParent() {
		return parent;
	}

	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	public List<WB_QuadtreeInteger> getNodes() {
		List<WB_QuadtreeInteger> result = new FastList<WB_QuadtreeInteger>();
		if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					result.add(nodes[i]);
				}
			}
		}
		return result;

	}

	public double getSize() {
		return size;
	}

	private void prune() {
		if (entries != null && entries.size() == 0) {
			entries = null;
		}
		if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null && nodes[i].entries == null) {
					nodes[i] = null;
				}
			}
		}
		if (parent != null) {
			parent.prune();
		}
	}

	public boolean remove(final WB_Coord p) {
		boolean found = false;
		WB_QuadtreeInteger node = getNode(p);
		if (node != null) {
			for (WB_QuadtreeIntegerEntry eo : node.entries) {
				if (eo.coord.equals(p)) {
					if (node.entries.remove(eo)) {
						found = true;
						if (autoPrune && node.entries.size() == 0) {
							node.prune();
						}
					}
				}
			}
		}
		return found;
	}

	public void removeAll(final Collection<WB_Coord> points) {
		for (WB_Coord p : points) {
			remove(p);
		}
	}

	public void setMinNodeSize(final double minNodeSize) {
		this.minNodeSize = minNodeSize * 0.5;
	}

	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	public WB_AABB2D getBox() {
		return box;
	}

	public List<WB_QuadtreeIntegerEntry> getEntries() {
		FastList<WB_QuadtreeIntegerEntry> result = null;
		if (entries != null) {
			result = new FastList<WB_QuadtreeIntegerEntry>();

			result.addAll(entries);

		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					List<WB_QuadtreeIntegerEntry> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new FastList<WB_QuadtreeIntegerEntry>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_QuadtreeIntegerEntry> getEntriesInRange(final WB_AABB2D AABB) {
		FastList<WB_QuadtreeIntegerEntry> result = new FastList<WB_QuadtreeIntegerEntry>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new FastList<WB_QuadtreeIntegerEntry>();
				for (WB_QuadtreeIntegerEntry oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}

				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						List<WB_QuadtreeIntegerEntry> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_QuadtreeIntegerEntry> getEntriesInRange(final WB_Circle circle) {
		FastList<WB_QuadtreeIntegerEntry> result = new FastList<WB_QuadtreeIntegerEntry>();
		if (box.intersects(circle)) {
			if (entries != null) {
				for (WB_QuadtreeIntegerEntry oe : entries) {
					if (circle.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						List<WB_QuadtreeIntegerEntry> points = nodes[i].getEntriesInRange(circle);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	public List<WB_QuadtreeIntegerEntry> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Circle(center, radius));
	}

	public List<WB_Coord> getPoints() {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		if (entries != null) {
			for (WB_QuadtreeIntegerEntry oe : entries) {
				result.add(oe.coord);
			}
		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					List<WB_Coord> childPoints = nodes[i].getPoints();
					if (childPoints != null) {
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_AABB2D AABB) {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		for (WB_QuadtreeIntegerEntry eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Circle circle) {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		for (WB_QuadtreeIntegerEntry eo : getEntriesInRange(circle)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Circle(center, radius));
	}

	public int[] getValues() {
		List<WB_QuadtreeIntegerEntry> allEntries = getEntries();
		int[] result = new int[allEntries.size()];
		int i = 0;
		for (WB_QuadtreeIntegerEntry eo : allEntries) {
			result[i++] = eo.value;
		}
		return result;

	}

	public int[] getValuesInRange(final WB_AABB2D AABB) {
		List<WB_QuadtreeIntegerEntry> entriesInRange = getEntriesInRange(AABB);
		int[] result = new int[entriesInRange.size()];
		int i = 0;
		for (WB_QuadtreeIntegerEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public int[] getValuesInRange(final WB_Circle circle) {
		List<WB_QuadtreeIntegerEntry> entriesInRange = getEntriesInRange(circle);
		int[] result = new int[entriesInRange.size()];
		int i = 0;
		for (WB_QuadtreeIntegerEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public int[] getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Circle(center, radius));
	}

	public static class WB_QuadtreeIntegerEntry {

		/**
		 *
		 */
		public WB_Coord coord;

		/**
		 *
		 */
		public int value;

		public WB_QuadtreeIntegerEntry(final WB_Coord coord, final int value) {
			this.coord = coord;
			this.value = value;
		}
	}

	public static void main(final String[] args) {
		WB_QuadtreeInteger tree;
		WB_AABB2D AABB = new WB_AABB2D(0, 0, 100, 100);
		ArrayList<WB_Point> points = new ArrayList<WB_Point>();
		WB_RandomGenerator rp = new WB_RandomRectangle().setSize(800, 800).setOffset(400, 400);
		for (int i = 0; i < 1000; i++) {
			points.add(rp.nextPoint());
		}
		tree = new WB_QuadtreeInteger(new WB_Point(400, 400), 800);
		tree.addAll(points);

		List<WB_Coord> inRange = tree.getPointsInRange(AABB);
		System.out.println(inRange.size());
	}

}
