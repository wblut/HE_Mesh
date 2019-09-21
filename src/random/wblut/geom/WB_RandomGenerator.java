/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for random vector/point generators
 *
 * @author frederikvanhoutte
 *
 */
public abstract class WB_RandomGenerator implements WB_RandomPoint, WB_RandomVector {
	/**
	 * Set the seed for the RNG.
	 *
	 * @param seed
	 * @return this
	 */
	abstract public WB_RandomGenerator setSeed(final long seed);



	/**
	 * Reset the RNG.
	 */
	abstract public void reset();

	/**
	 * Set point offset.
	 *
	 * @param offset
	 * @return
	 */
	abstract public WB_RandomGenerator setOffset(WB_Coord offset);

	/**
	 * Set point offset.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	abstract public WB_RandomGenerator setOffset(double x, double y);

	/**
	 * Set point offset.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	abstract public WB_RandomGenerator setOffset(double x, double y, double z);
	
	
	public WB_PointCollection getPoints(int N) {
		List<WB_Point> points=new ArrayList<WB_Point>();
		for(int i=0;i<N;i++) {
			points.add(nextPoint());
		}
		return WB_PointCollection.getCollection(points);
	}
	
	public WB_VectorCollection getVectors(int N) {
		List<WB_Vector> points=new ArrayList<WB_Vector>();
		for(int i=0;i<N;i++) {
			points.add(nextVector());
		}
		return WB_VectorCollection.getCollection(points);
	}
}
