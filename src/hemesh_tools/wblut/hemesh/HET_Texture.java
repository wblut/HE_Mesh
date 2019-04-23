/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import processing.core.PImage;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;
import wblut.math.WB_MTRandom;
import wblut.processing.WB_Color;
import wblut.processing.WB_Render3D;

public class HET_Texture {
	/**
	 *
	 */
	public static void cleanUVW(final HE_Mesh mesh) {
		HE_VertexIterator vItr = mesh.vItr();
		while (vItr.hasNext()) {
			vItr.next().cleanUVW();
		}
	}

	/**
	 *
	 */
	public static void clearUVW(final HE_Mesh mesh) {
		HE_VertexIterator vItr = mesh.vItr();
		while (vItr.hasNext()) {
			vItr.next().clearUVW();
		}
		HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			heItr.next().clearUVW();
		}
	}

	/**
	 * Set vertex colors according to the vertex normal normal.x: -1 to 1, red
	 * component from 0 to 255 normal.y: -1 to 1, green component from 0 to 255
	 * normal.z: -1 to 1, blue component from 0 to 255
	 *
	 * @param mesh
	 */
	public static void setVertexColorFromVertexNormal(final HE_Mesh mesh) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		WB_Coord n;
		int color;
		while (vitr.hasNext()) {
			v = vitr.next();
			n = HE_MeshOp.getVertexNormal(v);
			color = WB_Color.color((int) (128 * (n.xd() + 1)),
					(int) (128 * (n.yd() + 1)), (int) (128 * (n.zd() + 1)));
			v.setColor(color);
		}
	}

	/**
	 * Set vertex colors by vertex.getLabel() from a palette (an array of int)
	 *
	 * @param mesh
	 * @param palette
	 */
	public static void setVertexColorFromPalette(final HE_Mesh mesh,
			final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final int size = palette.length;
		int color;
		while (vitr.hasNext()) {
			v = vitr.next();
			final int choice = Math.max(0,
					Math.min(size - 1, v.getUserLabel()));
			color = palette[choice];
			v.setColor(color);
		}
	}

	/**
	 * Set vertex colors randomly chosen from a palette (an array of int)
	 *
	 * @param mesh
	 * @param palette
	 */
	public static void setRandomVertexColorFromPalette(final HE_Mesh mesh,
			final int[] palette) {
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

	/**
	 *
	 *
	 * @param mesh
	 * @param palette
	 * @param seed
	 */
	public static void setRandomVertexColorFromPalette(final HE_Mesh mesh,
			final int[] palette, final long seed) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		final int size = palette.length;
		int color;
		final WB_MTRandom random = new WB_MTRandom(seed);
		while (vitr.hasNext()) {
			v = vitr.next();
			final int choice = (int) Math.min(size - 1,
					random.nextDouble() * size);
			;
			color = palette[choice];
			v.setColor(color);
		}
	}

	/**
	 * Set vertex colors according to the umbrella angle. Angle: 0 (infinite
	 * outward or inward spike) to 2 Pi (flat).
	 *
	 * @param mesh
	 * @param minrange
	 * @param maxrange
	 * @param palette
	 */
	public static void setVertexColorFromVertexUmbrella(final HE_Mesh mesh,
			final double minrange, final double maxrange, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		int color;
		final double idenom = 0.5 * palette.length / Math.PI;
		while (vitr.hasNext()) {
			v = vitr.next();
			color = (int) (idenom * (HE_MeshOp.getUmbrellaAngle(v) - minrange)
					/ (maxrange - minrange));
			color = Math.max(0, Math.min(color, palette.length - 1));
			v.setColor(palette[color]);
		}
	}

	/**
	 * Set vertex colors according to the Gaussian curvature.
	 *
	 * @param mesh
	 * @param minrange
	 * @param maxrange
	 * @param palette
	 */
	public static void setVertexColorFromVertexCurvature(final HE_Mesh mesh,
			final double minrange, final double maxrange, final int[] palette) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		int color;
		final double idenom = 0.5 * palette.length / Math.PI;
		while (vitr.hasNext()) {
			v = vitr.next();
			color = (int) (idenom * (HE_MeshOp.getGaussianCurvature(v) - minrange)
					/ (maxrange - minrange));
			color = Math.max(0, Math.min(color, palette.length - 1));
			v.setColor(palette[color]);
		}
	}

	/**
	 * Set face colors according to the face normal normal.x: -1 to 1, red
	 * component from 0 to 255 normal.y: -1 to 1, green component from 0 to 255
	 * normal.z: -1 to 1, blue component from 0 to 255
	 *
	 * @param mesh
	 */
	public static void setFaceColorFromFaceNormal(final HE_Mesh mesh) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		WB_Coord n;
		int color;
		while (fitr.hasNext()) {
			f = fitr.next();
			n = HE_MeshOp.getFaceNormal(f);
			color = WB_Color.color((int) (128 * (n.xd() + 1)),
					(int) (128 * (n.yd() + 1)), (int) (128 * (n.zd() + 1)));
			f.setColor(color);
		}
	}

	/**
	 * Set face colors by face.getLabel() from a palette (an array of int)
	 *
	 * @param mesh
	 * @param palette
	 */
	public static void setFaceColorFromPalette(final HE_Mesh mesh,
			final int[] palette) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		final int size = palette.length;
		int color;
		while (fitr.hasNext()) {
			f = fitr.next();
			final int choice = Math.max(0,
					Math.min(size - 1, f.getUserLabel()));
			color = palette[choice];
			f.setColor(color);
		}
	}

	/**
	 * Set face colors randomly chosen from a palette (an array of int).
	 *
	 * @param mesh
	 * @param palette
	 */
	public static void setRandomFaceColorFromPalette(final HE_Mesh mesh,
			final int[] palette) {
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

	/**
	 *
	 *
	 * @param mesh
	 * @param palette
	 * @param seed
	 */
	public static void setRandomFaceColorFromPalette(final HE_Mesh mesh,
			final int[] palette, final long seed) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		final int size = palette.length;
		int color;
		final WB_MTRandom random = new WB_MTRandom(seed);
		while (fitr.hasNext()) {
			f = fitr.next();
			final int choice = (int) Math.min(size - 1,
					random.nextDouble() * size);
			color = palette[choice];
			f.setColor(color);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param texture
	 */
	public static void setFaceColorFromTexture(final HE_Mesh mesh,
			final PImage texture) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		HE_Vertex v;
		HE_TextureCoordinate uvw;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceVertexCirculator fvc = f.fvCrc();
			final WB_Point p = new WB_Point();
			int id = 0;
			while (fvc.hasNext()) {
				v = fvc.next();
				uvw = v.getUVW(f);
				p.addSelf(uvw.ud(), uvw.vd(), 0);
				id++;
			}
			p.divSelf(id);
			f.setColor(WB_Render3D.getColorFromPImage(p.xd(), p.yd(), texture));
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param texture
	 */
	public static void setHalfedgeColorFromTexture(final HE_Mesh mesh,
			final PImage texture) {
		final HE_FaceIterator fitr = mesh.fItr();
		HE_Face f;
		HE_Halfedge he;
		HE_TextureCoordinate p;
		while (fitr.hasNext()) {
			f = fitr.next();
			final HE_FaceHalfedgeInnerCirculator fhec = f.fheiCrc();
			while (fhec.hasNext()) {
				he = fhec.next();
				p = he.getVertex().getUVW(f);
				he.setColor(WB_Render3D.getColorFromPImage(p.ud(), p.vd(),
						texture));
			}
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param texture
	 */
	public static void setVertexColorFromTexture(final HE_Mesh mesh,
			final PImage texture) {
		final HE_VertexIterator vitr = mesh.vItr();
		HE_Vertex v;
		HE_TextureCoordinate p;
		while (vitr.hasNext()) {
			v = vitr.next();
			p = v.getVertexUVW();
			v.setColor(WB_Render3D.getColorFromPImage(p.ud(), p.vd(), texture));
		}
	}
	/*
	 * New matplotlib colormaps by Nathaniel J. Smith, Stefan van der Walt, and
	 * (in the case of viridis) Eric Firing.
	 * This file and the colormaps in it are released under the CC0 license /
	 * public domain dedication. We would appreciate credit if you use or
	 * redistribute these colormaps, but do not impose any legal restrictions.
	 * To the extent possible under law, the persons who associated CC0 with
	 * mpl-colormaps have waived all copyright and related or neighboring rights
	 * to mpl-colormaps.
	 * You should have received a copy of the CC0 legalcode along with this
	 * work. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
	 */
}
