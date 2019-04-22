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
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_Quadtree<V> {
	protected WB_AABB2D box;
	protected WB_Coord extent;

	protected WB_Coord min, max;
	protected double minNodeSize = 4.0;

	protected WB_Quadtree<V> parent;
	protected WB_Quadtree<V>[] nodes;
	protected int numNodes;
	protected List<WB_QuadtreeEntry<V>> entries;
	protected double size, hsize;
	protected WB_Coord center;
	private int level = 0;
	private boolean autoPrune = false;

	private WB_Quadtree(final WB_Quadtree<V> p, final WB_Coord center, final double size) {
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

	public WB_Quadtree(final WB_Coord center, final double size) {
		box = new WB_AABB2D(new WB_Point(center.xd(), center.yd()).subSelf(0.5 * size, 0.5 * size),
				new WB_Point(center.xd(), center.yd()).addSelf(0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	@SuppressWarnings("unchecked")
	public void addPoint(final WB_Coord p, final V value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new FastList<WB_QuadtreeEntry<V>>();
				}
				entries.add(new WB_QuadtreeEntry<V>(p, value));

				return;
			} else {
				if (nodes == null) {
					nodes = new WB_Quadtree[4];
				}
				int quadrant = getQuadrant(p);

				if (nodes[quadrant] == null) {
					WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((quadrant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(quadrant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize));

					nodes[quadrant] = new WB_Quadtree<V>(this, newCenter, hsize);
					numNodes++;
				}
				nodes[quadrant].addPoint(p, value);
				return;
			}
		}
		return;
	}

	public void clear() {
		numNodes = 0;
		nodes = null;
		entries = null;
	}

	public int getLevel() {
		return level;
	}

	public WB_Quadtree<V> getNode(final WB_Coord p) {
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

	public WB_Quadtree<V> getParent() {
		return parent;
	}

	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	public List<WB_Quadtree<V>> getNodes() {
		List<WB_Quadtree<V>> result = new FastList<WB_Quadtree<V>>();
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
		WB_Quadtree<V> node = getNode(p);
		if (node != null) {
			for (WB_QuadtreeEntry<V> eo : node.entries) {
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

	public List<WB_QuadtreeEntry<V>> getEntries() {
		FastList<WB_QuadtreeEntry<V>> result = null;
		if (entries != null) {
			result = new FastList<WB_QuadtreeEntry<V>>();

			result.addAll(entries);

		} else if (numNodes > 0) {
			for (int i = 0; i < 4; i++) {
				if (nodes[i] != null) {
					List<WB_QuadtreeEntry<V>> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new FastList<WB_QuadtreeEntry<V>>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_QuadtreeEntry<V>> getEntriesInRange(final WB_AABB2D AABB) {
		FastList<WB_QuadtreeEntry<V>> result = new FastList<WB_QuadtreeEntry<V>>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new FastList<WB_QuadtreeEntry<V>>();
				for (WB_QuadtreeEntry<V> oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}

				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						List<WB_QuadtreeEntry<V>> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_QuadtreeEntry<V>> getEntriesInRange(final WB_Circle circle) {
		FastList<WB_QuadtreeEntry<V>> result = new FastList<WB_QuadtreeEntry<V>>();
		if (box.intersects(circle)) {
			if (entries != null) {
				for (WB_QuadtreeEntry<V> oe : entries) {
					if (circle.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 4; i++) {
					if (nodes[i] != null) {
						List<WB_QuadtreeEntry<V>> points = nodes[i].getEntriesInRange(circle);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	public List<WB_QuadtreeEntry<V>> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Circle(center, radius));
	}

	public List<WB_Coord> getPoints() {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		if (entries != null) {
			for (WB_QuadtreeEntry<V> oe : entries) {
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
		for (WB_QuadtreeEntry<V> eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Circle circle) {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		for (WB_QuadtreeEntry<V> eo : getEntriesInRange(circle)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Circle(center, radius));
	}

	public List<V> getValues() {
		List<WB_QuadtreeEntry<V>> allEntries = getEntries();
		FastList<V> result = new FastList<V>();
		for (WB_QuadtreeEntry<V> eo : allEntries) {
			result.add(eo.value);
		}
		return result;

	}

	public List<V> getValuesInRange(final WB_AABB2D AABB) {
		List<WB_QuadtreeEntry<V>> entriesInRange = getEntriesInRange(AABB);
		FastList<V> result = new FastList<V>();
		for (WB_QuadtreeEntry<V> eo : entriesInRange) {
			result.add(eo.value);
		}
		return result;
	}

	public List<V> getValuesInRange(final WB_Circle circle) {
		List<WB_QuadtreeEntry<V>> entriesInRange = getEntriesInRange(circle);
		FastList<V> result = new FastList<V>();
		for (WB_QuadtreeEntry<V> eo : entriesInRange) {
			result.add(eo.value);
		}
		return result;
	}

	public List<V> getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Circle(center, radius));
	}

	public static class WB_QuadtreeEntry<V> {

		/**
		 *
		 */
		public WB_Coord coord;

		/**
		 *
		 */
		public V value;

		public WB_QuadtreeEntry(final WB_Coord coord, final V value) {
			this.coord = coord;
			this.value = value;
		}
	}

}
