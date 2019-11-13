/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import java.util.Comparator;

/**
 * @author FVH
 *
 */
public interface WB_TriangleSort extends Comparator<WB_Triangle> {
	public static class WB_TriangleSortCenterXYZ implements WB_TriangleSort {

		@Override
		public int compare(final WB_Triangle f0, final WB_Triangle f1) {
			return f0.getCenter().compareTo(f1.getCenter());
		}

	}

	public static class WB_TriangleSortCenter1D implements WB_TriangleSort {
		int dim = 0;

		public WB_TriangleSortCenter1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		@Override
		public int compare(final WB_Triangle f0, final WB_Triangle f1) {
			double r = f0.getCenter().getd(dim) - f1.getCenter().getd(dim);
			if (r > 0) {
				return +1;
			}
			if (r < 0) {
				return -1;
			}
			return 0;
		}

	}

	public static class WB_AABBSortCenter1D implements WB_TriangleSort {
		int dim = 0;

		public WB_AABBSortCenter1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		@Override
		public int compare(final WB_Triangle f0, final WB_Triangle f1) {
			double r = f0.getAABB().getCenter(dim) - f1.getAABB().getCenter(dim);
			if (r > 0) {
				return +1;
			}
			if (r < 0) {
				return -1;
			}
			return 0;
		}

	}

	public static class WB_AABBSortMax1D implements WB_TriangleSort {
		int dim = 0;

		public WB_AABBSortMax1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		@Override
		public int compare(final WB_Triangle f0, final WB_Triangle f1) {
			double r = f0.getAABB().getMax(dim) - f1.getAABB().getMax(dim);
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
