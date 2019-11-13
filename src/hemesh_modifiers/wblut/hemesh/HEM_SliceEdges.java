/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.collections.impl.map.mutable.UnifiedMap;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_AABBTree3D;
import wblut.geom.WB_Classification;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;
import wblut.math.WB_Epsilon;

/**
 * Planar cut of a mesh. No edges are created, no faces are removed.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_SliceEdges extends HEM_Modifier {
	static final int	ON	= 1, BACK = 2, FRONT = 3;
	/** Cut plane. */
	private WB_Plane	P;
	/** Stores cut faces. */
	public HE_Selection	cut;
	/** Stores new edges. */
	public HE_Selection	cutEdges;

	/**
	 * Instantiates a new HEM_SliceEdges.
	 */
	public HEM_SliceEdges() {
		super();
	}

	/**
	 * Set cut plane.
	 *
	 * @param P
	 *            cut plane
	 * @return self
	 */
	public HEM_SliceEdges setPlane(final WB_Plane P) {
		this.P = P;
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
	public HEM_SliceEdges setPlane(final double ox, final double oy,
			final double oz, final double nx, final double ny,
			final double nz) {
		P = new WB_Plane(ox, oy, oz, nx, ny, nz);
		return this;
	}

	/**
	 *
	 */
	private double offset;

	/**
	 * Set offset.
	 *
	 * @param d
	 *            offset
	 * @return self
	 */
	public HEM_SliceEdges setOffset(final double d) {
		offset = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_SliceEdges.");
		cut = HE_Selection.getSelection(mesh);
		cutEdges = HE_Selection.getSelection(mesh);
		mesh.resetEdgeInternalLabels();
		mesh.resetVertexInternalLabels();
		// no plane defined
		if (P == null) {
			tracker.setStopStatus(this,
					"No cutplane defined. Exiting HEM_SliceEdges.");
			return mesh;
		}
		// empty mesh
		if (mesh.getNumberOfVertices() == 0) {
			tracker.setStopStatus(this, "Empty mesh. Exiting HEM_SliceEdges.");
			return mesh;
		}
		// check if plane intersects mesh
		final WB_Plane lP = new WB_Plane(P.getNormal(), -P.d() + offset);
		if (!WB_GeometryOp.checkIntersection3D(HE_MeshOp.getAABB(mesh), lP)) {
			tracker.setStopStatus(this,
					"Plane doesn't intersect bounding box. Exiting HEM_SliceEdges.");
			return mesh;
		}
		tracker.setDuringStatus(this, "Creating bounding box tree.");
		final WB_AABBTree3D tree = new WB_AABBTree3D(mesh,
				Math.max(64, (int) Math.sqrt(mesh.getNumberOfFaces())));
		final HE_Selection faces = HE_Selection.getSelection(mesh);
		tracker.setDuringStatus(this, "Retrieving intersection candidates.");
		faces.addFaces(HE_MeshOp.getPotentialIntersectedFaces(tree, lP));
		faces.collectVertices();
		faces.collectEdgesByFace();
		WB_Classification tmp;
		final HashMap<Long, WB_Classification> vertexClass = new HashMap<Long, WB_Classification>();
		WB_ProgressCounter counter = new WB_ProgressCounter(
				faces.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Classifying vertices.", counter);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = faces.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			tmp = WB_GeometryOp.classifyPointToPlane3D(v, lP);
			if (tmp == WB_Classification.ON) {
				v.setInternalLabel(ON);
			} else if (tmp == WB_Classification.BACK) {
				v.setInternalLabel(BACK);
			} else if (tmp == WB_Classification.FRONT) {
				v.setInternalLabel(FRONT);
			}
			vertexClass.put(v.getKey(), tmp);
			counter.increment();
		}
		counter = new WB_ProgressCounter(faces.getNumberOfEdges(), 10);
		tracker.setCounterStatus(this, "Classifying edges.", counter);
		new ArrayList<HE_Vertex>();
		final HE_Selection split = HE_Selection.getSelection(mesh);
		final UnifiedMap<Long, Double> edgeInt = new UnifiedMap<Long, Double>();
		final Iterator<HE_Halfedge> eItr = faces.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (vertexClass
					.get(e.getStartVertex().getKey()) == WB_Classification.ON) {
				if (vertexClass.get(
						e.getEndVertex().getKey()) == WB_Classification.ON) {
					cutEdges.addEdge(e);
					e.setInternalLabel(1);
					e.getPair().setInternalLabel(1);
				} else {
					edgeInt.put(e.getKey(), 0.0);
				}
			} else if (vertexClass.get(
					e.getStartVertex().getKey()) == WB_Classification.BACK) {
				if (vertexClass.get(
						e.getEndVertex().getKey()) == WB_Classification.ON) {
					edgeInt.put(e.getKey(), 1.0);
				} else if (vertexClass.get(
						e.getEndVertex().getKey()) == WB_Classification.FRONT) {
					edgeInt.put(e.getKey(), HE_MeshOp.getIntersection(e, lP));
				}
			} else {
				if (vertexClass.get(
						e.getEndVertex().getKey()) == WB_Classification.ON) {
					edgeInt.put(e.getKey(), 1.0);
				} else if (vertexClass.get(
						e.getEndVertex().getKey()) == WB_Classification.BACK) {
					edgeInt.put(e.getKey(), HE_MeshOp.getIntersection(e, lP));
				}
			}
			counter.increment();
		}
		counter = new WB_ProgressCounter(edgeInt.size(), 10);
		tracker.setCounterStatus(this, "Indexing edge intersection.", counter);
		for (final Map.Entry<Long, Double> en : edgeInt.entrySet()) {
			final HE_Halfedge ce = mesh.getHalfedgeWithKey(en.getKey());
			final double u = en.getValue();
			if (ce.getFace() != null) {
				split.add(ce.getFace());
			}
			if (ce.getPair().getFace() != null) {
				split.add(ce.getPair().getFace());
			}
			if (u < WB_Epsilon.EPSILON) {
				split.add(ce.getStartVertex());
			} else if (u > 1.0 - WB_Epsilon.EPSILON) {
				split.add(ce.getEndVertex());
			} else {
				HE_Vertex vi = HE_MeshOp.splitEdge(mesh, ce, u).vItr().next();
				vi.setInternalLabel(ON);
				split.add(vi);
			}
			counter.increment();
		}
		tracker.setStopStatus(this, "Exiting HEM_SliceEdges.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_SliceEdges.");
		selection.getParent().resetEdgeInternalLabels();
		selection.getParent().resetVertexInternalLabels();
		cut = HE_Selection.getSelection(selection.getParent());
		cutEdges = HE_Selection.getSelection(selection.getParent());
		// no plane defined
		if (P == null) {
			tracker.setStopStatus(this,
					"No cutplane defined. Exiting HEM_SliceEdges.");
			return selection.getParent();
		}
		// empty mesh
		if (selection.getParent().getNumberOfVertices() == 0) {
			tracker.setStopStatus(this,
					"Empty vertex selection. Exiting HEM_SliceEdges.");
			return selection.getParent();
		}
		final WB_Plane lP = new WB_Plane(P.getNormal(), -P.d() + offset);
		tracker.setDuringStatus(this, "Creating bounding box tree.");
		final WB_AABBTree3D tree = new WB_AABBTree3D(selection.getParent(), 64);
		final HE_Selection faces = HE_Selection
				.getSelection(selection.getParent());
		tracker.setDuringStatus(this, "Retrieving intersection candidates.");
		faces.addFaces(HE_MeshOp.getPotentialIntersectedFaces(tree, lP));
		final HE_Selection lsel = selection.get();
		lsel.intersect(faces);
		lsel.collectEdgesByFace();
		lsel.collectVertices();
		// empty mesh
		if (lsel.getNumberOfVertices() == 0) {
			tracker.setStopStatus(this,
					"Plane doesn't intersect bounding box tree. Exiting HEM_SliceEdges.");
			return lsel.getParent();
		}
		// check if plane intersects mesh
		boolean positiveVertexExists = false;
		boolean negativeVertexExists = false;
		WB_Classification tmp;
		final UnifiedMap<Long, WB_Classification> vertexClass = new UnifiedMap<Long, WB_Classification>();
		HE_Vertex v;
		WB_ProgressCounter counter = new WB_ProgressCounter(
				lsel.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Classifying vertices.", counter);
		final Iterator<HE_Vertex> vItr = lsel.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			tmp = WB_GeometryOp.classifyPointToPlane3D(v, lP);
			vertexClass.put(v.getKey(), tmp);
			if (tmp == WB_Classification.FRONT) {
				positiveVertexExists = true;
			}
			if (tmp == WB_Classification.BACK) {
				negativeVertexExists = true;
			}
			counter.increment();
		}
		if (positiveVertexExists && negativeVertexExists) {
			new ArrayList<HE_Vertex>();
			final HE_Selection split = HE_Selection
					.getSelection(lsel.getParent());
			final HashMap<Long, Double> edgeInt = new HashMap<Long, Double>();
			final Iterator<HE_Halfedge> eItr = lsel.eItr();
			HE_Halfedge e;
			counter = new WB_ProgressCounter(lsel.getNumberOfEdges(), 10);
			tracker.setCounterStatus(this, "Classifying edges.", counter);
			while (eItr.hasNext()) {
				e = eItr.next();
				if (vertexClass.get(
						e.getStartVertex().getKey()) == WB_Classification.ON) {
					if (vertexClass.get(e.getEndVertex()
							.getKey()) == WB_Classification.ON) {
						cutEdges.addEdge(e);
						e.setInternalLabel(1);
					} else {
						edgeInt.put(e.getKey(), 0.0);
					}
				} else if (vertexClass.get(e.getStartVertex()
						.getKey()) == WB_Classification.BACK) {
					if (vertexClass.get(e.getEndVertex()
							.getKey()) == WB_Classification.ON) {
						edgeInt.put(e.getKey(), 1.0);
					} else if (vertexClass.get(e.getEndVertex()
							.getKey()) == WB_Classification.FRONT) {
						edgeInt.put(e.getKey(),
								HE_MeshOp.getIntersection(e, lP));
					}
				} else {
					if (vertexClass.get(e.getEndVertex()
							.getKey()) == WB_Classification.ON) {
						edgeInt.put(e.getKey(), 1.0);
					} else if (vertexClass.get(e.getEndVertex()
							.getKey()) == WB_Classification.BACK) {
						edgeInt.put(e.getKey(),
								HE_MeshOp.getIntersection(e, lP));
					}
				}
				counter.increment();
			}
			counter = new WB_ProgressCounter(edgeInt.size(), 10);
			tracker.setCounterStatus(this, "Indexing edge intersection.",
					counter);
			for (final Map.Entry<Long, Double> en : edgeInt.entrySet()) {
				final HE_Halfedge ce = lsel.getParent()
						.getHalfedgeWithKey(en.getKey());
				final double u = en.getValue();
				if (lsel.contains(ce.getFace())) {
					split.add(ce.getFace());
				}
				if (lsel.contains(ce.getPair().getFace())) {
					split.add(ce.getPair().getFace());
				}
				if (u < WB_Epsilon.EPSILON) {
					split.add(ce.getStartVertex());
				} else if (u > 1.0 - WB_Epsilon.EPSILON) {
					split.add(ce.getEndVertex());
				} else {
					HE_Vertex vi = HE_MeshOp.splitEdge(lsel.getParent(), ce, u)
							.vItr().next();
					vi.setInternalLabel(ON);
					split.add(vi);
				}
				counter.increment();
			}
		}
		tracker.setStopStatus(this, "Exiting HEM_SliceEdges.");
		return lsel.getParent();
	}
}
