package wblut.core;

/**
 *
 */
public class WB_Version {
	/**  */
	public static final WB_Version CURRENT_VERSION = new WB_Version();
	/**  */
	public static final int MAJOR = 2020;
	/**  */
	public static final int MINOR = 0;
	/**  */
	public static final int PATCH = 1;
	/**  */
	private static final String releaseInfo = "Quarante";

	/**
	 *
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out.println(CURRENT_VERSION);
	}

	/**
	 *
	 */
	private WB_Version() {
	}

	/**
	 *
	 *
	 * @return
	 */
	public static int getMajor() {
		return MAJOR;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static int getMinor() {
		return MINOR;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static int getPatch() {
		return PATCH;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public String toString() {
		final String ver = "W:Blut HE_Mesh " + MAJOR + "." + MINOR + "." + PATCH;
		if (releaseInfo != null && releaseInfo.length() > 0) {
			return ver + " " + releaseInfo;
		}
		return ver;
	}

	/**
	 *
	 *
	 * @return
	 */
	public static String version() {
		return CURRENT_VERSION.toString();
	}
}