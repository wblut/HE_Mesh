package wblut.geom;

public class WB_RandomInShell extends WB_RandomFactory {
	private double innerRadius;
	private double outerRadius;

	public WB_RandomInShell() {
		super();
		innerRadius = 0.5;
		outerRadius = 1.0;
	}

	public WB_RandomInShell(final long seed) {
		super(seed);
		innerRadius = 0.5;
		outerRadius = 1.0;
	}

	public WB_RandomInShell setRadius(final double ir, final double or) {
		innerRadius = Math.min(Math.abs(ir), Math.abs(or));
		outerRadius = Math.max(Math.abs(ir), Math.abs(or));
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		}
		return new WB_Point(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation));
	}

	@Override
	public WB_Vector nextVectorImp() {
		final double elevation = Math.asin(2.0 * randomGen.nextDouble() - 1);
		final double azimuth = 2 * Math.PI * randomGen.nextDouble();
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 1.0 / 3.0);
		}
		return new WB_Vector(r * Math.cos(elevation) * Math.cos(azimuth), r * Math.cos(elevation) * Math.sin(azimuth),
				r * Math.sin(elevation));
	}
}
