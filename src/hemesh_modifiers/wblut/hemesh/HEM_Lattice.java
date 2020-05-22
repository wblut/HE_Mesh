package wblut.hemesh;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_FactorScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEM_Lattice extends HEM_Modifier {
	/**  */
	private static final WB_GeometryFactory3D gf = new WB_GeometryFactory3D();
	/**  */
	private WB_ScalarParameter d;
	/**  */
	private WB_ScalarParameter sew;
	/**  */
	private WB_ScalarParameter hew;
	/**  */
	private double thresholdAngle;
	/**  */
	private boolean fuse;
	/**  */
	private double fuseAngle;
	/**  */
	private WB_ScalarParameter ibulge, obulge;

	/**
	 *
	 */
	public HEM_Lattice() {
		super();
		d = null;
		sew = null;
		thresholdAngle = -1;
		fuseAngle = Math.PI / 36;
		fuse = false;
		ibulge = obulge = new WB_ConstantScalarParameter(0);
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Lattice setDepth(final double d) {
		this.d = new WB_ConstantScalarParameter(-d);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Lattice setDepth(final WB_ScalarParameter d) {
		this.d = new WB_FactorScalarParameter(-1.0, d);
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @return
	 */
	public HEM_Lattice setWidth(final double w) {
		sew = new WB_ConstantScalarParameter(0.5 * w);
		hew = new WB_ConstantScalarParameter(w);
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @return
	 */
	public HEM_Lattice setWidth(final WB_ScalarParameter w) {
		sew = new WB_FactorScalarParameter(0.5, w);
		hew = w;
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @param hew
	 * @return
	 */
	public HEM_Lattice setWidth(final double w, final double hew) {
		sew = new WB_ConstantScalarParameter(0.5 * w);
		this.hew = new WB_ConstantScalarParameter(hew);
		return this;
	}

	/**
	 *
	 *
	 * @param w
	 * @param hew
	 * @return
	 */
	public HEM_Lattice setWidth(final WB_ScalarParameter w, final WB_ScalarParameter hew) {
		sew = new WB_FactorScalarParameter(0.5, w);
		this.hew = hew;
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Lattice setBulge(final double d) {
		ibulge = new WB_ConstantScalarParameter(d);
		obulge = new WB_ConstantScalarParameter(d);
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEM_Lattice setBulge(final WB_ScalarParameter d) {
		ibulge = d;
		obulge = d;
		return this;
	}

	/**
	 *
	 *
	 * @param inner
	 * @param outer
	 * @return
	 */
	public HEM_Lattice setBulge(final double inner, final double outer) {
		ibulge = new WB_ConstantScalarParameter(inner);
		obulge = new WB_ConstantScalarParameter(outer);
		return this;
	}

	/**
	 *
	 *
	 * @param inner
	 * @param outer
	 * @return
	 */
	public HEM_Lattice setBulge(final WB_ScalarParameter inner, final WB_ScalarParameter outer) {
		ibulge = inner;
		obulge = outer;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEM_Lattice setFuse(final boolean b) {
		fuse = b;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_Lattice setThresholdAngle(final double a) {
		thresholdAngle = a;
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEM_Lattice setFuseAngle(final double a) {
		fuseAngle = a;
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
		tracker.setStartStatus(this, "Starting HEM_Lattice.");
		if (d == null) {
			tracker.setStopStatus(this,
					"Can't create with zero thickness, use HEM_PunchHoles instead. Exiting HEM_Lattice.");
			return mesh;
		}
		if (sew == null) {
			tracker.setStopStatus(this, "Can't create with zero width. Exiting HEM_Lattice.");
			return mesh;
		}
		final HEM_Extrude extm = new HEM_Extrude().setDistance(0).setRelative(false).setChamfer(sew).setFuse(fuse)
				.setHardEdgeChamfer(hew).setFuseAngle(fuseAngle).setThresholdAngle(thresholdAngle);
		mesh.modify(extm);
		tracker.setDuringStatus(this, "Creating inner mesh.");
		final HEC_Copy cc = new HEC_Copy().setMesh(mesh);
		final HE_Mesh innerMesh = cc.create();
		final HE_LongMap allheCorrelation = cc.halfedgeCorrelation;
		WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfFaces(), 10);
		tracker.setCounterStatus(this, "Creating face correlations.", counter);
		final HashMap<Long, Long> faceCorrelation = new HashMap<>();
		final HE_FaceIterator fItr1 = mesh.fItr();
		final HE_FaceIterator fItr2 = innerMesh.fItr();
		HE_Face f1;
		HE_Face f2;
		while (fItr1.hasNext()) {
			f1 = fItr1.next();
			f2 = fItr2.next();
			faceCorrelation.put(f1.getKey(), f2.getKey());
			counter.increment();
		}
		counter = new WB_ProgressCounter(mesh.getNumberOfHalfedges(), 10);
		tracker.setCounterStatus(this, "Creating boundary halfedge correlations.", counter);
		final HashMap<Long, Long> heCorrelation = new HashMap<>();
		HE_Halfedge he1;
		HE_Halfedge he2;
		final long[] keys = allheCorrelation.keySet().toArray();
		final long[] values = allheCorrelation.values().toArray();
		for (int i = 0; i < keys.length; i++) {
			he1 = mesh.getHalfedgeWithKey(keys[i]);
			if (he1.getFace() == null) {
				he2 = innerMesh.getHalfedgeWithKey(values[i]);
				heCorrelation.put(he1.getKey(), he2.getKey());
			}
			counter.increment();
		}
		tracker.setDuringStatus(this, "Shrinking inner mesh.");
		final HEM_VertexExpand expm = new HEM_VertexExpand().setDistance(d);
		expm.applySelf(innerMesh);
		final HEM_FlipFaces ff = new HEM_FlipFaces();
		ff.applySelf(innerMesh);
		final int nf = mesh.getNumberOfFaces();
		final HE_Face[] origFaces = mesh.getFacesAsArray();
		mesh.selectAllFaces("outer");
		innerMesh.selectAllFaces("inner");
		mesh.add(innerMesh);
		HE_Face fo;
		HE_Face fi;
		HE_HalfedgeList hei;
		HE_HalfedgeList heo;
		WB_Point[] viPos;
		WB_Polygon poly;
		HE_Halfedge heoc, heic, heon, hein, heio, heoi;
		HE_Face fNew;
		WB_Coord ni;
		WB_Coord no;
		counter = new WB_ProgressCounter(nf, 10);
		WB_Coord co, ci;
		double ob, ib;
		final HE_Selection connect = HE_Selection.getSelection(mesh);
		final HE_Selection boundary = HE_Selection.getSelection(mesh);
		tracker.setCounterStatus(this, "Connecting outer and inner faces.", counter);
		for (int i = 0; i < nf; i++) {
			fo = origFaces[i];
			final Long innerKey = faceCorrelation.get(fo.getKey());
			if (mesh.getSelection("extruded").contains(fo)) {
				fi = mesh.getFaceWithKey(innerKey);
				co = mesh.getFaceCenter(fo);
				if ((ob = obulge.evaluate(co.xd(), co.yd(), co.zd())) != 0) {
					no = mesh.getFaceNormal(fo);
					fo.move(WB_Vector.mul(no, ob));
				}
				ci = mesh.getFaceCenter(fi);
				if ((ib = ibulge.evaluate(ci.xd(), ci.yd(), ci.zd())) != 0) {
					ni = mesh.getFaceNormal(fi);
					fi.move(WB_Vector.mul(ni, ib));
				}
				final int nvo = fo.getFaceDegree();
				final int nvi = fi.getFaceDegree();
				hei = fi.getFaceHalfedges();
				viPos = new WB_Point[nvi];
				for (int j = 0; j < nvi; j++) {
					viPos[j] = new WB_Point(hei.get(j).getVertex());
				}
				poly = gf.createSimplePolygon(viPos);
				heo = fo.getFaceHalfedges();
				for (int j = 0; j < nvo; j++) {
					heoc = heo.get(j);
					heon = heo.get((j + 1) % nvo);
					final int cic = poly.closestIndex(heoc.getVertex());
					final int cin = poly.closestIndex(heon.getVertex());
					heic = hei.get(cin);
					hein = hei.get(cic);
					heio = new HE_Halfedge();
					heoi = new HE_Halfedge();
					fNew = new HE_Face();
					connect.add(fNew);
					mesh.setVertex(heoi, heon.getVertex());
					mesh.setVertex(heio, hein.getVertex());
					mesh.setNext(heoc, heoi);
					mesh.setFace(heoc, fNew);
					if (cic == cin) {
						mesh.setNext(heoi, heio);
						mesh.setFace(heoi, fNew);
					} else {
						mesh.setNext(heoi, heic);
						mesh.setFace(heoi, fNew);
						mesh.setNext(heic, heio);
						mesh.setFace(heic, fNew);
					}
					mesh.setNext(heio, heoc);
					mesh.setFace(heio, fNew);
					mesh.setHalfedge(fNew, heoc);
					mesh.add(heio);
					mesh.add(heoi);
					mesh.add(fNew);
					mesh.remove(fo);
					mesh.remove(fi);
				}
			}
			counter.increment();
		}
		counter = new WB_ProgressCounter(heCorrelation.size(), 10);
		tracker.setCounterStatus(this, "Connecting outer and inner boundaries.", counter);
		final Iterator<Map.Entry<Long, Long>> it = heCorrelation.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<Long, Long> pairs = it.next();
			he1 = mesh.getHalfedgeWithKey(pairs.getKey());
			he2 = mesh.getHalfedgeWithKey(pairs.getValue());
			heio = new HE_Halfedge();
			heoi = new HE_Halfedge();
			mesh.add(heio);
			mesh.add(heoi);
			mesh.setVertex(heio, he1.getPair().getVertex());
			mesh.setVertex(heoi, he2.getPair().getVertex());
			mesh.setNext(he1, heio);
			mesh.setNext(heio, he2);
			mesh.setNext(he2, heoi);
			mesh.setNext(heoi, he1);
			fNew = new HE_Face();
			boundary.add(fNew);
			mesh.add(fNew);
			mesh.setHalfedge(fNew, he1);
			mesh.setFace(he1, fNew);
			mesh.setFace(he2, fNew);
			mesh.setFace(heio, fNew);
			mesh.setFace(heoi, fNew);
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(mesh);
		mesh.addSelection("connect", this, connect);
		mesh.addSelection("boundary", this, boundary);
		tracker.setStopStatus(this, "Exiting HEM_Lattice.");
		return mesh;
	}

	/**
	 *
	 *
	 * @param selection
	 * @return
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_Lattice.");
		if (d == null) {
			tracker.setStopStatus(this,
					"Can't create with zero thickness, use HEM_PunchHoles instead. Exiting HEM_Lattice.");
			return selection.getParent();
		}
		if (sew == null) {
			tracker.setStopStatus(this, "Can't create with zero width. Exiting HEM_Lattice.");
			return selection.getParent();
		}
		final HE_Mesh mesh = selection.getParent();
		final HEM_Extrude extm = new HEM_Extrude().setDistance(0).setRelative(false).setChamfer(sew).setFuse(fuse)
				.setHardEdgeChamfer(hew).setFuseAngle(fuseAngle).setThresholdAngle(thresholdAngle);
		selection.modify(extm);
		tracker.setDuringStatus(this, "Creating inner mesh.");
		final HEC_Copy cc = new HEC_Copy().setMesh(mesh);
		final HE_Mesh innerMesh = cc.create();
		final HE_LongMap allheCorrelation = cc.halfedgeCorrelation;
		WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfFaces(), 10);
		tracker.setCounterStatus(this, "Creating face correlations.", counter);
		final HashMap<Long, Long> faceCorrelation = new HashMap<>();
		final HE_FaceIterator fItr1 = mesh.fItr();
		final HE_FaceIterator fItr2 = innerMesh.fItr();
		HE_Face f1;
		HE_Face f2;
		while (fItr1.hasNext()) {
			f1 = fItr1.next();
			f2 = fItr2.next();
			faceCorrelation.put(f1.getKey(), f2.getKey());
			counter.increment();
		}
		counter = new WB_ProgressCounter(mesh.getNumberOfHalfedges(), 10);
		tracker.setCounterStatus(this, "Creating boundary halfedge correlations.", counter);
		final HashMap<Long, Long> heCorrelation = new HashMap<>();
		HE_Halfedge he1;
		HE_Halfedge he2;
		final long[] keys = allheCorrelation.keySet().toArray();
		final long[] values = allheCorrelation.values().toArray();
		for (int i = 0; i < keys.length; i++) {
			he1 = mesh.getHalfedgeWithKey(keys[i]);
			if (he1.getFace() == null) {
				he2 = innerMesh.getHalfedgeWithKey(values[i]);
				heCorrelation.put(he1.getKey(), he2.getKey());
			}
			counter.increment();
		}
		tracker.setDuringStatus(this, "Shrinking inner mesh.");
		final HEM_VertexExpand expm = new HEM_VertexExpand().setDistance(d);
		expm.applySelf(innerMesh);
		final HEM_FlipFaces ff = new HEM_FlipFaces();
		ff.applySelf(innerMesh);
		final int nf = mesh.getNumberOfFaces();
		final HE_Face[] origFaces = mesh.getFacesAsArray();
		mesh.selectAllFaces("outer");
		innerMesh.selectAllFaces("inner");
		mesh.add(innerMesh);
		HE_Face fo;
		HE_Face fi;
		HE_HalfedgeList hei;
		HE_HalfedgeList heo;
		WB_Point[] viPos;
		WB_Polygon poly;
		HE_Halfedge heoc, heic, heon, hein, heio, heoi;
		HE_Face fNew;
		WB_Coord ni, no;
		counter = new WB_ProgressCounter(nf, 10);
		WB_Coord co, ci;
		double ob, ib;
		final HE_Selection connect = HE_Selection.getSelection(mesh);
		final HE_Selection boundary = HE_Selection.getSelection(mesh);
		tracker.setCounterStatus(this, "Connecting outer and inner faces.", counter);
		for (int i = 0; i < nf; i++) {
			fo = origFaces[i];
			final Long innerKey = faceCorrelation.get(fo.getKey());
			if (mesh.getSelection("extruded").contains(fo)) {
				fi = mesh.getFaceWithKey(innerKey);
				co = mesh.getFaceCenter(fo);
				if ((ob = obulge.evaluate(co.xd(), co.yd(), co.zd())) != 0) {
					no = mesh.getFaceNormal(fo);
					fo.move(WB_Vector.mul(no, ob));
				}
				ci = mesh.getFaceCenter(fi);
				if ((ib = ibulge.evaluate(ci.xd(), ci.yd(), ci.zd())) != 0) {
					ni = mesh.getFaceNormal(fi);
					fi.move(WB_Vector.mul(ni, ib));
				}
				final int nvo = fo.getFaceDegree();
				final int nvi = fi.getFaceDegree();
				hei = fi.getFaceHalfedges();
				viPos = new WB_Point[nvi];
				for (int j = 0; j < nvi; j++) {
					viPos[j] = new WB_Point(hei.get(j).getVertex());
				}
				poly = gf.createSimplePolygon(viPos);
				heo = fo.getFaceHalfedges();
				for (int j = 0; j < nvo; j++) {
					heoc = heo.get(j);
					heon = heo.get((j + 1) % nvo);
					final int cic = poly.closestIndex(heoc.getVertex());
					final int cin = poly.closestIndex(heon.getVertex());
					heic = hei.get(cin);
					hein = hei.get(cic);
					heio = new HE_Halfedge();
					heoi = new HE_Halfedge();
					fNew = new HE_Face();
					mesh.setVertex(heoi, heon.getVertex());
					mesh.setVertex(heio, hein.getVertex());
					mesh.setNext(heoc, heoi);
					mesh.setFace(heoc, fNew);
					if (cic == cin) {
						mesh.setNext(heoi, heio);
						mesh.setFace(heoi, fNew);
					} else {
						mesh.setNext(heoi, heic);
						mesh.setFace(heoi, fNew);
						mesh.setNext(heic, heio);
						mesh.setFace(heic, fNew);
					}
					mesh.setNext(heio, heoc);
					mesh.setFace(heio, fNew);
					mesh.setHalfedge(fNew, heoc);
					mesh.add(heio);
					mesh.add(heoi);
					mesh.add(fNew);
					connect.add(fNew);
					mesh.remove(fo);
					mesh.remove(fi);
				}
			}
			counter.increment();
		}
		counter = new WB_ProgressCounter(heCorrelation.size(), 10);
		tracker.setCounterStatus(this, "Connecting outer and inner boundaries.", counter);
		final Iterator<Map.Entry<Long, Long>> it = heCorrelation.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<Long, Long> pairs = it.next();
			he1 = mesh.getHalfedgeWithKey(pairs.getKey());
			he2 = mesh.getHalfedgeWithKey(pairs.getValue());
			heio = new HE_Halfedge();
			heoi = new HE_Halfedge();
			mesh.add(heio);
			mesh.add(heoi);
			mesh.setVertex(heio, he1.getPair().getVertex());
			mesh.setVertex(heoi, he2.getPair().getVertex());
			mesh.setNext(he1, heio);
			mesh.setNext(heio, he2);
			mesh.setNext(he2, heoi);
			mesh.setNext(heoi, he1);
			fNew = new HE_Face();
			boundary.add(fNew);
			mesh.add(fNew);
			mesh.setHalfedge(fNew, he1);
			mesh.setFace(he1, fNew);
			mesh.setFace(he2, fNew);
			mesh.setFace(heio, fNew);
			mesh.setFace(heoi, fNew);
			counter.increment();
		}
		HE_MeshOp.pairHalfedges(mesh);
		mesh.addSelection("connect", this, connect);
		mesh.addSelection("boundary", this, boundary);
		tracker.setStopStatus(this, "Exiting HEM_Lattice.");
		return mesh;
	}
}
