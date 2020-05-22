package wblut.hemesh;

import wblut.geom.WB_Transform2D;

/**
 *
 */
public class HEC_Transform2D extends HEC_Creator {
	/**  */
	private WB_Transform2D T;
	/**  */
	private HE_Mesh source;

	/**
	 *
	 */
	public HEC_Transform2D() {
		super();
		T = null;
		source = null;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param source
	 * @param T
	 */
	public HEC_Transform2D(final HE_Mesh source, final WB_Transform2D T) {
		super();
		this.T = T;
		this.source = source;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param T
	 * @return
	 */
	public HEC_Transform2D setTransform(final WB_Transform2D T) {
		this.T = T;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEC_Transform2D setSource(final HE_Mesh mesh) {
		this.source = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public HE_Mesh createBase() {
		HE_Mesh result = new HE_Mesh();
		if (source == null) {
			return result;
		}
		result = source.copy();
		if (T == null) {
			return result;
		}
		final HE_VertexIterator vItr = result.vItr();
		while (vItr.hasNext()) {
			T.applyAsPoint2DSelf(vItr.next());
		}
		return result;
	}
}
