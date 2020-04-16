package wblut.processing;

import processing.core.PApplet;
import processing.core.PGraphics;

public class WB_Processing {
	protected PGraphics home;

	protected WB_Processing() {
	}

	public WB_Processing(final PApplet home) {
		if (home.g == null) {
			throw new IllegalArgumentException("WB_Render3D can only be used after size()");
		}
		this.home = home.g;
	}

	public WB_Processing(final PGraphics home) {
		this.home = home;
	}

	public static int color(final int r, final int g, final int b) {
		return 255 << 24 | r << 16 | g << 8 | b;
	}
}
