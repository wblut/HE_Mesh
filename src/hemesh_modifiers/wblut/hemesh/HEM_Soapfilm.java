package wblut.hemesh;

import java.util.Iterator;
import java.util.List;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Point;

public class HEM_Soapfilm extends HEM_Modifier {
	private HE_Selection fixed;
	private boolean keepBoundary;
	private double lambda;
	private int iter;

	public HEM_Soapfilm() {
		lambda = 0.5;
		iter = 1;
		keepBoundary = false;
		fixed = null;
	}

	public HEM_Soapfilm setFixed(final HE_Selection fixed) {
		this.fixed = fixed;
		return this;
	}

	public HEM_Soapfilm setIterations(final int r) {
		iter = r;
		return this;
	}

	public HEM_Soapfilm setKeepBoundary(final boolean b) {
		keepBoundary = b;
		return this;
	}

	public HEM_Soapfilm setLambda(final double lambda) {
		this.lambda = lambda;
		return this;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_Soapfilm.");
		if (fixed == null || fixed.getParent() != mesh) {
			return mesh;
		}
		fixed.collectVertices();
		final WB_Coord[] newPositions = new WB_Coord[mesh.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		final WB_ProgressCounter counter = new WB_ProgressCounter(iter * mesh.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Smoothing vertices.", counter);
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = mesh.vItr();
			HE_Vertex v;
			List<HE_Vertex> neighbors;
			int id = 0;
			WB_Point p;
			while (vItr.hasNext()) {
				v = vItr.next();
				if (v.isBoundary() && keepBoundary || fixed.contains(v)) {
					newPositions[id] = v;
				} else {
					p = new WB_Point(v).mulSelf(1.0 - lambda);
					neighbors = v.getNeighborVertices();
					for (int i = 0; i < neighbors.size(); i++) {
						p.addMulSelf(lambda / neighbors.size(), neighbors.get(i));
					}
					newPositions[id] = p;
				}
				id++;
			}
			vItr = mesh.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
				counter.increment();
			}
		}
		tracker.setStopStatus(this, "Exiting HEM_Soapfilm.");
		return mesh;
	}

	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_Soapfilm.");
		if (fixed == null || fixed.getParent() != selection.getParent()) {
			return selection.getParent();
		}
		fixed.collectVertices();
		selection.collectVertices();
		final WB_Coord[] newPositions = new WB_Coord[selection.getNumberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		final WB_ProgressCounter counter = new WB_ProgressCounter(iter * selection.getNumberOfVertices(), 10);
		tracker.setCounterStatus(this, "Smoothing vertices.", counter);
		for (int r = 0; r < iter; r++) {
			Iterator<HE_Vertex> vItr = selection.vItr();
			HE_Vertex v;
			HE_Vertex n;
			List<HE_Vertex> neighbors;
			int id = 0;
			while (vItr.hasNext()) {
				v = vItr.next();
				final WB_Point p = new WB_Point();
				if (v.isBoundary() && keepBoundary || fixed.contains(v)) {
					newPositions[id] = v;
				} else {
					neighbors = v.getNeighborVertices();
					final Iterator<HE_Vertex> nItr = neighbors.iterator();
					while (nItr.hasNext()) {
						n = nItr.next();
						if (!selection.contains(n)) {
							nItr.remove();
						}
					}
					for (int i = 0; i < neighbors.size(); i++) {
						p.addMulSelf(lambda / neighbors.size(), neighbors.get(i));
					}
					newPositions[id] = p.addMulSelf(1.0 - lambda, v);
				}
				id++;
			}
			vItr = selection.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
				counter.increment();
			}
		}
		tracker.setStopStatus(this, "Exiting HEM_Soapfilm.");
		return selection.getParent();
	}
}
