package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Line;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HEM_Stretch extends HEM_Modifier {
	/**  */
	private WB_Plane groundPlane;
	/**  */
	private WB_Vector stretchDirection;
	/**  */
	private double stretchFactor;
	/**  */
	private double compressionFactor;
	/**  */
	private boolean posOnly;

	/**
	 *
	 */
	public HEM_Stretch() {
		super();
	}

	/**
	 *
	 *
	 * @param P
	 * @return
	 */
	public HEM_Stretch setGroundPlane(final WB_Plane P) {
		groundPlane = P;
		return this;
	}

	/**
	 *
	 *
	 * @param ox
	 * @param oy
	 * @param oz
	 * @param nx
	 * @param ny
	 * @param nz
	 * @return
	 */
	public HEM_Stretch setGroundPlane(final double ox, final double oy, final double oz, final double nx,
			final double ny, final double nz) {
		groundPlane = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEM_Stretch setStretchFactor(final double f) {
		stretchFactor = f;
		compressionFactor = Math.sqrt(f);
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEM_Stretch setCompressionFactor(final double f) {
		if (f != 0) {
			compressionFactor = f;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Stretch setPosOnly(final boolean b) {
		posOnly = b;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (groundPlane != null && stretchDirection == null) {
			stretchDirection = new WB_Vector(groundPlane.getNormal());
		}
		if (groundPlane != null && stretchFactor != 0 && compressionFactor != 0) {
			final WB_Line L = new WB_Line(groundPlane.getOrigin(), stretchDirection);
			WB_Point p;
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			HE_Vertex v;
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > WB_Epsilon.EPSILON) {
					p = WB_GeometryOp.getClosestPoint3D(v, groundPlane);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(stretchFactor);
					v.getPosition().addSelf(p);
					p = WB_GeometryOp.getClosestPoint3D(v, L);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(1 / compressionFactor);
					v.getPosition().addSelf(p);
				}
			}
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		if (groundPlane != null && stretchDirection == null) {
			stretchDirection = new WB_Vector(groundPlane.getNormal());
		}
		if (groundPlane != null && (stretchFactor != 0 || compressionFactor != 0)) {
			final WB_Line L = new WB_Line(groundPlane.getOrigin(), stretchDirection);
			WB_Point p;
			final Iterator<HE_Vertex> vItr = selection.vItr();
			HE_Vertex v;
			while (vItr.hasNext()) {
				v = vItr.next();
				final double d = WB_GeometryOp.getDistance3D(v, groundPlane);
				if (!posOnly || d > WB_Epsilon.EPSILON) {
					p = WB_GeometryOp.getClosestPoint3D(v, groundPlane);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(stretchFactor);
					v.getPosition().addSelf(p);
					p = WB_GeometryOp.getClosestPoint3D(v, L);
					v.getPosition().subSelf(p);
					v.getPosition().mulSelf(1 / compressionFactor);
					v.getPosition().addSelf(p);
				}
			}
		}
		return selection.getParent();
	}
}
