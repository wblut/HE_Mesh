package wblut.geom;

/**
 *
 */
public abstract class WB_BinaryGrid3D {
	/**  */
	int sizeX;
	/**  */
	int sizeY;
	/**  */
	int sizeZ;
	/**  */
	WB_Point center;
	/**  */
	double dX, dY, dZ;
	/**  */
	int lx, ux, ly, uy, lz, uz;
	/**  */
	boolean invert;

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public abstract void set(int x, int y, int z);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public abstract void clear(int x, int y, int z);

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public final boolean get(final int x, final int y, final int z) {
		if (x > lx - 1 && y > ly - 1 && z > lz - 1 && x < ux && y < uy && z < uz) {
			return (invert) ? !getInternal(x, y, z) : getInternal(x, y, z);
		} else {
			return false;
		}
	}

	/**
	 *
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	abstract boolean getInternal(int x, int y, int z);

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
	public int getSizeZ() {
		return sizeZ;
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
		return center.sub(sizeX * 0.5 * dX, sizeY * 0.5 * dY, sizeZ * 0.5 * dZ);
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
	 * @return
	 */
	public double getDZ() {
		return dZ;
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
	 * @param dZ
	 */
	public void setDZ(final double dZ) {
		this.dZ = dZ;
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
	 */
	private WB_BinaryGrid3D() {
	}

	/**
	 *
	 *
	 * @param c
	 * @param sizeX
	 * @param dX
	 * @param sizeY
	 * @param dY
	 * @param sizeZ
	 * @param dZ
	 * @return
	 */
	public static WB_BinaryGrid3D createGrid(final WB_Coord c, final int sizeX, final double dX, final int sizeY,
			final double dY, final int sizeZ, final double dZ) {
		final WB_BinaryGrid3D grid = new WB_BinaryGridArray3D(c, sizeX, dX, sizeY, dY, sizeZ, dZ);
		return grid;
	}

	/**
	 *
	 *
	 * @param c
	 * @param sizeX
	 * @param dX
	 * @param sizeY
	 * @param dY
	 * @param sizeZ
	 * @param dZ
	 * @param values
	 * @return
	 */
	public static WB_BinaryGrid3D createGrid(final WB_Coord c, final int sizeX, final double dX, final int sizeY,
			final double dY, final int sizeZ, final double dZ, final boolean[] values) {
		final WB_BinaryGrid3D grid = new WB_BinaryGridArray3D(c, sizeX, dX, sizeY, dY, sizeZ, dZ, values);
		return grid;
	}

	/**
	 *
	 *
	 * @param lx
	 * @param ux
	 * @param ly
	 * @param uy
	 * @param lz
	 * @param uz
	 */
	public void setLimits(final int lx, final int ux, final int ly, final int uy, final int lz, final int uz) {
		this.lx = lx;
		this.ux = ux;
		this.ly = ly;
		this.uy = uy;
		this.lz = lz;
		this.uz = uz;
	}

	/**
	 *
	 */
	public void clearLimits() {
		this.lx = 0;
		this.ux = sizeX;
		this.ly = 0;
		this.uy = sizeY;
		this.lz = 0;
		this.uz = sizeZ;
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
	 *
	 * @return
	 */
	public int lz() {
		return lz;
	}

	/**
	 *
	 *
	 * @return
	 */
	public int uz() {
		return uz;
	}

	/**
	 *
	 */
	static class WB_BinaryGridArray3D extends WB_BinaryGrid3D {
		/**  */
		boolean[] grid;
		/**  */
		int sizeYZ;

		/**
		 *
		 *
		 * @param c
		 * @param sizeX
		 * @param dX
		 * @param sizeY
		 * @param dY
		 * @param sizeZ
		 * @param dZ
		 */
		WB_BinaryGridArray3D(final WB_Coord c, final int sizeX, final double dX, final int sizeY, final double dY,
				final int sizeZ, final double dZ) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.sizeZ = sizeZ;
			this.sizeYZ = sizeZ * sizeY;
			this.dX = dX;
			this.dY = dY;
			this.dZ = dZ;
			center = new WB_Point(c);
			grid = new boolean[sizeYZ * sizeX];
			clearLimits();
		}

		/**
		 *
		 *
		 * @param c
		 * @param sizeX
		 * @param dX
		 * @param sizeY
		 * @param dY
		 * @param sizeZ
		 * @param dZ
		 * @param grid
		 */
		WB_BinaryGridArray3D(final WB_Coord c, final int sizeX, final double dX, final int sizeY, final double dY,
				final int sizeZ, final double dZ, final boolean[] grid) {
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.sizeZ = sizeZ;
			this.sizeYZ = sizeZ * sizeY;
			this.dX = dX;
			this.dY = dY;
			this.dZ = dZ;
			center = new WB_Point(c);
			this.grid = grid;
			clearLimits();
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 * @param z
		 */
		@Override
		public void set(final int x, final int y, final int z) {
			if (x > -1 && y > -1 && z > -1 && x < sizeX && y < sizeY && z < sizeZ) {
				grid[index(x, y, z)] = true;
			}
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 * @param z
		 */
		@Override
		public void clear(final int x, final int y, final int z) {
			if (x > -1 && y > -1 && z > -1 && x < sizeX && y < sizeY && z < sizeZ) {
				grid[index(x, y, z)] = false;
			}
		}

		/**
		 *
		 *
		 * @param x
		 * @param y
		 * @param z
		 * @return
		 */
		@Override
		boolean getInternal(final int x, final int y, final int z) {
			return grid[index(x, y, z)];
		}

		/**
		 *
		 *
		 * @param i
		 * @param j
		 * @param k
		 * @return
		 */
		int index(final int i, final int j, final int k) {
			return k + j * sizeZ + i * sizeYZ;
		}
	}
}
