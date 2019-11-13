/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import static wblut.geom.WB_Classification.BACK;
import static wblut.geom.WB_Classification.FRONT;
import static wblut.geom.WB_Classification.ON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_IntersectionResult;
import wblut.geom.WB_Line;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.math.WB_Epsilon;

/**
 * @author FVH
 *
 *         Port of David Geier's algorithm
 *         https://geidav.wordpress.com/2015/03/21/splitting-an-arbitrary-
 *         polygon-by-a-line/
 *
 */
class HET_FaceSplitter {
	List<PolyEdge>				splitPoly;
	List<PolyEdge>				edgesOnLine;
	private WB_GeometryFactory	gf	= new WB_GeometryFactory();

	HET_FaceSplitter() {
	}

	// Divide a face whose edges have been split before by a plane
	List<HE_Vertex[]> splitFace(final HE_Face f, final WB_Plane P) {
		List<HE_Vertex[]> subfaces = new ArrayList<HE_Vertex[]>();
		WB_Plane Q = HE_MeshOp.getPlane(f);
		WB_IntersectionResult intersection = WB_GeometryOp
				.getIntersection3D(P, Q);
		if (!intersection.intersection) {
			return null;
		} else {
			List<HE_Vertex> coords = f.getFaceVertices();
			processEdges(coords, P);
			sortEdges((WB_Line) intersection.object);
			splitPolygon();
			subfaces.addAll(collectPolygons());
		}
		return subfaces;
	}

	void processEdges(final List<HE_Vertex> coords, final WB_Plane P) {
		splitPoly = new FastList<PolyEdge>();
		edgesOnLine = new FastList<PolyEdge>();
		for (int i = 0; i < coords.size(); i++) {
			WB_Classification edgeStartSide = WB_GeometryOp
					.classifyPointToPlane3D(coords.get(i), P);
			splitPoly.add(new PolyEdge(coords.get(i), edgeStartSide));
			if (WB_GeometryOp.classifyPointToPlane3D(coords.get(i),
					P) == ON) {
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			}
			// all split are dealt with before calling HET_FaceSplitter
		}
		for (int i = 0, j = splitPoly.size() - 1; i < splitPoly
				.size(); j = i, i++) {
			splitPoly.get(j).next = splitPoly.get(i);
			splitPoly.get(i).prev = splitPoly.get(j);
		}
	}

	void sortEdges(final WB_Line L) {
		Collections.sort(edgesOnLine, new EdgeOnLineComparator(L));
		for (int i = 1; i < edgesOnLine.size(); i++) {
			edgesOnLine.get(i).distOnLine = WB_Point.getDistance3D(
					edgesOnLine.get(i).pos, edgesOnLine.get(0).pos);
		}
		;
	}

	void splitPolygon() {
		if (edgesOnLine.size() == 2) {
			createBridge(edgesOnLine.get(0), edgesOnLine.get(1));
			verifyCycles();
		} else {
			PolyEdge reUseSrc = null;
			for (int i = 0; i < edgesOnLine.size(); i++) {
				// find source
				PolyEdge srcEdge = reUseSrc;
				reUseSrc = null;
				for (; srcEdge == null && i < edgesOnLine.size(); i++) {
					PolyEdge curEdge = edgesOnLine.get(i);
					WB_Classification curSide = curEdge.side;
					WB_Classification prevSide = curEdge.prev.side;
					WB_Classification nextSide = curEdge.next.side;
					assert curSide == ON;
					if (prevSide == FRONT && nextSide == BACK
							|| prevSide == FRONT && nextSide == ON
									&& curEdge.next.distOnLine < curEdge.distOnLine
							|| prevSide == ON && nextSide == BACK
									&& curEdge.prev.distOnLine < curEdge.distOnLine) {
						srcEdge = curEdge;
						srcEdge.isSource = true;
					}
				}
				// find destination
				PolyEdge dstEdge = null;
				for (; dstEdge == null && i < edgesOnLine.size();) {
					PolyEdge curEdge = edgesOnLine.get(i);
					WB_Classification curSide = curEdge.side;
					WB_Classification prevSide = curEdge.prev.side;
					WB_Classification nextSide = curEdge.next.side;
					assert curSide == ON;
					if (prevSide == BACK && nextSide == FRONT
							|| prevSide == ON && nextSide == FRONT
							|| prevSide == BACK && nextSide == ON
							|| prevSide == BACK && nextSide == BACK
							|| prevSide == FRONT && nextSide == FRONT) {
						dstEdge = curEdge;
						dstEdge.isDestination = true;
					} else {
						i++;
					}
				}
				// bridge source and destination
				if (srcEdge != null && dstEdge != null) {
					createBridge(srcEdge, dstEdge);
					verifyCycles();
					// is it a configuration in which a vertex
					// needs to be reused as source vertex?
					if (srcEdge.prev.prev.side == FRONT) {
						reUseSrc = srcEdge.prev;
						reUseSrc.isSource = true;
					} else if (dstEdge.next.side == BACK) {
						reUseSrc = dstEdge;
						reUseSrc.isSource = true;
					}
				}
			}
		}
	}

	void createBridge(final PolyEdge srcEdge, final PolyEdge dstEdge) {
		PolyEdge srcToDst = new PolyEdge(srcEdge.pos, srcEdge.side);
		PolyEdge dstToSrc = new PolyEdge(dstEdge.pos, dstEdge.side);
		splitPoly.add(srcToDst);
		splitPoly.add(dstToSrc);
		srcToDst.next = dstEdge;
		srcToDst.prev = srcEdge.prev;
		dstToSrc.next = srcEdge;
		dstToSrc.prev = dstEdge.prev;
		srcEdge.prev.next = srcToDst;
		srcEdge.prev = dstToSrc;
		dstEdge.prev.next = dstToSrc;
		dstEdge.prev = srcToDst;
	}

	void verifyCycles() {
		for (PolyEdge edge : splitPoly) {
			PolyEdge curEdge = edge;
			int count = 0;
			do {
				assert count < splitPoly.size();
				curEdge = curEdge.next;
				count++;
			} while (curEdge != edge);
		}
	}

	List<HE_Vertex[]> collectPolygons() {
		List<HE_Vertex[]> resPolys = new ArrayList<HE_Vertex[]>();
		for (PolyEdge e : splitPoly) {
			if (!e.visited) {
				List<PolyEdge> edges = new FastList<PolyEdge>();
				PolyEdge curEdge = e;
				do {
					curEdge.visited = true;
					edges.add(curEdge);
					curEdge = curEdge.next;
				} while (curEdge != e);
				if (edges.size() > 2) {
					HE_Vertex[] poly = new HE_Vertex[edges.size()];
					for (int i = 0; i < edges.size(); i++) {
						poly[i] = edges.get(i).pos;
					}
					resPolys.add(poly);
				}
			}
		}
		return resPolys;
	}

	static class PolyEdge {
		HE_Vertex			pos;			// start position on edge
		WB_Classification	side;			// start position's side of split
											// line
		PolyEdge			next;			// next polygon in linked list
		PolyEdge			prev;			// previous polygon in linked list
		double				distOnLine;		// distance relative to first point
											// on split line
		boolean				isSource;		// for visualization
		boolean				isDestination;	// for visualization
		boolean				visited;		// for collecting split polygons

		PolyEdge(final HE_Vertex pos, final WB_Classification side) {
			this.pos = pos;
			this.side = side;
			next = null;
			prev = null;
			isSource = false;
			isDestination = false;
			visited = false;
			distOnLine = 0;
		}
	}

	static class EdgeOnLineComparator implements Comparator<PolyEdge> {
		WB_Line L;

		EdgeOnLineComparator(final WB_Line L) {
			this.L = L;
		}

		@Override
		public int compare(final PolyEdge e0, final PolyEdge e1) {
			double d = WB_GeometryOp.getParameterOfPointOnLine3D(e0.pos, L)
					- WB_GeometryOp.getParameterOfPointOnLine3D(e1.pos, L);
			return WB_Epsilon.isZero(d) ? 0 : d > 0 ? 1 : -1;
		}
	}

	WB_Polygon polygonFromArray(final double[][] points) {
		List<WB_Coord> coords = new FastList<WB_Coord>();
		for (double[] p : points) {
			if (p.length == 2) {
				coords.add(new WB_Point(p[0], p[1]));
			} else if (p.length == 3) {
				coords.add(new WB_Point(p[0], p[1], p[2]));
			}
		}
		return new WB_Polygon(coords);
	}

	WB_Plane planeFromArray(final double[][] points) {
		WB_Point p0 = new WB_Point(points[0]);
		WB_Point p1 = new WB_Point(points[1]);
		WB_Point p2 = p0.add(0, 0, 100);
		return new WB_Plane(p0, p1, p2);
	}

	WB_Line lineFromArray(final double[][] points) {
		WB_Point p0 = new WB_Point(points[0]);
		WB_Point p1 = new WB_Point(points[1]);
		return gf.createLineThroughPoints(p0, p1);
	}
}
