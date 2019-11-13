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
 * Archimedean polyhedra.
 *
 * @author Implemented by Frederik Vanhoutte (W:Blut), painstakingly collected
 *         by David Marec. Many thanks, without David this wouldn't be here.
 *
 */
public class HEC_Archimedes extends HEC_Creator {
	final static String[] names = WB_PolyhedraData.Anames;
	final static double[][][] vertices = WB_PolyhedraData.Avertices;
	final static int[][][] faces = WB_PolyhedraData.Afaces;
	/** Edge. */
	private double R;
	/** Type. */
	private int type;
	/** The name. */
	private String name;

	/**
	 * Instantiates a new Archimedean polyhedron.
	 *
	 */
	public HEC_Archimedes() {
		super();
		R = 100;
		type = 1;
		name = "default";
	}

	/**
	 * Instantiates a new Archimedean polyhedron.
	 *
	 * @param type
	 *            the type
	 * @param E
	 *            edge length
	 */
	public HEC_Archimedes(final int type, final double E) {
		super();
		R = E;
		this.type = type;
		if (type < 1 || type > 13) {
			throw new IllegalArgumentException("Type of Archimedean polyhedron should be between 1 and 13.");
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
	public HEC_Archimedes setEdge(final double E) {
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
	public HEC_Archimedes setType(final int type) {
		if (type < 1 || type > 13) {
			throw new IllegalArgumentException("Type of Archimedean polyhedron should be between 1 and 13.");
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
