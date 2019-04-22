/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;

/**
 *
 */
public class HEM_TriangulateMT extends HEM_Modifier {
	/**
	 *
	 */
	public HE_Selection triangles;

	/**
	 *
	 */
	public HEM_TriangulateMT() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		triangles = HE_Selection.getSelection(mesh);
		tracker.setStartStatus(this, "Starting HEM_Triangulate.");
		final int n = mesh.getNumberOfFaces();
		List<HE_Face> faces = mesh.getFaces();
		List<int[]> trisPerFace = triangulate(faces);
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Triangulating faces.", counter);
		Iterator<int[]> tpf = trisPerFace.iterator();
		HE_FaceIterator fItr = mesh.fItr();
		for (int i = 0; i < n; i++) {
			triangulateNoPairing(fItr.next(), mesh, tpf.next());
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		tracker.setStopStatus(this, "Exiting HEM_Triangulate.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		triangles = HE_Selection.getSelection(selection.getParent());
		tracker.setStartStatus(this, "Starting HEM_Triangulate.");
		final int n = selection.getNumberOfFaces();
		List<HE_Face> faces = selection.getFaces();
		List<int[]> trisPerFace = triangulate(faces);
		WB_ProgressCounter counter = new WB_ProgressCounter(n, 10);
		tracker.setCounterStatus(this, "Triangulating faces.", counter);
		Iterator<int[]> tpf = trisPerFace.iterator();
		HE_FaceIterator fItr = selection.fItr();
		for (int i = 0; i < n; i++) {
			triangulateNoPairing(fItr.next(), selection.getParent(),
					tpf.next());
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(selection.getParent());
		HE_MeshOp.capHalfedges(selection.getParent());
		selection.clearFaces();
		selection.add(triangles);
		tracker.setStopStatus(this, "Exiting HEM_Triangulate.");
		return selection.getParent();
	}

	/**
	 *
	 *
	 * @param face
	 * @param mesh
	 * @param tris
	 */
	private void triangulateNoPairing(final HE_Face face, final HE_Mesh mesh,
			final int[] tris) {
		if (tris.length == 3) {
			triangles.add(face);
		} else if (tris.length > 3) {
			final List<HE_Vertex> vertices = face.getFaceVertices();
			int n = vertices.size();
			final List<HE_Halfedge> halfedges = face.getFaceHalfedges();
			final List<HE_TextureCoordinate> UVWs = face.getFaceUVWs();
			/*
			 * HE_Halfedge he = face.getHalfedge(); do { mesh.clearPair(he);
			 * mesh.remove(he); he = he.getNextInFace(); } while (he !=
			 * face.getHalfedge());
			 */
			for (int i = 0; i < tris.length; i += 3) {
				final HE_Face f = new HE_Face();
				mesh.addDerivedElement(f, face);
				triangles.add(f);
				f.copyProperties(face);
				final HE_Halfedge he1 = (tris[i] + 1) % n == tris[i + 1]
						? halfedges.get(tris[i])
						: new HE_Halfedge();
				final HE_Halfedge he2 = (tris[i + 1] + 1) % n == tris[i + 2]
						? halfedges.get(tris[i + 1])
						: new HE_Halfedge();
				final HE_Halfedge he3 = (tris[i + 2] + 1) % n == tris[i]
						? halfedges.get(tris[i + 2])
						: new HE_Halfedge();
				he1.setUVW(UVWs.get(tris[i]));
				he2.setUVW(UVWs.get(tris[i + 1]));
				he3.setUVW(UVWs.get(tris[i + 2]));
				mesh.setVertex(he1, vertices.get(tris[i]));
				mesh.setVertex(he2, vertices.get(tris[i + 1]));
				mesh.setVertex(he3, vertices.get(tris[i + 2]));
				mesh.setHalfedge(he1.getVertex(), he1);
				mesh.setHalfedge(he2.getVertex(), he2);
				mesh.setHalfedge(he3.getVertex(), he3);
				mesh.setFace(he1, f);
				mesh.setFace(he2, f);
				mesh.setFace(he3, f);
				mesh.setNext(he1, he2);
				mesh.setNext(he2, he3);
				mesh.setNext(he3, he1);
				mesh.setHalfedge(f, he1);
				mesh.add(he1);
				mesh.add(he2);
				mesh.add(he3);
			}
			mesh.remove(face);
		}
	}

	/**
	 *
	 *
	 * @param faces
	 * @return
	 */
	private List<int[]> triangulate(final List<HE_Face> faces) {
		List<int[]> tris = new FastList<int[]>();
		try {
			int threadCount = Runtime.getRuntime().availableProcessors();
			int dfaces = faces.size() / threadCount;
			if (dfaces < 1024) {
				dfaces = 1024;
				threadCount = (int) Math.ceil(faces.size() / 1024.0);
			}
			final ExecutorService executor = Executors
					.newFixedThreadPool(threadCount);
			final List<Future<ArrayList<int[]>>> list = new ArrayList<Future<ArrayList<int[]>>>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
						dfaces * i, dfaces * (i + 1) - 1, i, faces);
				list.add(executor.submit(runner));
			}
			final Callable<ArrayList<int[]>> runner = new TriangulateRunner(
					dfaces * i, faces.size() - 1, i, faces);
			list.add(executor.submit(runner));
			for (Future<ArrayList<int[]>> future : list) {
				tris.addAll(future.get());
			}
			executor.shutdown();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
		return tris;
	}

	/**
	 *
	 */
	class TriangulateRunner implements Callable<ArrayList<int[]>> {
		int				start;
		int				end;
		int				id;
		int[]			triangles;
		List<HE_Face>	faces;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param faces
		 */
		TriangulateRunner(final int s, final int e, final int id,
				final List<HE_Face> faces) {
			start = s;
			end = e;
			this.id = id;
			this.faces = faces;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public ArrayList<int[]> call() {
			ArrayList<int[]> tris = new ArrayList<int[]>();
			ListIterator<HE_Face> itr = faces.listIterator(start);
			for (int i = start; i <= end; i++) {
				tris.add(itr.next().getTriangles());
			}
			return tris;
		}
	}
}
