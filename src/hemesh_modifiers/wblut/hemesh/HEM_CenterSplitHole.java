package wblut.hemesh;

public class HEM_CenterSplitHole extends HEM_Modifier {
	private double d;
	private double c;
	boolean relative;
	private HE_Selection selectionOut;

	public HEM_CenterSplitHole() {
		super();
		d = 0;
		c = 0.5;
		relative = false;
	}

	public HEM_CenterSplitHole setOffset(final double d) {
		this.d = d;
		return this;
	}

	public HEM_CenterSplitHole setChamfer(final double c) {
		this.c = c;
		return this;
	}

	public HEM_CenterSplitHole setRelative(final boolean b) {
		this.relative = b;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(c).setDistance(d).setRelative(relative);
		mesh.modify(ext);
		mesh.deleteFaces(mesh.getSelection("extruded"));
		mesh.removeSelection("extruded");
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HEM_Extrude ext = new HEM_Extrude().setChamfer(c).setDistance(d).setRelative(relative);
		selection.modify(ext);
		selection.getParent().deleteFaces(selection.getParent().getSelection("extruded"));
		selection.getParent().removeSelection("extruded");
		return selection.getParent();
	}

	public HE_Selection getWallFaces() {
		return this.selectionOut;
	}
}
