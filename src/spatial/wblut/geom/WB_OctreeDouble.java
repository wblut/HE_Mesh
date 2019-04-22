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

public class WB_OctreeDouble {
	protected WB_AABB box;
	protected WB_Coord extent;

	protected WB_Coord min, max;
	protected double minNodeSize = 4.0;

	protected WB_OctreeDouble parent;
	protected WB_OctreeDouble[] nodes;
	protected int numNodes;
	protected List<WB_OctreeDoubleEntry> entries;
	protected double size, hsize;
	protected WB_Coord center;
	private int level = 0;
	private boolean autoPrune = false;

	private WB_OctreeDouble(final WB_OctreeDouble p, final WB_Coord center, final double size) {
		box = new WB_AABB(new WB_Point(center).subSelf(0.5 * size, 0.5 * size, 0.5 * size),
				new WB_Point(center).addSelf(0.5 * size, 0.5 * size, 0.5 * size));
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

	public WB_OctreeDouble(final WB_Coord center, final double size) {
		box = new WB_AABB(new WB_Point(center).subSelf(0.5 * size, 0.5 * size, 0.5 * size),
				new WB_Point(center).addSelf(0.5 * size, 0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	public void addPoint(final WB_Coord p, final double value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new FastList<WB_OctreeDoubleEntry>();
				}
				entries.add(new WB_OctreeDoubleEntry(p, value));

				return;
			} else {
				if (nodes == null) {
					nodes = new WB_OctreeDouble[8];
				}
				int octant = getOctant(p);

				if (nodes[octant] == null) {
					WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((octant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 4) != 0 ? 0.5 * hsize : -0.5 * hsize));

					nodes[octant] = new WB_OctreeDouble(this, newCenter, hsize);
					numNodes++;
				}
				nodes[octant].addPoint(p, value);
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

	public WB_OctreeDouble getNode(final WB_Coord p) {
		if (box.contains(p)) {
			if (numNodes > 0) {
				int octant = getOctant(p);
				if (nodes[octant] != null) {
					return nodes[octant].getNode(p);
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

	protected final int getOctant(final WB_Coord p) {

		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0) + (p.zd() >= center.zd() ? 4 : 0);
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_OctreeDouble getParent() {
		return parent;
	}

	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	public List<WB_OctreeDouble> getNodes() {
		List<WB_OctreeDouble> result = new FastList<WB_OctreeDouble>();
		if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
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
			for (int i = 0; i < 8; i++) {
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
		WB_OctreeDouble node = getNode(p);
		if (node != null) {
			for (WB_OctreeDoubleEntry eo : node.entries) {
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
		this.minNodeSize = minNodeSize * 0.5f;
	}

	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	public WB_AABB getBox() {
		return box;
	}

	public List<WB_OctreeDoubleEntry> getEntries() {
		FastList<WB_OctreeDoubleEntry> result = null;
		if (entries != null) {
			result = new FastList<WB_OctreeDoubleEntry>();

			result.addAll(entries);

		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
				if (nodes[i] != null) {
					List<WB_OctreeDoubleEntry> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new FastList<WB_OctreeDoubleEntry>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_OctreeDoubleEntry> getEntriesInRange(final WB_AABB AABB) {
		FastList<WB_OctreeDoubleEntry> result = new FastList<WB_OctreeDoubleEntry>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new FastList<WB_OctreeDoubleEntry>();
				for (WB_OctreeDoubleEntry oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}

				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						List<WB_OctreeDoubleEntry> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	public List<WB_OctreeDoubleEntry> getEntriesInRange(final WB_Sphere sphere) {
		FastList<WB_OctreeDoubleEntry> result = new FastList<WB_OctreeDoubleEntry>();
		if (box.intersects(sphere)) {
			if (entries != null) {
				for (WB_OctreeDoubleEntry oe : entries) {
					if (sphere.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						List<WB_OctreeDoubleEntry> points = nodes[i].getEntriesInRange(sphere);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	public List<WB_OctreeDoubleEntry> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Sphere(center, radius));
	}

	public List<WB_Coord> getPoints() {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		if (entries != null) {
			for (WB_OctreeDoubleEntry oe : entries) {
				result.add(oe.coord);
			}
		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
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

	public List<WB_Coord> getPointsInRange(final WB_AABB AABB) {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		for (WB_OctreeDoubleEntry eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Sphere sphere) {
		FastList<WB_Coord> result = new FastList<WB_Coord>();
		for (WB_OctreeDoubleEntry eo : getEntriesInRange(sphere)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Sphere(center, radius));
	}

	public double[] getValues() {
		List<WB_OctreeDoubleEntry> allEntries = getEntries();
		double[] result = new double[allEntries.size()];
		int i = 0;
		for (WB_OctreeDoubleEntry eo : allEntries) {
			result[i++] = eo.value;
		}
		return result;

	}

	public double[] getValuesInRange(final WB_AABB AABB) {
		List<WB_OctreeDoubleEntry> entriesInRange = getEntriesInRange(AABB);
		double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (WB_OctreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public double[] getValuesInRange(final WB_Sphere sphere) {
		List<WB_OctreeDoubleEntry> entriesInRange = getEntriesInRange(sphere);
		double[] result = new double[entriesInRange.size()];
		int i = 0;
		for (WB_OctreeDoubleEntry eo : entriesInRange) {
			result[i++] = eo.value;
		}
		return result;
	}

	public double[] getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Sphere(center, radius));
	}

	public static class WB_OctreeDoubleEntry {

		/**
		 *
		 */
		public WB_Coord coord;

		/**
		 *
		 */
		public double value;

		public WB_OctreeDoubleEntry(final WB_Coord coord, final double value) {
			this.coord = coord;
			this.value = value;
		}
	}

}
