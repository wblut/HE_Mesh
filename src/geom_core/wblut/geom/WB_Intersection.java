package wblut.geom;

import lombok.Data;

/**
 *
 */
/**
 *
 */
@Data
public class WB_Intersection {
	/**  */
	public double t1 = Float.NEGATIVE_INFINITY;
	/**  */
	public double t2 = Float.NEGATIVE_INFINITY;
	/**  */
	public boolean intersection = false;
	/**  */
	public double sqDist = Float.POSITIVE_INFINITY;
	/**  */
	public Object object;
	/**  */
	public int dimension = -1;
}
