/**
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 *
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 *
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Base element of the halfedge datastructure. Contains a unique key (long), a
 * user definable userLabel (int) and an internalLabel (int). The userLabel is
 * never modified by HE_Mesh. The internalLabel is set and reset by HE_Mesh.
 */
public abstract class HE_Element {
	private static AtomicLong	currentKey	= new AtomicLong(0);
	private final long			key;
	private int					internalLabel;
	private int					userLabel;

	/**
	 *
	 */
	public HE_Element() {
		key = currentKey.getAndAdd(1);
		internalLabel = -1;
		userLabel = -1;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final long getKey() {
		return key;
	}

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
	 * Set the user label to an integer value. -1 is the default value.
	 *
	 * @param label
	 */
	public final void setUserLabel(final int label) {
		userLabel = label;
	}

	/**
	 *
	 *
	 * @return
	 */
	public final int getUserLabel() {
		return userLabel;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int) (key ^ key >>> 32);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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
		userLabel = el.getUserLabel();
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
