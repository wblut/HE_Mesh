/**
 *
 */
package wblut.geom;

/**
 * @author FVH
 *
 */
public abstract class WB_BinaryGrid3D {
	int			sizeX;
	int			sizeY;
	int			sizeZ;
	WB_Point	center;
	double		dX, dY, dZ;
	int			lx, ux, ly, uy, lz, uz;
	boolean		invert;

	public abstract void set(int x, int y, int z);

	public abstract void clear(int x, int y, int z);

	public final boolean get(int x, int y, int z) {
		if (x > lx - 1 && y > ly - 1 && z > lz - 1 && x < ux && y < uy
				&& z < uz) {
			return (invert) ? !getInternal(x, y, z) : getInternal(x, y, z);
		} else {
			return false;
		}
	}

	abstract boolean getInternal(int x, int y, int z);

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getSizeZ() {
		return sizeZ;
	}

	public WB_Coord getCenter() {
		return center;
	}

	public WB_Coord getMin() {
		return center.sub(sizeX * 0.5 * dX, sizeY * 0.5 * dY, sizeZ * 0.5 * dZ);
	}

	public double getDX() {
		return dX;
	}

	public double getDY() {
		return dY;
	}

	public double getDZ() {
		return dZ;
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

	public void setDZ(final double dZ) {
		this.dZ = dZ;
	}

	public void setInvert(boolean b) {
		this.invert = b;
	}

	private WB_BinaryGrid3D() {
	}

	public static WB_BinaryGrid3D createGrid(final WB_Coord c, final int sizeX,
			final double dX, final int sizeY, final double dY, final int sizeZ,
			final double dZ) {
		WB_BinaryGrid3D grid = new WB_BinaryGridArray3D(c, sizeX, dX, sizeY, dY,
				sizeZ, dZ);
		return grid;
	}

	public static WB_BinaryGrid3D createGrid(final WB_Coord c, final int sizeX,
			final double dX, final int sizeY, final double dY, final int sizeZ,
			final double dZ, boolean[] values) {
		WB_BinaryGrid3D grid = new WB_BinaryGridArray3D(c, sizeX, dX, sizeY, dY,
				sizeZ, dZ, values);
		return grid;
	}

	public void setLimits(int lx, int ux, int ly, int uy, int lz, int uz) {
		this.lx = lx;
		this.ux = ux;
		this.ly = ly;
		this.uy = uy;
		this.lz = lz;
		this.uz = uz;
	}

	public void clearLimits() {
		this.lx = 0;
		this.ux = sizeX;
		this.ly = 0;
		this.uy = sizeY;
		this.lz = 0;
		this.uz = sizeZ;
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

	public int lz() {
		return lz;
	}

	public int uz() {
		return uz;
	}

	static class WB_BinaryGridArray3D extends WB_BinaryGrid3D {
		boolean[]	grid;
		int			sizeYZ;

		WB_BinaryGridArray3D(final WB_Coord c, final int sizeX, final double dX,
				final int sizeY, final double dY, final int sizeZ,
				final double dZ) {
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

		WB_BinaryGridArray3D(final WB_Coord c, final int sizeX, final double dX,
				final int sizeY, final double dY, final int sizeZ,
				final double dZ, boolean[] grid) {
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

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#set(boolean, int, int)
		 */
		@Override
		public void set(final int x, final int y, final int z) {
			if (x > -1 && y > -1 && z > -1 && x < sizeX && y < sizeY
					&& z < sizeZ) {
				grid[index(x, y, z)] = true;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#set(boolean, int, int)
		 */
		@Override
		public void clear(final int x, final int y, final int z) {
			if (x > -1 && y > -1 && z > -1 && x < sizeX && y < sizeY
					&& z < sizeZ) {
				grid[index(x, y, z)] = false;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see wblut.geom.WB_BinaryGrid2D#get(int, int)
		 */
		@Override
		boolean getInternal(final int x, final int y, final int z) {
			return grid[index(x, y, z)];
		}

		int index(final int i, final int j, final int k) {
			return k + j * sizeZ + i * sizeYZ;
		}
	}
}
