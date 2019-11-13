/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.data.WB_PolyhedraData;

/**
 * Catalan polyhedra.
 *
 * by David Marec. Many thanks, without David this wouldn't be here.
 *
 */
public class HEC_Catalan extends HEC_Creator {
	final static String[] names = WB_PolyhedraData.Cnames;
	final static double[][][] vertices = WB_PolyhedraData.Cvertices;
	final static int[][][] faces = WB_PolyhedraData.Cfaces;
	/** Edge. */
	private double R;
	/** Type. */
	private int type;
	/** The name. */
	private String name;

	/**
	 * Instantiates a new Catalan polyhedron.
	 *
	 */
	public HEC_Catalan() {
		super();
		R = 100;
		type = 1;
		name = "default";
	}

	/**
	 * Instantiates a new Catalan polyhedron.
	 *
	 * @param type
	 *            the type
	 * @param E
	 *            edge length
	 */
	public HEC_Catalan(final int type, final double E) {
		super();
		R = E;
		this.type = type;
		if (type < 1 || type > names.length) {
			throw new IllegalArgumentException("Type of polyhedron should be between 1 and " + names.length + ".");
		}
		name = names[type - 1];
	}

	/**
	 * Set edge length.
	 *
	 * @param E
	 *            edge length
	 * @return self
	 */
	public HEC_Catalan setEdge(final double E) {
		R = E;
		return this;
	}

	/**
	 * Set type.
	 *
	 * @param type
	 *            the type
	 * @return self
	 */
	public HEC_Catalan setType(final int type) {
		if (type < 1 || type > 13) {
			throw new IllegalArgumentException("Type of polyhedron should be between 1 and " + names.length + ".");
		}
		this.type = type;
		name = names[type - 1];
		return this;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Creator#create()
	 */
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

	/**
	 *
	 */
	public static void printTypes() {
		for (int i = 0; i < names.length; i++) {
			System.out.println(i + 1 + ": " + names[i]);
		}
	}
}
