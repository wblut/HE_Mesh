package wblut.geom;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3d;

import org.twak.camp.Corner;
import org.twak.camp.Edge;
import org.twak.camp.Machine;
import org.twak.camp.Output.Face;
import org.twak.camp.Output.LoopNormal;
import org.twak.camp.Skeleton;
import org.twak.utils.collections.Loop;
import org.twak.utils.collections.LoopL;

/**
 *
 */
public class WB_PyramidFactory {
	/**  */
	private WB_CoordCollection points;
	/**  */
	private boolean cap;
	/**  */
	private WB_Map2D map;

	/**
	 *
	 */
	public WB_PyramidFactory() {
		points = null;
		cap = true;
		map = new WB_DefaultMap2D();
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_PyramidFactory setPoints(final WB_CoordCollection points) {
		this.points = points;
		return this;
	}

	/**
	 *
	 *
	 * @param polygon
	 * @return
	 */
	public WB_PyramidFactory setPoints(final WB_Polygon polygon) {
		this.points = WB_CoordCollection.getCollection(polygon);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_PyramidFactory setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	public WB_PyramidFactory setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	/**
	 *
	 *
	 * @param cap
	 * @return
	 */
	public WB_PyramidFactory setCap(final boolean cap) {
		this.cap = cap;
		return this;
	}

	/**
	 *
	 *
	 * @param map
	 * @return
	 */
	public WB_PyramidFactory setMap(final WB_Map2D map) {
		this.map = map;
		return this;
	}

	/**
	 *
	 *
	 * @param angles
	 * @return
	 */
	public WB_SimpleMesh createPyramidWithAngle(final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_PyramidFactory.createPyramidWithAngle failed: invalid number of angles. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					i = 0;
					for (final Point3d p : faceloop) {
						tmp[i++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			final int n = tmpfaces.size();
			final int[][] faces = new int[cap ? n + 1 : n][];
			i = 0;
			for (final int[] tmp : tmpfaces) {
				faces[i++] = tmp;
			}
			if (cap) {
				faces[n] = new int[n];
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					faces[n][n - id - 1] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createPyramidWithAngle failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @param angles
	 * @return
	 */
	public WB_SimpleMesh createPyramidWithHeightAndAngle(final double height, final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_PyramidFactory.createPyramidWithAngle failed: invalid number of angles. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, height);
			skel.skeleton();
			skel.capAt(height);
			final List<LoopNormal> top = skel.output.nonSkelFaces;
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmpf = new int[faceloop.count()];
					int j = 0;
					for (final Point3d p : faceloop) {
						tmpf[j++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
						counter++;
					}
					tmpfaces.add(tmpf);
				}
			}
			final int n = points.size();
			if (cap && height != 0) {
				final int[] tmpf = new int[n];
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					tmpf[n - 1 - id] = counter++;
					lpoints.add(map.unmapPoint3D(coord.xd(), coord.yd(), 0));
				}
				tmpfaces.add(tmpf);
			}
			LoopL<? extends Point3d> toploop;
			for (i = 0; i < top.size(); i++) {
				toploop = top.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = 0;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k++] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
					}
					tmpfaces.add(tmpf);
				}
			}
			final int[][] faces = new int[tmpfaces.size()][];
			int j = 0;
			for (final int[] tmpf : tmpfaces) {
				faces[j++] = tmpf;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createPyramidWithAngleAndHeight failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createPyramidWithHeight(final double height) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			for (final Edge e : poly) {
				e.machine = new Machine(0.25 * Math.PI);
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			double zmax = -1.0;
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax = Math.max(zmax, p.z);
					}
				}
			}
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int i = 0;
					for (final Point3d p : faceloop) {
						tmp[i++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z * height / zmax));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			final int n = tmpfaces.size();
			final int[][] faces = new int[cap ? n + 1 : n][];
			int i = 0;
			for (final int[] tmp : tmpfaces) {
				faces[i++] = tmp;
			}
			if (cap && height != 0) {
				faces[n] = new int[n];
				i = 0;
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					faces[n][n - 1 - id] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			System.out.println("WB_PyramidFactory.createPyramidWithHeight failed failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @param offset
	 * @return
	 */
	public WB_SimpleMesh createPyramidWithHeightAndOffset(final double height, final double... offset) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		if (offset.length == 0) {
			return output;
		}
		try {
			final double[] angles = new double[offset.length];
			double maxoffset = offset[0];
			for (int i = 1; i < offset.length; i++) {
				if (offset[i] > maxoffset) {
					maxoffset = offset[i];
				}
			}
			for (int i = 0; i < offset.length; i++) {
				angles[i] = Math.atan(maxoffset / offset[i]);
			}
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_PyramidFactory.createOffset failed: invalid number of offsets. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, maxoffset);
			skel.skeleton();
			skel.capAt(maxoffset);
			final List<LoopNormal> top = skel.output.nonSkelFaces;
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			double zmax = -1.0;
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax = Math.max(zmax, p.z);
					}
				}
			}
			if (zmax < maxoffset) {
				maxoffset = zmax;
			}
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmpf = new int[faceloop.count()];
					int j = 0;
					for (final Point3d p : faceloop) {
						tmpf[j++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z / maxoffset * height));
						counter++;
					}
					tmpfaces.add(tmpf);
				}
			}
			final int n = points.size();
			if (cap && height != 0) {
				final int[] tmpf = new int[n];
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					tmpf[n - 1 - id] = counter++;
					lpoints.add(map.unmapPoint3D(coord.xd(), coord.yd(), 0));
				}
				tmpfaces.add(tmpf);
			}
			LoopL<? extends Point3d> toploop;
			for (i = 0; i < top.size(); i++) {
				toploop = top.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = 0;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k++] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z / maxoffset * height));
					}
					tmpfaces.add(tmpf);
				}
			}
			final int[][] faces = new int[tmpfaces.size()][];
			int j = 0;
			for (final int[] tmpf : tmpfaces) {
				faces[j++] = tmpf;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createOffset failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param angles
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithAngle(final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			final Loop<Edge> poly2 = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
				poly2.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				i = 0;
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else if (angles.length == 2 * points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_DipyramidFactory.createDipyramidWithAngle failed: invalid number of angles. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final LoopL<Edge> out2 = new LoopL<>();
			out2.add(poly2);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Skeleton skel2 = new Skeleton(out2, true);
			skel2.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			final Collection<Face> expfaces2 = skel2.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					i = 0;
					for (final Point3d p : faceloop) {
						tmp[i++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			for (final Face face : expfaces2) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp2 = new int[faceloop.count()];
					i = faceloop.count() - 1;
					for (final Point3d p : faceloop) {
						tmp2[i--] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z));
						counter++;
					}
					tmpfaces.add(tmp2);
				}
			}
			final int n = tmpfaces.size();
			final int[][] faces = new int[n][];
			i = 0;
			for (final int[] tmp : tmpfaces) {
				faces[i++] = tmp;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_DipyramidFactory.createDipyramidWithAngle failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @param height2
	 * @param angles
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeightAndAngle(final double height, final double height2,
			final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			final Loop<Edge> poly2 = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
				poly2.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				i = 0;
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else if (angles.length == 2 * points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_DipyramidFactory.createDipyramidWithAngle failed: invalid number of angles. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final LoopL<Edge> out2 = new LoopL<>();
			out2.add(poly2);
			final Skeleton skel = new Skeleton(out, height);
			skel.skeleton();
			skel.capAt(height);
			final Skeleton skel2 = new Skeleton(out2, height2);
			skel2.skeleton();
			skel2.capAt(height2);
			final List<LoopNormal> top = skel.output.nonSkelFaces;
			final Collection<Face> expfaces = skel.output.faces.values();
			final List<LoopNormal> top2 = skel2.output.nonSkelFaces;
			final Collection<Face> expfaces2 = skel2.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int j = 0;
					for (final Point3d p : faceloop) {
						tmp[j++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			for (final Face face : expfaces2) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int j = faceloop.count() - 1;
					for (final Point3d p : faceloop) {
						tmp[j--] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			LoopL<? extends Point3d> toploop;
			for (i = 0; i < top.size(); i++) {
				toploop = top.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = 0;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k++] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z));
					}
					tmpfaces.add(tmpf);
				}
			}
			for (i = 0; i < top2.size(); i++) {
				toploop = top2.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = toploop.get(j).count() - 1;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k--] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z));
					}
					tmpfaces.add(tmpf);
				}
			}
			final int[][] faces = new int[tmpfaces.size()][];
			int j = 0;
			for (final int[] tmpf : tmpfaces) {
				faces[j++] = tmpf;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_DipyramidFactory.createDipyramidWithAngleAndHeight failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @param height2
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeight(final double height, final double height2) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			final Loop<Edge> poly2 = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
				poly2.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			for (final Edge e : poly) {
				e.machine = new Machine(0.25 * Math.PI);
			}
			for (final Edge e : poly2) {
				e.machine = new Machine(0.25 * Math.PI);
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final LoopL<Edge> out2 = new LoopL<>();
			out2.add(poly2);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Skeleton skel2 = new Skeleton(out2, true);
			skel2.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			final Collection<Face> expfaces2 = skel2.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			double zmax = -1.0;
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax = Math.max(zmax, p.z);
					}
				}
			}
			double zmax2 = -1.0;
			for (final Face face : expfaces2) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax2 = Math.max(zmax2, p.z);
					}
				}
			}
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int i = 0;
					for (final Point3d p : faceloop) {
						tmp[i++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z * height / zmax));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			for (final Face face : expfaces2) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp2 = new int[faceloop.count()];
					int i = faceloop.count() - 1;
					for (final Point3d p : faceloop) {
						tmp2[i--] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z * height2 / zmax2));
						counter++;
					}
					tmpfaces.add(tmp2);
				}
			}
			final int n = tmpfaces.size();
			final int[][] faces = new int[n][];
			int i = 0;
			for (final int[] tmp : tmpfaces) {
				faces[i++] = tmp;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			System.out.println("WB_DipyramidFactory.createDipyramidWithHeight failed failed, returning empty mesh.");
		}
		return output;
	}

	/**
	 *
	 *
	 * @param height
	 * @param height2
	 * @param offset
	 * @return
	 */
	public WB_SimpleMesh createDipyramidWithHeightAndOffset(final double height, final double height2,
			final double... offset) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		if (offset.length == 0) {
			return output;
		}
		try {
			final double[] angles = new double[offset.length];
			double maxoffset = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < Math.min(points.size(), offset.length); i++) {
				maxoffset = Math.max(offset[i], maxoffset);
			}
			for (int i = 0; i < Math.min(points.size(), offset.length); i++) {
				angles[i] = Math.atan(maxoffset / offset[i]);
			}
			double maxoffset2 = (offset.length > points.size()) ? Double.NEGATIVE_INFINITY : maxoffset;
			for (int i = points.size(); i < offset.length; i++) {
				maxoffset2 = Math.max(offset[i], maxoffset2);
			}
			for (int i = points.size(); i < offset.length; i++) {
				angles[i] = Math.atan(maxoffset2 / offset[i]);
			}
			final WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<>();
			final Loop<Edge> poly2 = new Loop<>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
				poly2.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			int i = 0;
			if (angles.length == 1) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[0]);
				}
			} else if (angles.length == 2) {
				for (final Edge e : poly) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(Math.random() * (angles[1] - angles[0]) + angles[0]);
				}
			} else if (angles.length == points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				i = 0;
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else if (angles.length == 2 * points.size()) {
				for (final Edge e : poly) {
					e.machine = new Machine(angles[i++]);
				}
				for (final Edge e : poly2) {
					e.machine = new Machine(angles[i++]);
				}
			} else {
				System.out.println(
						"WB_DipyramidFactory.createOffset failed: invalid number of offsets. Returning empty mesh.");
				return output;
			}
			final LoopL<Edge> out = new LoopL<>();
			out.add(poly);
			final LoopL<Edge> out2 = new LoopL<>();
			out2.add(poly2);
			final Skeleton skel = new Skeleton(out, maxoffset);
			skel.skeleton();
			skel.capAt(maxoffset);
			final Skeleton skel2 = new Skeleton(out2, maxoffset2);
			skel2.skeleton();
			skel2.capAt(maxoffset2);
			final List<LoopNormal> top = skel.output.nonSkelFaces;
			final Collection<Face> expfaces = skel.output.faces.values();
			final List<LoopNormal> top2 = skel2.output.nonSkelFaces;
			final Collection<Face> expfaces2 = skel2.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new WB_List<>();
			final List<WB_Coord> lpoints = new WB_CoordList();
			double zmax = -1.0;
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax = Math.max(zmax, p.z);
					}
				}
			}
			if (zmax < maxoffset) {
				maxoffset = zmax;
			}
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmpf = new int[faceloop.count()];
					int j = 0;
					for (final Point3d p : faceloop) {
						tmpf[j++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z / maxoffset * height));
						counter++;
					}
					tmpfaces.add(tmpf);
				}
			}
			LoopL<? extends Point3d> toploop;
			for (i = 0; i < top.size(); i++) {
				toploop = top.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = 0;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k++] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z / maxoffset * height));
					}
					tmpfaces.add(tmpf);
				}
			}
			double zmax2 = -1.0;
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					for (final Point3d p : faceloop) {
						zmax2 = Math.max(zmax2, p.z);
					}
				}
			}
			if (zmax2 < maxoffset2) {
				maxoffset2 = zmax2;
			}
			for (final Face face : expfaces2) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmpf = new int[faceloop.count()];
					int j = faceloop.count() - 1;
					for (final Point3d p : faceloop) {
						tmpf[j--] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z / maxoffset2 * height2));
						counter++;
					}
					tmpfaces.add(tmpf);
				}
			}
			for (i = 0; i < top2.size(); i++) {
				toploop = top2.get(i).loopl;
				for (int j = 0; j < toploop.size(); j++) {
					final int[] tmpf = new int[toploop.get(j).count()];
					int k = toploop.get(j).count() - 1;
					for (final Point3d p : toploop.get(j)) {
						tmpf[k--] = counter++;
						lpoints.add(map.unmapPoint3D(p.x, p.y, -p.z / maxoffset2 * height2));
					}
					tmpfaces.add(tmpf);
				}
			}
			final int[][] faces = new int[tmpfaces.size()][];
			int j = 0;
			for (final int[] tmpf : tmpfaces) {
				faces[j++] = tmpf;
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("WB_DipyramidFactory.createOffset failed, returning empty mesh.");
		}
		return output;
	}
}
