/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.geom;

import wblut.math.WB_DoubleDouble;

public class WB_Predicates {
	public WB_Predicates() {

	}

	private static double orientErrorBound = -1;
	private static double insphereErrorBound = -1;
	private static double orientErrorBound2D = -1;
	private static double incircleErrorBound2D = -1;

	private static double findMachEpsilon() {
		double epsilon, check, lastcheck;
		epsilon = 1.0;
		check = 1.0;
		do {
			lastcheck = check;
			epsilon *= 0.5;
			check = 1.0 + epsilon;
		} while (check != 1.0 && check != lastcheck);
		return epsilon;
	}

	/**
	 *
	 */
	private static void init() {
		final double epsilon = findMachEpsilon();
		orientErrorBound = (7.0 + 56.0 * epsilon) * epsilon;
		insphereErrorBound = (16.0 + 224.0 * epsilon) * epsilon;
		orientErrorBound2D = (3.0 + 16.0 * epsilon) * epsilon;
		incircleErrorBound2D = (10.0 + 96.0 * epsilon) * epsilon;
	}

	// >0 if pa,pb,pc ccw
	// <0 if pa,pb,pc cw
	// =0 if colinear
	/**
	 *
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @return
	 */
	public static double orient2D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc) {
		if (orientErrorBound2D == -1) {
			init();
		}
		double detleft, detright, det;
		double detsum, errbound;
		detleft = (pa.xd() - pc.xd()) * (pb.yd() - pc.yd());
		detright = (pa.yd() - pc.yd()) * (pb.xd() - pc.xd());
		det = detleft - detright;
		if (detleft > 0.0) {
			if (detright <= 0.0) {
				return Math.signum(det);
			} else {
				detsum = detleft + detright;
			}
		} else if (detleft < 0.0) {
			if (detright >= 0.0) {
				return Math.signum(det);
			} else {
				detsum = -detleft - detright;
			}
		} else {
			return Math.signum(det);
		}
		errbound = orientErrorBound2D * detsum;
		if (det >= errbound || -det >= errbound) {
			return Math.signum(det);
		}
		return orientDD2D(pa, pb, pc);
	}

	/**
	 *
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @return
	 */
	private static double orientDD2D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc) {
		WB_DoubleDouble ax, ay, bx, by, cx, cy;
		WB_DoubleDouble acx, bcx, acy, bcy;
		WB_DoubleDouble detleft, detright, det;
		det = WB_DoubleDouble.valueOf(0.0);
		ax = WB_DoubleDouble.valueOf(pa.xd());
		ay = WB_DoubleDouble.valueOf(pa.yd());
		bx = WB_DoubleDouble.valueOf(pb.xd());
		by = WB_DoubleDouble.valueOf(pb.yd());
		cx = WB_DoubleDouble.valueOf(pc.xd());
		cy = WB_DoubleDouble.valueOf(pc.yd());
		acx = ax.add(cx.negate());
		bcx = bx.add(cx.negate());
		acy = ay.add(cy.negate());
		bcy = by.add(cy.negate());
		detleft = acx.multiply(bcy);
		detright = acy.multiply(bcx);
		det = detleft.add(detright.negate());
		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	// >0 if pd inside circle through pa,pb,pc (if ccw)
	// <0 if pd outside circle through pa,pb,pc (if ccw)
	// =0 if on circle
	/**
	 *
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @param pd
	 * @return
	 */
	public static double inCircle2D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd) {
		if (incircleErrorBound2D == -1) {
			init();
		}
		double adx, ady, bdx, bdy, cdx, cdy;
		double bdxcdy, cdxbdy, cdxady, adxcdy, adxbdy, bdxady;
		double alift, blift, clift;
		double det;
		double permanent, errbound;
		adx = pa.xd() - pd.xd();
		bdx = pb.xd() - pd.xd();
		cdx = pc.xd() - pd.xd();
		ady = pa.yd() - pd.yd();
		bdy = pb.yd() - pd.yd();
		cdy = pc.yd() - pd.yd();
		bdxcdy = bdx * cdy;
		cdxbdy = cdx * bdy;
		alift = adx * adx + ady * ady;
		cdxady = cdx * ady;
		adxcdy = adx * cdy;
		blift = bdx * bdx + bdy * bdy;
		adxbdy = adx * bdy;
		bdxady = bdx * ady;
		clift = cdx * cdx + cdy * cdy;
		det = alift * (bdxcdy - cdxbdy) + blift * (cdxady - adxcdy) + clift * (adxbdy - bdxady);
		if (bdxcdy < 0) {
			bdxcdy = -bdxcdy;
		}
		if (cdxbdy < 0) {
			cdxbdy = -cdxbdy;
		}
		if (cdxady < 0) {
			cdxady = -cdxady;
		}
		if (adxcdy < 0) {
			adxcdy = -adxcdy;
		}
		if (adxbdy < 0) {
			adxbdy = -adxbdy;
		}
		if (bdxady < 0) {
			bdxady = -bdxady;
		}
		permanent = (bdxcdy + cdxbdy) * alift + (cdxady + adxcdy) * blift + (adxbdy + bdxady) * clift;
		errbound = incircleErrorBound2D * permanent;
		if (det > errbound || -det > errbound) {
			return Math.signum(det);
		}
		return incircleDD2D(pa, pb, pc, pd);
	}

	/**
	 *
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @param pd
	 * @return
	 */
	private static double incircleDD2D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd) {
		WB_DoubleDouble ax, ay, bx, by, cx, cy, dx, dy;
		WB_DoubleDouble adx, ady, bdx, bdy, cdx, cdy;
		WB_DoubleDouble bdxcdy, cdxbdy, cdxady, adxcdy, adxbdy, bdxady;
		WB_DoubleDouble alift, blift, clift;
		WB_DoubleDouble det;
		det = WB_DoubleDouble.valueOf(0.0);
		ax = WB_DoubleDouble.valueOf(pa.xd());
		ay = WB_DoubleDouble.valueOf(pa.yd());
		bx = WB_DoubleDouble.valueOf(pb.xd());
		by = WB_DoubleDouble.valueOf(pb.yd());
		cx = WB_DoubleDouble.valueOf(pc.xd());
		cy = WB_DoubleDouble.valueOf(pc.yd());
		dx = WB_DoubleDouble.valueOf(pd.xd());
		dy = WB_DoubleDouble.valueOf(pd.yd());
		dx = dx.negate();
		dy = dy.negate();
		adx = ax.add(dx);
		bdx = bx.add(dx);
		cdx = cx.add(dx);
		ady = ay.add(dy);
		bdy = by.add(dy);
		cdy = cy.add(dy);
		bdxcdy = bdx.multiply(cdy);
		cdxbdy = cdx.multiply(bdy);
		cdxady = cdx.multiply(ady);
		adxcdy = adx.multiply(cdy);
		adxbdy = adx.multiply(bdy);
		bdxady = bdx.multiply(ady);
		adx = adx.multiply(adx);
		ady = ady.multiply(ady);
		alift = adx.add(ady);
		bdx = bdx.multiply(bdx);
		bdy = bdy.multiply(bdy);
		blift = bdx.add(bdy);
		cdx = cdx.multiply(cdx);
		cdy = cdy.multiply(cdy);
		clift = cdx.add(cdy);
		alift = alift.multiply(bdxcdy.add(cdxbdy.negate()));
		blift = blift.multiply(cdxady.add(adxcdy.negate()));
		clift = clift.multiply(adxbdy.add(bdxady.negate()));
		det = alift.add(blift).add(clift);
		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	// >0 if pd below plane defined by pa,pb,pc
	// <0 if above (pa,pb,pc are ccw viewed from above)
	// = 0 if on plane
	/**
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @param pd
	 * @return
	 */
	public static double orient3D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd) {
		if (orientErrorBound == -1) {
			init();
		}
		final double adx = pa.xd() - pd.xd(), bdx = pb.xd() - pd.xd(), cdx = pc.xd() - pd.xd();
		final double ady = pa.yd() - pd.yd(), bdy = pb.yd() - pd.yd(), cdy = pc.yd() - pd.yd();
		double adz = pa.zd() - pd.zd(), bdz = pb.zd() - pd.zd(), cdz = pc.zd() - pd.zd();
		double adxbdy = adx * bdy;
		double adybdx = ady * bdx;
		double adxcdy = adx * cdy;
		double adycdx = ady * cdx;
		double bdxcdy = bdx * cdy;
		double bdycdx = bdy * cdx;
		final double m1 = adxbdy - adybdx;
		final double m2 = adxcdy - adycdx;
		final double m3 = bdxcdy - bdycdx;
		final double det = m1 * cdz - m2 * bdz + m3 * adz;
		if (adxbdy < 0) {
			adxbdy = -adxbdy;
		}
		if (adybdx < 0) {
			adybdx = -adybdx;
		}
		if (adxcdy < 0) {
			adxcdy = -adxcdy;
		}
		if (adycdx < 0) {
			adycdx = -adycdx;
		}
		if (bdxcdy < 0) {
			bdxcdy = -bdxcdy;
		}
		if (bdycdx < 0) {
			bdycdx = -bdycdx;
		}
		if (adz < 0) {
			adz = -adz;
		}
		if (bdz < 0) {
			bdz = -bdz;
		}
		if (cdz < 0) {
			cdz = -cdz;
		}
		double errbound = (adxbdy + adybdx) * cdz + (adxcdy + adycdx) * bdz + (bdxcdy + bdycdx) * adz;
		errbound *= orientErrorBound;
		if (det >= errbound) {
			return det > 0 ? 1 : det == 0 ? 0 : -1;
		} else if (-det >= errbound) {
			return det > 0 ? 1 : det == 0 ? 0 : -1;
		} else {
			return orientDD3D(pa, pb, pc, pd);
		}
	}

	private static double orientDD3D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd) {
		WB_DoubleDouble ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz;
		WB_DoubleDouble adx, bdx, cdx, ady, bdy, cdy, adz, bdz, cdz;
		WB_DoubleDouble m1, m2, m3;
		WB_DoubleDouble det;
		det = WB_DoubleDouble.ZERO;
		ax = new WB_DoubleDouble(pa.xd());
		ay = new WB_DoubleDouble(pa.yd());
		az = new WB_DoubleDouble(pa.zd());
		bx = new WB_DoubleDouble(pb.xd());
		by = new WB_DoubleDouble(pb.yd());
		bz = new WB_DoubleDouble(pb.zd());
		cx = new WB_DoubleDouble(pc.xd());
		cy = new WB_DoubleDouble(pc.yd());
		cz = new WB_DoubleDouble(pc.zd());
		dx = new WB_DoubleDouble(pd.xd()).negate();
		dy = new WB_DoubleDouble(pd.yd()).negate();
		dz = new WB_DoubleDouble(pd.zd()).negate();
		adx = ax.add(dx);
		bdx = bx.add(dx);
		cdx = cx.add(dx);
		ady = ay.add(dy);
		bdy = by.add(dy);
		cdy = cy.add(dy);
		adz = az.add(dz);
		bdz = bz.add(dz);
		cdz = cz.add(dz);
		m1 = adx.multiply(bdy).subtract(ady.multiply(bdx));
		m2 = adx.multiply(cdy).subtract(ady.multiply(cdx));
		m3 = bdx.multiply(cdy).subtract(bdy.multiply(cdx));
		det = m1.multiply(cdz).add(m3.multiply(adz)).subtract(m2.multiply(bdz));
		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	// >0 if pe inside sphere through pa,pb,pc,pd (if orient3d(pa,pb,pc,pd)>0))
	// <0 if pe outside sphere through pa,pb,pc,pd (if orient3d(pa,pb,pc,pd)>0))
	// =0 if on sphere
	/**
	 *
	 * @param pa
	 * @param pb
	 * @param pc
	 * @param pd
	 * @param pe
	 * @return
	 */
	public static double inSphere3D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd,
			final WB_Coord pe) {
		if (insphereErrorBound == -1) {
			init();
		}
		double aex, bex, cex, dex;
		double aey, bey, cey, dey;
		double aez, bez, cez, dez;
		double aexbey, bexaey, bexcey, cexbey, cexdey, dexcey, dexaey, aexdey;
		double aexcey, cexaey, bexdey, dexbey;
		double alift, blift, clift, dlift;
		double ab, bc, cd, da, ac, bd;
		double abc, bcd, cda, dab;
		double aezplus, bezplus, cezplus, dezplus;
		double aexbeyplus, bexaeyplus, bexceyplus, cexbeyplus;
		double cexdeyplus, dexceyplus, dexaeyplus, aexdeyplus;
		double aexceyplus, cexaeyplus, bexdeyplus, dexbeyplus;
		double det;
		double permanent, errbound;
		aex = pa.xd() - pe.xd();
		bex = pb.xd() - pe.xd();
		cex = pc.xd() - pe.xd();
		dex = pd.xd() - pe.xd();
		aey = pa.yd() - pe.yd();
		bey = pb.yd() - pe.yd();
		cey = pc.yd() - pe.yd();
		dey = pd.yd() - pe.yd();
		aez = pa.zd() - pe.zd();
		bez = pb.zd() - pe.zd();
		cez = pc.zd() - pe.zd();
		dez = pd.zd() - pe.zd();
		aexbey = aex * bey;
		bexaey = bex * aey;
		ab = aexbey - bexaey;
		bexcey = bex * cey;
		cexbey = cex * bey;
		bc = bexcey - cexbey;
		cexdey = cex * dey;
		dexcey = dex * cey;
		cd = cexdey - dexcey;
		dexaey = dex * aey;
		aexdey = aex * dey;
		da = dexaey - aexdey;
		aexcey = aex * cey;
		cexaey = cex * aey;
		ac = aexcey - cexaey;
		bexdey = bex * dey;
		dexbey = dex * bey;
		bd = bexdey - dexbey;
		abc = aez * bc - bez * ac + cez * ab;
		bcd = bez * cd - cez * bd + dez * bc;
		cda = cez * da + dez * ac + aez * cd;
		dab = dez * ab + aez * bd + bez * da;
		alift = aex * aex + aey * aey + aez * aez;
		blift = bex * bex + bey * bey + bez * bez;
		clift = cex * cex + cey * cey + cez * cez;
		dlift = dex * dex + dey * dey + dez * dez;
		det = dlift * abc - clift * dab + (blift * cda - alift * bcd);
		aezplus = Math.abs(aez);
		bezplus = Math.abs(bez);
		cezplus = Math.abs(cez);
		dezplus = Math.abs(dez);
		aexbeyplus = Math.abs(aexbey);
		bexaeyplus = Math.abs(bexaey);
		bexceyplus = Math.abs(bexcey);
		cexbeyplus = Math.abs(cexbey);
		cexdeyplus = Math.abs(cexdey);
		dexceyplus = Math.abs(dexcey);
		dexaeyplus = Math.abs(dexaey);
		aexdeyplus = Math.abs(aexdey);
		aexceyplus = Math.abs(aexcey);
		cexaeyplus = Math.abs(cexaey);
		bexdeyplus = Math.abs(bexdey);
		dexbeyplus = Math.abs(dexbey);
		permanent = ((cexdeyplus + dexceyplus) * bezplus + (dexbeyplus + bexdeyplus) * cezplus
				+ (bexceyplus + cexbeyplus) * dezplus) * alift
				+ ((dexaeyplus + aexdeyplus) * cezplus + (aexceyplus + cexaeyplus) * dezplus
						+ (cexdeyplus + dexceyplus) * aezplus) * blift
				+ ((aexbeyplus + bexaeyplus) * dezplus + (bexdeyplus + dexbeyplus) * aezplus
						+ (dexaeyplus + aexdeyplus) * bezplus) * clift
				+ ((bexceyplus + cexbeyplus) * aezplus + (cexaeyplus + aexceyplus) * bezplus
						+ (aexbeyplus + bexaeyplus) * cezplus) * dlift;
		errbound = insphereErrorBound * permanent;
		if (det > errbound || -det > errbound) {
			return det > 0 ? 1 : det == 0 ? 0 : -1;
		}
		return insphereDD3D(pa, pb, pc, pd, pe);
	}

	private static double insphereDD3D(final WB_Coord pa, final WB_Coord pb, final WB_Coord pc, final WB_Coord pd,
			final WB_Coord pe) {
		WB_DoubleDouble ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, ex, ey, ez;
		WB_DoubleDouble aex, bex, cex, dex;
		WB_DoubleDouble aey, bey, cey, dey;
		WB_DoubleDouble aez, bez, cez, dez;
		WB_DoubleDouble aexbey, bexaey, bexcey, cexbey, cexdey, dexcey, dexaey, aexdey;
		WB_DoubleDouble aexcey, cexaey, bexdey, dexbey;
		WB_DoubleDouble alift, blift, clift, dlift;
		WB_DoubleDouble ab, bc, cd, da, ac, bd;
		WB_DoubleDouble abc, bcd, cda, dab;
		WB_DoubleDouble det;
		det = WB_DoubleDouble.ZERO;
		ax = new WB_DoubleDouble(pa.xd());
		ay = new WB_DoubleDouble(pa.yd());
		az = new WB_DoubleDouble(pa.zd());
		bx = new WB_DoubleDouble(pb.xd());
		by = new WB_DoubleDouble(pb.yd());
		bz = new WB_DoubleDouble(pb.zd());
		cx = new WB_DoubleDouble(pc.xd());
		cy = new WB_DoubleDouble(pc.yd());
		cz = new WB_DoubleDouble(pc.zd());
		dx = new WB_DoubleDouble(pd.xd());
		dy = new WB_DoubleDouble(pd.yd());
		dz = new WB_DoubleDouble(pd.zd());
		ex = new WB_DoubleDouble(pe.xd()).negate();
		ey = new WB_DoubleDouble(pe.yd()).negate();
		ez = new WB_DoubleDouble(pe.zd()).negate();
		aex = ax.add(ex);
		bex = bx.add(ex);
		cex = cx.add(ex);
		dex = dx.add(ex);
		aey = ay.add(ey);
		bey = by.add(ey);
		cey = cy.add(ey);
		dey = dy.add(ey);
		aez = az.add(ez);
		bez = bz.add(ez);
		cez = cz.add(ez);
		dez = dz.add(ez);
		aexbey = aex.multiply(bey);
		bexaey = bex.multiply(aey);
		ab = aexbey.subtract(bexaey);
		bexcey = bex.multiply(cey);
		cexbey = cex.multiply(bey);
		bc = bexcey.subtract(cexbey);
		cexdey = cex.multiply(dey);
		dexcey = dex.multiply(cey);
		cd = cexdey.subtract(dexcey);
		dexaey = dex.multiply(aey);
		aexdey = aex.multiply(dey);
		da = dexaey.subtract(aexdey);
		aexcey = aex.multiply(cey);
		cexaey = cex.multiply(aey);
		ac = aexcey.subtract(cexaey);
		bexdey = bex.multiply(dey);
		dexbey = dex.multiply(bey);
		bd = bexdey.subtract(dexbey);
		abc = aez.multiply(bc).add(cez.multiply(ab)).subtract(bez.multiply(ac));
		bcd = bez.multiply(cd).add(dez.multiply(bc)).subtract(cez.multiply(bd));
		cda = cez.multiply(da).add(aez.multiply(cd)).subtract(dez.multiply(ac));
		dab = dez.multiply(ab).add(bez.multiply(da)).subtract(aez.multiply(bd));
		alift = aex.sqr().add(aey.sqr()).add(aez.sqr());
		blift = bex.sqr().add(bey.sqr()).add(bez.sqr());
		clift = cex.sqr().add(cey.sqr()).add(cez.sqr());
		dlift = dex.sqr().add(dey.sqr()).add(dez.sqr());
		det = dlift.multiply(abc).subtract(clift.multiply(dab)).add(blift.multiply(cda)).subtract(alift.multiply(bcd));
		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param q
	 * @return
	 */
	public static boolean inTriangle2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord q) {
		return !diffSideOfLine2D(p1, p2, q, p3) && !diffSideOfLine2D(p2, p3, q, p1) && !diffSideOfLine2D(p3, p1, q, p2);
	}

	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @param q
	 * @return
	 */
	public static boolean inTetrahedron3D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord p4,
			final WB_Coord q) {
		return !diffSideOfPlane3D(p1, p2, p3, q, p4) && !diffSideOfPlane3D(p2, p3, p4, q, p1)
				&& !diffSideOfPlane3D(p1, p2, p4, q, p3) && !diffSideOfPlane3D(p1, p3, p4, q, p2);
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param q0
	 * @param q1
	 * @return
	 */
	public static WB_Classification relativeSideOfLine2D(final WB_Coord p0, final WB_Coord p1, final WB_Coord q0,
			final WB_Coord q1) {
		double a, b;
		a = orient2D(p0, p1, q0);
		b = orient2D(p0, p1, q1);
		if (a == 0 && b == 0) {
			return WB_Classification.COLLINEAR;
		}
		if (a > 0 && b < 0 || a < 0 && b > 0) {
			if (a == 0 || b == 0) {
				return WB_Classification.DIFF;
			}
			return WB_Classification.DIFFEXCL;
		}
		if (a > 0 && b > 0 || a < 0 && b < 0) {
			if (a == 0 || b == 0) {
				return WB_Classification.SAME;
			}
			return WB_Classification.SAMEEXCL;
		}
		return null;
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param q0
	 * @param q1
	 * @return
	 */
	public static WB_Classification relativeSideOfPlane3D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2,
			final WB_Coord q0, final WB_Coord q1) {
		double a, b;
		a = orient3D(p0, p1, p2, q0);
		b = orient3D(p0, p1, p2, q1);
		if (a == 0 && b == 0) {
			return WB_Classification.COPLANAR;
		}
		if (a > 0 && b < 0 || a < 0 && b > 0) {
			if (a == 0 || b == 0) {
				return WB_Classification.DIFF;
			}
			return WB_Classification.DIFFEXCL;
		}
		if (a > 0 && b > 0 || a < 0 && b < 0) {
			if (a == 0 || b == 0) {
				return WB_Classification.SAME;
			}
			return WB_Classification.SAMEEXCL;
		}
		return null;
	}

	// diffsides returns true if q1 and q2 are NOT on the same side of the line
	// expanded by p1 and p2.
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static boolean diffSideOfLine2D(final WB_Coord p1, final WB_Coord p2, final WB_Coord q1, final WB_Coord q2) {
		double a, b;
		a = orient2D(p1, p2, q1);
		b = orient2D(p1, p2, q2);
		return a > 0 && b < 0 || a < 0 && b > 0;
	}

	// diffsides returns true if q1 and q2 are NOT on the same side of the plane
	// expanded by p1,p2, and p3.
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static boolean diffSideOfPlane3D(final WB_Coord p1, final WB_Coord p2, final WB_Coord p3, final WB_Coord q1,
			final WB_Coord q2) {
		double a, b;
		a = orient3D(p1, p2, p3, q1);
		b = orient3D(p1, p2, p3, q2);
		return a > 0 && b < 0 || a < 0 && b > 0;
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static boolean onLine2D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2) {
		if (orient2D(p0, p1, p2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static boolean onPlane3D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		if (orient3D(p0, p1, p2, p3) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Circumcenter tri.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param c
	 *            the c
	 * @return the double[]
	 */
	public static double[] circumcenter2D(final double[] a, final double[] b, final double[] c) {
		double xba, yba, zba, xca, yca, zca;
		double balength, calength;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;
		xba = b[0] - a[0];
		yba = b[1] - a[1];
		zba = b[2] - a[2];
		xca = c[0] - a[0];
		yca = c[1] - a[1];
		zca = c[2] - a[2];
		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;
		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;
		denominator = 0.5 / (xcrossbc * xcrossbc + ycrossbc * ycrossbc + zcrossbc * zcrossbc);
		xcirca = ((balength * yca - calength * yba) * zcrossbc - (balength * zca - calength * zba) * ycrossbc)
				* denominator;
		ycirca = ((balength * zca - calength * zba) * xcrossbc - (balength * xca - calength * xba) * zcrossbc)
				* denominator;
		zcirca = ((balength * xca - calength * xba) * ycrossbc - (balength * yca - calength * yba) * xcrossbc)
				* denominator;
		final double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a[0];
		circumcenter[1] = ycirca + a[1];
		circumcenter[2] = zcirca + a[2];
		return circumcenter;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static double[] circumcenter2D(final WB_Coord a, final WB_Coord b, final WB_Coord c) {
		double xba, yba, zba, xca, yca, zca;
		double balength, calength;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;
		xba = b.xd() - a.xd();
		yba = b.yd() - a.yd();
		zba = b.zd() - a.zd();
		xca = c.xd() - a.xd();
		yca = c.yd() - a.yd();
		zca = c.zd() - a.zd();
		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;
		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;
		denominator = 0.5 / (xcrossbc * xcrossbc + ycrossbc * ycrossbc + zcrossbc * zcrossbc);
		xcirca = ((balength * yca - calength * yba) * zcrossbc - (balength * zca - calength * zba) * ycrossbc)
				* denominator;
		ycirca = ((balength * zca - calength * zba) * xcrossbc - (balength * xca - calength * xba) * zcrossbc)
				* denominator;
		zcirca = ((balength * xca - calength * xba) * ycrossbc - (balength * yca - calength * yba) * xcrossbc)
				* denominator;
		final double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a.xd();
		circumcenter[1] = ycirca + a.yd();
		circumcenter[2] = zcirca + a.zd();
		return circumcenter;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double[] circumcenter3D(final double[] a, final double[] b, final double[] c, final double[] d) {
		return circumcenter3D(a, b, c, d, null, null, null);
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static double[] circumcenter3D(final WB_Coord a, final WB_Coord b, final WB_Coord c, final WB_Coord d) {
		return circumcenter3D(a, b, c, d, null, null, null);
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param xi
	 * @param eta
	 * @param zeta
	 * @return
	 */
	public static double[] circumcenter3D(final double[] a, final double[] b, final double[] c, final double[] d,
			final double[] xi, final double[] eta, final double[] zeta) {
		double xba, yba, zba, xca, yca, zca, xda, yda, zda;
		double balength, calength, dalength;
		double xcrosscd, ycrosscd, zcrosscd;
		double xcrossdb, ycrossdb, zcrossdb;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;
		xba = b[0] - a[0];
		yba = b[1] - a[1];
		zba = b[2] - a[2];
		xca = c[0] - a[0];
		yca = c[1] - a[1];
		zca = c[2] - a[2];
		xda = d[0] - a[0];
		yda = d[1] - a[1];
		zda = d[2] - a[2];
		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;
		dalength = xda * xda + yda * yda + zda * zda;
		xcrosscd = yca * zda - yda * zca;
		ycrosscd = zca * xda - zda * xca;
		zcrosscd = xca * yda - xda * yca;
		xcrossdb = yda * zba - yba * zda;
		ycrossdb = zda * xba - zba * xda;
		zcrossdb = xda * yba - xba * yda;
		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;
		denominator = 0.5 / (xba * xcrosscd + yba * ycrosscd + zba * zcrosscd);// Inexact
		// denominator = 0.5 / orient3d(b,c,d,a);//Exact
		xcirca = (balength * xcrosscd + calength * xcrossdb + dalength * xcrossbc) * denominator;
		ycirca = (balength * ycrosscd + calength * ycrossdb + dalength * ycrossbc) * denominator;
		zcirca = (balength * zcrosscd + calength * zcrossdb + dalength * zcrossbc) * denominator;
		final double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a[0];
		circumcenter[1] = ycirca + a[1];
		circumcenter[2] = zcirca + a[2];
		if (xi != null) {
			xi[0] = (xcirca * xcrosscd + ycirca * ycrosscd + zcirca * zcrosscd) * (2.0 * denominator);
			eta[0] = (xcirca * xcrossdb + ycirca * ycrossdb + zcirca * zcrossdb) * (2.0 * denominator);
			zeta[0] = (xcirca * xcrossbc + ycirca * ycrossbc + zcirca * zcrossbc) * (2.0 * denominator);
		}
		return circumcenter;
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param xi
	 * @param eta
	 * @param zeta
	 * @return
	 */
	public static double[] circumcenter3D(final WB_Coord a, final WB_Coord b, final WB_Coord c, final WB_Coord d,
			final double[] xi, final double[] eta, final double[] zeta) {
		double xba, yba, zba, xca, yca, zca, xda, yda, zda;
		double balength, calength, dalength;
		double xcrosscd, ycrosscd, zcrosscd;
		double xcrossdb, ycrossdb, zcrossdb;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;
		xba = b.xd() - a.xd();
		yba = b.yd() - a.yd();
		zba = b.zd() - a.zd();
		xca = c.xd() - a.xd();
		yca = c.yd() - a.yd();
		zca = c.zd() - a.zd();
		xda = d.xd() - a.xd();
		yda = d.yd() - a.yd();
		zda = d.zd() - a.zd();
		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;
		dalength = xda * xda + yda * yda + zda * zda;
		xcrosscd = yca * zda - yda * zca;
		ycrosscd = zca * xda - zda * xca;
		zcrosscd = xca * yda - xda * yca;
		xcrossdb = yda * zba - yba * zda;
		ycrossdb = zda * xba - zba * xda;
		zcrossdb = xda * yba - xba * yda;
		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;
		denominator = 0.5 / (xba * xcrosscd + yba * ycrosscd + zba * zcrosscd);// Inexact

		// denominator = 0.5 / orient3d(b,c,d,a);//Exact
		xcirca = (balength * xcrosscd + calength * xcrossdb + dalength * xcrossbc) * denominator;
		ycirca = (balength * ycrosscd + calength * ycrossdb + dalength * ycrossbc) * denominator;
		zcirca = (balength * zcrosscd + calength * zcrossdb + dalength * zcrossbc) * denominator;
		final double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a.xd();
		circumcenter[1] = ycirca + a.yd();
		circumcenter[2] = zcirca + a.zd();
		if (xi != null) {
			xi[0] = (xcirca * xcrosscd + ycirca * ycrosscd + zcirca * zcrosscd) * (2.0 * denominator);
			eta[0] = (xcirca * xcrossdb + ycirca * ycrossdb + zcirca * zcrossdb) * (2.0 * denominator);
			zeta[0] = (xcirca * xcrossbc + ycirca * ycrossbc + zcirca * zcrossbc) * (2.0 * denominator);
		}
		return circumcenter;
	}

	/**
	 * Circumradius tri.
	 *
	 * @param p0
	 *            the p0
	 * @param p1
	 *            the p1
	 * @param p2
	 *            the p2
	 * @return the double
	 */
	public static double circumradius2D(final double[] p0, final double[] p1, final double[] p2) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter2D(p0, p1, p2);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return Math.sqrt(t1 + t2 + t3);
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double circumradius2D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter2D(p0, p1, p2);
		t1 = circumcenter[0] - p0.xd();
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0.yd();
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0.zd();
		t3 = t3 * t3;
		return Math.sqrt(t1 + t2 + t3);
	}

	/**
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static double circumradius3D(final double[] p0, final double[] p1, final double[] p2, final double[] p3) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter3D(p0, p1, p2, p3, null, null, null);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return Math.sqrt(t1 + t2 + t3);
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static double circumradius3D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter3D(p0, p1, p2, p3, null, null, null);
		t1 = circumcenter[0] - p0.xd();
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0.yd();
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0.zd();
		t3 = t3 * t3;

		return Math.sqrt(t1 + t2 + t3);
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static WB_Circle circumcircle2D(final double[] p0, final double[] p1, final double[] p2) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter2D(p0, p1, p2);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return new WB_Circle(new WB_Point(circumcenter), Math.sqrt(t1 + t2 + t3));
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static WB_Circle circumcircle2D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter2D(p0, p1, p2);
		t1 = circumcenter[0] - p0.xd();
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0.yd();
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0.zd();
		t3 = t3 * t3;
		return new WB_Circle(new WB_Point(circumcenter), Math.sqrt(t1 + t2 + t3));
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static WB_Sphere circumsphere3D(final double[] p0, final double[] p1, final double[] p2, final double[] p3) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter3D(p0, p1, p2, p3, null, null, null);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return new WB_Sphere(new WB_Point(circumcenter), Math.sqrt(t1 + t2 + t3));
	}

	/**
	 *
	 *
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static WB_Sphere circumsphere3D(final WB_Coord p0, final WB_Coord p1, final WB_Coord p2, final WB_Coord p3) {
		double t1, t2, t3;
		final double[] circumcenter = circumcenter3D(p0, p1, p2, p3, null, null, null);
		t1 = circumcenter[0] - p0.xd();
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0.yd();
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0.zd();
		t3 = t3 * t3;
		return new WB_Sphere(new WB_Point(circumcenter), Math.sqrt(t1 + t2 + t3));
	}

	/**
	 *
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static boolean getIntersectionProper2D(final WB_Coord a, final WB_Coord b, final WB_Coord c,
			final WB_Coord d) {
		if (orient2D(a, b, c) == 0 || orient2D(a, b, d) == 0 || orient2D(c, d, a) == 0 || orient2D(c, d, b) == 0) {
			return false;
		} else if (orient2D(a, b, c) * orient2D(a, b, d) > 0 || orient2D(c, d, a) * orient2D(c, d, b) > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		new WB_Predicates();
		System.out.println(WB_Predicates.inCircle2D(new WB_Point(0, 1.0000000000033), new WB_Point(0, -1.0000000000033),
				new WB_Point(1.0000000000033, 0), new WB_Point(-1.0000000000033, 0)));
	}
}
