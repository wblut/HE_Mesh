package wblut.geom;

public class WB_TransformMap implements WB_Map {
	private final WB_Transform3D T;
	private final WB_Transform3D invT;

	public WB_TransformMap(final WB_Transform3D transform) {
		T = transform.get();
		invT = transform.get();
		invT.inverse();
	}

	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsPointInto(p, result);
	}

	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		T.applyAsPointInto(x, y, z, result);
	}

	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsPointInto(p, result);
	}

	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		invT.applyAsPointInto(u, v, w, result);
	}

	@Override
	public void mapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		T.applyAsVectorInto(p, result);
	}

	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		T.applyAsVectorInto(x, y, z, result);
	}

	@Override
	public void unmapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		invT.applyAsVectorInto(p, result);
	}

	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		invT.applyAsVectorInto(u, v, w, result);
	}

	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		return T.applyAsPoint(p);
	}

	@Override
	public WB_Point mapPoint3D(final double x, final double y, final double z) {
		return T.applyAsPoint(x, y, z);
	}

	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		return invT.applyAsPoint(p);
	}

	@Override
	public WB_Point unmapPoint3D(final double u, final double v, final double w) {
		return invT.applyAsPoint(u, v, w);
	}

	@Override
	public WB_Vector mapVector3D(final WB_Coord p) {
		return T.applyAsVector(p);
	}

	@Override
	public WB_Vector mapVector3D(final double x, final double y, final double z) {
		return T.applyAsVector(x, y, z);
	}

	@Override
	public WB_Vector unmapVector3D(final WB_Coord p) {
		return invT.applyAsVector(p);
	}

	@Override
	public WB_Vector unmapVector3D(final double u, final double v, final double w) {
		return invT.applyAsVector(u, v, w);
	}
}
