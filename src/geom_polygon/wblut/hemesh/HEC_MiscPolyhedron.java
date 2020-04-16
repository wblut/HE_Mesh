package wblut.hemesh;

import wblut.data.WB_PolyhedraData;
import wblut.geom.WB_GeometryFactory3D;

public class HEC_MiscPolyhedron extends HEC_Creator {
	final static String[] names;
	static {
		names = new String[WB_PolyhedraData.Onames.length + 90];
		int id = 0;
		for (final String oname : WB_PolyhedraData.Onames) {
			names[id++] = oname;
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
	final static double[][][] vertices = WB_PolyhedraData.Overtices;
	final static int[][][] faces = WB_PolyhedraData.Ofaces;
	private int type;
	private String name;

	public HEC_MiscPolyhedron() {
		super();
		set("scale", 100);
		type = 0;
		name = "default";
	}

	public HEC_MiscPolyhedron(final int type, final double E) {
		this();
		set("scale", E);
		this.type = type;
		if (type < 1 || type > names.length) {
			throw new IllegalArgumentException("Type of polyhedron should be between 1 and " + names.length + ".");
		}
		name = names[type - 1];
	}

	public HEC_MiscPolyhedron setType(final int type) {
		if (type < 1 || type > names.length) {
			throw new IllegalArgumentException("Type of polyhedron should be between 1 and " + names.length + ".");
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
		final double R = getScale();
		if (type <= 5) {
			final double[][] verts = vertices[type - 1];
			final int[][] facs = faces[type - 1];
			final HEC_FromFacelist fl = new HEC_FromFacelist();
			fl.setVertices(verts).setFaces(facs).setCheckManifold(true);
			final HE_Mesh result = fl.create();
			result.scaleSelf(R);
			return result;
		} else if (type <= 149) {
			final HE_Mesh result = new HE_Mesh(
					WB_GeometryFactory3D.instance().createPolyhedronFromWRL(name.replaceAll(" ", "_"), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (final Exception e) {
				return new HE_Mesh(
						WB_GeometryFactory3D.instance().createPolyhedronFromWRL(name.replaceAll(" ", "_"), R));
			}
		} else if (type <= 186) {
			final HE_Mesh result = new HE_Mesh(
					WB_GeometryFactory3D.instance().createPolyhedronFromWRL("srtc_" + (type - 149), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (final Exception e) {
				return new HE_Mesh(WB_GeometryFactory3D.instance().createPolyhedronFromWRL("srtc_" + (type - 149), R));
			}
		} else if (type <= 218) {
			final HE_Mesh result = new HE_Mesh(
					WB_GeometryFactory3D.instance().createPolyhedronFromWRL("tsi" + (type - 186), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (final Exception e) {
				return new HE_Mesh(WB_GeometryFactory3D.instance().createPolyhedronFromWRL("tsi" + (type - 186), R));
			}
		} else if (type <= 239) {
			final HE_Mesh result = new HE_Mesh(
					WB_GeometryFactory3D.instance().createPolyhedronFromWRL("stt" + (type - 218), R));
			try {
				HET_Fixer.fixNonManifoldVertices(result);
				return result;
			} catch (final Exception e) {
				return new HE_Mesh(WB_GeometryFactory3D.instance().createPolyhedronFromWRL("stt" + (type - 218), R));
			}
		}
		return new HE_Mesh();
	}

	public static void printTypes() {
		for (int i = 0; i < names.length; i++) {
			System.out.println(i + 1 + ": " + names[i]);
		}
	}
}
