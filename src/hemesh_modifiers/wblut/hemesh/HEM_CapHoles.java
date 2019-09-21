/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.List;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;

/**
 *
 */
public class HEM_CapHoles extends HEM_Modifier {
	private HE_Selection caps;

	/**
	 *
	 */
	public HEM_CapHoles() {
		super();
		caps = null;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_CapHoles.");
		caps = HE_Selection.getSelection(mesh);
		final List<HE_Halfedge> unpairedEdges = mesh.getUnpairedHalfedges();
		HE_RAS<HE_Halfedge> loopedHalfedges;
		HE_Halfedge start;
		HE_Halfedge he;
		HE_Halfedge hen;
		HE_Face nf;
		HE_RAS<HE_Halfedge> newHalfedges;
		HE_Halfedge phe;
		HE_Halfedge nhe;
		WB_ProgressCounter counter = new WB_ProgressCounter(
				unpairedEdges.size(), 10);
		tracker.setCounterStatus(this, "Finding loops and closing holes.",
				counter);
		while (unpairedEdges.size() > 0) {
			boolean abort = false;
			loopedHalfedges = new HE_RAS<HE_Halfedge>();
			start = unpairedEdges.get(0);
			loopedHalfedges.add(start);
			he = start;
			hen = start;
			boolean noNextFound = false;
			do {
				for (int i = 0; i < unpairedEdges.size(); i++) {
					hen = unpairedEdges.get(i);
					if (hen.getVertex() == he.getNextInFace().getVertex()) {
						if (i > 0 && loopedHalfedges.contains(hen)) {
							abort = true;
							unpairedEdges.remove(start);
						}
						loopedHalfedges.add(hen);
						break;
					}
				}
				if (hen.getVertex() != he.getNextInFace().getVertex()) {
					noNextFound = true;
				}
				he = hen;
			} while (hen.getNextInFace().getVertex() != start.getVertex()
					&& !noNextFound && !abort);
			if (!abort) {
				nf = new HE_Face();
				boolean noLoopFound = start.getVertex() != loopedHalfedges
						.getWithIndex(loopedHalfedges.size() - 1)
						.getNextInFace().getVertex();
				int ii = 0;
				StringBuilder sb = new StringBuilder(100);
				if (noLoopFound) {
					sb.append("Polyline found: ");
					for (ii = 0; ii < loopedHalfedges.size() - 1; ii++) {
						sb.append(unpairedEdges.indexOf(
								loopedHalfedges.getWithIndex(ii)) + "-> ");
					}
					sb.append(unpairedEdges
							.indexOf(loopedHalfedges.getWithIndex(ii)));
				} else {
					sb.append("Cycle found: ");
					for (ii = 0; ii < loopedHalfedges.size(); ii++) {
						sb.append(unpairedEdges.indexOf(
								loopedHalfedges.getWithIndex(ii)) + "-> ");
					}
					sb.append(unpairedEdges
							.indexOf(loopedHalfedges.getWithIndex(0)));
				}
				tracker.setDuringStatus(this, sb.toString());
				unpairedEdges.removeAll(loopedHalfedges);
				if (!noLoopFound) {
					mesh.add(nf);
					caps.add(nf);
				}
				newHalfedges = new HE_RAS<HE_Halfedge>();
				for (int i = 0; i < loopedHalfedges.size(); i++) {
					phe = loopedHalfedges.getWithIndex(i);
					nhe = new HE_Halfedge();
					newHalfedges.add(nhe);
					mesh.setVertex(nhe, phe.getNextInFace().getVertex());
					mesh.setPair(nhe, phe);
					mesh.add(nhe);
					if (!noLoopFound) {
						mesh.setFace(nhe, nf);
						if (nf.getHalfedge() == null) {
							mesh.setHalfedge(nf, nhe);
						}
					}
				}
				HE_MeshOp.orderHalfedgesReverse(mesh, newHalfedges.getObjects(),
						!noLoopFound);
				counter.increment(newHalfedges.size());
			}
		}
		mesh.removeUnconnectedElements();
		HE_MeshOp.capHalfedges(mesh);
		mesh.addSelection("caps", this, caps);
		tracker.setStopStatus(this, "Exiting HEM_CapHoles.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
