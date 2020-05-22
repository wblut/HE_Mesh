package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 */
public abstract class HE_HalfedgeIterator implements Iterator<HE_Halfedge> {
	/**  */
	Iterator<HE_Halfedge> _itr;

	/**
	 *
	 */
	private HE_HalfedgeIterator() {
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HE_HalfedgeIterator(final HE_HalfedgeStructure mesh) {
		_itr = mesh.heItr();
	}

	/**
	 *
	 *
	 * @param halfedges
	 * @return
	 */
	public static HE_HalfedgeIterator getIterator(final Collection<HE_Halfedge> halfedges) {
		return new HE_HalfedgeCollectionIterator(halfedges);
	}

	/**
	 *
	 *
	 * @param edges
	 * @param halfedges
	 * @param unpairedHalfedges
	 * @return
	 */
	public static HE_HalfedgeIterator getIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges,
			final HE_RAS<HE_Halfedge> unpairedHalfedges) {
		return new HE_HalfedgeMeshIterator(edges, halfedges, unpairedHalfedges);
	}

	/**
	 *
	 *
	 * @param edges
	 * @param halfedges
	 * @return
	 */
	public static HE_HalfedgeIterator getIterator(final HE_RAS<HE_Halfedge> edges,
			final HE_RAS<HE_Halfedge> halfedges) {
		return new HE_HalfedgeSelectionIterator(edges, halfedges);
	}

	/**
	 *
	 */
	private static class HE_HalfedgeMeshIterator extends HE_HalfedgeIterator {
		/**  */
		Iterator<HE_Halfedge> _itre, _itrhe, _itruhe;

		/**
		 *
		 *
		 * @param edges
		 * @param halfedges
		 * @param unpairedHalfedges
		 */
		HE_HalfedgeMeshIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges,
				final HE_RAS<HE_Halfedge> unpairedHalfedges) {
			_itre = edges.iterator();
			_itrhe = halfedges.iterator();
			_itruhe = unpairedHalfedges.iterator();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public boolean hasNext() {
			return _itre.hasNext() || _itrhe.hasNext() || _itruhe.hasNext();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_Halfedge next() {
			return _itre.hasNext() ? _itre.next() : _itrhe.hasNext() ? _itrhe.next() : _itruhe.next();
		}

		/**
		 *
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 *
	 */
	private static class HE_HalfedgeCollectionIterator extends HE_HalfedgeIterator {
		/**  */
		Iterator<HE_Halfedge> _itrhe;

		/**
		 *
		 *
		 * @param halfedges
		 */
		HE_HalfedgeCollectionIterator(final Collection<HE_Halfedge> halfedges) {
			_itrhe = halfedges.iterator();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public boolean hasNext() {
			return _itrhe.hasNext();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_Halfedge next() {
			return _itrhe.next();
		}

		/**
		 *
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 *
	 */
	private static class HE_HalfedgeSelectionIterator extends HE_HalfedgeIterator {
		/**  */
		Iterator<HE_Halfedge> _itre, _itrhe;

		/**
		 *
		 *
		 * @param edges
		 * @param halfedges
		 */
		HE_HalfedgeSelectionIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges) {
			_itre = edges.iterator();
			_itrhe = halfedges.iterator();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public boolean hasNext() {
			return _itre.hasNext() || _itrhe.hasNext();
		}

		/**
		 *
		 *
		 * @return
		 */
		@Override
		public HE_Halfedge next() {
			return _itre.hasNext() ? _itre.next() : _itrhe.next();
		}

		/**
		 *
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
