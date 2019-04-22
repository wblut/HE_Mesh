/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import processing.core.PApplet;

/**
 * HET_Recorder is used to record meshes drawn in Processing.
 */
public class HET_Recorder {
	/** Mesh buffer. */
	private HET_MeshBuffer meshBuffer;
	/** Calling applet. */
	private final PApplet home;
	/** Recorded meshes. */
	public HE_MeshCollection meshes;
	/** Number of meshes. */
	public int numberOfMeshes;

	/**
	 * Instantiates a new HET_Recorder.
	 *
	 * @param home
	 *            calling applet, typically "this"
	 */
	public HET_Recorder(final PApplet home) {
		this.home = home;
	}

	/**
	 * Start recorder.
	 */
	public void start() {
		meshes = new HE_MeshCollection();
		meshBuffer = (HET_MeshBuffer) home.createGraphics(home.width, home.height, "wblut.hemesh.HET_MeshBuffer");
		meshBuffer.home = home;
		home.beginRecord(meshBuffer);
	}

	/**
	 * Start next mesh.
	 */
	public void nextMesh() {
		meshBuffer.nextMesh();
	}

	/**
	 * Stop recorder.
	 */
	public void stop() {
		meshBuffer.nextMesh();
		meshes = meshBuffer.meshes;
		home.endRecord();
		meshBuffer = null;
	}

}
