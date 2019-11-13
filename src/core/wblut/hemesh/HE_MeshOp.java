/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;


import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.core.WB_ProgressReporter.WB_ProgressTracker;
import wblut.geom.WB_AABB;
import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_AABBTree3D.WB_AABBNode3D;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_CoordinateSystem;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_IntersectionResult;
import wblut.geom.WB_KDTree3D;
import wblut.geom.WB_KDTree3D.WB_KDEntry;
import wblut.geom.WB_Line;
import wblut.geom.WB_OrthoProject;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Ray;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Sphere;
import wblut.geom.WB_Triangle;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_M33;
import wblut.math.WB_Math;

public class HE_MeshOp {
	
	public static class HE_FaceLineIntersection {

		/**
		 * 
		 */
		 HE_Face face;

		/**
		 * 
		 */
		 WB_Point point;

		/**
		 * 
		 *
		 * @param f
		 * @param p
		 */
		public HE_FaceLineIntersection(final HE_Face f, final WB_Point p) {
			face = f;
			point = p;
		}
		
		/**
		 *
		 *
		 * @return
		 */
		public HE_Face getFace() {
			return face;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Point getPoint() {
			return point;
		}
	}
	
	/**
	 *
	 */
	public static class HE_FaceFaceIntersection {
		/**
		 *
		 */
		HE_Face f1;
		/**
		 *
		 */
		HE_Face f2;
		/**
		 *
		 */
		WB_Segment segment;

		/**
		 *
		 *
		 * @param f1
		 * @param f2
		 * @param seg
		 */
		public HE_FaceFaceIntersection(final HE_Face f1, final HE_Face f2, final WB_Segment seg) {
			this.f1 = f1;
			this.f2 = f2;
			segment = seg;
		}

		/**
		 *
		 *
		 * @return
		 */
		public HE_Face getFace1() {
			return f1;
		}

		/**
		 *
		 *
		 * @return
		 */
		public HE_Face getFace2() {
			return f2;
		}

		/**
		 *
		 *
		 * @return
		 */
		public WB_Segment getSegment() {
			return segment;
		}
	}

	protected HE_MeshOp() {
	}

	private static WB_GeometryFactory gf = new WB_GeometryFactory();
	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	/**
	 * Split edge in half.
	 *
	 * @param mesh
	 * @param edge edge to split.
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final HE_Halfedge edge) {
		final WB_Point v = gf.createMidpoint(edge.getVertex(), edge.getEndVertex());
		return splitEdge(mesh, edge, v);
	}

	/**
	 * Split edge in two parts.
	 *
	 * @param mesh
	 * @param edge edge to split
	 * @param f    fraction of first part (0..1)
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final double f) {
		final WB_Point v = gf.createInterpolatedPoint(edge.getVertex(), edge.getEndVertex(),
				edge.isEdge() ? f : 1.0 - f);
		return splitEdge(mesh, edge, v);
	}

	/**
	 * Insert vertex in edge.
	 *
	 * @param mesh
	 * @param edge edge to split
	 * @param x    x-coordinate of new vertex
	 * @param y    y-coordinate of new vertex
	 * @param z    z-coordinate of new vertex
	 */
	public static void splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final double x, final double y,
			final double z) {
		splitEdge(mesh, edge, new WB_Point(x, y, z));
	}

	/**
	 * Split edge in multiple parts.
	 *
	 * @param mesh
	 * @param edge edge to split
	 * @param f    array of fractions (0..1)
	 */
	public static void splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final double[] f) {
		final double[] fArray = Arrays.copyOf(f, f.length);
		Arrays.sort(fArray);
		HE_Halfedge e = edge;
		final HE_Halfedge he0 = edge.isEdge() ? edge : edge.getPair();
		final HE_Halfedge he1 = he0.getPair();
		final HE_Vertex v0 = he0.getVertex();
		final HE_Vertex v1 = he1.getVertex();
		HE_Vertex v = new HE_Vertex();
		for (int i = 0; i < f.length; i++) {
			final double fi = fArray[i];
			if (fi > 0 && fi < 1) {
				v = new HE_Vertex(gf.createInterpolatedPoint(v0, v1, fi));
				e = splitEdge(mesh, e, v).eItr().next();
			}
		}
	}

	/**
	 * Split edge in multiple parts.
	 *
	 * @param mesh
	 * @param edge edge to split
	 * @param f    array of fractions (0..1)
	 */
	public static void splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final float[] f) {
		final float[] fArray = Arrays.copyOf(f, f.length);
		Arrays.sort(fArray);
		HE_Halfedge e = edge;
		final HE_Halfedge he0 = edge.isEdge() ? edge : edge.getPair();
		final HE_Halfedge he1 = he0.getPair();
		final HE_Vertex v0 = he0.getVertex();
		final HE_Vertex v1 = he1.getVertex();
		HE_Vertex v = new HE_Vertex();
		for (int i = 0; i < f.length; i++) {
			final double fi = fArray[i];
			if (fi > 0 && fi < 1) {
				v = new HE_Vertex(gf.createInterpolatedPoint(v0, v1, fi));
				e = splitEdge(mesh, e, v).eItr().next();
			}
		}
	}

	/**
	 * Insert vertex in edge.
	 *
	 * @param mesh
	 * @param edge edge to split
	 * @param v    position of new vertex
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final WB_Coord v) {
		final HE_Selection out = HE_Selection.getSelection(mesh);
		final HE_Halfedge he0 = edge.isEdge() ? edge : edge.getPair();
		final HE_Halfedge he1 = he0.getPair();
		final HE_Vertex vNew = new HE_Vertex(v);
		final HE_Halfedge he0new = new HE_Halfedge();
		final HE_Halfedge he1new = new HE_Halfedge();
		final HE_Halfedge he0n = he0.getNextInFace();
		final HE_Halfedge he1n = he1.getNextInFace();
		final double d0 = he0.getVertex().getPosition().getDistance(v);
		final double d1 = he1.getVertex().getPosition().getDistance(v);
		final double f0 = d1 / (d0 + d1);
		final double f1 = d0 / (d0 + d1);
		mesh.setVertex(he0new, vNew);
		mesh.setVertex(he1new, vNew);
		mesh.setHalfedge(vNew, he0new);
		mesh.setNext(he0new, he0n);
		he0new.copyProperties(he0);
		mesh.setNext(he1new, he1n);
		he1new.copyProperties(he1);
		if (he0.hasUVW() && he0n.hasUVW()) {
			he0new.setUVW(new HE_TextureCoordinate(f0, he0.getUVW(), he0n.getUVW()));
		}
		if (he1.hasUVW() && he1n.hasUVW()) {
			he1new.setUVW(new HE_TextureCoordinate(f1, he1.getUVW(), he1n.getUVW()));
		}
		mesh.setNext(he0, he0new);
		mesh.setNext(he1, he1new);
		mesh.setPair(he0, he1new);
		mesh.setPair(he0new, he1);
		if (he0.getFace() != null) {
			mesh.setFace(he0new, he0.getFace());
		}
		if (he1.getFace() != null) {
			mesh.setFace(he1new, he1.getFace());
		}
		vNew.setInternalLabel(1);
		mesh.addDerivedElement(vNew, edge);
		mesh.addDerivedElement(he0new, edge);
		mesh.addDerivedElement(he1new, edge);
		out.addEdge(he0new.isEdge() ? he0new : he1new);
		out.addEdge(he0.isEdge() ? he0 : he1);
		out.add(vNew);
		return out;
	}

	static HE_Selection splitEdge(final HE_Mesh mesh, final HE_Halfedge edge, final HE_Vertex v) {
		final HE_Selection out = HE_Selection.getSelection(mesh);
		final HE_Halfedge he0 = edge.isEdge() ? edge : edge.getPair();
		final HE_Halfedge he1 = he0.getPair();
		final HE_Vertex vNew = v;
		final HE_Halfedge he0new = new HE_Halfedge();
		final HE_Halfedge he1new = new HE_Halfedge();
		final HE_Halfedge he0n = he0.getNextInFace();
		final HE_Halfedge he1n = he1.getNextInFace();
		final double d0 = he0.getVertex().getPosition().getDistance(v);
		final double d1 = he1.getVertex().getPosition().getDistance(v);
		final double f0 = d1 / (d0 + d1);
		final double f1 = d0 / (d0 + d1);
		mesh.setVertex(he0new, vNew);
		mesh.setVertex(he1new, vNew);
		mesh.setHalfedge(vNew, he0new);
		mesh.setNext(he0new, he0n);
		he0new.copyProperties(he0);
		mesh.setNext(he1new, he1n);
		he1new.copyProperties(he1);
		if (he0.hasUVW() && he0n.hasUVW()) {
			he0new.setUVW(new HE_TextureCoordinate(f0, he0.getUVW(), he0n.getUVW()));
		}
		if (he1.hasUVW() && he1n.hasUVW()) {
			he1new.setUVW(new HE_TextureCoordinate(f1, he1.getUVW(), he1n.getUVW()));
		}
		mesh.setNext(he0, he0new);
		mesh.setNext(he1, he1new);
		mesh.setPair(he0, he1new);
		mesh.setPair(he0new, he1);
		if (he0.getFace() != null) {
			mesh.setFace(he0new, he0.getFace());
		}
		if (he1.getFace() != null) {
			mesh.setFace(he1new, he1.getFace());
		}
		vNew.setInternalLabel(1);
		mesh.addDerivedElement(vNew, edge);
		mesh.addDerivedElement(he0new, edge);
		mesh.addDerivedElement(he1new, edge);
		out.addEdge(he0new.isEdge() ? he0new : he1new);
		out.addEdge(he0.isEdge() ? he0 : he1);
		out.add(vNew);
		return out;
	}

	/**
	 * Split edge in half.
	 *
	 * @param mesh
	 * @param key  key of edge to split.
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final long key) {
		final HE_Halfedge edge = mesh.getHalfedgeWithKey(key);
		final WB_Point v = gf.createMidpoint(edge.getVertex(), edge.getEndVertex());
		return splitEdge(mesh, edge, v);
	}

	/**
	 * Split edge in two parts.
	 *
	 * @param mesh
	 * @param key  key of edge to split
	 * @param f    fraction of first part (0..1)
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final long key, final double f) {
		final HE_Halfedge edge = mesh.getHalfedgeWithKey(key);
		return splitEdge(mesh, edge, f);
	}

	/**
	 * Insert vertex in edge.
	 *
	 * @param mesh
	 * @param key  key of edge to split
	 * @param x    x-coordinate of new vertex
	 * @param y    y-coordinate of new vertex
	 * @param z    z-coordinate of new vertex
	 */
	public static void splitEdge(final HE_Mesh mesh, final long key, final double x, final double y, final double z) {
		splitEdge(mesh, key, new WB_Point(x, y, z));
	}

	/**
	 * Split edge in multiple parts.
	 *
	 * @param mesh
	 * @param key  key of edge to split
	 * @param f    array of fractions (0..1)
	 */
	public static void splitEdge(final HE_Mesh mesh, final long key, final double[] f) {
		final HE_Halfedge edge = mesh.getHalfedgeWithKey(key);
		splitEdge(mesh, edge, f);
	}

	/**
	 * Split edge in multiple parts.
	 *
	 * @param mesh
	 * @param key  key of edge to split
	 * @param f    array of fractions (0..1)
	 */
	public static void splitEdge(final HE_Mesh mesh, final long key, final float[] f) {
		final HE_Halfedge edge = mesh.getHalfedgeWithKey(key);
		splitEdge(mesh, edge, f);
	}

	/**
	 * Insert vertex in edge.
	 *
	 * @param mesh
	 * @param key  key of edge to split
	 * @param v    position of new vertex
	 *
	 * @return selection of new vertex and new edge
	 */
	public static HE_Selection splitEdge(final HE_Mesh mesh, final Long key, final WB_Point v) {
		final HE_Halfedge edge = mesh.getHalfedgeWithKey(key);
		return splitEdge(mesh, edge, v);
	}

	/**
	 * Split all edges in half.
	 *
	 * @param mesh
	 * @return selection of new vertices and new edges
	 */
	public static HE_Selection splitEdges(final HE_Mesh mesh) {
		final HE_Selection selectionOut = HE_Selection.getSelection(mesh);
		final HE_Halfedge[] edges = mesh.getEdgesAsArray();
		final int n = edges.length;
		for (int i = 0; i < n; i++) {
			selectionOut.add(splitEdge(mesh, edges[i], 0.5));
		}
		return selectionOut;
	}

	/**
	 * Split all edges in half, offset the center by a given distance along the edge
	 * normal.
	 *
	 * @param mesh
	 * @param offset the offset
	 *
	 * @return selection of new vertices and new edges
	 */
	public static HE_Selection splitEdges(final HE_Mesh mesh, final double offset) {
		final HE_Selection selectionOut = HE_Selection.getSelection(mesh);
		final HE_Halfedge[] edges = mesh.getEdgesAsArray();
		final int n = mesh.getNumberOfEdges();
		for (int i = 0; i < n; i++) {
			final WB_Point p = new WB_Point(getEdgeNormal(edges[i]));
			p.mulSelf(offset).addSelf(getHalfedgeCenter(edges[i]));
			selectionOut.add(splitEdge(mesh, edges[i], p));
		}
		return selectionOut;
	}

	/**
	 * Split edge in half.
	 *
	 * @param selection edges to split.
	 * @return selection of new vertices and new edges
	 */
	public static HE_Selection splitEdges(final HE_Selection selection) {
		final HE_Selection selectionOut = HE_Selection.getSelection(selection.getParent());
		selection.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = selection.heItr();
		while (eItr.hasNext()) {
			selectionOut.add(splitEdge(selection.getParent(), eItr.next(), 0.5));
		}
		selection.addHalfedges(selectionOut.getEdgesAsArray());
		return selectionOut;
	}

	/**
	 * Split edge in half, offset the center by a given distance along the edge
	 * normal.
	 *
	 * @param selection edges to split.
	 * @param offset    the offset
	 * @return selection of new vertices and new edges
	 */
	public static HE_Selection splitEdges(final HE_Selection selection, final double offset) {
		final HE_Selection selectionOut = HE_Selection.getSelection(selection.getParent());
		selection.collectEdgesByFace();
		final Iterator<HE_Halfedge> eItr = selection.heItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			final WB_Point p = new WB_Point(getEdgeNormal(e));
			p.mulSelf(offset).addSelf(getHalfedgeCenter(e));
			selectionOut.add(splitEdge(selection.getParent(), e, p));
		}
		selection.addHalfedges(selectionOut.getEdgesAsArray());
		return selectionOut;
	}

	/**
	 * Divide edge.
	 *
	 * @param mesh
	 * @param origE edge to divide
	 * @param n     number of parts
	 */
	public static void divideEdge(final HE_Mesh mesh, final HE_Halfedge origE, final int n) {
		if (n > 1) {
			final double[] f = new double[n - 1];
			final double in = 1.0 / n;
			for (int i = 0; i < n - 1; i++) {
				f[i] = (i + 1) * in;
			}
			splitEdge(mesh, origE, f);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param key
	 * @param n
	 */
	public static void divideEdge(final HE_Mesh mesh, final long key, final int n) {
		divideEdge(mesh, mesh.getHalfedgeWithKey(key), n);
	}

	/**
	 * Divide face along two vertices.
	 *
	 * @param mesh
	 * @param face face to divide
	 * @param vi   first vertex
	 * @param vj   second vertex
	 *
	 * @return new face and edge
	 */
	public static HE_Selection splitFace(final HE_Mesh mesh, final HE_Face face, final HE_Vertex vi,
			final HE_Vertex vj) {
		final HE_Selection out = HE_Selection.getSelection(mesh);
		final HE_Halfedge hei = vi.getHalfedge(face);
		final HE_Halfedge hej = vj.getHalfedge(face);
		final HE_TextureCoordinate ti = hei.hasUVW() ? hei.getUVW() : null;
		final HE_TextureCoordinate tj = hej.hasUVW() ? hej.getUVW() : null;
		final double d = vi.getPosition().getDistance(vj);
		boolean degenerate = false;
		if (WB_Epsilon.isZero(d)) {// happens when a collinear (part of a) face
			// is cut. Do not add a new edge connecting
			// these two points,rather collapse them into
			// each other and remove two-edge faces
			degenerate = true;
		}
		if (hei.getNextInFace() != hej || hei.getPrevInFace() != hej) {
			HE_Halfedge heiPrev;
			HE_Halfedge hejPrev;
			HE_Face faceNew;
			if (!degenerate) {
				HE_Halfedge he0new;
				HE_Halfedge he1new;
				heiPrev = hei.getPrevInFace();
				hejPrev = hej.getPrevInFace();
				he0new = new HE_Halfedge();
				he1new = new HE_Halfedge();
				mesh.setVertex(he0new, vj);
				if (tj != null) {
					he0new.setUVW(tj);
				}
				mesh.setVertex(he1new, vi);
				if (ti != null) {
					he1new.setUVW(ti);
				}
				mesh.setNext(he0new, hei);
				mesh.setNext(he1new, hej);
				mesh.setNext(heiPrev, he1new);
				mesh.setNext(hejPrev, he0new);
				mesh.setPair(he0new, he1new);
				he0new.setInternalLabel(1);
				he1new.setInternalLabel(1);
				mesh.setFace(he0new, face);
				faceNew = new HE_Face();
				mesh.setHalfedge(face, hei);
				mesh.setHalfedge(faceNew, hej);
				faceNew.copyProperties(face);
				assignFaceToLoop(mesh, faceNew, hej);
				mesh.addDerivedElement(he0new, face);
				mesh.addDerivedElement(he1new, face);
				mesh.addDerivedElement(faceNew, face);
				out.addEdge(he0new.isEdge() ? he0new : he1new);
				out.add(faceNew);
				return out;
			} else {
				heiPrev = hei.getPrevInFace();
				hejPrev = hej.getPrevInFace();
				for (final HE_Halfedge hejs : vj.getHalfedgeStar()) {
					mesh.setVertex(hejs, vi);
				}
				mesh.setNext(heiPrev, hej);
				mesh.setNext(hejPrev, hei);
				faceNew = new HE_Face();
				mesh.setHalfedge(face, hei);
				mesh.setHalfedge(faceNew, hej);
				faceNew.copyProperties(face);
				assignFaceToLoop(mesh, faceNew, hej);
				mesh.addDerivedElement(faceNew, face);
				mesh.remove(vj);
				out.add(faceNew);
				if (face.getFaceDegree() == 2) {
					HET_Fixer.deleteTwoEdgeFace(mesh, face);
				}
				if (faceNew.getFaceDegree() == 2) {
					HET_Fixer.deleteTwoEdgeFace(mesh, faceNew);
					out.remove(faceNew);
				}
				return out;
			}
		}
		return null;
	}

	/**
	 * Divide face along two vertices.
	 *
	 * @param mesh
	 * @param fkey  key of face
	 * @param vkeyi key of first vertex
	 * @param vkeyj key of second vertex
	 *
	 * @return new face and edge
	 */
	public static HE_Selection splitFace(final HE_Mesh mesh, final long fkey, final long vkeyi, final long vkeyj) {
		return splitFace(mesh, mesh.getFaceWithKey(fkey), mesh.getVertexWithKey(vkeyi), mesh.getVertexWithKey(vkeyj));
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Mesh mesh) {
		final HEM_CenterSplit cs = new HEM_CenterSplit();
		mesh.modify(cs);
		return mesh.getSelection("center");
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Mesh mesh, final double d) {
		final HEM_CenterSplit cs = new HEM_CenterSplit().setOffset(d);
		mesh.modify(cs);
		return mesh.getSelection("center");
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 * @param c
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Mesh mesh, final double d, final double c) {
		final HEM_CenterSplit cs = new HEM_CenterSplit().setOffset(d).setChamfer(c);
		mesh.modify(cs);
		return mesh.getSelection("center");
	}

	/**
	 *
	 *
	 * @param faces
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Selection faces) {
		final HEM_CenterSplit cs = new HEM_CenterSplit();
		faces.modify(cs);
		return faces.getParent().getSelection("center");
	}

	/**
	 *
	 *
	 * @param faces
	 * @param d
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Selection faces, final double d) {
		final HEM_CenterSplit cs = new HEM_CenterSplit().setOffset(d);
		faces.modify(cs);
		return faces.getParent().getSelection("center");
	}

	/**
	 *
	 *
	 * @param faces
	 * @param d
	 * @param c
	 * @return
	 */
	public static HE_Selection splitFacesCenter(final HE_Selection faces, final double d, final double c) {
		final HEM_CenterSplit cs = new HEM_CenterSplit().setOffset(d).setChamfer(c);
		faces.modify(cs);
		return faces.getParent().getSelection("center");
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Mesh mesh) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole();
		mesh.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Mesh mesh, final double d) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole().setOffset(d);
		mesh.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param d
	 * @param c
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Mesh mesh, final double d, final double c) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole().setOffset(d).setChamfer(c);
		mesh.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 *
	 *
	 * @param faces
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Selection faces) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole();
		faces.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 *
	 *
	 * @param faces
	 * @param d
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Selection faces, final double d) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole().setOffset(d);
		faces.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 *
	 *
	 * @param faces
	 * @param d
	 * @param c
	 * @return
	 */
	public static HE_Selection splitFacesCenterHole(final HE_Selection faces, final double d, final double c) {
		final HEM_CenterSplitHole csh = new HEM_CenterSplitHole().setOffset(d).setChamfer(c);
		faces.modify(csh);
		return csh.getWallFaces();
	}

	/**
	 * Hybrid split faces: midsplit for triangles, quad split otherwise.
	 *
	 * @param mesh
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesHybrid(final HE_Mesh mesh) {
		final HE_Selection selectionOut = HE_Selection.getSelection(mesh);
		final int n = mesh.getNumberOfFaces();
		final WB_Coord[] faceCenters = new WB_Coord[n];
		final int[] faceOrders = new int[n];
		HE_Face f;
		int i = 0;
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			faceCenters[i] = getFaceCenter(f);
			faceOrders[i] = f.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(mesh);
		orig.addFaces(mesh.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(splitEdges(mesh).getVerticesAsArray());
		final HE_Face[] faces = mesh.getFacesAsArray();
		HE_Vertex vi = new HE_Vertex();
		int fo;
		for (i = 0; i < n; i++) {
			f = faces[i];
			fo = f.getFaceDegree() / 2;
			if (fo == 3) {
				HE_Halfedge startHE = f.getHalfedge();
				while (orig.contains(startHE.getVertex())) {
					startHE = startHE.getNextInFace();
				}
				HE_Halfedge he = startHE;
				final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
				final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
				int c = 0;
				do {
					textures[c++] = he.hasUVW() ? he.getUVW() : null;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				c = 0;
				do {
					final HE_Face fn = new HE_Face();
					fn.copyProperties(f);
					mesh.addDerivedElement(fn, f);
					he0[c] = he;
					mesh.setFace(he, fn);
					mesh.setHalfedge(fn, he);
					he1[c] = he.getNextInFace();
					he2[c] = new HE_Halfedge();
					hec[c] = new HE_Halfedge();
					mesh.setVertex(hec[c], he.getVertex());
					if (textures[c] != null) {
						hec[c].setUVW(textures[c]);
					}
					mesh.setPair(hec[c], he2[c]);
					mesh.setFace(hec[c], f);
					mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
					if (textures[(c + 1) % fo] != null) {
						he2[c].setUVW(textures[(c + 1) % fo]);
					}
					mesh.setNext(he2[c], he0[c]);
					mesh.setFace(he1[c], fn);
					mesh.setFace(he2[c], fn);
					mesh.addDerivedElement(he2[c], f);
					mesh.addDerivedElement(hec[c], f);
					c++;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				mesh.setHalfedge(f, hec[0]);
				for (int j = 0; j < c; j++) {
					mesh.setNext(he1[j], he2[j]);
					mesh.setNext(hec[j], hec[(j + 1) % c]);
				}
			} else if (fo > 3) {
				vi = new HE_Vertex(faceCenters[i]);
				vi.setInternalLabel(2);
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = f.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(f)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(f).ud();
					v += he.getVertex().getUVW(f).vd();
					w += he.getVertex().getUVW(f).wd();
					he = he.getNextInFace();
				} while (he != f.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / f.getFaceDegree();
					vi.setUVW(u * ifo, v * ifo, w * ifo);
				}
				mesh.addDerivedElement(vi, f);
				selectionOut.add(vi);
				HE_Halfedge startHE = f.getHalfedge();
				while (orig.contains(startHE.getVertex())) {
					startHE = startHE.getNextInFace();
				}
				he = startHE;
				final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he3 = new HE_Halfedge[faceOrders[i]];
				int c = 0;
				do {
					HE_Face fc;
					if (c == 0) {
						fc = f;
					} else {
						fc = new HE_Face();
						fc.copyProperties(f);
						mesh.addDerivedElement(fc, f);
					}
					he0[c] = he;
					mesh.setFace(he, fc);
					mesh.setHalfedge(fc, he);
					he1[c] = he.getNextInFace();
					he2[c] = new HE_Halfedge();
					he3[c] = new HE_Halfedge();
					mesh.addDerivedElement(he2[c], f);
					mesh.addDerivedElement(he3[c], f);
					mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
					if (he2[c].getVertex().hasHalfedgeUVW(f)) {
						he2[c].setUVW(he2[c].getVertex().getHalfedgeUVW(f));
					}
					mesh.setVertex(he3[c], vi);
					mesh.setNext(he2[c], he3[c]);
					mesh.setNext(he3[c], he);
					mesh.setFace(he1[c], fc);
					mesh.setFace(he2[c], fc);
					mesh.setFace(he3[c], fc);
					c++;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				mesh.setHalfedge(vi, he3[0]);
				for (int j = 0; j < c; j++) {
					mesh.setNext(he1[j], he2[j]);
				}
			}
		}
		HE_MeshOp.pairHalfedges(mesh);
		return selectionOut;
	}

	/**
	 * Hybrid split faces: midsplit for triangles, quad split otherwise.
	 *
	 * @param sel the sel
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesHybrid(final HE_Selection sel) {
		final HE_Selection selectionOut = HE_Selection.getSelection(sel.getParent());
		final int n = sel.getNumberOfFaces();
		final WB_Coord[] faceCenters = new WB_Coord[n];
		final int[] faceOrders = new int[n];
		HE_Face f;
		int i = 0;
		final Iterator<HE_Face> fItr = sel.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			faceCenters[i] = getFaceCenter(f);
			faceOrders[i] = f.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(sel.getParent());
		orig.addFaces(sel.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(splitEdges(sel.getParent()).getVerticesAsArray());
		final HE_Face[] faces = sel.getFacesAsArray();
		HE_Vertex vi = new HE_Vertex();
		int fo;
		for (i = 0; i < n; i++) {
			f = faces[i];
			fo = f.getFaceDegree() / 2;
			if (fo == 3) {
				HE_Halfedge startHE = f.getHalfedge();
				while (orig.contains(startHE.getVertex())) {
					startHE = startHE.getNextInFace();
				}
				HE_Halfedge he = startHE;
				final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
				final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
				int c = 0;
				do {
					textures[c++] = he.hasUVW() ? he.getUVW() : null;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				c = 0;
				do {
					final HE_Face fn = new HE_Face();
					fn.copyProperties(f);
					sel.getParent().addDerivedElement(fn, f);
					sel.add(fn);
					he0[c] = he;
					sel.getParent().setFace(he, fn);
					sel.getParent().setHalfedge(fn, he);
					he1[c] = he.getNextInFace();
					he2[c] = new HE_Halfedge();
					hec[c] = new HE_Halfedge();
					sel.getParent().setVertex(hec[c], he.getVertex());
					if (textures[c] != null) {
						hec[c].setUVW(textures[c]);
					}
					sel.getParent().setPair(hec[c], he2[c]);
					sel.getParent().setFace(hec[c], f);
					sel.getParent().setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
					if (textures[(c + 1) % fo] != null) {
						he2[c].setUVW(textures[(c + 1) % fo]);
					}
					sel.getParent().setNext(he2[c], he0[c]);
					sel.getParent().setFace(he1[c], fn);
					sel.getParent().setFace(he2[c], fn);
					sel.getParent().addDerivedElement(he2[c], f);
					sel.getParent().addDerivedElement(hec[c], f);
					c++;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				sel.getParent().setHalfedge(f, hec[0]);
				for (int j = 0; j < c; j++) {
					sel.getParent().setNext(he1[j], he2[j]);
					sel.getParent().setNext(hec[j], hec[(j + 1) % c]);
				}
			} else if (fo > 3) {
				vi = new HE_Vertex(faceCenters[i]);
				vi.setInternalLabel(2);
				double u = 0;
				double v = 0;
				double w = 0;
				HE_Halfedge he = f.getHalfedge();
				boolean hasTexture = true;
				do {
					if (!he.getVertex().hasUVW(f)) {
						hasTexture = false;
						break;
					}
					u += he.getVertex().getUVW(f).ud();
					v += he.getVertex().getUVW(f).vd();
					w += he.getVertex().getUVW(f).wd();
					he = he.getNextInFace();
				} while (he != f.getHalfedge());
				if (hasTexture) {
					final double ifo = 1.0 / f.getFaceDegree();
					vi.setUVW(u * ifo, v * ifo, w * ifo);
				}
				sel.getParent().addDerivedElement(vi, f);
				selectionOut.add(vi);
				HE_Halfedge startHE = f.getHalfedge();
				while (orig.contains(startHE.getVertex())) {
					startHE = startHE.getNextInFace();
				}
				he = startHE;
				final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
				final HE_Halfedge[] he3 = new HE_Halfedge[faceOrders[i]];
				int c = 0;
				do {
					HE_Face fc;
					if (c == 0) {
						fc = f;
					} else {
						fc = new HE_Face();
						fc.copyProperties(f);
						sel.getParent().addDerivedElement(fc, f);
						sel.add(fc);
					}
					he0[c] = he;
					sel.getParent().setFace(he, fc);
					sel.getParent().setHalfedge(fc, he);
					he1[c] = he.getNextInFace();
					he2[c] = new HE_Halfedge();
					he3[c] = new HE_Halfedge();
					sel.getParent().addDerivedElement(he2[c], f);
					sel.getParent().addDerivedElement(he3[c], f);
					sel.getParent().setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
					if (he2[c].getVertex().hasHalfedgeUVW(f)) {
						he2[c].setUVW(he2[c].getVertex().getHalfedgeUVW(f));
					}
					sel.getParent().setVertex(he3[c], vi);
					sel.getParent().setNext(he2[c], he3[c]);
					sel.getParent().setNext(he3[c], he);
					sel.getParent().setFace(he1[c], fc);
					sel.getParent().setFace(he2[c], fc);
					sel.getParent().setFace(he3[c], fc);
					c++;
					he = he.getNextInFace().getNextInFace();
				} while (he != startHE);
				sel.getParent().setHalfedge(vi, he3[0]);
				for (int j = 0; j < c; j++) {
					sel.getParent().setNext(he1[j], he2[j]);
				}
			}
		}
		HE_MeshOp.pairHalfedges(sel.getParent());
		return selectionOut;
	}

	/**
	 * Midedge split faces.
	 *
	 * @param mesh
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesMidEdge(final HE_Mesh mesh) {
		final HE_Selection selectionOut = HE_Selection.getSelection(mesh);
		final int n = mesh.getNumberOfFaces();
		final int[] faceOrders = new int[n];
		HE_Face face;
		int i = 0;
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			faceOrders[i] = face.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(mesh);
		orig.addFaces(mesh.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(splitEdges(mesh).getVerticesAsArray());
		final HE_Face[] faces = mesh.getFacesAsArray();
		for (i = 0; i < n; i++) {
			face = faces[i];
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			HE_Halfedge he = startHE;
			final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final int fo = face.getFaceDegree() / 2;
			final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
			int c = 0;
			do {
				textures[c++] = he.hasUVW() ? he.getUVW() : null;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			c = 0;
			he = startHE;
			do {
				final HE_Face f = new HE_Face();
				f.copyProperties(face);
				mesh.addDerivedElement(f, face);
				he0[c] = he;
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				hec[c] = new HE_Halfedge();
				mesh.setVertex(hec[c], he.getVertex());
				if (textures[c] != null) {
					hec[c].setUVW(textures[c]);
				}
				mesh.setPair(hec[c], he2[c]);
				mesh.setFace(hec[c], face);
				mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (textures[(c + 1) % fo] != null) {
					he2[c].setUVW(textures[(c + 1) % fo]);
				}
				mesh.setNext(he2[c], he0[c]);
				mesh.setFace(he0[c], f);
				mesh.setHalfedge(f, he0[c]);
				mesh.setFace(he1[c], f);
				mesh.setFace(he2[c], f);
				mesh.addDerivedElement(he2[c], face);
				mesh.addDerivedElement(hec[c], face);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			mesh.setHalfedge(face, hec[0]);
			for (int j = 0; j < c; j++) {
				mesh.setNext(he1[j], he2[j]);
				mesh.setNext(hec[j], hec[(j + 1) % c]);
			}
		}
		return selectionOut;
	}

	/**
	 * Mid edge split selected faces.
	 *
	 * @param selection selection to split
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesMidEdge(final HE_Selection selection) {
		final HE_Selection selectionOut = HE_Selection.getSelection(selection.getParent());
		final int n = selection.getNumberOfFaces();
		final int[] faceOrders = new int[n];
		HE_Face face;
		final Iterator<HE_Face> fItr = selection.fItr();
		int i = 0;
		while (fItr.hasNext()) {
			face = fItr.next();
			faceOrders[i] = face.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(selection.getParent());
		orig.addFaces(selection.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(HE_MeshOp.splitEdges(orig).getVerticesAsArray());
		final HE_Face[] faces = selection.getFacesAsArray();
		for (i = 0; i < n; i++) {
			face = faces[i];
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			HE_Halfedge he = startHE;
			final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final int fo = face.getFaceDegree() / 2;
			final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
			int c = 0;
			do {
				textures[c++] = he.hasUVW() ? he.getUVW() : null;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			c = 0;
			do {
				final HE_Face f = new HE_Face();
				selection.getParent().addDerivedElement(f, face);
				f.copyProperties(face);
				selection.add(f);
				he0[c] = he;
				selection.getParent().setFace(he, f);
				selection.getParent().setHalfedge(f, he);
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				hec[c] = new HE_Halfedge();
				selection.getParent().setVertex(hec[c], he.getVertex());
				if (textures[c] != null) {
					hec[c].setUVW(textures[c]);
				}
				selection.getParent().setPair(hec[c], he2[c]);
				selection.getParent().setFace(hec[c], face);
				selection.getParent().setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (textures[(c + 1) % fo] != null) {
					he2[c].setUVW(textures[(c + 1) % fo]);
				}
				selection.getParent().setNext(he2[c], he0[c]);
				selection.getParent().setFace(he1[c], f);
				selection.getParent().setFace(he2[c], f);
				selection.getParent().addDerivedElement(he2[c], face);
				selection.getParent().addDerivedElement(hec[c], face);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			selection.getParent().setHalfedge(face, hec[0]);
			for (int j = 0; j < c; j++) {
				selection.getParent().setNext(he1[j], he2[j]);
				selection.getParent().setNext(hec[j], hec[(j + 1) % c]);
			}
		}
		return selectionOut;
	}

	/**
	 * Mid edge split faces.
	 *
	 * @param mesh
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesMidEdgeHole(final HE_Mesh mesh) {
		final HE_Selection selectionOut = HE_Selection.getSelection(mesh);
		final int n = mesh.getNumberOfFaces();
		final int[] faceOrders = new int[n];
		HE_Face face;
		int i = 0;
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			face = fItr.next();
			faceOrders[i] = face.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(mesh);
		orig.addFaces(mesh.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(splitEdges(mesh).getVerticesAsArray());
		final HE_Face[] faces = mesh.getFacesAsArray();
		for (i = 0; i < n; i++) {
			face = faces[i];
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			HE_Halfedge he = startHE;
			final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final int fo = face.getFaceDegree() / 2;
			final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
			int c = 0;
			do {
				textures[c++] = he.hasUVW() ? he.getUVW() : null;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			c = 0;
			do {
				final HE_Face f = new HE_Face();
				f.copyProperties(face);
				mesh.addDerivedElement(f, face);
				he0[c] = he;
				mesh.setFace(he, f);
				mesh.setHalfedge(f, he);
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				hec[c] = new HE_Halfedge();
				mesh.setVertex(hec[c], he.getVertex());
				if (textures[c] != null) {
					hec[c].setUVW(textures[c]);
				}
				mesh.setPair(hec[c], he2[c]);
				mesh.setFace(hec[c], face);
				mesh.setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (textures[(c + 1) % fo] != null) {
					he2[c].setUVW(textures[(c + 1) % fo]);
				}
				mesh.setNext(he2[c], he0[c]);
				mesh.setFace(he1[c], f);
				mesh.setFace(he2[c], f);
				mesh.addDerivedElement(he2[c], face);
				mesh.addDerivedElement(hec[c], face);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			mesh.setHalfedge(face, hec[0]);
			for (int j = 0; j < c; j++) {
				mesh.setNext(he1[j], he2[j]);
				mesh.setNext(hec[j], hec[(j + 1) % c]);
			}
			mesh.deleteFace(face);
		}
		return selectionOut;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	public static HE_Selection splitFacesMidEdgeHole(final HE_Selection selection) {
		final HE_Selection selectionOut = HE_Selection.getSelection(selection.getParent());
		final int n = selection.getNumberOfFaces();
		final int[] faceOrders = new int[n];
		HE_Face face;
		final Iterator<HE_Face> fItr = selection.fItr();
		int i = 0;
		while (fItr.hasNext()) {
			face = fItr.next();
			faceOrders[i] = face.getFaceDegree();
			i++;
		}
		final HE_Selection orig = HE_Selection.getSelection(selection.getParent());
		orig.addFaces(selection.getFacesAsArray());
		orig.collectVertices();
		orig.collectEdgesByFace();
		selectionOut.addVertices(splitEdges(orig).getVerticesAsArray());
		final HE_Face[] faces = selection.getFacesAsArray();
		for (i = 0; i < n; i++) {
			face = faces[i];
			HE_Halfedge startHE = face.getHalfedge();
			while (orig.contains(startHE.getVertex())) {
				startHE = startHE.getNextInFace();
			}
			HE_Halfedge he = startHE;
			final HE_Halfedge[] hec = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he0 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he1 = new HE_Halfedge[faceOrders[i]];
			final HE_Halfedge[] he2 = new HE_Halfedge[faceOrders[i]];
			final int fo = face.getFaceDegree() / 2;
			final HE_TextureCoordinate[] textures = new HE_TextureCoordinate[fo];
			int c = 0;
			do {
				textures[c++] = he.hasUVW() ? he.getUVW() : null;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			c = 0;
			do {
				final HE_Face f = new HE_Face();
				selection.getParent().addDerivedElement(f, face);
				f.copyProperties(face);
				selection.add(f);
				he0[c] = he;
				selection.getParent().setFace(he, f);
				selection.getParent().setHalfedge(f, he);
				he1[c] = he.getNextInFace();
				he2[c] = new HE_Halfedge();
				hec[c] = new HE_Halfedge();
				selection.getParent().setVertex(hec[c], he.getVertex());
				if (textures[c] != null) {
					hec[c].setUVW(textures[c]);
				}
				selection.getParent().setPair(hec[c], he2[c]);
				selection.getParent().setFace(hec[c], face);
				selection.getParent().setVertex(he2[c], he.getNextInFace().getNextInFace().getVertex());
				if (textures[(c + 1) % fo] != null) {
					he2[c].setUVW(textures[(c + 1) % fo]);
				}
				selection.getParent().setNext(he2[c], he0[c]);
				selection.getParent().setFace(he1[c], f);
				selection.getParent().setFace(he2[c], f);
				selection.getParent().addDerivedElement(he2[c], face);
				selection.getParent().addDerivedElement(hec[c], face);
				c++;
				he = he.getNextInFace().getNextInFace();
			} while (he != startHE);
			selection.getParent().setHalfedge(face, hec[0]);
			for (int j = 0; j < c; j++) {
				selection.getParent().setNext(he1[j], he2[j]);
				selection.getParent().setNext(hec[j], hec[(j + 1) % c]);
			}
			selection.getParent().deleteFace(face);
		}
		return selectionOut;
	}

	/**
	 * Quad split faces.
	 *
	 * @param mesh
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesQuad(final HE_Mesh mesh) {
		final HES_QuadSplit qs = new HES_QuadSplit();
		mesh.subdivide(qs);
		return qs.getSplitFaces();
	}

	/**
	 * Quad split faces.
	 *
	 * @param mesh
	 * @param d
	 *
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesQuad(final HE_Mesh mesh, final double d) {
		final HES_QuadSplit qs = new HES_QuadSplit().setOffset(d);
		mesh.subdivide(qs);
		return qs.getSplitFaces();
	}

	/**
	 * Quad split selected faces.
	 *
	 * @param sel selection to split
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesQuad(final HE_Selection sel) {
		final HES_QuadSplit qs = new HES_QuadSplit();
		sel.subdivide(qs);
		return qs.getSplitFaces();
	}

	/**
	 * Quad split selected faces.
	 *
	 * @param sel selection to split
	 * @param d
	 * @return selection of new faces and new vertices
	 */
	public static HE_Selection splitFacesQuad(final HE_Selection sel, final double d) {
		final HES_QuadSplit qs = new HES_QuadSplit().setOffset(d);
		sel.subdivide(qs);
		return qs.getSplitFaces();
	}

	/**
	 * Tri split faces.
	 *
	 * @param mesh
	 * @return selection of new faces and new vertex
	 */
	public static HE_Selection splitFacesTri(final HE_Mesh mesh) {
		final HEM_TriSplit ts = new HEM_TriSplit();
		mesh.modify(ts);
		return ts.getSplitFaces();
	}

	/**
	 * Tri split faces with offset along face normal.
	 *
	 * @param mesh
	 * @param d    offset along face normal
	 *
	 * @return selection of new faces and new vertex
	 */
	public static HE_Selection splitFacesTri(final HE_Mesh mesh, final double d) {
		final HEM_TriSplit ts = new HEM_TriSplit().setOffset(d);
		mesh.modify(ts);
		return ts.getSplitFaces();
	}

	/**
	 * Tri split faces.
	 *
	 * @param selection face selection to split
	 * @return selection of new faces and new vertex
	 */
	public static HE_Selection splitFacesTri(final HE_Selection selection) {
		final HEM_TriSplit ts = new HEM_TriSplit();
		selection.modify(ts);
		return ts.getSplitFaces();
	}

	/**
	 * Tri split faces with offset along face normal.
	 *
	 * @param selection face selection to split
	 * @param d         offset along face normal
	 * @return selection of new faces and new vertex
	 */
	public static HE_Selection splitFacesTri(final HE_Selection selection, final double d) {
		final HEM_TriSplit ts = new HEM_TriSplit().setOffset(d);
		selection.modify(ts);
		return ts.getSplitFaces();
	}

	/**
	 * Triangulate all faces.
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection triangulate(final HE_Mesh mesh) {
		final HEM_TriangulateMT tri = new HEM_TriangulateMT();
		mesh.modify(tri);
		return tri.triangles;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param face
	 * @return
	 */
	public static HE_Selection triangulate(final HE_Mesh mesh, final HE_Face face) {
		final HE_Selection sel = HE_Selection.getSelection(mesh);
		sel.add(face);
		return triangulate(sel);
	}

	/**
	 * Triangulate.
	 *
	 * @param sel selection
	 * @return
	 */
	public static HE_Selection triangulate(final HE_Selection sel) {
		final HEM_TriangulateMT tri = new HEM_TriangulateMT();
		sel.modify(tri);
		return tri.triangles;
	}

	/**
	 * Triangulate face.
	 *
	 * @param mesh
	 * @param key  key of face
	 *
	 * @return
	 */
	public static HE_Selection triangulate(final HE_Mesh mesh, final long key) {
		return triangulate(mesh, mesh.getFaceWithKey(key));
	}

	/**
	 * Triangulate face if concave.
	 *
	 * @param mesh
	 * @param face key of face
	 *
	 * @return
	 */
	public static HE_Selection triangulateConcaveFace(final HE_Mesh mesh, final HE_Face face) {
		if (getFaceType(face) == WB_Classification.CONCAVE) {
			return triangulate(mesh, face);
		}
		final HE_Selection sel = HE_Selection.getSelection(mesh);
		sel.add(face);
		return sel;
	}

	/**
	 * Triangulate face if concave.
	 *
	 * @param mesh
	 * @param key  key of face
	 *
	 * @return
	 */
	public static HE_Selection triangulateConcaveFace(final HE_Mesh mesh, final long key) {
		return triangulateConcaveFace(mesh, mesh.getFaceWithKey(key));
	}

	/**
	 * Triangulate all concave faces.
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Selection triangulateConcaveFaces(final HE_Mesh mesh) {
		final HE_Selection out = HE_Selection.getSelection(mesh);
		final HE_Face[] f = mesh.getFacesAsArray();
		final int n = mesh.getNumberOfFaces();
		for (int i = 0; i < n; i++) {
			if (getFaceType(f[i]) == WB_Classification.CONCAVE) {
				out.union(triangulate(mesh, f[i].getKey()));
			}
		}
		return out;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param sel
	 * @return
	 */
	public static HE_Selection triangulateConcaveFaces(final HE_Mesh mesh, final List<HE_Face> sel) {
		final HE_Selection out = HE_Selection.getSelection(mesh);
		final int n = sel.size();
		for (int i = 0; i < n; i++) {
			if (getFaceType(sel.get(i)) == WB_Classification.CONCAVE) {
				out.union(triangulate(mesh, sel.get(i).getKey()));
			} else {
				out.add(sel.get(i));
			}
		}
		return out;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param v
	 * @return
	 */
	public static HE_Selection triangulateFaceStar(final HE_Mesh mesh, final HE_Vertex v) {
		final HE_Selection vf = HE_Selection.getSelection(mesh);
		final HE_VertexFaceCirculator vfc = new HE_VertexFaceCirculator(v);
		HE_Face f;
		while (vfc.hasNext()) {
			f = vfc.next();
			if (f != null) {
				if (f.getFaceDegree() > 3) {
					if (!vf.contains(f)) {
						vf.add(f);
					}
				}
			}
		}
		return triangulate(vf);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param vertexkey
	 * @return
	 */
	public static HE_Selection triangulateFaceStar(final HE_Mesh mesh, final long vertexkey) {
		final HE_Selection vf = HE_Selection.getSelection(mesh);
		final HE_VertexFaceCirculator vfc = new HE_VertexFaceCirculator(mesh.getVertexWithKey(vertexkey));
		HE_Face f;
		while (vfc.hasNext()) {
			f = vfc.next();
			if (f != null) {
				if (f.getFaceDegree() > 3) {
					if (!vf.contains(f)) {
						vf.add(f);
					}
				}
			}
		}
		return triangulate(vf);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param he
	 * @return
	 */
	public static HE_Face createFaceFromHalfedgeLoop(final HE_Mesh mesh, final HE_Halfedge he) {
		if (mesh == null || he == null) {
			return null;
		}
		if (he.getFace() != null) {
			return null;
		}
		if (!mesh.contains(he)) {
			return null;
		}
		HE_Halfedge hen = he;
		final HE_Face newFace = new HE_Face();
		mesh.setHalfedge(newFace, he);
		do {
			mesh.setFace(hen, newFace);
			hen = hen.getNextInFace();
		} while (hen != he);
		mesh.add(newFace);
		return newFace;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param hes
	 * @return
	 */
	public static List<HE_Face> createFaceFromHalfedgeLoop(final HE_Mesh mesh, final List<HE_Halfedge> hes) {
		final List<HE_Face> newFaces = new ArrayList<HE_Face>();
		if (mesh == null || hes == null) {
			return newFaces;
		}
		for (final HE_Halfedge he : hes) {
			he.clearVisited();
		}
		for (final HE_Halfedge he : hes) {
			if (he.getFace() != null) {
				continue;
			}
			if (!mesh.contains(he)) {
				continue;
			}
			HE_Halfedge hen = he;
			final HE_Face newFace = new HE_Face();
			mesh.setHalfedge(newFace, he);
			do {
				mesh.setFace(hen, newFace);
				if (hes.contains(hen)) {
					hen.setVisited();
				}
				hen = hen.getNextInFace();
			} while (hen != he);
			mesh.add(newFace);
			newFaces.add(newFace);
		}
		return newFaces;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param unpairedHalfedge
	 * @return
	 */
	public static HE_RAS<HE_Face> selectAllFacesConnectedToUnpairedHalfedge(final HE_Mesh mesh,
			final HE_Halfedge unpairedHalfedge) {
		final HE_RAS<HE_Face> faces = new HE_RAS<HE_Face>();
		HE_Face face = unpairedHalfedge.getFace();
		if (face == null) {
			return faces;
		}
		final HE_FaceIterator fitr = mesh.fItr();
		while (fitr.hasNext()) {
			fitr.next().clearVisited();
		}
		final HE_RAS<HE_Face> facesToCheck = new HE_RAS<HE_Face>();
		facesToCheck.add(face);
		face.setVisited();
		HE_Halfedge he;
		HE_Face neighbor;
		do {
			face = facesToCheck.getWithIndex(0);
			facesToCheck.remove(face);
			faces.add(face);
			final HE_FaceHalfedgeInnerCirculator heitr = new HE_FaceHalfedgeInnerCirculator(face);
			while (heitr.hasNext()) {
				he = heitr.next();
				if (he.getPair() != null) {
					neighbor = he.getPair().getFace();
					if (neighbor != null && !neighbor.isVisited()) {
						facesToCheck.add(neighbor);
						neighbor.setVisited();
					}
				}
			}
		} while (facesToCheck.size() > 0);
		return faces;
	}

	/**
	 * Reverse all faces. Flips normals.
	 *
	 * @param mesh
	 * @return
	 */
	public static HE_Mesh flipFaces(final HE_Mesh mesh) {
		mesh.modify(new HEM_FlipFaces());
		return mesh;
	}

	public static HE_Mesh flipFaces(final HE_Selection sel) {
		sel.modify(new HEM_FlipFaces());
		return sel.getParent();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param he
	 * @return
	 */
	public static boolean flipEdge(final HE_Mesh mesh, final HE_Halfedge he) {
		// boundary edge
		if (he.getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getFace().getFaceDegree() != 3) {
			return false;
		}
		// unpaired edge
		if (he.getPair() == null) {
			return false;
		}
		// boundary edge
		if (he.getPair().getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getPair().getFace().getFaceDegree() != 3) {
			return false;
		}
		// not planar
		if (Math.PI - getEdgeDihedralAngle(he) > WB_Epsilon.EPSILON) {
			return false;
		}
		// flip would result in overlapping triangles, this detected by
		// comparing the areas of the two triangles before and after.
		WB_Plane P = new WB_Plane(getHalfedgeCenter(he), getEdgeNormal(he));
		final WB_Coord a = WB_GeometryOp.projectOnPlane(he.getVertex(), P);
		final WB_Coord b = WB_GeometryOp.projectOnPlane(he.getNextInFace().getVertex(), P);
		final WB_Coord c = WB_GeometryOp.projectOnPlane(he.getNextInFace().getNextInFace().getVertex(), P);
		final WB_Coord d = WB_GeometryOp.projectOnPlane(he.getPair().getNextInFace().getNextInFace().getVertex(), P);
		double Ai = WB_GeometryOp.getArea(a, b, c);
		Ai += WB_GeometryOp.getArea(a, d, b);
		double Af = WB_GeometryOp.getArea(a, d, c);
		Af += WB_GeometryOp.getArea(c, d, b);
		final double ratio = Ai / Af;
		if (ratio > 1.000001 || ratio < 0.99999) {
			return false;
		}
		// get the 3 edges of triangle t1 and t2, he1t1 and he1t2 is the edge to
		// be flipped
		final HE_Halfedge he1t1 = he;
		final HE_Halfedge he1t2 = he.getPair();
		final HE_Halfedge he2t1 = he1t1.getNextInFace();
		final HE_Halfedge he2t2 = he1t2.getNextInFace();
		final HE_Halfedge he3t1 = he2t1.getNextInFace();
		final HE_Halfedge he3t2 = he2t2.getNextInFace();
		final HE_Face t1 = he1t1.getFace();
		final HE_Face t2 = he1t2.getFace();
		// Fix vertex assignment
		// First make sure the original vertices get assigned another halfedge
		mesh.setHalfedge(he1t1.getVertex(), he2t2);
		mesh.setHalfedge(he1t2.getVertex(), he2t1);
		// Now assign the new vertices to the flipped edges
		mesh.setVertex(he1t1, he3t1.getVertex());
		mesh.setVertex(he1t2, he3t2.getVertex());
		// Reconstruct triangle t1
		mesh.setNext(he2t1, he1t1);
		mesh.setNext(he1t1, he3t2);
		mesh.setNext(he3t2, he2t1);
		mesh.setFace(he3t2, t1);
		mesh.setHalfedge(t1, he1t1);
		// reconstruct triangle t2
		mesh.setNext(he2t2, he1t2);
		mesh.setNext(he1t2, he3t1);
		mesh.setNext(he3t1, he2t2);
		mesh.setFace(he3t1, t2);
		mesh.setHalfedge(t2, he1t2);
		return true;
	}

	public static boolean flipEdgeConditional(final HE_Mesh mesh, final HE_Halfedge he) {
		// boundary edge
		if (he.getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getFace().getFaceDegree() != 3) {
			return false;
		}
		// unpaired edge
		if (he.getPair() == null) {
			return false;
		}
		// boundary edge
		if (he.getPair().getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getPair().getFace().getFaceDegree() != 3) {
			return false;
		}
		// flip would result in overlapping triangles, this detected by
		// comparing the areas of the two triangles before and after.
		WB_Plane P = new WB_Plane(getHalfedgeCenter(he), getEdgeNormal(he));
		final WB_Coord a = WB_GeometryOp.projectOnPlane(he.getVertex(), P);
		final WB_Coord b = WB_GeometryOp.projectOnPlane(he.getNextInFace().getVertex(), P);
		final WB_Coord c = WB_GeometryOp.projectOnPlane(he.getNextInFace().getNextInFace().getVertex(), P);
		final WB_Coord d = WB_GeometryOp.projectOnPlane(he.getPair().getNextInFace().getNextInFace().getVertex(), P);
		double Ai = WB_GeometryOp.getArea(a, b, c);
		Ai += WB_GeometryOp.getArea(a, d, b);
		double Af = WB_GeometryOp.getArea(a, d, c);
		Af += WB_GeometryOp.getArea(c, d, b);
		final double ratio = Ai / Af;
		if (ratio > 1.000001 || ratio < 0.99999) {
			return false;
		}
		// get the 3 edges of triangle t1 and t2, he1t1 and he1t2 is the edge to
		// be flipped
		final HE_Halfedge he1t1 = he;
		final HE_Halfedge he1t2 = he.getPair();
		final HE_Halfedge he2t1 = he1t1.getNextInFace();
		final HE_Halfedge he2t2 = he1t2.getNextInFace();
		final HE_Halfedge he3t1 = he2t1.getNextInFace();
		final HE_Halfedge he3t2 = he2t2.getNextInFace();
		// Don't flip if the new triangle edge is equal or longer than the
		// current one.
		if (getSqLength(he1t1) <= he2t1.getEndVertex().getPosition().getSqDistance(he2t2.getEndVertex())) {
			return false;
		}
		final HE_Face t1 = he1t1.getFace();
		final HE_Face t2 = he1t2.getFace();
		// Fix vertex assignment
		// First make sure the original vertices get assigned another halfedge
		mesh.setHalfedge(he1t1.getVertex(), he2t2);
		mesh.setHalfedge(he1t2.getVertex(), he2t1);
		// Now assign the new vertices to the flipped edges
		mesh.setVertex(he1t1, he3t1.getVertex());
		mesh.setVertex(he1t2, he3t2.getVertex());
		// Reconstruct triangle t1
		mesh.setNext(he2t1, he1t1);
		mesh.setNext(he1t1, he3t2);
		mesh.setNext(he3t2, he2t1);
		mesh.setFace(he3t2, t1);
		mesh.setHalfedge(t1, he1t1);
		// reconstruct triangle t2
		mesh.setNext(he2t2, he1t2);
		mesh.setNext(he1t2, he3t1);
		mesh.setNext(he3t1, he2t2);
		mesh.setFace(he3t1, t2);
		mesh.setHalfedge(t2, he1t2);
		return true;
	}

	public static boolean flipPlanarEdgeConditional(final HE_Mesh mesh, final HE_Halfedge he, final double cosa) {
		// boundary edge
		if (he.getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getFace().getFaceDegree() != 3) {
			return false;
		}
		// unpaired edge
		if (he.getPair() == null) {
			return false;
		}
		// boundary edge
		if (he.getPair().getFace() == null) {
			return false;
		}
		// not a triangle
		if (he.getPair().getFace().getFaceDegree() != 3) {
			return false;
		}
		if (getEdgeCosDihedralAngle(he) > cosa) {
			return false;
		}
		// flip would result in overlapping triangles, this detected by
		// comparing the areas of the two triangles before and after.
		WB_Plane P = new WB_Plane(getHalfedgeCenter(he), getEdgeNormal(he));
		final WB_Coord a = WB_GeometryOp.projectOnPlane(he.getVertex(), P);
		final WB_Coord b = WB_GeometryOp.projectOnPlane(he.getNextInFace().getVertex(), P);
		final WB_Coord c = WB_GeometryOp.projectOnPlane(he.getNextInFace().getNextInFace().getVertex(), P);
		final WB_Coord d = WB_GeometryOp.projectOnPlane(he.getPair().getNextInFace().getNextInFace().getVertex(), P);
		double Ai = WB_GeometryOp.getArea(a, b, c);
		Ai += WB_GeometryOp.getArea(a, d, b);
		double Af = WB_GeometryOp.getArea(a, d, c);
		if (WB_Epsilon.isZero(Af)) {
			return false;
		}
		double Af2 = WB_GeometryOp.getArea(c, d, b);
		if (WB_Epsilon.isZero(Af2)) {
			return false;
		}
		Af += Af2;
		final double ratio = Ai / Af;
		if (ratio > 1.000001 || ratio < 0.99999) {
			return false;
		}
		// get the 3 edges of triangle t1 and t2, he1t1 and he1t2 is the edge to
		// be flipped
		final HE_Halfedge he1t1 = he;
		final HE_Halfedge he1t2 = he.getPair();
		final HE_Halfedge he2t1 = he1t1.getNextInFace();
		final HE_Halfedge he2t2 = he1t2.getNextInFace();
		final HE_Halfedge he3t1 = he2t1.getNextInFace();
		final HE_Halfedge he3t2 = he2t2.getNextInFace();
		// Don't flip if the new triangle edge is equal or longer than the
		// current one.
		if (getSqLength(he1t1) <= he2t1.getEndVertex().getPosition().getSqDistance(he2t2.getEndVertex())) {
			return false;
		}
		final HE_Face t1 = he1t1.getFace();
		final HE_Face t2 = he1t2.getFace();
		// Fix vertex assignment
		// First make sure the original vertices get assigned another halfedge
		mesh.setHalfedge(he1t1.getVertex(), he2t2);
		mesh.setHalfedge(he1t2.getVertex(), he2t1);
		// Now assign the new vertices to the flipped edges
		mesh.setVertex(he1t1, he3t1.getVertex());
		mesh.setVertex(he1t2, he3t2.getVertex());
		// Reconstruct triangle t1
		mesh.setNext(he2t1, he1t1);
		mesh.setNext(he1t1, he3t2);
		mesh.setNext(he3t2, he2t1);
		mesh.setFace(he3t2, t1);
		mesh.setHalfedge(t1, he1t1);
		// reconstruct triangle t2
		mesh.setNext(he2t2, he1t2);
		mesh.setNext(he1t2, he3t1);
		mesh.setNext(he3t1, he2t2);
		mesh.setFace(he3t1, t2);
		mesh.setHalfedge(t2, he1t2);
		return true;
	}

	public static boolean flipPlanarEdgeConditional(final HE_Mesh mesh, final HE_Halfedge he) {
		return flipPlanarEdgeConditional(mesh, he, Math.cos(Math.PI - WB_Epsilon.EPSILONANGLE));
	}

	public static void improveTriangulation(final HE_Mesh mesh, final HE_Selection triangles) {
		improveTriangulation(mesh, triangles, Math.cos(Math.PI - WB_Epsilon.EPSILONANGLE));
	}

	public static void improveTriangulation(final HE_Mesh mesh) {
		improveTriangulation(mesh, mesh.selectAllFaces(), Math.cos(Math.PI - WB_Epsilon.EPSILONANGLE));
	}

	public static void improveTriangulation(final HE_Mesh mesh, final double cosa) {
		improveTriangulation(mesh, mesh.selectAllFaces(), cosa);
	}

	public static void improveTriangulation(final HE_Mesh mesh, final HE_Selection triangles, final double cosa) {
		if (mesh != triangles.getParent()) {
			return;
		}
		HE_Selection sel = HE_Selection.getSelection(mesh);
		sel.addFaces(triangles.getFaces());
		int flip;
		do {
			sel.clearEdges();
			sel.collectEdgesByFace();
			List<HE_Halfedge> check = sel.getInnerEdges();
			flip = 0;
			boolean flipped;
			for (HE_Halfedge e : check) {
				flipped = HE_MeshOp.flipPlanarEdgeConditional(mesh, e, cosa);
				if (flipped) {
					flip++;
				}
			}
		} while (flip > 0);
	}

	/**
	 * Clean all mesh elements not used by any faces. Will leave boundary halfedges
	 * uncapped!
	 *
	 * @param mesh
	 * @return self
	 * @deprecated Use {@link #removeUnconnectedElements(HE_Mesh)} instead
	 */
	public static HE_Mesh cleanUnusedElementsByFace(final HE_Mesh mesh) {
		return removeUnconnectedElements(mesh);
	}

	/**
	 * Clean all mesh elements not used by any faces. Will leave boundary halfedges
	 * uncapped!
	 *
	 * @param mesh
	 * @return self
	 */
	public static HE_Mesh removeUnconnectedElements(final HE_Mesh mesh) {
		final HE_RAS<HE_Vertex> cleanedVertices = new HE_RAS<HE_Vertex>();
		final HE_RAS<HE_Halfedge> cleanedHalfedges = new HE_RAS<HE_Halfedge>();
		tracker.setStartStatusStr("HET_MeshOp", "Cleaning unused elements.");
		HE_Halfedge he;
		WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfFaces(), 10);
		tracker.setCounterStatusStr("HET_MeshOp", "Processing faces.", counter);
		HE_Face f;
		final Iterator<HE_Face> fItr = mesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			he = f.getHalfedge();
			do {
				if (!cleanedVertices.contains(he.getVertex())) {
					cleanedVertices.add(he.getVertex());
					mesh.setHalfedge(he.getVertex(), he);
				}
				if (!cleanedHalfedges.contains(he)) {
					cleanedHalfedges.add(he);
				}
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			counter.increment();
		}
		counter = new WB_ProgressCounter(cleanedHalfedges.size(), 10);
		tracker.setCounterStatusStr("HET_MeshOp", "Processing halfedges.", counter);
		final int n = cleanedHalfedges.size();
		for (int i = 0; i < n; i++) {
			he = cleanedHalfedges.getWithIndex(i);
			if (!cleanedHalfedges.contains(he.getPair())) {
				mesh.clearPair(he);
				mesh.setHalfedge(he.getVertex(), he);
			}
			counter.increment();
		}
		List<HE_Vertex> removev = new FastList<HE_Vertex>();
		for (HE_Vertex v : mesh.getVertices()) {
			if (!cleanedVertices.contains(v)) {
				removev.add(v);
			}
		}
		mesh.removeVertices(removev);
		HE_HalfedgeIterator heItr = mesh.heItr();
		List<HE_Halfedge> remove = new FastList<HE_Halfedge>();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (!cleanedHalfedges.contains(he)) {
				remove.add(he);
			}
		}
		mesh.removeHalfedges(remove);
		tracker.setStopStatusStr("HET_MeshOp", "Done cleaning unused elements.");
		return mesh;
	}

	/**
	 * Assign face to halfedge loop.
	 *
	 * @param mesh
	 * @param face     face
	 * @param halfedge halfedge loop
	 */
	public static void assignFaceToLoop(final HE_Mesh mesh, final HE_Face face, final HE_Halfedge halfedge) {
		HE_Halfedge he = halfedge;
		do {
			mesh.setFace(he, face);
			he = he.getNextInFace();
		} while (he != halfedge);
	}

	/**
	 *
	 *
	 * @param face
	 * @param line
	 * @return
	 */
	public static HE_FaceLineIntersection getIntersection(final HE_Face face, final WB_Line line) {
		final WB_Plane P = getPlane(face);
		HE_FaceLineIntersection p = null;
		final WB_IntersectionResult lpi = WB_GeometryOp.getIntersection3D(line, P);
		if (lpi.intersection) {
			p = new HE_FaceLineIntersection(face, (WB_Point) lpi.object);
			if (WB_Epsilon.isZero(WB_GeometryOp.getDistanceToClosestPoint3D(p.point, getPolygon(face)))) {
				return p;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @param face
	 * @param ray
	 * @return
	 */
	public static HE_FaceLineIntersection getIntersection(final HE_Face face, final WB_Ray ray) {
		final WB_Plane P = getPlane(face);
		HE_FaceLineIntersection p = null;
		final WB_IntersectionResult lpi = WB_GeometryOp.getIntersection3D(ray, P);
		if (lpi.intersection) {
			p = new HE_FaceLineIntersection(face, (WB_Point) lpi.object);
			if (WB_Epsilon.isZero(WB_GeometryOp.getDistanceToClosestPoint3D(p.point, getPolygon(face)))) {
				return new HE_FaceLineIntersection(face, p.point);
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @param face
	 * @param segment
	 * @return
	 */
	public static HE_FaceLineIntersection getIntersection(final HE_Face face, final WB_Segment segment) {
		final WB_Plane P = getPlane(face);
		HE_FaceLineIntersection p = null;
		final WB_IntersectionResult lpi = WB_GeometryOp.getIntersection3D(segment, P);
		if (lpi.intersection) {
			p = new HE_FaceLineIntersection(face, (WB_Point) lpi.object);
			if (WB_Epsilon.isZero(WB_GeometryOp.getDistanceToClosestPoint3D(p.point, getPolygon(face)))) {
				return p;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @param e
	 * @param P
	 * @return
	 */
	public static double getIntersection(final HE_Halfedge e, final WB_Plane P) {
		final WB_IntersectionResult i = WB_GeometryOp.getIntersection3D(e.getStartVertex(), e.getEndVertex(), P);
		if (i.intersection == false) {
			return -1.0;// intersection beyond endpoints
		}
		return i.t1;// intersection on edge
	}

	/**
	 *
	 *
	 * @param tree
	 * @param ray
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final WB_AABBTree3D tree, final WB_Ray ray) {
		final List<HE_FaceLineIntersection> p = new FastList<HE_FaceLineIntersection>();
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(ray, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, ray);
			if (sect != null) {
				p.add(sect);
			}
		}
		return p;
	}

	public static List<HE_FaceLineIntersection> getIntersectionNoOrigin(final WB_AABBTree3D tree, final WB_Ray ray) {
		final List<HE_FaceLineIntersection> p = new FastList<HE_FaceLineIntersection>();
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(ray, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, ray);
			if (sect != null && !WB_Epsilon.isZeroSq(WB_Point.getSqDistance3D(ray.getOrigin(), sect.point))) {
				p.add(sect);
			}
		}
		return p;
	}


	public static boolean isInside(final HE_Mesh mesh, final WB_Coord p) {
		return isInside(new WB_AABBTree3D(mesh, 1), p);
	}

	public static boolean isInside(final WB_AABBTree3D tree, final WB_Coord p) {
		final List<HE_FaceLineIntersection> ints = new FastList<HE_FaceLineIntersection>();
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final WB_Vector dir = new WB_Vector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
		final WB_Ray R = new WB_Ray(p, dir);
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(R, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, R);
			if (sect != null) {
				ints.add(sect);
			}
		}
		return ints.size() % 2 == 1;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param segment
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final WB_AABBTree3D tree, final WB_Segment segment) {
		final List<HE_FaceLineIntersection> p = new FastList<HE_FaceLineIntersection>();
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(segment, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, segment);
			if (sect != null) {
				p.add(sect);
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param line
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final WB_AABBTree3D tree, final WB_Line line) {
		final List<HE_FaceLineIntersection> p = new FastList<HE_FaceLineIntersection>();
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(line, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, line);
			if (sect != null) {
				p.add(sect);
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param P
	 * @return
	 */
	public static List<WB_Segment> getIntersection(final WB_AABBTree3D tree, final WB_Plane P) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(P, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		final List<WB_Segment> cuts = new FastList<WB_Segment>();
		for (final HE_Face face : candidates) {
			cuts.addAll(WB_GeometryOp.getIntersection3D(getPolygon(face), P));
		}
		return cuts;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param P
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Plane P) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(P, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param T
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Triangle T) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(T, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_AABB AABB) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(AABB, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Coord p) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(p, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param R
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Ray R) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(R, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param L
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Line L) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(L, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param segment
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final WB_AABBTree3D tree, final WB_Segment segment) {
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(segment, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		return candidates;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param ray
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final WB_AABBTree3D tree, final WB_Ray ray) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(ray, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2min = Double.POSITIVE_INFINITY;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, ray);
			if (sect != null) {
				d2 = sect.point.getSqDistance(ray.getOrigin());
				if (d2 < d2min) {
					p = sect;
					d2min = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param ray
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final WB_AABBTree3D tree, final WB_Ray ray) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(ray, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2max = -1;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, ray);
			if (sect != null) {
				d2 = sect.point.getSqDistance(ray.getOrigin());
				if (d2 > d2max) {
					p = sect;
					d2max = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param line
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final WB_AABBTree3D tree, final WB_Line line) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(line, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2min = Double.POSITIVE_INFINITY;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, line);
			if (sect != null) {
				d2 = sect.point.getSqDistance(line.getOrigin());
				if (d2 < d2min) {
					p = sect;
					d2min = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param line
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final WB_AABBTree3D tree, final WB_Line line) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(line, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2max = -1;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, line);
			if (sect != null) {
				d2 = sect.point.getSqDistance(line.getOrigin());
				if (d2 > d2max) {
					p = sect;
					d2max = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param segment
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final WB_AABBTree3D tree, final WB_Segment segment) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(segment, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2min = Double.POSITIVE_INFINITY;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, segment);
			if (sect != null) {
				d2 = sect.point.getSqDistance(segment.getOrigin());
				if (d2 < d2min) {
					p = sect;
					d2min = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param tree
	 * @param segment
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final WB_AABBTree3D tree, final WB_Segment segment) {
		HE_FaceLineIntersection p = null;
		final List<HE_Face> candidates = new FastList<HE_Face>();
		final List<WB_AABBNode3D> nodes = WB_GeometryOp.getIntersection3D(segment, tree);
		for (final WB_AABBNode3D n : nodes) {
			candidates.addAll(n.getFaces());
		}
		double d2, d2max = -1;
		for (final HE_Face face : candidates) {
			final HE_FaceLineIntersection sect = getIntersection(face, segment);
			if (sect != null) {
				d2 = sect.point.getSqDistance(segment.getOrigin());
				if (d2 > d2max) {
					p = sect;
					d2max = d2;
				}
			}
		}
		return p;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param ray
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final HE_Mesh mesh, final WB_Ray ray) {
		return getIntersection(new WB_AABBTree3D(mesh, 10), ray);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param segment
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final HE_Mesh mesh, final WB_Segment segment) {
		return getIntersection(new WB_AABBTree3D(mesh, 10), segment);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param line
	 * @return
	 */
	public static List<HE_FaceLineIntersection> getIntersection(final HE_Mesh mesh, final WB_Line line) {
		return getIntersection(new WB_AABBTree3D(mesh, 10), line);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static List<WB_Segment> getIntersection(final HE_Mesh mesh, final WB_Plane P) {
		return getIntersection(new WB_AABBTree3D(mesh, 10), P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param P
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final HE_Mesh mesh, final WB_Plane P) {
		return getPotentialIntersectedFaces(new WB_AABBTree3D(mesh, 10), P);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param R
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final HE_Mesh mesh, final WB_Ray R) {
		return getPotentialIntersectedFaces(new WB_AABBTree3D(mesh, 10), R);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param L
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final HE_Mesh mesh, final WB_Line L) {
		return getPotentialIntersectedFaces(new WB_AABBTree3D(mesh, 10), L);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param segment
	 * @return
	 */
	public static List<HE_Face> getPotentialIntersectedFaces(final HE_Mesh mesh, final WB_Segment segment) {
		return getPotentialIntersectedFaces(new WB_AABBTree3D(mesh, 10), segment);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param ray
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final HE_Mesh mesh, final WB_Ray ray) {
		return getClosestIntersection(new WB_AABBTree3D(mesh, 10), ray);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param ray
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final HE_Mesh mesh, final WB_Ray ray) {
		return getFurthestIntersection(new WB_AABBTree3D(mesh, 10), ray);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param line
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final HE_Mesh mesh, final WB_Line line) {
		return getClosestIntersection(new WB_AABBTree3D(mesh, 10), line);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param line
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final HE_Mesh mesh, final WB_Line line) {
		return getFurthestIntersection(new WB_AABBTree3D(mesh, 10), line);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param segment
	 * @return
	 */
	public static HE_FaceLineIntersection getClosestIntersection(final HE_Mesh mesh, final WB_Segment segment) {
		return getClosestIntersection(new WB_AABBTree3D(mesh, 10), segment);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param segment
	 * @return
	 */
	public static HE_FaceLineIntersection getFurthestIntersection(final HE_Mesh mesh, final WB_Segment segment) {
		return getFurthestIntersection(new WB_AABBTree3D(mesh, 10), segment);
	}

	public static WB_Classification classifyFaceToPlane3D(final HE_Face f, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		HE_Halfedge he = f.getHalfedge();
		do {
			switch (WB_GeometryOp.classifyPointToPlane3D(P, he.getVertex())) {
			case FRONT:
				numInFront++;
				break;
			case BACK:
				numBehind++;
				break;
			default:
			}
			if (numBehind != 0 && numInFront != 0) {
				return WB_Classification.CROSSING;
			}
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	public static WB_Classification classifyEdgeToPlane3D(final HE_Halfedge edge, final WB_Plane P) {
		int numInFront = 0;
		int numBehind = 0;
		switch (WB_GeometryOp.classifyPointToPlane3D(edge.getStartVertex(), P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		switch (WB_GeometryOp.classifyPointToPlane3D(edge.getEndVertex(), P)) {
		case FRONT:
			numInFront++;
			break;
		case BACK:
			numBehind++;
			break;
		default:
		}
		if (numBehind != 0 && numInFront != 0) {
			return WB_Classification.CROSSING;
		}
		if (numInFront != 0) {
			return WB_Classification.FRONT;
		}
		if (numBehind != 0) {
			return WB_Classification.BACK;
		}
		return WB_Classification.ON;
	}

	public static WB_Classification classifyVertexToPlane3D(final HE_Vertex v, final WB_Plane P) {
		return WB_GeometryOp.classifyPointToPlane3D(v, P);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public static WB_Point getNormalOffsetPosition(HE_Vertex v, final double d) {
		return WB_Point.addMul(v.getPosition(), d, getVertexNormal(v));
	}

	public static WB_Classification getVertexType(final HE_Vertex vertex) {
		HE_Halfedge he = vertex.getHalfedge();
		if (he == null) {
			return WB_Classification.UNKNOWN;
		}
		int nconcave = 0;
		int nconvex = 0;
		int nflat = 0;
		do {
			HE_Face f = he.getFace();
			if (f == null) {
				f = he.getPair().getFace();
			}
			final WB_Point v = new WB_Point(he.getNextInFace().getVertex());
			v.subSelf(he.getVertex());
			he = he.getNextInVertex();
			HE_Face fn = he.getFace();
			if (fn == null) {
				fn = he.getPair().getFace();
			}
			final WB_Vector c = WB_Vector.cross(getFaceNormal(f), getFaceNormal(fn));
			final double d = v.dot(c);
			if (Math.abs(d) < WB_Epsilon.EPSILON) {
				nflat++;
			} else if (d < 0) {
				nconcave++;
			} else {
				nconvex++;
			}
		} while (he != vertex.getHalfedge());
		if (nconcave > 0) {
			if (nconvex > 0) {
				return WB_Classification.SADDLE;
			} else {
				if (nflat > 0) {
					return WB_Classification.FLATCONCAVE;
				} else {
					return WB_Classification.CONCAVE;
				}
			}
		} else if (nconvex > 0) {
			if (nflat > 0) {
				return WB_Classification.FLATCONVEX;
			} else {
				return WB_Classification.CONVEX;
			}
		}
		return WB_Classification.FLAT;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_CoordinateSystem getVertexCS(final HE_Vertex v) {
		WB_Coord vn = getVertexNormal(v);
		final WB_Vector normal = vn == null ? null : new WB_Vector(getVertexNormal(v));
		if (normal == null) {
			return null;
		}
		WB_Vector t2 = new WB_Vector();
		if (Math.abs(normal.xd()) < Math.abs(normal.yd())) {
			t2.setX(1.0);
		} else {
			t2.setY(1.0);
		}
		final WB_Vector t1 = normal.cross(t2);
		final double n = t1.getLength();
		if (n < WB_Epsilon.EPSILON) {
			return null;
		}
		t1.mulSelf(1.0 / n);
		t2 = normal.cross(t1);
		return gf.createCSFromOriginAndXYZ(v, t1, t2, normal);
	}

	// Common area-weighted mean normal
	/**
	 *
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormal(final HE_Vertex v) {
		if (v.getHalfedge() == null) {
			return null;
		}
		return getVertexNormalAngle(v);
	}

	/**
	 * Computes the normal at a vertex using the "equally weighted" method.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalAverage(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		do {
			if (he.getFace() != null) {
				n.addSelf(getFaceNormal(he.getFace()));
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
	}

	/**
	 * Computes the normal at a vertex using the "face area weights" method.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalArea(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		do {
			if (he.getFace() != null) {
				n.addMulSelf(getFaceArea(he.getFace()), getFaceNormal(he.getFace()));
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
		// WB_Vector normal = new WB_Vector();
		// final WB_Vector[] temp = new WB_Vector[3];
		// for (int i = 0; i < 3; i++) {
		// temp[i] = new WB_Vector();
		// }
		// HE_Halfedge he = v.getHalfedge();
		// final HE_Vertex d = he.getEndVertex();
		// do {
		// he = he.getNextInVertex();
		// if (he.getFace() == null) {
		// continue;
		// }
		// final double area = computeNormal3D(v, he.getEndVertex(),
		// he.getPrevInFace().getVertex(), temp[0], temp[1],
		// temp[2]);
		// normal.addMulSelf(area, temp[2]);
		// } while (he.getEndVertex() != d);
		// final double n = normal.getLength();
		// normal.mulSelf(1.0 / n);
		// return normal;
	}

	/**
	 * Computes the normal at a vertex using the "tip angle weights" method.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalAngle(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		do {
			if (he.getFace() != null) {
				n.addMulSelf(getAngle(he), getFaceNormal(he.getFace()));
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
	}

	/**
	 * Computes the normal at a vertex using the "gauss curvature" method.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalGaussianCurvature(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		do {
			double weight = 0.5 * getHalfedgeDihedralAngle(he) / getLength(he);
			n.addMulSelf(-weight, getHalfedgeVector(he));
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
	}

	/**
	 * Computes the normal at a vertex using the "mean curvature" method. Triangles
	 * only.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalMeanCurvature(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		do {
			double weight = 0.5 * (getCotan(he) + getCotan(he.getPair()));
			n.addMulSelf(-weight, getHalfedgeVector(he));
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
	}

	/**
	 * Computes the normal at a vertex using the "inscribed sphere" method.
	 * Triangles only.
	 *
	 * @return
	 */
	public static WB_Vector getVertexNormalSphereInscribed(final HE_Vertex v) {
		HE_Halfedge he = v.getHalfedge();
		WB_Vector n = new WB_Vector();
		WB_Vector u, w;
		do {
			u = new WB_Vector(getHalfedgeVector(he));
			w = new WB_Vector(getHalfedgeVector(he.getPrevInFace())).mulSelf(-1);
			n.addMulSelf(1.0 / (u.getSqLength() * w.getSqLength()), u.cross(w));
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		n.normalizeSelf();
		return n;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static double getUmbrellaAngle(final HE_Vertex v) {
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		if (he == null) {
			return 0;
		}
		do {
			result += getAngle(he);
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return result;
	}

	/**
	 * Computes the angle defect at a vertex (= 2PI minus the sum of incident angles
	 * at an interior vertex or PI minus the sum of incident angles at a boundary
	 * vertex).
	 *
	 * @return
	 */
	public static double getAngleDefect(final HE_Vertex v) {
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		if (he == null) {
			return 0;
		}
		do {
			if (he.getFace() != null) {
				result += getAngle(he);
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return v.isBoundary() ? Math.PI - result : 2 * Math.PI - result;
	}

	public static double getAngleDefect(final HE_Mesh mesh) {
		double sum = 0;
		HE_VertexIterator vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			sum += HE_MeshOp.getAngleDefect(v);
		}
		return sum;
	}

	/**
	 * Computes the (integrated) scalar gauss curvature at a vertex.
	 *
	 * @param v
	 * @return
	 */
	public static double getScalarGaussianCurvature(final HE_Vertex v) {
		return getAngleDefect(v);
	}

	/**
	 * Computes the (integrated) scalar mean curvature at a vertex.
	 *
	 * @param v
	 * @return
	 */
	public static double getScalarMeanCurvature(final HE_Vertex v) {
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		if (he == null) {
			return 0;
		}
		do {
			result += 0.5 * getLength(he) * getHalfedgeDihedralAngle(he);
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return result;
	}

	public static double getVertexArea(final HE_Vertex v) {
		if (v.getHalfedge() == null) {
			return 0;
		}
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		do {
			if (he.getFace() != null) {
				result += getFaceArea(he.getFace());
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return result;
	}

	/**
	 * Get the barycentric dual area. Triangles only.
	 *
	 * @return
	 */
	public static double getBarycentricDualVertexArea(final HE_Vertex v) {
		if (v.getHalfedge() == null) {
			return 0;
		}
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		do {
			if (he.getFace() != null) {
				result += getFaceArea(he.getFace()) / 3.0;
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return result;
	}

	/**
	 * Get the circumcentric dual area. Triangles only.
	 *
	 * @return
	 */
	public static double getCircumcentricDualVertexArea(final HE_Vertex v) {
		if (v.getHalfedge() == null) {
			return 0;
		}
		double result = 0;
		HE_Halfedge he = v.getHalfedge();
		HE_Halfedge hep;
		do {
			hep = he.getPrevInFace();
			double u2 = getSqLength(hep);
			double v2 = getSqLength(he);
			double cotAlpha = getCotan(hep);
			double cotBeta = getCotan(he);
			result += 0.125 * (u2 * cotAlpha + v2 * cotBeta);
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		return result;
	}

	/**
	 * Computes the (pointwise) minimum and maximum principal curvature values at a
	 * vertex.
	 *
	 * @param v
	 * @return
	 */
	public static double[] getPrincipalCurvatures(final HE_Vertex v) {
		double A = getCircumcentricDualVertexArea(v);
		double H = getScalarMeanCurvature(v) / A;
		double K = getAngleDefect(v) / A;
		double discriminant = H * H - K;
		if (discriminant > 0) {
			discriminant = Math.sqrt(discriminant);
		} else {
			discriminant = 0;
		}
		double k1 = H - discriminant;
		double k2 = H + discriminant;
		if (Math.abs(k1) > Math.abs(k2)) {
			return new double[] { k2, k1 };
		}
		return new double[] { k1, k2 };
	}

	/**
	 * Returns the discrete Gaussian curvature and the mean normal. These discrete
	 * operators are described in "Discrete Differential-Geometry Operators for
	 * Triangulated 2-Manifolds", Mark Meyer, Mathieu Desbrun, Peter Schr???der, and
	 * Alan H. Barr. http://www.cs.caltech.edu/~mmeyer/Publications/diffGeomOps.pdf
	 * http://www.cs.caltech.edu/~mmeyer/Publications/diffGeomOps.pdf Note: on a
	 * sphere, the Gaussian curvature is very accurate, but not the mean curvature.
	 * Guoliang Xu suggests improvements in his papers
	 * http://lsec.cc.ac.cn/~xuguo/xuguo3.htm
	 *
	 * @param vertex
	 * @param meanCurvatureVector
	 * @return
	 */
	public static double getGaussianCurvature(final HE_Vertex vertex, final WB_Vector meanCurvatureVector) {
		meanCurvatureVector.set(0, 0, 0);
		WB_Vector vect1 = new WB_Vector();
		WB_Vector vect2 = new WB_Vector();
		WB_Vector vect3 = new WB_Vector();
		double mixed = 0.0;
		double gauss = 0.0;
		HE_Halfedge ot = vertex.getHalfedge();
		final HE_Vertex d = ot.getEndVertex();
		do {
			ot = ot.getNextInVertex();
			if (ot.getFace() == null) {
				continue;
			}
			/*
			 * if (ot.getPair().getFace() == null) { meanCurvatureVector.set(0, 0, 0);
			 * return 0.0; }
			 */
			final HE_Vertex p1 = ot.getEndVertex();
			final HE_Vertex p2 = ot.getPrevInFace().getVertex();
			vect1 = new WB_Vector(vertex, p1);
			vect2 = new WB_Vector(p1, p2);
			vect3 = new WB_Vector(p2, vertex);
			final double c12 = vect1.dot(vect2);
			final double c23 = vect2.dot(vect3);
			final double c31 = vect3.dot(vect1);
			// Override vect2
			vect2 = vect1.cross(vect3);
			final double area = 0.5 * vect2.getLength();
			if (c31 > 0.0) {
				mixed += 0.5 * area;
			} else if (c12 > 0.0 || c23 > 0.0) {
				mixed += 0.25 * area;
			} else {
				if (area > 0.0 && area > -WB_Epsilon.EPSILON * (c12 + c23)) {
					mixed -= 0.125 * 0.5 * (c12 * vect3.dot(vect3) + c23 * vect1.dot(vect1)) / area;
				}
			}
			gauss += Math.abs(Math.atan2(2.0 * area, -c31));
			meanCurvatureVector.addMulSelf(0.5 / area, vect3.mulAddMul(c12, -c23, vect1));
		} while (ot.getEndVertex() != d);
		meanCurvatureVector.mulSelf(0.5 / mixed);
		return (2.0 * Math.PI - gauss) / mixed;
	}

	/**
	 * Returns the discrete Gaussian curvature. These discrete operators are
	 * described in "Discrete Differential-Geometry Operators for Triangulated
	 * 2-Manifolds", Mark Meyer, Mathieu Desbrun, Peter Schr?der, and Alan H. Barr.
	 * http://www.cs.caltech.edu/~mmeyer/Publications/diffGeomOps.pdf
	 * http://www.cs.caltech.edu/~mmeyer/Publications/diffGeomOps.pdf Note: on a
	 * sphere, the Gaussian curvature is very accurate, but not the mean curvature.
	 * Guoliang Xu suggests improvements in his papers
	 * http://lsec.cc.ac.cn/~xuguo/xuguo3.htm
	 *
	 *
	 * @return
	 */
	public static double getGaussianCurvature(final HE_Vertex vertex) {
		final WB_Vector meanCurvatureVector = new WB_Vector(0, 0, 0);
		if (vertex.isBoundary()) {
			return 0.0;
		}
		WB_Vector vect1 = new WB_Vector();
		WB_Vector vect2 = new WB_Vector();
		WB_Vector vect3 = new WB_Vector();
		double mixed = 0.0;
		double gauss = 0.0;
		HE_Halfedge ot = vertex.getHalfedge();
		final HE_Vertex d = ot.getEndVertex();
		do {
			ot = ot.getNextInVertex();
			if (ot.getFace() == null) {
				continue;
			}
			/*
			 * if (ot.getPair().getFace() == null) { meanCurvatureVector.set(0, 0, 0);
			 * return 0.0; }
			 */
			final HE_Vertex p1 = ot.getEndVertex();
			final HE_Vertex p2 = ot.getPrevInFace().getVertex();
			vect1 = new WB_Vector(vertex, p1);
			vect2 = new WB_Vector(p1, p2);
			vect3 = new WB_Vector(p2, vertex);
			final double c12 = vect1.dot(vect2);
			final double c23 = vect2.dot(vect3);
			final double c31 = vect3.dot(vect1);
			vect2 = vect1.cross(vect3);
			final double area = 0.5 * vect2.getLength();
			// This angle is obtuse
			if (c31 > 0.0) {
				mixed += 0.5 * area;
				// One of the other angles is obtuse
			} else if (c12 > 0.0 || c23 > 0.0) {
				mixed += 0.25 * area;
			} else {
				if (area > 0.0 && area > -WB_Epsilon.EPSILON * (c12 + c23)) {
					mixed -= 0.125 * 0.5 * (c12 * vect3.dot(vect3) + c23 * vect1.dot(vect1)) / area;
				}
			}
			gauss += Math.abs(Math.atan2(2.0 * area, -c31));
			meanCurvatureVector.addMulSelf(0.5 / area, vect3.mulAddMul(c12, -c23, vect1));
		} while (ot.getEndVertex() != d);
		meanCurvatureVector.mulSelf(0.5 / mixed);
		// Discrete gaussian curvature
		return (2.0 * Math.PI - gauss) / mixed;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_CoordinateSystem getCurvatureDirections(final HE_Vertex v) {
		final WB_CoordinateSystem tangent = getVertexCS(v);
		if (tangent == null) {
			return null;
		}
		final WB_Vector vect1 = findOptimalSolution(v, tangent.getZ(), tangent.getX(), tangent.getY());
		if (vect1 == null) {
			return null;
		}
		double e1, e2;
		if (Math.abs(vect1.yd()) < WB_Epsilon.EPSILON) {
			if (Math.abs(vect1.xd()) < Math.abs(vect1.zd())) {
				e1 = 0.0;
				e2 = 1.0;
			} else {
				e1 = 1.0;
				e2 = 0.0;
			}
		} else {
			e2 = 1.0;
			final double delta = Math
					.sqrt((vect1.xd() - vect1.zd()) * (vect1.xd() - vect1.zd()) + 4.0 * vect1.yd() * vect1.yd());
			double K1;
			if (vect1.xd() + vect1.zd() < 0.0) {
				K1 = 0.5 * (vect1.xd() + vect1.zd() - delta);
			} else {
				K1 = 0.5 * (vect1.xd() + vect1.zd() + delta);
			}
			e1 = (K1 - vect1.xd()) / vect1.yd();
			final double n = Math.sqrt(e1 * e1 + e2 * e2);
			e1 /= n;
			e2 /= n;
		}
		final WB_Vector t1 = tangent.getX();
		final WB_Vector t2 = tangent.getY();
		final WB_Vector X = t1.mulAddMul(e1, e2, t2);
		final WB_Vector Y = t1.mulAddMul(-e2, e1, t2);
		return gf.createCSFromOriginAndXYZ(v, X, Y, tangent.getZ());
	}

	/**
	 *
	 * @param v
	 * @param normal
	 * @param t1
	 * @param t2
	 * @return
	 */
	private static WB_Vector findOptimalSolution(final HE_Vertex v, final WB_Vector normal, final WB_Vector t1,
			final WB_Vector t2) {
		WB_Vector vect1 = new WB_Vector();
		WB_Vector vect2 = new WB_Vector();
		WB_Vector vect3 = new WB_Vector();
		final WB_Vector g0 = new WB_Vector();
		final WB_Vector g1 = new WB_Vector();
		final WB_Vector g2 = new WB_Vector();
		final WB_Vector h = new WB_Vector();
		HE_Halfedge ot = v.getHalfedge();
		final HE_Vertex d = ot.getEndVertex();
		do {
			ot = ot.getNextInVertex();
			if (ot.getFace() == null) {
				continue;
			}
			final WB_Coord p1 = ot.getEndVertex();
			final WB_Coord p2 = ot.getPrevInFace().getVertex();
			vect1 = new WB_Vector(v, p1);
			vect2 = new WB_Vector(p1, p2);
			vect3 = new WB_Vector(p2, v);
			final double c12 = vect1.dot(vect2);
			final double c23 = vect2.dot(vect3);
			// Override vect2
			vect2 = vect1.cross(vect3);
			final double area = 0.5 * vect2.getLength();
			final double len2 = vect1.dot(vect1);
			if (len2 < WB_Epsilon.SQEPSILON) {
				continue;
			}
			final double kappa = 2.0 * vect1.dot(normal) / len2;
			double d1 = vect1.dot(t1);
			double d2 = vect1.dot(t2);
			final double n = Math.sqrt(d1 * d1 + d2 * d2);
			if (n < WB_Epsilon.EPSILON) {
				continue;
			}
			d1 /= n;
			d2 /= n;
			final double omega = 0.5 * (c12 * vect3.dot(vect3) + c23 * vect1.dot(vect1)) / area;
			g0.addSelf(omega * d1 * d1 * d1 * d1, omega * 2.0 * d1 * d1 * d1 * d2, omega * d1 * d1 * d2 * d2);
			g1.addSelf(omega * 4.0 * d1 * d1 * d2 * d2, omega * 2.0 * d1 * d2 * d2 * d2, omega * d2 * d2 * d2 * d2);
			h.addSelf(omega * kappa * d1 * d1, omega * kappa * 2.0 * d1 * d2, omega * kappa * d2 * d2);
		} while (ot.getEndVertex() != d);
		g1.setX(g0.yd());
		g2.setX(g0.zd());
		g2.setY(g1.zd());
		WB_M33 G = new WB_M33(g0.xd(), g1.xd(), g2.xd(), g0.yd(), g1.yd(), g2.yd(), g0.zd(), g1.zd(), g2.zd());
		G = G.inverse();
		if (G == null) {
			return null;
		}
		return WB_M33.mulToPoint(G, h);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Point getFaceCenter(HE_Face f) {
		if (f.getHalfedge() == null) {
			return null;
		}
		HE_Halfedge he = f.getHalfedge();
		WB_Point center = new WB_Point();
		int c = 0;
		do {
			center.addSelf(he.getVertex());
			c++;
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		center.divSelf(c);
		return center;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public static WB_Point getNormalOffsetFaceCenter(HE_Face f, final double d) {
		if (f.getHalfedge() == null) {
			return null;
		}
		HE_Halfedge he = f.getHalfedge();
		WB_Point center = new WB_Point();
		int c = 0;
		do {
			center.addSelf(he.getVertex());
			c++;
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		center.divSelf(c);
		return center.addMul(d, getFaceNormal(f));
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Triangle getTriangle(HE_Face f) {
		if (f.getFaceDegree() != 3) {
			return null;
		}
		return new WB_Triangle(f.getHalfedge().getVertex(), f.getHalfedge().getEndVertex(),
				f.getHalfedge().getNextInFace().getEndVertex());
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Polygon getPolygon(HE_Face f) {
		final int n = f.getFaceDegree();
		if (n == 0) {
			return null;
		}
		final WB_Point[] points = new WB_Point[n];
		int i = 0;
		HE_Halfedge he = f.getHalfedge();
		do {
			points[i++] = new WB_Point(he.getVertex().xd(), he.getVertex().yd(), he.getVertex().zd());
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		return gf.createSimplePolygon(points);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Polygon getOrthoPolygon(HE_Face f) {
		final int n = f.getFaceDegree();
		if (n == 0) {
			return null;
		}
		WB_OrthoProject op = new WB_OrthoProject(getFaceNormal(f));
		final WB_Point[] points = new WB_Point[n];
		int i = 0;
		HE_Halfedge he = f.getHalfedge();
		do {
			points[i] = new WB_Point();
			op.mapPoint3D(he.getVertex(), points[i]);
			i++;
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		return gf.createSimplePolygon(points);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Polygon getPlanarPolygon(HE_Face f) {
		final int n = f.getFaceDegree();
		if (n == 0) {
			return null;
		}
		final WB_Point[] points = new WB_Point[n];
		final WB_Plane P = getPlane(f);
		int i = 0;
		HE_Halfedge he = f.getHalfedge();
		do {
			points[i] = WB_GeometryOp.projectOnPlane(he.getVertex(), P);
			he = he.getNextInFace();
			i++;
		} while (he != f.getHalfedge());
		return gf.createSimplePolygon(points);
	}

	public static WB_Vector getFaceNormal(final HE_Face face) {
		HE_Halfedge he = face.getHalfedge();
		if (he == null) {
			return null;
		}
		// calculate normal with Newell's method
		final WB_Vector _normal = new WB_Vector();
		HE_Vertex p0;
		HE_Vertex p1;
		do {
			p0 = he.getVertex();
			p1 = he.getNextInFace().getVertex();
			_normal.addSelf((p0.yd() - p1.yd()) * (p0.zd() + p1.zd()), (p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
					(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
			he = he.getNextInFace();
		} while (he != face.getHalfedge());
		_normal.normalizeSelf();
		return _normal;
	}

	public static WB_Vector getFaceNormalNotNormalized(final HE_Face face) {
		HE_Halfedge he = face.getHalfedge();
		if (he == null) {
			return null;
		}
		// calculate normal with Newell's method
		final WB_Vector _normal = new WB_Vector();
		HE_Vertex p0;
		HE_Vertex p1;
		do {
			p0 = he.getVertex();
			p1 = he.getNextInFace().getVertex();
			_normal.addSelf((p0.yd() - p1.yd()) * (p0.zd() + p1.zd()), (p0.zd() - p1.zd()) * (p0.xd() + p1.xd()),
					(p0.xd() - p1.xd()) * (p0.yd() + p1.yd()));
			he = he.getNextInFace();
		} while (he != face.getHalfedge());
		return _normal;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Plane getPlane(HE_Face f) {
		WB_Coord fn = getFaceNormal(f);
		if (WB_Vector.getSqLength3D(fn) < 0.5) {
			if (WB_Epsilon.isEqual(f.getHalfedge().getVertex().xd(), f.getHalfedge().getEndVertex().xd())) {
				fn = new WB_Vector(1, 0, 0);
			} else {
				fn = new WB_Vector(0, 0, 1);
			}
		}
		return new WB_Plane(getFaceCenter(f), fn);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public static WB_Plane getNormalOffsetPlane(HE_Face f, final double d) {
		final WB_Coord fn = getFaceNormal(f);
		return new WB_Plane(WB_Point.addMul(getFaceCenter(f), d, fn), fn);
	}

	public static double getFaceArea(final HE_Face face) {
		HE_Halfedge he = face.getHalfedge();
		if (he == null) {
			return 0;
		}
		final WB_Coord n = getFaceNormal(face);
		if (WB_Vector.getLength3D(n) < 0.5) {
			return 0;
		}
		final double x = WB_Math.fastAbs(n.xd());
		final double y = WB_Math.fastAbs(n.yd());
		final double z = WB_Math.fastAbs(n.zd());
		double area = 0;
		int coord = 3;
		if (x >= y && x >= z) {
			coord = 1;
		} else if (y >= x && y >= z) {
			coord = 2;
		}
		do {
			switch (coord) {
			case 1:
				area += he.getVertex().yd()
						* (he.getNextInFace().getVertex().zd() - he.getPrevInFace().getVertex().zd());
				break;
			case 2:
				area += he.getVertex().xd()
						* (he.getNextInFace().getVertex().zd() - he.getPrevInFace().getVertex().zd());
				break;
			case 3:
				area += he.getVertex().xd()
						* (he.getNextInFace().getVertex().yd() - he.getPrevInFace().getVertex().yd());
				break;
			}
			he = he.getNextInFace();
		} while (he != face.getHalfedge());
		switch (coord) {
		case 1:
			area *= 0.5 / x;
			break;
		case 2:
			area *= 0.5 / y;
			break;
		case 3:
			area *= 0.5 / z;
		}
		return WB_Math.fastAbs(area);
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Classification getFaceType(final HE_Face face) {
		HE_Halfedge he = face.getHalfedge();
		if (he == null) {
			return WB_Classification.UNKNOWN;
		}
		do {
			if (getHalfedgeType(he) == WB_Classification.CONCAVE) {
				return WB_Classification.CONCAVE;
			}
			he = he.getNextInFace();
		} while (he != face.getHalfedge());
		return WB_Classification.CONVEX;
	}

	public static WB_CoordinateSystem getFaceCS(HE_Face f) {
		if (f.getHalfedge() == null) {
			return null;
		}
		HE_Halfedge he = f.getHalfedge();
		WB_Vector u = new WB_Vector(getHalfedgeTangent(he));
		WB_Coord n = getFaceNormal(f);
		WB_Vector v = WB_Vector.cross(n, u);
		return new WB_CoordinateSystem(HE_MeshOp.getFaceCenter(f), u, v, n);
	}

	public static boolean collapseHalfedge(final HE_Mesh mesh, final HE_Halfedge he) {
		if (mesh.contains(he)) {
			final HE_Halfedge hePair = he.getPair();
			final HE_Face f = he.getFace();
			final HE_Face fp = hePair.getFace();
			final HE_Vertex v = he.getVertex();
			final HE_Vertex vp = hePair.getVertex();
			final List<HE_Halfedge> tmp = v.getHalfedgeStar();
			final HE_Halfedge hen = he.getNextInFace();
			final HE_Halfedge hep = he.getPrevInFace();
			final HE_Halfedge hePairn = hePair.getNextInFace();
			final HE_Halfedge hePairp = hePair.getPrevInFace();
			if (f != null) {
				mesh.setHalfedge(f, hen);
			}
			if (fp != null) {
				mesh.setHalfedge(fp, hePairn);
			}
			mesh.setNext(hep, hen);
			mesh.setNext(hePairp, hePairn);
			for (int i = 0; i < tmp.size(); i++) {
				mesh.setVertex(tmp.get(i), vp);
			}
			mesh.setHalfedge(vp, hen);
			mesh.remove(he);
			mesh.remove(hePair);
			mesh.remove(v);
			if (f != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, f);
			}
			if (fp != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, fp);
			}
			return true;
		}
		return false;
	}

	/**
	 * Collapse halfedge if its vertex doesn't belong to the boundary
	 *
	 * @param mesh
	 * @param he   he
	 *
	 * @return true, if successful
	 */
	public static boolean collapseHalfedgeBoundaryPreserving(final HE_Mesh mesh, final HE_Halfedge he) {
		if (mesh.contains(he)) {
			final HE_Halfedge hePair = he.getPair();
			final HE_Face f = he.getFace();
			final HE_Face fp = hePair.getFace();
			final HE_Vertex v = he.getVertex();
			final HE_Vertex vp = hePair.getVertex();
			if (v.isBoundary()) {
				return false;
			}
			final List<HE_Halfedge> tmp = v.getHalfedgeStar();
			for (int i = 0; i < tmp.size(); i++) {
				mesh.setVertex(tmp.get(i), vp);
			}
			mesh.setHalfedge(vp, hePair.getNextInVertex());
			final HE_Halfedge hen = he.getNextInFace();
			final HE_Halfedge hep = he.getPrevInFace();
			final HE_Halfedge hePairn = hePair.getNextInFace();
			final HE_Halfedge hePairp = hePair.getPrevInFace();
			if (f != null) {
				mesh.setHalfedge(f, hen);
			}
			if (fp != null) {
				mesh.setHalfedge(fp, hePairn);
			}
			mesh.setNext(hep, hen);
			mesh.setNext(hePairp, hePairn);
			mesh.remove(he);
			mesh.remove(hePair);
			mesh.remove(v);
			HET_Fixer.deleteTwoEdgeFace(mesh, f);
			HET_Fixer.deleteTwoEdgeFace(mesh, fp);
			return true;
		}
		return false;
	}

	/**
	 * Collapse edge. End vertices are averaged. Degenerate faces are removed. This
	 * function can result in non-manifold meshes.
	 *
	 * @param mesh
	 * @param e    edge to collapse
	 * @return true, if successful
	 */
	public static boolean collapseEdge(final HE_Mesh mesh, final HE_Halfedge e) {
		if (mesh.contains(e)) {
			final HE_Halfedge he = e.isEdge() ? e : e.getPair();
			final HE_Halfedge hePair = he.getPair();
			final HE_Face f = he.getFace();
			final HE_Face fp = hePair.getFace();
			final HE_Vertex v = he.getVertex();
			final HE_Vertex vp = hePair.getVertex();
			vp.getPosition().addSelf(v).mulSelf(0.5);
			final List<HE_Halfedge> tmp = v.getHalfedgeStar();
			for (int i = 0; i < tmp.size(); i++) {
				mesh.setVertex(tmp.get(i), vp);
			}
			mesh.setHalfedge(vp, hePair.getNextInVertex());
			final HE_Halfedge hen = he.getNextInFace();
			final HE_Halfedge hep = he.getPrevInFace();
			final HE_Halfedge hePairn = hePair.getNextInFace();
			final HE_Halfedge hePairp = hePair.getPrevInFace();
			if (f != null) {
				mesh.setHalfedge(f, hen);
			}
			if (fp != null) {
				mesh.setHalfedge(fp, hePairn);
			}
			mesh.setNext(hep, hen);
			mesh.setNext(hePairp, hePairn);
			mesh.remove(he);
			mesh.remove(hePair);
			mesh.remove(v);
			if (f != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, f);
			}
			if (fp != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, fp);
			}
			return true;
		}
		return false;
	}

	/**
	 * Collapse edge to its midpoint or to point on boundary
	 *
	 * @param mesh
	 * @param e
	 * @param strict if true then an edge with two vertices on the boundary is
	 *               always preserved
	 *
	 * @return
	 */
	public static boolean collapseEdgeBoundaryPreserving(final HE_Mesh mesh, final HE_Halfedge e,
			final boolean strict) {
		if (mesh.contains(e)) {
			final HE_Halfedge he = e.isEdge() ? e : e.getPair();
			final HE_Halfedge hePair = he.getPair();
			final HE_Face f = he.getFace();
			final HE_Face fp = hePair.getFace();
			final HE_Vertex v = he.getVertex();
			final HE_Vertex vp = hePair.getVertex();
			if (v.isBoundary()) {
				if (vp.isBoundary()) {
					// In some cases both vertices are on the boundary but the
					// edge itself is not a boundary edge.
					// Collapsing this edge would pinch the mesh creating an
					// invalid topology.
					if (!he.isInnerBoundary() || strict) {
						return false;
					}
					vp.getPosition().addSelf(v).mulSelf(0.5);
				} else {
					vp.set(v);
				}
			} else {
				if (!vp.isBoundary()) {
					vp.getPosition().addSelf(v).mulSelf(0.5);
				}
			}
			final List<HE_Halfedge> tmp = v.getHalfedgeStar();
			for (int i = 0; i < tmp.size(); i++) {
				mesh.setVertex(tmp.get(i), vp);
			}
			mesh.setHalfedge(vp, hePair.getNextInVertex());
			final HE_Halfedge hen = he.getNextInFace();
			final HE_Halfedge hep = he.getPrevInFace();
			final HE_Halfedge hePairn = hePair.getNextInFace();
			final HE_Halfedge hePairp = hePair.getPrevInFace();
			if (f != null) {
				mesh.setHalfedge(f, hen);
			}
			if (fp != null) {
				mesh.setHalfedge(fp, hePairn);
			}
			mesh.setNext(hep, hen);
			mesh.setNext(hePairp, hePairn);
			mesh.remove(he);
			mesh.remove(hePair);
			mesh.remove(e);
			mesh.remove(v);
			if (f != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, f);
			}
			if (fp != null) {
				HET_Fixer.deleteTwoEdgeFace(mesh, fp);
			}
			return true;
		}
		return false;
	}

	/**
	 * Check if point lies inside or on edge of face.
	 *
	 * @param p point
	 * @param f the f
	 * @return true/false
	 */
	public static boolean pointIsInFace(final WB_Coord p, final HE_Face f) {
		return WB_Epsilon.isZero(WB_GeometryOp.getDistanceToClosestPoint3D(p, getPolygon(f)));
	}

	/**
	 * Check if point lies strictly inside face.
	 *
	 * @param p point
	 * @param f the f
	 * @return true/false
	 */
	public static boolean pointIsStrictlyInFace(final WB_Coord p, final HE_Face f) {
		final WB_Polygon poly = getPolygon(f);
		if (!WB_Epsilon.isZeroSq(WB_CoordOp.getSqDistance3D(p, WB_GeometryOp.getClosestPoint3D(p, poly)))) {
			return false;
		}
		if (!WB_Epsilon
				.isZeroSq(WB_CoordOp.getSqDistance3D(p, WB_GeometryOp.getClosestPointOnPeriphery3D(p, poly)))) {
			return false;
		}
		return true;
	}

	/**
	 * Expand vertex to new edge.
	 *
	 * @param mesh
	 * @param v    vertex to expand
	 * @param f1   first face
	 * @param f2   second face
	 * @param vn   position of new vertex
	 */
	public static void expandVertexToEdge(final HE_Mesh mesh, final HE_Vertex v, final HE_Face f1, final HE_Face f2,
			final WB_Coord vn) {
		if (f1 == f2) {
			return;
		}
		HE_Halfedge he = v.getHalfedge();
		HE_Halfedge he1 = new HE_Halfedge();
		HE_Halfedge he2 = new HE_Halfedge();
		do {
			if (he.getFace() == f1) {
				he1 = he;
			}
			if (he.getFace() == f2) {
				he2 = he;
			}
			he = he.getNextInVertex();
		} while (he != v.getHalfedge());
		final HE_Vertex vNew = new HE_Vertex(vn);
		mesh.setHalfedge(vNew, he1);
		mesh.addDerivedElement(vNew, v);
		he = he1;
		do {
			mesh.setVertex(he, vNew);
			he = he.getNextInVertex();
		} while (he != he2);
		final HE_Halfedge he1p = he1.getPrevInFace();
		final HE_Halfedge he2p = he2.getPrevInFace();
		final HE_Halfedge he1new = new HE_Halfedge();
		final HE_Halfedge he2new = new HE_Halfedge();
		mesh.setVertex(he1new, v);
		mesh.setVertex(he2new, vNew);
		mesh.setNext(he1p, he1new);
		mesh.setNext(he1new, he1);
		mesh.setNext(he2p, he2new);
		mesh.setNext(he2new, he2);
		mesh.setPair(he1new, he2new);
		mesh.setFace(he1new, f1);
		mesh.setFace(he2new, f2);
		mesh.addDerivedElement(he1new, v);
		mesh.addDerivedElement(he2new, v);
	}

	public static double getVolume(final HE_Mesh mesh) {
		WB_Coord c = HE_MeshOp.getCenter(mesh);
		double volume = 0.0;
		HE_FaceIterator fItr = mesh.fItr();
		while (fItr.hasNext()) {
			volume += signedVolume(fItr.next(), c);
		}
		return volume;
	}

	private static double signedVolume(final HE_Face f, final WB_Coord center) {
		int[] triangles = f.getTriangles();
		List<HE_Vertex> vertices = f.getFaceVertices();
		double sgnvol = 0;
		for (int i = 0; i < triangles.length; i += 3) {
			WB_Coord p1 = vertices.get(triangles[i]).getPosition().sub(center);
			WB_Coord p2 = vertices.get(triangles[i + 1]).getPosition().sub(center);
			WB_Coord p3 = vertices.get(triangles[i + 2]).getPosition().sub(center);
			sgnvol += 1.0 / 6.0
					* (-p3.xd() * p2.yd() * p1.zd() + p2.xd() * p3.yd() * p1.zd() + p3.xd() * p1.yd() * p2.zd()
							- p1.xd() * p3.yd() * p2.zd() - p2.xd() * p1.yd() * p3.zd() + p1.xd() * p2.yd() * p3.zd());
		}
		return sgnvol;
	}

	/**
	 * Get the closest point to triangle face and its normal.
	 *
	 * @param p point
	 * @param T
	 * @return WB_Coord[2], first WB_Coord is the closest point, second WB_Coord is
	 *         the normal
	 */
	public static WB_Coord[] getClosestPointToTriangleFace(final WB_Coord p, final HE_Face T) {
		WB_Coord p1 = T.getHalfedge().getVertex();
		WB_Coord p2 = T.getHalfedge().getNextInFace().getVertex();
		WB_Coord p3 = T.getHalfedge().getNextInFace().getNextInFace().getVertex();
		final WB_Vector ab = new WB_Point(p2).subToVector3D(p1);
		final WB_Vector ac = new WB_Point(p3).subToVector3D(p1);
		final WB_Vector ap = new WB_Vector(p1, p);
		final double d1 = ab.dot(ap);
		final double d2 = ac.dot(ap);
		if (d1 <= 0 && d2 <= 0) {
			return new WB_Coord[] { new WB_Point(p1), getVertexNormal(T.getHalfedge().getVertex()) };
		}
		final WB_Vector bp = new WB_Vector(p2, p);
		final double d3 = ab.dot(bp);
		final double d4 = ac.dot(bp);
		if (d3 >= 0 && d4 <= d3) {
			return new WB_Coord[] { new WB_Point(p2), getVertexNormal(T.getHalfedge().getNextInFace().getVertex()) };
		}
		final double vc = d1 * d4 - d3 * d2;
		if (vc <= 0 && d1 >= 0 && d3 <= 0) {
			final double v = d1 / (d1 - d3);
			return new WB_Coord[] { new WB_Point(p1).addSelf(ab.mulSelf(v)), getEdgeNormal(T.getHalfedge()) };
		}
		final WB_Vector cp = new WB_Vector(p3, p);
		final double d5 = ab.dot(cp);
		final double d6 = ac.dot(cp);
		if (d6 >= 0 && d5 <= d6) {
			return new WB_Coord[] { new WB_Point(p3),
					getVertexNormal(T.getHalfedge().getNextInFace().getNextInFace().getVertex()) };
		}
		final double vb = d5 * d2 - d1 * d6;
		if (vb <= 0 && d2 >= 0 && d6 <= 0) {
			final double w = d2 / (d2 - d6);
			return new WB_Coord[] { new WB_Point(p1).addSelf(ac.mulSelf(w)),
					getEdgeNormal(T.getHalfedge().getNextInFace().getNextInFace()) };
		}
		final double va = d3 * d6 - d5 * d4;
		if (va <= 0 && d4 - d3 >= 0 && d5 - d6 >= 0) {
			final double w = (d4 - d3) / (d4 - d3 + (d5 - d6));
			return new WB_Coord[] { new WB_Point(p2).addSelf(new WB_Point(p3).subToVector3D(p2).mulSelf(w)),
					getEdgeNormal(T.getHalfedge().getNextInFace()) };
		}
		final double denom = 1.0 / (va + vb + vc);
		final double v = vb * denom;
		final double w = vc * denom;
		return new WB_Coord[] { new WB_Point(p1).addSelf(ab.mulSelf(v).addSelf(ac.mulSelf(w))), getFaceNormal(T) };
	}

	public static WB_Coord[] getClosestPointToTriangleMesh(final WB_Coord p, final HE_Mesh mesh) {
		HE_Mesh tris = mesh.get();
		triangulate(tris);
		double d2;
		double d2max = Double.POSITIVE_INFINITY;
		WB_Coord[] resultmax = null;
		WB_Coord[] result;
		HE_FaceIterator fItr = tris.fItr();
		HE_Face tri;
		while (fItr.hasNext()) {
			tri = fItr.next();
			result = getClosestPointToTriangleFace(p, tri);
			d2 = WB_Point.getSqDistance3D(p, result[0]);
			if (d2 < d2max) {
				d2max = d2;
				resultmax = result;
			}
		}
		return resultmax;
	}

	public static void liftEdges(final HE_Selection edges, final double d) {
		HE_Selection sel = edges.get();
		sel.clearFaces();
		sel.clearVertices();
		sel.collectVertices();
		HE_VertexIterator vItr = sel.vItr();
		WB_Vector[] displacement = new WB_Vector[sel.getNumberOfVertices()];
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			displacement[i++] = WB_Vector.mul(HE_MeshOp.getVertexNormal(v), d);
		}
		vItr = sel.vItr();
		i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			v.getPosition().addSelf(displacement[i++]);
		}
	}

	public static double getCotan(final HE_Halfedge he) {
		if (he.isOuterBoundary()) {
			return 0.0;
		}
		WB_Coord u = getHalfedgeVector(he.getPrevInFace());
		WB_Coord v = new WB_Vector(getHalfedgeVector(he.getNextInFace())).mulSelf(-1);
		return WB_Vector.dot(u, v) / WB_Vector.cross(u, v).getLength();
	}

	public static double[] getMassArray(final HE_Mesh mesh) {
		double[] MA = new double[mesh.getNumberOfVertices()];
		HE_VertexIterator vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			int i = mesh.getIndex(v);
			MA[i] = getBarycentricDualVertexArea(v);
		}
		return MA;
	}

	public static List<HE_FaceFaceIntersection> getIntersection(final HE_Mesh mesh1, final HE_Mesh mesh2) {
		final List<HE_FaceFaceIntersection> ints = new FastList<HE_FaceFaceIntersection>();
		triangulate(mesh1);
		mesh1.resetFaceInternalLabels();
		triangulate(mesh2);
		mesh2.resetFaceInternalLabels();
		HE_Selection sifs1 = HE_Selection.getSelection(mesh1);
		WB_AABBTree3D tree1 = new WB_AABBTree3D(mesh1, 1);
		HE_Selection sifs2 = HE_Selection.getSelection(mesh2);
		WB_AABBTree3D tree2 = new WB_AABBTree3D(mesh2, 1);
		List<WB_AABBNode3D[]> atat = WB_GeometryOp.getIntersection3D(tree1, tree2);
		WB_Triangle T0, T1;
		List<HE_Face> neighbors;
		for (WB_AABBNode3D[] node : atat) {
			for (HE_Face f0 : node[0].getFaces()) {
				T0 = getTriangle(f0);
				neighbors = f0.getNeighborFaces();
				for (HE_Face f1 : node[1].getFaces()) {
					if (!neighbors.contains(f1) && f1.getKey() > f0.getKey()) {
						T1 = getTriangle(f1);
						final WB_IntersectionResult ir = WB_GeometryOp.getIntersection3D(T0, T1);
						if (ir.intersection && ir.object != null
								&& !WB_Epsilon.isZero(((WB_Segment) ir.object).getLength())) {
							f0.setInternalLabel(1);
							f1.setInternalLabel(1);
							sifs1.add(f0);
							sifs2.add(f1);
							ints.add(new HE_FaceFaceIntersection(f0, f1, (WB_Segment) ir.object));
						}
					}
				}
			}
		}
		mesh1.addSelection("intersection", sifs1);
		mesh2.addSelection("intersection", sifs2);
		return ints;
	}

	public Map<Long, HE_IntersectedFace> processIntersectedFaces(List<HE_FaceFaceIntersection> intersections) {
		Map<Long, HE_IntersectedFace> intersectedFaces = new UnifiedMap<Long, HE_IntersectedFace>();
		
		Map<Long, FaceSegmentBin> segmentBins = new UnifiedMap<Long,FaceSegmentBin>();
		
		for (HE_FaceFaceIntersection ffir : intersections) {
			FaceSegmentBin fsb=segmentBins.get(ffir.getFace1().getKey());
			if(fsb==null) {
				fsb=new FaceSegmentBin(ffir.getFace1());
				segmentBins.put(ffir.getFace1().getKey(), fsb);
			}
			fsb.addSegment(ffir.getSegment());
			fsb=segmentBins.get(ffir.getFace2().getKey());
			if(fsb==null) {
				fsb=new FaceSegmentBin(ffir.getFace2());
				segmentBins.put(ffir.getFace2().getKey(), fsb);
			}
			fsb.addSegment(ffir.getSegment());
		}

		

		return intersectedFaces;
	}
	
	class FaceSegmentBin{
		HE_Face face;
		List<WB_Segment> segments;
		
		FaceSegmentBin(HE_Face face){
			this.face=face;
			segments=new FastList<WB_Segment>();
			
		}
		
		void addSegment(WB_Segment segment) {
			segments.add(segment);
			
		}
		
	}

	public class HE_IntersectedFace {
		HE_Face face;
		List<WB_Coord> points;
		int[] constraints;

		public HE_IntersectedFace(HE_Face face, List<WB_Coord> points, int[] constraints) {
			this.face = face;
			this.points = points;
			this.constraints = constraints;
		}

	}

	/**
	 * Fuse all planar faces. Can lead to concave faces.
	 *
	 */
	public static void fuseCoplanarFaces(final HE_Mesh mesh) {
		fuseCoplanarFaces(mesh, 0);
	}

	/**
	 * Fuse all planar faces. Can lead to concave faces.
	 *
	 * @param a
	 * 
	 */
	public static void fuseCoplanarFaces(final HE_Mesh mesh, final double a) {
		int ni;
		int no;
		do {
			ni = mesh.getNumberOfEdges();
			final List<HE_Halfedge> edges = mesh.getEdges();
			for (HE_Halfedge e : edges) {
				if (Math.abs(getEdgeDihedralAngle(e) - Math.PI) < a + WB_Epsilon.EPSILON) {
					if (e.getFace() != e.getPair().getFace()) {
						deleteEdge(mesh, e);
					}
				}
			}
			no = mesh.getNumberOfEdges();
		} while (no < ni);
		HET_Fixer.deleteDanglingEdges(mesh);
		HET_Fixer.deleteCollinearVertices(mesh);
	}

	public static void fuseCoplanarFaces(final HE_Selection sel) {
		fuseCoplanarFaces(sel, 0);
	}

	/**
	 * Fuse all planar faces. Can lead to concave faces.
	 *
	 * @param a the a
	 */
	public static void fuseCoplanarFaces(final HE_Selection sel, final double a) {
		sel.collectEdgesByFace();
		final List<HE_Halfedge> edges = sel.getInnerEdges();
		for (HE_Halfedge e : edges) {
			if (Math.abs(getEdgeDihedralAngle(e) - Math.PI) < a + WB_Epsilon.EPSILON) {
				if (e.getFace() != e.getPair().getFace()) {
					deleteEdge(sel.getParent(), e);
				}
			}
		}
		sel.cleanSelection();
		HET_Fixer.deleteDanglingEdges(sel.getParent());
		HET_Fixer.deleteCollinearVertices(sel.getParent());
	}

	/**
	 * Delete edge. Adjacent faces are fused.
	 *
	 * @param e edge to delete
	 * @return fused face (or null)
	 */
	public static HE_Face deleteEdge(final HE_Mesh mesh, final HE_Halfedge e) {
		HE_Face f = null;
		final HE_Halfedge he1 = e;
		final HE_Halfedge he2 = he1.getPair();
		final HE_Halfedge he1n = he1.getNextInFace();
		final HE_Halfedge he2n = he2.getNextInFace();
		final HE_Halfedge he1p = he1.getPrevInFace();
		final HE_Halfedge he2p = he2.getPrevInFace();
		HE_Vertex v = he1.getVertex();
		if (v.getHalfedge() == he1) {
			mesh.setHalfedge(v, he1.getNextInVertex());
		}
		v = he2.getVertex();
		if (v.getHalfedge() == he2) {
			mesh.setHalfedge(v, he2.getNextInVertex());
		}
		mesh.setNext(he1p, he2n);
		mesh.setNext(he2p, he1n);
		if (he1.getFace() != null && he2.getFace() != null) {
			f = new HE_Face();
			f.copyProperties(he1.getFace());
			mesh.setHalfedge(f, he1p);
			HE_Halfedge he = he1p;
			do {
				mesh.setFace(he, f);
				he = he.getNextInFace();
			} while (he != he1p);
		}
		mesh.remove(he1);
		mesh.remove(he2);
		mesh.addDerivedElement(f, he1.getFace(), he2.getFace());
		if (he1.getFace() != null) {
			mesh.remove(he1.getFace());
		}
		if (he2.getFace() != null) {
			mesh.remove(he2.getFace());
		}
		return f;
	}

	public static WB_Point getClosestPoint(HE_Face f, final WB_Coord p) {
		if (f.getFaceDegree() == 3) {
			return WB_GeometryOp.getClosestPointToTriangle3D(p, f.getHalfedge().getVertex(),
					f.getHalfedge().getNextInFace().getVertex(), f.getHalfedge().getNextInFace(2).getVertex());
		}
		List<HE_Vertex> points = f.getFaceVertices();
		int[] tris = f.getTriangles();
		final int n = tris.length;
		double dmax2 = Double.POSITIVE_INFINITY;
		WB_Point closest = new WB_Point();
		WB_Point tmp;
		for (int i = 0; i < n; i += 3) {
			tmp = WB_GeometryOp.getClosestPointToTriangle3D(p, points.get(tris[i]), points.get(tris[i + 1]),
					points.get(tris[i + 2]));
			final double d2 = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d2 < dmax2) {
				closest = tmp;
				dmax2 = d2;
			}
		}
		return closest;
	}

	/**
	 * Get type of face vertex associated with halfedge.
	 *
	 * @return HE.FLAT, HE.CONVEX, HE.CONCAVE
	 */
	public static WB_Classification getHalfedgeType(HE_Halfedge he) {
		if (he.getVertex() == null) {
			return null;
		}
		WB_Vector v = WB_Vector.subToVector3D(he.getVertex(), he.getPrevInFace().getVertex());
		v.normalizeSelf();
		final WB_Vector vn = WB_Vector.subToVector3D(he.getNextInFace().getVertex(), he.getVertex());
		vn.normalizeSelf();
		v = v.cross(vn);
		final WB_Coord n;
		if (he.getFace() == null) {
			n = WB_Vector.mul(HE_MeshOp.getFaceNormal(he.getPair().getFace()), -1);
		} else {
			n = HE_MeshOp.getFaceNormal(he.getFace());
		}
		final double dot = v.dot(n);
		if (v.isParallel(vn)) {
			return WB_Classification.FLAT;
		} else if (dot > -WB_Epsilon.EPSILON) {
			return WB_Classification.CONVEX;
		} else {
			return WB_Classification.CONCAVE;
		}
	}

	public static WB_Vector getHalfedgeVector(HE_Halfedge he) {
		if (he.getPair() != null && he.getVertex() != null && he.getPair().getVertex() != null) {
			final WB_Vector v = WB_Vector.subToVector3D(he.getPair().getVertex(), he.getVertex());
			return v;
		}
		return null;
	}

	/**
	 * Get tangent WB_Vector of halfedge.
	 *
	 * @return tangent
	 */
	public static WB_Vector getHalfedgeTangent(HE_Halfedge he) {
		if (he.getPair() != null && he.getVertex() != null && he.getPair().getVertex() != null) {
			final WB_Vector v = WB_Vector.subToVector3D(he.getPair().getVertex(), he.getVertex());
			v.normalizeSelf();
			return v;
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Vector getEdgeTangent(HE_Halfedge he) {
		final WB_Vector v = getHalfedgeTangent(he);
		if (v == null) {
			return null;
		}
		return he.isEdge() ? v : WB_Vector.mul(v, -1);
	}

	/**
	 * Get center of halfedge.
	 *
	 * @return center
	 */
	public static WB_Point getHalfedgeCenter(HE_Halfedge he) {
		if (he.getNextInFace() != null && he.getVertex() != null && he.getNextInFace().getVertex() != null) {
			return gf.createMidpoint(he.getNextInFace().getVertex(), he.getVertex());
		}
		return null;
	}

	/**
	 * Get offset center of halfedge.
	 *
	 * @param f
	 * @return center
	 */
	public static WB_Point getHalfedgeCenter(HE_Halfedge he, final double f) {
		if (he.getNextInFace() != null && he.getVertex() != null && he.getNextInFace().getVertex() != null) {
			return gf.createMidpoint(he.getNextInFace().getVertex(), he.getVertex()).addMulSelf(f,
					getHalfedgeNormal(he));
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Point getEdgeCenter(HE_Halfedge he) {
		if (he.getNextInFace() != null && he.getVertex() != null && he.getNextInFace().getVertex() != null) {
			return gf.createMidpoint(he.getNextInFace().getVertex(), he.getVertex());
		}
		return null;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public static WB_Point getEdgeCenter(HE_Halfedge he, final double f) {
		if (he.getNextInFace() != null && he.getVertex() != null && he.getNextInFace().getVertex() != null) {
			return gf.createMidpoint(he.getNextInFace().getVertex(), he.getVertex()).addMulSelf(f, getEdgeNormal(he));
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static WB_Vector getEdgeNormal(HE_Halfedge he) {
		if (he.getPair() == null) {
			return null;
		}
		HE_Halfedge he1, he2;
		if (he.isEdge()) {
			he1 = he;
			he2 = he.getPair();
		} else {
			he1 = he.getPair();
			he2 = he;
		}
		if (he1.getFace() == null && he2.getFace() == null) {
			return null;
		}
		final WB_Coord n1 = he1.getFace() != null ? HE_MeshOp.getFaceNormal(he1.getFace()) : new WB_Vector(0, 0, 0);
		final WB_Coord n2 = he2.getFace() != null ? HE_MeshOp.getFaceNormal(he2.getFace()) : new WB_Vector(0, 0, 0);
		final WB_Vector n = new WB_Vector(n1.xd() + n2.xd(), n1.yd() + n2.yd(), n1.zd() + n2.zd());
		n.normalizeSelf();
		return n;
	}

	/**
	 * Get halfedge normal.
	 *
	 * @return in-face normal of face, points inwards
	 */
	public static WB_Vector getHalfedgeNormal(HE_Halfedge he) {
		WB_Coord fn;
		if (he.getFace() == null && he.getPair() == null) {
			return null;
		}
		if (he.getFace() == null) {
			if (he.getPair().getFace() == null) {
				return null;
			}
			fn = HE_MeshOp.getFaceNormal(he.getPair().getFace());
		} else {
			fn = HE_MeshOp.getFaceNormal(he.getFace());
		}
		final HE_Vertex vn = he.getNextInFace().getVertex();
		final WB_Vector _normal = new WB_Vector(vn);
		_normal.subSelf(he.getVertex());
		_normal.set(WB_Vector.cross(fn, _normal));
		_normal.normalizeSelf();
		return _normal;
	}

	/**
	 * Get area of faces bounding halfedge.
	 *
	 * @return area
	 */
	public static double getHalfedgeArea(HE_Halfedge he) {
		return 0.5 * getEdgeArea(he);
	}

	/**
	 * Get angle between adjacent faces.
	 *
	 * @return angle
	 */
	public static double getHalfedgeDihedralAngle(HE_Halfedge he) {
		if (he.isOuterBoundary() || he.isInnerBoundary()) {
			return 0.0;
		}
		WB_Coord n1 = HE_MeshOp.getFaceNormal(he.getFace());
		WB_Coord n2 = HE_MeshOp.getFaceNormal(he.getPair().getFace());
		WB_Coord w = getHalfedgeTangent(he);
		double cosTheta = WB_Vector.dot(n1, n2);
		double sinTheta = WB_Vector.cross(n1, n2).dot(w);
		return Math.atan2(sinTheta, cosTheta);
	}

	/**
	 * Get area of faces bounding edge.
	 *
	 * @return area
	 */
	public static double getEdgeArea(HE_Halfedge he) {
		if (he.getPair() == null) {
			return Double.NaN;
		}
		HE_Halfedge he1, he2;
		if (he.isEdge()) {
			he1 = he;
			he2 = he.getPair();
		} else {
			he1 = he.getPair();
			he2 = he;
		}
		if (he1.getFace() == null && he2.getFace() == null) {
			return Double.NaN;
		}
		double result = 0;
		int n = 0;
		if (he1.getFace() != null) {
			result += HE_MeshOp.getFaceArea(he1.getFace());
			n++;
		}
		if (he2.getFace() != null) {
			result += HE_MeshOp.getFaceArea(he2.getFace());
			n++;
		}
		return result / n;
	}

	

	/**
	 * Return angle between adjacent faces.
	 *
	 * @return angle
	 */
	public static double getEdgeDihedralAngle(HE_Halfedge he) {
		if (he.getPair() == null) {
			return 0.0;
		}
		HE_Halfedge he1, he2;
		if (he.isEdge()) {
			he1 = he;
			he2 = he.getPair();
		} else {
			he1 = he.getPair();
			he2 = he;
		}
		if (he1.getFace() == null || he2.getFace() == null) {
			return 0.0;
		} else {
			final WB_Coord n1 = HE_MeshOp.getFaceNormal(he1.getFace());
			final WB_Coord n2 = HE_MeshOp.getFaceNormal(he2.getFace());
			return WB_GeometryOp.getDihedralAngleNorm(n1, n2);
		}
	}

	public static double getEdgeCosDihedralAngle(HE_Halfedge he) {
		if (he.getPair() == null) {
			return Double.NaN;
		}
		HE_Halfedge he1, he2;
		if (he.isEdge()) {
			he1 = he;
			he2 = he.getPair();
		} else {
			he1 = he.getPair();
			he2 = he;
		}
		if (he1.getFace() == null || he2.getFace() == null) {
			return Double.NaN;
		} else {
			final WB_Coord n1 = HE_MeshOp.getFaceNormal(he1.getFace());
			final WB_Coord n2 = HE_MeshOp.getFaceNormal(he2.getFace());
			return WB_GeometryOp.getCosDihedralAngleNorm(n1, n2);
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public static double getLength(HE_Halfedge he) {
		return WB_CoordOp.getDistance3D(he.getVertex(), he.getEndVertex());
	}

	/**
	 *
	 *
	 * @return
	 */
	public static double getSqLength(HE_Halfedge he) {
		return WB_CoordOp.getSqDistance3D(he.getVertex(), he.getEndVertex());
	}

	/**
	 *
	 *
	 * @return
	 */
	public static double getAngle(HE_Halfedge he) {
		final WB_Coord c = he.getVertex();
		final WB_Coord p1 = he.getEndVertex();
		final WB_Coord p2 = he.getPrevInFace().getVertex();
		if (c == null) {
			return Double.NaN;
		}
		return WB_CoordOp.getAngleBetween(c.xd(), c.yd(), c.zd(), p1.xd(), p1.yd(), p1.zd(), p2.xd(), p2.yd(),
				p2.zd());
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public static WB_AABB getAABB(HE_Face f) {
		WB_AABB aabb = new WB_AABB();
		HE_Halfedge he = f.getHalfedge();
		do {
			aabb.expandToInclude(he.getVertex());
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		return aabb;
	}

	/**
	 * 
	 * @param he
	 * @return
	 */
	public static WB_AABB getAABB(HE_Halfedge he) {
		WB_AABB aabb = new WB_AABB();
		aabb.expandToInclude(he.getVertex());
		aabb.expandToInclude(he.getEndVertex());
		return aabb;
	}

	public static WB_AABB getAABB(HE_Mesh mesh) {
		final double[] result = getLimits(mesh);
		final WB_Point min = gf.createPoint(result[0], result[1], result[2]);
		final WB_Point max = gf.createPoint(result[3], result[4], result[5]);
		return new WB_AABB(min, max);
	}

	public static WB_Sphere getBoundingSphere(HE_Mesh mesh) {
		return WB_GeometryOp.getBoundingSphere(mesh.getVertices());
	}

	public static final WB_AABB getAABB(HE_Selection sel) {
		final double[] result = getLimits(sel);
		final WB_Point min = gf.createPoint(result[0], result[1], result[2]);
		final WB_Point max = gf.createPoint(result[3], result[4], result[5]);
		return new WB_AABB(min, max);
	}

	public static final WB_Sphere getBoundingSphere(HE_Selection sel) {
		return WB_GeometryOp.getBoundingSphere(sel.getVertices());
	}

	/**
	 * Get range of vertex coordinates.
	 *
	 * @return array of limit values: min x, min y, min z, max x, max y, max z
	 */
	public static double[] getLimits(HE_HalfedgeStructure hes) {
		final double[] result = new double[6];
		for (int i = 0; i < 3; i++) {
			result[i] = Double.POSITIVE_INFINITY;
		}
		for (int i = 3; i < 6; i++) {
			result[i] = Double.NEGATIVE_INFINITY;
		}
		HE_Vertex v;
		for (int i = 0; i < hes.getNumberOfVertices(); i++) {
			v = hes.getVertexWithIndex(i);
			result[0] = Math.min(result[0], v.xd());
			result[1] = Math.min(result[1], v.yd());
			result[2] = Math.min(result[2], v.zd());
			result[3] = Math.max(result[3], v.xd());
			result[4] = Math.max(result[4], v.yd());
			result[5] = Math.max(result[5], v.zd());
		}
		return result;
	}

	/**
	 * Split the closest face in the query point.
	 *
	 * @param p          query point
	 * @param vertexTree KD-tree from mesh (from vertexTree())
	 */
	public static void addPointInClosestFace(HE_Mesh mesh, final WB_Coord p,
			final WB_KDTree3D<WB_Coord, Long> vertexTree) {
		final WB_KDEntry<WB_Coord, Long>[] closestVertex = vertexTree.getNearestNeighbors(p, 1);
		final HE_Vertex v = mesh.getVertexWithKey(closestVertex[0].value);
		final List<HE_Face> faces = v.getFaceStar();
		double d;
		double dmin = Double.POSITIVE_INFINITY;
		HE_Face face = new HE_Face();
		for (int i = 0; i < faces.size(); i++) {
			final WB_Polygon poly = HE_MeshOp.getPolygon(faces.get(i));
			final WB_Coord tmp = WB_GeometryOp.getClosestPoint3D(p, poly);
			d = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d < dmin) {
				dmin = d;
				face = faces.get(i);
				;
			}
		}
		final HE_Vertex nv = HEM_TriSplit.splitFaceTri(mesh, face, p).vItr().next();
		vertexTree.add(nv, nv.getKey());
	}

	/**
	 *
	 *
	 * @param vertices
	 * @param loop
	 * @return
	 */
	public static HE_Path createPathFromIndices(HE_Mesh mesh, final int[] vertices, final boolean loop) {
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		if (vertices.length > 1) {
			HE_Halfedge he;
			for (int i = 0; i < vertices.length - 1; i++) {
				he = mesh.getHalfedgeFromTo(mesh.getVertexWithIndex(vertices[i]),
						mesh.getVertexWithIndex(vertices[i + 1]));
				if (he == null) {
					// throw new IllegalArgumentException(
					// "Two vertices " + vertices[i] + " and " + vertices[i + 1]
					// + " in path are not connected.");
				} else {
					halfedges.add(he);
				}
			}
			if (loop) {
				he = mesh.getHalfedgeFromTo(mesh.getVertexWithIndex(vertices[vertices.length - 1]),
						mesh.getVertexWithIndex(vertices[0]));
				if (he == null) {
					throw new IllegalArgumentException("Vertices " + vertices[vertices.length - 1] + " and "
							+ vertices[0] + " in path are not connected: path is not a loop.");
				}
			}
		}
		final HE_Path path = new HE_Path(halfedges, loop);
		return path;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static double getArea(HE_Mesh mesh) {
		final Iterator<HE_Face> fItr = mesh.fItr();
		double A = 0.0;
		while (fItr.hasNext()) {
			A += HE_MeshOp.getFaceArea(fItr.next());
		}
		return A;
	}

	/**
	 * Return the closest point on the mesh.
	 *
	 * @param p          query point
	 * @param vertexTree KD-tree from mesh (from vertexTree())
	 * @return WB_Coordinate closest point
	 */
	public static WB_Point getClosestPoint(HE_Mesh mesh, final WB_Coord p, final WB_KDTree3D<WB_Coord, Long> vertexTree) {
		final WB_KDEntry<WB_Coord, Long>[] closestVertex = vertexTree.getNearestNeighbors(p, 1);
		final HE_Vertex v = mesh.getVertexWithKey(closestVertex[0].value);
		if (v == null) {
			return null;
		}
		final List<HE_Face> faces = v.getFaceStar();
		double d;
		double dmin = Double.POSITIVE_INFINITY;
		WB_Point result = new WB_Point();
		for (int i = 0; i < faces.size(); i++) {
			final WB_Polygon poly = HE_MeshOp.getPolygon(faces.get(i));
			final WB_Point tmp = WB_GeometryOp.getClosestPoint3D(p, poly);
			d = WB_CoordOp.getSqDistance3D(tmp, p);
			if (d < dmin) {
				dmin = d;
				result = tmp;
			}
		}
		return result;
	}

	/**
	 * Return the closest vertex on the mesh.
	 *
	 * @param p          query point
	 * @param vertexTree KD-tree from mesh (from vertexTree())
	 * @return HE_Vertex closest vertex
	 */
	public static HE_Vertex getClosestVertex(HE_Mesh mesh, final WB_Coord p,
			final WB_KDTree3D<WB_Coord, Long> vertexTree) {
		final WB_KDEntry<WB_Coord, Long>[] closestVertex = vertexTree.getNearestNeighbors(p, 1);
		if (closestVertex.length == 0) {
			return null;
		}
		return mesh.getVertexWithKey(closestVertex[0].value);
	}

	/**
	 * Get the center (average of all vertex positions).
	 *
	 * @return the center
	 */
	public static WB_Point getCenter(HE_Mesh mesh) {
		final WB_Point c = new WB_Point(0, 0, 0);
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		while (vItr.hasNext()) {
			c.addSelf(vItr.next());
		}
		c.divSelf(mesh.getNumberOfVertices());
		return c;
	}

	/**
	 * Cycle halfedges.
	 *
	 * @param halfedges halfedges to cycle
	 */
	public static void cycleHalfedges(HE_Mesh mesh, final List<HE_Halfedge> halfedges) {
		orderHalfedges(mesh, halfedges, true);
	}

	/**
	 * Cycle halfedges.
	 *
	 * @param halfedges halfedges to cycle
	 */
	public static void cycleHalfedgesReverse(HE_Mesh mesh, final List<HE_Halfedge> halfedges) {
		orderHalfedgesReverse(mesh, halfedges, true);
	}

	public static void orderHalfedges(HE_Mesh mesh, final List<HE_Halfedge> halfedges, final boolean loop) {
		HE_Halfedge he;
		final int n = halfedges.size();
		if (n > 0) {
			for (int j = 0; j < n - 1; j++) {
				he = halfedges.get(j);
				mesh.setNext(he, halfedges.get(j + 1));
			}
			if (loop) {
				he = halfedges.get(n - 1);
				mesh.setNext(he, halfedges.get(0));
			}
		}
	}

	/**
	 * Cycle halfedges.
	 *
	 * @param halfedges halfedges to cycle
	 */
	public static void orderHalfedgesReverse(HE_Mesh mesh, final List<HE_Halfedge> halfedges, final boolean loop) {
		HE_Halfedge he;
		final int n = halfedges.size();
		if (n > 0) {
			if (loop) {
				he = halfedges.get(0);
				mesh.setNext(he, halfedges.get(n - 1));
			}
			for (int j = 1; j < n; j++) {
				he = halfedges.get(j);
				mesh.setNext(he, halfedges.get(j - 1));
			}
		}
	}

	/**
	 * Cap all remaining unpaired halfedges. Only use after pairHalfedges();
	 */
	public static void capHalfedges(HE_Mesh mesh) {
		tracker.setStartStatusStr("HE_MeshOp", "Capping unpaired halfedges.");
		final List<HE_Halfedge> unpairedHalfedges = mesh.getUnpairedHalfedges();
		final int nuh = unpairedHalfedges.size();
		final HE_Halfedge[] newHalfedges = new HE_Halfedge[nuh];
		HE_Halfedge he1, he2;
		WB_ProgressCounter counter = new WB_ProgressCounter(nuh, 10);
		tracker.setCounterStatusStr("HE_MeshOp", "Capping unpaired halfedges.", counter);
		for (int i = 0; i < nuh; i++) {
			he1 = unpairedHalfedges.get(i);
			he2 = new HE_Halfedge();
			mesh.setVertex(he2, he1.getNextInFace().getVertex());
			mesh.setPair(he1, he2);
			newHalfedges[i] = he2;
			mesh.addDerivedElement(he2);
			counter.increment();
		}
		counter = new WB_ProgressCounter(nuh, 10);
		tracker.setCounterStatusStr("HE_MeshOp", "Cycling new halfedges.", counter);
		for (int i = 0; i < nuh; i++) {
			he1 = newHalfedges[i];
			if (he1.getNextInFace() == null) {
				for (int j = 0; j < nuh; j++) {
					he2 = newHalfedges[j];
					if (!he2.isVisited()) {
						if (he2.getVertex() == he1.getPair().getVertex()) {
							mesh.setNext(he1, he2);
							he2.setVisited();
							break;
						}
					}
				}
			}
			counter.increment();
		}
		tracker.setStopStatusStr("HE_MeshOp", "Processed unpaired halfedges.");
	}

	/**
	 *
	 */
	public static void pairHalfedges(HE_Mesh mesh) {
		pairHalfedgesOnePass(mesh);
	}

	/**
	 * Pair halfedges.
	 *
	 * @param unpairedHalfedges
	 */
	public static void pairHalfedges(HE_Mesh mesh, final List<HE_Halfedge> unpairedHalfedges) {
		class VertexInfo {
			FastList<HE_Halfedge> in;
			FastList<HE_Halfedge> out;

			VertexInfo() {
				out = new FastList<HE_Halfedge>();
				in = new FastList<HE_Halfedge>();
			}
		}
		final LongObjectHashMap<VertexInfo> vertexLists = new LongObjectHashMap<VertexInfo>();
		HE_Vertex v;
		VertexInfo vi;
		for (final HE_Halfedge he : unpairedHalfedges) {
			v = he.getVertex();
			vi = vertexLists.get(v.getKey());
			if (vi == null) {
				vi = new VertexInfo();
				vertexLists.put(v.getKey(), vi);
			}
			vi.out.add(he);
			v = he.getNextInFace().getVertex();
			vi = vertexLists.get(v.getKey());
			if (vi == null) {
				vi = new VertexInfo();
				vertexLists.put(v.getKey(), vi);
			}
			vi.in.add(he);
		}
		HE_Halfedge he;
		HE_Halfedge he2;
		final Iterator<VertexInfo> vitr = vertexLists.iterator();
		VertexInfo vInfo;
		while (vitr.hasNext()) {
			vInfo = vitr.next();
			for (int i = 0; i < vInfo.out.size(); i++) {
				he = vInfo.out.get(i);
				if (he.getPair() == null) {
					for (int j = 0; j < vInfo.in.size(); j++) {
						he2 = vInfo.in.get(j);
						if (he2.getPair() == null) {
							if (he.getVertex() == he2.getNextInFace().getVertex()
									&& he2.getVertex() == he.getNextInFace().getVertex()) {
								mesh.setPair(he, he2);
								break;
							}
						}
					}
					for (int j = 0; j < vInfo.out.size(); j++) {
						he2 = vInfo.out.get(j);
						if (he2 != he && he2.getPair() == null) {
							if (he.getNextInFace().getVertex() == he2.getNextInFace().getVertex()) {
								// Two identical halfedges found!
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Try to pair all unpaired halfedges.
	 *
	 * @return
	 */
	public static List<HE_Halfedge> pairHalfedgesOnePass(HE_Mesh mesh) {
		tracker.setStartStatusStr("HE_MeshOp", "Pairing halfedges.");
		class VertexInfo {
			FastList<HE_Halfedge> in;
			FastList<HE_Halfedge> out;

			VertexInfo() {
				out = new FastList<HE_Halfedge>();
				in = new FastList<HE_Halfedge>();
			}
		}
		final LongObjectHashMap<VertexInfo> vertexLists = new LongObjectHashMap<VertexInfo>();
		final List<HE_Halfedge> unpairedHalfedges = mesh.getUnpairedHalfedges();
		HE_Vertex v;
		VertexInfo vi;
		WB_ProgressCounter counter = new WB_ProgressCounter(unpairedHalfedges.size(), 10);
		tracker.setCounterStatusStr("HE_MeshOp", "Classifying unpaired halfedges.", counter);
		for (final HE_Halfedge he : unpairedHalfedges) {
			v = he.getVertex();
			vi = vertexLists.get(v.getKey());
			if (vi == null) {
				vi = new VertexInfo();
				vertexLists.put(v.getKey(), vi);
			}
			vi.out.add(he);
			v = he.getNextInFace().getVertex();
			vi = vertexLists.get(v.getKey());
			if (vi == null) {
				vi = new VertexInfo();
				vertexLists.put(v.getKey(), vi);
			}
			vi.in.add(he);
			counter.increment();
		}
		HE_Halfedge he;
		HE_Halfedge he2;
		counter = new WB_ProgressCounter(vertexLists.size(), 10);
		tracker.setCounterStatusStr("HE_MeshOp", "Pairing unpaired halfedges per vertex.", counter);
		final Iterator<VertexInfo> vitr = vertexLists.iterator();
		VertexInfo vInfo;
		final List<HE_Halfedge> mismatchedHalfedges = new FastList<HE_Halfedge>();
		while (vitr.hasNext()) {
			vInfo = vitr.next();
			for (int i = 0; i < vInfo.out.size(); i++) {
				he = vInfo.out.get(i);
				if (he.getPair() == null) {
					for (int j = 0; j < vInfo.in.size(); j++) {
						he2 = vInfo.in.get(j);
						if (he2.getPair() == null) {
							if (he.getVertex() == he2.getNextInFace().getVertex()
									&& he2.getVertex() == he.getNextInFace().getVertex()) {
								mesh.setPair(he, he2);
								break;
							}
						}
					}
					for (int j = 0; j < vInfo.out.size(); j++) {
						he2 = vInfo.out.get(j);
						if (he2 != he && he2.getPair() == null) {
							if (he.getNextInFace().getVertex() == he2.getNextInFace().getVertex()) {
								mismatchedHalfedges.add(he);
								mismatchedHalfedges.add(he2);
								break;
							}
						}
					}
				}
			}
			counter.increment();
		}
		tracker.setStopStatusStr("HE_MeshOp", "Processed unpaired halfedges.");
		return mismatchedHalfedges;
	}

}
