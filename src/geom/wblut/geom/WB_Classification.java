/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

public enum WB_Classification {

	UNKNOWN,

	CONVEX,

	CONCAVE,

	SADDLE,

	FLAT,

	FLATCONVEX,

	FLATCONCAVE,

	FRONT,

	BACK,

	ON,

	CROSSING,

	SPANNING,

	FRONTEXCL,

	BACKEXCL,

	CROSSINGEXCL,

	INSIDE,

	OUTSIDE,

	OBLIQUE,

	INLINE,

	PARALLEL,

	ANTIPARALLEL,

	PERPENDICULAR,

	COLLINEAR,

	COPLANAR,

	SAME,

	DIFF,

	SAMEEXCL,

	DIFFEXCL,

	CLOCKWISE,

	COUNTERCLOCKWISE,

	CONTAINING,

	TANGENT
}
