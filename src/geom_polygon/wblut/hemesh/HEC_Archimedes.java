package wblut.hemesh;

import wblut.data.WB_PolyhedraData;

public class HEC_Archimedes extends HEC_Creator {
	final static String[] names = WB_PolyhedraData.Anames;
	final static double[][][] vertices = WB_PolyhedraData.Avertices;
	final static int[][][] faces = WB_PolyhedraData.Afaces;
	private double R;
	private int type;
	private String name;

	public HEC_Archimedes() {
		super();
		R = 100;
		type = 1;
		name = "default";
	}

	public HEC_Archimedes(final int type, final double E) {
		super();
		R = E;
		this.type = type;
		if (type < 1 || type > 13) {
			throw new IllegalArgumentException("Type of Archimedean polyhedron should be between 1 and 13.");
		}
		name = names[type - 1];
	}

	public HEC_Archimedes setEdge(final double E) {
		R = E;
		return this;
	}

	public HEC_Archimedes setType(final int type) {
		if (type < 1 || type > 13) {
			throw new IllegalArgumentException("Type of Archimedean polyhedron should be between 1 and 13.");
		}
		this.type = type;
		name = names[type - 1];
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public HE_Mesh createBase() {
		final double[][] verts = vertices[type - 1];
		final int[][] facs = faces[type - 1];
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(verts).setFaces(facs);
		final HE_Mesh result = fl.create();
		result.scaleSelf(R);
		return result;
	}

	public static void printTypes() {
		for (int i = 0; i < names.length; i++) {
			System.out.println(i + 1 + ": " + names[i]);
		}
	}
}
