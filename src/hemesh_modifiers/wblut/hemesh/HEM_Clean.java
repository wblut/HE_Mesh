/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

/**
 *
 */
public class HEM_Clean extends HEM_Modifier {

	/**
	 *
	 */
	public HEM_Clean() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		mesh.getFaceColors();
		mesh.getFaceUserLabels();
		mesh.getFaceInternalLabels();
		mesh.getFaceTextureIds();
		mesh.getFaceVisibility();

		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(mesh.getPoints()).setFaces(mesh.getFacesAsInt())
				.setCheckDuplicateVertices(true).setUseFaceInformation(true)
				.setFaceInformation(mesh.getFaceColors(), mesh.getFaceUserLabels(), mesh.getFaceVisibility(),
						mesh.getFaceInternalLabels(), mesh.getFaceTextureIds())
				.setUseVertexInformation(true).setVertexInformation(mesh.getVertexColors(), mesh.getVertexVisibility(),
						mesh.getVertexUserLabels(), mesh.getVertexInternalLabels());
		ffl.setCheckNormals(true);

		mesh.setNoCopy(ffl.create());
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
