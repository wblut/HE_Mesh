/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.awt.Font;

import wblut.geom.WB_GeometryFactory;

/**
 * @author FVH
 *
 */
public class HET_TextFactory {

	private HET_TextFactory() {

	}

	public static HE_Mesh createText(final String text, final Font font, final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, font, flatness)));
	}

	public static HE_Mesh createText(final String text, final Font font, final int style, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, font, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public static HE_Mesh createText(final String text, final String fontName, final float pointSize) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, fontName, pointSize)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createText(final String text, final String fontName, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, fontName, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createText(final String text, final String fontName, final int style, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, fontName, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public static HE_Mesh createTextWithOpenTypeFont(final String text, final String fontName, final float pointSize) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithOpenTypeFont(text, fontName, pointSize)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithOpenTypeFont(final String text, final String fontName, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithOpenTypeFont(text, fontName, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithOpenTypeFont(final String text, final String fontName, final int style,
			final float pointSize, final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(
				new HEC_FromPolygons(gf.createTextWithOpenTypeFont(text, fontName, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public static HE_Mesh createTextWithTrueTypeFont(final String text, final String fontName, final float pointSize) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithTrueTypeFont(text, fontName, pointSize)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithTrueTypeFont(final String text, final String fontName, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithTrueTypeFont(text, fontName, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithTrueTypeFont(final String text, final String fontName, final int style,
			final float pointSize, final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(
				new HEC_FromPolygons(gf.createTextWithTrueTypeFont(text, fontName, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public static HE_Mesh createTextWithType1Font(final String text, final String fontName, final float pointSize) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithType1Font(text, fontName, pointSize)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithType1Font(final String text, final String fontName, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithType1Font(text, fontName, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithType1Font(final String text, final String fontName, final int style,
			final float pointSize, final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(
				new HEC_FromPolygons(gf.createTextWithType1Font(text, fontName, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @return
	 */
	public static HE_Mesh createTextWithFont(final String text, final String fontName, final float pointSize) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithFont(text, fontName, pointSize)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithFont(final String text, final String fontName, final float pointSize,
			final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithFont(text, fontName, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createTextWithFont(final String text, final String fontName, final int style,
			final float pointSize, final double flatness) {
		WB_GeometryFactory gf = new WB_GeometryFactory();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithFont(text, fontName, style, pointSize, flatness)));
	}

}
