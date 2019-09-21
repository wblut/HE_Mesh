package wblut.hemesh;

import java.util.ArrayList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Danzer3D;
import wblut.geom.WB_Danzer3D.WB_DanzerTile3D;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Math;

public class HEMC_Danzer extends HEMC_MultiCreator {

	public HEMC_Danzer() {
		super();

		setCrop(false);
	}

	protected WB_Point getOrigin() {
		return (WB_Point) parameters.get("origin", new WB_Point());
	}

	protected WB_Vector getExtents() {
		return (WB_Vector) parameters.get("extents", new WB_Vector());
	}

	protected double getScale() {
		return parameters.get("scale", 0.0);
	}

	protected double getSpacing() {
		return parameters.get("spacing", 0.0);
	}

	protected int getLevel() {
		return parameters.get("level", 0);
	}

	protected WB_Danzer3D.Type getType() {
		return (WB_Danzer3D.Type) parameters.get("type", WB_Danzer3D.Type.A);
	}

	protected boolean[] getCrop() {
		return (boolean[]) parameters.get("crop", new boolean[] { false, false, false, false, false, false });
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEMC_Danzer setOrigin(final WB_Coord p) {
		parameters.set("origin", new WB_Point(p));
		return this;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public HEMC_Danzer setExtents(final WB_Vector v) {
		parameters.set("extents", new WB_Vector(v));
		return this;
	}

	public HEMC_Danzer setScale(final double sc) {
		parameters.set("scale", sc);
		return this;
	}

	public HEMC_Danzer setSpacing(final double sp) {
		parameters.set("spacing", sp);
		return this;
	}

	/**
	 *
	 *
	 * @param U
	 * @param V
	 * @param W
	 * @return
	 */
	public HEMC_Danzer setLevel(final int level) {
		parameters.set("level", WB_Math.max(0, level));
		return this;

	}

	/**
	 *
	 *
	 * @param crop
	 * @return
	 */
	public HEMC_Danzer setCrop(final boolean crop) {
		parameters.set("crop", new boolean[] { crop, crop, crop, crop, crop, crop });
		return this;
	}

	/**
	 *
	 *
	 * @param cropU
	 * @param cropV
	 * @param cropW
	 * @return
	 */
	public HEMC_Danzer setCrop(final boolean cropU, final boolean cropV, final boolean cropW) {
		parameters.set("crop", new boolean[] { cropU, cropV, cropW, cropU, cropV, cropW });
		return this;
	}

	/**
	 *
	 *
	 * @param cropUm
	 * @param cropVm
	 * @param cropWm
	 * @param cropUp
	 * @param cropVp
	 * @param cropWp
	 * @return
	 */
	public HEMC_Danzer setCrop(final boolean cropUm, final boolean cropVm, final boolean cropWm, final boolean cropUp,
			final boolean cropVp, final boolean cropWp) {
		parameters.set("crop", new boolean[] { cropUm, cropVm, cropWm, cropUp, cropVp, cropWp });
		return this;
	}

	public HEMC_Danzer setType(WB_Danzer3D.Type type) {
		parameters.set("type", type);
		return this;
	}

	@Override
	void create(final HE_MeshCollection result) {
		WB_Vector extents = getExtents();
		WB_Danzer3D danzer = new WB_Danzer3D(getType(), getScale(), getOrigin());
		WB_Point shift= getOrigin().sub(danzer.getTiles().get(0).getCenter());
		danzer.inflate(getLevel());

		HE_Mesh[] tmpCells = new HE_Mesh[danzer.getNumberOfTiles()];
		int id = 0;
		double spacing=getSpacing();
		for (WB_DanzerTile3D tile : danzer.getTiles()) {
			
			tmpCells[id] = new HE_Mesh(new HEC_Tetrahedron().setPoints(tile).setOverride(true));
			tmpCells[id].scaleSelf(1.0-spacing, tile.getCenter());
			tmpCells[id].moveSelf(shift);
			tmpCells[id].setInternalLabel(tile.getTypeAsInt());
			id++;
		}

		final ArrayList<WB_Plane> planes = new ArrayList<WB_Plane>(6);
		boolean[] crop = getCrop();
		WB_Point origin = getOrigin();
		final WB_Point start = origin.addMul(-0.5, extents);
		if (crop[0]) {
			planes.add(new WB_Plane(start, new WB_Vector(1, 0, 0)));
		}
		if (crop[1]) {
			planes.add(new WB_Plane(start, new WB_Vector(0, 1, 0)));
		}
		if (crop[2]) {
			planes.add(new WB_Plane(start, new WB_Vector(0, 0, 1)));
		}
		final WB_Point end = origin.addMul(0.5, extents);
		if (crop[3]) {
			planes.add(new WB_Plane(end, new WB_Vector(-1, 0, 0)));
		}
		if (crop[4]) {
			planes.add(new WB_Plane(end, new WB_Vector(0, -1, 0)));
		}
		if (crop[5]) {
			planes.add(new WB_Plane(end, new WB_Vector(0, 0, -1)));
		}
		final HEM_MultiSlice ms = new HEM_MultiSlice().setPlanes(planes);
		for (int i = 0; i < danzer.getNumberOfTiles(); i++) {
			if (planes.size() > 0) {
				tmpCells[i].modify(ms);
			}
			if (tmpCells[i].getNumberOfVertices() > 0) {
				result.add(tmpCells[i]);
			}
		}

		_numberOfMeshes = result.size();

	}

}
