package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

public abstract class HE_HalfedgeIterator implements Iterator<HE_Halfedge> {
	Iterator<HE_Halfedge> _itr;

	private HE_HalfedgeIterator() {
	}

	public HE_HalfedgeIterator(final HE_HalfedgeStructure mesh) {
		_itr = mesh.heItr();
	}

	public static HE_HalfedgeIterator getIterator(final Collection<HE_Halfedge> halfedges) {
		return new HE_HalfedgeCollectionIterator(halfedges);
	}

	public static HE_HalfedgeIterator getIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges,
			final HE_RAS<HE_Halfedge> unpairedHalfedges) {
		return new HE_HalfedgeMeshIterator(edges, halfedges, unpairedHalfedges);
	}

	public static HE_HalfedgeIterator getIterator(final HE_RAS<HE_Halfedge> edges,
			final HE_RAS<HE_Halfedge> halfedges) {
		return new HE_HalfedgeSelectionIterator(edges, halfedges);
	}

	private static class HE_HalfedgeMeshIterator extends HE_HalfedgeIterator {
		Iterator<HE_Halfedge> _itre, _itrhe, _itruhe;

		HE_HalfedgeMeshIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges,
				final HE_RAS<HE_Halfedge> unpairedHalfedges) {
			_itre = edges.iterator();
			_itrhe = halfedges.iterator();
			_itruhe = unpairedHalfedges.iterator();
		}

		@Override
		public boolean hasNext() {
			return _itre.hasNext() || _itrhe.hasNext() || _itruhe.hasNext();
		}

		@Override
		public HE_Halfedge next() {
			return _itre.hasNext() ? _itre.next() : _itrhe.hasNext() ? _itrhe.next() : _itruhe.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class HE_HalfedgeCollectionIterator extends HE_HalfedgeIterator {
		Iterator<HE_Halfedge> _itrhe;

		HE_HalfedgeCollectionIterator(final Collection<HE_Halfedge> halfedges) {
			_itrhe = halfedges.iterator();
		}

		@Override
		public boolean hasNext() {
			return _itrhe.hasNext();
		}

		@Override
		public HE_Halfedge next() {
			return _itrhe.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class HE_HalfedgeSelectionIterator extends HE_HalfedgeIterator {
		Iterator<HE_Halfedge> _itre, _itrhe;

		HE_HalfedgeSelectionIterator(final HE_RAS<HE_Halfedge> edges, final HE_RAS<HE_Halfedge> halfedges) {
			_itre = edges.iterator();
			_itrhe = halfedges.iterator();
		}

		@Override
		public boolean hasNext() {
			return _itre.hasNext() || _itrhe.hasNext();
		}

		@Override
		public HE_Halfedge next() {
			return _itre.hasNext() ? _itre.next() : _itrhe.next();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
