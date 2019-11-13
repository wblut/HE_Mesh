/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;

/**
 * Tries to expand a mesh by moving all faces a distance along their normal. No
 * vertices are added. The new position of a vertex is found by displacing all
 * the planes in its star and searching for their intersection. If the planes
 * intersect in a single point,this point is used. Otherwise a least-square
 * approximation of their intersection is used , i.e. the point that minimizes
 * the combined squared distance to the planes. If the least-square
 * approximation fails, the vertex is displaced along the vertex normal instead.
 * A cutoff factor can be specified to limit the movement of the vertices. If a
 * vertex would move more than cutoff*distance, it is displaced along the vertex
 * normal instead. If not specified, the cutoff factor defaults to 4.0,
 * corresponding to the displacement expected in an acute angle of 30°.
 *
 */
public class HEM_FacePlanarize extends HEM_Modifier {
	/**
	 * 
	 * 
	 * /**
	 *
	 */
	public HEM_FacePlanarize() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		HE_Vertex v;
		HE_Face f;
		for (int r = 0; r < 100; r++) {
			List<HE_Face> faces = mesh.getFaces();
			Collections.shuffle(faces);
			Iterator<HE_Face> fItr = faces.iterator();
			while (fItr.hasNext()) {
				f = fItr.next();
				WB_Plane P = HE_MeshOp.getPlane(f);
				HE_FaceVertexCirculator fvCrc = f.fvCrc();
				while (fvCrc.hasNext()) {
					v = fvCrc.next();
					v.getPosition().mulAddMulSelf(0.9, 0.1,
							WB_GeometryOp.projectOnPlane(v, P));
				}
			}
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		selection.collectVertices();
		HE_Mesh mesh = selection.getParent();
		WB_Plane[] planes = mesh.getFacePlanes();
		HE_FaceIterator fItr = mesh.fItr();
		int id = 0;
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(id++);
		}
		HE_Vertex v;
		Iterator<HE_Vertex> vItr = selection.vItr();
		List<HE_Face> faces;
		WB_Point target;
		WB_Point[] targets = new WB_Point[selection.getNumberOfVertices()];
		id = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			faces = v.getFaceStar();
			target = new WB_Point();
			for (HE_Face f : faces) {
				target.addSelf(WB_GeometryOp.projectOnPlane(v,
						planes[f.getInternalLabel()]));
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
