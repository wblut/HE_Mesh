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

import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;

public abstract class WB_VectorCollection extends WB_CoordCollection {
	public static WB_VectorCollection getCollection(final WB_Coord[] coords) {
		return new WB_VectorCollectionArray(coords);
	}

	public static WB_VectorCollection getCollection(final String path, final double scale) {
		return new WB_VectorCollectionObj(path, scale);
	}

	public static WB_VectorCollection getCollection(final WB_Polygon polygon) {
		return new WB_VectorCollectionPolygon(polygon);
	}

	public static WB_VectorCollection getCollection(final Collection<? extends WB_Coord> coords) {
		return new WB_VectorCollectionList(coords);
	}

	public static WB_VectorCollection getCollection(final WB_VectorFactory factory, final int numberOfPoints) {
		return new WB_VectorCollectionFactory(factory, numberOfPoints);
	}

	public static WB_VectorCollection getCollection(final double[][] coords) {
		return new WB_VectorCollectionRaw(coords);
	}

	public static WB_VectorCollection getCollection(final float[][] coords) {
		return new WB_VectorCollectionRaw(coords);
	}

	public static WB_VectorCollection getCollection(final int[][] coords) {
		return new WB_VectorCollectionRaw(coords);
	}

	public static WB_VectorCollection getCollection(final HE_Mesh mesh) {
		return getCollection(mesh.getVerticesAsCoord());
	}

	public static WB_VectorCollection getCollection(final WB_SimpleMesh mesh) {
		return getCollection(mesh.vertices);
	}

	@Override
	public WB_VectorCollection noise(final WB_VectorFactory factory) {
		return new WB_VectorCollectionNoise(this, factory);
	}

	@Override
	public WB_VectorCollection jitter(final double d) {
		return new WB_VectorCollectionJitter(this, d);
	}

	public WB_VectorCollection map(final WB_Map3D map) {
		return new WB_VectorCollectionMap(this, map);
	}

	@Override
	public WB_VectorCollection unique() {
		return new WB_VectorCollectionUnique(this);
	}

	@Override
	abstract public WB_Vector get(final int i);

	@Override
	abstract public double getX(final int i);

	@Override
	abstract public double getY(final int i);

	@Override
	abstract public double getZ(final int i);

	@Override
	public List<WB_Coord> subList(final int fromInc, final int toExcl) {
		return toList().subList(fromInc, toExcl);
	}

	@Override
	abstract public int size();

	@Override
	abstract public WB_Vector[] toArray();

	@Override
	abstract public List<WB_Coord> toList();

	static class WB_VectorCollectionArray extends WB_VectorCollection {
		WB_Vector[] array;

		WB_VectorCollectionArray(final WB_Coord[] coords) {
			this.array = new WB_Vector[coords.length];
			for (int i = 0; i < coords.length; i++) {
				array[i] = new WB_Vector(coords[i]);
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return array[i];
		}

		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		@Override
		public int size() {
			return array.length;
		}

		@Override
		public WB_Vector[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Vector c : array) {
				list.add(c);
			}
			return list;
		}
	}

	static class WB_VectorCollectionList extends WB_VectorCollection {
		List<WB_Vector> list;

		WB_VectorCollectionList(final Collection<? extends WB_Coord> coords) {
			this.list = new WB_VectorList();
			for (final WB_Coord c : coords) {
				list.add(new WB_Vector(c));
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return list.get(i);
		}

		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[list.size()];
			int i = 0;
			for (final WB_Vector c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> clist = new WB_CoordList();
			for (final WB_Vector c : list) {
				clist.add(c);
			}
			return clist;
		}
	}

	static class WB_VectorCollectionRaw extends WB_VectorCollection {
		WB_Vector[] array;

		WB_VectorCollectionRaw(final double[][] coords) {
			this.array = new WB_Vector[coords.length];
			int id = 0;
			for (final double[] coord : coords) {
				array[id++] = new WB_Vector(coord[0], coord[1], coord[2]);
			}
		}

		WB_VectorCollectionRaw(final float[][] coords) {
			this.array = new WB_Vector[coords.length];
			int id = 0;
			for (final float[] coord : coords) {
				array[id++] = new WB_Vector(coord[0], coord[1], coord[2]);
			}
		}

		WB_VectorCollectionRaw(final int[][] coords) {
			this.array = new WB_Vector[coords.length];
			int id = 0;
			for (final int[] coord : coords) {
				array[id++] = new WB_Vector(coord[0], coord[1], coord[2]);
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return array[i];
		}

		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		@Override
		public int size() {
			return array.length;
		}

		@Override
		public WB_Vector[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Vector c : array) {
				list.add(c);
			}
			return list;
		}
	}

	static class WB_VectorCollectionFlat extends WB_VectorCollection {
		double[] array;

		WB_VectorCollectionFlat(final double[] coords) {
			this.array = coords;
		}

		@Override
		public WB_Vector get(final int i) {
			return new WB_Vector(array[3 * i], array[3 * i + 1], array[3 * i + 2]);
		}

		@Override
		public double getX(final int i) {
			return array[3 * i];
		}

		@Override
		public double getY(final int i) {
			return array[3 * i + 1];
		}

		@Override
		public double getZ(final int i) {
			return array[3 * i + 2];
		}

		@Override
		public int size() {
			return array.length / 3;
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] out = new WB_Vector[array.length / 3];
			for (int i = 0; i < array.length; i += 3) {
				out[i / 3] = new WB_Vector(array[i], array[i + 1], array[i + 2]);
			}
			return out;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < array.length; i += 3) {
				list.add(new WB_Point(array[i], array[i + 1], array[i + 2]));
			}
			return list;
		}
	}

	static class WB_VectorCollectionFactory extends WB_VectorCollection {
		WB_Vector[] array;

		WB_VectorCollectionFactory(final WB_VectorFactory factory, final int numberOfPoints) {
			this.array = new WB_Vector[numberOfPoints];
			for (int i = 0; i < numberOfPoints; i++) {
				array[i] = factory.nextVector();
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return array[i];
		}

		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		@Override
		public int size() {
			return array.length;
		}

		@Override
		public WB_Vector[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Vector c : array) {
				list.add(c);
			}
			return list;
		}
	}

	static class WB_VectorCollectionPolygon extends WB_VectorCollection {
		WB_Polygon polygon;

		WB_VectorCollectionPolygon(final WB_Polygon polygon) {
			this.polygon = polygon;
		}

		@Override
		public WB_Vector get(final int i) {
			return polygon.getPoint(i);
		}

		@Override
		public double getX(final int i) {
			return polygon.getPoint(i).xd();
		}

		@Override
		public double getY(final int i) {
			return polygon.getPoint(i).yd();
		}

		@Override
		public double getZ(final int i) {
			return polygon.getPoint(i).zd();
		}

		@Override
		public int size() {
			return polygon.getNumberOfShellPoints();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[polygon.getNumberOfShellPoints()];
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				array[i] = polygon.getPoint(i);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				list.add(polygon.getPoint(i));
			}
			return list;
		}
	}

	static class WB_VectorCollectionNoise extends WB_VectorCollection {
		WB_VectorCollection source;
		WB_Vector[] noise;

		WB_VectorCollectionNoise(final WB_VectorCollection source, final WB_VectorFactory factory) {
			this.source = source;
			noise = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				noise[i] = factory.nextVector();
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return WB_Vector.add(source.get(i), noise[i]);
		}

		@Override
		public double getX(final int i) {
			return source.getX(i) + noise[i].xd();
		}

		@Override
		public double getY(final int i) {
			return source.getY(i) + noise[i].yd();
		}

		@Override
		public double getZ(final int i) {
			return source.getZ(i) + noise[i].xd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Vector.add(source.get(i), noise[i]);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Vector.add(source.get(i), noise[i]));
			}
			return list;
		}
	}

	static class WB_VectorCollectionMap extends WB_VectorCollection {
		WB_VectorCollection source;
		WB_Map3D map;

		WB_VectorCollectionMap(final WB_VectorCollection source, final WB_Map3D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Vector get(final int i) {
			return map.mapPoint3D(source.get(i));
		}

		@Override
		public double getX(final int i) {
			return map.mapPoint3D(source.get(i)).xd();
		}

		@Override
		public double getY(final int i) {
			return map.mapPoint3D(source.get(i)).yd();
		}

		@Override
		public double getZ(final int i) {
			return map.mapPoint3D(source.get(i)).zd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.mapPoint3D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.mapPoint3D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_VectorCollectionUnmap3D extends WB_VectorCollection {
		WB_VectorCollection source;
		WB_Map3D map;

		WB_VectorCollectionUnmap3D(final WB_VectorCollection source, final WB_Map3D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Vector get(final int i) {
			return map.unmapPoint3D(source.get(i));
		}

		@Override
		public double getX(final int i) {
			return map.unmapPoint3D(source.get(i)).xd();
		}

		@Override
		public double getY(final int i) {
			return map.unmapPoint3D(source.get(i)).yd();
		}

		@Override
		public double getZ(final int i) {
			return map.unmapPoint3D(source.get(i)).zd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint3D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint3D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_VectorCollectionUnmap2D extends WB_VectorCollection {
		WB_VectorCollection source;
		WB_Map2D map;

		WB_VectorCollectionUnmap2D(final WB_VectorCollection source, final WB_Map2D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Vector get(final int i) {
			return map.unmapPoint2D(source.get(i));
		}

		@Override
		public double getX(final int i) {
			return map.unmapPoint2D(source.get(i)).xd();
		}

		@Override
		public double getY(final int i) {
			return map.unmapPoint2D(source.get(i)).yd();
		}

		@Override
		public double getZ(final int i) {
			return map.unmapPoint2D(source.get(i)).zd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint2D(source.get(i));
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint2D(source.get(i)));
			}
			return list;
		}
	}

	static class WB_VectorCollectionJitter extends WB_VectorCollection {
		WB_VectorCollection source;
		WB_Vector[] noise;

		WB_VectorCollectionJitter(final WB_VectorCollection source, final double d) {
			this.source = source;
			noise = new WB_Vector[source.size()];
			final WB_VectorFactory factory = new WB_RandomOnSphere();
			for (int i = 0; i < source.size(); i++) {
				noise[i] = factory.nextVector().mulSelf(d);
			}
		}

		@Override
		public WB_Vector get(final int i) {
			return WB_Vector.add(source.get(i), noise[i]);
		}

		@Override
		public double getX(final int i) {
			return source.getX(i) + noise[i].xd();
		}

		@Override
		public double getY(final int i) {
			return source.getY(i) + noise[i].yd();
		}

		@Override
		public double getZ(final int i) {
			return source.getZ(i) + noise[i].xd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Vector.add(source.get(i), noise[i]);
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Vector.add(source.get(i), noise[i]));
			}
			return list;
		}
	}

	static class WB_VectorCollectionUnique extends WB_VectorCollection {
		List<WB_Vector> list;

		WB_VectorCollectionUnique(final WB_VectorCollection source) {
			this.list = new WB_VectorList();
			final WB_KDTreeInteger3D<WB_Vector> tree = new WB_KDTreeInteger3D<>();
			WB_Vector c;
			WB_KDEntryInteger<WB_Vector>[] neighbors;
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
		public WB_Vector get(final int i) {
			return list.get(i);
		}

		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[list.size()];
			int i = 0;
			for (final WB_Vector c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> clist = new WB_CoordList();
			for (final WB_Vector c : list) {
				clist.add(c);
			}
			return clist;
		}
	}

	static class WB_VectorCollectionObj extends WB_VectorCollection {
		List<WB_Vector> list = new WB_VectorList();

		WB_VectorCollectionObj(final String path, final double scale) {
			this.list = new WB_VectorList();
			final File file = new File(path);
			try (InputStream is = createInputStream(file);
					final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));) {
				if (is == null) {
					return;
				}
				String line;
				while ((line = reader.readLine()) != null) {
					final String[] parts = line.split("\\s+");
					if (parts[0].equals("v")) {
						final double x1 = scale * Double.parseDouble(parts[1]);
						final double y1 = scale * Double.parseDouble(parts[2]);
						final double z1 = parts.length > 3 ? scale * Double.parseDouble(parts[3]) : 0;
						final WB_Vector pointLoc = new WB_Vector(x1, y1, z1);
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
		public WB_Vector get(final int i) {
			return list.get(i);
		}

		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		@Override
		public int size() {
			return list.size();
		}

		@Override
		public WB_Vector[] toArray() {
			final WB_Vector[] array = new WB_Vector[list.size()];
			int i = 0;
			for (final WB_Vector c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> clist = new WB_CoordList();
			for (final WB_Vector c : list) {
				clist.add(c);
			}
			return clist;
		}
	}
}
