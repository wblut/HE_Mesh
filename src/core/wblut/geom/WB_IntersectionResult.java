/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import lombok.Data;

@Data
public class WB_IntersectionResult {

	public double t1 = Float.NEGATIVE_INFINITY;

	public double t2 = Float.NEGATIVE_INFINITY;;

	public boolean intersection = false;

	public double sqDist = Float.POSITIVE_INFINITY;

	public Object object;

	public int dimension = -1;
}
