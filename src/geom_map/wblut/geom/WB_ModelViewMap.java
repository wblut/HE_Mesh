package wblut.geom;

import processing.core.PApplet;
import processing.opengl.PGraphics3D;

public class WB_ModelViewMap implements WB_Map {
	private PApplet home;
	private float cx, cy, cz;

	public WB_ModelViewMap(final PApplet home) {
		if (home.g instanceof PGraphics3D) {
			this.home = home;
		} else {
			throw new IllegalArgumentException(
					"WB_ModelViewMap can only be used with P3D, OPENGL or derived ProcessingPGraphics object");
		}
	}

	@Override
	public void mapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public void mapPoint3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		cx = (float) x;
		cy = (float) y;
		cz = (float) z;
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public void unmapPoint3D(final WB_Coord p, final WB_MutableCoord result) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public void unmapPoint3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public void mapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public void mapVector3D(final double x, final double y, final double z, final WB_MutableCoord result) {
		cx = (float) x;
		cy = (float) y;
		cz = (float) z;
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public void unmapVector3D(final WB_Coord p, final WB_MutableCoord result) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public void unmapVector3D(final double u, final double v, final double w, final WB_MutableCoord result) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord mapPoint3D(final WB_Coord p) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		return new WB_Point(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord mapPoint3D(final double x, final double y, final double z) {
		cx = (float) x;
		cy = (float) y;
		cz = (float) z;
		return new WB_Point(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord unmapPoint3D(final WB_Coord p) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord unmapPoint3D(final double u, final double v, final double w) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord mapVector3D(final WB_Coord p) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		return new WB_Vector(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord mapVector3D(final double x, final double y, final double z) {
		return new WB_Vector(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz), home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord unmapVector3D(final WB_Coord p) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord unmapVector3D(final double u, final double v, final double w) {
		throw new UnsupportedOperationException("WB_ModelViewMap doesn't support unmapping.");
	}
}
