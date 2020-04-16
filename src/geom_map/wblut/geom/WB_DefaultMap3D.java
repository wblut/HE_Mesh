package wblut.geom;

public class WB_DefaultMap3D implements WB_Map {
	public WB_DefaultMap3D() {
	}

	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p);
	}

	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		result.set(x, y, z);
	}

	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		result.set(p);
	}

	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		result.set(u, v, w);
	}

	@Override
	public void mapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v);
	}

	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		result.set(x, y, z);
	}

	@Override
	public void unmapVector3D(final WB_Coord v, final WB_MutableCoord result) {
		result.set(v);
	}

	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		result.set(u, v, w);
	}

	@Override
	public WB_Point mapPoint3D(final WB_Coord p) {
		final WB_Point result = new WB_Point();
		result.set(p);
		return result;
	}

	@Override
	public WB_Point mapPoint3D(final double x, final double y, final double z) {
		final WB_Point result = new WB_Point();
		result.set(x, y, z);
		return result;
	}

	@Override
	public WB_Point unmapPoint3D(final WB_Coord p) {
		final WB_Point result = new WB_Point();
		result.set(p);
		return result;
	}

	@Override
	public WB_Point unmapPoint3D(final double u, final double v, final double w) {
		final WB_Point result = new WB_Point();
		result.set(u, v, w);
		return result;
	}

	@Override
	public WB_Vector mapVector3D(final WB_Coord v) {
		final WB_Vector result = new WB_Vector();
		result.set(v);
		return result;
	}

	@Override
	public WB_Vector mapVector3D(final double x, final double y, final double z) {
		final WB_Vector result = new WB_Vector();
		result.set(x, y, z);
		return result;
	}

	@Override
	public WB_Vector unmapVector3D(final WB_Coord v) {
		final WB_Vector result = new WB_Vector();
		result.set(v);
		return result;
	}

	@Override
	public WB_Vector unmapVector3D(final double u, final double v, final double w) {
		final WB_Vector result = new WB_Vector();
		result.set(u, v, w);
		return result;
	}
}
