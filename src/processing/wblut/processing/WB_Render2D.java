package wblut.processing;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import wblut.geom.WB_AABB2D;
import wblut.geom.WB_AABBTree2D;
import wblut.geom.WB_AABBTree2D.WB_AABBNode2D;
import wblut.geom.WB_AlphaTriangulation2D;
import wblut.geom.WB_BinaryGrid2D;
import wblut.geom.WB_Circle;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Hexagon;
import wblut.geom.WB_IndexedAABBTree2D;
import wblut.geom.WB_IndexedAABBTree2D.WB_IndexedAABBNode2D;
import wblut.geom.WB_Line;
import wblut.geom.WB_Map2D;
import wblut.geom.WB_Octagon;
import wblut.geom.WB_Pentagon;
import wblut.geom.WB_Point;
import wblut.geom.WB_PolyLine;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Quad;
import wblut.geom.WB_Ray;
import wblut.geom.WB_Ring;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Triangle;
import wblut.geom.WB_TriangleFactory;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation2DWithPoints;

public class WB_Render2D extends WB_Processing {
	protected WB_Render2D() {
		super();
	}

	public WB_Render2D(final PApplet home) {
		super(home);
		if (home.g == null) {
			throw new IllegalArgumentException("WB_Render3D can only be used after size()");
		}
	}

	public WB_Render2D(final PGraphics home) {
		this.home = home;
	}

	public void vertex2D(final WB_Coord p) {
		home.vertex(p.xf(), p.yf());
	}

	public void vertex2D(final double x, final double y) {
		home.vertex((float) x, (float) y);
	}

	public void drawPoint2D(final WB_Coord p) {
		home.point(p.xf(), p.yf());
	}

	public void drawPoint2D(final WB_Coord p, final double r) {
		home.ellipse(p.xf(), p.yf(), 2 * (float) r, 2 * (float) r);
	}

	public void drawPoint2D(final Collection<? extends WB_Coord> points) {
		for (final WB_Coord p : points) {
			drawPoint2D(p);
		}
	}

	public void drawPoint2D(final WB_Coord[] points) {
		for (final WB_Coord p : points) {
			drawPoint2D(p);
		}
	}

	public void drawPoint2D(final Collection<? extends WB_Coord> points, final double r) {
		for (final WB_Coord p : points) {
			drawPoint2D(p, r);
		}
	}

	public void drawPoint2D(final WB_Coord[] points, final double r) {
		for (final WB_Coord p : points) {
			drawPoint2D(p, r);
		}
	}

	public void drawVector2D(final WB_Coord p, final WB_Coord v, final double r) {
		home.pushMatrix();
		home.translate(p.xf(), p.yf());
		home.line(0f, 0f, (float) (r * v.xd()), (float) (r * v.yd()));
		home.popMatrix();
	}

	public void drawLine2D(final WB_Line L, final double d) {
		home.line((float) (L.getOrigin().xd() - d * L.getDirection().xd()),
				(float) (L.getOrigin().yd() - d * L.getDirection().yd()),
				(float) (L.getOrigin().xd() + d * L.getDirection().xd()),
				(float) (L.getOrigin().yd() + d * L.getDirection().yd()));
	}

	public void drawRay2D(final WB_Ray R, final double d) {
		home.line((float) R.getOrigin().xd(), (float) R.getOrigin().yd(),
				(float) (R.getOrigin().xd() + d * R.getDirection().xd()),
				(float) (R.getOrigin().yd() + d * R.getDirection().yd()));
	}

	public void drawSegment2D(final WB_Coord p, final WB_Coord q) {
		home.line((float) p.xd(), (float) p.yd(), (float) q.xd(), (float) q.yd());
	}

	public void drawPolyLine2D(final WB_PolyLine P) {
		for (int i = 0; i < P.getNumberOfPoints() - 1; i++) {
			home.line((float) P.getPoint(i).xd(), (float) P.getPoint(i).yd(), (float) P.getPoint(i + 1).xd(),
					(float) P.getPoint(i + 1).yd());
		}
	}

	public void drawRing2D(final WB_Ring P) {
		for (int i = 0, j = P.getNumberOfPoints() - 1; i < P.getNumberOfPoints(); j = i++) {
			home.line((float) P.getPoint(j).xd(), (float) P.getPoint(j).yd(), (float) P.getPoint(i).xd(),
					(float) P.getPoint(i).yd());
		}
	}

	public void drawPolygon2D(final WB_Polygon P) {
		final int[] tris = P.getTriangles();
		for (int i = 0; i < tris.length; i += 3) {
			drawTriangle2D(P.getPoint(tris[i]), P.getPoint(tris[i + 1]), P.getPoint(tris[i + 2]));
		}
	}

	public void drawPolygon2D(final WB_Polygon[] P) {
		for (final WB_Polygon poly : P) {
			drawPolygon2D(poly);
		}
	}

	public void drawPolygon2D(final Collection<? extends WB_Polygon> P) {
		for (final WB_Polygon poly : P) {
			drawPolygon2D(poly);
		}
	}

	public void drawPolygonEdges2D(final WB_Polygon P) {
		final int[] npc = P.getNumberOfPointsPerContour();
		int index = 0;
		for (int i = 0; i < P.getNumberOfContours(); i++) {
			home.beginShape();
			for (int j = 0; j < npc[i]; j++) {
				vertex2D(P.getPoint(index++));
			}
			home.endShape(PConstants.CLOSE);
		}
	}

	public void drawPolygonEdges2D(final WB_Polygon[] P) {
		for (final WB_Polygon poly : P) {
			drawPolygon2D(poly);
		}
	}

	public void drawPolygonEdges2D(final Collection<? extends WB_Polygon> P) {
		for (final WB_Polygon poly : P) {
			drawPolygon2D(poly);
		}
	}

	public void drawCircle2D(final WB_Circle C) {
		home.ellipse((float) C.getCenter().xd(), (float) C.getCenter().yd(), 2 * (float) C.getRadius(),
				2 * (float) C.getRadius());
	}

	public void drawTriangle2D(final WB_Triangle T) {
		home.beginShape(PConstants.TRIANGLES);
		vertex2D(T.p1());
		vertex2D(T.p2());
		vertex2D(T.p3());
		home.endShape();
	}

	public void drawTriangle2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		home.beginShape(PConstants.TRIANGLES);
		vertex2D(p1);
		vertex2D(p2);
		vertex2D(p3);
		home.endShape();
	}

	public void drawTriangle2D(final int[] tri, final List<? extends WB_Coord> points) {
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertex2D(points.get(tri[i]));
			vertex2D(points.get(tri[i + 1]));
			vertex2D(points.get(tri[i + 2]));
			home.endShape();
		}
	}

	public void drawTriangle2D(final int[] tri, final WB_Coord[] points) {
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertex2D(points[tri[i]]);
			vertex2D(points[tri[i + 1]]);
			vertex2D(points[tri[i + 2]]);
			home.endShape();
		}
	}

	public void drawTriangle2D(final int[] tri, final WB_CoordCollection points) {
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertex2D(points.get(tri[i]));
			vertex2D(points.get(tri[i + 1]));
			vertex2D(points.get(tri[i + 2]));
			home.endShape();
		}
	}

	public void drawTriangulation2D(final WB_Triangulation2D tri, final List<? extends WB_Coord> points) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertex2D(points.get(triangles[i]));
			vertex2D(points.get(triangles[i + 1]));
			vertex2D(points.get(triangles[i + 2]));
		}
		home.endShape();
	}

	public void drawTriangulation2D(final WB_Triangulation2D tri, final WB_CoordCollection points) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertex2D(points.get(triangles[i]));
			vertex2D(points.get(triangles[i + 1]));
			vertex2D(points.get(triangles[i + 2]));
		}
		home.endShape();
	}

	public void drawTriangulationEdges2D(final WB_Triangulation2D tri, final List<? extends WB_Coord> points) {
		final int[] edges = tri.getEdges();
		for (int i = 0; i < edges.length; i += 2) {
			drawSegment2D(points.get(edges[i]), points.get(edges[i + 1]));
		}
	}

	public void drawTriangulation2D(final WB_Triangulation2D tri, final WB_Coord[] points) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertex2D(points[triangles[i]]);
			vertex2D(points[triangles[i + 1]]);
			vertex2D(points[triangles[i + 2]]);
		}
		home.endShape();
	}

	public void drawTriangulationEdges2D(final WB_Triangulation2D tri, final WB_Coord[] points) {
		final int[] edges = tri.getEdges();
		for (int i = 0; i < edges.length; i += 2) {
			drawSegment2D(points[edges[i]], points[edges[i + 1]]);
		}
	}

	public void drawTriangle2D(final Collection<? extends WB_Triangle> triangles) {
		final Iterator<? extends WB_Triangle> triItr = triangles.iterator();
		while (triItr.hasNext()) {
			drawTriangle2D(triItr.next());
		}
	}

	public void drawTriangle2D(final WB_Triangle[] triangles) {
		for (final WB_Triangle triangle : triangles) {
			drawTriangle2D(triangle);
		}
	}

	public void drawTriangle2DEdges(final Collection<? extends WB_Triangle> triangles) {
		final Iterator<? extends WB_Triangle> triItr = triangles.iterator();
		while (triItr.hasNext()) {
			drawTriangle2DEdges(triItr.next());
		}
	}

	public void drawTriangle2DEdges(final WB_Triangle triangle) {
		line2D(triangle.p1(), triangle.p2());
		line2D(triangle.p3(), triangle.p2());
		line2D(triangle.p1(), triangle.p3());
	}

	public void drawTriangle2DEdges(final WB_Triangle[] triangles) {
		for (final WB_Triangle triangle : triangles) {
			drawTriangle2DEdges(triangle);
		}
	}

	public void drawSegment2D(final Collection<? extends WB_Segment> segments) {
		final Iterator<? extends WB_Segment> segItr = segments.iterator();
		while (segItr.hasNext()) {
			drawSegment2D(segItr.next());
		}
	}

	public void drawSegment2D(final WB_Segment segment) {
		line2D(segment.getOrigin(), segment.getEndpoint());
	}

	public void drawSegment2D(final WB_Segment[] segments) {
		for (final WB_Segment segment : segments) {
			drawSegment2D(segment);
		}
	}

	public void drawSegment2D(final int[] segs, final List<? extends WB_Coord> coords) {
		for (int i = 0; i < segs.length; i += 2) {
			line2D(coords.get(segs[i]), coords.get(segs[i + 1]));
		}
	}

	public void translate2D(final WB_Coord p) {
		home.translate(p.xf(), p.yf());
	}

	private void line2D(final WB_Coord p, final WB_Coord q) {
		home.beginShape(PConstants.LINES);
		vertex2D(p);
		vertex2D(q);
		home.endShape();
	}

	public void line2D(final double x1, final double y1, final double x2, final double y2) {
		home.beginShape(PConstants.LINES);
		vertex2D(x1, y1);
		vertex2D(x2, y2);
		home.endShape();
	}

	public void drawAABB2D(final WB_AABB2D AABB) {
		home.rect((float) AABB.getMinX(), (float) AABB.getMinY(), (float) AABB.getWidth(), (float) AABB.getHeight());
	}

	public void drawAABBNode2D(final WB_AABBNode2D node) {
		drawAABB2D(node.getAABB());
		if (node.getChildA() != null) {
			drawAABBNode2D(node.getChildA());
		}
		if (node.getChildB() != null) {
			drawAABBNode2D(node.getChildB());
		}
	}

	private void drawAABBNode2D(final WB_AABBNode2D node, final int level) {
		if (node.getLevel() == level) {
			drawAABB2D(node.getAABB());
		}
		if (node.getLevel() < level) {
			if (node.getChildA() != null) {
				drawAABBNode2D(node.getChildA(), level);
			}
			if (node.getChildB() != null) {
				drawAABBNode2D(node.getChildB(), level);
			}
		}
	}

	private void drawAABBLeafNode2D(final WB_AABBNode2D node) {
		if (node.isLeaf()) {
			drawAABB2D(node.getAABB());
		} else {
			if (node.getChildA() != null) {
				drawAABBLeafNode2D(node.getChildA());
			}
			if (node.getChildB() != null) {
				drawAABBLeafNode2D(node.getChildB());
			}
		}
	}

	public void drawAABBLeafNodes2D(final WB_AABBTree2D tree) {
		drawAABBLeafNode2D(tree.getRoot());
	}

	public void drawAABBTree2D(final WB_AABBTree2D tree) {
		drawAABBNode2D(tree.getRoot());
	}

	public void drawAABBTree2D(final WB_AABBTree2D tree, final int level) {
		drawAABBNode2D(tree.getRoot(), level);
	}

	public void drawAABBNode2D(final WB_IndexedAABBNode2D node) {
		drawAABB2D(node.getAABB());
		if (node.getChildA() != null) {
			drawAABBNode2D(node.getChildA());
		}
		if (node.getChildB() != null) {
			drawAABBNode2D(node.getChildB());
		}
	}

	private void drawAABBNode2D(final WB_IndexedAABBNode2D node, final int level) {
		if (node.getLevel() == level) {
			drawAABB2D(node.getAABB());
		}
		if (node.getLevel() < level) {
			if (node.getChildA() != null) {
				drawAABBNode2D(node.getChildA(), level);
			}
			if (node.getChildB() != null) {
				drawAABBNode2D(node.getChildB(), level);
			}
		}
	}

	private void drawAABBLeafNode2D(final WB_IndexedAABBNode2D node) {
		if (node.isLeaf()) {
			drawAABB2D(node.getAABB());
		} else {
			if (node.getChildA() != null) {
				drawAABBLeafNode2D(node.getChildA());
			}
			if (node.getChildB() != null) {
				drawAABBLeafNode2D(node.getChildB());
			}
		}
	}

	public void drawAABBLeafNodes2D(final WB_IndexedAABBTree2D tree) {
		drawAABBLeafNode2D(tree.getRoot());
	}

	public void drawAABBTree2D(final WB_IndexedAABBTree2D tree) {
		drawAABBNode2D(tree.getRoot());
	}

	public void drawAABBTree2D(final WB_IndexedAABBTree2D tree, final int level) {
		drawAABBNode2D(tree.getRoot(), level);
	}

	public void drawTriangulation2D(final WB_AlphaTriangulation2D tri) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertex2D(tri.getPoints().get(triangles[i]));
			vertex2D(tri.getPoints().get(triangles[i + 1]));
			vertex2D(tri.getPoints().get(triangles[i + 2]));
		}
		home.endShape();
	}

	public void drawTriangulation2D(final WB_AlphaTriangulation2D tri, final double alpha) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			if (tri.getAlpha()[i / 3] <= alpha) {
				vertex2D(tri.getPoints().get(triangles[i]));
				vertex2D(tri.getPoints().get(triangles[i + 1]));
				vertex2D(tri.getPoints().get(triangles[i + 2]));
			}
		}
		home.endShape();
	}

	public void drawTriangulationEdges2D(final WB_AlphaTriangulation2D tri) {
		final int[] edges = tri.getEdges();
		for (int i = 0; i < edges.length; i += 2) {
			drawSegment2D(tri.getPoints().get(edges[i]), tri.getPoints().get(edges[i + 1]));
		}
	}

	public void drawTriangulation2D(final WB_Triangulation2DWithPoints tri) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertex2D(tri.getPoints().get(triangles[i]));
			vertex2D(tri.getPoints().get(triangles[i + 1]));
			vertex2D(tri.getPoints().get(triangles[i + 2]));
		}
		home.endShape();
	}

	public void drawTriangulationEdges2D(final WB_Triangulation2DWithPoints tri) {
		final int[] edges = tri.getEdges();
		for (int i = 0; i < edges.length; i += 2) {
			drawSegment2D(tri.getPoints().get(edges[i]), tri.getPoints().get(edges[i + 1]));
		}
	}

	public void drawLineMapped(final WB_Line L, final double d, final WB_Map2D map) {
		drawSegmentMapped(WB_Point.addMul(L.getOrigin(), -d, L.getDirection()),
				WB_Point.addMul(L.getOrigin(), d, L.getDirection()), map);
	}

	public void drawPointMapped(final Collection<? extends WB_Coord> points, final double r, final WB_Map2D map) {
		for (final WB_Coord p : points) {
			drawPointMapped(p, r, map);
		}
	}

	public void drawPointMapped(final Collection<? extends WB_Coord> points, final WB_Map2D map) {
		for (final WB_Coord p : points) {
			drawPointMapped(p, map);
		}
	}

	public void drawPointMapped(final WB_Coord p, final double r, final WB_Map2D map) {
		final WB_Point q = new WB_Point();
		map.mapPoint3D(p, q);
		drawPoint2D(q, r);
	}

	public void drawPointMapped(final WB_Coord p, final WB_Map2D map) {
		final WB_Point q = new WB_Point();
		map.mapPoint3D(p, q);
		drawPoint2D(q);
	}

	public void drawPointMapped(final WB_Coord[] points, final double r, final WB_Map2D map) {
		for (final WB_Coord p : points) {
			drawPointMapped(p, r, map);
		}
	}

	public void drawPointMapped(final WB_Coord[] points, final WB_Map2D map) {
		for (final WB_Coord p : points) {
			drawPointMapped(p, map);
		}
	}

	public void drawPolygonEdgesMapped(final WB_Polygon P, final WB_Map2D map) {
		final int[] npc = P.getNumberOfPointsPerContour();
		int index = 0;
		for (int i = 0; i < P.getNumberOfContours(); i++) {
			home.beginShape();
			for (int j = 0; j < npc[i]; j++) {
				vertexMapped(P.getPoint(index++), map);
			}
			home.endShape(PConstants.CLOSE);
		}
	}

	public void drawPolygonMapped(final WB_Polygon P, final WB_Map2D map) {
		final int[] tris = P.getTriangles();
		for (int i = 0; i < tris.length; i += 3) {
			drawTriangleMapped(P.getPoint(tris[i]), P.getPoint(tris[i + 1]), P.getPoint(tris[i + 2]), map);
		}
	}

	public void drawPolyLineMapped(final WB_PolyLine P, final WB_Map2D map) {
		for (int i = 0; i < P.getNumberOfPoints() - 1; i++) {
			drawSegmentMapped(P.getPoint(i), P.getPoint(i + 1), map);
		}
	}

	public void drawRayMapped(final WB_Ray R, final double d, final WB_Map2D map) {
		drawSegmentMapped(R.getOrigin(), WB_Point.addMul(R.getOrigin(), d, R.getDirection()), map);
	}

	public void drawRingMapped(final WB_Ring P, final WB_Map2D map) {
		for (int i = 0, j = P.getNumberOfPoints() - 1; i < P.getNumberOfPoints(); j = i++) {
			drawSegmentMapped(P.getPoint(j), P.getPoint(i), map);
		}
	}

	public void drawSegmentMapped(final Collection<? extends WB_Segment> segments, final WB_Map2D map) {
		final Iterator<? extends WB_Segment> segItr = segments.iterator();
		while (segItr.hasNext()) {
			drawSegmentMapped(segItr.next(), map);
		}
	}

	public void drawSegmentMapped(final WB_Coord p, final WB_Coord q, final WB_Map2D map) {
		home.beginShape();
		vertexMapped(p, map);
		vertexMapped(q, map);
		home.endShape();
	}

	public void drawSegmentMapped(final WB_Segment segment, final WB_Map2D map) {
		drawSegmentMapped(segment.getOrigin(), segment.getEndpoint(), map);
	}

	public void drawSegmentMapped(final WB_Segment[] segments, final WB_Map2D map) {
		for (final WB_Segment segment : segments) {
			drawSegmentMapped(segment, map);
		}
	}

	public void drawTriangleEdgesMapped(final Collection<? extends WB_Triangle> triangles, final WB_Map2D map) {
		final Iterator<? extends WB_Triangle> triItr = triangles.iterator();
		while (triItr.hasNext()) {
			drawTriangleEdgesMapped(triItr.next(), map);
		}
	}

	public void drawTriangleEdgesMapped(final WB_Triangle triangle, final WB_Map2D map) {
		drawSegmentMapped(triangle.p1(), triangle.p2(), map);
		drawSegmentMapped(triangle.p2(), triangle.p3(), map);
		drawSegmentMapped(triangle.p3(), triangle.p1(), map);
	}

	public void drawTriangleEdgesMapped(final WB_Triangle[] triangles, final WB_Map2D map) {
		for (final WB_Triangle triangle : triangles) {
			drawTriangleEdgesMapped(triangle, map);
		}
	}

	public void drawTriangleMapped(final Collection<? extends WB_Triangle> triangles, final WB_Map2D map) {
		final Iterator<? extends WB_Triangle> triItr = triangles.iterator();
		while (triItr.hasNext()) {
			drawTriangleMapped(triItr.next(), map);
		}
	}

	public void drawTriangleMapped(final int[] tri, final List<? extends WB_Coord> points, final WB_Map2D map) {
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertexMapped(points.get(tri[i]), map);
			vertexMapped(points.get(tri[i + 1]), map);
			vertexMapped(points.get(tri[i + 2]), map);
			home.endShape();
		}
	}

	public void drawTriangleMapped(final int[] tri, final WB_Coord[] points, final WB_Map2D map) {
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertexMapped(points[tri[i]], map);
			vertexMapped(points[tri[i + 1]], map);
			vertexMapped(points[tri[i + 2]], map);
			home.endShape();
		}
	}

	public void drawTriangleMapped(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Map2D map) {
		home.beginShape(PConstants.TRIANGLES);
		vertexMapped(p1, map);
		vertexMapped(p2, map);
		vertexMapped(p3, map);
		home.endShape();
	}

	public void drawTriangleMapped(final WB_Triangle T, final WB_Map2D map) {
		home.beginShape(PConstants.TRIANGLES);
		vertexMapped(T.p1(), map);
		vertexMapped(T.p2(), map);
		vertexMapped(T.p3(), map);
		home.endShape();
	}

	public void drawTriangleMapped(final WB_Triangle[] triangles, final WB_Map2D map) {
		for (final WB_Triangle triangle : triangles) {
			drawTriangleMapped(triangle, map);
		}
	}

	public void drawTriangulationMapped(final WB_Triangulation2D tri, final List<? extends WB_Coord> points,
			final WB_Map2D map) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertexMapped(points.get(triangles[i]), map);
			vertexMapped(points.get(triangles[i + 1]), map);
			vertexMapped(points.get(triangles[i + 2]), map);
		}
		home.endShape();
	}

	public void drawTriangulationMapped(final WB_Triangulation2D tri, final WB_Coord[] points, final WB_Map2D map) {
		final int[] triangles = tri.getTriangles();
		home.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < triangles.length; i += 3) {
			vertexMapped(points[triangles[i]], map);
			vertexMapped(points[triangles[i + 1]], map);
			vertexMapped(points[triangles[i + 2]], map);
		}
		home.endShape();
	}

	public void drawVectorMapped(final WB_Coord p, final WB_Coord v, final double r, final WB_Map2D map) {
		drawSegmentMapped(p, WB_Point.addMul(p, r, v), map);
	}

	public void vertexMapped(final WB_Coord p, final WB_Map2D map) {
		final WB_Point q = new WB_Point();
		map.mapPoint3D(p, q);
		vertex2D(q);
	}

	public void drawGizmo(final double d) {
		home.pushStyle();
		home.noFill();
		home.stroke(255, 0, 0);
		home.line(0f, 0f, (float) d, 0);
		home.stroke(0, 255, 0);
		home.line(0f, 0f, 0, (float) d);
		home.popStyle();
	}

	public void drawTriangle2D(final WB_TriangleFactory triangleGenerator) {
		final int[] tri = triangleGenerator.getTriangles();
		final WB_CoordCollection points = triangleGenerator.getPoints();
		for (int i = 0; i < tri.length; i += 3) {
			home.beginShape(PConstants.TRIANGLES);
			vertex2D(points.get(tri[i]));
			vertex2D(points.get(tri[i + 1]));
			vertex2D(points.get(tri[i + 2]));
			home.endShape();
		}
	}

	public void drawBinaryGrid2D(final WB_BinaryGrid2D grid) {
		home.pushMatrix();
		translate2D(grid.getMin());
		drawFaces(grid);
		home.popMatrix();
	}

	public void drawBinaryGridOutline2D(final WB_BinaryGrid2D grid) {
		home.pushMatrix();
		translate2D(grid.getMin());
		drawXEdges(grid);
		drawYEdges(grid);
		home.popMatrix();
	}

	public void drawBinaryGridOutline2D(final WB_BinaryGrid2D grid, final int cx, final int cy) {
		home.pushStyle();
		home.pushMatrix();
		translate2D(grid.getMin());
		home.stroke(cx);
		drawXEdges(grid);
		home.stroke(cy);
		drawYEdges(grid);
		home.popMatrix();
		home.popStyle();
	}

	void drawXEdges(final WB_BinaryGrid2D grid) {
		double x, y;
		for (int i = grid.lx(); i < grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j <= grid.uy(); j++) {
				y = j * grid.getDY();
				if (grid.get(i, j) ^ grid.get(i, j - 1)) {
					line2D(x, y, x + grid.getDX(), y);
				}
			}
		}
	}

	void drawYEdges(final WB_BinaryGrid2D grid) {
		double x, y;
		for (int j = grid.ly(); j < grid.uy(); j++) {
			y = j * grid.getDY();
			for (int i = grid.lx(); i <= grid.ux(); i++) {
				x = i * grid.getDX();
				if (grid.get(i, j) ^ grid.get(i - 1, j)) {
					line2D(x, y, x, y + grid.getDY());
				}
			}
		}
	}

	void drawFaces(final WB_BinaryGrid2D grid) {
		double x, y;
		for (int i = grid.lx(); i < grid.ux(); i++) {
			x = i * grid.getDX();
			for (int j = grid.ly(); j < grid.uy(); j++) {
				y = j * grid.getDY();
				if (grid.get(i, j)) {
					home.beginShape();
					vertex2D(x, y);
					vertex2D(x + grid.getDX(), y);
					vertex2D(x + grid.getDX(), y + grid.getDY());
					vertex2D(x, y + grid.getDY());
					home.endShape();
				}
			}
		}
	}

	public void drawQuad2D(final WB_Quad quad) {
		home.beginShape();
		vertex2D(quad.getP1());
		vertex2D(quad.getP2());
		vertex2D(quad.getP3());
		vertex2D(quad.getP4());
		home.endShape(PConstants.CLOSE);
	}

	public void drawQuad2D(final Collection<? extends WB_Quad> quads) {
		final Iterator<? extends WB_Quad> qItr = quads.iterator();
		while (qItr.hasNext()) {
			drawQuad2D(qItr.next());
		}
	}

	public void drawPentagon2D(final WB_Pentagon pentagon) {
		home.beginShape();
		vertex2D(pentagon.getP1());
		vertex2D(pentagon.getP2());
		vertex2D(pentagon.getP3());
		vertex2D(pentagon.getP4());
		vertex2D(pentagon.getP5());
		home.endShape(PConstants.CLOSE);
	}

	public void drawPentagon2D(final Collection<? extends WB_Pentagon> pentagons) {
		final Iterator<? extends WB_Pentagon> pItr = pentagons.iterator();
		while (pItr.hasNext()) {
			drawPentagon2D(pItr.next());
		}
	}

	public void drawHexagon2D(final WB_Hexagon hexagon) {
		home.beginShape();
		vertex2D(hexagon.getP1());
		vertex2D(hexagon.getP2());
		vertex2D(hexagon.getP3());
		vertex2D(hexagon.getP4());
		vertex2D(hexagon.getP5());
		vertex2D(hexagon.getP6());
		home.endShape(PConstants.CLOSE);
	}

	public void drawHexagon2D(final Collection<? extends WB_Hexagon> hexagons) {
		final Iterator<? extends WB_Hexagon> hItr = hexagons.iterator();
		while (hItr.hasNext()) {
			drawHexagon2D(hItr.next());
		}
	}

	public void drawOctagon2D(final WB_Octagon octagon) {
		home.beginShape();
		vertex2D(octagon.getP1());
		vertex2D(octagon.getP2());
		vertex2D(octagon.getP3());
		vertex2D(octagon.getP4());
		vertex2D(octagon.getP5());
		vertex2D(octagon.getP6());
		vertex2D(octagon.getP7());
		vertex2D(octagon.getP8());
		home.endShape(PConstants.CLOSE);
	}

	public void drawOctagon2D(final Collection<? extends WB_Octagon> octagons) {
		final Iterator<? extends WB_Octagon> oItr = octagons.iterator();
		while (oItr.hasNext()) {
			drawOctagon2D(oItr.next());
		}
	}
}
