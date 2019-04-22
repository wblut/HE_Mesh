/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Collections;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * Bend a mesh. Determined by a ground plane, a bend axis and an angle factor.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_EqualizeValence extends HEM_Modifier {
	/** threshold. */
	private double threshold;

	/**
	 * Instantiates a new HEM_Bend.
	 */
	public HEM_EqualizeValence() {
		super();
		threshold = -Math.PI;
	}

	/**
	 * Set threshold angle.
	 *
	 * @param a
	 *            threshold angle in radians
	 * @return self
	 */
	public HEM_EqualizeValence setThreshold(final double a) {
		threshold = a;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_MeshOp.triangulate(mesh);
		HE_Vertex a, b, c, d;
		int devpre = 0, devpost = 0;
		for (int r = 0; r < 2; r++) {
			List<HE_Halfedge> edges = new FastList<HE_Halfedge>();
			edges.addAll(mesh.getEdges());
			Collections.shuffle(edges);
			for (HE_Halfedge e : edges) {
				if (!e.isInnerBoundary()
						&& HE_MeshOp.getEdgeDihedralAngle(e) > threshold) {
					a = e.getVertex();
					b = e.getEndVertex();
					c = e.getNextInFace().getEndVertex();
					d = e.getPair().getNextInFace().getEndVertex();
					devpre = Math.abs(
							(a.isBoundary() ? 4 : 6) - a.getVertexDegree());
					devpre += Math.abs(
							(b.isBoundary() ? 4 : 6) - b.getVertexDegree());
					devpre += Math.abs(
							(c.isBoundary() ? 4 : 6) - c.getVertexDegree());
					devpre += Math.abs(
							(d.isBoundary() ? 4 : 6) - d.getVertexDegree());
					if (devpre > 0) {
						boolean trial = HE_MeshOp.flipEdge(mesh, e);
						if (trial) {
							devpost = Math.abs((a.isBoundary() ? 4 : 6)
									- a.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6)
									- b.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6)
									- c.getVertexDegree());
							devpost += Math.abs((b.isBoundary() ? 4 : 6)
									- d.getVertexDegree());
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

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		HE_MeshOp.triangulate(selection);
		List<HE_Halfedge> edges = new FastList<HE_Halfedge>();
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
					devpre = Math.abs(
							(a.isBoundary() ? 4 : 6) - a.getVertexDegree());
					devpre += Math.abs(
							(b.isBoundary() ? 4 : 6) - b.getVertexDegree());
					devpre += Math.abs(
							(c.isBoundary() ? 4 : 6) - c.getVertexDegree());
					devpre += Math.abs(
							(d.isBoundary() ? 4 : 6) - d.getVertexDegree());
					if (HE_MeshOp.flipEdge(selection.getParent(), e)) {
						devpost = Math.abs(
								(a.isBoundary() ? 4 : 6) - a.getVertexDegree());
						devpost += Math.abs(
								(b.isBoundary() ? 4 : 6) - b.getVertexDegree());
						devpost += Math.abs(
								(b.isBoundary() ? 4 : 6) - c.getVertexDegree());
						devpost += Math.abs(
								(b.isBoundary() ? 4 : 6) - d.getVertexDegree());
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
