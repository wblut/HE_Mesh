package wblut.hemesh;

public class HEM_Clean extends HEM_Modifier {
	public HEM_Clean() {
		super();
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		mesh.removeUnconnectedElements();
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(mesh.getPoints()).setFaces(mesh.getFacesAsInt())
				.setCheckDuplicateVertices(true)
				.setFaceInformation(mesh.getFaceColors(), mesh.getFaceLabels(), mesh.getFaceVisibility(),
						mesh.getFaceInternalLabels(), mesh.getFaceTextureIds())
				.setVertexInformation(mesh.getVertexColors(), mesh.getVertexVisibility(), mesh.getVertexLabels(),
						mesh.getVertexInternalLabels());
		ffl.setCheckNormals(true);
		mesh.setNoCopy(ffl.create());
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
