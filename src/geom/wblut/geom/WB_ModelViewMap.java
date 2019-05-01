package wblut.geom;
import processing.core.PApplet;
import processing.opengl.PGraphics3D;
public class WB_ModelViewMap implements WB_Map {
	
	private PApplet home;
	private  float cx, cy, cz;
	public WB_ModelViewMap(PApplet home) {
		if (home.g instanceof PGraphics3D) {
			this.home=home;
		}else {
			throw new IllegalArgumentException(
					"WB_ModelViewMap can only be used with P3D, OPENGL or derived ProcessingPGraphics object");
		}
	}
	
	
	@Override
	public void mapPoint3D(WB_Coord p, WB_MutableCoord result) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));

	}

	@Override
	public void mapPoint3D(double x, double y, double z, WB_MutableCoord result) {
		cx = (float)x;
		cy = (float)y;
		cz = (float)z;
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));

	}

	@Override
	public void unmapPoint3D(WB_Coord p, WB_MutableCoord result) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");

	}

	@Override
	public void unmapPoint3D(double u, double v, double w, WB_MutableCoord result) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");

	}

	@Override
	public void mapVector3D(WB_Coord p, WB_MutableCoord result) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));

	}

	@Override
	public void mapVector3D(double x, double y, double z, WB_MutableCoord result) {
		cx = (float)x;
		cy = (float)y;
		cz = (float)z;
		result.set(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));

	}

	@Override
	public void unmapVector3D(WB_Coord p, WB_MutableCoord result) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");

	}

	@Override
	public void unmapVector3D(double u, double v, double w, WB_MutableCoord result) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");

	}

	@Override
	public WB_Coord mapPoint3D(WB_Coord p) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		return new WB_Point(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord mapPoint3D(double x, double y, double z) {
		cx = (float)x;
		cy = (float)y;
		cz = (float)z;
		return new WB_Point(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord unmapPoint3D(WB_Coord p) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord unmapPoint3D(double u, double v, double w) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord mapVector3D(WB_Coord p) {
		cx = p.xf();
		cy = p.yf();
		cz = p.zf();
		return new WB_Vector(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord mapVector3D(double x, double y, double z) {
		return new WB_Vector(home.modelX(cx, cy, cz), home.modelY(cx, cy, cz),
				home.modelZ(cx, cy, cz));
	}

	@Override
	public WB_Coord unmapVector3D(WB_Coord p) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");
	}

	@Override
	public WB_Coord unmapVector3D(double u, double v, double w) {
		throw new UnsupportedOperationException(
				"WB_ModelViewMap doesn't support unmapping.");
	}

}
