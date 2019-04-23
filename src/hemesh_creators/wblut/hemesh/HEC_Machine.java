/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

/**
 * @author FVH
 *
 */
public class HEC_Machine extends HEC_Creator {

	private HE_Machine machine;
	private HE_Mesh source;

	public HEC_Machine() {
		super();
		machine = null;
		source = null;
		setOverride(true);

	}

	public HEC_Machine(final HE_Mesh source, final HE_Machine machine) {
		super();
		this.machine = machine;
		this.source = source;
		setOverride(true);

	}

	public HEC_Machine setMachine(final HE_Machine machine) {
		this.machine = machine;
		return this;

	}

	public HEC_Machine setSource(final HE_Mesh mesh) {
		this.source = mesh;
		return this;

	}

	@Override
	public HE_Mesh createBase() {
		HE_Mesh result = new HE_Mesh();
		if (source == null) {
			return result;
		}
		result = source.copy();
		if (machine == null) {
			return result;
		}
		machine.apply(result);

		return result;
	}

}
