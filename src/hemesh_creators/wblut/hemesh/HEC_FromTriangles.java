/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Collection;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.geom.WB_Triangle;

/**
 * Creates a new mesh from a list of triangles. Duplicate vertices are fused.
 *
 * the generic type
 *
 * @author Frederik Vanhoutte (W:Blut)
 */
public class HEC_FromTriangles extends HEC_Creator {
	/** Source triangles. */
	List<WB_Triangle> triangles;

	/**
	 * Instantiates a new HEC_FromTriangles.
	 *
	 */
	public HEC_FromTriangles() {
		super();
		setOverride(true);
	}

	/**
	 * Sets the source triangles.
	 *
	 * @param ts
	 *            source triangles
	 * @return self
	 */
	public HEC_FromTriangles setTriangles(final WB_Triangle[] ts) {
		triangles = new FastList<WB_Triangle>();
		for (final WB_Triangle tri : ts) {
			triangles.add(tri);
		}
		return this;
	}

	/**
	 * Sets the source triangles.
	 *
	 * @param ts
	 *            source triangles
	 * @return self
	 */
	public HEC_FromTriangles setTriangles(final Collection<? extends WB_Triangle> ts) {
		triangles = new FastList<WB_Triangle>();
		triangles.addAll(ts);
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
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
