package wblut.geom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 */
public class WB_FromSimpleMeshFile {
	/**
	 *
	 */
	private WB_FromSimpleMeshFile() {
	}

	/**
	 *
	 *
	 * @param path
	 * @param scale
	 * @return
	 */
	public static WB_SimpleMesh createMesh(final String path, final double scale) {
		if (path == null) {
			return new WB_SimpleMesh();
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
		return new WB_SimpleMesh(vertices, faces);
	}
}
