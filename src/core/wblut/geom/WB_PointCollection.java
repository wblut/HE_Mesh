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

import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Epsilon;

/**
 *
 *
 * @author Frederik Vanhoutte
 *
 */
public abstract class WB_PointCollection extends WB_CoordCollection {
	
	public static WB_PointCollection getCollection(final WB_Coord[] coords) {
		return new WB_PointCollectionArray(coords);
	}

	public static WB_PointCollection getCollection(final String path,
			double scale) {
		return new WB_PointCollectionObj(path, scale);
	}

	public static WB_PointCollection getCollection(final WB_Polygon polygon) {
		return new WB_PointCollectionPolygon(polygon);
	}

	public static WB_PointCollection getCollection(
			final Collection<? extends WB_Coord> coords) {
		return new WB_PointCollectionList(coords);
	}
	
	public static WB_PointCollection getCollection(WB_PointGenerator factory, int numberOfPoints) { 
	return new WB_PointCollectionFactory(factory, numberOfPoints);
	}
	
	public static WB_PointCollection getCollection(WB_Coord point,WB_PointGenerator factory, int numberOfPoints) { 
		return new WB_PointCollectionFactory(point,factory, numberOfPoints);
		}
	
	public static WB_PointCollection getCollection(double[][] coords) {
		return new WB_PointCollectionRaw(coords);
	}
	
	public static WB_PointCollection getCollection(float[][] coords) {
		return new WB_PointCollectionRaw(coords);
	}
	
	public static WB_PointCollection getCollection(int[][] coords) {
		return new WB_PointCollectionRaw(coords);
	}
	
	public static WB_PointCollection getCollection(HE_Mesh mesh) {
		return getCollection(mesh.getVerticesAsCoord());
	}
	
	public static WB_PointCollection getCollection(WB_SimpleMesh mesh) {
		return getCollection(mesh.vertices);
	}


	public WB_PointCollection noise(final WB_VectorGenerator factory) {
		return new WB_PointCollectionNoise(this, factory);
	}

	public WB_PointCollection jitter(final double d) {
		return new WB_PointCollectionJitter(this, d);
	}

	public WB_PointCollection map(final WB_Map3D map) {
		return new WB_PointCollectionMap(this, map);
	}

	public WB_PointCollection unique() {
		return new WB_PointCollectionUnique(this);
	}

	abstract public WB_Point get(final int i);

	public List<WB_Coord> subList(final int fromInc, final int toExcl) {
		return toList().subList(fromInc, toExcl);
	}

	abstract public int size();

	abstract public WB_Point[] toArray();

	abstract public List<WB_Coord> toList();

	static class WB_PointCollectionArray extends WB_PointCollection {
		WB_Point[] array;

		WB_PointCollectionArray(final WB_Coord[] coords) {
			this.array = new WB_Point[coords.length];
			for(int i=0;i<coords.length;i++) {
				array[i]=new WB_Point(coords[i]);
			}
		}

		@Override
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (WB_Point c : array) {
				list.add(c);
			}
			return list;
		}
	}

	static class WB_PointCollectionList extends WB_PointCollection {
		List<WB_Point> list;

		WB_PointCollectionList(final Collection<? extends WB_Coord> coords) {
			this.list = new FastList<WB_Point>();
			for (WB_Coord c : coords) {
				list.add(new WB_Point(c));
			}
		}

		@Override
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[list.size()];
			int i = 0;
			for (WB_Point c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> clist = new FastList<WB_Coord>();
			for (WB_Point c : list) {
				clist.add(c);
			}
			return clist;
		}
	}
	
	static class WB_PointCollectionRaw extends WB_PointCollection {
		WB_Point[] array;

		WB_PointCollectionRaw(final double[][] coords) {
			this.array = new WB_Point[coords.length];
			int id=0;
			for(double[] coord:coords) {
				array[id++]=new WB_Point(coord[0],coord[1],coord[2]);
			}
		}
		WB_PointCollectionRaw(final float[][] coords) {
			this.array = new WB_Point[coords.length];
			int id=0;
			for(float[] coord:coords) {
				array[id++]=new WB_Point(coord[0],coord[1],coord[2]);
			}
		}
		
		WB_PointCollectionRaw(final int[][] coords) {
			this.array = new WB_Point[coords.length];
			int id=0;
			for(int[] coord:coords) {
				array[id++]=new WB_Point(coord[0],coord[1],coord[2]);
			}
		}

		@Override
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (WB_Point c : array) {
				list.add(c);
			}
			return list;
		}
	}
	
	static class WB_PointCollectionFlat extends WB_PointCollection {
		double[] array;

		WB_PointCollectionFlat(final double[] coords) {
			this.array = coords;
			
		}
		

		@Override
		public WB_Point get(final int i) {
			return new WB_Point(array[3*i],array[3*i+1],array[3*i+2]);
		}
		
		@Override
		public double getX(final int i) {
			return array[3*i];
		}
		@Override
		public double getY(final int i) {
			return array[3*i+1];
		}
		@Override
		public double getZ(final int i) {
			return array[3*i+2];
		}

		@Override
		public int size() {
			return array.length/3;
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] out = new WB_Point[array.length/3];
			for(int i = 0;i<array.length;i+=3){
				out[i/3] = new WB_Point(array[i],array[i+1],array[i+2]);
			}
			return out;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for(int i = 0;i<array.length;i+=3){
				list.add(new WB_Point(array[i],array[i+1],array[i+2]));
			}
			return list;
		}
	}

	static class WB_PointCollectionFactory extends WB_PointCollection {
		WB_Point[] array;

		WB_PointCollectionFactory(final WB_PointGenerator factory, int numberOfPoints) {
			this.array = new WB_Point[numberOfPoints] ;
			for(int i=0;i<numberOfPoints;i++) {
				array[i]=factory.nextPoint();
			}
		}
		

		WB_PointCollectionFactory(WB_Coord point, final WB_PointGenerator factory, int numberOfPoints) {
			this.array = new WB_Point[numberOfPoints+1] ;
			array[0]=new WB_Point(point);
			for(int i=1;i<=numberOfPoints;i++) {
				array[i]=factory.nextPoint();
			}
		}

		@Override
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> list = new FastList<WB_Coord>();
			for (WB_Point c : array) {
				list.add(c);
			}
			return list;
		}
	}
	
	static class WB_PointCollectionPolygon extends WB_PointCollection {
		WB_Polygon polygon;

		WB_PointCollectionPolygon(final WB_Polygon polygon) {
			this.polygon = polygon;
		}

		@Override
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[polygon.getNumberOfShellPoints()];
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

	static class WB_PointCollectionNoise extends WB_PointCollection {
		WB_PointCollection	source;
		WB_Vector[]			noise;

		WB_PointCollectionNoise(final WB_PointCollection source,
				final WB_VectorGenerator factory) {
			this.source = source;
			noise = new WB_Vector[source.size()];
			for (int i = 0; i < source.size(); i++) {
				noise[i] = factory.nextVector();
			}
		}

		@Override
		public WB_Point get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}
		
		@Override
		public double getX(final int i) {
			return source.getX(i)+noise[i].xd();
		}
		
		@Override
		public double getY(final int i) {
			return source.getY(i)+noise[i].yd();
		}
		
		
		@Override
		public double getZ(final int i) {
			return source.getZ(i)+noise[i].xd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[source.size()];
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

	static class WB_PointCollectionMap extends WB_PointCollection {
		WB_PointCollection	source;
		WB_Map3D				map;

		WB_PointCollectionMap(final WB_PointCollection source,
				final WB_Map3D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Point get(final int i) {
			return map.mapPoint3D(source.get(i));
		}
		@Override
		public double getX(int i) {
			
			return map.mapPoint3D(source.get(i)).xd();
		}

		@Override
		public double getY(int i) {
			
			return map.mapPoint3D(source.get(i)).yd();
		}

		@Override
		public double getZ(int i) {
			return map.mapPoint3D(source.get(i)).zd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[source.size()];
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

	static class WB_PointCollectionUnmap3D extends WB_PointCollection {
		WB_PointCollection	source;
		WB_Map3D				map;

		WB_PointCollectionUnmap3D(final WB_PointCollection source,
				final WB_Map3D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Point get(final int i) {
			return map.unmapPoint3D(source.get(i));
		}
		
		@Override
		public double getX(int i) {
			return map.unmapPoint3D(source.get(i)).xd();
		}

		@Override
		public double getY(int i) {
			return map.unmapPoint3D(source.get(i)).yd();
		}

		@Override
		public double getZ(int i) {
			return map.unmapPoint3D(source.get(i)).zd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[source.size()];
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

	static class WB_PointCollectionUnmap2D extends WB_PointCollection {
		WB_PointCollection	source;
		WB_Map2D			map;

		WB_PointCollectionUnmap2D(final WB_PointCollection source,
				final WB_Map2D map) {
			this.source = source;
			this.map = map;
		}

		@Override
		public WB_Point get(final int i) {
			return map.unmapPoint2D(source.get(i));
		}


		@Override
		public double getX(int i) {
			return map.unmapPoint2D(source.get(i)).xd();
		}

		@Override
		public double getY(int i) {
			return map.unmapPoint2D(source.get(i)).yd();
		}

		@Override
		public double getZ(int i) {
			return map.unmapPoint2D(source.get(i)).zd();
		}
		
		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[source.size()];
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

	static class WB_PointCollectionJitter extends WB_PointCollection {
		WB_PointCollection	source;
		WB_Vector[]			noise;

		WB_PointCollectionJitter(final WB_PointCollection source,
				final double d) {
			this.source = source;
			noise = new WB_Point[source.size()];
			WB_VectorGenerator factory = new WB_RandomOnSphere();
			for (int i = 0; i < source.size(); i++) {
				noise[i] = factory.nextVector().mulSelf(d);
			}
		}

		@Override
		public WB_Point get(final int i) {
			return WB_Point.add(source.get(i), noise[i]);
		}
		@Override
		public double getX(final int i) {
			return source.getX(i)+noise[i].xd();
		}
		
		@Override
		public double getY(final int i) {
			return source.getY(i)+noise[i].yd();
		}
		
		
		@Override
		public double getZ(final int i) {
			return source.getZ(i)+noise[i].xd();
		}

		@Override
		public int size() {
			return source.size();
		}

		@Override
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[source.size()];
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

	static class WB_PointCollectionUnique extends WB_PointCollection {
		List<WB_Point> list;

		WB_PointCollectionUnique(final WB_PointCollection source) {
			this.list = new FastList<WB_Point>();
			WB_KDTreeInteger3D<WB_Point> tree = new WB_KDTreeInteger3D<WB_Point>();
			WB_Point c;
			WB_KDEntryInteger<WB_Point>[] neighbors;
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
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[list.size()];
			int i = 0;
			for (WB_Point c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> clist = new FastList<WB_Coord>();
			for (WB_Point c : list) {
				clist.add(c);
			}
			return clist;
		}
	}

	static class WB_PointCollectionObj extends WB_PointCollection {
		List<WB_Point> list = new FastList<WB_Point>();

		WB_PointCollectionObj(String path, double scale) {
			this.list = new FastList<WB_Point>();
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
		public WB_Point get(final int i) {
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
		public WB_Point[] toArray() {
			WB_Point[] array = new WB_Point[list.size()];
			int i = 0;
			for (WB_Point c : list) {
				array[i++] = c;
			}
			return array;
		}

		@Override
		public List<WB_Coord> toList() {
			List<WB_Coord> clist = new FastList<WB_Coord>();
			for (WB_Point c : list) {
				clist.add(c);
			}
			return clist;
		}
	}
}
