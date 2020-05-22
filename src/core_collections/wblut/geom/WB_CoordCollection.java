package wblut.geom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;

import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;

/**
 *
 */
public abstract class WB_CoordCollection {
	public static WB_CoordCollection getCollection(final WB_CoordCollection coords) {
		return WB_CoordCollection.getCollection(coords.toList());
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final WB_Coord... coords) {
		return new WB_CoordCollectionArray(coords);
	}

	/**
	 *
	 *
	 * @param path
	 * @param scale
	 * @return
	 */
	public static WB_CoordCollection getCollection(final String path, final double scale) {
		return new WB_CoordCollectionObj(path, scale);
	}

	/**
	 *
	 *
	 * @param polygon
	 * @return
	 */
	public static WB_CoordCollection getCollection(final WB_Polygon polygon) {
		return new WB_CoordCollectionPolygon(polygon);
	}
	
	public static WB_CoordCollection getCollection(final HE_Face face) {
		return new WB_CoordCollectionHEFace(face);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final Collection<? extends WB_Coord> coords) {
		return new WB_CoordCollectionList(coords);
	}

	/**
	 *
	 *
	 * @param generator
	 * @param numberOfPoints
	 * @return
	 */
	public static WB_CoordCollection getCollection(final WB_PointFactory generator, final int numberOfPoints) {
		return new WB_CoordCollectionFactory(generator, numberOfPoints);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final double[][] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final float[][] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final int[][] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final double[] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final float[] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public static WB_CoordCollection getCollection(final int[] coords) {
		return new WB_CoordCollectionRaw(coords);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static WB_CoordCollection getCollection(final HE_Mesh mesh) {
		return getCollection(mesh.getVerticesAsCoord());
	}

	/**
	 *
	 *
	 * @param generator
	 * @return
	 */
	public WB_CoordCollection noise(final WB_VectorFactory generator) {
		return new WB_CoordCollectionNoise(this, generator);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public WB_CoordCollection jitter(final double d) {
		return new WB_CoordCollectionJitter(this, d);
	}

	/**
	 *
	 *
	 * @param map
	 * @return
	 */
	public WB_CoordCollection map(final WB_Map map) {
		return new WB_CoordCollectionMap(this, map);
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_CoordCollection unique() {
		return new WB_CoordCollectionUnique(this);
	}

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	abstract public WB_Coord get(final int i);

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	abstract public double getX(final int i);

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	abstract public double getY(final int i);

	/**
	 *
	 *
	 * @param i
	 * @return
	 */
	abstract public double getZ(final int i);

	/**
	 *
	 *
	 * @param fromInc
	 * @param toExcl
	 * @return
	 */
	public List<WB_Coord> subList(final int fromInc, final int toExcl) {
		return toList().subList(fromInc, toExcl);
	}

	abstract public void add(WB_Coord... coord);

	abstract public void add(Collection<? extends WB_Coord> coord);

	abstract public void add(WB_CoordCollection coords);

	/**
	 *
	 *
	 * @return
	 */
	abstract public int size();

	/**
	 *
	 *
	 * @return
	 */
	abstract public WB_Coord[] toArray();

	/**
	 *
	 *
	 * @return
	 */
	abstract public List<WB_Coord> toList();

	/**
	 *
	 */
	static class WB_CoordCollectionArray extends WB_CoordCollection {
		/**  */
		WB_Coord[] array;

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionArray(final WB_Coord[] coords) {
			this.array = coords;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return array[i];
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return array.length;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Coord c : array) {
				list.add(c);
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			array = Arrays.copyOf(array, array.length + coord.length);
			for (int i = 0; i < coord.length; i++) {
				array[array.length - coord.length + i] = coord[i];
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			int i = 0;
			for (final WB_Coord coord : coords) {
				array[array.length - coords.size() + (i++)] = coord;
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			for (int i = 0; i < coords.size(); i++) {
				array[array.length - coords.size() + i] = coords.get(i);
			}
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionList extends WB_CoordCollection {
		/**  */
		List<WB_Coord> list;

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionList(final Collection<? extends WB_Coord> coords) {
			this.list = new WB_CoordList();
			list.addAll(coords);
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return list.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (final WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			for (final WB_Coord element : coord) {
				list.add(element);
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			for (final WB_Coord coord : coords) {
				list.add(coord);
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			for (int i = 0; i < coords.size(); i++) {
				list.add(coords.get(i));
			}
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionRaw extends WB_CoordCollection {
		/**  */
		WB_Coord[] array;

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final double[][] coords) {
			this.array = new WB_Point[coords.length];
			int id = 0;
			for (final double[] coord : coords) {
				array[id++] = new WB_Point(coord[0], coord[1], coord[2]);
			}
		}

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final float[][] coords) {
			this.array = new WB_Point[coords.length];
			int id = 0;
			for (final float[] coord : coords) {
				array[id++] = new WB_Point(coord[0], coord[1], coord[2]);
			}
		}

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final int[][] coords) {
			this.array = new WB_Point[coords.length];
			int id = 0;
			for (final int[] coord : coords) {
				array[id++] = new WB_Point(coord[0], coord[1], coord[2]);
			}
		}

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final double[] coords) {
			this.array = new WB_Point[coords.length / 3];
			for (int id = 0; id < coords.length; id += 3) {
				array[id / 3] = new WB_Point(coords[id], coords[id + 1], coords[id + 2]);
			}
		}

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final float[] coords) {
			this.array = new WB_Point[coords.length / 3];
			for (int id = 0; id < coords.length; id += 3) {
				array[id / 3] = new WB_Point(coords[id], coords[id + 1], coords[id + 2]);
			}
		}

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionRaw(final int[] coords) {
			this.array = new WB_Point[coords.length / 3];
			for (int id = 0; id < coords.length; id += 3) {
				array[id / 3] = new WB_Point(coords[id], coords[id + 1], coords[id + 2]);
			}
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return array[i];
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return array.length;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Coord c : array) {
				list.add(c);
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			array = Arrays.copyOf(array, array.length + coord.length);
			for (int i = 0; i < coord.length; i++) {
				array[array.length - coord.length + i] = coord[i];
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			int i = 0;
			for (final WB_Coord coord : coords) {
				array[array.length - coords.size() + (i++)] = coord;
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			for (int i = 0; i < coords.size(); i++) {
				array[array.length - coords.size() + i] = coords.get(i);
			}
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionFlat extends WB_CoordCollection {
		/**  */
		double[] array;

		/**
		 *
		 *
		 * @param coords
		 */
		WB_CoordCollectionFlat(final double[] coords) {
			this.array = coords;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return new WB_Point(array[3 * i], array[3 * i + 1], array[3 * i + 2]);
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return array[3 * i];
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return array[3 * i + 1];
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return array[3 * i + 2];
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return array.length / 3;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] out = new WB_Coord[array.length / 3];
			for (int i = 0; i < array.length; i += 3) {
				out[i / 3] = new WB_Point(array[i], array[i + 1], array[i + 2]);
			}
			return out;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < array.length; i += 3) {
				list.add(new WB_Point(array[i], array[i + 1], array[i + 2]));
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			array = Arrays.copyOf(array, array.length + 3 * coord.length);
			for (int i = 0; i < 3 * coord.length; i += 3) {
				array[array.length - 3 * coord.length + i] = coord[i].xd();
				array[array.length - 3 * coord.length + i + 1] = coord[i].yd();
				array[array.length - 3 * coord.length + i + 2] = coord[i].zd();
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			array = Arrays.copyOf(array, array.length + 3 * coords.size());
			int i = 0;
			for (final WB_Coord coord : coords) {
				array[array.length - 3 * coords.size() + (i++)] = coord.xd();
				array[array.length - 3 * coords.size() + (i++)] = coord.yd();
				array[array.length - 3 * coords.size() + (i++)] = coord.zd();
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			array = Arrays.copyOf(array, array.length + 3 * coords.size());
			for (int i = 0; i < 3 * coords.size(); i += 3) {
				array[array.length - 3 * coords.size() + i] = coords.getX(i);
				array[array.length - 3 * coords.size() + i + 1] = coords.getY(i);
				array[array.length - 3 * coords.size() + i + 2] = coords.getZ(i);
			}
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionFactory extends WB_CoordCollection {
		/**  */
		WB_Coord[] array;

		/**
		 *
		 *
		 * @param generator
		 * @param numberOfPoints
		 */
		WB_CoordCollectionFactory(final WB_PointFactory generator, final int numberOfPoints) {
			this.array = new WB_Point[numberOfPoints];
			for (int i = 0; i < numberOfPoints; i++) {
				array[i] = generator.nextPoint();
			}
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return array[i];
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return array[i].xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return array[i].yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return array[i].zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return array.length;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (final WB_Coord c : array) {
				list.add(c);
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			array = Arrays.copyOf(array, array.length + coord.length);
			for (int i = 0; i < coord.length; i++) {
				array[array.length - coord.length + i] = coord[i];
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			int i = 0;
			for (final WB_Coord coord : coords) {
				array[array.length - coords.size() + (i++)] = coord;
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			array = Arrays.copyOf(array, array.length + coords.size());
			for (int i = 0; i < coords.size(); i++) {
				array[array.length - coords.size() + i] = coords.get(i);
			}
		}
	}


	
	/**
	 *
	 */
	static class WB_CoordCollectionPolygon extends WB_CoordCollection {
		/**  */
		WB_Polygon polygon;

		/**
		 *
		 *
		 * @param polygon
		 */
		WB_CoordCollectionPolygon(final WB_Polygon polygon) {
			this.polygon = polygon;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return polygon.getPoint(i);
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return polygon.getPoint(i).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return polygon.getPoint(i).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return polygon.getPoint(i).zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return polygon.getNumberOfShellPoints();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[polygon.getNumberOfShellPoints()];
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				array[i] = polygon.getPoint(i);
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < polygon.getNumberOfShellPoints(); i++) {
				list.add(polygon.getPoint(i));
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}
	}


	/**
	 *
	 */
	static class WB_CoordCollectionHEFace extends WB_CoordCollection {
		/**  */
		HE_Face face;
int size;
		/**
		 *
		 *
		 * @param polygon
		 */
		WB_CoordCollectionHEFace(final HE_Face face) {
			this.face = face;
			size=face.getFaceDegree();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			HE_Halfedge he=face.getHalfedge();
			int index=0;
			while(index<i) {
				he=he.getNextInFace();
				index++;
				
			}
			return he.getVertex();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return get(i).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return get(i).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return get(i).zd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return size;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[size];
			for (int i = 0; i < size; i++) {
				array[i] = get(i);
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < size; i++) {
				list.add(get(i));
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			throw new java.lang.UnsupportedOperationException("Not supported yet.");
		}
	}
	
	
	/**
	 *
	 */
	static class WB_CoordCollectionNoise extends WB_CoordCollection {
		/**  */
		WB_CoordCollection source;
		/**  */
		WB_Coord[] noise;

		/**
		 *
		 *
		 * @param source
		 * @param generator
		 */
		WB_CoordCollectionNoise(final WB_CoordCollection source, final WB_VectorFactory generator) {
			this.source = source;
			noise = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				noise[i] = generator.nextVector();
			}
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return source.getX(i) + noise[i].xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return source.getY(i) + noise[i].yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return source.getZ(i) + noise[i].xd();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return source.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Point.add(source.get(i), noise[i]);
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Point.add(source.get(i), noise[i]));
			}
			return list;
		}

		@Override
		public void add(final WB_Coord... coord) {
			source.add(coord);
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			source.add(coord);
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			source.add(coords);
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionMap extends WB_CoordCollection {
		/**  */
		WB_CoordCollection source;
		/**  */
		WB_Map map;

		/**
		 *
		 *
		 * @param source
		 * @param map
		 */
		WB_CoordCollectionMap(final WB_CoordCollection source, final WB_Map map) {
			this.source = source;
			this.map = map;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return map.mapPoint3D(source.get(i));
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return source.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.mapPoint3D(source.get(i));
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.mapPoint3D(source.get(i)));
			}
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return map.mapPoint3D(source.get(i)).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return map.mapPoint3D(source.get(i)).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return map.mapPoint3D(source.get(i)).zd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			source.add(coord);
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			source.add(coord);
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			source.add(coords);
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionUnmap3D extends WB_CoordCollection {
		/**  */
		WB_CoordCollection source;
		/**  */
		WB_Map map;

		/**
		 *
		 *
		 * @param source
		 * @param map
		 */
		WB_CoordCollectionUnmap3D(final WB_CoordCollection source, final WB_Map map) {
			this.source = source;
			this.map = map;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return map.unmapPoint3D(source.get(i));
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return source.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint3D(source.get(i));
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint3D(source.get(i)));
			}
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return map.unmapPoint3D(source.get(i)).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return map.unmapPoint3D(source.get(i)).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return map.unmapPoint3D(source.get(i)).zd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			source.add(coord);
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			source.add(coord);
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			source.add(coords);
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionUnmap2D extends WB_CoordCollection {
		/**  */
		WB_CoordCollection source;
		/**  */
		WB_Map2D map;

		/**
		 *
		 *
		 * @param source
		 * @param map
		 */
		WB_CoordCollectionUnmap2D(final WB_CoordCollection source, final WB_Map2D map) {
			this.source = source;
			this.map = map;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return map.unmapPoint2D(source.get(i));
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return source.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = map.unmapPoint2D(source.get(i));
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(map.unmapPoint2D(source.get(i)));
			}
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return map.unmapPoint2D(source.get(i)).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return map.unmapPoint2D(source.get(i)).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return map.unmapPoint2D(source.get(i)).zd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			source.add(coord);
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			source.add(coord);
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			source.add(coords);
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionJitter extends WB_CoordCollection {
		/**  */
		WB_CoordCollection source;
		/**  */
		WB_Coord[] noise;

		/**
		 *
		 *
		 * @param source
		 * @param d
		 */
		WB_CoordCollectionJitter(final WB_CoordCollection source, final double d) {
			this.source = source;
			noise = new WB_Coord[source.size()];
			final WB_VectorFactory generator = new WB_RandomOnSphere();
			for (int i = 0; i < source.size(); i++) {
				noise[i] = generator.nextVector().mulSelf(d);
			}
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return source.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[source.size()];
			for (int i = 0; i < source.size(); i++) {
				array[i] = WB_Point.add(source.get(i), noise[i]);
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			final List<WB_Coord> list = new WB_CoordList();
			for (int i = 0; i < source.size(); i++) {
				list.add(WB_Point.add(source.get(i), noise[i]));
			}
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return source.getX(i) + noise[i].xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return source.getY(i) + noise[i].yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return source.getZ(i) + noise[i].xd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			source.add(coord);
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coord) {
			source.add(coord);
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			source.add(coords);
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionUnique extends WB_CoordCollection {
		/**  */
		List<WB_Coord> list;

		/**
		 *
		 *
		 * @param source
		 */
		WB_CoordCollectionUnique(final WB_CoordCollection source) {
			this.list = new WB_CoordList();
			final WB_KDTreeInteger3D<WB_Coord> tree = new WB_KDTreeInteger3D<>();
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

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return list.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (final WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			for (final WB_Coord element : coord) {
				list.add(element);
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			for (final WB_Coord coord : coords) {
				list.add(coord);
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			for (int i = 0; i < coords.size(); i++) {
				list.add(coords.get(i));
			}
		}
	}

	/**
	 *
	 */
	static class WB_CoordCollectionObj extends WB_CoordCollection {
		/**  */
		List<WB_Coord> list = new WB_CoordList();

		/**
		 *
		 *
		 * @param path
		 * @param scale
		 */
		WB_CoordCollectionObj(final String path, final double scale) {
			this.list = new WB_CoordList();
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
						final WB_Point pointLoc = new WB_Point(x1, y1, z1);
						list.add(pointLoc);
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 *
		 *
		 * @param file
		 * @return
		 */
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

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public WB_Coord get(final int i) {
			return list.get(i);
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public int size() {
			return list.size();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public WB_Coord[] toArray() {
			final WB_Coord[] array = new WB_Coord[list.size()];
			int i = 0;
			for (final WB_Coord c : list) {
				array[i++] = c;
			}
			return array;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<WB_Coord> toList() {
			return list;
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getX(final int i) {
			return list.get(i).xd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getY(final int i) {
			return list.get(i).yd();
		}

		/**
		 *
		 *
		 * @param i
		 * @return
		 */
		@Override
		public double getZ(final int i) {
			return list.get(i).zd();
		}

		@Override
		public void add(final WB_Coord... coord) {
			for (final WB_Coord element : coord) {
				list.add(element);
			}
		}

		@Override
		public void add(final Collection<? extends WB_Coord> coords) {
			for (final WB_Coord coord : coords) {
				list.add(coord);
			}
		}

		@Override
		public void add(final WB_CoordCollection coords) {
			for (int i = 0; i < coords.size(); i++) {
				list.add(coords.get(i));
			}
		}
	}
}
