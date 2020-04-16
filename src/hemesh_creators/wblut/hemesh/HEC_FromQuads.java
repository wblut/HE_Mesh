package wblut.hemesh;

import java.util.Collection;

import wblut.geom.WB_Point;
import wblut.geom.WB_Quad;

public class HEC_FromQuads extends HEC_Creator {
	private WB_Quad[] quads;

	public HEC_FromQuads() {
		super();
		setOverride(true);
	}

	public HEC_FromQuads(final WB_Quad[] qs) {
		this();
		quads = qs;
	}

	public HEC_FromQuads(final Collection<WB_Quad> qs) {
		this();
		setQuads(qs);
	}

	public HEC_FromQuads setQuads(final WB_Quad[] qs) {
		quads = qs;
		return this;
	}

	public HEC_FromQuads setQuads(final Collection<WB_Quad> qs) {
		final int n = qs.size();
		quads = new WB_Quad[n];
		int i = 0;
		for (final WB_Quad quad : qs) {
			quads[i] = quad;
			i++;
		}
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (quads != null) {
			final int nq = quads.length;
			final WB_Point[] vertices = new WB_Point[nq * 4];
			final int[][] faces = new int[nq][4];
			for (int i = 0; i < nq; i++) {
				vertices[4 * i] = quads[i].getP1();
				vertices[4 * i + 1] = quads[i].getP2();
				vertices[4 * i + 2] = quads[i].getP3();
				vertices[4 * i + 3] = quads[i].getP4();
				faces[i][0] = 4 * i;
				faces[i][1] = 4 * i + 1;
				faces[i][2] = 4 * i + 2;
				faces[i][3] = 4 * i + 3;
			}
			final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
					.setCheckDuplicateVertices(true);
			return ffl.createBase();
		}
		return null;
	}
}
