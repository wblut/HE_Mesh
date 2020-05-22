package wblut.geom;

import java.util.Collection;
import java.util.List;

/**
 *
 *
 * @param <V>
 */
public class WB_Octree<V> {
	/**  */
	protected WB_AABB box;
	/**  */
	protected WB_Coord extent;
	/**  */
	protected WB_Coord min, max;
	/**  */
	protected double minNodeSize = 4.0;
	/**  */
	protected WB_Octree<V> parent;
	/**  */
	protected WB_Octree<V>[] nodes;
	/**  */
	protected int numNodes;
	/**  */
	protected List<WB_OctreeEntry<V>> entries;
	/**  */
	protected double size, hsize;
	/**  */
	protected WB_Coord center;
	/**  */
	private int level = 0;
	/**  */
	private boolean autoPrune = false;

	/**
	 *
	 *
	 * @param p
	 * @param center
	 * @param size
	 */
	private WB_Octree(final WB_Octree<V> p, final WB_Coord center, final double size) {
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

	/**
	 *
	 *
	 * @param center
	 * @param size
	 */
	public WB_Octree(final WB_Coord center, final double size) {
		box = new WB_AABB(new WB_Point(center).subSelf(0.5 * size, 0.5 * size, 0.5 * size),
				new WB_Point(center).addSelf(0.5 * size, 0.5 * size, 0.5 * size));
		this.parent = null;
		this.hsize = 0.5 * size;
		this.size = size;
		this.center = new WB_Point(center);
		this.numNodes = 0;
	}

	/**
	 *
	 *
	 * @param p
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void addPoint(final WB_Coord p, final V value) {
		if (box.contains(p)) {
			if (hsize <= minNodeSize) {
				if (entries == null) {
					entries = new WB_List<>();
				}
				entries.add(new WB_OctreeEntry<>(p, value));
				return;
			} else {
				if (nodes == null) {
					nodes = new WB_Octree[8];
				}
				final int octant = getOctant(p);
				if (nodes[octant] == null) {
					final WB_Coord newCenter = WB_Point.add(center,
							new WB_Point((octant & 1) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 2) != 0 ? 0.5 * hsize : -0.5 * hsize,
									(octant & 4) != 0 ? 0.5 * hsize : -0.5 * hsize));
					nodes[octant] = new WB_Octree<>(this, newCenter, hsize);
					numNodes++;
				}
				nodes[octant].addPoint(p, value);
				return;
			}
		}
		return;
	}

	/**
	 *
	 */
	public void clear() {
		numNodes = 0;
		nodes = null;
		entries = null;
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
	 * @param p
	 * @return
	 */
	public WB_Octree<V> getNode(final WB_Coord p) {
		if (box.contains(p)) {
			if (numNodes > 0) {
				final int octant = getOctant(p);
				if (nodes[octant] != null) {
					return nodes[octant].getNode(p);
				}
			} else if (entries != null) {
				return this;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getMinNodeSize() {
		return minNodeSize;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	protected final int getOctant(final WB_Coord p) {
		return (p.xd() >= center.xd() ? 1 : 0) + (p.yd() >= center.yd() ? 2 : 0) + (p.zd() >= center.zd() ? 4 : 0);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getCenter() {
		return center;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Octree<V> getParent() {
		return parent;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getNumberOfPoints() {
		if (entries == null) {
			return 0;
		}
		return entries.size();
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Octree<V>> getNodes() {
		final List<WB_Octree<V>> result = new WB_List<>();
		if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
				if (nodes[i] != null) {
					result.add(nodes[i]);
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getSize() {
		return size;
	}

	/**
	 *
	 */
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

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public boolean remove(final WB_Coord p) {
		boolean found = false;
		final WB_Octree<V> node = getNode(p);
		if (node != null) {
			for (final WB_OctreeEntry<V> eo : node.entries) {
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

	/**
	 *
	 *
	 * @param points
	 */
	public void removeAll(final Collection<WB_Coord> points) {
		for (final WB_Coord p : points) {
			remove(p);
		}
	}

	/**
	 *
	 *
	 * @param minNodeSize
	 */
	public void setMinNodeSize(final double minNodeSize) {
		this.minNodeSize = minNodeSize * 0.5f;
	}

	/**
	 *
	 *
	 * @param state
	 */
	public void setAutoPrune(final boolean state) {
		autoPrune = state;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_AABB getBox() {
		return box;
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_OctreeEntry<V>> getEntries() {
		WB_List<WB_OctreeEntry<V>> result = null;
		if (entries != null) {
			result = new WB_List<>();
			result.addAll(entries);
		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
				if (nodes[i] != null) {
					final List<WB_OctreeEntry<V>> childPoints = nodes[i].getEntries();
					if (childPoints != null) {
						if (result == null) {
							result = new WB_List<>();
						}
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public List<WB_OctreeEntry<V>> getEntriesInRange(final WB_AABB AABB) {
		WB_List<WB_OctreeEntry<V>> result = new WB_List<>();
		if (box.intersects(AABB)) {
			if (entries != null) {
				result = new WB_List<>();
				for (final WB_OctreeEntry<V> oe : entries) {
					if (AABB.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						final List<WB_OctreeEntry<V>> points = nodes[i].getEntriesInRange(AABB);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param sphere
	 * @return
	 */
	public List<WB_OctreeEntry<V>> getEntriesInRange(final WB_Sphere sphere) {
		final WB_List<WB_OctreeEntry<V>> result = new WB_List<>();
		if (box.intersects(sphere)) {
			if (entries != null) {
				for (final WB_OctreeEntry<V> oe : entries) {
					if (sphere.contains(oe.coord)) {
						result.add(oe);
					}
				}
			} else if (numNodes > 0) {
				for (int i = 0; i < 8; i++) {
					if (nodes[i] != null) {
						final List<WB_OctreeEntry<V>> points = nodes[i].getEntriesInRange(sphere);
						if (points != null) {
							result.addAll(points);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public List<WB_OctreeEntry<V>> getEntriesInRange(final WB_Coord center, final double radius) {
		return getEntriesInRange(new WB_Sphere(center, radius));
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<WB_Coord> getPoints() {
		final WB_CoordList result = new WB_CoordList();
		if (entries != null) {
			for (final WB_OctreeEntry<V> oe : entries) {
				result.add(oe.coord);
			}
		} else if (numNodes > 0) {
			for (int i = 0; i < 8; i++) {
				if (nodes[i] != null) {
					final List<WB_Coord> childPoints = nodes[i].getPoints();
					if (childPoints != null) {
						result.addAll(childPoints);
					}
				}
			}
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_AABB AABB) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_OctreeEntry<V> eo : getEntriesInRange(AABB)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param sphere
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_Sphere sphere) {
		final WB_CoordList result = new WB_CoordList();
		for (final WB_OctreeEntry<V> eo : getEntriesInRange(sphere)) {
			result.add(eo.coord);
		}
		return result.asUnmodifiable();
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public List<WB_Coord> getPointsInRange(final WB_Coord center, final double radius) {
		return getPointsInRange(new WB_Sphere(center, radius));
	}

	/**
	 *
	 *
	 * @return
	 */
	public List<V> getValues() {
		final List<WB_OctreeEntry<V>> allEntries = getEntries();
		final WB_List<V> result = new WB_List<>();
		for (final WB_OctreeEntry<V> eo : allEntries) {
			result.add(eo.value);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param AABB
	 * @return
	 */
	public List<V> getValuesInRange(final WB_AABB AABB) {
		final List<WB_OctreeEntry<V>> entriesInRange = getEntriesInRange(AABB);
		final WB_List<V> result = new WB_List<>();
		for (final WB_OctreeEntry<V> eo : entriesInRange) {
			result.add(eo.value);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param sphere
	 * @return
	 */
	public List<V> getValuesInRange(final WB_Sphere sphere) {
		final List<WB_OctreeEntry<V>> entriesInRange = getEntriesInRange(sphere);
		final WB_List<V> result = new WB_List<>();
		for (final WB_OctreeEntry<V> eo : entriesInRange) {
			result.add(eo.value);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param center
	 * @param radius
	 * @return
	 */
	public List<V> getValuesInRange(final WB_Coord center, final double radius) {
		return getValuesInRange(new WB_Sphere(center, radius));
	}

	/**
	 *
	 *
	 * @param <V>
	 */
	public static class WB_OctreeEntry<V> {
		/**  */
		public WB_Coord coord;
		/**  */
		public V value;

		/**
		 *
		 *
		 * @param coord
		 * @param value
		 */
		public WB_OctreeEntry(final WB_Coord coord, final V value) {
			this.coord = coord;
			this.value = value;
		}
	}
}
