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
 * Platonic polyhedra.
 *
 * @author Implemented by Frederik Vanhoutte (W:Blut), painstakingly collected
 *         by David Marec. Many thanks, without David this wouldn't be here.
 *
 */
public class HEC_Plato extends HEC_Creator {
	final static String[] names = WB_PolyhedraData.Pnames;
	final static double[][][] vertices = WB_PolyhedraData.Pvertices;
	final static int[][][] faces = WB_PolyhedraData.Pfaces;
	/**
	 *
	 */
	private double R;
	/**
	 *
	 */
	private int type;
	/**
	 *
	 */
	private String name;

	/**
	 * Instantiates a new Archimedean polyhedron.
	 *
	 */
	public HEC_Plato() {
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
	public HEC_Plato(final int type, final double E) {
		super();
		R = E;
		this.type = type;
		if (type < 1 || type > 5) {
			throw new IllegalArgumentException("Type of Platonic polyhedron should be between 1 and 5.");
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
	public HEC_Plato setEdge(final double E) {
		R = E;
		return this;
	}

	/**
	 *
	 *
	 * @param type
	 * @return
	 */
	public HEC_Plato setType(final int type) {
		if (type < 1 || type > 5) {
			throw new IllegalArgumentException("Type of Platonic polyhedron should be between 1 and 5.");
		}
		this.type = type;
		name = names[type - 1];
		return this;
	}

	/**
	 *
	 *
	 * @return
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
