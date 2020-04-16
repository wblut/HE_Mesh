package wblut.hemesh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import wblut.geom.WB_Coord;
import wblut.geom.WB_IndexedObjectMap;
import wblut.geom.WB_Point;
import wblut.math.WB_Epsilon;

public class HEC_IsoSkin extends HEC_Creator {
	private Substrate substrate;
	private double isolevel;

	public HEC_IsoSkin() {
		setOverride(true);
		substrate = null;
		isolevel = 0.5;
	}

	public HEC_IsoSkin(final HE_Mesh mesh, final double[] layers) {
		this();
		substrate = new Substrate(mesh, layers);
		isolevel = 0.5;
	}

	public HEC_IsoSkin(final HE_Mesh mesh, final int layers, final double offset, final double d) {
		this();
		final double[] l = new double[layers + 1];
		for (int i = 0; i <= layers; i++) {
			l[i] = offset + d * i;
		}
		if (checkMesh(mesh)) {
			substrate = new Substrate(mesh, l);
		}
		isolevel = 0.5;
	}

	public HEC_IsoSkin setSubstrate(final HE_Mesh mesh, final double[] layers) {
		if (checkMesh(mesh)) {
			substrate = new Substrate(mesh, layers);
		}
		return this;
	}

	public HEC_IsoSkin setSubstrate(final HE_Mesh mesh, final int layers, final double offset, final double d) {
		final double[] l = new double[layers];
		for (int i = 0; i < layers; i++) {
			l[i] = offset + d * i;
		}
		if (checkMesh(mesh)) {
			substrate = new Substrate(mesh, l);
		} else {
			throw (new IllegalArgumentException("HEC_IsoSkin substrate can only have triangle or quad faces."));
		}
		isolevel = 0.5;
		return this;
	}

	private boolean checkMesh(final HE_Mesh mesh) {
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		int fd;
		while (fItr.hasNext()) {
			f = fItr.next();
			fd = f.getFaceDegree();
			if (fd != 3 && fd != 4) {
				return false;
			}
		}
		return true;
	}

	public HEC_IsoSkin setValues(final double[][] values) {
		substrate.setValues(values);
		return this;
	}

	public HEC_IsoSkin setValuesBilayer() {
		substrate.setValuesBilayer();
		return this;
	}

	public HEC_IsoSkin setValuesSolid() {
		substrate.setValuesSolid();
		return this;
	}

	public HEC_IsoSkin setValuesLayers() {
		substrate.setValuesLayers();
		return this;
	}

	public HEC_IsoSkin setIsolevel(final double isolevel) {
		this.isolevel = isolevel;
		return this;
	}

	public int getNumberOfLayers() {
		return substrate.getNumberOfLayers();
	}

	@Override
	protected HE_Mesh createBase() {
		if (substrate == null || substrate.values == null || substrate.values.length != substrate.numberOfLayers + 1
				|| substrate.values[0].length != substrate.numberOfVertices) {
			return new HE_Mesh();
		}
		return substrate.getSkin(isolevel);
	}

	public WB_Coord[][] getGridpositions() {
		return substrate.getGridpositions();
	}

	public Cell[][] getCells() {
		return substrate.getCells3D();
	}

	private static class Substrate {
		final static int ONVERTEX = 0;
		final static int ONEDGE = 1;
		final static int EQUAL = 1;
		final static int POSITIVE = 2;
		private final int numberOfLayers;
		private double[][] values;
		private Cell[][] cells3D;
		private Cell[][] cells2D;
		private WB_Coord[][] gridpositions;
		private HE_ObjectMap<HE_Vertex> edges;
		private final HE_Mesh substrate;
		private final int numberOfVertices;
		private final int totalNumberOfVertices;
		private int[] digits;
		private HE_IntMap keysToIndex;
		final static int[][] isovertices2D = new int[][] { { ONVERTEX, 0 }, { ONVERTEX, 1 }, { ONVERTEX, 2 },
				{ ONVERTEX, 3 }, { ONEDGE, 0 }, { ONEDGE, 1 }, { ONEDGE, 2 }, { ONEDGE, 3 } };
		// ISOVERTICES3D: 20
		// type=ONVERTEX iso vertex on vertex, index in vertex list
		// type=ONEDGE iso vertex on edge, index in edge list
		final static int[][] isovertices3D = new int[][] { { ONVERTEX, 0 }, { ONVERTEX, 1 }, { ONVERTEX, 2 },
				{ ONVERTEX, 3 }, { ONVERTEX, 4 }, { ONVERTEX, 5 }, { ONVERTEX, 6 }, { ONVERTEX, 7 }, { ONEDGE, 0 },
				{ ONEDGE, 1 }, { ONEDGE, 2 }, { ONEDGE, 3 }, { ONEDGE, 4 }, { ONEDGE, 5 }, { ONEDGE, 6 }, { ONEDGE, 7 },
				{ ONEDGE, 8 }, { ONEDGE, 9 }, { ONEDGE, 10 }, { ONEDGE, 11 } };
		final static int[][] entries2D = new int[][] { { 0 }, { 0 }, { 1, 3, 0, 4, 5 }, { 0 }, { 0 }, { 1, 3, 0, 1, 5 },
				{ 1, 3, 1, 6, 4 }, { 1, 3, 1, 6, 0 }, { 1, 4, 0, 1, 6, 5 }, { 0 }, { 0 }, { 1, 3, 0, 4, 2 }, { 0 },
				{ 1, 3, 0, 1, 2 }, { 1, 3, 0, 1, 2 }, { 2, 3, 1, 6, 2, 3, 1, 2, 4 }, { 2, 3, 0, 6, 2, 3, 6, 0, 1 },
				{ 2, 3, 0, 6, 2, 3, 6, 0, 1 }, { 1, 3, 2, 5, 7 }, { 1, 3, 2, 0, 7 }, { 1, 4, 0, 4, 7, 2 },
				{ 2, 3, 5, 1, 2, 3, 1, 7, 2 }, { 2, 3, 1, 7, 2, 3, 1, 2, 0 }, { 2, 3, 1, 7, 2, 3, 1, 2, 0 },
				{ 4, 3, 5, 4, 1, 3, 5, 1, 2, 3, 6, 7, 2, 3, 6, 2, 1 }, { 3, 3, 0, 6, 7, 3, 6, 0, 1, 3, 0, 7, 2 },
				{ 3, 3, 0, 6, 7, 3, 0, 7, 2, 3, 6, 0, 1 }, { 0 }, { 0 }, { 2, 3, 0, 4, 3, 3, 0, 3, 5 }, { 0 },
				{ 1, 3, 3, 0, 1 }, { 2, 3, 1, 3, 5, 3, 1, 5, 0 }, { 1, 3, 1, 3, 4 }, { 1, 3, 1, 3, 0 },
				{ 2, 3, 3, 5, 1, 3, 1, 5, 0 }, { 0 }, { 1, 3, 2, 0, 3 }, { 2, 3, 4, 3, 2, 3, 4, 2, 0 },
				{ 1, 3, 2, 1, 3 }, { 1, 4, 0, 1, 3, 2 }, { 1, 4, 0, 1, 3, 2 }, { 2, 3, 2, 4, 3, 3, 3, 4, 1 },
				{ 1, 4, 0, 1, 3, 2 }, { 1, 4, 0, 1, 3, 2 }, { 1, 3, 5, 3, 2 }, { 1, 3, 0, 3, 2 },
				{ 2, 3, 4, 3, 0, 3, 0, 3, 2 }, { 2, 3, 5, 1, 3, 3, 5, 3, 2 }, { 1, 4, 0, 1, 3, 2 },
				{ 1, 4, 0, 1, 3, 2 }, { 3, 3, 5, 4, 3, 3, 3, 4, 1, 3, 5, 3, 2 }, { 1, 4, 0, 1, 3, 2 },
				{ 1, 4, 0, 1, 3, 2 }, { 1, 3, 3, 7, 6 }, { 2, 3, 0, 6, 3, 3, 0, 3, 7 },
				{ 4, 3, 4, 6, 0, 3, 0, 6, 3, 3, 7, 5, 0, 3, 7, 0, 3 }, { 1, 3, 3, 7, 1 }, { 2, 3, 1, 7, 0, 3, 7, 1, 3 },
				{ 3, 3, 7, 5, 1, 3, 1, 5, 0, 3, 7, 1, 3 }, { 1, 4, 7, 4, 1, 3 }, { 2, 3, 7, 0, 1, 3, 7, 1, 3 },
				{ 3, 3, 7, 5, 1, 3, 1, 5, 0, 3, 7, 1, 3 }, { 1, 3, 2, 6, 3 }, { 2, 3, 0, 6, 2, 3, 2, 6, 3 },
				{ 3, 3, 4, 6, 2, 3, 2, 6, 3, 3, 4, 2, 0 }, { 1, 3, 2, 1, 3 }, { 1, 4, 0, 1, 3, 2 },
				{ 1, 4, 0, 1, 3, 2 }, { 2, 3, 2, 4, 3, 3, 3, 4, 1 }, { 1, 4, 0, 1, 3, 2 }, { 1, 4, 0, 1, 3, 2 },
				{ 1, 4, 5, 6, 3, 2 }, { 2, 3, 0, 6, 2, 3, 2, 6, 3 }, { 3, 3, 2, 4, 6, 3, 4, 2, 0, 3, 2, 6, 3 },
				{ 2, 3, 5, 1, 2, 3, 2, 1, 3 }, { 1, 4, 0, 1, 3, 2 }, { 1, 4, 0, 1, 3, 2 },
				{ 3, 3, 3, 5, 4, 3, 5, 3, 2, 3, 3, 4, 1 }, { 1, 4, 0, 1, 3, 2 }, { 1, 4, 0, 1, 3, 2 } };
		int[][] entries3D;
		private WB_IndexedObjectMap<HE_Vertex> vertices;

		Substrate(final HE_Mesh mesh, final double[] d) {
			this.substrate = mesh;
			this.numberOfLayers = d.length - 1;
			numberOfVertices = mesh.getNumberOfVertices();
			totalNumberOfVertices = numberOfVertices * (numberOfLayers + 1);
			setGridPositions(d);
			createGrid();
			keysToIndex = null;
			String line = "";
			final String cvsSplitBy = " ";
			BufferedReader br = null;
			InputStream is = null;
			InputStreamReader isr = null;
			entries3D = new int[6561][];
			try {
				is = this.getClass().getClassLoader().getResourceAsStream("resources/isonepcube3D.txt");
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				int i = 0;
				while ((line = br.readLine()) != null) {
					final String[] cell = line.split(cvsSplitBy);
					final int[] indices = new int[cell.length];
					for (int j = 0; j < cell.length; j++) {
						indices[j] = Integer.parseInt(cell[j]);
					}
					entries3D[i] = indices;
					i++;
				}
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
						isr.close();
						is.close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		void setGridPositions(final double d[]) {
			gridpositions = new WB_Coord[numberOfLayers + 1][numberOfVertices];
			values = new double[numberOfLayers + 1][numberOfVertices];
			keysToIndex = new HE_IntMap();
			final HE_VertexIterator vitr = substrate.vItr();
			HE_Vertex v;
			WB_Coord n;
			int j = 0;
			while (vitr.hasNext()) {
				v = vitr.next();
				n = HE_MeshOp.getVertexNormal(v);
				keysToIndex.put(v, j);
				for (int i = 0; i < numberOfLayers + 1; i++) {
					gridpositions[i][j] = v.getPosition().addMul(d[i], n);
					values[i][j] = -1;
				}
				j++;
			}
		}

		void setValues(final double[][] values) {
			for (int i = 0; i < numberOfLayers; i++) {
				for (int j = 0; j < numberOfVertices; j++) {
					this.values[i][j] = values[i][j];
				}
			}
		}

		void setValuesBilayer() {
			final HE_VertexIterator vitr = substrate.vItr();
			int j = 0;
			while (vitr.hasNext()) {
				vitr.next();
				for (int i = 1; i < numberOfLayers; i++) {
					values[i][j] = i == 1 || i == numberOfLayers - 1 ? 1 : -1;
				}
				j++;
			}
		}

		void setValuesSolid() {
			final HE_VertexIterator vitr = substrate.vItr();
			int j = 0;
			while (vitr.hasNext()) {
				vitr.next();
				for (int i = 1; i < numberOfLayers; i++) {
					values[i][j] = 1;
				}
				j++;
			}
		}

		void setValuesLayers() {
			final HE_VertexIterator vitr = substrate.vItr();
			int j = 0;
			while (vitr.hasNext()) {
				vitr.next();
				for (int i = 1; i < numberOfLayers; i++) {
					values[i][j] = 2 * (i % 2) - 1;
				}
				j++;
			}
		}

		private void createGrid() {
			final int nof = substrate.getNumberOfFaces();
			setCells3D(new Cell[numberOfLayers][nof]);
			HE_Halfedge he;
			int j = 0;
			int[] cornerIndices;
			final HE_FaceIterator fitr = substrate.fItr();
			HE_Face f;
			while (fitr.hasNext()) {
				f = fitr.next();
				cornerIndices = new int[4];
				he = f.getHalfedge();
				cornerIndices[0] = keysToIndex.get(he.getVertex());
				he = he.getNextInFace();
				cornerIndices[1] = keysToIndex.get(he.getVertex());
				he = he.getNextInFace();
				cornerIndices[3] = keysToIndex.get(he.getVertex());
				he = he.getNextInFace();
				cornerIndices[2] = keysToIndex.get(he.getVertex());
				for (int i = 0; i < numberOfLayers; i++) {
					getCells3D()[i][j] = new Cell();
					getCells3D()[i][j].setCornerIndices(cornerIndices);
					getCells3D()[i][j].layer = i;
				}
				j++;
			}
			// Boundary caps
			int nobe = 0;
			Iterator<HE_Halfedge> heitr = substrate.heItr();
			while (heitr.hasNext()) {
				he = heitr.next();
				if (he.isInnerBoundary()) {
					nobe++;
				}
			}
			cells2D = new Cell[numberOfLayers][nobe];
			heitr = substrate.heItr();
			j = 0;
			while (heitr.hasNext()) {
				he = heitr.next();
				if (he.isInnerBoundary()) {
					cornerIndices = new int[2];
					cornerIndices[0] = keysToIndex.get(he.getVertex());
					cornerIndices[1] = keysToIndex.get(he.getEndVertex());
					for (int i = 0; i < numberOfLayers; i++) {
						cells2D[i][j] = new Cell();
						cells2D[i][j].setCornerIndices(cornerIndices);
						cells2D[i][j].layer = i;
					}
					j++;
				}
			}
		}

		HE_Mesh getSkin(final double isolevel) {
			final HE_Mesh patch = new HE_Mesh();
			edges = new HE_ObjectMap<>();
			vertices = new WB_IndexedObjectMap<>();
			polygonise(isolevel, patch);
			patch.removeUnconnectedElements();
			patch.clean();
			return patch;
		}

		int getNumberOfLayers() {
			return numberOfLayers;
		}

		private void polygonise(final double isolevel, final HE_Mesh mesh) {
			for (int i = 0; i < numberOfLayers; i++) {
				for (int j = 0; j < getCells3D()[0].length; j++) {
					getPolygons3D(getCells3D()[i][j], isolevel, mesh);
				}
				for (int j = 0; j < cells2D[0].length; j++) {
					getPolygons2D(cells2D[i][j], isolevel, mesh);
				}
			}
		}

		private void getPolygons2D(final Cell cell, final double isolevel, final HE_Mesh mesh) {
			final int squareindex = classifyCell2D(cell, isolevel);
			final int[] indices = entries2D[squareindex];
			final int numfaces = indices[0];
			int currentindex = 1;
			for (int t = 0; t < numfaces; t++) {
				final int n = indices[currentindex++];
				final HE_Face f = new HE_Face();
				final HE_Vertex v1 = getIsoVertex2D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Vertex v2 = getIsoVertex2D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Vertex v3 = getIsoVertex2D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Vertex v4 = n == 4 ? getIsoVertex2D(indices[currentindex++], cell, isolevel, mesh) : null;
				if (n == 3) {
					final HE_Halfedge he1 = new HE_Halfedge();
					final HE_Halfedge he2 = new HE_Halfedge();
					final HE_Halfedge he3 = new HE_Halfedge();
					mesh.setNext(he2, he1);
					mesh.setNext(he1, he3);
					mesh.setNext(he3, he2);
					mesh.setFace(he1, f);
					mesh.setFace(he2, f);
					mesh.setFace(he3, f);
					mesh.setVertex(he1, v1);
					mesh.setHalfedge(v1, he1);
					mesh.setVertex(he2, v2);
					mesh.setHalfedge(v2, he2);
					mesh.setVertex(he3, v3);
					mesh.setHalfedge(v3, he3);
					mesh.setHalfedge(f, he1);
					mesh.setInternalLabel(squareindex);
					if (HE_MeshOp.getFaceArea(f) > 0) {
						mesh.add(f);
						mesh.add(he1);
						mesh.add(he2);
						mesh.add(he3);
					}
				} else if (n == 4) {
					final HE_Halfedge he1 = new HE_Halfedge();
					final HE_Halfedge he2 = new HE_Halfedge();
					final HE_Halfedge he3 = new HE_Halfedge();
					final HE_Halfedge he4 = new HE_Halfedge();
					mesh.setNext(he1, he2);
					mesh.setNext(he2, he3);
					mesh.setNext(he3, he4);
					mesh.setNext(he4, he1);
					mesh.setFace(he1, f);
					mesh.setFace(he2, f);
					mesh.setFace(he3, f);
					mesh.setFace(he4, f);
					mesh.setVertex(he1, v1);
					mesh.setHalfedge(v1, he1);
					mesh.setVertex(he2, v2);
					mesh.setHalfedge(v2, he2);
					mesh.setVertex(he3, v3);
					mesh.setHalfedge(v3, he3);
					mesh.setVertex(he4, v4);
					mesh.setHalfedge(v4, he4);
					mesh.setHalfedge(f, he1);
					f.setInternalLabel(squareindex);
					if (HE_MeshOp.getFaceArea(f) > 0) {
						mesh.add(f);
						mesh.add(he1);
						mesh.add(he2);
						mesh.add(he3);
						mesh.add(he4);
					}
				}
			}
		}

		private void getPolygons3D(final Cell cell, final double isolevel, final HE_Mesh mesh) {
			final int cubeindex = classifyCell3D(cell, isolevel);
			final int[] indices = entries3D[cubeindex];
			final int numtris = indices[0];
			int currentindex = 1;
			for (int t = 0; t < numtris; t++) {
				final HE_Face f = new HE_Face();
				final HE_Vertex v1 = getIsoVertex3D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Vertex v2 = getIsoVertex3D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Vertex v3 = getIsoVertex3D(indices[currentindex++], cell, isolevel, mesh);
				final HE_Halfedge he1 = new HE_Halfedge();
				final HE_Halfedge he2 = new HE_Halfedge();
				final HE_Halfedge he3 = new HE_Halfedge();
				mesh.setNext(he3, he2);
				mesh.setNext(he2, he1);
				mesh.setNext(he1, he3);
				mesh.setFace(he1, f);
				mesh.setFace(he2, f);
				mesh.setFace(he3, f);
				mesh.setVertex(he1, v1);
				mesh.setHalfedge(v1, he1);
				mesh.setVertex(he2, v2);
				mesh.setHalfedge(v2, he2);
				mesh.setVertex(he3, v3);
				mesh.setHalfedge(v3, he3);
				mesh.setHalfedge(f, he1);
				if (HE_MeshOp.getFaceArea(f) > 0) {
					mesh.add(f);
					mesh.add(he1);
					mesh.add(he2);
					mesh.add(he3);
				}
			}
		}

		private int classifyCell2D(final Cell cell, final double isolevel) {
			final int layer = cell.layer;
			int squareindex = 0;
			int offset = 1;
			if (values[layer][cell.getCornerIndices()[0]] > isolevel) {
				squareindex += 2 * offset;
			} else if (values[layer][cell.getCornerIndices()[0]] == isolevel) {
				squareindex += offset;
			}
			offset *= 3;
			if (values[layer][cell.getCornerIndices()[1]] > isolevel) {
				squareindex += 2 * offset;
			} else if (values[layer][cell.getCornerIndices()[1]] == isolevel) {
				squareindex += offset;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[0]] > isolevel) {
				squareindex += 2 * offset;
			} else if (values[layer + 1][cell.getCornerIndices()[0]] == isolevel) {
				squareindex += offset;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[1]] > isolevel) {
				squareindex += 2 * offset;
			} else if (values[layer + 1][cell.getCornerIndices()[1]] == isolevel) {
				squareindex += offset;
			}
			return squareindex;
		}

		private int classifyCell3D(final Cell cell, final double isolevel) {
			final int layer = cell.layer;
			int cubeindex = 0;
			int offset = 1;
			digits = new int[8];
			if (values[layer][cell.getCornerIndices()[0]] > isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (values[layer][cell.getCornerIndices()[0]] == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (values[layer][cell.getCornerIndices()[1]] > isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (values[layer][cell.getCornerIndices()[1]] == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (values[layer][cell.getCornerIndices()[2]] > isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (values[layer][cell.getCornerIndices()[2]] == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
			offset *= 3;
			if (values[layer][cell.getCornerIndices()[3]] > isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (values[layer][cell.getCornerIndices()[3]] == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[0]] > isolevel) {
				cubeindex += 2 * offset;
				digits[0] = POSITIVE;
			} else if (values[layer + 1][cell.getCornerIndices()[0]] == isolevel) {
				cubeindex += offset;
				digits[0] = EQUAL;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[1]] > isolevel) {
				cubeindex += 2 * offset;
				digits[1] = POSITIVE;
			} else if (values[layer + 1][cell.getCornerIndices()[1]] == isolevel) {
				cubeindex += offset;
				digits[1] = EQUAL;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[2]] > isolevel) {
				cubeindex += 2 * offset;
				digits[3] = POSITIVE;
			} else if (values[layer + 1][cell.getCornerIndices()[2]] == isolevel) {
				cubeindex += offset;
				digits[3] = EQUAL;
			}
			offset *= 3;
			if (values[layer + 1][cell.getCornerIndices()[3]] > isolevel) {
				cubeindex += 2 * offset;
				digits[2] = POSITIVE;
			} else if (values[layer + 1][cell.getCornerIndices()[3]] == isolevel) {
				cubeindex += offset;
				digits[2] = EQUAL;
			}
			return cubeindex;
		}

		private HE_Vertex getIsoVertex2D(final int isopointindex, final Cell cell, final double isolevel,
				final HE_Mesh mesh) {
			final int layer = cell.layer;
			final int[] indices = cell.getCornerIndices();
			if (isovertices2D[isopointindex][0] == ONVERTEX) {
				switch (isovertices2D[isopointindex][1]) {
				case 0:
					return vertex(layer, indices[0], mesh);
				case 1:
					return vertex(layer, indices[1], mesh);
				case 2:
					return vertex(layer + 1, indices[0], mesh);
				case 3:
					return vertex(layer + 1, indices[1], mesh);
				default:
					return null;
				}
			} else if (isovertices2D[isopointindex][0] == ONEDGE) {
				switch (isovertices2D[isopointindex][1]) {
				case 0:
					return edge(isolevel, indices[0], layer, indices[1], layer, mesh);
				case 1:
					return edge(isolevel, indices[0], layer, indices[0], layer + 1, mesh);
				case 2:
					return edge(isolevel, indices[1], layer, indices[1], layer + 1, mesh);
				case 3:
					return edge(isolevel, indices[0], layer + 1, indices[1], layer + 1, mesh);
				default:
					return null;
				}
			}
			return null;
		}

		private HE_Vertex getIsoVertex3D(final int isopointindex, final Cell cell, final double isolevel,
				final HE_Mesh mesh) {
			final int layer = cell.layer;
			final int[] indices = cell.getCornerIndices();
			if (isovertices3D[isopointindex][0] == ONVERTEX) {
				switch (isovertices3D[isopointindex][1]) {
				case 0:
					return vertex(layer, indices[0], mesh);
				case 1:
					return vertex(layer, indices[1], mesh);
				case 2:
					return vertex(layer, indices[2], mesh);
				case 3:
					return vertex(layer, indices[3], mesh);
				case 4:
					return vertex(layer + 1, indices[0], mesh);
				case 5:
					return vertex(layer + 1, indices[1], mesh);
				case 6:
					return vertex(layer + 1, indices[2], mesh);
				case 7:
					return vertex(layer + 1, indices[3], mesh);
				default:
					return null;
				}
			} else if (isovertices3D[isopointindex][0] == ONEDGE) {
				switch (isovertices3D[isopointindex][1]) {
				case 0:
					return edge(isolevel, indices[0], layer, indices[1], layer, mesh);
				case 1:
					return edge(isolevel, indices[0], layer, indices[2], layer, mesh);
				case 2:
					return edge(isolevel, indices[1], layer, indices[3], layer, mesh);
				case 3:
					return edge(isolevel, indices[2], layer, indices[3], layer, mesh);
				case 4:
					return edge(isolevel, indices[0], layer, indices[0], layer + 1, mesh);
				case 5:
					return edge(isolevel, indices[1], layer, indices[1], layer + 1, mesh);
				case 6:
					return edge(isolevel, indices[2], layer, indices[2], layer + 1, mesh);
				case 7:
					return edge(isolevel, indices[3], layer, indices[3], layer + 1, mesh);
				case 8:
					return edge(isolevel, indices[0], layer + 1, indices[1], layer + 1, mesh);
				case 9:
					return edge(isolevel, indices[0], layer + 1, indices[2], layer + 1, mesh);
				case 10:
					return edge(isolevel, indices[1], layer + 1, indices[3], layer + 1, mesh);
				case 11:
					return edge(isolevel, indices[2], layer + 1, indices[3], layer + 1, mesh);
				default:
					return null;
				}
			}
			return null;
		}

		private HE_Vertex edge(final double isolevel, final int i, final int layeri, final int j, final int layerj,
				final HE_Mesh mesh) {
			final long index = edgeindex(layeri, i, layerj, j);
			HE_Vertex edge = edges.get(index);
			if (edge != null) {
				return edge;
			}
			final WB_Coord pi = gridpositions[layeri][i];
			final WB_Coord pj = gridpositions[layerj][j];
			final double vali = values[layeri][i];
			final double valj = values[layerj][j];
			edge = new HE_Vertex(interp(isolevel, pi, pj, vali, valj));
			mesh.add(edge);
			edges.put(index, edge);
			return edge;
		}

		private HE_Vertex vertex(final int layer, final int i, final HE_Mesh mesh) {
			HE_Vertex vertex = vertices.get(vertexindex(layer, i));
			if (vertex != null) {
				return vertex;
			}
			vertex = new HE_Vertex(gridpositions[layer][i]);
			mesh.add(vertex);
			vertices.put(vertexindex(layer, i), vertex);
			return vertex;
		}

		private WB_Point interp(final double isolevel, final WB_Coord p1, final WB_Coord p2, final double valp1,
				final double valp2) {
			double mu;
			if (WB_Epsilon.isEqualAbs(isolevel, valp1)) {
				return new WB_Point(p1);
			}
			if (WB_Epsilon.isEqualAbs(isolevel, valp2)) {
				return new WB_Point(p2);
			}
			if (WB_Epsilon.isEqualAbs(valp1, valp2)) {
				return new WB_Point(p1);
			}
			mu = (isolevel - valp1) / (valp2 - valp1);
			return new WB_Point(p1.xd() + mu * (p2.xd() - p1.xd()), p1.yd() + mu * (p2.yd() - p1.yd()),
					p1.zd() + mu * (p2.zd() - p1.zd()));
		}

		private long edgeindex(final int layeri, final int i, final int layerj, final int j) {
			return (long) (layeri * numberOfVertices + i) * totalNumberOfVertices + layerj * numberOfVertices + j;
		}

		private int vertexindex(final int layeri, final int i) {
			if (layeri < 0 || layeri > numberOfLayers) {
				return -1;
			}
			return layeri * numberOfVertices + i;
		}

		Cell[][] getCells3D() {
			return cells3D;
		}

		private void setCells3D(final Cell[][] cells3d) {
			cells3D = cells3d;
		}

		WB_Coord[][] getGridpositions() {
			return gridpositions;
		}
	}

	public static class Cell {
		private int layer;

		int getLayer() {
			return layer;
		}

		void setLayer(final int layer) {
			this.layer = layer;
		}

		private int[] cornerIndices;

		public int[] getCornerIndices() {
			return cornerIndices;
		}

		void setCornerIndices(final int[] cornerIndices) {
			this.cornerIndices = cornerIndices;
		}
	}
}
