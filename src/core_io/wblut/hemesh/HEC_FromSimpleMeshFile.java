package wblut.hemesh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import wblut.geom.WB_Point;

public class HEC_FromSimpleMeshFile extends HEC_Creator {
	private String path;

	public HEC_FromSimpleMeshFile() {
		super();
		setOverride(true);
	}

	public HEC_FromSimpleMeshFile(final String path) {
		this();
		this.path = path;
	}

	public HEC_FromSimpleMeshFile setPath(final String path) {
		this.path = path;
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (path == null) {
			return null;
		}
		final StringBuilder contents = new StringBuilder();
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			final BufferedReader input = new BufferedReader(new FileReader(path));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		final String data = contents.toString();
		final String[] result = data.split(System.getProperty("line.separator"));
		int id = 0;
		final int numVertices = Integer.parseInt(result[id]);
		id++;
		final WB_Point[] vertices = new WB_Point[numVertices];
		String[] subresult;
		double x, y, z;
		for (int i = 0; i < numVertices; i++) {
			subresult = result[id].split("\\s");
			x = Double.parseDouble(subresult[0]);
			y = Double.parseDouble(subresult[1]);
			z = Double.parseDouble(subresult[2]);
			vertices[i] = new WB_Point(x, y, z);
			id++;
		}
		final int numFaces = Integer.parseInt(result[id]);
		id++;
		final int[][] faces = new int[numFaces][];
		int nv;
		for (int i = 0; i < numFaces; i++) {
			subresult = result[id].split("\\s");
			id++;
			nv = Integer.parseInt(subresult[0]);
			faces[i] = new int[nv];
			for (int j = 0; j < nv; j++) {
				faces[i][j] = Integer.parseInt(subresult[j + 1]);
			}
		}
		final HEC_FromFacelist ffl = new HEC_FromFacelist().setVertices(vertices).setFaces(faces)
				.setCheckDuplicateVertices(false);
		return ffl.createBase();
	}
}
