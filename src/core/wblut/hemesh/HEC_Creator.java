/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;


import processing.core.PApplet;

import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordinateSystem;
import wblut.geom.WB_DefaultMap3D;
import wblut.geom.WB_Map;
import wblut.geom.WB_ModelViewMap;
import wblut.geom.WB_Point;
import wblut.geom.WB_Transform3D;
import wblut.geom.WB_TransformMap;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_Math;

/**
 * Abstract base class for mesh creation. Implementation should return a valid
 * HE_Mesh.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public abstract class HEC_Creator extends HE_Machine {

	public HEC_Creator() {
		super();
		parameters.set("center", new WB_Point());
		parameters.set("zaxis", new WB_Vector(WB_Vector.Z()));
		parameters.set("verticalaxis", new WB_Vector(WB_Vector.Z()));
		parameters.set("rotationangle", 0.0);
		parameters.set("scale", 1.0);
		parameters.set("modelview", false);
		parameters.set("manifoldcheck", false);
		parameters.set("override", false);
		parameters.set("mvoverride", false);
		setCheckNormals(false);
	}

	public WB_Point getCenter() {
		return (WB_Point) parameters.get("center", new WB_Point());
	}

	public WB_Vector getZAxis() {
		return (WB_Vector) parameters.get("zaxis", new WB_Vector());
	}

	protected WB_Vector getVerticalAxis() {
		return (WB_Vector) parameters.get("verticalaxis", new WB_Vector());
	}

	protected double getRotationAngle() {
		return parameters.get("rotationangle", 0.0);
	}

	protected double getScale() {
		return parameters.get("scale", 0.0);
	}

	protected boolean getModelView() {
		return parameters.get("modelview", false);
	}

	protected PApplet getHome() {
		return (PApplet) parameters.get("home", null);
	}

	protected boolean getCheckManifold() {
		return parameters.get("manifoldcheck", false);
	}

	protected boolean getOverride() {
		return parameters.get("override", false);
	}
	
	protected boolean getModelViewOverride() {
		return parameters.get("mvoverride", false);
	}
	
	protected boolean getCheckNormals() {
		return parameters.get("normalcheck", false);
	}
	
	/**
	 * @deprecated Use {@link #getRemoveUnconnectedElements()} instead
	 */
	protected boolean getCleanUnconnectedElements() {
		return getRemoveUnconnectedElements();
	}

	protected boolean getRemoveUnconnectedElements() {
		
		return parameters.get("removeunconnected", false);
	}

	/**
	 * Set center of mesh. After creation the mesh is moved so that its center of
	 * gravity equals center.
	 *
	 * @param x x-coordinate of center
	 * @param y y-coordinate of center
	 * @param z z-coordinate of center
	 * @return self
	 */
	public HEC_Creator setCenter(final double x, final double y, final double z) {
		parameters.set("center", new WB_Point(x, y, z));
		return this;
	}

	/**
	 * Set center of mesh. After creation the mesh is moved so that its center of
	 * gravity equals center.
	 *
	 * @param c center
	 * @return self
	 */
	public HEC_Creator setCenter(final WB_Coord c) {
		parameters.set("center", c);
		return this;
	}

	/**
	 *
	 *
	 * @param s
	 * @return
	 */
	public HEC_Creator setScale(final double s) {
		parameters.set("scale", (double)s);
		return this;
	}

	/**
	 * Rotation of mesh about local Z-axis.
	 *
	 * @param a angle
	 * @return self
	 */
	public HEC_Creator setZAngle(final double a) {
		parameters.set("rotationangle", (double)a);
		return this;
	}

	/**
	 * Orientation of local Z-axis of mesh.
	 *
	 * @param x x-coordinate of axis vector
	 * @param y y-coordinate of axis vector
	 * @param z z-coordinate of axis vector
	 * @return self
	 */
	public HEC_Creator setZAxis(final double x, final double y, final double z) {
		WB_Vector zaxis = new WB_Vector(x, y, z);
		zaxis.normalizeSelf();
		parameters.set("zaxis", zaxis);
		return this;
	}

	/**
	 * Local Z-axis of mesh.
	 *
	 * @param p0x x-coordinate of first point on axis
	 * @param p0y y-coordinate of first point on axis
	 * @param p0z z-coordinate of first point on axis
	 * @param p1x x-coordinate of second point on axis
	 * @param p1y y-coordinate of second point on axis
	 * @param p1z z-coordinate of second point on axis
	 * @return self
	 */
	public HEC_Creator setZAxis(final double p0x, final double p0y, final double p0z, final double p1x,
			final double p1y, final double p1z) {
		WB_Vector zaxis = new WB_Vector(p1x - p0x, p1y - p0y, p1z - p0z);
		zaxis.normalizeSelf();
		parameters.set("zaxis", zaxis);
		return this;
	}

	/**
	 * Orientation of local Z-axis of mesh.
	 *
	 * @param p axis vector
	 * @return self
	 */
	public HEC_Creator setZAxis(final WB_Coord p) {
		WB_Vector zaxis = new WB_Vector(p);
		zaxis.normalizeSelf();
		parameters.set("zaxis", zaxis);
		return this;
	}

	/**
	 * Local Z-axis of mesh.
	 *
	 * @param p0 first point on axis
	 * @param p1 second point on axis
	 * @return self
	 */
	public HEC_Creator setZAxis(final WB_Coord p0, final WB_Coord p1) {
		WB_Vector zaxis = WB_Vector.sub(p0, p1);
		zaxis.normalizeSelf();
		parameters.set("zaxis", zaxis);
		return this;
	}

	protected HEC_Creator setVerticalAxis(final double x, final double y, final double z) {
		WB_Vector verticalaxis = new WB_Vector(x, y, z);
		verticalaxis.normalizeSelf();
		parameters.set("verticalaxis", verticalaxis);
		return this;
	}

	protected HEC_Creator setVerticalAxis(final double p0x, final double p0y, final double p0z, final double p1x,
			final double p1y, final double p1z) {
		WB_Vector verticalaxis = new WB_Vector(p1x - p0x, p1y - p0y, p1z - p0z);
		verticalaxis.normalizeSelf();
		parameters.set("verticalaxis", verticalaxis);
		return this;
	}

	protected HEC_Creator setVerticalAxis(WB_Coord axis) {
		WB_Vector verticalaxis = new WB_Vector(axis);
		verticalaxis.normalizeSelf();
		parameters.set("verticalaxis", verticalaxis);
		return this;
	}

	protected HEC_Creator setVerticalAxis(final WB_Coord p0, final WB_Coord p1) {
		WB_Vector verticalaxis = WB_Vector.sub(p0, p1);
		verticalaxis.normalizeSelf();
		parameters.set("verticalaxis", verticalaxis);
		return this;
	}

	/**
	 * Use the applet's modelview coordinates.
	 *
	 * @param home calling applet, typically "this"
	 * @return self
	 */
	public HEC_Creator setToModelview(final PApplet home) {
		parameters.set("map", new WB_ModelViewMap(home));
		parameters.set("modelview", true);
		return this;
	}

	public HEC_Creator setToModelview(final WB_Transform3D T) {
		parameters.set("map", new WB_TransformMap(T));
		parameters.set("modelview", true);
		return this;
	}

	public HEC_Creator setToModelview(final WB_CoordinateSystem CS) {
		WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(CS);
		parameters.set("map", new WB_TransformMap(T));
		parameters.set("modelview", true);
		return this;
	}

	/**
	 * Use absolute coordinates.
	 *
	 * @return self
	 */
	public HEC_Creator setToWorldview() {
		parameters.remove("map");
		parameters.set("modelview", false);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_Creator setCheckManifold(final boolean b) {
		parameters.set("manifoldcheck", b);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_Creator setOverride(final boolean b) {
		parameters.set("override", b);
		return this;
	}
	
	public HEC_Creator setModelViewOverride(final boolean b) {
		parameters.set("mvoverride", b);
		return this;
	}
	/**
	 * Check face normals?.
	 *
	 * @param b
	 *            true/false
	 * @return self
	 */
	public HEC_Creator setCheckNormals(final boolean b) {
		parameters.set("normalcheck" ,b);
		return this;
	}
	
	
	public HEC_Creator setRemoveUnconnectedElements(final boolean b) {
		parameters.set("removeunconnected",b);
		return this;
	}


	/**
	 * Creates the base.
	 *
	 * @return HE_Mesh
	 */
	protected abstract HE_Mesh createBase();

	/**
	 * Generate a mesh, move to center and orient along axis.
	 *
	 * @return HE_Mesh
	 */
	public final HE_Mesh create() {
		tracker.setStartStatus(this, "Creating base mesh.");
		final HE_Mesh base = createBase();
		tracker.setStopStatus(this, "Base mesh created.");
		
		
		
		if (getRemoveUnconnectedElements()) {
			
			base.removeUnconnectedElements();
			HE_MeshOp.capHalfedges(base);
		}
		
		if (getCheckManifold()) {
			tracker.setStartStatus(this, "Checking and fixing manifold.");
			HET_Fixer.fixNonManifoldVertices(base);
			tracker.setStopStatus(this, "Manifold checked.");
		}
		
		if (getCheckNormals()) {
			final HE_FaceIterator fitr = base.fItr();
			HE_Face f;
			HE_Face left = null;
			WB_Coord fcleft = new WB_Point(Double.MAX_VALUE,
					Double.MAX_VALUE, Double.MAX_VALUE);
			while (fitr.hasNext()) {
				f = fitr.next();
				if (HE_MeshOp.getFaceCenter(f).xd() < fcleft.xd()) {
					left = f;
					fcleft = HE_MeshOp.getFaceCenter(left);
				}
			}
			final WB_Coord leftn = HE_MeshOp.getFaceNormal(left);
			if (leftn.xd() > 0) {
				HE_MeshOp.flipFaces(base);
			}
		}
		
		tracker.setStartStatus(this, "Transforming base mesh.");
		WB_Coord ctr = HE_MeshOp.getCenter(base);
		if (!getOverride()) {
			base.scaleSelf(getScale());
			if (getRotationAngle() != 0) {
				base.rotateAboutAxis2PSelf(getRotationAngle(), ctr.xd(), ctr.yd(), ctr.zd(), ctr.xd(), ctr.yd(),
						ctr.zd() + 1);
			}
			final WB_Vector tmp = getZAxis().cross(getVerticalAxis());
			if (!WB_Epsilon.isZeroSq(tmp.getSqLength())) {
				base.rotateAboutAxis2PSelf(-Math.acos(WB_Math.clamp(getZAxis().dot(getVerticalAxis()), -1, 1)),
						ctr.xd(), ctr.yd(), ctr.zd(), ctr.xd() + tmp.xd(), ctr.yd() + tmp.yd(), ctr.zd() + tmp.zd());
			} else if (getZAxis().dot(getVerticalAxis()) < -1 + WB_Epsilon.EPSILON) {
				base.scaleSelf(1, 1, -1);
			}
			base.moveToSelf(getCenter());
		}
		tracker.setStopStatus(this, "Base mesh transformed.");
		HE_Vertex v;
		if (getModelView() && !getModelViewOverride()) {
			tracker.setStartStatus(this, "Transforming mesh from model view to world view.");
			WB_Map map = (WB_Map) parameters.get("map", new WB_DefaultMap3D());
			final Iterator<HE_Vertex> vItr = base.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				map.mapPoint3D(v,v);
			}
			tracker.setStopStatus(this, "Mesh transformed to world view.");
		}
		

		return base;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		mesh.setNoCopy(create());
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HE_Machine#apply(wblut.hemesh.HE_Selection)
	 */
	@Override
	public HE_Mesh apply(final HE_Selection sel) {
		throw new UnsupportedOperationException("The operation apply(HE_Selection) is not available for a HEC_Creator.");
	}
}
