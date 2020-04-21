package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_CoordList;
import wblut.geom.WB_List;
import wblut.geom.WB_Polygon;

public class HEC_FromPolygons extends HEC_Creator {
	private WB_Polygon[] polygons;
	private boolean checkNormals;

	public HEC_FromPolygons() {
		super();
		setOverride(true);
	}

	public HEC_FromPolygons(final WB_Polygon[] qs) {
		this();
		polygons = qs;
	}

	public HEC_FromPolygons(final Collection<? extends WB_Polygon> qs) {
		this();
		setPolygons(qs);
	}

	public HEC_FromPolygons setPolygons(final WB_Polygon[] qs) {
		polygons = qs;
		return this;
	}

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

	@Override
	protected HE_Mesh createBase() {
		if (polygons != null) {
			if (polygons.length > 0) {
				final int nq = polygons.length;
				final WB_CoordList vertices = new WB_CoordList();
				final WB_List<int[]> faces = new WB_List<>();
				int id = 0;
				for (int i = 0; i < nq; i++) {
					final WB_Polygon poly = polygons[i];
					if (poly.isSimple()) {
						final int[] face = new int[poly.getNumberOfPoints()];
						for (int j = 0; j < poly.getNumberOfPoints(); j++) {
							vertices.add(poly.getPoint(j));
							face[j] = id;
							id++;
						}
						faces.add(face);
					} else {
						final int[] tris = poly.getTriangles();
						for (int j = 0; j < tris.length; j += 3) {
							final int[] face = new int[3];
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
