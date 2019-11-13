/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

import wblut.geom.WB_CoordOp;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;

/**
 *
 */
public class HES_DooSabin extends HES_Subdividor {
	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.subdividors.HES_Subdividor#subdivide(wblut.hemesh.HE_Mesh)
	 */
	/**
	 *
	 */
	private double		faceFactor;
	/**
	 *
	 */
	private double		edgeFactor;
	/**
	 *
	 */
	private boolean		absolute;
	/**
	 *
	 */
	private double		d;
	/**
	 *
	 */
	public HE_Selection	faceFaces;
	/**
	 *
	 */
	public HE_Selection	edgeFaces;
	/**
	 *
	 */
	public HE_Selection	vertexFaces;

	/**
	 *
	 */
	public HES_DooSabin() {
		faceFactor = 1.0;
		edgeFactor = 1.0;
	}

	/**
	 *
	 *
	 * @param ff
	 * @param ef
	 * @return
	 */
	public HES_DooSabin setFactors(final double ff, final double ef) {
		faceFactor = ff;
		edgeFactor = ef;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HES_DooSabin setAbsolute(final boolean b) {
		absolute = b;
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HES_DooSabin setDistance(final double d) {
		this.d = d;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HES_Subdividor#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (mesh.selectBoundaryEdges().getNumberOfEdges() > 0) {
			throw new IllegalArgumentException(
					"HES_DooSabin only supports closed meshes at this time.");
		}
		Iterator<HE_Face> fItr = mesh.fItr();
		final LongIntHashMap halfedgeCorrelation = new LongIntHashMap();
		final ArrayList<WB_Point> newVertices = new ArrayList<WB_Point>();
		HE_Face f;
		HE_Halfedge he;
		WB_Point fc;
		int vertexCount = 0;
		double div = 1.0 + 2.0 * edgeFactor + faceFactor;
		if (WB_Epsilon.isZero(div)) {
			div = 1.0;
		}
		if (absolute) {
			div = 4.0;
		}
		while (fItr.hasNext()) {
			f = fItr.next();
			he = f.getHalfedge();
			fc = new WB_Point(HE_MeshOp.getFaceCenter(f));
			do {
				final WB_Point p = fc.mul(faceFactor);
				p.addSelf(he.getVertex());
				p.addSelf(WB_Vector.mul(HE_MeshOp.getHalfedgeCenter(he),
						edgeFactor));
				p.addSelf(WB_Vector.mul(
						HE_MeshOp.getHalfedgeCenter(he.getPrevInFace()),
						edgeFactor));
				p.divSelf(div);
				if (absolute) {
					final double dcurrent = WB_CoordOp.getDistance3D(p,
							he.getVertex());
					p.subSelf(he.getVertex());
					p.mulSelf(d / dcurrent);
					p.addSelf(he.getVertex());
				}
				halfedgeCorrelation.put(he.getKey(), vertexCount);
				vertexCount++;
				newVertices.add(p);
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
		}
		final int[][] faces = new int[mesh.getNumberOfFaces()
				+ mesh.getNumberOfEdges() + mesh.getNumberOfVertices()][];
		final int[] labels = new int[mesh.getNumberOfFaces()
				+ mesh.getNumberOfEdges() + mesh.getNumberOfVertices()];
		final int[] noe = { mesh.getNumberOfFaces(), mesh.getNumberOfEdges(),
				mesh.getNumberOfVertices() };
		int currentFace = 0;
		fItr = mesh.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			faces[currentFace] = new int[f.getFaceDegree()];
			he = f.getHalfedge();
			int i = 0;
			labels[currentFace] = currentFace;
			do {
				faces[currentFace][i] = halfedgeCorrelation.get(he.getKey());
				he = he.getNextInFace();
				i++;
			} while (he != f.getHalfedge());
			currentFace++;
		}
		final Iterator<HE_Halfedge> eItr = mesh.eItr();
		HE_Halfedge e;
		int currentEdge = 0;
		while (eItr.hasNext()) {
			e = eItr.next();
			faces[currentFace] = new int[4];
			faces[currentFace][3] = halfedgeCorrelation.get(e.getKey());
			faces[currentFace][2] = halfedgeCorrelation
					.get(e.getNextInFace().getKey());
			faces[currentFace][1] = halfedgeCorrelation
					.get(e.getPair().getKey());
			faces[currentFace][0] = halfedgeCorrelation
					.get(e.getPair().getNextInFace().getKey());
			labels[currentFace] = currentEdge;
			currentEdge++;
			currentFace++;
		}
		final Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		int currentVertex = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			faces[currentFace] = new int[v.getVertexDegree()];
			he = v.getHalfedge();
			int i = v.getVertexDegree() - 1;
			do {
				faces[currentFace][i] = halfedgeCorrelation.get(he.getKey());
				he = he.getNextInVertex();
				i--;
			} while (he != v.getHalfedge());
			labels[currentFace] = currentVertex;
			currentVertex++;
			currentFace++;
		}
		final HEC_FromFacelist fl = new HEC_FromFacelist().setFaces(faces)
				.setVertices(newVertices).setCheckDuplicateVertices(false);
		mesh.setNoCopy(fl.create());
		fItr = mesh.fItr();
		currentFace = 0;
		faceFaces = HE_Selection.getSelection(mesh);
		edgeFaces = HE_Selection.getSelection(mesh);
		vertexFaces = HE_Selection.getSelection(mesh);
		while (fItr.hasNext()) {
			f = fItr.next();
			f.setInternalLabel(labels[currentFace]);
			if (currentFace < noe[0]) {
				faceFaces.add(f);
			} else if (currentFace < noe[0] + noe[1]) {
				edgeFaces.add(f);
			} else {
				vertexFaces.add(f);
			}
			currentFace++;
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * wblut.hemesh.subdividors.HES_Subdividor#subdivideSelected(wblut.hemesh
	 * .HE_Mesh, wblut.hemesh.HE_Selection)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return selection.getParent();
	}
}
