/**
 * 
 */
package wblut.geom;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * WB_BSPNode.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_BSPNode3D {
	
	/** 3D partition. */
	protected WB_Plane				partition;
	
	/** Polygons on the partition */
	protected FastList<WB_Polygon>	polygons;
	
	/** Positive child node */
	protected WB_BSPNode3D			pos	= null;
	
	/** Negative child node */
	protected WB_BSPNode3D			neg	= null;

	/**
	 * Instantiates a new node.
	 */
	public WB_BSPNode3D() {
		polygons = new FastList<WB_Polygon>();
	}

}