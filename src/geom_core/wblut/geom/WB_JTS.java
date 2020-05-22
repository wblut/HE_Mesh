package wblut.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.algorithm.PointLocation;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.triangulate.quadedge.QuadEdgeSubdivision;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.core.WB_ProgressReporter.WB_ProgressTracker;
import wblut.hemesh.HEC_FromFacelist;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_MeshOp;
import wblut.hemesh.HE_Path;
import wblut.hemesh.HE_Vertex;
import wblut.math.WB_Epsilon;

public class WB_JTS {
	private final static GeometryFactory JTSgf = new GeometryFactory(new PrecisionModel(WB_Epsilon.SCALE));
	private final static WB_Map2D XY = new WB_GeometryFactory3D().createEmbeddedPlane();

	private WB_JTS() {
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static Polygon toJTSPolygon2D(final WB_Polygon poly) {
		final int[] npc = poly.getNumberOfPointsPerContour();
		Coordinate[] coords = new Coordinate[npc[0] + 1];
		int i = 0;
		for (i = 0; i < npc[0]; i++) {
			coords[i] = toJTSCoordinate2D(poly.getPoint(i), i);
		}
		coords[i] = toJTSCoordinate2D(poly.getPoint(0), 0);
		final LinearRing shell = JTSgf.createLinearRing(coords);
		final LinearRing[] holes = new LinearRing[poly.getNumberOfHoles()];
		int index = poly.getNumberOfShellPoints();
		for (i = 0; i < poly.getNumberOfHoles(); i++) {
			coords = new Coordinate[npc[i + 1] + 1];
			coords[npc[i + 1]] = toJTSCoordinate2D(poly.getPoint(index), index);
			for (int j = 0; j < npc[i + 1]; j++) {
				coords[j] = toJTSCoordinate2D(poly.getPoint(index), index);
				index++;
			}
			holes[i] = JTSgf.createLinearRing(coords);
		}
		return JTSgf.createPolygon(shell, holes);
	}

	static Geometry toJTSMultiPolygon2D(final List<WB_Polygon> polys) {
		final Polygon[] JTSpolys = new Polygon[polys.size()];
		for (int j = 0; j < polys.size(); j++) {
			final WB_Polygon poly = polys.get(j);
			final int[] npc = poly.getNumberOfPointsPerContour();
			Coordinate[] coords = new Coordinate[npc[0] + 1];
			int i = 0;
			for (i = 0; i < npc[0]; i++) {
				coords[i] = toJTSCoordinate2D(poly.getPoint(i), i);
			}
			coords[i] = toJTSCoordinate2D(poly.getPoint(0), 0);
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final LinearRing[] holes = new LinearRing[poly.getNumberOfHoles()];
			int index = poly.getNumberOfShellPoints();
			for (i = 0; i < poly.getNumberOfHoles(); i++) {
				coords = new Coordinate[npc[i + 1] + 1];
				coords[npc[i + 1]] = toJTSCoordinate2D(poly.getPoint(index), index);
				for (int k = 0; k < npc[i + 1]; k++) {
					coords[k] = toJTSCoordinate2D(poly.getPoint(index), index);
					index++;
				}
				holes[i] = JTSgf.createLinearRing(coords);
			}
			JTSpolys[j] = JTSgf.createPolygon(shell, holes);
		}
		return JTSgf.createMultiPolygon(JTSpolys).buffer(0);
	}

	/**
	 *
	 *
	 * @param point
	 * @param i
	 * @return
	 */
	static Coordinate toJTSCoordinate2D(final WB_Coord point, final int i) {
		return new Coordinate(point.xd(), point.yd(), i);
	}

	/**
	 *
	 *
	 * @param coord
	 * @return
	 */
	static WB_Point createPoint2D(final Coordinate coord) {
		return new WB_Point(coord.x, coord.y);
	}

	/**
	 *
	 *
	 * @param JTSpoly
	 * @return
	 */
	static WB_Polygon createPolygonFromJTSPolygon2D(final Polygon JTSpoly) {
		final LineString shell = JTSpoly.getExteriorRing();
		Coordinate[] coords = shell.getCoordinates();
		final WB_Coord[] points = new WB_Coord[coords.length - 1];
		for (int i = 0; i < coords.length - 1; i++) {
			points[i] = createPoint2D(coords[i]);
		}
		final int numholes = JTSpoly.getNumInteriorRing();
		if (numholes > 0) {
			final WB_Coord[][] holecoords = new WB_Coord[numholes][];
			for (int i = 0; i < numholes; i++) {
				final LineString hole = JTSpoly.getInteriorRingN(i);
				coords = hole.getCoordinates();
				holecoords[i] = new WB_Coord[coords.length - 1];
				for (int j = 0; j < coords.length - 1; j++) {
					holecoords[i][j] = createPoint2D(coords[j]);
				}
			}
			return new WB_GeometryFactory2D().createPolygonWithHoles(points, holecoords);
		} else {
			return new WB_GeometryFactory2D().createSimplePolygon(points);
		}
	}

	/**
	 *
	 *
	 * @param geometry
	 * @return
	 */
	static List<WB_Polygon> createPolygonsFromJTSGeometry2D(final Geometry geometry) {
		final List<WB_Polygon> polygons = new FastList<>();
		for (int i = 0; i < geometry.getNumGeometries(); i++) {
			final Geometry geo = geometry.getGeometryN(i);
			if (!geo.isEmpty()) {
				if (geo.getGeometryType().equals("Polygon")) {
					polygons.add(createPolygonFromJTSPolygon2D((Polygon) geo));
				} else if (geo.getGeometryType().equals("MultiPolygon")) {
					for (int j = 0; j < geo.getNumGeometries(); j++) {
						final Geometry ggeo = geo.getGeometryN(j);
						polygons.add(createPolygonFromJTSPolygon2D((Polygon) ggeo));
					}
				} else if (geo.getGeometryType().equals("GeometryCollection")) {
					for (int j = 0; j < geo.getNumGeometries(); j++) {
						final Geometry ggeo = geo.getGeometryN(j);
						polygons.addAll(createPolygonsFromJTSGeometry2D(ggeo));
					}
				}
			}
		}
		return polygons;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons2D(final WB_Polygon poly, final double d) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons2D(final Collection<? extends WB_Polygon> poly, final double d) {
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon2D(pol);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry result = BufferOp.bufferOp(collPoly, d);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons2D(final WB_Polygon poly, final double d, final int n) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final BufferParameters parameters = new BufferParameters(n, BufferParameters.CAP_ROUND,
				n == 0 ? BufferParameters.JOIN_MITRE : BufferParameters.CAP_ROUND,
				BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d, parameters);
		return createPolygonsFromJTSGeometry2D(result);
	}

	static List<WB_Polygon> createBufferedPolygonsStraight2D(final WB_Polygon poly, final double d) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final BufferParameters parameters = new BufferParameters(0, BufferParameters.CAP_ROUND,
				BufferParameters.JOIN_MITRE, BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d, parameters);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons2D(final Collection<? extends WB_Polygon> poly, final double d,
			final int n) {
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon2D(pol);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final BufferParameters parameters = new BufferParameters(n, BufferParameters.CAP_ROUND,
				n == 0 ? BufferParameters.JOIN_MITRE : BufferParameters.JOIN_ROUND,
				BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(collPoly, d, parameters);
		return createPolygonsFromJTSGeometry2D(result);
	}

	static List<WB_Polygon> createBufferedPolygonsStraight2D(final Collection<? extends WB_Polygon> poly,
			final double d) {
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon2D(pol);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final BufferParameters parameters = new BufferParameters(0, BufferParameters.CAP_ROUND,
				BufferParameters.JOIN_MITRE, BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(collPoly, d, parameters);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static List<WB_Polygon> createBoundaryPolygons2D(final WB_Polygon poly) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final LineString result = JTSpoly.getExteriorRing();
		return createPolygonsFromJTSGeometry2D(JTSgf.createPolygon(result.getCoordinates()));
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons2D(final WB_Polygon poly, final double d) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final Geometry clean = BufferOp.bufferOp(JTSpoly, 0);
		final Geometry outer = BufferOp.bufferOp(clean, d * 0.5);
		final Geometry inner = BufferOp.bufferOp(clean, -d * 0.5);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons2D(final Collection<? extends WB_Polygon> poly, final double d) {
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon2D(pol);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry clean = BufferOp.bufferOp(collPoly, 0);
		final Geometry outer = BufferOp.bufferOp(clean, d * 0.5);
		final Geometry inner = BufferOp.bufferOp(clean, -d * 0.5);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons2D(final WB_Polygon poly, final double o, final double i) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final Geometry clean = BufferOp.bufferOp(JTSpoly, 0);
		final Geometry outer = BufferOp.bufferOp(clean, o);
		final Geometry inner = BufferOp.bufferOp(clean, -i);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons2D(final Collection<? extends WB_Polygon> poly, final double o,
			final double i) {
		final Polygon[] allPoly = new Polygon[poly.size()];
		int j = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[j++] = toJTSPolygon2D(pol);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry clean = BufferOp.bufferOp(collPoly, 0);
		final Geometry outer = BufferOp.bufferOp(clean, o);
		final Geometry inner = BufferOp.bufferOp(clean, -i);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param tol
	 * @return
	 */
	static List<WB_Polygon> createSimplifiedPolygon2D(final WB_Polygon poly, final double tol) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		// final Geometry result = DouglasPeuckerSimplifier.simplify(JTSpoly,
		// tol);
		final Geometry result = TopologyPreservingSimplifier.simplify(JTSpoly, tol);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param max
	 * @return
	 */
	static List<WB_Polygon> createDensifiedPolygon2D(final WB_Polygon poly, final double max) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		// final Geometry result = DouglasPeuckerSimplifier.simplify(JTSpoly,
		// tol);
		final Geometry result = Densifier.densify(JTSpoly, max);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> unionPolygons2D(final WB_Polygon poly1, final WB_Polygon poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon JTSpoly2 = toJTSPolygon2D(poly2);
		final Geometry result = JTSpoly1.union(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> unionPolygons2D(final WB_Polygon poly1, final Collection<? extends WB_Polygon> poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		int i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = JTSpoly1.union(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> unionPolygons2D(final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		final Polygon[] allPoly1 = new Polygon[poly1.size()];
		int i = 0;
		for (final WB_Polygon poly : poly1) {
			allPoly1[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly1 = JTSgf.createMultiPolygon(allPoly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = collPoly1.union(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> subtractPolygons2D(final WB_Polygon poly1, final WB_Polygon poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon JTSpoly2 = toJTSPolygon2D(poly2);
		final Geometry result = JTSpoly1.difference(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> subtractPolygons2D(final WB_Polygon poly1, final Collection<? extends WB_Polygon> poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		int i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = JTSpoly1.difference(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> subtractPolygons2D(final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		final Polygon[] allPoly1 = new Polygon[poly1.size()];
		int i = 0;
		for (final WB_Polygon poly : poly1) {
			allPoly1[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly1 = JTSgf.createMultiPolygon(allPoly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = collPoly1.difference(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> subtractPolygons2D(final Collection<? extends WB_Polygon> poly1, final WB_Polygon poly2) {
		final Polygon[] allPoly1 = new Polygon[poly1.size()];
		int i = 0;
		for (final WB_Polygon poly : poly1) {
			allPoly1[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly1 = JTSgf.createMultiPolygon(allPoly1);
		final Polygon JTSpoly2 = toJTSPolygon2D(poly2);
		final Geometry result = collPoly1.difference(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> intersectPolygons2D(final WB_Polygon poly1, final WB_Polygon poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon JTSpoly2 = toJTSPolygon2D(poly2);
		final Geometry result = JTSpoly1.intersection(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> intersectPolygons2D(final WB_Polygon poly1, final Collection<? extends WB_Polygon> poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		int i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = JTSpoly1.intersection(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> intersectPolygons2D(final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		final Polygon[] allPoly1 = new Polygon[poly1.size()];
		int i = 0;
		for (final WB_Polygon poly : poly1) {
			allPoly1[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly1 = JTSgf.createMultiPolygon(allPoly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = collPoly1.intersection(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> symDifferencePolygons2D(final WB_Polygon poly1, final WB_Polygon poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon JTSpoly2 = toJTSPolygon2D(poly2);
		final Geometry result = JTSpoly1.symDifference(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> symDifferencePolygons2D(final WB_Polygon poly1,
			final Collection<? extends WB_Polygon> poly2) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		int i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = JTSpoly1.symDifference(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly1
	 * @param poly2
	 * @return
	 */
	static List<WB_Polygon> symDifferencePolygons2D(final Collection<? extends WB_Polygon> poly1,
			final Collection<? extends WB_Polygon> poly2) {
		final Polygon[] allPoly1 = new Polygon[poly1.size()];
		int i = 0;
		for (final WB_Polygon poly : poly1) {
			allPoly1[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly1 = JTSgf.createMultiPolygon(allPoly1);
		final Polygon[] allPoly2 = new Polygon[poly2.size()];
		i = 0;
		for (final WB_Polygon poly : poly2) {
			allPoly2[i++] = toJTSPolygon2D(poly);
		}
		final MultiPolygon collPoly2 = JTSgf.createMultiPolygon(allPoly2);
		final Geometry result = collPoly1.symDifference(collPoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons2D(final WB_Polygon poly, final WB_Polygon container) {
		final Polygon JTSpoly1 = toJTSPolygon2D(poly);
		final Polygon JTSpoly2 = toJTSPolygon2D(container);
		final Geometry result = JTSpoly1.intersection(JTSpoly2);
		return createPolygonsFromJTSGeometry2D(result);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons2D(final WB_Polygon[] polygons, final WB_Polygon container) {
		final List<WB_Polygon> polys = new FastList<>();
		for (final WB_Polygon poly : polygons) {
			final Polygon JTSpoly1 = toJTSPolygon2D(poly);
			final Polygon JTSpoly2 = toJTSPolygon2D(container);
			final Geometry result = JTSpoly1.intersection(JTSpoly2);
			polys.addAll(createPolygonsFromJTSGeometry2D(result));
		}
		return polys;
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons2D(final List<WB_Polygon> polygons, final WB_Polygon container) {
		final List<WB_Polygon> polys = new FastList<>();
		for (final WB_Polygon poly : polygons) {
			final Polygon JTSpoly1 = toJTSPolygon2D(poly);
			final Polygon JTSpoly2 = toJTSPolygon2D(container);
			final Geometry result = JTSpoly1.intersection(JTSpoly2);
			if (!result.isEmpty()) {
				polys.addAll(createPolygonsFromJTSGeometry2D(result));
			}
		}
		return polys;
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static WB_Polygon createPolygonConvexHull2D(final WB_Polygon poly) {
		final Polygon JTSpoly = toJTSPolygon2D(poly);
		final Geometry result = new ConvexHull(JTSpoly).getConvexHull();
		if (result.getGeometryType().equals("Polygon")) {
			return createPolygonFromJTSPolygon2D((Polygon) result);
		}
		return null;
	}

	static WB_Polygon createPolygonFromJTSPolygon(final Polygon JTSpoly, final WB_Map2D map) {
		final LineString shell = JTSpoly.getExteriorRing();
		Coordinate[] coords = shell.getCoordinates();
		final WB_Point[] points = new WB_Point[coords.length - 1];
		for (int i = 0; i < coords.length - 1; i++) {
			points[i] = createPoint2D(coords[i]);
			map.unmapPoint3D(points[i], points[i]);
		}
		final int numholes = JTSpoly.getNumInteriorRing();
		if (numholes > 0) {
			final WB_Point[][] holecoords = new WB_Point[numholes][];
			for (int i = 0; i < numholes; i++) {
				final LineString hole = JTSpoly.getInteriorRingN(i);
				coords = hole.getCoordinates();
				holecoords[i] = new WB_Point[coords.length - 1];
				for (int j = 0; j < coords.length - 1; j++) {
					holecoords[i][j] = createPoint2D(coords[j]);
					map.unmapPoint3D(holecoords[i][j], holecoords[i][j]);
				}
			}
			return new WB_GeometryFactory3D().createPolygonWithHoles(points, holecoords);
		} else {
			return new WB_GeometryFactory3D().createSimplePolygon(points);
		}
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static Polygon toJTSPolygon(final WB_Polygon poly, final WB_Map2D map) {
		final int[] npc = poly.getNumberOfPointsPerContour();
		Coordinate[] coords = new Coordinate[npc[0] + 1];
		int i = 0;
		for (i = 0; i < npc[0]; i++) {
			coords[i] = toJTSCoordinate(poly.getPoint(i), i, map);
		}
		coords[i] = toJTSCoordinate(poly.getPoint(0), 0, map);
		final LinearRing shell = JTSgf.createLinearRing(coords);
		final LinearRing[] holes = new LinearRing[poly.getNumberOfHoles()];
		int index = poly.getNumberOfShellPoints();
		for (i = 0; i < poly.getNumberOfHoles(); i++) {
			coords = new Coordinate[npc[i + 1] + 1];
			coords[npc[i + 1]] = toJTSCoordinate(poly.getPoint(index), index, map);
			for (int j = 0; j < npc[i + 1]; j++) {
				coords[j] = toJTSCoordinate(poly.getPoint(index), index, map);
				index++;
			}
			holes[i] = JTSgf.createLinearRing(coords);
		}
		return JTSgf.createPolygon(shell, holes);
	}

	static Geometry toJTSMultiPolygon(final List<WB_Polygon> polys, final WB_Map2D map) {
		final Polygon[] JTSpolys = new Polygon[polys.size()];
		for (int j = 0; j < polys.size(); j++) {
			final WB_Polygon poly = polys.get(j);
			final int[] npc = poly.getNumberOfPointsPerContour();
			Coordinate[] coords = new Coordinate[npc[0] + 1];
			int i = 0;
			for (i = 0; i < npc[0]; i++) {
				coords[i] = toJTSCoordinate(poly.getPoint(i), i, map);
			}
			coords[i] = toJTSCoordinate2D(poly.getPoint(0), 0);
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final LinearRing[] holes = new LinearRing[poly.getNumberOfHoles()];
			int index = poly.getNumberOfShellPoints();
			for (i = 0; i < poly.getNumberOfHoles(); i++) {
				coords = new Coordinate[npc[i + 1] + 1];
				coords[npc[i + 1]] = toJTSCoordinate(poly.getPoint(index), index, map);
				for (int k = 0; k < npc[i + 1]; k++) {
					coords[k] = toJTSCoordinate(poly.getPoint(index), index, map);
					index++;
				}
				holes[i] = JTSgf.createLinearRing(coords);
			}
			JTSpolys[j] = JTSgf.createPolygon(shell, holes);
		}
		return JTSgf.createMultiPolygon(JTSpolys).buffer(0);
	}

	/**
	 *
	 *
	 * @param point
	 * @param i
	 * @return
	 */
	static Coordinate toJTSCoordinate(final WB_Coord point, final int i, final WB_Map2D map) {
		final WB_Point mp = new WB_Point();
		map.mapPoint3D(point, mp);
		return new Coordinate(mp.xd(), mp.yd(), i);
	}

	/**
	 *
	 * @param geometry
	 * @param map
	 * @return
	 */
	private static List<WB_Polygon> createPolygonsFromJTSGeometry(final Geometry geometry, final WB_Map2D map) {
		final List<WB_Polygon> polygons = new FastList<>();
		for (int i = 0; i < geometry.getNumGeometries(); i++) {
			final Geometry geo = geometry.getGeometryN(i);
			if (!geo.isEmpty()) {
				if (geo.getGeometryType().equals("Polygon")) {
					polygons.add(createPolygonFromJTSPolygon((Polygon) geo, map));
				} else if (geo.getGeometryType().equals("MultiPolygon")) {
					for (int j = 0; j < geo.getNumGeometries(); j++) {
						final Geometry ggeo = geo.getGeometryN(j);
						polygons.add(createPolygonFromJTSPolygon((Polygon) ggeo, map));
					}
				} else if (geo.getGeometryType().equals("GeometryCollection")) {
					for (int j = 0; j < geo.getNumGeometries(); j++) {
						final Geometry ggeo = geo.getGeometryN(j);
						polygons.addAll(createPolygonsFromJTSGeometry(ggeo, map));
					}
				}
			}
		}
		return polygons;
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static WB_Polygon createPolygonConvexHull(final WB_Polygon poly) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final Geometry result = new ConvexHull(JTSpoly).getConvexHull();
		if (result.getGeometryType().equals("Polygon")) {
			createPolygonFromJTSPolygon((Polygon) result, map);
		}
		return null;
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly, final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons(final WB_Polygon poly, final double d, final int n) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final BufferParameters parameters = new BufferParameters(n, BufferParameters.CAP_ROUND,
				n == 0 ? BufferParameters.JOIN_MITRE : BufferParameters.CAP_ROUND,
				BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d, parameters);
		return createPolygonsFromJTSGeometry(result, map);
	}

	static List<WB_Polygon> createBufferedPolygonsStraight(final WB_Polygon poly, final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final BufferParameters parameters = new BufferParameters(0, BufferParameters.CAP_ROUND,
				BufferParameters.JOIN_MITRE, BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(JTSpoly, d, parameters);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons(final Collection<? extends WB_Polygon> poly, final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.iterator().next().getPlane(0));
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon(pol, map);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry result = BufferOp.bufferOp(collPoly, d);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @param n
	 * @return
	 */
	static List<WB_Polygon> createBufferedPolygons(final Collection<? extends WB_Polygon> poly, final double d,
			final int n) {
		final WB_Map2D map = new WB_PlanarMap(poly.iterator().next().getPlane(0));
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon(pol, map);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final BufferParameters parameters = new BufferParameters(n, BufferParameters.CAP_ROUND,
				n == 0 ? BufferParameters.JOIN_MITRE : BufferParameters.JOIN_ROUND,
				BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(collPoly, d, parameters);
		return createPolygonsFromJTSGeometry(result, map);
	}

	static List<WB_Polygon> createBufferedPolygonsStraight(final Collection<? extends WB_Polygon> poly,
			final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.iterator().next().getPlane(0));
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon(pol, map);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final BufferParameters parameters = new BufferParameters(0, BufferParameters.CAP_ROUND,
				BufferParameters.JOIN_MITRE, BufferParameters.DEFAULT_MITRE_LIMIT);
		final Geometry result = BufferOp.bufferOp(collPoly, d, parameters);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @return
	 */
	static List<WB_Polygon> createBoundaryPolygons(final WB_Polygon poly) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final LineString result = JTSpoly.getExteriorRing();
		return createPolygonsFromJTSGeometry(JTSgf.createPolygon(result.getCoordinates()), map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly, final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final Geometry outer = BufferOp.bufferOp(JTSpoly, d * 0.5);
		final Geometry inner = BufferOp.bufferOp(JTSpoly, -d * 0.5);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param d
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons(final Collection<? extends WB_Polygon> poly, final double d) {
		final WB_Map2D map = new WB_PlanarMap(poly.iterator().next().getPlane(0));
		final Polygon[] allPoly = new Polygon[poly.size()];
		int i = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[i++] = toJTSPolygon(pol, map);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry outer = BufferOp.bufferOp(collPoly, d * 0.5);
		final Geometry inner = BufferOp.bufferOp(collPoly, -d * 0.5);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons(final WB_Polygon poly, final double o, final double i) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		final Geometry outer = BufferOp.bufferOp(JTSpoly, o);
		final Geometry inner = BufferOp.bufferOp(JTSpoly, -i);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 * @param poly
	 * @param o
	 * @param i
	 * @return
	 */
	static List<WB_Polygon> createRibbonPolygons(final Collection<? extends WB_Polygon> poly, final double o,
			final double i) {
		final WB_Map2D map = new WB_PlanarMap(poly.iterator().next().getPlane(0));
		final Polygon[] allPoly = new Polygon[poly.size()];
		int j = 0;
		for (final WB_Polygon pol : poly) {
			allPoly[j++] = toJTSPolygon(pol, map);
		}
		final MultiPolygon collPoly = JTSgf.createMultiPolygon(allPoly);
		final Geometry outer = BufferOp.bufferOp(collPoly, o);
		final Geometry inner = BufferOp.bufferOp(collPoly, -i);
		final Geometry result = outer.difference(inner);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param tol
	 * @return
	 */
	static List<WB_Polygon> createSimplifiedPolygon(final WB_Polygon poly, final double tol) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		// final Geometry result = DouglasPeuckerSimplifier.simplify(JTSpoly,
		// tol);
		final Geometry result = TopologyPreservingSimplifier.simplify(JTSpoly, tol);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param max
	 * @return
	 */
	static List<WB_Polygon> createDensifiedPolygon(final WB_Polygon poly, final double max) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly = toJTSPolygon(poly, map);
		// final Geometry result = DouglasPeuckerSimplifier.simplify(JTSpoly,
		// tol);
		final Geometry result = Densifier.densify(JTSpoly, max);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param poly
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons(final WB_Polygon poly, final WB_Polygon container) {
		final WB_Map2D map = new WB_PlanarMap(poly.getPlane(0));
		final Polygon JTSpoly1 = toJTSPolygon(poly, map);
		final Polygon JTSpoly2 = toJTSPolygon(container, map);
		final Geometry result = JTSpoly1.intersection(JTSpoly2);
		return createPolygonsFromJTSGeometry(result, map);
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons(final WB_Polygon[] polygons, final WB_Polygon container) {
		final WB_Map2D map = new WB_PlanarMap(polygons[0].getPlane(0));
		final List<WB_Polygon> polys = new FastList<>();
		for (final WB_Polygon poly : polygons) {
			final Polygon JTSpoly1 = toJTSPolygon(poly, map);
			final Polygon JTSpoly2 = toJTSPolygon(container, map);
			final Geometry result = JTSpoly1.intersection(JTSpoly2);
			polys.addAll(createPolygonsFromJTSGeometry(result, map));
		}
		return polys;
	}

	/**
	 *
	 *
	 * @param polygons
	 * @param container
	 * @return
	 */
	static List<WB_Polygon> constrainPolygons(final List<WB_Polygon> polygons, final WB_Polygon container) {
		final WB_Map2D map = new WB_PlanarMap(polygons.get(0).getPlane(0));
		final List<WB_Polygon> polys = new FastList<>();
		for (final WB_Polygon poly : polygons) {
			final Polygon JTSpoly1 = toJTSPolygon(poly, map);
			final Polygon JTSpoly2 = toJTSPolygon(container, map);
			final Geometry result = JTSpoly1.intersection(JTSpoly2);
			if (!result.isEmpty()) {
				polys.addAll(createPolygonsFromJTSGeometry(result, map));
			}
		}
		return polys;
	}

	/**
	 *
	 */
	public static class PlanarPathTriangulator {
		/**
		 *
		 */
		static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

		/**
		 *
		 */
		public PlanarPathTriangulator() {
		}

		/**
		 *
		 *
		 * @param paths
		 * @param P
		 * @return
		 */
		public static long[] getTriangleKeys(final List<? extends HE_Path> paths, final WB_Plane P) {
			tracker.setStartStatus("HET_PlanarPathTriangulator", "Starting planar path triangulation.");
			final WB_Map2D emb = new WB_OrthoProject(P);
			final RingTree ringtree = new RingTree();
			List<HE_Vertex> vertices;
			Coordinate[] pts;
			final WB_KDTree3D<WB_Point, Long> vertextree = new WB_KDTree3D<>();
			tracker.setDuringStatus("HET_PlanarPathTriangulator", "Building contours tree.");
			for (int i = 0; i < paths.size(); i++) {
				final HE_Path path = paths.get(i);
				if (path.isLoop() && path.getPathOrder() > 2) {
					vertices = path.getPathVertices();
					pts = new Coordinate[vertices.size() + 1];
					for (int j = 0; j < vertices.size(); j++) {
						final WB_Point proj = new WB_Point();
						emb.mapPoint3D(vertices.get(j), proj);
						vertextree.add(proj, vertices.get(j).getKey());
						pts[vertices.size() - j] = new Coordinate(proj.xd(), proj.yd(), 0);
					}
					final WB_Point proj = new WB_Point();
					emb.mapPoint3D(vertices.get(0), proj);
					pts[0] = new Coordinate(proj.xd(), proj.yd(), 0);
					ringtree.add(JTSgf.createLinearRing(pts));
				}
			}
			tracker.setDuringStatus("HET_PlanarPathTriangulator", "Extracting polygons from contours tree.");
			final List<WB_Polygon> polygons = ringtree.extractPolygons();
			final List<WB_Coord[]> triangles = new FastList<>();
			final WB_ProgressCounter counter = new WB_ProgressCounter(polygons.size(), 10);
			tracker.setCounterStatus("HET_PlanarPathTriangulator", "Triangulating polygons.", counter);
			for (final WB_Polygon poly : polygons) {
				final int[] tris = poly.getTriangles();
				for (int i = 0; i < tris.length; i += 3) {
					triangles.add(new WB_Coord[] { poly.getPoint(tris[i]), poly.getPoint(tris[i + 1]),
							poly.getPoint(tris[i + 2]) });
				}
				counter.increment();
			}
			final long[] trianglekeys = new long[3 * triangles.size()];
			for (int i = 0; i < triangles.size(); i++) {
				final WB_Coord[] tri = triangles.get(i);
				final long key0 = vertextree.getNearestNeighbor(tri[0]).value;
				final long key1 = vertextree.getNearestNeighbor(tri[1]).value;
				final long key2 = vertextree.getNearestNeighbor(tri[2]).value;
				// reverse triangles
				trianglekeys[3 * i] = key0;
				trianglekeys[3 * i + 1] = key2;
				trianglekeys[3 * i + 2] = key1;
			}
			tracker.setStopStatus("HET_PlanarPathTriangulator", "All paths triangulated.");
			return trianglekeys;
		}

		/**
		 *
		 *
		 * @param tri
		 * @return
		 */
		public static HE_Mesh toMesh(final WB_Triangulation2DWithPoints tri) {
			final HEC_FromFacelist ffl = new HEC_FromFacelist().setFaces(tri.getTriangles())
					.setVertices(tri.getPoints());
			return new HE_Mesh(ffl);
		}

		// The JTS implementation of ShapeReader does not handle overlapping
		// polygons well. All code below this point is my solution for this. A
		// hierarchical tree that orders rings from the outside in. All input
		// has to
		// be well-ordered: CCW for shell, CW for hole.
		/**
		 *
		 */
		private static class RingNode {
			/**
			 *
			 */
			@SuppressWarnings("unused")
			RingNode parent;
			/**
			 *
			 */
			List<RingNode> children;
			/**
			 *
			 */
			LinearRing ring;
			/**
			 *
			 */
			Polygon poly; // redundant, but useful for
							// within/contains
							// checks
			/**
			 *
			 */
			boolean hole;

			/**
			 *
			 */
			RingNode() {
				parent = null;
				ring = null;
				children = new ArrayList<>();
				hole = true;
			}

			/**
			 *
			 *
			 * @param parent
			 * @param ring
			 */
			RingNode(final RingNode parent, final LinearRing ring) {
				this.parent = parent;
				this.ring = ring;
				final Coordinate[] coords = ring.getCoordinates();
				poly = JTSgf.createPolygon(coords);
				hole = !Orientation.isCCW(coords);
				children = new ArrayList<>();
			}
		}

		/**
		 *
		 */
		private static class RingTree {
			/**
			 *
			 */
			RingNode root;

			/**
			 *
			 */
			RingTree() {
				root = new RingNode();
			}

			/**
			 *
			 *
			 * @param ring
			 */
			void add(final LinearRing ring) {
				final Polygon poly = JTSgf.createPolygon(ring);
				RingNode currentParent = root;
				RingNode foundParent;
				do {
					foundParent = null;
					for (final RingNode node : currentParent.children) {
						final Polygon other = node.poly;
						if (poly.within(other)) {
							foundParent = node;
							currentParent = node;
							break;
						}
					}
				} while (foundParent != null);
				final RingNode newNode = new RingNode(currentParent, ring);
				final List<RingNode> nodesToRemove = new ArrayList<>();
				for (int i = 0; i < currentParent.children.size(); i++) {
					final RingNode node = currentParent.children.get(i);
					final Polygon other = node.poly;
					if (other.within(poly)) {
						newNode.children.add(node);
						nodesToRemove.add(node);
					}
				}
				currentParent.children.removeAll(nodesToRemove);
				currentParent.children.add(newNode);
			}

			/**
			 *
			 *
			 * @return
			 */
			List<WB_Polygon> extractPolygons() {
				final List<WB_Polygon> polygons = new ArrayList<>();
				final List<RingNode> shellNodes = new ArrayList<>();
				addExteriorNodes(root, shellNodes);
				for (final RingNode node : shellNodes) {
					int count = 0;
					for (final RingNode element : node.children) {
						if (element.hole) {
							count++;
						}
					}
					final LinearRing[] holes = new LinearRing[count];
					int index = 0;
					for (final RingNode element : node.children) {
						if (element.hole) {
							holes[index++] = element.ring;
						}
					}
					final Geometry result = JTSgf.createPolygon(node.ring, holes);
					if (result.getGeometryType().equals("Polygon")) {
						polygons.add(createPolygonFromJTSPolygon2D((Polygon) result));
					} else if (result.getGeometryType().equals("MultiPolygon")) {
						for (int j = 0; j < result.getNumGeometries(); j++) {
							final Geometry ggeo = result.getGeometryN(j);
							polygons.add(createPolygonFromJTSPolygon2D((Polygon) ggeo));
						}
					}
				}
				return polygons;
			}

			/**
			 *
			 *
			 * @param parent
			 * @param shellNodes
			 */
			void addExteriorNodes(final RingNode parent, final List<RingNode> shellNodes) {
				for (final RingNode node : parent.children) {
					if (node.hole == false) {
						shellNodes.add(node);
					}
					addExteriorNodes(node, shellNodes);
				}
			}
		}
	}

	/**
	 *
	 */
	public static class PolygonTriangulatorJTS {
		/**
		 *
		 */
		public PolygonTriangulatorJTS() {
		}

		/**
		 *
		 *
		 * @param p0
		 * @param p1
		 * @param p2
		 * @param p3
		 * @return
		 */
		public static int[] triangulateQuad(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2,
				final WB_Coord p3) {
			final boolean p0inside = WB_GeometryOp.pointInTriangleBary3D(p0, p1, p2, p3);
			if (p0inside) {
				return new int[] { 0, 1, 2, 0, 2, 3 };
			}
			final boolean p2inside = WB_GeometryOp.pointInTriangleBary3D(p2, p1, p0, p3);
			if (p2inside) {
				return new int[] { 0, 1, 2, 0, 2, 3 };
			}
			return new int[] { 0, 1, 3, 1, 2, 3 };
		}

		/**
		 * @author Michael Bedward
		 */
		private static class EdgeFlipper {
			private final List<Coordinate> shellCoords;

			/**
			 *
			 *
			 * @param shellCoords
			 */
			EdgeFlipper(final List<Coordinate> shellCoords) {
				this.shellCoords = Collections.unmodifiableList(shellCoords);
			}

			/**
			 *
			 *
			 * @param ear0
			 * @param ear1
			 * @param sharedVertices
			 * @return
			 */
			boolean flip(final IndexedTriangle ear0, final IndexedTriangle ear1, final int[] sharedVertices) {
				if (sharedVertices == null || sharedVertices.length != 2) {
					return false;
				}
				final Coordinate shared0 = shellCoords.get(sharedVertices[0]);
				final Coordinate shared1 = shellCoords.get(sharedVertices[1]);
				int[] vertices = ear0.getVertices();
				int i = 0;
				while (vertices[i] == sharedVertices[0] || vertices[i] == sharedVertices[1]) {
					i++;
				}
				final int v0 = vertices[i];
				boolean reverse = false;
				if (vertices[(i + 1) % 3] == sharedVertices[0]) {
					reverse = true;
				}
				final Coordinate c0 = shellCoords.get(v0);
				i = 0;
				vertices = ear1.getVertices();
				while (vertices[i] == sharedVertices[0] || vertices[i] == sharedVertices[1]) {
					i++;
				}
				final int v1 = vertices[i];
				final Coordinate c1 = shellCoords.get(v1);
				final int dir0 = Orientation.index(c0, c1, shared0);
				final int dir1 = Orientation.index(c0, c1, shared1);
				if (dir0 == -dir1) {
					if (c0.distance(c1) < shared0.distance(shared1)) {
						if (reverse) {
							ear0.setPoints(sharedVertices[0], v1, v0);
							ear1.setPoints(v0, v1, sharedVertices[1]);
						} else {
							ear0.setPoints(sharedVertices[0], v0, v1);
							ear1.setPoints(v1, v0, sharedVertices[1]);
						}
						return true;
					}
				}
				return false;
			}
		}

		/**
			 *
			 */
		private static class IndexedTriangle {
			private final int[] points;

			/**
			 *
			 *
			 * @param v0
			 * @param v1
			 * @param v2
			 */
			IndexedTriangle(final int v0, final int v1, final int v2) {
				points = new int[3];
				setPoints(v0, v1, v2);
			}

			/**
			 *
			 *
			 * @param v0
			 * @param v1
			 * @param v2
			 */
			void setPoints(final int v0, final int v1, final int v2) {
				points[0] = v0;
				points[1] = v1;
				points[2] = v2;
			}

			/**
			 *
			 *
			 * @return
			 */
			int[] getVertices() {
				final int[] copy = new int[3];
				for (int i = 0; i < 3; i++) {
					copy[i] = points[i];
				}
				return copy;
			}

			/**
			 *
			 *
			 * @param other
			 * @return
			 */
			int[] getSharedVertices(final IndexedTriangle other) {
				int count = 0;
				final boolean[] shared = new boolean[3];
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						if (points[i] == other.points[j]) {
							count++;
							shared[i] = true;
						}
					}
				}
				int[] common = null;
				if (count > 0) {
					common = new int[count];
					for (int i = 0, k = 0; i < 3; i++) {
						if (shared[i]) {
							common[k++] = points[i];
						}
					}
				}
				return common;
			}
		}

		private List<Coordinate> shellCoords;
		private boolean[] shellCoordAvailable;

		/**
		 *
		 *
		 * @param polygon
		 * @param optimize
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public WB_Triangulation2D triangulatePolygon2D(final WB_Polygon polygon, final boolean optimize) {
			final List<WB_Point> pts = new FastList<>();
			for (int i = 0; i < polygon.numberOfShellPoints; i++) {
				pts.add(polygon.getPoint(i));
			}
			int index = polygon.numberOfShellPoints;
			final List<WB_Point>[] hpts = new FastList[polygon.numberOfContours - 1];
			for (int i = 0; i < polygon.numberOfContours - 1; i++) {
				hpts[i] = new FastList<>();
				for (int j = 0; j < polygon.numberOfPointsPerContour[i + 1]; j++) {
					hpts[i].add(polygon.points.get(index++));
				}
			}
			final WB_Plane P = polygon.getPlane(0);
			/*
			 * if (P.getNormal().getLength() < WB_Epsilon.EPSILON) { P = new
			 * WB_Plane(P.getOrigin(), WB_Vector.Z()); }
			 */
			final WB_Triangulation2DWithPoints triangulation = triangulatePolygon2D(pts, hpts, optimize,
					new WB_GeometryFactory3D().createEmbeddedPlane(P));
			final WB_KDTreeInteger3D<WB_Point> pointmap = new WB_KDTreeInteger3D<>();
			for (int i = 0; i < polygon.numberOfPoints; i++) {
				pointmap.add(polygon.getPoint(i), i);
			}
			final int[] triangles = triangulation.getTriangles();
			final int[] edges = triangulation.getEdges();
			final WB_CoordCollection tripoints = triangulation.getPoints();
			final int[] intmap = new int[tripoints.size()];
			index = 0;
			for (int i = 0; i < tripoints.size(); i++) {
				final int found = pointmap.getNearestNeighbor(tripoints.get(i)).value;
				intmap[index++] = found;
			}
			for (int i = 0; i < triangles.length; i++) {
				triangles[i] = intmap[triangles[i]];
			}
			for (int i = 0; i < edges.length; i++) {
				edges[i] = intmap[edges[i]];
			}
			return new WB_Triangulation2D(triangles, edges);
		}

		/**
		 *
		 *
		 * @param outerPolygon
		 * @param innerPolygons
		 * @param optimize
		 * @param context
		 * @return
		 */
		public WB_Triangulation2DWithPoints triangulatePolygon2D(final List<? extends WB_Coord> outerPolygon,
				final List<? extends WB_Coord>[] innerPolygons, final boolean optimize, final WB_Map2D context) {
			final Coordinate[] coords = new Coordinate[outerPolygon.size() + 1];
			WB_Point point = new WB_Point();
			for (int i = 0; i < outerPolygon.size(); i++) {
				context.mapPoint3D(outerPolygon.get(i), point);
				coords[i] = new Coordinate(point.xd(), point.yd(), 0);
			}
			context.mapPoint3D(outerPolygon.get(0), point);
			coords[outerPolygon.size()] = new Coordinate(point.xd(), point.yd(), 0);
			LinearRing[] holes = null;
			if (innerPolygons != null) {
				holes = new LinearRing[innerPolygons.length];
				for (int j = 0; j < innerPolygons.length; j++) {
					final Coordinate[] icoords = new Coordinate[innerPolygons[j].size() + 1];
					for (int i = 0; i < innerPolygons[j].size(); i++) {
						context.mapPoint3D(innerPolygons[j].get(i), point);
						icoords[i] = new Coordinate(point.xd(), point.yd(), 0);
					}
					context.mapPoint3D(innerPolygons[j].get(0), point);
					icoords[innerPolygons[j].size()] = new Coordinate(point.xd(), point.yd(), 0);
					final LinearRing hole = JTSgf.createLinearRing(icoords);
					holes[j] = hole;
				}
			}
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final Polygon inputPolygon = JTSgf.createPolygon(shell, holes);
			final int[] ears = triangulate(inputPolygon, optimize);
			final int[] E = extractEdgesTri(ears);
			final List<WB_Point> Points = new FastList<>();
			for (int i = 0; i < shellCoords.size() - 1; i++) {
				point = new WB_Point();
				context.unmapPoint2D(shellCoords.get(i).x, shellCoords.get(i).y, point);
				Points.add(point);
			}
			return new WB_Triangulation2DWithPoints(ears, E, Points);
		}
		
		
		public int[] triangulateHEFace(final HE_Face face,
				final boolean optimize) {
			WB_CoordCollection vertices=WB_CoordCollection.getCollection(face);
			WB_CoordCollection orthoVertices=vertices.map(new WB_OrthoProject(HE_MeshOp.getFaceNormal(face)));
			final Coordinate[] coords = new Coordinate[vertices.size() + 1];
			final WB_KDTreeInteger2D<WB_Coord> pointmap = new WB_KDTreeInteger2D<>();
			for (int i = 0; i < vertices.size(); i++) {
				pointmap.add(orthoVertices.get(i), i);
				coords[i] = new Coordinate(orthoVertices.getX(i), orthoVertices.getY(i), 0);
			}
			
			coords[vertices.size()] = new Coordinate(orthoVertices.getX(0), orthoVertices.getY(0), 0);
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final Polygon inputPolygon = JTSgf.createPolygon(shell);
			final int[] ears = triangulate(inputPolygon, optimize);
			final int[] intmap = new int[shellCoords.size() - 1];
			int index = 0;
			for (int i = 0; i < shellCoords.size() - 1; i++) {
				final int found = pointmap.getNearestNeighbor(shellCoords.get(i).x,shellCoords.get(i).y).value;
				intmap[index++] = found;
			}
			for (int i = 0; i <ears.length; i++) {
				ears[i] = intmap[ears[i]];
			}

			return ears;
		}
		
		

		/**
		 *
		 *
		 * @param polygon
		 * @param points
		 * @param optimize
		 * @param context
		 * @return
		 */
		public WB_Triangulation2DWithPoints triangulatePolygon2D(final int[] polygon, final WB_Coord[] points,
				final boolean optimize, final WB_Map2D context) {
			final Coordinate[] coords = new Coordinate[polygon.length + 1];
			WB_Point point = new WB_Point();
			for (int i = 0; i < polygon.length; i++) {
				context.mapPoint3D(points[polygon[i]], point);
				coords[i] = new Coordinate(point.xd(), point.yd(), i);
			}
			context.mapPoint3D(points[polygon[0]], point);
			coords[polygon.length] = new Coordinate(point.xd(), point.yd(), 0);
			final Polygon inputPolygon = JTSgf.createPolygon(coords);
			final int[] ears = triangulate(inputPolygon, optimize);
			for (int i = 0; i < ears.length; i++) {
				ears[i] = polygon[ears[i]];
			}
			final int[] E = extractEdgesTri(ears);
			final List<WB_Point> Points = new FastList<>();
			for (int i = 0; i < shellCoords.size() - 1; i++) {
				point = new WB_Point();
				context.unmapPoint2D(shellCoords.get(i).x, shellCoords.get(i).y, point);
				Points.add(point);
			}
			return new WB_Triangulation2DWithPoints(ears, E, Points);
		}

		/**
		 *
		 *
		 * @param polygon
		 * @param points
		 * @param optimize
		 * @param context
		 * @return
		 */
		public WB_Triangulation2D triangulatePolygon2D(final int[] polygon, final List<? extends WB_Coord> points,
				final boolean optimize, final WB_Map2D context) {
			final Coordinate[] coords = new Coordinate[polygon.length + 1];
			final WB_Point point = new WB_Point();
			for (int i = 0; i < polygon.length; i++) {
				context.mapPoint3D(points.get(polygon[i]), point);
				coords[i] = new Coordinate(point.xd(), point.yd(), polygon[i]);
			}
			context.mapPoint3D(points.get(polygon[0]), point);
			coords[polygon.length] = new Coordinate(point.xd(), point.yd(), polygon[0]);
			final Polygon inputPolygon = JTSgf.createPolygon(coords);
			final int[] ears = triangulate(inputPolygon, optimize);
			for (int i = 0; i < ears.length; i++) {
				ears[i] = (int) shellCoords.get(ears[i]).getZ();
			}
			final int[] E = extractEdgesTri(ears);
			return new WB_Triangulation2D(ears, E);
		}

		public int[] triangulateFace(final HE_Mesh mesh, final HE_Face face, final boolean optimize) {
			final int fo = face.getFaceDegree();
			final Coordinate[] coords = new Coordinate[fo + 1];
			final WB_Coord normal = mesh.getFaceNormal(face);
			WB_Swizzle coordViewer;
			if (Math.abs(normal.xd()) > Math.abs(normal.yd())) {
				coordViewer = Math.abs(normal.xd()) > Math.abs(normal.zd()) ? WB_Swizzle.YZ : WB_Swizzle.XY;
			} else {
				coordViewer = Math.abs(normal.yd()) > Math.abs(normal.zd()) ? WB_Swizzle.ZX : WB_Swizzle.XY;
			}
			final WB_KDTreeInteger3D<WB_Point> pointmap = new WB_KDTreeInteger3D<>();
			int i = 0;
			HE_Halfedge he = face.getHalfedge();
			do {
				coords[i] = new Coordinate(coordViewer.xd(he.getVertex()), coordViewer.yd(he.getVertex()), 0);
				pointmap.add(new WB_Point(coordViewer.xd(he.getVertex()), coordViewer.yd(he.getVertex())), i);
				i++;
				he = he.getNextInFace();
			} while (he != face.getHalfedge());
			coords[i] = new Coordinate(coordViewer.xd(he.getVertex()), coordViewer.yd(he.getVertex()), 0);
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final Polygon inputPolygon = JTSgf.createPolygon(shell);
			final int[] ears = triangulate(inputPolygon, optimize);
			final List<WB_Point> tripoints = new FastList<>();
			for (int j = 0; j < shellCoords.size() - 1; j++) {
				tripoints.add(new WB_Point(shellCoords.get(j).x, shellCoords.get(j).y));
			}
			final int[] intmap = new int[tripoints.size()];
			i = 0;
			for (final WB_Coord point : tripoints) {
				final int found = pointmap.getNearestNeighbor(point).value;
				intmap[i++] = found;
			}
			for (int j = 0; j < ears.length; j++) {
				ears[j] = intmap[ears[j]];
			}
			return ears;
		}

		public int[] triangulateSimplePolygon(final WB_Polygon polygon, final boolean optimize) {
			final int fo = polygon.getNumberOfShellPoints();
			final Coordinate[] coords = new Coordinate[fo + 1];
			final WB_Coord normal = polygon.getNormal();
			WB_Swizzle coordViewer;
			if (Math.abs(normal.xd()) > Math.abs(normal.yd())) {
				coordViewer = Math.abs(normal.xd()) > Math.abs(normal.zd()) ? WB_Swizzle.YZ : WB_Swizzle.XY;
			} else {
				coordViewer = Math.abs(normal.yd()) > Math.abs(normal.zd()) ? WB_Swizzle.ZX : WB_Swizzle.XY;
			}
			final WB_KDTreeInteger3D<WB_Point> pointmap = new WB_KDTreeInteger3D<>();
			WB_Coord c;
			for (int i = 0; i <= polygon.getNumberOfShellPoints(); i++) {
				c = polygon.getPoint(i);
				coords[i] = new Coordinate(coordViewer.xd(c), coordViewer.yd(c), 0);
				pointmap.add(new WB_Point(coordViewer.xd(c), coordViewer.yd(c)), i);
				i++;
			}
			final LinearRing shell = JTSgf.createLinearRing(coords);
			final Polygon inputPolygon = JTSgf.createPolygon(shell);
			final int[] ears = triangulate(inputPolygon, optimize);
			final List<WB_Point> tripoints = new FastList<>();
			for (int j = 0; j < shellCoords.size() - 1; j++) {
				tripoints.add(new WB_Point(shellCoords.get(j).x, shellCoords.get(j).y));
			}
			final int[] intmap = new int[tripoints.size()];
			int i = 0;
			for (final WB_Coord point : tripoints) {
				final int found = pointmap.getNearestNeighbor(point).value;
				intmap[i++] = found;
			}
			for (int j = 0; j < ears.length; j++) {
				ears[j] = intmap[ears[j]];
			}
			return ears;
		}

		/**
		 *
		 *
		 * @param ears
		 * @return
		 */
		private static int[] extractEdgesTri(final int[] ears) {
			final int f = ears.length;
			final UnifiedMap<Long, int[]> map = new UnifiedMap<>();
			for (int i = 0; i < ears.length; i += 3) {
				final int v0 = ears[i];
				final int v1 = ears[i + 1];
				final int v2 = ears[i + 2];
				long index = getIndex(v0, v1, f);
				map.put(index, new int[] { v0, v1 });
				index = getIndex(v1, v2, f);
				map.put(index, new int[] { v1, v2 });
				index = getIndex(v2, v0, f);
				map.put(index, new int[] { v2, v0 });
			}
			final int[] edges = new int[2 * map.size()];
			final Collection<int[]> values = map.values();
			int i = 0;
			for (final int[] value : values) {
				edges[2 * i] = value[0];
				edges[2 * i + 1] = value[1];
				i++;
			}
			return edges;
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param f
		 * @return
		 */
		private static long getIndex(final int i, final int j, final int f) {
			return i > j ? j + i * f : i + j * f;
		}

		/**
		 * Perform the triangulation.
		 *
		 * @param inputPolygon
		 * @param improve      if true, improvement of the triangulation is attempted as
		 *                     a post-processing step
		 * @return GeometryCollection of triangular polygons
		 */
		private int[] triangulate(final Polygon inputPolygon, final boolean improve) {
			final List<IndexedTriangle> earList = new ArrayList<>();
			createShell(inputPolygon);
			final Geometry test = inputPolygon.buffer(0);
			int N = shellCoords.size() - 1;
			shellCoordAvailable = new boolean[N];
			Arrays.fill(shellCoordAvailable, true);
			boolean finished = false;
			boolean found = false;
			int k0 = 0;
			int k1 = 1;
			int k2 = 2;
			int firstK = 0;
			do {
				found = false;
				while (Orientation.index(shellCoords.get(k0), shellCoords.get(k1), shellCoords.get(k2)) == 0) {
					k0 = k1;
					if (k0 == firstK) {
						finished = true;
						break;
					}
					k1 = k2;
					k2 = nextShellCoord(k2 + 1);
				}
				if (!finished && isValidEdge(k0, k2)) {
					final LineString ls = JTSgf
							.createLineString(new Coordinate[] { shellCoords.get(k0), shellCoords.get(k2) });
					if (test.covers(ls)) {
						final Polygon earPoly = JTSgf.createPolygon(JTSgf.createLinearRing(new Coordinate[] {
								shellCoords.get(k0), shellCoords.get(k1), shellCoords.get(k2), shellCoords.get(k0) }),
								null);
						if (test.covers(earPoly)) {
							found = true;
							// System.out.println(earPoly);
							final IndexedTriangle ear = new IndexedTriangle(k0, k1, k2);
							earList.add(ear);
							shellCoordAvailable[k1] = false;
							N--;
							k0 = nextShellCoord(0);
							k1 = nextShellCoord(k0 + 1);
							k2 = nextShellCoord(k1 + 1);
							firstK = k0;
							if (N < 3) {
								finished = true;
							}
						}
					}
				}
				if (!finished && !found) {
					k0 = k1;
					if (k0 == firstK) {
						finished = true;
					} else {
						k1 = k2;
						k2 = nextShellCoord(k2 + 1);
					}
				}
			} while (!finished);
			if (improve) {// && inputPolygon.getNumInteriorRing() == 0) {
				doImprove(earList);
			}
			final int[] tris = new int[3 * earList.size()];
			for (int i = 0; i < earList.size(); i++) {
				final int[] tri = earList.get(i).getVertices();
				// final boolean flip = true;
				/*
				 * if (improve) { if (Orientation.orientationIndex(shellCoords.get(tri[0]),
				 * shellCoords.get(tri[1]), shellCoords.get(tri[2])) > 0) { flip = false; } } if
				 * (flip) {
				 */
				tris[3 * i] = tri[0];
				tris[3 * i + 1] = tri[1];
				tris[3 * i + 2] = tri[2];
				/*
				 * } else { tris[i][0] = tri[0]; tris[i][1] = tri[1]; tris[i][2] = tri[2]; }
				 */
			}
			return tris;
		}

		/**
		 *
		 *
		 * @param polygon
		 * @return
		 */
		protected WB_Polygon makeSimplePolygon(final WB_Polygon polygon) {
			final Polygon poly = toJTSPolygon2D(polygon);
			createShell(poly);
			final Coordinate[] coords = new Coordinate[shellCoords.size()];
			return createPolygonFromJTSPolygon2D(JTSgf.createPolygon(shellCoords.toArray(coords)));
		}

		/**
		 * Transforms the input polygon into a single, possible self-intersecting shell
		 * by connecting holes to the exterior ring, The holes are added from the lowest
		 * upwards. As the resulting shell develops, a hole might be added to what was
		 * originally another hole.
		 *
		 * @param inputPolygon
		 */
		protected void createShell(final Polygon inputPolygon) {
			final Polygon poly = (Polygon) inputPolygon.copy();
			// Normalization changes the order of the vertices and messes up any
			// indexed scheme
			// Not sure if commenting out line will give later side effects...
			// poly.normalize();
			shellCoords = new ArrayList<>();
			final List<Geometry> orderedHoles = getOrderedHoles(poly);
			final Coordinate[] coords = poly.getExteriorRing().getCoordinates();
			shellCoords.addAll(Arrays.asList(coords));
			for (final Geometry orderedHole : orderedHoles) {
				joinHoleToShell(inputPolygon, orderedHole);
			}
		}

		/**
		 * Check if a candidate edge between two vertices passes through any other
		 * available vertices.
		 *
		 * @param index0 first vertex
		 * @param index1 second vertex
		 * @return true if the edge does not pass through any other available vertices;
		 *         false otherwise
		 */
		private boolean isValidEdge(final int index0, final int index1) {
			final Coordinate[] line = { shellCoords.get(index0), shellCoords.get(index1) };
			int index = nextShellCoord(index0 + 1);
			while (index != index0) {
				if (index != index1) {
					final Coordinate c = shellCoords.get(index);
					if (!(c.equals2D(line[0]) || c.equals2D(line[1]))) {
						if (PointLocation.isOnLine(c, line)) {
							return false;
						}
					}
				}
				index = nextShellCoord(index + 1);
			}
			return true;
		}

		/**
		 * Get the index of the next available shell coordinate starting from the given
		 * candidate position.
		 *
		 * @param pos candidate position
		 * @return index of the next available shell coordinate
		 */
		private int nextShellCoord(final int pos) {
			int pnew = pos % shellCoordAvailable.length;
			while (!shellCoordAvailable[pnew]) {
				pnew = (pnew + 1) % shellCoordAvailable.length;
			}
			return pnew;
		}

		/**
		 * Attempts to improve the triangulation by examining pairs of triangles with a
		 * common edge, forming a quadrilateral, and testing if swapping the diagonal of
		 * this quadrilateral would produce two new triangles with larger minimum
		 * interior angles.
		 *
		 * @param earList
		 */
		private void doImprove(final List<IndexedTriangle> earList) {
			final EdgeFlipper ef = new EdgeFlipper(shellCoords);
			boolean changed;
			do {
				changed = false;
				for (int i = 0; i < earList.size() - 1 && !changed; i++) {
					final IndexedTriangle ear0 = earList.get(i);
					for (int j = i + 1; j < earList.size() && !changed; j++) {
						final IndexedTriangle ear1 = earList.get(j);
						final int[] sharedVertices = ear0.getSharedVertices(ear1);
						if (sharedVertices != null && sharedVertices.length == 2) {
							if (ef.flip(ear0, ear1, sharedVertices)) {
								changed = true;
							}
						}
					}
				}
			} while (changed);
		}

		/**
		 * Returns a list of holes in the input polygon (if any) ordered by y coordinate
		 * with ties broken using x coordinate.
		 *
		 * @param poly input polygon
		 * @return a list of Geometry objects representing the ordered holes (may be
		 *         empty)
		 */
		private static List<Geometry> getOrderedHoles(final Polygon poly) {
			final List<Geometry> holes = new ArrayList<>();
			final List<IndexedEnvelope> bounds = new ArrayList<>();
			if (poly.getNumInteriorRing() > 0) {
				for (int i = 0; i < poly.getNumInteriorRing(); i++) {
					bounds.add(new IndexedEnvelope(i, poly.getInteriorRingN(i).getEnvelopeInternal()));
				}
				Collections.sort(bounds, new IndexedEnvelopeComparator());
				for (final IndexedEnvelope bound : bounds) {
					holes.add(poly.getInteriorRingN(bound.index));
				}
			}
			return holes;
		}

		/**
		 * Join a given hole to the current shell. The hole coordinates are inserted
		 * into the list of shell coordinates.
		 *
		 * @param inputPolygon
		 * @param hole         the hole to join
		 */
		private void joinHoleToShell(final Polygon inputPolygon, final Geometry hole) {
			double minD2 = Double.MAX_VALUE;
			int shellVertexIndex = -1;
			final int Ns = shellCoords.size() - 1;
			final int holeVertexIndex = getLowestVertex(hole);
			final Coordinate[] holeCoords = hole.getCoordinates();
			final Coordinate ch = holeCoords[holeVertexIndex];
			final List<IndexedDouble> distanceList = new ArrayList<>();
			/*
			 * Note: it's important to scan the shell vertices in reverse so that if a hole
			 * ends up being joined to what was originally another hole, the previous hole's
			 * coordinates appear in the shell before the new hole's coordinates (otherwise
			 * the triangulation algorithm tends to get stuck).
			 */
			for (int i = Ns - 1; i >= 0; i--) {
				final Coordinate cs = shellCoords.get(i);
				final double d2 = (ch.x - cs.x) * (ch.x - cs.x) + (ch.y - cs.y) * (ch.y - cs.y);
				if (d2 < minD2) {
					minD2 = d2;
					shellVertexIndex = i;
				}
				distanceList.add(new IndexedDouble(i, d2));
			}
			/*
			 * Try a quick join: if the closest shell vertex is reachable without crossing
			 * any holes.
			 */
			LineString join = JTSgf.createLineString(new Coordinate[] { ch, shellCoords.get(shellVertexIndex) });
			if (inputPolygon.covers(join)) {
				doJoinHole(shellVertexIndex, holeCoords, holeVertexIndex);
				return;
			}
			/*
			 * Quick join didn't work. Sort the shell coords on distance to the hole vertex
			 * nnd choose the closest reachable one.
			 */
			Collections.sort(distanceList, new IndexedDoubleComparator());
			for (int i = 1; i < distanceList.size(); i++) {
				join = JTSgf.createLineString(new Coordinate[] { ch, shellCoords.get(distanceList.get(i).index) });
				if (inputPolygon.covers(join)) {
					shellVertexIndex = distanceList.get(i).index;
					doJoinHole(shellVertexIndex, holeCoords, holeVertexIndex);
					return;
				}
			}
			// throw new IllegalStateException("Failed to join hole to shell");
		}

		/**
		 * Helper method for joinHoleToShell. Insert the hole coordinates into the shell
		 * coordinate list.
		 *
		 *
		 * @param shellVertexIndex insertion point in the shell coordinate list
		 * @param holeCoords       array of hole coordinates
		 * @param holeVertexIndex  attachment point of hole
		 */
		private void doJoinHole(final int shellVertexIndex, final Coordinate[] holeCoords, final int holeVertexIndex) {
			final List<Coordinate> newCoords = new ArrayList<>();
			newCoords.add(new Coordinate(shellCoords.get(shellVertexIndex)));
			final int N = holeCoords.length - 1;
			int i = holeVertexIndex;
			do {
				newCoords.add(new Coordinate(holeCoords[i]));
				i = (i + 1) % N;
			} while (i != holeVertexIndex);
			newCoords.add(new Coordinate(holeCoords[holeVertexIndex]));
			shellCoords.addAll(shellVertexIndex, newCoords);
		}

		/**
		 * Return the index of the lowest vertex.
		 *
		 * @param geom input geometry
		 * @return index of the first vertex found at lowest point of the geometry
		 */
		private static int getLowestVertex(final Geometry geom) {
			final Coordinate[] coords = geom.getCoordinates();
			final double minY = geom.getEnvelopeInternal().getMinY();
			for (int i = 0; i < coords.length; i++) {
				if (Math.abs(coords[i].y - minY) < WB_Epsilon.EPSILON) {
					return i;
				}
			}
			throw new IllegalStateException("Failed to find lowest vertex");
		}

		/**
		 *
		 */
		private static class IndexedEnvelope {
			/**
			 *
			 */
			int index;
			/**
			 *
			 */
			Envelope envelope;

			/**
			 *
			 *
			 * @param i
			 * @param env
			 */
			IndexedEnvelope(final int i, final Envelope env) {
				index = i;
				envelope = env;
			}
		}

		/**
		 *
		 */
		private static class IndexedEnvelopeComparator implements Comparator<IndexedEnvelope> {
			/*
			 * (non-Javadoc)
			 *
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(final IndexedEnvelope o1, final IndexedEnvelope o2) {
				double delta = o1.envelope.getMinY() - o2.envelope.getMinY();
				if (Math.abs(delta) < WB_Epsilon.EPSILON) {
					delta = o1.envelope.getMinX() - o2.envelope.getMinX();
					if (Math.abs(delta) < WB_Epsilon.EPSILON) {
						return 0;
					}
				}
				return delta > 0 ? 1 : -1;
			}
		}

		/**
		 *
		 */
		private static class IndexedDouble {
			/**
			 *
			 */
			int index;
			/**
			 *
			 */
			double value;

			/**
			 *
			 *
			 * @param i
			 * @param v
			 */
			IndexedDouble(final int i, final double v) {
				index = i;
				value = v;
			}
		}

		/**
		 *
		 */
		private static class IndexedDoubleComparator implements Comparator<IndexedDouble> {
			/*
			 * (non-Javadoc)
			 *
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(final IndexedDouble o1, final IndexedDouble o2) {
				final double delta = o1.value - o2.value;
				if (Math.abs(delta) < WB_Epsilon.EPSILON) {
					return 0;
				}
				return delta > 0 ? 1 : -1;
			}
		}
	}
	/*
	 * Conversion of ShapeReader class by Frederik Vanhoutte (W:Blut) The JTS
	 * Topology Suite is a collection of Java classes that implement the fundamental
	 * operations required to validate a given geo-spatial data set to a known
	 * topological specification. Copyright (C) 2001 Vivid Solutions This library is
	 * free software; you can redistribute it and/or modify it under the terms of
	 * the GNU Lesser General Public License as published by the Free Software
	 * Foundation; either version 2.1 of the License, or (at your option) any later
	 * version. This library is distributed in the hope that it will be useful, but
	 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
	 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General License for more
	 * details. You should have received a copy of the GNU Lesser General Public
	 * License along with this library; if not, write to the Free Software
	 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA For
	 * more information, contact: Vivid Solutions Suite #1A 2328 Government Street
	 * Victoria BC V8T 5G5 Canada (250)385-6040 www.vividsolutions.com
	 */

	/**
	 * Converts a Java2D shape or the more general PathIterator into a List of
	 * WB_polygon.
	 * <p>
	 * The coordinate system for Java2D is typically screen coordinates, which has
	 * the Y axis inverted relative to the usual coordinate system.
	 * <p>
	 * PathIterators to be converted are expected to be linear or flat. That is,
	 * they should contain only <tt>SEG_MOVETO</tt>, <tt>SEG_LINETO</tt>, and
	 * <tt>SEG_CLOSE</tt> segment types. Any other segment types will cause an
	 * exception.
	 *
	 * @author Martin Davis
	 * @author Frederik Vanhoutte (W:Blut)
	 *
	 */
	static class ShapeReader {
		/**
		 *
		 */
		private static AffineTransform INVERT_Y = AffineTransform.getScaleInstance(1, -1);

		/**
		 *
		 */
		/**
		 *
		 */
		ShapeReader() {
		}

		/**
		 *
		 *
		 * @param shp
		 * @param flatness
		 * @return
		 */
		List<WB_Polygon> read(final Shape shp, final double flatness) {
			final PathIterator pathIt = shp.getPathIterator(INVERT_Y, flatness);
			return read(pathIt);
		}

		/**
		 *
		 *
		 * @param pathIt
		 * @return
		 */
		List<WB_Polygon> read(final PathIterator pathIt) {
			final List<Coordinate[]> pathPtSeq = toCoordinates(pathIt);
			final RingTree tree = new RingTree();
			for (final Coordinate[] pts : pathPtSeq) {
				final LinearRing ring = JTSgf.createLinearRing(pts);
				tree.add(ring);
			}
			return tree.extractPolygons();
		}

		/**
		 *
		 *
		 * @param pathIt
		 * @return
		 */
		private static List<Coordinate[]> toCoordinates(final PathIterator pathIt) {
			final List<Coordinate[]> coordArrays = new ArrayList<>();
			while (!pathIt.isDone()) {
				final Coordinate[] pts = nextCoordinateArray(pathIt);
				if (pts == null) {
					break;
				}
				coordArrays.add(pts);
			}
			return coordArrays;
		}

		/**
		 *
		 *
		 * @param pathIt
		 * @return
		 */
		private static Coordinate[] nextCoordinateArray(final PathIterator pathIt) {
			final double[] pathPt = new double[6];
			CoordinateList coordList = null;
			boolean isDone = false;
			while (!pathIt.isDone()) {
				final int segType = pathIt.currentSegment(pathPt);
				switch (segType) {
				case PathIterator.SEG_MOVETO:
					if (coordList != null) {
						// don't advance pathIt, to retain start of next
						// path if
						// any
						isDone = true;
					} else {
						coordList = new CoordinateList();
						coordList.add(new Coordinate(pathPt[0], pathPt[1]));
						pathIt.next();
					}
					break;
				case PathIterator.SEG_LINETO:
					coordList.add(new Coordinate(pathPt[0], pathPt[1]));
					pathIt.next();
					break;
				case PathIterator.SEG_CLOSE:
					coordList.closeRing();
					pathIt.next();
					isDone = true;
					break;
				default:
					throw new IllegalArgumentException("unhandled (non-linear) segment type encountered");
				}
				if (isDone) {
					break;
				}
			}
			return coordList.toCoordinateArray();
		}

		// The JTS implementation of ShapeReader does not handle overlapping
		// polygons well. All code below this point is my solution for this. A
		// hierarchical tree that orders rings from the outside in. All input
		// has to
		// be well-ordered: CW for shell, CCW for hole.
		/**
		 *
		 */
		private static class RingNode {
			/**
			 *
			 */
			@SuppressWarnings("unused")
			RingNode parent;
			/**
			 *
			 */
			List<RingNode> children;
			/**
			 *
			 */
			LinearRing ring;
			/**
			 *
			 */
			Polygon poly; // redundant, but useful for
							// within/contains
							// checks
			/**
			 *
			 */
			boolean hole;

			/**
			 *
			 */
			RingNode() {
				parent = null;
				ring = null;
				children = new ArrayList<>();
				hole = true;
			}

			/**
			 *
			 *
			 * @param parent
			 * @param ring
			 */
			RingNode(final RingNode parent, final LinearRing ring) {
				this.parent = parent;
				this.ring = ring;
				final Coordinate[] coords = ring.getCoordinates();
				poly = JTSgf.createPolygon(coords);
				hole = Orientation.isCCW(coords);
				children = new ArrayList<>();
			}
		}

		/**
		 *
		 */
		private class RingTree {
			/**
			 *
			 */
			RingNode root;

			/**
			 *
			 */
			RingTree() {
				root = new RingNode();
			}

			/**
			 *
			 *
			 * @param ring
			 */
			void add(final LinearRing ring) {
				final Polygon poly = JTSgf.createPolygon(ring);
				RingNode currentParent = root;
				RingNode foundParent;
				do {
					foundParent = null;
					for (final RingNode node : currentParent.children) {
						final Polygon other = node.poly;
						if (poly.within(other)) {
							foundParent = node;
							currentParent = node;
							break;
						}
					}
				} while (foundParent != null);
				final RingNode newNode = new RingNode(currentParent, ring);
				final List<RingNode> nodesToRemove = new ArrayList<>();
				for (int i = 0; i < currentParent.children.size(); i++) {
					final RingNode node = currentParent.children.get(i);
					final Polygon other = node.poly;
					if (other.within(poly)) {
						newNode.children.add(node);
						nodesToRemove.add(node);
					}
				}
				currentParent.children.removeAll(nodesToRemove);
				currentParent.children.add(newNode);
			}

			/**
			 *
			 *
			 * @return
			 */
			List<WB_Polygon> extractPolygons() {
				final List<WB_Polygon> polygons = new ArrayList<>();
				final List<RingNode> shellNodes = new ArrayList<>();
				addExteriorNodes(root, shellNodes);
				for (final RingNode node : shellNodes) {
					int count = 0;
					for (final RingNode element : node.children) {
						if (element.hole) {
							count++;
						}
					}
					final LinearRing[] holes = new LinearRing[count];
					int index = 0;
					for (final RingNode element : node.children) {
						if (element.hole) {
							holes[index++] = element.ring;
						}
					}
					final Geometry result = JTSgf.createPolygon(node.ring, holes);
					if (result.getGeometryType().equals("Polygon")) {
						polygons.add(createPolygonFromJTSPolygon2D((Polygon) result));
					} else if (result.getGeometryType().equals("MultiPolygon")) {
						for (int j = 0; j < result.getNumGeometries(); j++) {
							final Geometry ggeo = result.getGeometryN(j);
							polygons.add(createPolygonFromJTSPolygon2D((Polygon) ggeo));
						}
					}
				}
				return polygons;
			}

			/**
			 *
			 *
			 * @param parent
			 * @param shellNodes
			 */
			void addExteriorNodes(final RingNode parent, final List<RingNode> shellNodes) {
				for (final RingNode node : parent.children) {
					if (node.hole == false) {
						shellNodes.add(node);
					}
					addExteriorNodes(node, shellNodes);
				}
			}
		}
	}

	static WB_Triangulation2D triangulate2D(final WB_CoordCollection points) {
		final int n = points.size();
		final List<Coordinate> coords = new FastList<>();
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			coords.add(new Coordinate(c.xd(), c.yd(), i));
		}
		final WB_Triangulation2D result = getTriangles2D(coords);
		return result;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	static WB_Triangulation2D getTriangles2D(final List<Coordinate> coords) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qesd = dtb.getSubdivision();
		final GeometryCollection tris = (GeometryCollection) qesd.getTriangles(JTSgf);
		final int ntris = tris.getNumGeometries();
		List<int[]> result = new FastList<>();
		for (int i = 0; i < ntris; i++) {
			final Polygon tri = (Polygon) tris.getGeometryN(i);
			final Coordinate[] tricoord = tri.getCoordinates();
			final int[] triind = new int[3];
			for (int j = 0; j < 3; j++) {
				triind[j] = (int) tricoord[j].getZ();
			}
			result.add(triind);
		}
		final int[] T = new int[3 * result.size()];
		for (int i = 0; i < result.size(); i++) {
			T[3 * i] = result.get(i)[0];
			T[3 * i + 1] = result.get(i)[1];
			T[3 * i + 2] = result.get(i)[2];
		}
		final MultiLineString edges = (MultiLineString) qesd.getEdges(JTSgf);
		final int nedges = edges.getNumGeometries();
		result = new FastList<>();
		for (int i = 0; i < nedges; i++) {
			final LineString edge = (LineString) edges.getGeometryN(i);
			final Coordinate[] edgecoord = edge.getCoordinates();
			final int[] edgeind = new int[2];
			for (int j = 0; j < 2; j++) {
				edgeind[j] = (int) edgecoord[j].getZ();
			}
			result.add(edgeind);
		}
		final int[] E = new int[2 * result.size()];
		for (int i = 0; i < result.size(); i++) {
			E[2 * i] = result.get(i)[0];
			E[2 * i + 1] = result.get(i)[1];
		}
		return new WB_Triangulation2D(T, E);
	}

	static WB_Triangulation2DWithPoints getConformingTriangles2D(final Coordinate[] coords, final int[] constraints,
			final double tol) {
		final int m = constraints.length;
		final LineString[] constraintlines = new LineString[m / 2];
		for (int i = 0; i < m; i += 2) {
			final Coordinate[] pair = { coords[constraints[i]], coords[constraints[i + 1]] };
			constraintlines[i / 2] = JTSgf.createLineString(pair);
		}
		final ConformingDelaunayTriangulationBuilder dtb = new ConformingDelaunayTriangulationBuilder();
		dtb.setTolerance(tol);
		dtb.setSites(JTSgf.createMultiPointFromCoords(coords));
		dtb.setConstraints(JTSgf.createMultiLineString(constraintlines));
		final QuadEdgeSubdivision qesd = dtb.getSubdivision();
		final GeometryCollection tris = (GeometryCollection) qesd.getTriangles(JTSgf);
		final Coordinate[] newcoords = tris.getCoordinates();
		final List<WB_Coord> uniquePoints = new FastList<>();
		final WB_KDTreeInteger3D<WB_Point> tree = new WB_KDTreeInteger3D<>();
		int currentSize = 0;
		for (final Coordinate newcoord : newcoords) {
			final WB_Point p = new WB_Point(newcoord.x, newcoord.y, 0);
			final Integer index = tree.add(p, currentSize);
			if (index == -1) {
				currentSize++;
				uniquePoints.add(p);
			}
		}
		final int ntris = tris.getNumGeometries();
		List<int[]> result = new FastList<>();
		for (int i = 0; i < ntris; i++) {
			final Polygon tri = (Polygon) tris.getGeometryN(i);
			final Coordinate[] tricoord = tri.getCoordinates();
			final int[] triind = new int[3];
			for (int j = 0; j < 3; j++) {
				triind[j] = tree.add(new WB_Point(tricoord[j].x, tricoord[j].y, 0), 0);
			}
			result.add(triind);
		}
		final int[] T = new int[3 * result.size()];
		for (int i = 0; i < result.size(); i++) {
			T[3 * i] = result.get(i)[0];
			T[3 * i + 1] = result.get(i)[1];
			T[3 * i + 2] = result.get(i)[2];
		}
		final MultiLineString edges = (MultiLineString) qesd.getEdges(JTSgf);
		final int nedges = edges.getNumGeometries();
		result = new FastList<>();
		for (int i = 0; i < nedges; i++) {
			final LineString edge = (LineString) edges.getGeometryN(i);
			final Coordinate[] edgecoord = edge.getCoordinates();
			final int[] edgeind = new int[2];
			for (int j = 0; j < 2; j++) {
				edgeind[j] = tree.add(new WB_Point(edgecoord[j].x, edgecoord[j].y, 0), 0);
			}
			result.add(edgeind);
		}
		final int[] E = new int[2 * result.size()];
		for (int i = 0; i < result.size(); i++) {
			E[2 * i] = result.get(i)[0];
			E[2 * i + 1] = result.get(i)[1];
		}
		final List<WB_Coord> Points = new FastList<>();
		for (int i = 0; i < uniquePoints.size(); i++) {
			Points.add(uniquePoints.get(i));
		}
		return new WB_Triangulation2DWithPoints(T, E, Points);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points) {
		final int n = points.size();
		final int[] constraints = new int[2 * n];
		for (int i = 0, j = n - 1; i < n; j = i++) {
			constraints[2 * i] = j;
			constraints[2 * i + 1] = i;
		}
		final Coordinate[] coords = new Coordinate[n];
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			coords[i] = new Coordinate(c.xd(), c.yd(), i);
		}
		return getConformingTriangles2D(coords, constraints, WB_Epsilon.EPSILON);
	}

	/**
	 *
	 * @param points
	 * @param tolerance
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final double tolerance) {
		final int n = points.size();
		final int[] constraints = new int[2 * n];
		for (int i = 0, j = n - 1; i < n; j = i++) {
			constraints[2 * i] = j;
			constraints[2 * i + 1] = i;
		}
		final Coordinate[] coords = new Coordinate[n];
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			coords[i] = new Coordinate(c.xd(), c.yd(), i);
		}
		return getConformingTriangles2D(coords, constraints, tolerance);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final int[] constraints) {
		if (constraints == null) {
			return new WB_Triangulation2DWithPoints(WB_TriangulationFactory2D.triangulate2D(points));
		}
		final int m = constraints.length;
		if (m == 0 || m % 2 == 1) {
			return new WB_Triangulation2DWithPoints(WB_TriangulationFactory2D.triangulate2D(points));
		}
		final int n = points.size();
		final Coordinate[] coords = new Coordinate[n];
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			coords[i] = new Coordinate(c.xd(), c.yd(), i);
		}
		return getConformingTriangles2D(coords, constraints, WB_Epsilon.EPSILON);
	}

	/**
	 *
	 * @param points
	 * @param constraints
	 * @param tolerance
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_CoordCollection points,
			final int[] constraints, final double tolerance) {
		if (constraints == null) {
			return new WB_Triangulation2DWithPoints(WB_TriangulationFactory2D.triangulate2D(points));
		}
		final int m = constraints.length;
		if (m == 0 || m % 2 == 1) {
			return new WB_Triangulation2DWithPoints(WB_TriangulationFactory2D.triangulate2D(points));
		}
		final int n = points.size();
		final Coordinate[] coords = new Coordinate[n];
		WB_Coord c;
		for (int i = 0; i < n; i++) {
			c = points.get(i);
			coords[i] = new Coordinate(c.xd(), c.yd(), i);
		}
		return getConformingTriangles2D(coords, constraints, tolerance);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final WB_Map2D context) {
		final int n = points.size();
		final int[] constraints = new int[2 * n];
		for (int i = 0, j = n - 1; i < n; j = i++) {
			constraints[2 * i] = j;
			constraints[2 * i + 1] = i;
		}
		final Coordinate[] coords = new Coordinate[n];
		final WB_Point point = new WB_Point();
		int i = 0;
		for (final WB_Coord p : points) {
			context.mapPoint3D(p, point);
			coords[i] = new Coordinate(point.xd(), point.yd(), i);
			i++;
		}
		return getConformingTriangles2D(coords, constraints, WB_Epsilon.EPSILON);
	}

	/**
	 *
	 *
	 * @param points
	 * @param tol
	 * @param context
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final Collection<? extends WB_Coord> points,
			final double tol, final WB_Map2D context) {
		final int n = points.size();
		final int[] constraints = new int[2 * n];
		for (int i = 0, j = n - 1; i < n; j = i++) {
			constraints[2 * i] = j;
			constraints[2 * i + 1] = i;
		}
		final Coordinate[] coords = new Coordinate[n];
		final WB_Point point = new WB_Point();
		int i = 0;
		for (final WB_Coord p : points) {
			context.mapPoint3D(p, point);
			coords[i] = new Coordinate(point.xd(), point.yd(), i);
			i++;
		}
		return getConformingTriangles2D(coords, constraints, tol);
	}

	/**
	 *
	 *
	 * @param polygon
	 * @param tol
	 * @return
	 */
	static WB_Triangulation2DWithPoints triangulateConforming2D(final WB_Polygon polygon, final double tol) {
		final int n = polygon.getNumberOfPoints();
		final int[] constraints = new int[2 * n];
		int index = 0;
		for (int i = 0, j = polygon.getNumberOfShellPoints() - 1; i < polygon.getNumberOfShellPoints(); j = i++) {
			constraints[2 * index] = j;
			constraints[2 * index + 1] = i;
			index++;
		}
		final int nh = polygon.getNumberOfHoles();
		final int[] npc = polygon.getNumberOfPointsPerContour();
		int offset = 0;
		for (int i = 0; i < nh; i++) {
			offset += npc[i];
			for (int j = 0; j < npc[i + 1]; j++) {
				constraints[2 * index] = offset + j;
				constraints[2 * index + 1] = offset + (j + 1) % npc[i + 1];
				index++;
			}
		}
		final Coordinate[] coords = new Coordinate[n];
		final WB_Point p = new WB_Point();
		final WB_Map2D context = new WB_GeometryFactory3D().createEmbeddedPlane(polygon.getPlane());
		for (int i = 0; i < n; i++) {
			context.mapPoint3D(polygon.getPoint(i), p);
			coords[i] = new Coordinate(p.xd(), p.yd(), i);
		}
		final WB_Triangulation2DWithPoints tri = getConformingTriangles2D(coords, constraints, tol);
		final List<WB_Point> upoints = new FastList<>();
		final WB_CoordCollection points = tri.getPoints();
		for (int i = 0; i < points.size(); i++) {
			final WB_Point q = new WB_Point();
			context.unmapPoint2D(points.get(i), q);
			upoints.add(q);
		}
		return new WB_Triangulation2DWithPoints(tri.getTriangles(), tri.getEdges(), upoints);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D voronoi2D(final ArrayList<Coordinate> coords, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		for (int i = 0; i < npolys; i++) {
			final Polygon poly = (Polygon) polys.getGeometryN(i);
			final Coordinate[] polycoord = poly.getCoordinates();
			final List<WB_Coord> polypoints = new FastList<>();
			for (final Coordinate element : polycoord) {
				polypoints.add(toPoint(element.x, element.y, context));
			}
			final Point centroid = poly.getCentroid();
			final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
			final int index = (int) ((Coordinate) poly.getUserData()).getZ();
			final double area = poly.getArea();
			result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D voronoi2D(final ArrayList<Coordinate> coords, final double d, final int c,
			final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		Coordinate[] coordsArray = new Coordinate[coords.size()];
		coordsArray = coords.toArray(coordsArray);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			Geometry intersect = poly;
			intersect = intersect.buffer(-d, c);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 * @param coords
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		Coordinate[] coordsArray = new Coordinate[coords.size()];
		coordsArray = coords.toArray(coordsArray);
		final ConvexHull ch = new ConvexHull(coordsArray, JTSgf);
		final Geometry hull = ch.getConvexHull();
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			final Geometry intersect = poly.intersection(hull.getGeometryN(0));
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 * @param coords
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final double d, final int c,
			final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		Coordinate[] coordsArray = new Coordinate[coords.size()];
		coordsArray = coords.toArray(coordsArray);
		final ConvexHull ch = new ConvexHull(coordsArray, JTSgf);
		final Geometry hull = ch.getConvexHull();
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			Geometry intersect = poly.intersection(hull.getGeometryN(0));
			intersect = intersect.buffer(-d, c);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param bdcoords
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords,
			final ArrayList<Coordinate> bdcoords, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		Coordinate[] bdcoordsArray = new Coordinate[bdcoords.size()];
		bdcoordsArray = bdcoords.toArray(bdcoordsArray);
		final Polygon hull = JTSgf.createPolygon(bdcoordsArray);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			final Geometry intersect = poly.intersection(hull);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param constraint
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final WB_Polygon constraint,
			final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		final Polygon hull = toJTSPolygon2D(constraint);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			final Geometry intersect = poly.intersection(hull);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param constraint
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final List<WB_Polygon> constraint,
			final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		final Geometry hull = toJTSMultiPolygon2D(constraint);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			final Geometry intersect = poly.intersection(hull);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param bdcoords
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords,
			final ArrayList<Coordinate> bdcoords, final double d, final int c, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		Coordinate[] bdcoordsArray = new Coordinate[bdcoords.size()];
		bdcoordsArray = bdcoords.toArray(bdcoordsArray);
		final Polygon hull = JTSgf.createPolygon(bdcoordsArray);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			Geometry intersect = poly.intersection(hull);
			intersect = intersect.buffer(-d, c);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param coords
	 * @param constraint
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final WB_Polygon constraint,
			final double d, final int c, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		final Polygon hull = toJTSPolygon2D(constraint);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			Geometry intersect = poly.intersection(hull);
			intersect = intersect.buffer(-d, c);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	private static WB_Voronoi2D clippedVoronoi2D(final ArrayList<Coordinate> coords, final List<WB_Polygon> constraint,
			final double d, final int c, final WB_Map2D context) {
		final DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
		dtb.setSites(coords);
		final QuadEdgeSubdivision qes = dtb.getSubdivision();
		final GeometryCollection polys = (GeometryCollection) qes.getVoronoiDiagram(JTSgf);
		final int npolys = polys.getNumGeometries();
		final List<WB_VoronoiCell2D> result = new FastList<>();
		final Geometry hull = toJTSMultiPolygon2D(constraint);
		for (int i = 0; i < npolys; i++) {
			Polygon poly = (Polygon) polys.getGeometryN(i);
			Geometry intersect = poly.intersection(hull);
			intersect = intersect.buffer(-d, c);
			final double cellindex = ((Coordinate) poly.getUserData()).getZ();
			for (int j = 0; j < intersect.getNumGeometries(); j++) {
				if (intersect.getGeometryN(j).getGeometryType().equals("Polygon")
						&& !intersect.getGeometryN(j).isEmpty()) {
					poly = (Polygon) intersect.getGeometryN(j);
					final Coordinate[] polycoord = poly.getCoordinates();
					final List<WB_Point> polypoints = new FastList<>();
					for (final Coordinate element : polycoord) {
						polypoints.add(toPoint(element.x, element.y, context));
					}
					final Point centroid = poly.getCentroid();
					final WB_Point pc = centroid == null ? null : toPoint(centroid.getX(), centroid.getY(), context);
					final int index = (int) cellindex;
					final double area = poly.getArea();
					result.add(new WB_VoronoiCell2D(polypoints, index, createPoint2D(coords.get(index)), area, pc));
				}
			}
		}
		return new WB_Voronoi2D(result);
	}

	/**
	 *
	 *
	 * @param p
	 * @param i
	 * @param context
	 * @return
	 */
	private static Coordinate toCoordinate(final WB_Coord p, final int i, final WB_Map2D context) {
		final WB_Point tmp = new WB_Point();
		context.mapPoint3D(p, tmp);
		final Coordinate c = new Coordinate(tmp.xd(), tmp.yd(), i);
		return c;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param context
	 * @return
	 */
	private static WB_Point toPoint(final double x, final double y, final WB_Map2D context) {
		final WB_Point tmp = new WB_Point();
		context.unmapPoint3D(x, y, 0, tmp);
		return tmp;
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return voronoi2D(coords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return voronoi2D(coords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c, final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return voronoi2D(coords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c,
			final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return voronoi2D(coords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return voronoi2D(coords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final Collection<? extends WB_Coord> points) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return voronoi2D(coords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return voronoi2D(coords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Voronoi2D getVoronoi2D(final WB_Coord[] points) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return voronoi2D(coords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary,
			final WB_Map2D context) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, context));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary,
			final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final WB_Map2D context) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, context));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary,
			final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, context);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final WB_Map2D context) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, context));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, boundary, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final WB_Map2D context) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, context));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary,
			final double d, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, 2, context);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d,
			final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, d, 2, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c,
			final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c, final WB_Map2D context) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, context));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c, final WB_Map2D context) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, context));
		}
		return clippedVoronoi2D(coords, boundary, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c,
			final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c, final WB_Map2D context) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, context));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @param context
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary,
			final double d, final int c, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, c, context);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c, final WB_Map2D context) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, context));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, c, context);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d, final int c) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, XY));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary, final double d) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, XY));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final Collection<? extends WB_Coord> boundary) {
		int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		n = boundary.size();
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		id = 0;
		for (final WB_Coord p : boundary) {
			bdcoords.add(toCoordinate(p, id, XY));
			id++;
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d, final int c) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final double d) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d, final int c) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final double d) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d,
			final int c) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, XY));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary, final double d) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, XY));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Coord[] boundary) {
		int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		n = boundary.length;
		final ArrayList<Coordinate> bdcoords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			bdcoords.add(toCoordinate(boundary[i], i, XY));
		}
		if (!bdcoords.get(0).equals(bdcoords.get(n - 1))) {
			bdcoords.add(bdcoords.get(0));
		}
		return clippedVoronoi2D(coords, bdcoords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, d, 2, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary, final double d) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary,
			final double d) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, 2, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, 2, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final WB_Polygon boundary, final double d,
			final int c) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, d, c, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final WB_Coord[] points, final List<WB_Polygon> boundary, final double d,
			final int c) {
		final int n = points.length;
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			coords.add(toCoordinate(points[i], i, XY));
		}
		return clippedVoronoi2D(coords, boundary, d, c, XY);
	}

	/**
	 *
	 *
	 * @param points
	 * @param boundary
	 * @param d
	 * @param c
	 * @return
	 */
	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points, final WB_Polygon boundary,
			final double d, final int c) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, c, XY);
	}

	static WB_Voronoi2D getClippedVoronoi2D(final Collection<? extends WB_Coord> points,
			final List<WB_Polygon> boundary, final double d, final int c) {
		final int n = points.size();
		final ArrayList<Coordinate> coords = new ArrayList<>(n);
		int id = 0;
		for (final WB_Coord p : points) {
			coords.add(toCoordinate(p, id, XY));
			id++;
		}
		return clippedVoronoi2D(coords, boundary, d, c, XY);
	}
}
