package wblut.core;

/**
 *
 */
public class WB_Disclaimer {
	/**
	 *
	 */
	private WB_Disclaimer() {
	}

	/**  */
	public static final WB_Disclaimer CURRENT_DISCLAIMER = new WB_Disclaimer();

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		final String dis = "License: https://github.com/wblut/HE_Mesh#license";
		return dis;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static String disclaimer() {
		return CURRENT_DISCLAIMER.toString();
	}
}