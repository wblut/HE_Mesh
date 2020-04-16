package wblut.geom;

public class WB_RandomRing extends WB_RandomFactory {
	private double innerRadius;
	private double outerRadius;

	public WB_RandomRing() {
		super();
		innerRadius = 0.5;
		outerRadius = 1.0;
	}

	public WB_RandomRing(final long seed) {
		super(seed);
		innerRadius = 0.5;
		outerRadius = 1.0;
	}

	public WB_RandomRing setRadius(final double ir, final double or) {
		innerRadius = Math.min(Math.abs(ir), Math.abs(or));
		outerRadius = Math.max(Math.abs(ir), Math.abs(or));
		return this;
	}

	@Override
	public WB_Point nextPointImp() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point(r * Math.cos(t), r * Math.sin(t), 0);
	}

	@Override
	public WB_Vector nextVectorImp() {
		double r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		while (r < innerRadius) {
			r = outerRadius * Math.pow(randomGen.nextDouble(), 0.5);
		}
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector(r * Math.cos(t), r * Math.sin(t), 0);
	}
}