package wblut.geom;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.eclipse.collections.impl.list.mutable.FastList;

public class WB_FromOBJFile {
	/**
	 *
	 */
	private WB_FromOBJFile() {
	}

	public static WB_SimpleMesh readMesh(String path, double scale) {
		if (path == null) {
			return new WB_SimpleMesh();
		}
		final File file = new File(path);
		final List<WB_Point> vertexList = new FastList<WB_Point>();
		final List<int[]> faceList = new FastList<int[]>();
		int faceCount = 0;
		try (InputStream is = createInputStream(file);
				final BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));) {
			if (is == null) {
				return new WB_SimpleMesh();
			}
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] parts = line.split("\\s+");
				if (parts[0].equals("v")) {
					final double x1 = scale * Double.parseDouble(parts[1]);
					final double y1 = scale * Double.parseDouble(parts[2]);
					final double z1 = parts.length > 3
							? scale * Double.parseDouble(parts[3])
							: 0;
					final WB_Point pointLoc = new WB_Point(x1, y1, z1);
					vertexList.add(pointLoc);
				}
				if (parts[0].equals("f")) {
					final int[] tempFace = new int[parts.length - 1];
					for (int j = 0; j < parts.length - 1; j++) {
						final String[] num = parts[j + 1].split("/");
						tempFace[j] = Integer.parseInt(num[0]) - 1;
					}
					faceList.add(tempFace);
					faceCount++;
				}
			}
			final int[][] faces = new int[faceCount][];
			for (int i = 0; i < faceCount; i++) {
				final int[] tempFace = faceList.get(i);
				faces[i] = tempFace;
			}
			return new WB_SimpleMesh(vertexList, faces);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return new WB_SimpleMesh();
	}

	// Code excerpts from processing.core
	/**
	 *
	 *
	 * @param file
	 * @return
	 */
	private static InputStream createInputStream(final File file) {
		if (file == null) {
			throw new IllegalArgumentException("file can't be null");
		}
		try {
			InputStream stream = new FileInputStream(file);
			if (file.getName().toLowerCase().endsWith(".gz")) {
				stream = new GZIPInputStream(stream);
			}
			return stream;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
