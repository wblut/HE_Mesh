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
public class WB_BSPNode {
	
	/** 3D partition. */
	protected WB_Plane				partition;
	
	/** Polygons on the partition */
	protected FastList<WB_Polygon>	polygons;
	
	/** Positive child node */
	protected WB_BSPNode			pos	= null;
	
	/** Negative child node */
	protected WB_BSPNode			neg	= null;

	/**
	 * Instantiates a new node.
	 */
	public WB_BSPNode() {
		polygons = new FastList<WB_Polygon>();
	}

}