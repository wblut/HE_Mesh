/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_KDTreeInteger.WB_KDEntryInteger;
import wblut.math.WB_Epsilon;

/**
 *
 *
 * @author Frederik Vanhoutte
 *
 */
public abstract class WB_CoordCollection {
	private WB_CoordCollection() {
	}

	public static WB_CoordCollection getCollection(final WB_Coord[] coords) {
		return new WB_CoordCollectionArray(coords);
	}

	public static WB_CoordCollection getCollection(final String path,
			double scale) {
		return new WB_CoordCollectionObj(path, scale);
	}

	public static WB_CoordCollection getCollection(final WB_Polygon polygon) {
		return new WB_CoordCollectionPolygon(polygon);
	}

	public static WB_CoordCollection getCollection(
			final WB_PointGenerator generator, final int n) {
		WB_Coord[] coords = new WB_Point[n];
		for (int i = 0; i < n; i++) {
			coords[i] = generator.nextPoint();
		}
		return new WB_CoordCollectionArray(coords);
	}

	public WB_CoordCollection noise(final WB_VectorGenerator generator) {
		return new WB_CoordCollectionNoise(this, generator);
	}

	public WB_CoordCollection jitter(final double d) {
		return new WB_CoordCollectionJitter(this, d);
	}

	public WB_CoordCollection map(final WB_Map map) {
		return new WB_CoordCollectionMap(this, map);
	}

	public WB_CoordCollection unique() {
		return new WB_CoordCollectionUnique(this);
	}

	public static WB_CoordCollection getCollection(
			final Collection<? extends WB_Coord> coords) {
		return new WB_CoordCollectionList(coords);
	}

	abstract public WB_Coord get(final int i);

	public List<WB_Coord> subList(final int fromInc, final int toExcl) {
		return toList().subList(fromInc, toExcl);
	}

	abstract public int size();

	abstract public WB_Coord[] toArray();

	abstract public List<WB_Coord> toList();

	static class WB_CoordCollectionArray extends WB_CoordCollection {
		WB_Coord[] array;

		WB_CoordCollectionArray(final WB_Coord[] coords) {
			this.array = coords;
		}

		@Override
		public WB_Coord get(final int i) {
			return array[i];
		}

		@Override
		public int size() {
			return array.length;
		}

		@Override
		public WB_Coord[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (WB_Coord c : array) {
				list.add(c);
			}
			return list;
		}
	}

	static class WB_CoordCollectionList extends WB_CoordCollection {
		List<WB_Coord> list;

		WB_CoordCollectionList(final Collection<? extends WB_Coord> coords) {
			this.list = new FastList<WB_Coord>();
			list.addAll(coords);
		}

		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			return list;
		}
	}

	static class WB_CoordCollectionPolygon extends WB_CoordCollection {
		WB_Polygon polygon;

		WB_CoordCollectionPolygon(final WB_Polygon polygon) {
			this.polygon = polygon;
		}

		@Override
		public WB_Coord get(final int i) {
			return polygon.getPoint(i);
		}

		@Override
		public int size() {
			return polygon.getNumberOfShellPoints();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[polygon.getNumberOfShellPoints()];
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				array[i] = polygon.getPoint(i);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				list.add(polygon.getPoint(i));
			}
			return list;
		}
	}

	static class WB_CoordCollectionNoise extends WB_CoordCollection {
		WB_CoordCollection	source;
		WB_Coord[]			noise;

		WB_CoordCollectionNoise(final WB_CoordCollection source,
				final WB_VectorGenerator generator) {
			this.source = source;
			noise = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				noise[i] = generator.nextVector();
			}
		}

		@Override
		public WB_Coord get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Point.add(source.get(i), noise[i]);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Point.add(source.get(i), noise[i]));
			}
			return list;
		}
	}

	static class WB_CoordCollectionMap extends WB_CoordCollection {
		WB_CoordCollection	source;
		WB_Map				map;

		WB_CoordCollectionMap(final WB_CoordCollection source,
				final WB_Map map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Coord get(final int i) {
			return map.mapPoint3D(source.get(i));
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.mapPoint3D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.mapPoint3D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_CoordCollectionUnmap3D extends WB_CoordCollection {
		WB_CoordCollection	source;
		WB_Map				map;

		WB_CoordCollectionUnmap3D(final WB_CoordCollection source,
				final WB_Map map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Coord get(final int i) {
			return map.unmapPoint3D(source.get(i));
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint3D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint3D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_CoordCollectionUnmap2D extends WB_CoordCollection {
		WB_CoordCollection	source;
		WB_Map2D			map;

		WB_CoordCollectionUnmap2D(final WB_CoordCollection source,
				final WB_Map2D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Coord get(final int i) {
			return map.unmapPoint2D(source.get(i));
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint2D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint2D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_CoordCollectionJitter extends WB_CoordCollection {
		WB_CoordCollection	source;
		WB_Coord[]			noise;

		WB_CoordCollectionJitter(final WB_CoordCollection source,
				final double d) {
			this.source = source;
			noise = new WB_Coord[source.size()];
			WB_VectorGenerator generator = new WB_RandomOnSphere();
			for (int i = 0; i < source.size(); i++) {
				noise[i] = generator.nextVector().mulSelf(d);
			}
		}

		@Override
		public WB_Coord get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Point.add(source.get(i), noise[i]);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Point.add(source.get(i), noise[i]));
			}
			return list;
		}
	}

	static class WB_CoordCollectionUnique extends WB_CoordCollection {
		List<WB_Coord> list;

		WB_CoordCollectionUnique(final WB_CoordCollection source) {
			this.list = new FastList<WB_Coord>();
			WB_KDTreeInteger<WB_Coord> tree = new WB_KDTreeInteger<WB_Coord>();
			WB_Coord c;
			WB_KDEntryInteger<WB_Coord>[] neighbors;
			for (int i = 0; i < source.size(); i++) {
				c = source.get(i);
				neighbors = tree.getNearestNeighbors(c, 1);
				if (neighbors[0].d2 > WB_Epsilon.SQEPSILON) {
					tree.add(c, i);
					list.add(c);
				}
			}
		}

		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			return list;
		}
	}

	static class WB_CoordCollectionObj extends WB_CoordCollection {
		List<WB_Coord> list = new FastList<WB_Coord>();

		WB_CoordCollectionObj(String path, double scale) {
			this.list = new FastList<WB_Coord>();
			final File file = new File(path);
			try (InputStream is = createInputStream(file);
					final BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));) {
				if (is == null) {
					return;
				}
				String line;
				while ((line = reader.readLine()) != null) {
					final String[] parts = line.split("\\s+");
					if (parts[0].equals("v")) {
						final double x1 = scale * Double.parseDouble(parts[1]);
						final double y1 = scale * Double.parseDouble(parts[2]);
						final double z1 = parts.length > 3
								? scale * Double.parseDouble(parts[3])
								: 0;
						final WB_Point pointLoc = new WB_Point(x1, y1, z1);
						list.add(pointLoc);
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		private InputStream createInputStream(final File file) {
			if (file == null) {
				throw new IllegalArgumentException("file can't be null");
			}
			try {
				InputStream stream = new FileInputStream(file);
				if (file.getName().toLowerCase().endsWith(".gz")) {
					stream = new GZIPInputStream(stream);
				}
				return stream;
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Coord[] toArray() {
			WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			return list;
		}
	}
}
