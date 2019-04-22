/**
 * 
 */
package wblut.geom;

import org.eclipse.collections.impl.list.mutable.FastList;

/**
 * WB_BSPNode2D.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_BSPNode2D {
	
	/** 2D partition. */
	protected WB_Line							partition;
	
	/** Segments on the partition */
	protected FastList<WB_Segment>	segments;
	
	/** Positive child node */
	protected WB_BSPNode2D						pos	= null;
	
	/** Negative child node */
	protected WB_BSPNode2D						neg	= null;

	/**
	 * Instantiates a new 2D node.
	 */
	public WB_BSPNode2D() {
		segments = new FastList<WB_Segment>();
	}

}