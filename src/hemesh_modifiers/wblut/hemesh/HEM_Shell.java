/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Map;

import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_FactorScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 * Turns a solid into a rudimentary shelled structure.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Shell extends HEM_Modifier {
	/**
	 *
	 */
	private WB_ScalarParameter d;
	private boolean twosided;

	/**
	 *
	 */
	public HEM_Shell() {
		super();
		d = WB_ScalarParameter.ZERO;
		twosided=false;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Shell setThickness(final double d) {
		this.d = d == 0.0 ? WB_ScalarParameter.ZERO
				: new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Shell setThickness(final WB_ScalarParameter d) {
		this.d = d;
		return this;
	}
	
	public HEM_Shell setTwoSided(final boolean b) {
		twosided=b;
		return this;
	}


	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		if (d == WB_ScalarParameter.ZERO) {
			return mesh;
		}
		
		HEC_Copy cc = new HEC_Copy().setMesh(mesh);
		final HE_Mesh innerMesh = cc.create();
		
		Map<Long,Long> heCorrelation = cc.halfedgeCorrelation;
		HEM_VertexExpand expm;
		if(twosided) {
			expm= new HEM_VertexExpand()
					.setDistance(new WB_FactorScalarParameter(-0.5, d));
		}else{
		 expm= new HEM_VertexExpand()
				.setDistance(new WB_FactorScalarParameter(-1.0, d));
		}
		innerMesh.modify(expm);
		mesh.selectAllFaces("outer");
		if(twosided) {
			expm= new HEM_VertexExpand()
					.setDistance(new WB_FactorScalarParameter(0.5, d));
			mesh.modify(expm);
		}
		HE_MeshOp.flipFaces(innerMesh);
		innerMesh.selectAllFaces("inner");
		mesh.add(innerMesh);
		HE_Halfedge he1, he2, heio, heoi;
		HE_Face fNew;
		Object[] keys = heCorrelation.keySet().toArray();
		Object[] values =heCorrelation.values().toArray();
		HE_Selection sel = mesh.getNewSelection("boundary");
		for (int i = 0; i < keys.length; i++) {
			he1 = mesh.getHalfedgeWithKey((Long)keys[i]);
			if (he1.isOuterBoundary()) {
				he2 = mesh.getHalfedgeWithKey((Long)values[i]);
				heio = new HE_Halfedge();
				heoi = new HE_Halfedge();
				mesh.setVertex(heio, he1.getPair().getVertex());
				heio.setUVW(he1.getPair().getVertex()
						.getUVW(he1.getPair().getFace()));
				mesh.setVertex(heoi, he2.getPair().getVertex());
				heoi.setUVW(he2.getPair().getVertex()
						.getUVW(he2.getPair().getFace()));
				mesh.setNext(he1, heio);
				mesh.setNext(heio, he2);
				mesh.setNext(he2, heoi);
				mesh.setNext(heoi, he1);
				fNew = new HE_Face();
				fNew.setInternalLabel(1);
				mesh.add(fNew);
				sel.add(fNew);
				mesh.setHalfedge(fNew, he1);
				mesh.setFace(he1, fNew);
				mesh.setFace(he2, fNew);
				mesh.setFace(heio, fNew);
				mesh.setFace(heoi, fNew);
				mesh.add(heio);
				mesh.add(heoi);
			}
		}
		HE_MeshOp.pairHalfedges(mesh);
		HE_MeshOp.capHalfedges(mesh);
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		return applySelf(selection.getParent());
	}
}
