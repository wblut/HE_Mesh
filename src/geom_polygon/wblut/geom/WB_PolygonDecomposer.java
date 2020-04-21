package wblut.geom;

import java.util.List;

public class WB_PolygonDecomposer {
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	public static List<WB_Polygon> decomposePolygon2D(WB_Polygon polygon) {
		if (!polygon.isSimple()) {
			polygon = gf.createSimplePolygon(polygon);
		}
		final List<WB_Polygon> polys = new WB_List<>();
		if (polygon == null) {
			return polys;
		}
		final int size = polygon.getNumberOfShellPoints();
		if (size < 4) {
			polys.add(polygon);
			return polys;
		}
		decomposePolygon(polygon.getPoints(), polys);
		return polys;
	}

	private static void decomposePolygon(final List<WB_Coord> pointlist, final List<WB_Polygon> accumulator) {
		decomposePolygon(WB_CoordCollection.getCollection(pointlist), accumulator);
	}

	private static void decomposePolygon(final WB_CoordCollection pointlist, final List<WB_Polygon> accumulator) {
		final int n = pointlist.size();
		final WB_Point upperIntersection = gf.createPoint();
		final WB_Point lowerIntersection = gf.createPoint();
		double upperDistance = Double.MAX_VALUE;
		double lowerDistance = Double.MAX_VALUE;
		double closestDistance = Double.MAX_VALUE;
		int upperIndex = 0;
		int lowerIndex = 0;
		int closestIndex = 0;
		final List<WB_Coord> lower = new WB_CoordList();
		final List<WB_Coord> upper = new WB_CoordList();
		for (int i = 0; i < n; i++) {
			final WB_Coord iVertex = pointlist.get(i);
			final WB_Coord iVertexPrev = pointlist.get(i == 0 ? n - 1 : i - 1);
			final WB_Coord iVertexNext = pointlist.get(i + 1 == n ? 0 : i + 1);
			if (WB_GeometryOp2D.isReflex2D(iVertexPrev, iVertex, iVertexNext)) {
				for (int j = 0; j < n; j++) {
					final WB_Coord jVertex = pointlist.get(j);
					final WB_Coord jVertexPrev = pointlist.get(j == 0 ? n - 1 : j - 1);
					final WB_Coord jVertexNext = pointlist.get(j + 1 == n ? 0 : j + 1);
					final WB_Point intersection = gf.createPoint();
					if (WB_GeometryOp2D.isLeftStrict2D(iVertexPrev, iVertex, jVertex)
							&& WB_GeometryOp2D.isRight2D(iVertexPrev, iVertex, jVertexPrev)) {
						if (WB_GeometryOp2D.getLineIntersectionInto2D(iVertexPrev, iVertex, jVertex, jVertexPrev,
								intersection)) {
							if (WB_GeometryOp2D.isRightStrict2D(iVertexNext, iVertex, intersection)) {
								final double dist = WB_Vector.getSqDistance2D(iVertex, intersection);
								if (dist < lowerDistance) {
									lowerDistance = dist;
									lowerIntersection.set(intersection);
									lowerIndex = j;
								}
							}
						}
					}
					if (WB_GeometryOp2D.isLeftStrict2D(iVertexNext, iVertex, jVertexNext)
							&& WB_GeometryOp2D.isRight2D(iVertexNext, iVertex, jVertex)) {
						if (WB_GeometryOp2D.getLineIntersectionInto2D(iVertexNext, iVertex, jVertex, jVertexNext,
								intersection)) {
							if (WB_GeometryOp2D.isLeftStrict2D(iVertexPrev, iVertex, intersection)) {
								final double dist = WB_Vector.getSqDistance2D(iVertex, intersection);
								if (dist < upperDistance) {
									upperDistance = dist;
									upperIntersection.set(intersection);
									upperIndex = j;
								}
							}
						}
					}
				}
				if (lowerIndex == (upperIndex + 1) % n) {
					final WB_Point midpoint = upperIntersection.add(lowerIntersection).mulSelf(0.5);
					if (i < upperIndex) {
						lower.addAll(pointlist.subList(i, upperIndex + 1));
						lower.add(midpoint);
						upper.add(midpoint);
						if (lowerIndex != 0) {
							upper.addAll(pointlist.subList(lowerIndex, n));
						}
						upper.addAll(pointlist.subList(0, i + 1));
					} else {
						if (i != 0) {
							lower.addAll(pointlist.subList(i, n));
						}
						lower.addAll(pointlist.subList(0, upperIndex + 1));
						lower.add(midpoint);
						upper.add(midpoint);
						upper.addAll(pointlist.subList(lowerIndex, i + 1));
					}
				} else {
					if (lowerIndex > upperIndex) {
						upperIndex += n;
					}
					closestIndex = lowerIndex;
					for (int j = lowerIndex; j <= upperIndex; j++) {
						final int jmod = j % n;
						final WB_Coord q = pointlist.get(jmod);
						if (q == iVertex || q == iVertexPrev || q == iVertexNext) {
							continue;
						}
						if (isVisible(pointlist, i, jmod)) {
							final double dist = WB_Vector.getSqDistance2D(iVertex, q);
							if (dist < closestDistance) {
								closestDistance = dist;
								closestIndex = jmod;
							}
						}
					}
					if (i < closestIndex) {
						lower.addAll(pointlist.subList(i, closestIndex + 1));
						if (closestIndex != 0) {
							upper.addAll(pointlist.subList(closestIndex, n));
						}
						upper.addAll(pointlist.subList(0, i + 1));
					} else {
						if (i != 0) {
							lower.addAll(pointlist.subList(i, n));
						}
						lower.addAll(pointlist.subList(0, closestIndex + 1));
						upper.addAll(pointlist.subList(closestIndex, i + 1));
					}
				}
				if (lower.size() < upper.size()) {
					decomposePolygon(lower, accumulator);
					decomposePolygon(upper, accumulator);
				} else {
					decomposePolygon(upper, accumulator);
					decomposePolygon(lower, accumulator);
				}
				return;
			}
		}
		if (pointlist.size() < 3) {
			return;
		}
		accumulator.add(gf.createSimplePolygon(pointlist));
	}

	private static boolean isVisible(final WB_CoordCollection pointlist, final int i, final int j) {
		final int n = pointlist.size();
		WB_Coord iVertex, jVertex;
		iVertex = pointlist.get(i);
		jVertex = pointlist.get(j);
		WB_Coord iVertexPrev, iVertexNext, jVertexPrev, jVertexNext;
		iVertexPrev = pointlist.get(i == 0 ? n - 1 : i - 1);
		iVertexNext = pointlist.get(i + 1 == n ? 0 : i + 1);
		jVertexPrev = pointlist.get(j == 0 ? n - 1 : j - 1);
		jVertexNext = pointlist.get(j + 1 == n ? 0 : j + 1);
		if (WB_GeometryOp2D.isReflex2D(iVertexPrev, iVertex, iVertexNext)) {
			if (WB_GeometryOp2D.isLeft2D(iVertex, iVertexPrev, jVertex)
					&& WB_GeometryOp2D.isRight2D(iVertex, iVertexNext, jVertex)) {
				return false;
			}
		} else {
			if (WB_GeometryOp2D.isRight2D(iVertex, iVertexNext, jVertex)
					|| WB_GeometryOp2D.isLeft2D(iVertex, iVertexPrev, jVertex)) {
				return false;
			}
		}
		if (WB_GeometryOp2D.isReflex2D(jVertexPrev, jVertex, jVertexNext)) {
			if (WB_GeometryOp2D.isLeft2D(jVertex, jVertexPrev, iVertex)
					&& WB_GeometryOp2D.isRight2D(jVertex, jVertexNext, iVertex)) {
				return false;
			}
		} else {
			if (WB_GeometryOp2D.isRight2D(jVertex, jVertexNext, iVertex)
					|| WB_GeometryOp2D.isLeft2D(jVertex, jVertexPrev, iVertex)) {
				return false;
			}
		}
		for (int k = 0; k < n; k++) {
			final int knext = k + 1 == n ? 0 : k + 1;
			if (k == i || k == j || knext == i || knext == j) {
				continue;
			}
			final WB_Coord kVertex = pointlist.get(k);
			final WB_Coord kVertexNext = pointlist.get(knext);
			final WB_Coord in = WB_GeometryOp2D.getSegmentIntersection2D(iVertex, jVertex, kVertex, kVertexNext);
			if (in != null) {
				return false;
			}
		}
		return true;
	}
}
