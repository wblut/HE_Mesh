package wblut.hemesh;

/**
 *
 */
public class HEMC_Explode extends HEMC_MultiCreator {
	/**  */
	private HE_Mesh mesh;

	/**
	 *
	 */
	public HEMC_Explode() {
		super();
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HEMC_Explode(final HE_Mesh mesh) {
		super();
		setMesh(mesh);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEMC_Explode setMesh(final HE_Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @param result
	 */
	@Override
	void create(final HE_MeshCollection result) {
		if (mesh == null) {
			_numberOfMeshes = 0;
			return;
		}
		mesh.clearVisitedElements();
		HE_Face start = mesh.getFaceWithIndex(0);
		int lastfound = 0;
		HE_Selection submesh;
		do {
			// find next unvisited face
			for (int i = lastfound; i < mesh.getNumberOfFaces(); i++) {
				start = mesh.getFaceWithIndex(i);
				lastfound = i;
				if (!start.isVisited()) {// found
					break;
				}
			}
			// reached last face, was already visited
			if (start.isVisited()) {
				break;
			}
			start.setVisited();// visited
			submesh = HE_Selection.getSelection(mesh);
			submesh.add(start);
			// find all unvisited faces connected to face
			HE_RAS<HE_Face> facesToProcess = new HE_RAS<>();
			HE_RAS<HE_Face> newFacesToProcess;
			facesToProcess.add(start);
			HE_FaceList neighbors;
			do {
				newFacesToProcess = new HE_RAS<>();
				for (final HE_Face f : facesToProcess) {
					neighbors = f.getNeighborFaces();
					for (final HE_Face neighbor : neighbors) {
						if (!neighbor.isVisited()) {
							neighbor.setVisited();// visited
							submesh.add(neighbor);
							newFacesToProcess.add(neighbor);
						}
					}
				}
				facesToProcess = newFacesToProcess;
			} while (facesToProcess.size() > 0);
			result.add(submesh.getAsMesh());
		} while (true);
	}
}
