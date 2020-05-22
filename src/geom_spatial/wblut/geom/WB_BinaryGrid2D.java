package wblut.geom;

/**
 *
 */
public abstract class WB_BinaryGrid2D {
	/**  */
	int sizeX;
	/**  */
	int sizeY;
	/**  */
	WB_Point center;
	/**  */
	double dX, dY;
	/**  */
	int lx, ux, ly, uy;
	/**  */
	boolean invert;

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public abstract void set(int x, int y);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 */
	public abstract void clear(int x, int y);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public final boolean get(final int x, final int y) {
		if (x > lx - 1 && y > ly - 1 && x < ux && y < uy) {
			return (invert) ? !getInternal(x, y) : getInternal(x, y);
		} else {
			return false;
		}
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	abstract boolean getInternal(int x, int y);

	/**
	 *
	 */
	private WB_BinaryGrid2D() {
	}

	/**
	 *
	 *
	 * @param c
	 * @param sizeX
	 * @param dX
	 * @param sizeY
	 * @param dY
	 * @return
	 */
	public static WB_BinaryGrid2D createGrid(final WB_Coord c, final int sizeX, final double dX, final int sizeY,
			final double dY) {
		return new WB_BinaryGridSafeArray2D(c, sizeX, dX, sizeY, dY);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getCenter() {
		return center;
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Coord getMin() {
		return center.sub(sizeX * 0.5 * dX, sizeY * 0.5 * dY);
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getDX() {
		return dX;
	}

	/**
	 *
	 *
	 * @return
	 */
	public double getDY() {
		return dY;
	}

	/**
	 *
	 *
	 * @param c
	 */
	public void setCenter(final WB_Coord c) {
		center.set(c);
	}

	/**
	 *
	 *
	 * @param dX
	 */
	public void setDX(final double dX) {
		this.dX = dX;
	}

	/**
	 *
	 *
	 * @param dY
	 */
	public void setDY(final double dY) {
		this.dY = dY;
	}

	/**
	 *
	 *
	 * @param b
	 */
	public void setInvert(final boolean b) {
		this.invert = b;
	}

	/**
	 *
	 *
	 * @param lx
	 * @param ux
	 * @param ly
	 * @param uy
	 */
	public void setLimits(final int lx, final int ux, final int ly, final int uy) {
		this.lx = lx;
		this.ux = ux;
		this.ly = ly;
		this.uy = uy;
	}

	/**
	 *
	 */
	public void clearLimits() {
		this.lx = 0;
		this.ux = sizeX;
		this.ly = 0;
		this.uy = sizeY;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int lx() {
		return lx;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int ux() {
		return ux;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int ly() {
		return ly;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int uy() {
		return uy;
	}

	/**
	 *
	 */
	static class WB_BinaryGridSafeArray2D extends WB_BinaryGrid2D {
		/**  */
		boolean[] grid;

		/**
		 *
		 *
		 * @param c
		 * @param sizeX
		 * @param dX
		 * @param sizeY
		 * @param dY
		 */
		WB_BinaryGridSafeArray2D(final WB_Coord c, final int sizeX, final double dX, final int sizeY, final double dY) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.dX = dX;
			this.dY = dY;
			center = new WB_Point(c);
			grid = new boolean[sizeX * sizeY];
			clearLimits();
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 */
		@Override
		public void set(final int x, final int y) {
			if (x > -1 && y > -1 && x < sizeX && y < sizeY) {
				grid[index(x, y)] = true;
			}
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 */
		@Override
		public void clear(final int x, final int y) {
			if (x > -1 && y > -1 && x < sizeX && y < sizeY) {
				grid[index(x, y)] = false;
			}
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 * @return
		 */
		@Override
		public boolean getInternal(final int x, final int y) {
			return grid[index(x, y)];
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @return
		 */
		int index(final int i, final int j) {
			return i + j * sizeX;
		}
	}
}
