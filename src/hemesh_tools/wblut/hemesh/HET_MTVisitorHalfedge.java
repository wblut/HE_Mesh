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
public class HET_MTVisitorHalfedge<E extends Object> {
	HET_InfoHalfedge<E> halfedgeInfo;

	public HET_MTVisitorHalfedge(final HET_InfoHalfedge<E> halfedgeInfo) {
		this.halfedgeInfo = halfedgeInfo;
	}

	public List<E> getHalfedgeInfo(final HE_HalfedgeStructure mesh) {
		return visit(mesh.getHalfedges());
	}

	private List<E> visit(final List<HE_Halfedge> halfedges) {
		final List<E> result = new WB_List<>();
		try {
			final int threadCount = Runtime.getRuntime().availableProcessors();
			final int dedges = halfedges.size() / threadCount;
			final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			final List<Future<List<E>>> list = new ArrayList<>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<List<E>> runner = new HET_HalfedgeVisitor(dedges * i, dedges * (i + 1) - 1, i,
						halfedges);
				list.add(executor.submit(runner));
			}
			final Callable<List<E>> runner = new HET_HalfedgeVisitor(dedges * i, halfedges.size() - 1, i, halfedges);
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

	class HET_HalfedgeVisitor implements Callable<List<E>> {
		int start;
		int end;
		int id;
		List<HE_Halfedge> halfedges;

		public HET_HalfedgeVisitor(final int s, final int e, final int id, final List<HE_Halfedge> halfedges) {
			start = s;
			end = e;
			this.id = id;
			this.halfedges = halfedges;
		}

		@Override
		public List<E> call() {
			final ArrayList<E> result = new ArrayList<>();
			final ListIterator<HE_Halfedge> itr = halfedges.listIterator(start);
			for (int i = start; i <= end; i++) {
				result.add(halfedgeInfo.retrieve(itr.next()));
			}
			return result;
		}
	}
}
