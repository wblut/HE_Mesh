package wblut.hemesh;

public class HET_Import {
	public static HE_Mesh readFromHemeshFile(final String path) {
		final HEC_FromHemeshFile ff = new HEC_FromHemeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromBinaryHemeshFile(final String path) {
		final HEC_FromBinaryHemeshFile ff = new HEC_FromBinaryHemeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromBinarySTLFile(final String path) {
		final HEC_FromBinarySTLFile ff = new HEC_FromBinarySTLFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromOBJFile(final String path) {
		final HEC_FromOBJFile ff = new HEC_FromOBJFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromOBJFileNoCheck(final String path) {
		final HEC_FromOBJFile ff = new HEC_FromOBJFile().setNoCheck(true);
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromObjFile(final String path) {
		final HEC_FromOBJFile ff = new HEC_FromOBJFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromObjFileNoCheck(final String path) {
		final HEC_FromOBJFile ff = new HEC_FromOBJFile().setNoCheck(true);
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	public static HE_Mesh readFromSimpleMeshFile(final String path) {
		final HEC_FromSimpleMeshFile ff = new HEC_FromSimpleMeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}
}
