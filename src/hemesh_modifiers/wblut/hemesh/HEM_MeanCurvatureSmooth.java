/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Iterator;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;

/**
 *
 */
public class HEM_MeanCurvatureSmooth extends HEM_Modifier {
	/**
	 *
	 */
	private boolean	autoRescale;
	/**
	 *
	 */
	private boolean	keepBoundary;
	private double	lambda;
	/**
	 *
	 */
	private int		iter;

	/**
	 *
	 */
	public HEM_MeanCurvatureSmooth() {
		lambda = 0.5;
		iter = 1;
		keepBoundary = false;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MeanCurvatureSmooth setAutoRescale(final boolean b) {
		autoRescale = b;
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEM_MeanCurvatureSmooth setIterations(final int r) {
		iter = r;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_MeanCurvatureSmooth setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	/**
	 *
	 *
	 * @param lambda
	 * @return
	 */
	public HEM_MeanCurvatureSmooth setLambda(final double lambda) {
		this.lambda = lambda;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_MeanCurvatureSmooth.");
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = HE_MeshOp.getAABB(mesh);
		}
		final WB_Coord[] newPositions = new WB_Coord[mesh
				.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		WB_ProgressCounter counter = new WB_ProgressCounter(
				iter * mesh.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Smoothing vertices.", counter);
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = mesh.vItr();
			HE_Vertex v;
			int id = 0;
			WB_Point p;
			while (vItr.hasNext()) {
				v = vItr.next();
				if (v.isBoundary() && keepBoundary) {
					newPositions[id] = v;
				} else {
					p = new WB_Point();
					double factor = 0;
					HE_Halfedge he = v.getHalfedge();
					do {
						double cotana = HE_MeshOp.getCotan(he);
						double cotanb = HE_MeshOp.getCotan(he.getPair());
						p.addMulSelf(cotana + cotanb,
								WB_Vector.sub(he.getEndVertex(), v));
						factor += cotana + cotanb;
						he = he.getNextInVertex();
					} while (he != v.getHalfedge());
					newPositions[id] = p.mulSelf(lambda / factor).addSelf(v);
				}
				id++;
			}
			vItr = mesh.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
				counter.increment();
			}
		}
		if (autoRescale) {
			mesh.fitInAABB(box);
		}
		tracker.setStopStatus(this, "Exiting HEM_MeanCurvatureSmooth.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_MeanCurvatureSmooth.");
		selection.collectVertices();
		WB_AABB box = new WB_AABB();
		if (autoRescale) {
			box = HE_MeshOp.getAABB(selection.getParent());
		}
		final WB_Coord[] newPositions = new WB_Coord[selection
				.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		WB_ProgressCounter counter = new WB_ProgressCounter(
				iter * selection.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Smoothing vertices.", counter);
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = selection.vItr();
			HE_Vertex v;
			int id = 0;
			WB_Point p;
			while (vItr.hasNext()) {
				v = vItr.next();
				if (v.isBoundary() && keepBoundary) {
					newPositions[id] = v;
				} else {
					p = new WB_Point();
					double factor = 0;
					HE_Halfedge he = v.getHalfedge();
					do {
						double cotana = HE_MeshOp.getCotan(he);
						double cotanb = HE_MeshOp.getCotan(he.getPair());
						p.addMulSelf(cotana + cotanb,
								WB_Vector.sub(he.getEndVertex(), v));
						factor += cotana + cotanb;
						he = he.getNextInVertex();
					} while (he != v.getHalfedge());
					newPositions[id] = p.mulSelf(lambda / factor).addSelf(v);
				}
				id++;
			}
			vItr = selection.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
				counter.increment();
			}
		}
		if (autoRescale) {
			selection.getParent().fitInAABB(box);
		}
		tracker.setStopStatus(this, "Exiting HEM_MeanCurvatureSmooth.");
		return selection.getParent();
	}
}
