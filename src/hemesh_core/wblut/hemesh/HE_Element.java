package wblut.hemesh;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public abstract class HE_Element {
	/**  */
	private static AtomicLong currentKey = new AtomicLong(0);
	/**  */
	private final long key;
	/**  */
	private int internalLabel;
	/**  */
	private int label;

	/**
	 *
	 */
	public HE_Element() {
		key = currentKey.getAndAdd(1);
		internalLabel = -1;
		label = -1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final long getKey() {
		return key;
	}

	/**
	 *
	 *
	 * @param label
	 */
	protected final void setInternalLabel(final int label) {
		internalLabel = label;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final int getInternalLabel() {
		return internalLabel;
	}

	/**
	 *
	 *
	 * @param label
	 */
	public final void setLabel(final int label) {
		this.label = label;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final int getLabel() {
		return label;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int hashCode() {
		return (int) (key ^ key >>> 32);
	}

	/**
	 *
	 *
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof HE_Element)) {
			return false;
		}
		return ((HE_Element) other).getKey() == key;
	}

	/**
	 *
	 *
	 * @param el
	 */
	public void copyProperties(final HE_Element el) {
		internalLabel = el.getInternalLabel();
		label = el.getLabel();
	}

	/**
	 *
	 */
	protected abstract void clear();

	/**
	 *
	 */
	protected abstract void clearPrecomputed();
}
