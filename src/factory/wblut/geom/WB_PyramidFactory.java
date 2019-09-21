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

import wblut.external.straightskeleton.Corner;
import wblut.external.straightskeleton.Edge;
import wblut.external.straightskeleton.Loop;
import wblut.external.straightskeleton.LoopL;
import wblut.external.straightskeleton.Machine;
import wblut.external.straightskeleton.Output.Face;
import wblut.external.straightskeleton.Point3d;
import wblut.external.straightskeleton.Skeleton;

/**
 * @author FVH
 *
 */
public class WB_PyramidFactory {
	private WB_CoordCollection points;
	private boolean cap;
	private WB_Map2D map;

	public WB_PyramidFactory() {
		points = null;
		cap = true;
		map = new WB_DefaultMap2D();
	}

	public WB_PyramidFactory setPoints(final WB_CoordCollection points) {
		this.points = points;
		return this;
	}

	public WB_PyramidFactory setPoints(final WB_Polygon polygon) {
		this.points = WB_CoordCollection.getCollection(polygon);
		return this;
	}

	public WB_PyramidFactory setPoints(final Collection<? extends WB_Coord> points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	public WB_PyramidFactory setPoints(final WB_Coord[] points) {
		this.points = WB_CoordCollection.getCollection(points);
		return this;
	}

	public WB_PyramidFactory setCap(final boolean cap) {
		this.cap = cap;
		return this;
	}

	public WB_PyramidFactory setMap(final WB_Map2D map) {
		this.map = map;
		return this;
	}

	public WB_SimpleMesh createPyramidWithAngle(final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {

			WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<Edge>();
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
			final LoopL<Edge> out = new LoopL<Edge>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new FastList<int[]>();
			final List<WB_Coord> lpoints = new FastList<WB_Coord>();
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
					faces[n][id] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createPyramidWithAngle failed, returning empty mesh.");

		}
		return output;
	}

	public WB_SimpleMesh createPyramidWithHeightAndAngle(final double height, final double... angles) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {

			WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<Edge>();
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
			final LoopL<Edge> out = new LoopL<Edge>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, height);
			skel.skeleton();
			final LoopL<Corner> top = skel.flatTop;
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new FastList<int[]>();
			final List<WB_Coord> lpoints = new FastList<WB_Coord>();

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
			final int n = tmpfaces.size();
			final int[][] faces = new int[n + (cap ? 1 : 0) + top.size()][];
			int j = 0;
			for (final int[] tmp : tmpfaces) {
				faces[j++] = tmp;
			}
			if (cap) {
				faces[n] = new int[n];
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					faces[n][id] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}

			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + (cap ? 1 : 0) + i;
				faces[index] = new int[tmp.count()];
				j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					lpoints.add(map.unmapPoint3D(c.x, c.y, c.z));
				}
			}

			output = new WB_SimpleMesh(lpoints, faces);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createPyramidWithAngleAndHeight failed, returning empty mesh.");

		}
		return output;
	}

	/**
	 *
	 * @param height
	 * @return
	 */
	public WB_SimpleMesh createPyramidWithHeight(final double height) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		try {
			WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<Edge>();
			for (int i = 0; i < points.size(); i++) {
				poly.append(new Edge(corners[i], corners[(i + 1) % points.size()]));
			}
			for (final Edge e : poly) {
				e.machine = new Machine(0.25 * Math.PI);
			}
			final LoopL<Edge> out = new LoopL<Edge>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, true);
			skel.skeleton();
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new FastList<int[]>();
			final List<WB_Coord> lpoints = new FastList<WB_Coord>();
			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int i = 0;
					for (final Point3d p : faceloop) {
						tmp[i++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z == 0 ? 0 : height));
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
			if (cap) {
				faces[n] = new int[n];
				i = 0;
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					faces[n][id] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (Exception e) {
			System.out.println("WB_PyramidFactory.createPyramidWithHeight failed failed, returning empty mesh.");

		}
		return output;
	}

	public WB_SimpleMesh createOffset(final double height, final double... offset) {
		WB_SimpleMesh output = new WB_SimpleMesh();
		if (offset.length == 0) {
			return output;
		}
		try {
			double[] angles = new double[offset.length];
			double maxoffset = offset[0];
			for (int i = 1; i < offset.length; i++) {
				if (offset[i] > maxoffset) {
					maxoffset = offset[i];
				}
			}
			for (int i = 0; i < offset.length; i++) {
				angles[i] = Math.atan(maxoffset / offset[i]);
			}

			WB_CoordCollection mappedPoints = points.map(map);
			final Corner[] corners = new Corner[points.size()];
			int id = 0;
			WB_Coord coord;
			for (id = 0; id < points.size(); id++) {
				coord = mappedPoints.get(id);
				corners[id] = new Corner(coord.xd(), coord.yd());
			}
			final Loop<Edge> poly = new Loop<Edge>();
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

			final LoopL<Edge> out = new LoopL<Edge>();
			out.add(poly);
			final Skeleton skel = new Skeleton(out, maxoffset);
			skel.skeleton();
			final LoopL<Corner> top = skel.flatTop;
			final Collection<Face> expfaces = skel.output.faces.values();
			int counter = 0;
			final List<int[]> tmpfaces = new FastList<int[]>();
			final List<WB_Coord> lpoints = new FastList<WB_Coord>();

			for (final Face face : expfaces) {
				for (final Loop<Point3d> faceloop : face.points) {
					final int[] tmp = new int[faceloop.count()];
					int j = 0;
					for (final Point3d p : faceloop) {
						tmp[j++] = counter;
						lpoints.add(map.unmapPoint3D(p.x, p.y, p.z / maxoffset * height));
						counter++;
					}
					tmpfaces.add(tmp);
				}
			}
			final int n = tmpfaces.size();
			final int[][] faces = new int[n + top.size() + (cap && height != 0 ? 1 : 0)][];
			int j = 0;
			for (final int[] tmp : tmpfaces) {
				faces[j++] = tmp;
			}

			Loop<Corner> tmp;
			for (i = 0; i < top.size(); i++) {
				tmp = top.get(i);
				final int index = n + i;
				faces[index] = new int[tmp.count()];
				j = 0;
				for (final Corner c : tmp) {
					faces[index][j++] = counter++;
					lpoints.add(map.unmapPoint3D(c.x, c.y, c.z / maxoffset * height));
				}
			}
			if (cap && height != 0) {
				faces[n + top.size()] = new int[n];
				i = 0;
				for (id = 0; id < points.size(); id++) {
					coord = points.get(id);
					faces[n + top.size()][id] = counter++;
					lpoints.add(map.unmapPoint2D(coord.xd(), coord.yd()));
				}
			}
			output = new WB_SimpleMesh(lpoints, faces);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WB_PyramidFactory.createOffset failed, returning empty mesh.");

		}
		return output;
	}

}
