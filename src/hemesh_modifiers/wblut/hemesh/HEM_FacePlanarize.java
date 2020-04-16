package wblut.hemesh;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;

public class HEM_FacePlanarize extends HEM_Modifier {
	public HEM_FacePlanarize() {
		super();
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_Vertex v;
		HE_Face f;
		for (int r = 0; r < 100; r++) {
			final List<HE_Face> faces = mesh.getFaces();
			Collections.shuffle(faces);
			final Iterator<HE_Face> fItr = faces.iterator();
			while (fItr.hasNext()) {
				f = fItr.next();
				final WB_Plane P = HE_MeshOp.getPlane(f);
				final HE_FaceVertexCirculator fvCrc = f.fvCrc();
				while (fvCrc.hasNext()) {
					v = fvCrc.next();
					v.getPosition().mulAddMulSelf(0.9, 0.1, WB_GeometryOp3D.projectOnPlane(v, P));
				}
			}
		}
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.collectVertices();
		final HE_Mesh mesh = selection.getParent();
		final WB_Plane[] planes = mesh.getFacePlanes();
		final HE_FaceIterator fItr = mesh.fItr();
		int id = 0;
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(id++);
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = selection.vItr();
		List<HE_Face> faces;
		WB_Point target;
		final WB_Point[] targets = new WB_Point[selection.getNumberOfVertices()];
		id = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			faces = v.getFaceStar();
			target = new WB_Point();
			for (final HE_Face f : faces) {
				target.addSelf(WB_GeometryOp3D.projectOnPlane(v, planes[f.getInternalLabel()]));
			}
			target.divSelf(faces.size());
			targets[id++] = target;
		}
		vItr = selection.vItr();
		id = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			v.getPosition().mulAddMulSelf(0.5, 0.5, targets[id++]);
		}
		return mesh;
	}
}
