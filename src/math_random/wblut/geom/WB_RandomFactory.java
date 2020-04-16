package wblut.geom;

import java.util.ArrayList;
import java.util.List;

import wblut.math.WB_MTRandom;

public abstract class WB_RandomFactory implements WB_PointFactory, WB_VectorFactory {
	final WB_MTRandom randomGen;
	private WB_Transform3D T;
	private final WB_Vector offset;

	WB_RandomFactory() {
		randomGen = new WB_MTRandom();
		T = new WB_Transform3D();
		offset = new WB_Vector();
	}

	WB_RandomFactory(final long seed) {
		randomGen = new WB_MTRandom(seed);
		T = new WB_Transform3D();
		offset = new WB_Vector();
	}

	final public WB_RandomFactory setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	abstract WB_Point nextPointImp();

	abstract WB_Vector nextVectorImp();

	@Override
	final public WB_Point nextPoint() {
		return nextPointImp().applyAsPointSelf(T).addSelf(offset);
	}

	@Override
	final public WB_Vector nextVector() {
		return nextVectorImp().applyAsVectorSelf(T).addSelf(offset);
	}

	@Override
	final public WB_PointCollection getPoints(final int N) {
		final List<WB_Point> points = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			points.add(nextPoint());
		}
		return WB_PointCollection.getCollection(points);
	}

	@Override
	final public WB_VectorCollection getVectors(final int N) {
		final List<WB_Vector> points = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			points.add(nextVector());
		}
		return WB_VectorCollection.getCollection(points);
	}

	@Override
	final public WB_PointCollection getPoints(final int N, final WB_Coord... coords) {
		final List<WB_Point> points = new ArrayList<>();
		for (final WB_Coord c : coords) {
			points.add(new WB_Point(c).applyAsPointSelf(T).addSelf(offset));
		}
		for (int i = 0; i < N; i++) {
			points.add(nextPoint());
		}
		return WB_PointCollection.getCollection(points);
	}

	@Override
	final public WB_VectorCollection getVectors(final int N, final WB_Coord... coords) {
		final List<WB_Vector> vectors = new ArrayList<>();
		for (final WB_Coord c : coords) {
			vectors.add(new WB_Vector(c).applyAsVectorSelf(T).addSelf(offset));
		}
		for (int i = 0; i < N; i++) {
			vectors.add(nextVector());
		}
		return WB_VectorCollection.getCollection(vectors);
	}

	final public void reset() {
		randomGen.reset();
	}

	final public WB_RandomFactory setOffset(final WB_Coord offset) {
		this.offset.set(offset);
		return this;
	}

	final public WB_RandomFactory setOffset(final double x, final double y) {
		this.offset.set(x, y, 0);
		return this;
	}

	final public WB_RandomFactory setOffset(final double x, final double y, final double z) {
		this.offset.set(x, y, z);
		return this;
	}

	final public WB_RandomFactory setTransform(final WB_Transform3D T) {
		this.T = T;
		return this;
	}

	final public WB_RandomFactory rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord axis) {
		this.T.addRotateAboutAxis(angle, p, axis);
		return this;
	}
}
