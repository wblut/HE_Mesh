package wblut.geom;

import java.util.Comparator;

/**
 *
 */
public interface WB_TetrahedronSort extends Comparator<WB_Tetrahedron> {
	/**
	 *
	 */
	public static class WB_TetrahedronSortCenterXYZ implements WB_TetrahedronSort {
		/**
		 *
		 *
		 * @param f0
		 * @param f1
		 * @return
		 */
		@Override
		public int compare(final WB_Tetrahedron f0, final WB_Tetrahedron f1) {
			return f0.getCenter().compareTo(f1.getCenter());
		}
	}

	/**
	 *
	 */
	public static class WB_TetrahedronSortCenter1D implements WB_TetrahedronSort {
		/**  */
		int dim = 0;

		/**
		 *
		 *
		 * @param dim
		 */
		public WB_TetrahedronSortCenter1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		/**
		 *
		 *
		 * @param f0
		 * @param f1
		 * @return
		 */
		@Override
		public int compare(final WB_Tetrahedron f0, final WB_Tetrahedron f1) {
			final double r = f0.getCenter().getd(dim) - f1.getCenter().getd(dim);
			if (r > 0) {
				return +1;
			}
			if (r < 0) {
				return -1;
			}
			return 0;
		}
	}

	/**
	 *
	 */
	public static class WB_AABBSortCenter1D implements WB_TetrahedronSort {
		/**  */
		int dim = 0;

		/**
		 *
		 *
		 * @param dim
		 */
		public WB_AABBSortCenter1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		/**
		 *
		 *
		 * @param f0
		 * @param f1
		 * @return
		 */
		@Override
		public int compare(final WB_Tetrahedron f0, final WB_Tetrahedron f1) {
			final double r = f0.getAABB().getCenter(dim) - f1.getAABB().getCenter(dim);
			if (r > 0) {
				return +1;
			}
			if (r < 0) {
				return -1;
			}
			return 0;
		}
	}

	/**
	 *
	 */
	public static class WB_AABBSortMax1D implements WB_TetrahedronSort {
		/**  */
		int dim = 0;

		/**
		 *
		 *
		 * @param dim
		 */
		public WB_AABBSortMax1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		/**
		 *
		 *
		 * @param f0
		 * @param f1
		 * @return
		 */
		@Override
		public int compare(final WB_Tetrahedron f0, final WB_Tetrahedron f1) {
			final double r = f0.getAABB().getMax(dim) - f1.getAABB().getMax(dim);
			if (r > 0) {
				return +1;
			}
			if (r < 0) {
				return -1;
			}
			return 0;
		}
	}
}
