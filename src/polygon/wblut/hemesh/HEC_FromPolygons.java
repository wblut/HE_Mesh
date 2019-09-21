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

import org.eclipse.collections.impl.list.mutable.FastList;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Polygon;

/**
 * Creates a new mesh from a list of polygons. Duplicate vertices are fused.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEC_FromPolygons extends HEC_Creator {

	/**
	 *
	 */
	private WB_Polygon[] polygons;

	/**
	 *
	 */
	private boolean checkNormals;

	/**
	 * Instantiates a new HEC_FromPolygons.
	 *
	 */
	public HEC_FromPolygons() {
		super();
		setOverride(true);
	}

	/**
	 * Instantiates a new HEC_FromPolygons.
	 *
	 * @param qs
	 *            the qs
	 */
	public HEC_FromPolygons(final WB_Polygon[] qs) {
		this();
		polygons = qs;
	}

	/**
	 * Instantiates a new hE c_ from polygons.
	 *
	 * @param qs
	 *            the qs
	 */
	public HEC_FromPolygons(final Collection<? extends WB_Polygon> qs) {
		this();
		setPolygons(qs);
	}

	/**
	 * Sets the source polygons.
	 *
	 * @param qs
	 *            source polygons
	 * @return self
	 */
	public HEC_FromPolygons setPolygons(final WB_Polygon[] qs) {
		polygons = qs;
		return this;
	}

	/**
	 * Sets the source polygons.
	 *
	 * @param qs
	 *            source polygons
	 * @return self
	 */
	public HEC_FromPolygons setPolygons(final Collection<? extends WB_Polygon> qs) {
		final int n = qs.size();
		polygons = new WB_Polygon[n];
		int i = 0;
		for (final WB_Polygon poly : qs) {
			polygons[i] = poly;
			i++;
		}
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromPolygons setCheckNormals(final boolean b) {
		checkNormals = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (polygons != null) {
			if (polygons.length > 0) {
				final int nq = polygons.length;
				final FastList<WB_Coord> vertices = new FastList<WB_Coord>();
				final FastList<int[]> faces = new FastList<int[]>();
				int id = 0;
				for (int i = 0; i < nq; i++) {
					WB_Polygon poly = polygons[i];
					if (poly.isSimple()) {
						int[] face = new int[poly.getNumberOfPoints()];
						for (int j = 0; j < poly.getNumberOfPoints(); j++) {
							vertices.add(poly.getPoint(j));
							face[j] = id;
							id++;
						}
						faces.add(face);
					} else {
						int[] tris = poly.getTriangles();
						for (int j = 0; j < tris.length; j += 3) {
							int[] face = new int[3];
							vertices.add(poly.getPoint(tris[j]));
							face[0] = id;
							id++;
							vertices.add(poly.getPoint(tris[j + 1]));
							face[1] = id;
							id++;
							vertices.add(poly.getPoint(tris[j + 2]));
							face[2] = id;
							id++;
							faces.add(face);
						}
					}
				}

				final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
						.setCheckDuplicateVertices(true);
				ffl.setCheckNormals(checkNormals);

				return ffl.createBase();
			}
		}
		return new HE_Mesh();
	}
}
