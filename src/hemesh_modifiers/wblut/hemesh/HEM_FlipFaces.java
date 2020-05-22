package wblut.hemesh;

import java.util.Iterator;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;

/**
 *
 */
public class HEM_FlipFaces extends HEM_Modifier {
	/**
	 *
	 */
	public HEM_FlipFaces() {
		super();
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatusStr("HEM_FlipFacesMeshOp", "Flipping faces.");
		WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfEdges(), 10);
		tracker.setCounterStatusStr("HEM_FlipFacesMeshOp", "Reversing edges.", counter);
		HE_Halfedge he1;
		HE_Halfedge he2;
		HE_Vertex tmp;
		HE_Halfedge[] prevHe;
		HE_TextureCoordinate[] nextHeUVW;
		HE_Halfedge he;
		mesh.clearVisitedElements();
		prevHe = new HE_Halfedge[mesh.getNumberOfHalfedges()];
		nextHeUVW = new HE_TextureCoordinate[mesh.getNumberOfHalfedges()];
		int i = 0;
		HE_HalfedgeIterator heItr = mesh.heItr();
		counter = new WB_ProgressCounter(2 * mesh.getNumberOfHalfedges(), 10);
		tracker.setCounterStatusStr("HEM_FlipFacesMeshOp", "Reordering halfedges.", counter);
		while (heItr.hasNext()) {
			he = heItr.next();
			prevHe[i] = he.getPrevInFace();
			nextHeUVW[i] = he.getNextInFace().hasUVW() ? he.getNextInFace().getUVW() : null;
			i++;
			counter.increment();
		}
		i = 0;
		heItr = mesh.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			mesh.setNext(he, prevHe[i]);
			if (nextHeUVW[i] == null) {
				he.setUVW(0, 0, 0);
			} else {
				he.setUVW(nextHeUVW[i]);
			}
			i++;
			counter.increment();
		}
		counter = new WB_ProgressCounter(2 * mesh.getNumberOfEdges(), 10);
		tracker.setCounterStatusStr("HET_MeshOp", "Flipping edges.", counter);
		final HE_EdgeIterator eItr = mesh.eItr();
		while (eItr.hasNext()) {
			he1 = eItr.next();
			he2 = he1.getPair();
			tmp = he1.getVertex();
			mesh.setVertex(he1, he2.getVertex());
			mesh.setVertex(he2, tmp);
			mesh.setHalfedge(he1.getVertex(), he1);
			mesh.setHalfedge(he2.getVertex(), he2);
			counter.increment();
		}
		tracker.setStopStatusStr("HET_MeshOp", "Faces flipped.");
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
		tracker.setStartStatusStr("HEM_FlipFacesMeshOp", "Flipping faces.");
		WB_ProgressCounter counter = new WB_ProgressCounter(selection.getNumberOfEdges(), 10);
		tracker.setCounterStatusStr("HEM_FlipFacesMeshOp", "Reversing edges.", counter);
		HE_Halfedge he1;
		HE_Halfedge he2;
		HE_Vertex tmp;
		HE_Halfedge[] prevHe;
		HE_TextureCoordinate[] nextHeUVW;
		HE_Halfedge he;
		selection.getParent().clearVisitedElements();
		selection.collectHalfedges();
		prevHe = new HE_Halfedge[selection.getNumberOfHalfedges()];
		nextHeUVW = new HE_TextureCoordinate[selection.getNumberOfHalfedges()];
		int i = 0;
		Iterator<HE_Halfedge> heItr = selection.heItr();
		counter = new WB_ProgressCounter(2 * selection.getNumberOfHalfedges(), 10);
		tracker.setCounterStatusStr("HEM_FlipFacesMeshOp", "Reordering halfedges.", counter);
		while (heItr.hasNext()) {
			he = heItr.next();
			prevHe[i] = he.getPrevInFace();
			nextHeUVW[i] = he.getNextInFace().hasUVW() ? he.getNextInFace().getUVW() : null;
			i++;
			counter.increment();
		}
		i = 0;
		heItr = selection.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			selection.getParent().setNext(he, prevHe[i]);
			if (nextHeUVW[i] == null) {
				he.setUVW(0, 0, 0);
			} else {
				he.setUVW(nextHeUVW[i]);
			}
			i++;
			counter.increment();
		}
		counter = new WB_ProgressCounter(2 * selection.getNumberOfEdges(), 10);
		tracker.setCounterStatusStr("HET_MeshOp", "Flipping edges.", counter);
		final HE_EdgeIterator eItr = selection.eItr();
		while (eItr.hasNext()) {
			he1 = eItr.next();
			he2 = he1.getPair();
			tmp = he1.getVertex();
			selection.getParent().setVertex(he1, he2.getVertex());
			selection.getParent().setVertex(he2, tmp);
			selection.getParent().setHalfedge(he1.getVertex(), he1);
			selection.getParent().setHalfedge(he2.getVertex(), he2);
			counter.increment();
		}
		tracker.setStopStatusStr("HET_MeshOp", "Faces flipped.");
		return selection.getParent();
	}
}
