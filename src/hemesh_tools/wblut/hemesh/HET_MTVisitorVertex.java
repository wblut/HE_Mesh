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
public class HET_MTVisitorVertex<E extends Object> {
	HET_InfoVertex<E> vertexInfo;

	/**
	 *
	 *
	 * @param vertexInfo
	 */
	public HET_MTVisitorVertex(final HET_InfoVertex<E> vertexInfo) {
		this.vertexInfo = vertexInfo;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public List<E> getVertexInfo(final HE_HalfedgeStructure mesh) {
		return visit(mesh.getVertices());
	}

	/**
	 *
	 *
	 * @param vertices
	 * @return
	 */
	private List<E> visit(final List<HE_Vertex> vertices) {

		List<E> result = new FastList<E>();
		try {
			int threadCount = Runtime.getRuntime().availableProcessors();
			int dvertices = vertices.size() / threadCount;
			final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
			final List<Future<List<E>>> list = new ArrayList<Future<List<E>>>();
			int i = 0;
			for (i = 0; i < threadCount - 1; i++) {
				final Callable<List<E>> runner = new HET_VertexVisitor(dvertices * i, dvertices * (i + 1) - 1, i,
						vertices);
				list.add(executor.submit(runner));
			}
			final Callable<List<E>> runner = new HET_VertexVisitor(dvertices * i, vertices.size() - 1, i, vertices);
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

	class HET_VertexVisitor implements Callable<List<E>> {
		int start;
		int end;
		int id;
		List<HE_Vertex> vertices;

		/**
		 *
		 *
		 * @param s
		 * @param e
		 * @param id
		 * @param vertices
		 */
		public HET_VertexVisitor(final int s, final int e, final int id, final List<HE_Vertex> vertices) {
			start = s;
			end = e;
			this.id = id;
			this.vertices = vertices;

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public List<E> call() {
			ArrayList<E> result = new ArrayList<E>();
			ListIterator<HE_Vertex> itr = vertices.listIterator(start);
			for (int i = start; i <= end; i++) {
				result.add(vertexInfo.retrieve(itr.next()));
			}
			return result;
		}

	}

}
