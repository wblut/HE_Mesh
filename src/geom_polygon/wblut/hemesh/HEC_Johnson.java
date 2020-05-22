package wblut.hemesh;

import wblut.data.WB_JohnsonPolyhedraData01;
import wblut.data.WB_JohnsonPolyhedraData02;
import wblut.data.WB_JohnsonPolyhedraData03;
import wblut.data.WB_JohnsonPolyhedraData04;

/**
 *
 */
public class HEC_Johnson extends HEC_Creator {
	/**  */
	private double R;
	/**  */
	private int type;
	/**  */
	private String name;

	/**
	 *
	 */
	public HEC_Johnson() {
		super();
		R = 100;
		type = 1;
		name = "default";
	}

	/**
	 *
	 *
	 * @param type
	 * @param E
	 */
	public HEC_Johnson(final int type, final double E) {
		super();
		R = E;
		this.type = type;
		if (type < 1 || type > 92) {
			throw new IllegalArgumentException("Type of Johnson polyhedron should be between 1 and 92.");
		}
		if (type < 24) {
			name = WB_JohnsonPolyhedraData01.names[type - 1];
		} else if (type < 47) {
			name = WB_JohnsonPolyhedraData02.names[type - 24];
		} else if (type < 71) {
			name = WB_JohnsonPolyhedraData03.names[type - 47];
		} else {
			name = WB_JohnsonPolyhedraData04.names[type - 71];
		}
	}

	/**
	 *
	 *
	 * @param E
	 * @return
	 */
	public HEC_Johnson setEdge(final double E) {
		R = E;
		return this;
	}

	/**
	 *
	 *
	 * @param type
	 * @return
	 */
	public HEC_Johnson setType(final int type) {
		if (type < 1 || type > 92) {
			throw new IllegalArgumentException("Type of Johnson polyhedron should be between 1 and 92.");
		}
		this.type = type;
		if (type < 24) {
			name = WB_JohnsonPolyhedraData01.names[type - 1];
		} else if (type < 47) {
			name = WB_JohnsonPolyhedraData02.names[type - 24];
		} else if (type < 71) {
			name = WB_JohnsonPolyhedraData03.names[type - 47];
		} else {
			name = WB_JohnsonPolyhedraData04.names[type - 71];
		}
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		final double[][] vertices;
		final int[][] faces;
		if (type < 24) {
			vertices = WB_JohnsonPolyhedraData01.vertices[type - 1];
			faces = WB_JohnsonPolyhedraData01.faces[type - 1];
		} else if (type < 47) {
			vertices = WB_JohnsonPolyhedraData02.vertices[type - 24];
			faces = WB_JohnsonPolyhedraData02.faces[type - 24];
		} else if (type < 71) {
			vertices = WB_JohnsonPolyhedraData03.vertices[type - 47];
			faces = WB_JohnsonPolyhedraData03.faces[type - 47];
		} else {
			vertices = WB_JohnsonPolyhedraData04.vertices[type - 71];
			faces = WB_JohnsonPolyhedraData04.faces[type - 71];
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(vertices).setFaces(faces);
		final HE_Mesh result = fl.create();
		result.scaleSelf(R);
		return result;
	}

	/**
	 *
	 */
	public static void printTypes() {
		for (int i = 1; i <= 92; i++) {
			String name = "";
			if (i < 24) {
				name = WB_JohnsonPolyhedraData01.names[i - 1];
			} else if (i < 47) {
				name = WB_JohnsonPolyhedraData02.names[i - 24];
			} else if (i < 71) {
				name = WB_JohnsonPolyhedraData03.names[i - 47];
			} else {
				name = WB_JohnsonPolyhedraData04.names[i - 71];
			}
			System.out.println(i + ": " + name);
		}
	}
}
