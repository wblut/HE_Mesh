package wblut.hemesh;

import java.awt.Font;

import wblut.geom.WB_GeometryFactory3D;

/**
 *
 */
public class HE_TextFactory {
	/**
	 *
	 */
	private HE_TextFactory() {
	}

	/**
	 *
	 *
	 * @param text
	 * @param font
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createText(final String text, final Font font, final double flatness) {
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, font, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param font
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @return
	 */
	public static HE_Mesh createText(final String text, final Font font, final int style, final float pointSize,
			final double flatness) {
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, font, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param font
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @param d
	 * @return
	 */
	public static HE_Mesh createText(final String text, final Font font, final int style, final float pointSize,
			final double flatness, final double d) {
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_Polygon(gf.createText(text, font, style, pointSize, flatness), d));
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_FromPolygons(gf.createText(text, fontName, style, pointSize, flatness)));
	}

	/**
	 *
	 *
	 * @param text
	 * @param fontName
	 * @param style
	 * @param pointSize
	 * @param flatness
	 * @param d
	 * @return
	 */
	public static HE_Mesh createText(final String text, final String fontName, final int style, final float pointSize,
			final double flatness, final double d) {
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_Polygon(gf.createText(text, fontName, style, pointSize, flatness), d));
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
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
		final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
		return new HE_Mesh(new HEC_FromPolygons(gf.createTextWithFont(text, fontName, style, pointSize, flatness)));
	}
}
