package wblut.hemesh;

import processing.core.PApplet;

public class HET_Recorder {
	private HET_MeshBuffer meshBuffer;
	private final PApplet home;
	public HE_MeshCollection meshes;
	public int numberOfMeshes;

	public HET_Recorder(final PApplet home) {
		this.home = home;
	}

	public void start() {
		meshes = new HE_MeshCollection();
		meshBuffer = (HET_MeshBuffer) home.createGraphics(home.width, home.height, "wblut.hemesh.HET_MeshBuffer");
		meshBuffer.home = home;
		home.beginRecord(meshBuffer);
	}

	public void nextMesh() {
		meshBuffer.nextMesh();
	}

	public void stop() {
		meshBuffer.nextMesh();
		meshes = meshBuffer.meshes;
		home.endRecord();
		meshBuffer = null;
	}
}
