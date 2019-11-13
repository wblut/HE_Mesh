/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import static wblut.geom.WB_Classification.BACK;
import static wblut.geom.WB_Classification.FRONT;
import static wblut.geom.WB_Classification.ON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.math.WB_Epsilon;

/**
 * @author FVH
 *
 *         Port of David Geier's algorithm
 *         https://geidav.wordpress.com/2015/03/21/splitting-an-arbitrary-
 *         polygon-by-a-line/
 *
 */
public class WB_PolygonSplitter {
	List<PolyEdge> splitPoly;
	List<PolyEdge> edgesOnLine;
	WB_GeometryFactory gf = new WB_GeometryFactory();

	// Split a convex or concave polygon without holes with a given line
	public List<WB_Polygon> splitSimplePolygon2D(final WB_Polygon polygon, final WB_Line L) {
		List<WB_Polygon> polys = new ArrayList<WB_Polygon>();
		List<WB_Coord> coords = polygon.getPoints().toList();
		splitEdges(coords, L);
		sortEdges(L);
		splitPolygon();
		polys.addAll(collectPolygons());

		return polys;
	}

	// Split a convex or concave polygon without holes with a given plane
	public List<WB_Polygon> splitSimplePolygon3D(final WB_Polygon polygon, final WB_Plane P) {
		List<WB_Polygon> polys = new ArrayList<WB_Polygon>();
		WB_Plane Q = polygon.getPlane();
		WB_IntersectionResult intersection = WB_GeometryOp.getIntersection3D(P, Q);
		if (!intersection.intersection) {
			polys.add(polygon);
		} else {

			List<WB_Coord> coords = polygon.getPoints().toList();
			splitEdges(coords, P);
			sortEdges((WB_Line) intersection.object);
			splitPolygon();
			polys.addAll(collectPolygons());
		}
		return polys;
	}

	public List<WB_Polygon> splitPolygon2D(final WB_Polygon polygon, final WB_Line L) {
		List<WB_Polygon> polys = new ArrayList<WB_Polygon>();
		WB_Polygon spolygon = gf.createSimplePolygon(polygon);
		List<WB_Coord> coords = spolygon.getPoints().toList();
		splitEdges(coords, L);
		sortEdges(L);
		splitPolygon();
		polys.addAll(collectPolygons());

		return polys;
	}

	// Split a convex or concave polygon without holes with a given plane
	public List<WB_Polygon> splitPolygon3D(final WB_Polygon polygon, final WB_Plane P) {
		WB_Polygon spolygon = gf.createSimplePolygon(polygon);
		List<WB_Polygon> polys = new ArrayList<WB_Polygon>();
		WB_Plane Q = spolygon.getPlane();
		WB_IntersectionResult intersection = WB_GeometryOp.getIntersection3D(P, Q);
		if (!intersection.intersection) {
			polys.add(spolygon);
		} else {

			List<WB_Coord> coords = spolygon.getPoints().toList();
			splitEdges(coords, P);
			sortEdges((WB_Line) intersection.object);
			splitPolygon();
			polys.addAll(collectPolygons());
		}
		return polys;
	}

	void splitEdges(final List<? extends WB_Coord> coords, final WB_Plane P) {
		splitPoly = new FastList<PolyEdge>();
		edgesOnLine = new FastList<PolyEdge>();

		for (int i = 0; i < coords.size(); i++) {
			WB_Segment edge = new WB_Segment(coords.get(i), coords.get((i + 1) % coords.size()));
			WB_Classification edgeStartSide = WB_GeometryOp.classifyPointToPlane3D(P, edge.getOrigin());
			WB_Classification edgeEndSide = WB_GeometryOp.classifyPointToPlane3D(P, edge.getEndpoint());
			splitPoly.add(new PolyEdge(coords.get(i), edgeStartSide));

			if (edgeStartSide == ON) {
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			} else if (edgeStartSide != edgeEndSide && edgeEndSide != ON) {
				WB_IntersectionResult intersection = WB_GeometryOp.getIntersection3D(edge, P);
				assert intersection.intersection;
				splitPoly.add(new PolyEdge((WB_Coord) intersection.object, ON));
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			}
		}

		for (int i = 0, j = splitPoly.size() - 1; i < splitPoly.size(); j = i, i++) {
			splitPoly.get(j).next = splitPoly.get(i);
			splitPoly.get(i).prev = splitPoly.get(j);

		}

	}

	void splitEdges(final List<? extends WB_Coord> coords, final WB_Line L) {
		splitPoly = new FastList<PolyEdge>();
		edgesOnLine = new FastList<PolyEdge>();

		for (int i = 0; i < coords.size(); i++) {
			WB_Segment edge = new WB_Segment(coords.get(i), coords.get((i + 1) % coords.size()));
			WB_Classification edgeStartSide = WB_GeometryOp.classifyPointToLine2D(edge.getOrigin(), L);
			WB_Classification edgeEndSide = WB_GeometryOp.classifyPointToLine2D(edge.getEndpoint(), L);
			splitPoly.add(new PolyEdge(coords.get(i), edgeStartSide));

			if (edgeStartSide == ON) {
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			} else if (edgeStartSide != edgeEndSide && edgeEndSide != ON) {
				WB_IntersectionResult intersection = WB_GeometryOp.getClosestPoint2D(L, edge);
				assert intersection.intersection;
				splitPoly.add(new PolyEdge((WB_Coord) intersection.object, ON));
				edgesOnLine.add(splitPoly.get(splitPoly.size() - 1));
			}
		}

		for (int i = 0, j = splitPoly.size() - 1; i < splitPoly.size(); j = i, i++) {
			splitPoly.get(j).next = splitPoly.get(i);
			splitPoly.get(i).prev = splitPoly.get(j);

		}

	}

	void sortEdges(final WB_Line L) {
		Collections.sort(edgesOnLine, new EdgeOnLineComparator(L));
		for (int i = 1; i < edgesOnLine.size(); i++) {
			edgesOnLine.get(i).distOnLine = WB_Point.getDistance3D(edgesOnLine.get(i).pos, edgesOnLine.get(0).pos);
		}
		
	}

	void splitPolygon() {
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
						|| prevSide == FRONT && nextSide == ON && curEdge.next.distOnLine < curEdge.distOnLine
						|| prevSide == ON && nextSide == BACK && curEdge.prev.distOnLine < curEdge.distOnLine) {
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

	List<WB_Polygon> collectPolygons() {
		List<WB_Polygon> resPolys = new ArrayList<WB_Polygon>();
		for (PolyEdge e : splitPoly) {
			if (!e.visited) {
				List<WB_Coord> splitCoords = new FastList<WB_Coord>();
				PolyEdge curEdge = e;
				do {
					curEdge.visited = true;
					splitCoords.add(curEdge.pos);
					curEdge = curEdge.next;
				} while (curEdge != e);

				WB_Polygon poly = new WB_Polygon(splitCoords);
				resPolys.add(poly);
			}
		}

		return resPolys;
	}

	static class PolyEdge {
		WB_Coord pos; // start position on edge
		WB_Classification side; // start position's side of split line
		PolyEdge next; // next polygon in linked list
		PolyEdge prev; // previous polygon in linked list
		double distOnLine; // distance relative to first point on split line
		boolean isSource; // for visualization
		boolean isDestination; // for visualization
		boolean visited; // for collecting split polygons

		PolyEdge(final WB_Coord pos, final WB_Classification side) {
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

	public static void main(final String[] args) {
WB_PolygonSplitter ps=new WB_PolygonSplitter();
		WB_Polygon poly0 = ps.polygonFromArray(new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 } });
		WB_Polygon poly1 = ps.polygonFromArray(
				new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 }, { 0, 0 } });
		WB_Polygon poly2 = ps.polygonFromArray(new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 },
				{ 15, 50 }, { 15, 25 }, { -15, 25 }, { -15, 50 } });
		WB_Polygon poly3 = ps.polygonFromArray(new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 },
				{ 15, 50 }, { 15, 25 }, { 0, 40 }, { -15, 25 }, { -15, 50 } });
		WB_Polygon poly4 = ps.polygonFromArray(new double[][] { { -40, 50 }, { -50, 40 }, { -50, -50 }, { 50, -50 },
				{ 50, 50 }, { 15, 50 }, { 15, 15 }, { -30, 15 }, { -30, 40 } });
		WB_Polygon poly5 = ps.polygonFromArray(new double[][] { { -40, 40 }, { -50, 30 }, { -50, -50 }, { 50, -50 },
				{ 50, 50 }, { 15, 50 }, { 15, 15 }, { -30, 15 }, { -30, 30 } });
		WB_Polygon poly6 = ps.polygonFromArray(
				new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 }, { 0, 50 } });
		WB_Polygon poly7 = ps.polygonFromArray(new double[][] { { -50, 50 }, { -50, -50 }, { 50, -50 }, { 50, 50 },
				{ 25, 50 }, { 25, 15 }, { 10, 40 }, { -10, 40 }, { -25, 15 }, { -25, 50 } });

		class TestCase2D {
			WB_Polygon polygon;
			WB_Line L;

			TestCase2D(final WB_Polygon polygon, final double[][] line) {
				this.polygon = polygon;
				this.L = ps.lineFromArray(line);
			}

		}
		List<TestCase2D> cases2D = new FastList<TestCase2D>();
		cases2D.add(new TestCase2D(poly1, new double[][] { { -60, 0 }, { 55, 0 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { 60, 0 }, { -60, 0 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { -60, 20 }, { 60, 20 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { 60, 50 }, { -60, 50 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { -60, 60 }, { 60, -60 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { 55, -55 }, { -55, 55 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { -55, -55 }, { 55, 55 } }));
		cases2D.add(new TestCase2D(poly1, new double[][] { { 55, 55 }, { -55, -55 } }));

		cases2D.add(new TestCase2D(poly2, new double[][] { { -60, 25 }, { 60, 25 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { 60, 25 }, { -55, 25 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { -60, 50 }, { 60, 50 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { 60, 50 }, { -55, 50 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { 15, 60 }, { 15, -60 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { 15, -60 }, { 15, 60 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { -55, -15 }, { 25, 60 } }));
		cases2D.add(new TestCase2D(poly2, new double[][] { { 25, 60 }, { -55, -15 } }));

		cases2D.add(new TestCase2D(poly0, new double[][] { { -60, 0 }, { 60, 0 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { 60, 50 }, { -60, 50 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { -60, -50 }, { 55, -50 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { 60, -50 }, { -60, -50 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { -60, 60 }, { 60, -60 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { 60, -60 }, { -60, 60 } }));
		cases2D.add(new TestCase2D(poly0, new double[][] { { -60, 40 }, { -40, 60 } }));

		cases2D.add(new TestCase2D(poly7, new double[][] { { 55, 40 }, { -60, 40 } }));
		cases2D.add(new TestCase2D(poly7, new double[][] { { -55, 40 }, { 60, 40 } }));
		cases2D.add(new TestCase2D(poly7, new double[][] { { 55, 45 }, { -60, 45 } }));
		cases2D.add(new TestCase2D(poly7, new double[][] { { -55, 45 }, { 60, 45 } }));
		cases2D.add(new TestCase2D(poly7, new double[][] { { -50, 60 }, { 55, -55 } }));
		cases2D.add(new TestCase2D(poly7, new double[][] { { 55, -55 }, { -50, 60 } }));

		cases2D.add(new TestCase2D(poly3, new double[][] { { -60, 40 }, { 60, 40 } }));
		cases2D.add(new TestCase2D(poly3, new double[][] { { 60, 40 }, { -55, 40 } }));
		cases2D.add(new TestCase2D(poly3, new double[][] { { -60, 25 }, { 60, 25 } }));
		cases2D.add(new TestCase2D(poly3, new double[][] { { 60, 25 }, { -55, 25 } }));

		cases2D.add(new TestCase2D(poly4, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases2D.add(new TestCase2D(poly4, new double[][] { { 60, 50 }, { -55, 50 } }));

		cases2D.add(new TestCase2D(poly5, new double[][] { { -60, 40 }, { 55, 40 } }));
		cases2D.add(new TestCase2D(poly5, new double[][] { { 60, 40 }, { -55, 40 } }));

		cases2D.add(new TestCase2D(poly6, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases2D.add(new TestCase2D(poly6, new double[][] { { 60, 50 }, { -60, 50 } }));
		int counter = 1;
		for (TestCase2D testCase : cases2D) {
			System.out
					.println("2D case " + counter++ + ": " + ps.splitSimplePolygon2D(testCase.polygon, testCase.L).size());

		}

		class TestCase3D {
			WB_Polygon polygon;
			WB_Plane P;

			TestCase3D(final WB_Polygon polygon, final double[][] plane) {
				this.polygon = polygon;
				this.P = ps.planeFromArray(plane);
			}

		}
		List<TestCase3D> cases3D = new FastList<TestCase3D>();
		cases3D.add(new TestCase3D(poly1, new double[][] { { -60, 0 }, { 55, 0 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { 60, 0 }, { -60, 0 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { -60, 20 }, { 60, 20 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { 60, 50 }, { -60, 50 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { -60, 60 }, { 60, -60 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { 55, -55 }, { -55, 55 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { -55, -55 }, { 55, 55 } }));
		cases3D.add(new TestCase3D(poly1, new double[][] { { 55, 55 }, { -55, -55 } }));

		cases3D.add(new TestCase3D(poly2, new double[][] { { -60, 25 }, { 60, 25 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { 60, 25 }, { -55, 25 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { -60, 50 }, { 60, 50 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { 60, 50 }, { -55, 50 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { 15, 60 }, { 15, -60 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { 15, -60 }, { 15, 60 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { -55, -15 }, { 25, 60 } }));
		cases3D.add(new TestCase3D(poly2, new double[][] { { 25, 60 }, { -55, -15 } }));

		cases3D.add(new TestCase3D(poly0, new double[][] { { -60, 0 }, { 60, 0 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { 60, 50 }, { -60, 50 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { -60, -50 }, { 55, -50 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { 60, -50 }, { -60, -50 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { -60, 60 }, { 60, -60 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { 60, -60 }, { -60, 60 } }));
		cases3D.add(new TestCase3D(poly0, new double[][] { { -60, 40 }, { -40, 60 } }));

		cases3D.add(new TestCase3D(poly7, new double[][] { { 55, 40 }, { -60, 40 } }));
		cases3D.add(new TestCase3D(poly7, new double[][] { { -55, 40 }, { 60, 40 } }));
		cases3D.add(new TestCase3D(poly7, new double[][] { { 55, 45 }, { -60, 45 } }));
		cases3D.add(new TestCase3D(poly7, new double[][] { { -55, 45 }, { 60, 45 } }));
		cases3D.add(new TestCase3D(poly7, new double[][] { { -50, 60 }, { 55, -55 } }));
		cases3D.add(new TestCase3D(poly7, new double[][] { { 55, -55 }, { -50, 60 } }));

		cases3D.add(new TestCase3D(poly3, new double[][] { { -60, 40 }, { 60, 40 } }));
		cases3D.add(new TestCase3D(poly3, new double[][] { { 60, 40 }, { -55, 40 } }));
		cases3D.add(new TestCase3D(poly3, new double[][] { { -60, 25 }, { 60, 25 } }));
		cases3D.add(new TestCase3D(poly3, new double[][] { { 60, 25 }, { -55, 25 } }));

		cases3D.add(new TestCase3D(poly4, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases3D.add(new TestCase3D(poly4, new double[][] { { 60, 50 }, { -55, 50 } }));

		cases3D.add(new TestCase3D(poly5, new double[][] { { -60, 40 }, { 55, 40 } }));
		cases3D.add(new TestCase3D(poly5, new double[][] { { 60, 40 }, { -55, 40 } }));

		cases3D.add(new TestCase3D(poly6, new double[][] { { -60, 50 }, { 55, 50 } }));
		cases3D.add(new TestCase3D(poly6, new double[][] { { 60, 50 }, { -60, 50 } }));

		for (TestCase3D testCase : cases3D) {
			System.out
					.println("3D case " + counter++ + ": " + ps.splitSimplePolygon3D(testCase.polygon, testCase.P).size());

		}

	}

}
