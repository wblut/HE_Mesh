/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import wblut.data.WB_PolyhedraData;
import wblut.geom.WB_GeometryFactory;

/**
 * Catalan polyhedra.
 *
 *
 *
 */
public class HEC_MiscPolyhedron extends HEC_Creator {
	final static String[] names;
	static {
		names = new String[WB_PolyhedraData.Onames.length + 90];
		int id = 0;
		for (int i = 0; i < WB_PolyhedraData.Onames.length; i++) {
			names[id++] = WB_PolyhedraData.Onames[i];
		}
		for (int i = 0; i < 37; i++) {
			names[id++] = "stellated rhombic triacontahedron " + (i + 1);
		}
		for (int i = 0; i < 32; i++) {
			names[id++] = "tetrahedrally stellated icosahedron " + (i + 1);
		}
		for (int i = 0; i < 21; i++) {
			names[id++] = "stellated truncated tetrahedron " + (i + 1);
		}
	}
	final static double[][][]	vertices	= WB_PolyhedraData.Overtices;
	final static int[][][]		faces		= WB_PolyhedraData.Ofaces;
	/** Edge. */
	private double				R;
	/** Type. */
	private int					type;
	/** The name. */
	private String				name;

	/**
	 * Instantiates a new polyhedron.
	 *
	 */
	public HEC_MiscPolyhedron() {
		super();
		R = 100;
		type = 0;
		name = "default";
	}

	/**
	 * Instantiates a new polyhedron.
	 *
	 * @param type
	 *            the type
	 * @param E
	 *            edge length
	 */
	public HEC_MiscPolyhedron(final int type, final double E) {
		this();
		R = E;
		this.type = type;
		if (type < 1 || type > names.length) {
			throw new IllegalArgumentException(
					"Type of polyhedron should be between 1 and " + names.length
							+ ".");
		}
		name = names[type - 1];
	}

	/**
	 * Set scale.
	 *
	 * @param E
	 *            scale
	 * @return self
	 */
	@Override
	public HEC_MiscPolyhedron setScale(final double E) {
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
	public HEC_MiscPolyhedron setType(final int type) {
		if (type < 1 || type > names.length) {
			throw new IllegalArgumentException(
					"Type of polyhedron should be between 1 and " + names.length
							+ ".");
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
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	public HE_Mesh createBase() {
		if (type <= 5) {
			final double[][] verts = vertices[type - 1];
			final int[][] facs = faces[type - 1];
			final HEC_FromFacelist fl = new HEC_FromFacelist();
			fl.setVertices(verts).setFaces(facs).setCheckManifold(true);
			final HE_Mesh result = fl.create();
			result.scaleSelf(R);
			return result;
		} else if (type <= 149) {
			final HE_Mesh result = new HE_Mesh(WB_GeometryFactory.instance()
					.createPolyhedronFromWRL(name.replaceAll(" ", "_"), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (Exception e) {
				return new HE_Mesh(WB_GeometryFactory.instance()
						.createPolyhedronFromWRL(name.replaceAll(" ", "_"), R));
			}
		} else if (type <= 186) {
			final HE_Mesh result = new HE_Mesh(WB_GeometryFactory.instance()
					.createPolyhedronFromWRL("srtc_" + (type - 149), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (Exception e) {
				return new HE_Mesh(WB_GeometryFactory.instance()
						.createPolyhedronFromWRL("srtc_" + (type - 149), R));
			}
		} else if (type <= 218) {
			final HE_Mesh result = new HE_Mesh(WB_GeometryFactory.instance()
					.createPolyhedronFromWRL("tsi" + (type - 186), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (Exception e) {
				return new HE_Mesh(WB_GeometryFactory.instance()
						.createPolyhedronFromWRL("tsi" + (type - 186), R));
			}
		} else if (type <= 239) {
			final HE_Mesh result = new HE_Mesh(WB_GeometryFactory.instance()
					.createPolyhedronFromWRL("stt" + (type - 218), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (Exception e) {
				return new HE_Mesh(WB_GeometryFactory.instance()
						.createPolyhedronFromWRL("stt" + (type - 218), R));
			}
		}
		return new HE_Mesh();
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
