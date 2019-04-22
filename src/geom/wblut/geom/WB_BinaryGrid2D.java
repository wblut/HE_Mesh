/**
 *
 */
package wblut.geom;

/**
 * @author FVH
 *
 */
public abstract class WB_BinaryGrid2D {
	int			sizeX;
	int			sizeY;
	WB_Point	center;
	double		dX, dY;
	int			lx, ux, ly, uy;
	boolean		invert;

	public abstract void set(int x, int y);

	public abstract void clear(int x, int y);

	public final boolean get(int x, int y) {
		if (x > lx - 1 && y > ly - 1 && x < ux && y < uy) {
			return (invert) ? !getInternal(x, y) : getInternal(x, y);
		} else {
			return false;
		}
	}

	abstract boolean getInternal(int x, int y);

	private WB_BinaryGrid2D() {
	}

	public static WB_BinaryGrid2D createGrid(final WB_Coord c, final int sizeX,
			final double dX, final int sizeY, final double dY) {
		return new WB_BinaryGridSafeArray2D(c, sizeX, dX, sizeY, dY);
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_Coord getMin() {
		return center.sub(sizeX * 0.5 * dX, sizeY * 0.5 * dY);
	}

	public double getDX() {
		return dX;
	}

	public double getDY() {
		return dY;
	}

	public void setCenter(final WB_Coord c) {
		center.set(c);
	}

	public void setDX(final double dX) {
		this.dX = dX;
	}

	public void setDY(final double dY) {
		this.dY = dY;
	}

	public void setInvert(boolean b) {
		this.invert = b;
	}

	public void setLimits(int lx, int ux, int ly, int uy) {
		this.lx = lx;
		this.ux = ux;
		this.ly = ly;
		this.uy = uy;
	}

	public void clearLimits() {
		this.lx = 0;
		this.ux = sizeX;
		this.ly = 0;
		this.uy = sizeY;
	}

	public int lx() {
		return lx;
	}

	public int ux() {
		return ux;
	}

	public int ly() {
		return ly;
	}

	public int uy() {
		return uy;
	}

	static class WB_BinaryGridSafeArray2D extends WB_BinaryGrid2D {
		boolean[] grid;

		WB_BinaryGridSafeArray2D(final WB_Coord c, final int sizeX,
				final double dX, final int sizeY, final double dY) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.dX = dX;
			this.dY = dY;
			center = new WB_Point(c);
			grid = new boolean[sizeX * sizeY];
			clearLimits();
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#set(boolean, int, int)
		 */
		@Override
		public void set(final int x, final int y) {
			if (x > -1 && y > -1 && x < sizeX && y < sizeY) {
				grid[index(x, y)] = true;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#set(boolean, int, int)
		 */
		@Override
		public void clear(final int x, final int y) {
			if (x > -1 && y > -1 && x < sizeX && y < sizeY) {
				grid[index(x, y)] = false;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#get(int, int)
		 */
		@Override
		public boolean getInternal(final int x, final int y) {
			return grid[index(x, y)];
		}

		int index(final int i, final int j) {
			return i + j * sizeX;
		}
	}
}
