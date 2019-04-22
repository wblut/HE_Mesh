/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.geom;

/**
 * Interface for implementing mutable transformation operations on 4D
 * coordinates.
 *
 * All of the operators defined in the interface change the calling object. All
 * operators use the label "Self", such as "rotateXYSelf" to indicate this.
 *
 * @author Frederik Vanhoutte
 *
 */
public interface WB_MutableCoordTransform4D {
	public WB_Vector4D rotateXWSelf(double angle);

	public WB_Vector4D rotateXYSelf(double angle);

	public WB_Vector4D rotateXZSelf(double angle);

	public WB_Vector4D rotateYWSelf(double angle);

	public WB_Vector4D rotateYZSelf(double angle);

	public WB_Vector4D rotateZWSelf(double angle);
}
