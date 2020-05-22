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
 *
 */
public abstract class HEC_Creator extends HE_Machine {
	/**
	 *
	 */
	public HEC_Creator() {
		super();
		parameters.set("creationorigin", new WB_Point());
		parameters.set("creationaxis", new WB_Vector(WB_Vector.Z()));
		parameters.set("axis", new WB_Vector(WB_Vector.Z()));
		parameters.set("rotationangle", 0.0);
		parameters.set("scale", 1.0);
		parameters.set("modelview", false);
		parameters.set("manifoldcheck", false);
		parameters.set("override", false);
		parameters.set("mvoverride", false);
		setCheckNormals(false);
	}

	/**
	 *
	 *
	 * @return
	 */
	final public WB_Point getCenter() {
		return (WB_Point) parameters.get("center", new WB_Point());
	}

	/**
	 *
	 *
	 * @return
	 */
	final public WB_Point getCreationOrigin() {
		return (WB_Point) parameters.get("creationorigin", new WB_Point());
	}

	/**
	 *
	 *
	 * @return
	 */
	final public WB_Vector getCreationAxis() {
		return (WB_Vector) parameters.get("creationaxis", new WB_Vector(0, 0, 1));
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected WB_Vector getAxis() {
		return (WB_Vector) parameters.get("axis", new WB_Vector(0, 0, 1));
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected double getRotationAngle() {
		return parameters.get("rotationangle", 0.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected double getScale() {
		return parameters.get("scale", 1.0);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getModelView() {
		return parameters.get("modelview", false);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected PApplet getHome() {
		return (PApplet) parameters.get("home", null);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getCheckManifold() {
		return parameters.get("manifoldcheck", false);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getOverride() {
		return parameters.get("override", false);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getModelViewOverride() {
		return parameters.get("mvoverride", false);
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getCheckNormals() {
		return parameters.get("normalcheck", false);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Deprecated
	final protected boolean getCleanUnconnectedElements() {
		return getRemoveUnconnectedElements();
	}

	/**
	 *
	 *
	 * @return
	 */
	final protected boolean getRemoveUnconnectedElements() {
		return parameters.get("removeunconnected", false);
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public HEC_Creator setCenter(final double x, final double y, final double z) {
		parameters.set("center", new WB_Point(x, y, z));
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	final public HEC_Creator setCenter(final WB_Coord c) {
		parameters.set("center", c);
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final protected HEC_Creator setCreationOrigin(final double x, final double y, final double z) {
		parameters.set("creationorigin", new WB_Point(x, y, z));
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	final protected HEC_Creator setCreationOrigin(final WB_Coord c) {
		parameters.set("creationorigin", c);
		return this;
	}

	/**
	 *
	 *
	 * @param s
	 * @return
	 */
	final public HEC_Creator setScale(final double s) {
		parameters.set("scale", s);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	final public HEC_Creator setRotationAngle(final double a) {
		parameters.set("rotationangle", a);
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final protected HEC_Creator setCreationAxis(final double x, final double y, final double z) {
		final WB_Vector creationaxis = new WB_Vector(x, y, z);
		creationaxis.normalizeSelf();
		parameters.set("creationaxis", creationaxis);
		return this;
	}

	/**
	 *
	 *
	 * @param p0x
	 * @param p0y
	 * @param p0z
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @return
	 */
	final protected HEC_Creator setCreationAxis(final double p0x, final double p0y, final double p0z, final double p1x,
			final double p1y, final double p1z) {
		final WB_Vector creationaxis = new WB_Vector(p1x - p0x, p1y - p0y, p1z - p0z);
		creationaxis.normalizeSelf();
		parameters.set("creationaxis", creationaxis);
		return this;
	}

	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	final protected HEC_Creator setCreationAxis(final WB_Coord p) {
		final WB_Vector creationaxis = new WB_Vector(p);
		creationaxis.normalizeSelf();
		parameters.set("creationaxis", creationaxis);
		return this;
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @return
	 */
	final protected HEC_Creator setCreationAxis(final WB_Coord p0, final WB_Coord p1) {
		final WB_Vector creationaxis = WB_Vector.sub(p0, p1);
		creationaxis.normalizeSelf();
		parameters.set("creationaxis", creationaxis);
		return this;
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	final public HEC_Creator setAxis(final double x, final double y, final double z) {
		final WB_Vector axis = new WB_Vector(x, y, z);
		axis.normalizeSelf();
		parameters.set("axis", axis);
		return this;
	}

	/**
	 *
	 *
	 * @param p0x
	 * @param p0y
	 * @param p0z
	 * @param p1x
	 * @param p1y
	 * @param p1z
	 * @return
	 */
	final public HEC_Creator setAxis(final double p0x, final double p0y, final double p0z, final double p1x,
			final double p1y, final double p1z) {
		final WB_Vector axis = new WB_Vector(p1x - p0x, p1y - p0y, p1z - p0z);
		axis.normalizeSelf();
		parameters.set("axis", axis);
		return this;
	}

	/**
	 *
	 *
	 * @param axis
	 * @return
	 */
	final public HEC_Creator setAxis(final WB_Coord axis) {
		final WB_Vector laxis = new WB_Vector(axis);
		laxis.normalizeSelf();
		parameters.set("axis", laxis);
		return this;
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @return
	 */
	final public HEC_Creator setAxis(final WB_Coord p0, final WB_Coord p1) {
		final WB_Vector axis = WB_Vector.sub(p0, p1);
		axis.normalizeSelf();
		parameters.set("axis", axis);
		return this;
	}

	/**
	 *
	 *
	 * @param home
	 * @return
	 */
	final public HEC_Creator setToModelview(final PApplet home) {
		parameters.set("map", new WB_ModelViewMap(home));
		parameters.set("modelview", true);
		return this;
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	final public HEC_Creator setToModelview(final WB_Transform3D T) {
		parameters.set("map", new WB_TransformMap(T));
		parameters.set("modelview", true);
		return this;
	}

	/**
	 *
	 *
	 * @param CS
	 * @return
	 */
	final public HEC_Creator setToModelview(final WB_CoordinateSystem CS) {
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromCSToWorld(CS);
		parameters.set("map", new WB_TransformMap(T));
		parameters.set("modelview", true);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	final public HEC_Creator setToWorldview() {
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
	final public HEC_Creator setCheckManifold(final boolean b) {
		parameters.set("manifoldcheck", b);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	final public HEC_Creator setOverride(final boolean b) {
		parameters.set("override", b);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	final public HEC_Creator setModelViewOverride(final boolean b) {
		parameters.set("mvoverride", b);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	final public HEC_Creator setCheckNormals(final boolean b) {
		parameters.set("normalcheck", b);
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	final public HEC_Creator setRemoveUnconnectedElements(final boolean b) {
		parameters.set("removeunconnected", b);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	protected abstract HE_Mesh createBase();

	/**
	 *
	 *
	 * @return
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
			WB_Coord fcleft = new WB_Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
			while (fitr.hasNext()) {
				f = fitr.next();
				if (base.getFaceCenter(f).xd() < fcleft.xd()) {
					left = f;
					fcleft = base.getFaceCenter(left);
				}
			}
			final WB_Coord leftn = base.getFaceNormal(left);
			if (leftn.xd() > 0) {
				HE_MeshOp.flipFaces(base);
			}
		}
		tracker.setStartStatus(this, "Transforming base mesh.");
		final WB_Coord ctr = getCreationOrigin();
		if (!getOverride()) {
			final WB_Vector creationAxis = getCreationAxis();
			final WB_Vector axis = getAxis();
			base.scaleSelf(getScale());
			if (getRotationAngle() != 0) {
				base.rotateAboutAxis2PSelf(getRotationAngle(), ctr.xd(), ctr.yd(), ctr.zd(),
						ctr.xd() + creationAxis.xd(), ctr.yd() + creationAxis.yd(), ctr.zd() + +creationAxis.zd());
			}
			final WB_Vector tmp = creationAxis.cross(getAxis());
			if (!WB_Epsilon.isZeroSq(tmp.getSqLength3D())) {
				base.rotateAboutAxis2PSelf(Math.acos(WB_Math.clamp(creationAxis.dot(axis), -1, 1)), ctr.xd(), ctr.yd(),
						ctr.zd(), ctr.xd() + tmp.xd(), ctr.yd() + tmp.yd(), ctr.zd() + tmp.zd());
			} else if (getCreationAxis().dot(axis) < -1 + WB_Epsilon.EPSILON) {
				base.scaleSelf(1, 1, -1);
			}
			base.moveSelf(getCenter().sub(ctr));
		}
		tracker.setStopStatus(this, "Base mesh transformed.");
		HE_Vertex v;
		if (getModelView() && !getModelViewOverride()) {
			tracker.setStartStatus(this, "Transforming mesh from model view to world view.");
			final WB_Map map = (WB_Map) parameters.get("map", new WB_DefaultMap3D());
			final Iterator<HE_Vertex> vItr = base.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				map.mapPoint3D(v, v);
			}
			tracker.setStopStatus(this, "Mesh transformed to world view.");
		}
		return base;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		mesh.setNoCopy(create());
		return mesh;
	}

	/**
	 *
	 *
	 * @param sel
	 * @return
	 */
	@Override
	public HE_Mesh apply(final HE_Selection sel) {
		throw new UnsupportedOperationException(
				"The operation apply(HE_Selection) is not available for a HEC_Creator.");
	}
}
