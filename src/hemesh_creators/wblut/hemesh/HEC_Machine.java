package wblut.hemesh;

/**
 *
 */
public class HEC_Machine extends HEC_Creator {
	/**  */
	private HE_Machine machine;
	/**  */
	private HE_Mesh source;

	/**
	 *
	 */
	public HEC_Machine() {
		super();
		machine = null;
		source = null;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param source
	 * @param machine
	 */
	public HEC_Machine(final HE_Mesh source, final HE_Machine machine) {
		super();
		this.machine = machine;
		this.source = source;
		setOverride(true);
	}

	/**
	 *
	 *
	 * @param machine
	 * @return
	 */
	public HEC_Machine setMachine(final HE_Machine machine) {
		this.machine = machine;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEC_Machine setSource(final HE_Mesh mesh) {
		this.source = mesh;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
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
