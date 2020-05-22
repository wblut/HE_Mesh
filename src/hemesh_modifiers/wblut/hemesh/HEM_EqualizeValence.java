package wblut.hemesh;

import java.util.Collections;

/**
 *
 */
public class HEM_EqualizeValence extends HEM_Modifier {
	/**  */
	private double threshold;

	/**
	 *
	 */
	public HEM_EqualizeValence() {
		super();
		threshold = -Math.PI;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_EqualizeValence setThreshold(final double a) {
		threshold = a;
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
		HE_MeshOp.triangulate(mesh);
		HE_Vertex a, b, c, d;
		int devpre = 0, devpost = 0;
		for (int r = 0; r < 2; r++) {
			final HE_HalfedgeList edges = new HE_HalfedgeList();
			edges.addAll(mesh.getEdges());
			Collections.shuffle(edges);
			for (final HE_Halfedge e : edges) {
				if (!e.isInnerBoundary() && HE_MeshOp.getEdgeDihedralAngle(e) > threshold) {
					a = e.getVertex();
					b = e.getEndVertex();
					c = e.getNextInFace().getEndVertex();
					d = e.getPair().getNextInFace().getEndVertex();
					devpre = Math.abs((a.isBoundary() ? 4 : 6) - a.getVertexDegree());
					devpre += Math.abs((b.isBoundary() ? 4 : 6) - b.getVertexDegree());
					devpre += Math.abs((c.isBoundary() ? 4 : 6) - c.getVertexDegree());
					devpre += Math.abs((d.isBoundary() ? 4 : 6) - d.getVertexDegree());
					if (devpre > 0) {
						final boolean trial = HE_MeshOp.flipEdge(mesh, e);
						if (trial) {
							devpost = Math.abs((a.isBoundary() ? 4 : 6) - a.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6) - b.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6) - c.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6) - d.getVertexDegree());
							if (devpre <= devpost) {
								HE_MeshOp.flipEdge(mesh, e);
							}
						}
					}
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
		HE_MeshOp.triangulate(selection);
		final HE_HalfedgeList edges = new HE_HalfedgeList();
		edges.addAll(selection.getInnerEdges());
		Collections.shuffle(edges);
		HE_Vertex a, b, c, d;
		int devpre, devpost;
		for (int r = 0; r < 2; r++) {
			for (final HE_Halfedge e : edges) {
				if (HE_MeshOp.getEdgeDihedralAngle(e) > threshold) {
					a = e.getVertex();
					b = e.getPair().getVertex();
					c = e.getNextInFace().getEndVertex();
					d = e.getPair().getNextInFace().getEndVertex();
					devpre = Math.abs((a.isBoundary() ? 4 : 6) - a.getVertexDegree());
					devpre += Math.abs((b.isBoundary() ? 4 : 6) - b.getVertexDegree());
					devpre += Math.abs((c.isBoundary() ? 4 : 6) - c.getVertexDegree());
					devpre += Math.abs((d.isBoundary() ? 4 : 6) - d.getVertexDegree());
					if (HE_MeshOp.flipEdge(selection.getParent(), e)) {
						devpost = Math.abs((a.isBoundary() ? 4 : 6) - a.getVertexDegree());
						devpost += Math.abs((b.isBoundary() ? 4 : 6) - b.getVertexDegree());
						devpost += Math.abs((b.isBoundary() ? 4 : 6) - c.getVertexDegree());
						devpost += Math.abs((b.isBoundary() ? 4 : 6) - d.getVertexDegree());
						if (devpre <= devpost) {
							HE_MeshOp.flipEdge(selection.getParent(), e);
						}
					}
				}
			}
		}
		return selection.getParent();
	}
}
