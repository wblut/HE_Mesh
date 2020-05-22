package wblut.hemesh;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import wblut.geom.WB_List;

/**
 *
 *
 * @param <E>
 */
public class HET_MTVisitorFace<E extends Object> {
	/**  */
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
	private List<E> visit(final HE_FaceList faces) {
		final List<E> result = new WB_List<>();
		try {
			final int threadCount = Runtime.getRuntime().availableProcessors();
			final int dfaces = faces.size() / threadCount;
			final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			final List<Future<List<E>>> list = new ArrayList<>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<List<E>> runner = new HET_FaceVisitor(dfaces * i, dfaces * (i + 1) - 1, i, faces);
				list.add(executor.submit(runner));
			}
			final Callable<List<E>> runner = new HET_FaceVisitor(dfaces * i, faces.size() - 1, i, faces);
			list.add(executor.submit(runner));
			for (final Future<List<E>> future : list) {
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

	/**
	 *
	 */
	class HET_FaceVisitor implements Callable<List<E>> {
		/**  */
		int start;
		/**  */
		int end;
		/**  */
		int id;
		/**  */
		HE_FaceList faces;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param faces
		 */
		public HET_FaceVisitor(final int s, final int e, final int id, final HE_FaceList faces) {
			start = s;
			end = e;
			this.id = id;
			this.faces = faces;
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public List<E> call() {
			final ArrayList<E> result = new ArrayList<>();
			final ListIterator<HE_Face> itr = faces.listIterator(start);
			for (int i = start; i <= end; i++) {
				result.add(faceInfo.retrieve(itr.next()));
			}
			return result;
		}
	}
}
