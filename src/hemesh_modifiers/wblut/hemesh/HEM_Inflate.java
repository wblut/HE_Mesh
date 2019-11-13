/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import wblut.geom.WB_AABB;
import wblut.geom.WB_KDTreeInteger3D;
import wblut.geom.WB_KDTreeInteger3D.WB_KDEntryInteger;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_Inflate extends HEM_Modifier {
	/**
	 *
	 */
	private boolean	autoRescale;
	/**
	 *
	 */
	private int		iter;
	/**
	 *
	 */
	private double	radius;
	/**
	 *
	 */
	private double	factor;

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	/**
	 *
	 */
	public HEM_Inflate() {
		radius = 10;
		factor = 0.1;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Inflate setAutoRescale(final boolean b) {
		autoRescale = b;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_Inflate setIterations(final int r) {
		iter = r;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_Inflate setRadius(final double r) {
		radius = r;
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEM_Inflate setFactor(final double f) {
		factor = f;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = HE_MeshOp.getAABB(mesh);
		}
		final WB_KDTreeInteger3D<HE_Vertex> tree = new WB_KDTreeInteger3D<HE_Vertex>();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		int id = 0;
		while (vItr.hasNext()) {
			tree.add(vItr.next(), id++);
		}
		final WB_Point[] newPositions = new WB_Point[mesh
				.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			vItr = mesh.vItr();
			WB_KDEntryInteger<HE_Vertex>[] neighbors;
			id = 0;
			WB_Vector dv;
			while (vItr.hasNext()) {
				v = vItr.next();
				dv = new WB_Vector(v);
				neighbors = tree.getRange(v, radius);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i].coord != v) {
						final WB_Vector tmp = WB_Vector
								.subToVector3D(neighbors[i].coord, v);
						tmp.normalizeSelf();
						dv.addSelf(tmp);
					}
				}
				dv.normalizeSelf();
				dv.mulSelf(factor);
				newPositions[id] = v.getPosition().add(dv);
				id++;
			}
			vItr = mesh.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
			}
		}
		if (autoRescale) {
			mesh.fitInAABBConstrained(box);
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.collectVertices();
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = HE_MeshOp.getAABB(selection.getParent());
		}
		final WB_KDTreeInteger3D<HE_Vertex> tree = new WB_KDTreeInteger3D<HE_Vertex>();
		Iterator<HE_Vertex> vItr = selection.getParent().vItr();
		HE_Vertex v;
		int id = 0;
		while (vItr.hasNext()) {
			tree.add(vItr.next(), id++);
		}
		final WB_Point[] newPositions = new WB_Point[selection
				.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			vItr = selection.vItr();
			WB_KDEntryInteger<HE_Vertex>[] neighbors;
			id = 0;
			while (vItr.hasNext()) {
				v = vItr.next();
				final WB_Vector dv = new WB_Vector(v);
				neighbors = tree.getRange(v, radius);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i].coord != v) {
						final WB_Vector tmp = WB_Vector
								.subToVector3D(neighbors[i].coord, v);
						tmp.normalizeSelf();
						dv.addSelf(tmp);
					}
				}
				dv.normalizeSelf();
				dv.mulSelf(factor);
				newPositions[id] = v.getPosition().add(dv);
				id++;
			}
			vItr = selection.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
			}
		}
		if (autoRescale) {
			selection.getParent().fitInAABBConstrained(box);
		}
		return selection.getParent();
	}
}
