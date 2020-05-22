package wblut.geom;

import java.util.Collection;

import wblut.hemesh.HE_Mesh;

/**
 *
 */
public class WB_RandomInCollection extends WB_RandomFactory {
	/**  */
	private WB_CoordCollection collection;
	/**  */
	private int size;

	/**
	 *
	 */
	public WB_RandomInCollection() {
		super();
		collection = null;
		size = 0;
	}

	/**
	 *
	 *
	 * @param seed
	 */
	public WB_RandomInCollection(final long seed) {
		super(seed);
		collection = null;
		size = 0;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final WB_CoordCollection coords) {
		collection = coords;
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final WB_Coord[] coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final Collection<? extends WB_Coord> coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final float[][] coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final double[][] coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final int[][] coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @param coords
	 * @return
	 */
	public WB_RandomInCollection setCollection(final HE_Mesh coords) {
		collection = WB_CoordCollection.getCollection(coords);
		size = collection.size();
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Point nextPointImp() {
		final int i = (int) (size * randomGen.nextDouble());
		return new WB_Point(collection.get(i));
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public WB_Vector nextVectorImp() {
		final int i = (int) (size * randomGen.nextDouble());
		return new WB_Vector(collection.get(i));
	}
}