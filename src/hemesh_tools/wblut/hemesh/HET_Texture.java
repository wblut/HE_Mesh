package wblut.hemesh;

import java.util.Iterator;

import processing.core.PImage;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordinateSystem;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Transform3D;
import wblut.math.WB_MTRandom;
import wblut.processing.WB_Color;
import wblut.processing.WB_Render3D;

public class HET_Texture {
	public static void setVertexColorFromVertexNormal(final HE_Mesh mesh) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		WB_Coord n;
		int color;
		while (vitr.hasNext()) {
			v = vitr.next();
			n = HE_MeshOp.getVertexNormal(v);
			color = WB_Color.color((int) (128 * (n.xd() + 1)), (int) (128 * (n.yd() + 1)), (int) (128 * (n.zd() + 1)));
			v.setColor(color);
		}
	}

	public static void setVertexColorFromPalette(final HE_Mesh mesh, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final int size = palette.length;
		int color;
		while (vitr.hasNext()) {
			v = vitr.next();
			final int choice = Math.max(0, Math.min(size - 1, v.getLabel()));
			color = palette[choice];
			v.setColor(color);
		}
	}

	public static void setRandomVertexColorFromPalette(final HE_Mesh mesh, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final int size = palette.length;
		int color;
		while (vitr.hasNext()) {
			v = vitr.next();
			final int choice = (int) Math.min(size - 1, Math.random() * size);
			color = palette[choice];
			v.setColor(color);
		}
	}

	public static void setRandomVertexColorFromPalette(final HE_Mesh mesh, final int[] palette, final long seed) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final int size = palette.length;
		int color;
		final WB_MTRandom random = new WB_MTRandom(seed);
		while (vitr.hasNext()) {
			v = vitr.next();
			final int choice = (int) Math.min(size - 1, random.nextDouble() * size);
			color = palette[choice];
			v.setColor(color);
		}
	}

	public static void setVertexColorFromVertexUmbrella(final HE_Mesh mesh, final double minrange,
			final double maxrange, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		int color;
		final double idenom = 0.5 * palette.length / Math.PI;
		while (vitr.hasNext()) {
			v = vitr.next();
			color = (int) (idenom * (HE_MeshOp.getUmbrellaAngle(v) - minrange) / (maxrange - minrange));
			color = Math.max(0, Math.min(color, palette.length - 1));
			v.setColor(palette[color]);
		}
	}

	public static void setVertexColorFromVertexCurvature(final HE_Mesh mesh, final double minrange,
			final double maxrange, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		int color;
		final double idenom = 0.5 * palette.length / Math.PI;
		while (vitr.hasNext()) {
			v = vitr.next();
			color = (int) (idenom * (HE_MeshOp.getGaussianCurvature(v) - minrange) / (maxrange - minrange));
			color = Math.max(0, Math.min(color, palette.length - 1));
			v.setColor(palette[color]);
		}
	}

	public static void setFaceColorFromFaceNormal(final HE_Mesh mesh) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		WB_Coord n;
		int color;
		while (fitr.hasNext()) {
			f = fitr.next();
			n = HE_MeshOp.getFaceNormal(f);
			color = WB_Color.color((int) (128 * (n.xd() + 1)), (int) (128 * (n.yd() + 1)), (int) (128 * (n.zd() + 1)));
			f.setColor(color);
		}
	}

	public static void setFaceColorFromPalette(final HE_Mesh mesh, final int[] palette) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		final int size = palette.length;
		int color;
		while (fitr.hasNext()) {
			f = fitr.next();
			final int choice = Math.max(0, Math.min(size - 1, f.getLabel()));
			color = palette[choice];
			f.setColor(color);
		}
	}

	public static void setRandomFaceColorFromPalette(final HE_Mesh mesh, final int[] palette) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		final int size = palette.length;
		int color;
		while (fitr.hasNext()) {
			f = fitr.next();
			final int choice = (int) Math.min(size - 1, Math.random() * size);
			color = palette[choice];
			f.setColor(color);
		}
	}

	public static void setRandomFaceColorFromPalette(final HE_Mesh mesh, final int[] palette, final long seed) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		final int size = palette.length;
		int color;
		final WB_MTRandom random = new WB_MTRandom(seed);
		while (fitr.hasNext()) {
			f = fitr.next();
			final int choice = (int) Math.min(size - 1, random.nextDouble() * size);
			color = palette[choice];
			f.setColor(color);
		}
	}

	public static void setFaceColorFromTexture(final HE_Mesh mesh, final PImage texture) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		HE_Halfedge he;
		HE_TextureCoordinate uvw;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceHalfedgeInnerCirculator fhc = f.fheiCrc();
			final WB_Point p = new WB_Point();
			int id = 0;
			while (fhc.hasNext()) {
				he = fhc.next();
				uvw = he.getUVW();
				p.addSelf(uvw.ud(), uvw.vd(), 0);
				id++;
			}
			p.divSelf(id);
			f.setColor(WB_Render3D.getColorFromPImage(p.xd(), p.yd(), texture));
		}
	}

	public static void setHalfedgeColorFromTexture(final HE_Mesh mesh, final PImage texture) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		HE_Halfedge he;
		HE_TextureCoordinate p;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceHalfedgeInnerCirculator fhec = f.fheiCrc();
			while (fhec.hasNext()) {
				he = fhec.next();
				p = he.getUVW();
				he.setColor(WB_Render3D.getColorFromPImage(p.ud(), p.vd(), texture));
			}
		}
	}

	public static void setVertexColorFromTexture(final HE_Mesh mesh, final PImage texture) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		HE_TextureCoordinate p;
		while (vitr.hasNext()) {
			v = vitr.next();
			p = v.getHalfedge().getUVW();
			v.setColor(WB_Render3D.getColorFromPImage(p.ud(), p.vd(), texture));
		}
	}

	public static double uWrapThreshold = 0.25;

	public static void setUVWPlanar(final HE_HalfedgeStructure mesh, final WB_Coord origin, final WB_Coord uDirection,
			final WB_Coord vDirection, final double uSize, final double vSize) {
		final WB_CoordinateSystem CS = new WB_CoordinateSystem();
		CS.setOrigin(new WB_Point(origin));
		CS.setXY(uDirection, vDirection);
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromWorldToCS(CS);
		final WB_Point p = new WB_Point();
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			T.applyAsPointInto(v, p);
			v.setUVW(0.5 + p.xd() / uSize, 0.5 + p.yd() / vSize, 0);
		}
	}

	public static void setUVWProjection(final HE_HalfedgeStructure mesh, final WB_Coord origin, final double distance,
			final WB_Coord uDirection, final WB_Coord vDirection, final double uSize, final double vSize) {
		final WB_CoordinateSystem CS = new WB_CoordinateSystem();
		CS.setOrigin(new WB_Point(origin));
		CS.setXY(uDirection, vDirection);
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromWorldToCS(CS);
		final WB_Point p = new WB_Point();
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			T.applyAsPointInto(v, p);
			v.setUVW(0.5 + p.xd() / uSize * p.zd() / distance, 0.5 + p.yd() / vSize * p.zd() / distance, 0);
		}
	}

	public static void setUVWCylindrical(final HE_HalfedgeStructure mesh, final WB_Coord origin,
			final WB_Coord direction, final double height) {
		final WB_CoordinateSystem CS = new WB_CoordinateSystem(new WB_Plane(origin, direction));
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromWorldToCS(CS);
		final WB_Point p = new WB_Point();
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			T.applyAsPointInto(v, p);
			v.setUVW(0.5 * (Math.PI + Math.atan2(p.yd(), p.xd())) / Math.PI, 0.5 + p.zd() / height, 0);
		}
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		HE_FaceHalfedgeInnerCirculator fheiCrc;
		HE_Halfedge he;
		boolean uWrap1, uWrap2;
		while (fItr.hasNext()) {
			f = fItr.next();
			uWrap1 = false;
			uWrap2 = false;
			fheiCrc = f.fheiCrc();
			while (fheiCrc.hasNext()) {
				he = fheiCrc.next();
				uWrap1 |= he.getUVW().ud() < uWrapThreshold;
				uWrap2 |= he.getUVW().ud() > (1.0 - uWrapThreshold);
			}
			if (uWrap1 && uWrap2) {
				fheiCrc = f.fheiCrc();
				while (fheiCrc.hasNext()) {
					he = fheiCrc.next();
					if (he.getUVW().ud() < uWrapThreshold) {
						final HE_TextureCoordinate uvw = he.getUVW();
						he.setUVW(uvw.ud() + 1.0, uvw.yd(), uvw.zd());
					}
				}
			}
		}
	}

	public static void setUVWSpherical(final HE_HalfedgeStructure mesh, final WB_Coord origin,
			final WB_Coord direction) {
		final WB_CoordinateSystem CS = new WB_CoordinateSystem(new WB_Plane(origin, direction));
		final WB_Transform3D T = new WB_Transform3D();
		T.addFromWorldToCS(CS);
		final WB_Point p = new WB_Point();
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		double r, azimuth, inclination;
		while (vItr.hasNext()) {
			v = vItr.next();
			T.applyAsPointInto(v, p);
			r = p.getLength();
			azimuth = Math.atan2(p.yd(), p.xd());
			inclination = Math.acos(Math.max(-1.0, Math.min(1.0, p.zd() / r)));
			v.setUVW(0.5 * (azimuth + Math.PI) / Math.PI, 1.0 - inclination / Math.PI, 0.0);
		}
		final HE_FaceIterator fItr = mesh.fItr();
		HE_Face f;
		HE_FaceHalfedgeInnerCirculator fheiCrc;
		HE_Halfedge he;
		boolean uWrap1, uWrap2;
		while (fItr.hasNext()) {
			f = fItr.next();
			uWrap1 = false;
			uWrap2 = false;
			fheiCrc = f.fheiCrc();
			while (fheiCrc.hasNext()) {
				he = fheiCrc.next();
				uWrap1 |= he.getUVW().ud() < uWrapThreshold;
				uWrap2 |= he.getUVW().ud() > (1.0 - uWrapThreshold);
			}
			if (uWrap1 && uWrap2) {
				fheiCrc = f.fheiCrc();
				while (fheiCrc.hasNext()) {
					he = fheiCrc.next();
					if (he.getUVW().ud() < uWrapThreshold) {
						final HE_TextureCoordinate uvw = he.getUVW();
						he.setUVW(uvw.ud() + 1.0, uvw.yd(), uvw.zd());
					}
				}
			}
		}
	}
}
