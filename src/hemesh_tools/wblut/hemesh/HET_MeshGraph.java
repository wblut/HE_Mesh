package wblut.hemesh;

import java.util.List;

import wblut.geom.WB_Coord;
import wblut.geom.WB_Network;
import wblut.geom.WB_SimpleMesh;
import wblut.geom.WB_Triangulation2D;
import wblut.geom.WB_Triangulation3D;

public class HET_MeshGraph extends HET_MeshNetwork {
	/**
	 *
	 *
	 * @param mesh
	 */
	public HET_MeshGraph(final WB_SimpleMesh mesh) {
		super(mesh);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HET_MeshGraph(final HE_Mesh mesh) {
		super(mesh);
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param offset
	 */
	public HET_MeshGraph(final HE_Mesh mesh, final double offset) {
		super(mesh, offset);
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulation
	 */
	public HET_MeshGraph(final List<? extends WB_Coord> points, final WB_Triangulation3D triangulation) {
		super(points, triangulation);
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulation
	 */
	public HET_MeshGraph(final WB_Coord[] points, final WB_Triangulation3D triangulation) {
		super(points, triangulation);
	}

	/**
	 *
	 *
	 * @param points
	 * @param triangulation
	 */
	public HET_MeshGraph(final WB_Coord[] points, final WB_Triangulation2D triangulation) {
		super(points, triangulation);
	}

	/**
	 *
	 *
	 * @param network
	 */
	public HET_MeshGraph(final WB_Network network) {
		super(network);
	}
}
