package wblut.hemesh;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_Crocodile extends HEM_Modifier {
	/**  */
	private static WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	/**  */
	private WB_ScalarParameter distance;
	/**  */
	private WB_ScalarParameter chamfer;

	/**
	 *
	 */
	public HEM_Crocodile() {
		chamfer = new WB_ConstantScalarParameter(0.5);
		distance = WB_ScalarParameter.ZERO;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Crocodile setDistance(final double d) {
		distance = d == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Crocodile setDistance(final WB_ScalarParameter d) {
		distance = d;
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEM_Crocodile setChamfer(final double c) {
		chamfer = c == 0 ? WB_ScalarParameter.ZERO : new WB_ConstantScalarParameter(c);
		return this;
	}

	/**
	 *
	 *
	 * @param c
	 * @return
	 */
	public HEM_Crocodile setChamfer(final WB_ScalarParameter c) {
		chamfer = c;
		return this;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		final HE_Selection selection = mesh.selectAllVertices();
		return apply(selection);
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		final HE_Selection spikes = HE_Selection.getSelection(selection.getParent());
		selection.collectVertices();
		tracker.setStartStatus(this, "Starting HEM_Crocodile.");
		final HE_ObjectMap<WB_Coord> umbrellapoints = new HE_ObjectMap<>();
		HE_VertexIterator vitr = selection.vItr();
		HE_Vertex v;
		if (chamfer == WB_ScalarParameter.ZERO) {
			tracker.setStopStatus(this, "Chamfer is 0, nothing to do. Exiting HEM_Crocodile.");
			return selection.getParent();
		}
		WB_ProgressCounter counter = new WB_ProgressCounter(selection.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Enumerating vertex umbrellas.", counter);
		HE_HalfedgeList star;
		double ch = 0.0;
		while (vitr.hasNext()) {
			v = vitr.next();
			ch = chamfer.evaluate(v.xd(), v.yd(), v.zd());
			if (ch < 0.0) {
				ch *= -1;
			}
			ch = ch - (int) ch;
			if (ch > 0.5) {
				ch = 1.0 - ch;
			}
			if (ch == 0.0) {
				continue;
			} else if (ch == 0.5) {
				star = v.getEdgeStar();
			} else {
				star = v.getHalfedgeStar();
			}
			for (final HE_Halfedge he : star) {
				umbrellapoints.put(he.getKey(), gf.createInterpolatedPoint(he.getVertex(), he.getEndVertex(), ch));
			}
			counter.increment();
		}
		counter = new WB_ProgressCounter(umbrellapoints.size(), 10);
		tracker.setCounterStatus(this, "Splitting edges.", counter);
		for (final long e : umbrellapoints.keySet().toArray()) {
			HE_MeshOp.splitEdge(selection.getParent(), selection.getParent().getEdgeWithKey(e), umbrellapoints.get(e));
			counter.increment();
		}
		counter = new WB_ProgressCounter(selection.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Splitting faces.", counter);
		vitr = selection.vItr();
		while (vitr.hasNext()) {
			v = vitr.next();
			final HE_VertexHalfedgeOutCirculator vhoc = new HE_VertexHalfedgeOutCirculator(v);
			HE_Halfedge he;
			while (vhoc.hasNext()) {
				he = vhoc.next();
				if (he.getFace() != null) {
					spikes.union(HE_MeshOp.splitFace(selection.getParent(), he.getFace(), he.getEndVertex(),
							he.getPrevInVertex().getEndVertex()));
				}
			}
			counter.increment();
			v.getPosition().addMulSelf(distance.evaluate(v.xd(), v.yd(), v.zd()),
					selection.getParent().getVertexNormal(v));
		}
		selection.getParent().addSelection("spikes", this, spikes);
		tracker.setStopStatus(this, "Exiting HEM_Crocodile.");
		return selection.getParent();
	}
}
