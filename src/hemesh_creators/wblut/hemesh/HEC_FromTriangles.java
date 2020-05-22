package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_List;
import wblut.geom.WB_Point;
import wblut.geom.WB_Triangle;

/**
 *
 */
public class HEC_FromTriangles extends HEC_Creator {
	/**  */
	List<WB_Triangle> triangles;

	/**
	 *
	 */
	public HEC_FromTriangles() {
		super();
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param ts
	 * @return
	 */
	public HEC_FromTriangles setTriangles(final WB_Triangle[] ts) {
		triangles = new WB_List<>();
		for (final WB_Triangle tri : ts) {
			triangles.add(tri);
		}
		return this;
	}

	/**
	 *
	 *
	 * @param ts
	 * @return
	 */
	public HEC_FromTriangles setTriangles(final Collection<? extends WB_Triangle> ts) {
		triangles = new WB_List<>();
		triangles.addAll(ts);
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		if (triangles != null) {
			final WB_Coord[] vertices = new WB_Point[triangles.size() * 3];
			final int[][] faces = new int[triangles.size()][3];
			for (int i = 0; i < triangles.size(); i++) {
				vertices[3 * i] = triangles.get(i).p1();
				vertices[3 * i + 1] = triangles.get(i).p2();
				vertices[3 * i + 2] = triangles.get(i).p3();
				faces[i][0] = 3 * i;
				faces[i][1] = 3 * i + 1;
				faces[i][2] = 3 * i + 2;
			}
			final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
					.setCheckDuplicateVertices(true);
			return ffl.createBase();
		}
		return null;
	}
}
