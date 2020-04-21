package wblut.hemesh;

import static wblut.geom.WB_Classification.BACK;
import static wblut.geom.WB_Classification.FRONT;
import static wblut.geom.WB_Classification.ON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_IntersectionResult;
import wblut.geom.WB_Line;
import wblut.geom.WB_List;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

class HET_FaceSplitter {
	List<PolyEdge> splitPoly;
	List<PolyEdge> edgesOnLine;
	private final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	HET_FaceSplitter() {
	}

	// Divide a face whose edges have been split before by a plane
	List<HE_Vertex[]> splitFace(final HE_Face f, final WB_Plane P) {
		final List<HE_Vertex[]> subfaces = new ArrayList<>();
		final WB_Plane Q = HE_MeshOp.getPlane(f);
		final WB_IntersectionResult intersection = WB_GeometryOp3D.getIntersection3D(P, Q);
		if (!intersection.intersection) {
			return null;
		} else {
			final List<HE_Vertex> coords = f.getFaceVertices();
			processEdges(coords, P);
			sortEdges((WB_Line) intersection.object);
			splitPolygon();
			subfaces.addAll(collectPolygons());
		}
		return subfaces;
	}

	void processEdges(final List<HE_Vertex> coords, final WB_Plane P) {
		splitPoly = new WB_List<>();
		edgesOnLine = new WB_List<>();
		for (final HE_Vertex coord : coords) {
			final WB_Classification edgeStartSide = WB_GeometryOp3D.classifyPointToPlane3D(coord, P);
			splitPoly.add(new PolyEdge(coord, edgeStartSide));
			if (WB_GeometryOp3D.classifyPointToPlane3D(coord, P) == ON) {
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			}
			// all split are dealt with before calling HET_FaceSplitter
		}
		for (int i = 0, j = splitPoly.size() - 1; i < splitPoly.size(); j = i, i++) {
			splitPoly.get(j).next = splitPoly.get(i);
			splitPoly.get(i).prev = splitPoly.get(j);
		}
	}

	void sortEdges(final WB_Line L) {
		Collections.sort(edgesOnLine, new EdgeOnLineComparator(L));
		for (int i = 1; i < edgesOnLine.size(); i++) {
			edgesOnLine.get(i).distOnLine = WB_Vector.getDistance3D(edgesOnLine.get(i).pos, edgesOnLine.get(0).pos);
		}
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
					final PolyEdge curEdge = edgesOnLine.get(i);
					final WB_Classification curSide = curEdge.side;
					final WB_Classification prevSide = curEdge.prev.side;
					final WB_Classification nextSide = curEdge.next.side;
					assert curSide == ON;
					if (prevSide == FRONT && nextSide == BACK
							|| prevSide == FRONT && nextSide == ON && curEdge.next.distOnLine < curEdge.distOnLine
							|| prevSide == ON && nextSide == BACK && curEdge.prev.distOnLine < curEdge.distOnLine) {
						srcEdge = curEdge;
						srcEdge.isSource = true;
					}
				}
				// find destination
				PolyEdge dstEdge = null;
				for (; dstEdge == null && i < edgesOnLine.size();) {
					final PolyEdge curEdge = edgesOnLine.get(i);
					final WB_Classification curSide = curEdge.side;
					final WB_Classification prevSide = curEdge.prev.side;
					final WB_Classification nextSide = curEdge.next.side;
					assert curSide == ON;
					if (prevSide == BACK && nextSide == FRONT || prevSide == ON && nextSide == FRONT
							|| prevSide == BACK && nextSide == ON || prevSide == BACK && nextSide == BACK
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
		final PolyEdge srcToDst = new PolyEdge(srcEdge.pos, srcEdge.side);
		final PolyEdge dstToSrc = new PolyEdge(dstEdge.pos, dstEdge.side);
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
		for (final PolyEdge edge : splitPoly) {
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
		final List<HE_Vertex[]> resPolys = new ArrayList<>();
		for (final PolyEdge e : splitPoly) {
			if (!e.visited) {
				final List<PolyEdge> edges = new WB_List<>();
				PolyEdge curEdge = e;
				do {
					curEdge.visited = true;
					edges.add(curEdge);
					curEdge = curEdge.next;
				} while (curEdge != e);
				if (edges.size() > 2) {
					final HE_Vertex[] poly = new HE_Vertex[edges.size()];
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
		HE_Vertex pos; // start position on edge
		WB_Classification side; // start position's side of split
								// line
		PolyEdge next; // next polygon in linked list
		PolyEdge prev; // previous polygon in linked list
		double distOnLine; // distance relative to first point
							// on split line
		boolean isSource; // for visualization
		boolean isDestination; // for visualization
		boolean visited; // for collecting split polygons

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
			final double d = WB_GeometryOp3D.getParameterOfPointOnLine3D(e0.pos, L)
					- WB_GeometryOp3D.getParameterOfPointOnLine3D(e1.pos, L);
			return WB_Epsilon.isZero(d) ? 0 : d > 0 ? 1 : -1;
		}
	}

	WB_Polygon polygonFromArray(final double[][] points) {
		final List<WB_Coord> coords = new WB_CoordList();
		for (final double[] p : points) {
			if (p.length == 2) {
				coords.add(new WB_Point(p[0], p[1]));
			} else if (p.length == 3) {
				coords.add(new WB_Point(p[0], p[1], p[2]));
			}
		}
		return new WB_Polygon(coords);
	}

	WB_Plane planeFromArray(final double[][] points) {
		final WB_Point p0 = new WB_Point(points[0]);
		final WB_Point p1 = new WB_Point(points[1]);
		final WB_Point p2 = p0.add(0, 0, 100);
		return new WB_Plane(p0, p1, p2);
	}

	WB_Line lineFromArray(final double[][] points) {
		final WB_Point p0 = new WB_Point(points[0]);
		final WB_Point p1 = new WB_Point(points[1]);
		return gf.createLineThroughPoints(p0, p1);
	}
}
