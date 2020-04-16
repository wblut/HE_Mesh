package wblut.hemesh;

import wblut.core.WB_ProgressReporter.WB_ProgressTracker;

public abstract class HE_Machine {
	private String name;
	protected HE_ParameterSet parameters;

	HE_Machine() {
		setName(this.getClass().getSimpleName());
		parameters = new HE_ParameterSet(name);
	}

	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	public abstract HE_Mesh apply(HE_Mesh mesh);

	public abstract HE_Mesh apply(HE_Selection selection);

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public HE_ParameterSet getParameterSet() {
		return parameters;
	}

	public void setParameterSet(final HE_ParameterSet parameters) {
		this.parameters = parameters;
	}

	public void set(final String name, final Object value) {
		parameters.set(name, value);
	}

	public void set(final String name, final int value) {
		parameters.set(name, value);
	}

	public void set(final String name, final double value) {
		parameters.set(name, value);
	}

	public Object get(final String name) {
		return parameters.get(name, null);
	}

	public String[] getParameterNames() {
		return parameters.getParameterNames();
	}
}
