/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import wblut.core.WB_ProgressReporter.WB_ProgressTracker;

/**
 *
 */
public abstract class HE_Machine {
	private String name;
	protected HE_ParameterSet parameters;
	HE_Machine() {
		setName(this.getClass().getSimpleName());
		parameters=new HE_ParameterSet(name);
	}

	/**
	 * Make the singleton WB_ProgressTracker available for use in creators,
	 * modifiers and subdividors.
	 */
	public static final WB_ProgressTracker tracker = WB_ProgressTracker.instance();

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public abstract HE_Mesh apply(HE_Mesh mesh);

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
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
	
	public void setParameterSet(HE_ParameterSet parameters) {
		this.parameters=parameters;
	}

	public void set(String name, Object value) {
		parameters.set(name, value);
	}
	
	public void set(String name, int value) {
		parameters.set(name, value);
	}
	
	public void set(String name, double value) {
		parameters.set(name, value);
	}
	
	public Object get(String name) {
		return parameters.get(name, null);
	}
	
}
