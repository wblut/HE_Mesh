/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_Epsilon;

/**
 * @author FVH
 *
 */
public class WB_GeometryOp4D extends WB_GeometryOp {

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param uw
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param vw
	 * @return
	 */
	public static double dot4D(final double ux, final double uy, final double uz, final double uw, final double vx,
			final double vy, final double vz, final double vw) {
		return ux * vx + uy * vy + uz * vz + uw * vw;
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param pw
	 * @param qx
	 * @param qy
	 * @param qz
	 * @param qw
	 * @return
	 */
	public static double getDistance4D(final double px, final double py, final double pz, final double pw,
			final double qx, final double qy, final double qz, final double qw) {
		return Math.sqrt((qx - px) * (qx - px) + (qy - py) * (qy - py) + (qz - pz) * (qz - pz) + (qw - pw) * (qw - pw));
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param uw
	 * @return
	 */
	public static double getLength4D(final double ux, final double uy, final double uz, final double uw) {
		return Math.sqrt(ux * ux + uy * uy + uz * uz + uw * uw);
	}

	/**
	 *
	 *
	 * @param px
	 * @param py
	 * @param pz
	 * @param pw
	 * @param qx
	 * @param qy
	 * @param qz
	 * @param qw
	 * @return
	 */
	public static double getSqDistance4D(final double px, final double py, final double pz, final double pw,
			final double qx, final double qy, final double qz, final double qw) {
		return (qx - px) * (qx - px) + (qy - py) * (qy - py) + (qz - pz) * (qz - pz) + (qw - pw) * (qw - pw);
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param uw
	 * @return
	 */
	public static double getSqLength4D(final double ux, final double uy, final double uz, final double uw) {
		return ux * ux + uy * uy + uz * uz + uw * uw;
	}

	/**
	 *
	 *
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param uw
	 * @return
	 */
	public static boolean isZero4D(final double ux, final double uy, final double uz, final double uw) {
		return getSqLength4D(ux, uy, uz, uw) < WB_Epsilon.SQEPSILON;
	}

}
