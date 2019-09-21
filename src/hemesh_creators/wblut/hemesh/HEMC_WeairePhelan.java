/*
 * HE_Mesh  Frederik Vanhoutte - www.wblut.com
 * 
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * 
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */

package wblut.hemesh;

import java.util.ArrayList;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Math;

/**
 *
 */
public class HEMC_WeairePhelan extends HEMC_MultiCreator {

	/**
	 *
	 */
	private static final double[][] dodecahedronPoints = { { 0.31498, 0, 0.62996 }, { -0.31498, 0, 0.62996 },
			{ 0.41997, 0.41997, 0.41997 }, { 0, 0.62996, 0.31498 }, { -0.41997, 0.41997, 0.41997 },
			{ -0.41997, -0.41997, 0.41997 }, { 0, -0.62996, .31498 }, { .41997, -.41997, .41997 },
			{ .62996, .31498, 0 }, { -.62996, .31498, 0 }, { -.62996, -.31498, 0 }, { .62996, -.31498, 0 },
			{ .41997, .41997, -.41997 }, { 0, .62996, -.31498 }, { -.41997, .41997, -.41997 },
			{ -.41997, -.41997, -.41997 }, { 0, -.62996, -.31498 }, { .41997, -.41997, -.41997 },
			{ .31498, 0, -.62996 }, { -.31498, 0, -.62996 } };

	/**
	 *
	 */
	private static final double[][] tetrakaidecahedronPoints = { { .314980, .370039, .5 }, { -.314980, .370039, .5 },
			{ -.5, 0, .5 }, { -.314980, -.370039, .5 }, { .314980, -.370039, .5 }, { .5, 0, .5 },
			{ .419974, .580026, 0.080026 }, { -.419974, .580026, 0.080026 }, { -.685020, 0, .129961 },
			{ -.419974, -.580026, 0.080026 }, { .419974, -.580026, 0.080026 }, { .685020, 0, .129961 },
			{ .580026, .419974, -0.080026 }, { 0, .685020, -0.129961 }, { -.580026, .419974, -0.080026 },
			{ -.580026, -.419974, -0.080026 }, { 0, -.685020, -.129961 }, { .580026, -.419974, -0.080026 },
			{ .370039, .314980, -.5 }, { 0, .5, -.5 }, { -.370039, .314980, -.5 }, { -.370039, -.314980, -.5 },
			{ 0, -.5, -.5 }, { .370039, -.314980, -.5 } };

	/**
	 *
	 */
	private final HE_Mesh dodecahedron;

	/**
	 *
	 */
	private final HE_Mesh tetrakaidecahedron;


	/**
	 *
	 */
	private static int[] colors = new int[] { -65536, -16384, -8519936, -16711870, -16712705, -16761857, -8126209,
			-65351 };

	/**
	 *
	 */
	public HEMC_WeairePhelan() {
		super();
		dodecahedron = new HE_Mesh(new HEC_ConvexHull().setPoints(dodecahedronPoints));
		tetrakaidecahedron = new HE_Mesh(new HEC_ConvexHull().setPoints(tetrakaidecahedronPoints));
		HE_MeshOp.fuseCoplanarFaces(dodecahedron,0.1);
		HE_MeshOp.fuseCoplanarFaces(tetrakaidecahedron,0.1);
		setCrop(false);
	}
	
	protected WB_Point getOrigin() {
		return (WB_Point)parameters.get("origin", new WB_Point());
	}
	
	protected WB_Vector getExtents() {
		return (WB_Vector)parameters.get("extents", new WB_Vector());
	}
	
	protected double[] getScale() {
		return (double[])parameters.get("scale", new double[] {0.0,0.0,0.0});
	}
	
	protected double getSpacing() {
		return parameters.get("spacing", 0.0);
	}
	
	protected int[] getNumberOfUnits() {
		return (int[])parameters.get("uvw", new int[] {1,1,1});
	}
	
	protected boolean[] getCrop() {
		return (boolean[])parameters.get("crop", new boolean[] {false,false,false,false,false,false});
	}
	/**
	 *
	 *
	 * @param p
	 * @return
	 */
	public HEMC_WeairePhelan setOrigin(final WB_Coord p) {
		parameters.set("origin", new WB_Point(p));
		return this;
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	public HEMC_WeairePhelan setExtents(final WB_Vector v) {
		parameters.set("extents", new WB_Vector(v));
		return this;
	}

	/**
	 *
	 *
	 * @param scU
	 * @param scV
	 * @param scW
	 * @return
	 */
	public HEMC_WeairePhelan setScale(final double scU, final double scV, final double scW) {
		parameters.set("scale", new double[] {scU,scV,scW});
		return this;
	}
	
	public HEMC_WeairePhelan setScale(final double sc) {
		parameters.set("scale", new double[] {sc,sc,sc});
		return this;
	}
	
	public HEMC_WeairePhelan setSpacing(final double sp) {
		parameters.set("spacing", sp);
		return this;
	}

	/**
	 *
	 *
	 * @param U
	 * @param V
	 * @param W
	 * @return
	 */
	public HEMC_WeairePhelan setNumberOfUnits(final int U, final int V, final int W) {
		parameters.set("uvw", new int[] {WB_Math.max(1, U),WB_Math.max(1, V), WB_Math.max(1, W)});
		return this;
		
	}

	/**
	 *
	 *
	 * @param crop
	 * @return
	 */
	public HEMC_WeairePhelan setCrop(final boolean crop) {
		parameters.set("crop", new boolean[] {crop,crop,crop,crop,crop,crop});
		return this;
	}

	/**
	 *
	 *
	 * @param cropU
	 * @param cropV
	 * @param cropW
	 * @return
	 */
	public HEMC_WeairePhelan setCrop(final boolean cropU, final boolean cropV, final boolean cropW) {
		parameters.set("crop", new boolean[] {cropU,cropV,cropW,cropU,cropV,cropW});
		return this;
	}

	/**
	 *
	 *
	 * @param cropUm
	 * @param cropVm
	 * @param cropWm
	 * @param cropUp
	 * @param cropVp
	 * @param cropWp
	 * @return
	 */
	public HEMC_WeairePhelan setCrop(final boolean cropUm, final boolean cropVm, final boolean cropWm,
			final boolean cropUp, final boolean cropVp, final boolean cropWp) {
		parameters.set("crop", new boolean[] {cropUm,cropVm,cropWm,cropUp,cropVp,cropWp});
		return this;
	}

	/**
	 *
	 *
	 * @param offset
	 * @return
	 */
	private HE_Mesh[] singleCell(final WB_Vector offset) {
		double[] scale=getScale();
		double spacing=getSpacing();

		final HE_Mesh[] cells = new HE_Mesh[8];
		cells[0] = tetrakaidecahedron.copy();
		cells[0].scaleSelf(1.0-spacing);
		cells[0].moveSelf(0, 0, -.5);
		cells[1] = tetrakaidecahedron.copy();
		cells[1].rotateAboutAxisSelf(0.5 * Math.PI, 0, 0, 0, 0, 0, 1);
		cells[1].scaleSelf(1.0-spacing);
		cells[1].moveSelf(0, 0, 0.5);
		cells[2] = tetrakaidecahedron.copy();
		cells[2].rotateAboutAxisSelf(-0.5 * Math.PI, 0, 0, 0, 0, 1, 0);
		cells[2].scaleSelf(1.0-spacing);
		cells[2].moveSelf(-.5, 1, 1);
		cells[3] = tetrakaidecahedron.copy();
		cells[3].rotateAboutAxisSelf(0.5 * Math.PI, 0, 0, 0, 0, 1, 0);
		cells[3].scaleSelf(1.0-spacing);
		cells[3].moveSelf(.5, 1, 1);
		cells[4] = tetrakaidecahedron.copy();
		cells[4].rotateAboutAxisSelf(0.5 * Math.PI, 0, 0, 0, 1, 0, 0);
		cells[4].scaleSelf(1.0-spacing);
		cells[4].moveSelf(1, .5, 0);
		cells[5] = tetrakaidecahedron.copy();
		cells[5].rotateAboutAxisSelf(-0.5 * Math.PI, 0, 0, 0, 1, 0, 0);
		cells[5].scaleSelf(1.0-spacing);
		cells[5].moveSelf(1, -.5, 0);
		cells[6] = dodecahedron.copy();
		cells[6].scaleSelf(1.0-spacing);
		cells[6].moveSelf(1, 0, 1);
		cells[7] = dodecahedron.copy();
		cells[7].rotateAboutAxisSelf(-0.5 * Math.PI, 0, 0, 0, 0, 1, 0);
		cells[7].scaleSelf(1.0-spacing);
		cells[7].moveSelf(0, 1, 0);
		for (int i = 0; i < 8; i++) {
			cells[i].scaleSelf(0.5 * scale[0], 0.5 * scale[1], 0.5 * scale[2], new WB_Point(0, 0, 0));
			cells[i].moveSelf(offset);
			cells[i].setColor(colors[i]);
		}
		return cells;
	}

	@Override
	void create(final HE_MeshCollection result) {
		double[] scale=getScale();
		WB_Vector extents=getExtents();
		int[] numberOfUnits=getNumberOfUnits();
		if (scale[0] == 0) {
			scale[0] = extents.xd() / numberOfUnits[0];
		}
		if (scale[1] == 0) {
			scale[1] = extents.yd() / numberOfUnits[1];
		}
		if (scale[2] == 0) {
			scale[2] = extents.zd() / numberOfUnits[2];
		}
		HE_Mesh[] tmpCells;
		final ArrayList<WB_Plane> planes = new ArrayList<WB_Plane>(6);
		boolean[] crop=getCrop();
		WB_Point origin=getOrigin();
		final WB_Point start = origin.addMul(-0.5,extents);
		if (crop[0]) {
			planes.add(new WB_Plane(start, new WB_Vector(1, 0, 0)));
		}
		if (crop[1]) {
			planes.add(new WB_Plane(start, new WB_Vector(0, 1, 0)));
		}
		if (crop[2]) {
			planes.add(new WB_Plane(start, new WB_Vector(0, 0, 1)));
		}
		final WB_Point end = origin.addMul(0.5,extents);
		if (crop[3]) {
			planes.add(new WB_Plane(end, new WB_Vector(-1, 0, 0)));
		}
		if (crop[4]) {
			planes.add(new WB_Plane(end, new WB_Vector(0, -1, 0)));
		}
		if (crop[5]) {
			planes.add(new WB_Plane(end, new WB_Vector(0, 0, -1)));
		}
		final HEM_MultiSlice ms = new HEM_MultiSlice().setPlanes(planes);
		for (int i = 0; i < numberOfUnits[0] ; i++) {
			for (int j = 0; j < numberOfUnits[1] ; j++) {
				for (int k = 0; k < numberOfUnits[2] ; k++) {
					final WB_Vector offset = new WB_Vector(origin.xd() + (i-(numberOfUnits[0]-1.0)*0.5 - 0.25) * scale[0], origin.yd() + (j -(numberOfUnits[1]-1.0)*0.5- 0.25) * scale[1],
							origin.zd() + (k -(numberOfUnits[2]-1.0)*0.5- 0.25) * scale[2]);
					tmpCells = singleCell(offset);
					for (int c = 0; c < 8; c++) {
						if (planes.size() > 0) {
							tmpCells[c].modify(ms);
						}
						if (tmpCells[c].getNumberOfVertices() > 0) {
							result.add(tmpCells[c]);
						}
					}
				}
			}
		}

		_numberOfMeshes = result.size();

	}
}