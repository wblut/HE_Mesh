/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * @author FVH
 *
 */
public class HET_MTVisitorFace<E extends Object> {
	HET_InfoFace<E> faceInfo;

	/**
	 *
	 *
	 * @param faceInfo
	 */
	public HET_MTVisitorFace(final HET_InfoFace<E> faceInfo) {
		this.faceInfo = faceInfo;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public List<E> getFaceInfo(final HE_HalfedgeStructure mesh) {
		return visit(mesh.getFaces());
	}

	/**
	 *
	 *
	 * @param faces
	 * @return
	 */
	private List<E> visit(final List<HE_Face> faces) {

		List<E> result = new FastList<E>();
		try {
			int threadCount = Runtime.getRuntime().availableProcessors();
			int dfaces = faces.size() / threadCount;
			final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			final List<Future<List<E>>> list = new ArrayList<Future<List<E>>>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<List<E>> runner = new HET_FaceVisitor(dfaces * i, dfaces * (i + 1) - 1, i, faces);
				list.add(executor.submit(runner));
			}
			final Callable<List<E>> runner = new HET_FaceVisitor(dfaces * i, faces.size() - 1, i, faces);
			list.add(executor.submit(runner));

			for (Future<List<E>> future : list) {
				result.addAll(future.get());
			}

			executor.shutdown();

		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		} catch (final ExecutionException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	class HET_FaceVisitor implements Callable<List<E>> {
		int start;
		int end;
		int id;
		List<HE_Face> faces;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param faces
		 */
		public HET_FaceVisitor(final int s, final int e, final int id, final List<HE_Face> faces) {
			start = s;
			end = e;
			this.id = id;
			this.faces = faces;

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public List<E> call() {
			ArrayList<E> result = new ArrayList<E>();
			ListIterator<HE_Face> itr = faces.listIterator(start);
			for (int i = start; i <= end; i++) {
				result.add(faceInfo.retrieve(itr.next()));
			}
			return result;
		}

	}

}
