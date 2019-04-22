/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.Comparator;

/**
 * @author FVH
 *
 */
public interface HE_VertexSort extends Comparator<HE_Vertex> {

	public static class HE_VertexSortXYZ implements HE_VertexSort {

		@Override
		public int compare(final HE_Vertex v0, final HE_Vertex v1) {
			return v0.compareTo(v1);
		}

	}

	public static class HE_VertexSortYXZ implements HE_VertexSort {

		@Override
		public int compare(final HE_Vertex v0, final HE_Vertex v1) {
			return v0.compareToY1st(v1);
		}

	}

	public static class HE_VertexSortCenter1D implements HE_VertexSort {
		int dim = 0;

		HE_VertexSortCenter1D(final int dim) {
			this.dim = Math.min(Math.max(dim, 0), 2);
		}

		@Override
		public int compare(final HE_Vertex v0, final HE_Vertex v1) {
			double r = v0.getd(dim) - v1.getd(dim);
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
