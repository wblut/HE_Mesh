/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

/**
 * The Class HET_Import.
 */
public class HET_Import {
	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromHemeshFile(final String path) {
		HEC_FromHemeshFile ff = new HEC_FromHemeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromBinaryHemeshFile(final String path) {
		HEC_FromBinaryHemeshFile ff = new HEC_FromBinaryHemeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromBinarySTLFile(final String path) {
		HEC_FromBinarySTLFile ff = new HEC_FromBinarySTLFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromOBJFile(final String path) {
		HEC_FromOBJFile ff = new HEC_FromOBJFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromOBJFileNoCheck(final String path) {
		HEC_FromOBJFile ff = new HEC_FromOBJFile().setNoCheck(true);
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromObjFile(final String path) {
		HEC_FromOBJFile ff = new HEC_FromOBJFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromObjFileNoCheck(final String path) {
		HEC_FromOBJFile ff = new HEC_FromOBJFile().setNoCheck(true);
		ff.setPath(path);
		return new HE_Mesh(ff);
	}

	/**
	 *
	 *
	 * @param path
	 * @return
	 */
	public static HE_Mesh readFromSimpleMeshFile(final String path) {
		HEC_FromSimpleMeshFile ff = new HEC_FromSimpleMeshFile();
		ff.setPath(path);
		return new HE_Mesh(ff);
	}
}
