/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.List;
import java.util.Map;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.primitive.LongDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordOp;
import wblut.geom.WB_GeometryOp;
import wblut.geom.WB_IntersectionResult;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_Epsilon;
import wblut.math.WB_ScalarParameter;

/**
 * Extrudes and scales a face along its face normal.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HEM_Extrude extends HEM_Modifier {
	/** Extrusion distance. */
	private WB_ScalarParameter			d;
	/** Threshold angle for hard edges. */
	private double						thresholdAngle;
	/** Chamfer factor or distance. */
	private WB_ScalarParameter			chamfer;
	/** Hard edge chamfer distance. */
	private WB_ScalarParameter			hardEdgeChamfer;
	/** Extrusion mode. */
	private boolean						relative;
	/** Fuse coplanar faces. */
	private boolean						fuse;
	/** Turn non-extrudable faces into spiked faces?. */
	private boolean						peak;
	/** Limit angle for face fusion. */
	private double						fuseAngle;
	/** Vertex normals. */
	private Map<Long, WB_Vector>			_faceNormals;
	/** Halfedge normals. */
	private LongObjectHashMap<WB_Vector>	_halfedgeNormals;
	/** Extrusion widths. */
	private LongDoubleHashMap			_halfedgeEWs;
	/** Face centers. */
	private Map<Long, WB_Point>			_faceCenters;
	/**
	 *
	 */
	private double[]					heights;
	private HE_Selection				walls;
	private HE_Selection				extruded;
	private HE_Selection				peaks;
	private HE_Selection				fused;
	private List<HE_Face>				failedFaces;
	private List<Double>				failedHeights;
	boolean								isFlat, isSpiky, isStraight;

	/**
	 * Instantiates a new HEM_Extrude.
	 */
	public HEM_Extrude() {
		super();
		d = new WB_ConstantScalarParameter(0.0);
		isFlat = true;
		isSpiky = false;
		isStraight = true;
		thresholdAngle = Math.PI;
		chamfer = new WB_ConstantScalarParameter(0.0);
		hardEdgeChamfer = new WB_ConstantScalarParameter(0.0);
		relative = true;
		fuseAngle = Math.PI / 36;
		heights = null;
	}

	/**
	 * Set extrusion distance.
	 *
	 * @param d
	 *            extrusion distance
	 * @return self
	 */
	public HEM_Extrude setDistance(final double d) {
		this.d = WB_Epsilon.isZero(d) ? WB_ScalarParameter.ZERO
				: new WB_ConstantScalarParameter(d);
		isFlat = WB_Epsilon.isZero(d);
		return this;
	}

	/**
	 * Sets the distance.
	 *
	 * @param d
	 * @return self
	 */
	public HEM_Extrude setDistance(final WB_ScalarParameter d) {
		this.d = d;
		isFlat = false;
		return this;
	}

	/**
	 * Set chamfer factor.
	 *
	 * @param c
	 * @return self
	 */
	public HEM_Extrude setChamfer(final double c) {
		chamfer = new WB_ConstantScalarParameter(c);
		isSpiky = relative && WB_Epsilon.isZero(1.0 - c);
		isStraight = WB_Epsilon.isZero(c);
		return this;
	}

	public HEM_Extrude setChamfer(final WB_ScalarParameter c) {
		chamfer = c;
		isSpiky = false;
		isStraight = false;
		return this;
	}

	/**
	 * Set hard edge chamfer distance
	 *
	 * @param c
	 * @return self
	 */
	public HEM_Extrude setHardEdgeChamfer(final double c) {
		hardEdgeChamfer = new WB_ConstantScalarParameter(c);
		return this;
	}

	public HEM_Extrude setHardEdgeChamfer(final WB_ScalarParameter c) {
		hardEdgeChamfer = c;
		return this;
	}

	/**
	 * Set chamfer mode.
	 *
	 * @param relative
	 *            true/false
	 * @return self
	 */
	public HEM_Extrude setRelative(final boolean relative) {
		this.relative = relative;
		return this;
	}

	/**
	 * Set fuse option: merges coplanar faces.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEM_Extrude setFuse(final boolean b) {
		fuse = b;
		return this;
	}

	/**
	 * Set peak option.
	 *
	 * @param b
	 *            true, false
	 * @return self
	 */
	public HEM_Extrude setPeak(final boolean b) {
		peak = b;
		return this;
	}

	/**
	 * Set threshold angle for hard edge.
	 *
	 * @param a
	 *            threshold angle
	 * @return self
	 */
	public HEM_Extrude setThresholdAngle(final double a) {
		thresholdAngle = a;
		return this;
	}

	/**
	 * Set threshold angle for fuse.
	 *
	 * @param a
	 *            threshold angle
	 * @return self
	 */
	public HEM_Extrude setFuseAngle(final double a) {
		fuseAngle = a;
		return this;
	}

	/**
	 *
	 *
	 * @param distances
	 * @return
	 */
	public HEM_Extrude setDistances(final double[] distances) {
		this.heights = distances;
		return this;
	}

	/**
	 *
	 *
	 * @param distances
	 * @return
	 */
	public HEM_Extrude setDistances(final float[] distances) {
		heights = new double[distances.length];
		for (int i = 0; i < distances.length; i++) {
			heights[i] = distances[i];
		}
		return this;
	}

	/**
	 *
	 *
	 * @param distances
	 * @return
	 */
	public HEM_Extrude setDistances(final int[] distances) {
		heights = new double[distances.length];
		for (int i = 0; i < distances.length; i++) {
			heights[i] = distances[i];
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Mesh mesh) {
		tracker.setStartStatus(this, "Starting HEM_Extrude.");
		mesh.resetFaceInternalLabels();
		walls = HE_Selection.getSelection(mesh);
		extruded = HE_Selection.getSelection(mesh);
		peaks = HE_Selection.getSelection(mesh);
		fused = HE_Selection.getSelection(mesh);
		if (chamfer == null && d == null && heights == null) {
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return mesh;
		}
		HE_Face f;
		HE_Halfedge he;
		WB_Coord c;
		final List<HE_Face> faces = mesh.getFaces();
		_faceNormals = mesh.getKeyedFaceNormals();
		_faceCenters = mesh.getKeyedFaceCenters();
		final int nf = faces.size();
		WB_ProgressCounter counter = new WB_ProgressCounter(nf, 10);
		tracker.setCounterStatus(this,
				"Collecting halfedge information per face.", counter);
		_halfedgeNormals = new LongObjectHashMap<WB_Vector>();
		_halfedgeEWs = new LongDoubleHashMap();
		for (int i = 0; i < nf; i++) {
			f = faces.get(i);
			c = _faceCenters.get(f.getKey());
			he = f.getHalfedge();
			do {
				_halfedgeNormals.put(he.getKey(),
						HE_MeshOp.getHalfedgeNormal(he));
				_halfedgeEWs.put(he.getKey(),
						HE_MeshOp.getHalfedgeDihedralAngle(he) < thresholdAngle
								? chamfer.evaluate(c.xd(), c.yd(), c.zd())
								: hardEdgeChamfer.evaluate(c.xd(), c.yd(),
										c.zd()));
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			counter.increment();
		}
		if (isStraight) {
			applyStraight(mesh, mesh.getFaces());
			HET_Texture.cleanUVW(mesh);
			mesh.addSelection("extruded", this, extruded);
			mesh.addSelection("walls", this, walls);
			mesh.addSelection("peaks", this, peaks);
			mesh.addSelection("fused", this, fused);
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return mesh;
		}
		final List<HE_Face> facelist = mesh.getFaces();
		if (isSpiky) {
			applyPeaked(mesh, facelist);
			HET_Texture.cleanUVW(mesh);
			mesh.addSelection("extruded", this, extruded);
			mesh.addSelection("walls", this, walls);
			mesh.addSelection("peaks", this, peaks);
			mesh.addSelection("fused", this, fused);
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return mesh;
		}
		failedFaces = new FastList<HE_Face>();
		failedHeights = new FastList<Double>();
		applyFlat(mesh, faces, isFlat && fuse);
		if (heights != null) {
			for (int i = 0; i < failedHeights.size(); i++) {
				heights[facelist.indexOf(failedFaces.get(i))] = failedHeights
						.get(i);
			}
		}
		if (peak) {
			applyPeaked(mesh, failedFaces);
		}
		WB_Coord n;
		if (!isFlat) {
			if (heights != null) {
				if (heights.length == faces.size()) {
					for (int i = 0; i < faces.size(); i++) {
						f = faces.get(i);
						if (!failedFaces.contains(f)) {
							n = _faceNormals.get(f.getKey());
							he = f.getHalfedge();
							do {
								he.getVertex().getPosition()
										.addMulSelf(heights[i], n);
								he = he.getNextInFace();
							} while (he != f.getHalfedge());
						}
					}
				} else {
					throw new IllegalArgumentException(
							"Length of heights array does not correspond to number of extruded faces.");
				}
			} else {
				for (int i = 0; i < nf; i++) {
					f = faces.get(i);
					if (!failedFaces.contains(f)) {
						n = _faceNormals.get(f.getKey());
						he = f.getHalfedge();
						do {
							final HE_Vertex v = he.getVertex();
							v.getPosition().addMulSelf(
									d.evaluate(v.xd(), v.yd(), v.zd()), n);
							he = he.getNextInFace();
						} while (he != f.getHalfedge());
					}
				}
			}
		}
		HET_Texture.cleanUVW(mesh);
		mesh.addSelection("extruded", this, extruded);
		mesh.addSelection("walls", this, walls);
		mesh.addSelection("peaks", this, peaks);
		mesh.addSelection("fused", this, fused);
		tracker.setStopStatus(this, "Exiting HEM_Extrude.");
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HE_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	protected HE_Mesh applySelf(final HE_Selection selection) {
		tracker.setStartStatus(this, "Starting HEM_Extrude.");
		selection.getParent().resetFaceInternalLabels();
		walls = HE_Selection.getSelection(selection.getParent());
		extruded = HE_Selection.getSelection(selection.getParent());
		peaks = HE_Selection.getSelection(selection.getParent());
		fused = HE_Selection.getSelection(selection.getParent());
		if (selection.getNumberOfFaces() == 0) {
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return selection.getParent();
		}
		_halfedgeNormals = new LongObjectHashMap<WB_Vector>();
		_halfedgeEWs = new LongDoubleHashMap();
		if (chamfer == null && isFlat && heights == null) {
			return selection.getParent();
		}
		HE_Face f;
		HE_Halfedge he;
		WB_Coord c;
		final List<HE_Face> selFaces = selection.getFaces();
		_faceNormals = selection.getParent().getKeyedFaceNormals();
		_faceCenters = selection.getParent().getKeyedFaceCenters();
		final int nf = selFaces.size();
		WB_ProgressCounter counter = new WB_ProgressCounter(nf, 10);
		tracker.setCounterStatus(this,
				"Collecting halfedge information per face.", counter);
		for (int i = 0; i < nf; i++) {
			f = selFaces.get(i);
			he = f.getHalfedge();
			c = _faceCenters.get(f.getKey());
			do {
				_halfedgeNormals.put(he.getKey(),
						HE_MeshOp.getHalfedgeNormal(he));
				_halfedgeEWs.put(he.getKey(),
						HE_MeshOp.getHalfedgeDihedralAngle(he) < thresholdAngle
								? chamfer.evaluate(c.xd(), c.yd(), c.zd())
								: hardEdgeChamfer.evaluate(c.xd(), c.yd(),
										c.zd()));
				he = he.getNextInFace();
			} while (he != f.getHalfedge());
			counter.increment();
		}
		if (isStraight) {
			applyStraight(selection.getParent(), selFaces);
			HET_Texture.cleanUVW(selection.getParent());
			selection.getParent().addSelection("extruded", this, extruded);
			selection.getParent().addSelection("walls", this, walls);
			selection.getParent().addSelection("peaks", this, peaks);
			selection.getParent().addSelection("fused", this, fused);
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return selection.getParent();
		}
		if (isSpiky) {
			applyPeaked(selection.getParent(), selFaces);
			HET_Texture.cleanUVW(selection.getParent());
			selection.getParent().addSelection("extruded", this, extruded);
			selection.getParent().addSelection("walls", this, walls);
			selection.getParent().addSelection("peaks", this, peaks);
			selection.getParent().addSelection("fused", this, fused);
			tracker.setStopStatus(this, "Exiting HEM_Extrude.");
			return selection.getParent();
		}
		failedFaces = new FastList<HE_Face>();
		failedHeights = new FastList<Double>();
		applyFlat(selection.getParent(), selFaces, isFlat && fuse);
		if (heights != null) {
			for (int i = 0; i < failedHeights.size(); i++) {
				heights[selFaces.indexOf(failedFaces.get(i))] = failedHeights
						.get(i);
			}
		}
		if (peak) {
			applyPeaked(selection.getParent(), failedFaces);
		}
		WB_Coord n;
		if (!isFlat) {
			if (heights != null) {
				if (heights.length == selFaces.size()) {
					for (int i = 0; i < selFaces.size(); i++) {
						f = selFaces.get(i);
						n = _faceNormals.get(f.getKey());
						he = f.getHalfedge();
						do {
							he.getVertex().getPosition().addMulSelf(heights[i],
									n);
							he = he.getNextInFace();
						} while (he != f.getHalfedge());
					}
				} else {
					throw new IllegalArgumentException(
							"Length of heights array does not correspond to number of extruded faces.");
				}
			} else {
				for (int i = 0; i < selFaces.size(); i++) {
					f = selFaces.get(i);
					n = _faceNormals.get(f.getKey());
					he = f.getHalfedge();
					do {
						final HE_Vertex v = he.getVertex();
						v.getPosition().addMulSelf(
								d.evaluate(v.xd(), v.yd(), v.zd()), n);
						he = he.getNextInFace();
					} while (he != f.getHalfedge());
				}
			}
		}
		HET_Texture.cleanUVW(selection.getParent());
		selection.getParent().addSelection("extruded", this, extruded);
		selection.getParent().addSelection("walls", this, walls);
		selection.getParent().addSelection("peaks", this, peaks);
		selection.getParent().addSelection("fused", this, fused);
		tracker.setStopStatus(this, "Exiting HEM_Extrude.");
		return selection.getParent();
	}

	/**
	 * Apply straight extrusion.
	 *
	 * @param mesh
	 *            the mesh
	 * @param faces
	 *            the faces
	 * @return mesh
	 */
	private HE_Mesh applyStraight(final HE_Mesh mesh,
			final List<HE_Face> faces) {
		final int nf = faces.size();
		final boolean[] visited = new boolean[nf];
		WB_Coord fc;
		WB_ProgressCounter counter = new WB_ProgressCounter(nf, 10);
		tracker.setCounterStatus(this, "Creating straight extrusions.",
				counter);
		if (heights != null) {
			if (heights.length == faces.size()) {
				for (int i = 0; i < nf; i++) {
					// System.out.println(heights[i]);
					applyStraightToOneFaceIgnoreNeighborhood(i, faces, mesh,
							visited, heights[i]);
					counter.increment();
				}
			} else {
				throw new IllegalArgumentException(
						"Length of heights array does not correspond to number of extruded faces.");
			}
		} else {
			for (int i = 0; i < nf; i++) {
				fc = HE_MeshOp.getFaceCenter(faces.get(i));
				applyStraightToOneFace(i, faces, mesh, visited,
						d.evaluate(fc.xd(), fc.yd(), fc.zd()));
				counter.increment();
			}
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param id
	 * @param selfaces
	 * @param mesh
	 * @param visited
	 * @param d
	 * @return
	 */
	private boolean applyStraightToOneFaceIgnoreNeighborhood(final int id,
			final List<HE_Face> selfaces, final HE_Mesh mesh,
			final boolean[] visited, final double d) {
		if (visited[id]) {
			return false;
		}
		final HE_Face f = selfaces.get(id);
		final WB_Coord n = _faceNormals.get(f.getKey());
		final List<HE_Face> neighborhood = new FastList<HE_Face>();
		neighborhood.add(f);
		f.setInternalLabel(1);
		visited[id] = true;
		extruded.addFaces(neighborhood);
		final List<HE_Halfedge> outerHalfedges = new FastList<HE_Halfedge>();
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		final List<HE_Vertex> vertices = new FastList<HE_Vertex>();
		final List<HE_Halfedge> pairHalfedges = new FastList<HE_Halfedge>();
		final List<HE_Vertex> outerVertices = new FastList<HE_Vertex>();
		final List<HE_Vertex> extOuterVertices = new FastList<HE_Vertex>();
		for (int i = 0; i < neighborhood.size(); i++) {
			HE_Halfedge he = neighborhood.get(i).getHalfedge();
			do {
				final HE_Face fp = he.getPair().getFace();
				if (fp == null || !neighborhood.contains(fp)) {
					outerHalfedges.add(he);
				}
				halfedges.add(he);
				if (!vertices.contains(he.getVertex())) {
					vertices.add(he.getVertex());
				}
				he = he.getNextInFace();
			} while (he != neighborhood.get(i).getHalfedge());
		}
		for (int i = 0; i < outerHalfedges.size(); i++) {
			pairHalfedges.add(outerHalfedges.get(i).getPair());
			outerVertices.add(outerHalfedges.get(i).getVertex());
			final HE_Vertex eov = new HE_Vertex(
					outerHalfedges.get(i).getVertex());
			eov.copyProperties(outerHalfedges.get(i).getVertex());
			if (n != null) {
				eov.getPosition().addMulSelf(d, n);
			}
			extOuterVertices.add(eov);
		}
		mesh.addVertices(extOuterVertices);
		for (int i = 0; i < vertices.size(); i++) {
			final HE_Vertex v = vertices.get(i);
			if (!outerVertices.contains(v)) {
				v.getPosition().addMulSelf(d, n);
			}
		}
		for (int i = 0; i < halfedges.size(); i++) {
			final HE_Halfedge he = halfedges.get(i);
			final int ovi = outerVertices.indexOf(he.getVertex());
			if (ovi >= 0) {
				mesh.setVertex(he, extOuterVertices.get(ovi));
				mesh.setHalfedge(extOuterVertices.get(ovi), he);
			}
		}
		final List<HE_Halfedge> newhes = new FastList<HE_Halfedge>();
		for (int c = 0; c < outerHalfedges.size(); c++) {
			final HE_Face fNew = new HE_Face();
			walls.add(fNew);
			fNew.copyProperties(f);
			fNew.setInternalLabel(2);
			final HE_Halfedge heOrig1 = outerHalfedges.get(c);
			final HE_Halfedge heOrig2 = pairHalfedges.get(c);
			final HE_Halfedge heNew1 = new HE_Halfedge();
			final HE_Halfedge heNew2 = new HE_Halfedge();
			final HE_Halfedge heNew3 = new HE_Halfedge();
			final HE_Halfedge heNew4 = new HE_Halfedge();
			HE_Halfedge hen = heOrig1.getNextInFace();
			int cp = -1;
			do {
				cp = outerHalfedges.indexOf(hen);
				hen = hen.getPair().getNextInFace();
			} while (hen != heOrig1.getNextInFace() && cp == -1);
			final HE_Vertex v1 = outerVertices.get(c);
			final HE_Vertex v2 = outerVertices.get(cp);
			final HE_Vertex v4 = extOuterVertices.get(c);
			final HE_Vertex v3 = extOuterVertices.get(cp);
			mesh.setVertex(heNew1, v1);
			mesh.setHalfedge(v1, heNew1);
			mesh.setFace(heNew1, fNew);
			mesh.setHalfedge(fNew, heNew1);
			mesh.setPair(heOrig2, heNew1);
			mesh.setNext(heNew1, heNew2);
			mesh.setVertex(heNew2, v2);
			mesh.setHalfedge(v2, heNew2);
			mesh.setFace(heNew2, fNew);
			mesh.setNext(heNew2, heNew3);
			mesh.setVertex(heNew3, v3);
			mesh.setHalfedge(v3, heNew3);
			mesh.setFace(heNew3, fNew);
			mesh.setPair(heNew3, heOrig1);
			mesh.setNext(heNew3, heNew4);
			mesh.setVertex(heNew4, v4);
			mesh.setHalfedge(v4, heNew4);
			mesh.setFace(heNew4, fNew);
			mesh.setNext(heNew4, heNew1);
			mesh.setVertex(heOrig1, v4);
			mesh.addDerivedElement(fNew, heOrig1, heOrig2);
			mesh.add(heNew1);
			mesh.add(heNew2);
			mesh.add(heNew3);
			mesh.add(heNew4);
			newhes.add(heNew1);
			newhes.add(heNew2);
			newhes.add(heNew3);
			newhes.add(heNew4);
		}
		HE_MeshOp.pairHalfedges(mesh, newhes);
		return true;
	}

	/**
	 * Apply straight extrusion to one face.
	 *
	 * @param id
	 *            the id
	 * @param selfaces
	 *            the selfaces
	 * @param mesh
	 *            the mesh
	 * @param visited
	 *            the visited
	 * @param d
	 * @return true, if successful
	 */
	private boolean applyStraightToOneFace(final int id,
			final List<HE_Face> selfaces, final HE_Mesh mesh,
			final boolean[] visited, final double d) {
		if (visited[id]) {
			return false;
		}
		final HE_Face f = selfaces.get(id);
		final WB_Coord n = _faceNormals.get(f.getKey());
		final List<HE_Face> neighborhood = new FastList<HE_Face>();
		neighborhood.add(f);
		f.setInternalLabel(1);
		visited[id] = true;
		int no = 0;
		int nn = 1;
		do {
			nn = neighborhood.size();
			for (int i = no; i < nn; i++) {
				final HE_Face fi = neighborhood.get(i);
				final List<HE_Face> faces = fi.getNeighborFaces();
				for (int j = 0; j < faces.size(); j++) {
					final HE_Face fj = faces.get(j);
					if (_faceNormals.get(fi.getKey()) != null
							&& _faceNormals.get(fj.getKey()) != null) {
						if (WB_Vector.isParallel(_faceNormals.get(fi.getKey()),
								_faceNormals.get(fj.getKey()))) {
							final int ij = selfaces.indexOf(fj);
							if (ij >= 0) {
								if (!neighborhood.contains(fj)) {
									neighborhood.add(fj);
									fj.setInternalLabel(1);
								}
								visited[ij] = true;
							}
						}
					}
				}
			}
			no = nn;
		} while (neighborhood.size() > nn);
		extruded.addFaces(neighborhood);
		final List<HE_Halfedge> outerHalfedges = new FastList<HE_Halfedge>();
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		final List<HE_Vertex> vertices = new FastList<HE_Vertex>();
		final List<HE_Halfedge> pairHalfedges = new FastList<HE_Halfedge>();
		final List<HE_Vertex> outerVertices = new FastList<HE_Vertex>();
		final List<HE_Vertex> extOuterVertices = new FastList<HE_Vertex>();
		for (int i = 0; i < neighborhood.size(); i++) {
			HE_Halfedge he = neighborhood.get(i).getHalfedge();
			do {
				final HE_Face fp = he.getPair().getFace();
				if (fp == null || !neighborhood.contains(fp)) {
					outerHalfedges.add(he);
				}
				halfedges.add(he);
				if (!vertices.contains(he.getVertex())) {
					vertices.add(he.getVertex());
				}
				he = he.getNextInFace();
			} while (he != neighborhood.get(i).getHalfedge());
		}
		for (int i = 0; i < outerHalfedges.size(); i++) {
			pairHalfedges.add(outerHalfedges.get(i).getPair());
			outerVertices.add(outerHalfedges.get(i).getVertex());
			final HE_Vertex eov = new HE_Vertex(
					outerHalfedges.get(i).getVertex());
			eov.copyProperties(outerHalfedges.get(i).getVertex());
			if (n != null) {
				eov.getPosition().addMulSelf(d, n);
			}
			extOuterVertices.add(eov);
		}
		mesh.addVertices(extOuterVertices);
		for (int i = 0; i < vertices.size(); i++) {
			final HE_Vertex v = vertices.get(i);
			if (!outerVertices.contains(v)) {
				v.getPosition().addMulSelf(d, n);
			}
		}
		for (int i = 0; i < halfedges.size(); i++) {
			final HE_Halfedge he = halfedges.get(i);
			final int ovi = outerVertices.indexOf(he.getVertex());
			if (ovi >= 0) {
				mesh.setVertex(he, extOuterVertices.get(ovi));
				mesh.setHalfedge(extOuterVertices.get(ovi), he);
			}
		}
		final List<HE_Halfedge> newhes = new FastList<HE_Halfedge>();
		for (int c = 0; c < outerHalfedges.size(); c++) {
			final HE_Face fNew = new HE_Face();
			walls.add(fNew);
			fNew.copyProperties(f);
			fNew.setInternalLabel(2);
			final HE_Halfedge heOrig1 = outerHalfedges.get(c);
			final HE_Halfedge heOrig2 = pairHalfedges.get(c);
			final HE_Halfedge heNew1 = new HE_Halfedge();
			final HE_Halfedge heNew2 = new HE_Halfedge();
			final HE_Halfedge heNew3 = new HE_Halfedge();
			final HE_Halfedge heNew4 = new HE_Halfedge();
			HE_Halfedge hen = heOrig1.getNextInFace();
			int cp = -1;
			do {
				cp = outerHalfedges.indexOf(hen);
				hen = hen.getPair().getNextInFace();
			} while (hen != heOrig1.getNextInFace() && cp == -1);
			final HE_Vertex v1 = outerVertices.get(c);
			final HE_Vertex v2 = outerVertices.get(cp);
			final HE_Vertex v4 = extOuterVertices.get(c);
			final HE_Vertex v3 = extOuterVertices.get(cp);
			mesh.setVertex(heNew1, v1);
			mesh.setHalfedge(v1, heNew1);
			mesh.setFace(heNew1, fNew);
			mesh.setHalfedge(fNew, heNew1);
			mesh.setPair(heNew1, heOrig2);
			mesh.setNext(heNew1, heNew2);
			mesh.setVertex(heNew2, v2);
			mesh.setHalfedge(v2, heNew2);
			mesh.setFace(heNew2, fNew);
			mesh.setNext(heNew2, heNew3);
			mesh.setVertex(heNew3, v3);
			mesh.setHalfedge(v3, heNew3);
			mesh.setFace(heNew3, fNew);
			mesh.remove(heOrig1);
			mesh.add(heOrig1);
			mesh.setPair(heNew3, heOrig1);
			mesh.setNext(heNew3, heNew4);
			mesh.setVertex(heNew4, v4);
			mesh.setHalfedge(v4, heNew4);
			mesh.setFace(heNew4, fNew);
			mesh.setNext(heNew4, heNew1);
			mesh.setVertex(heOrig1, v4);
			mesh.addDerivedElement(fNew, heOrig1, heOrig2);
			mesh.add(heNew1);
			mesh.add(heNew2);
			mesh.add(heNew3);
			mesh.add(heNew4);
			newhes.add(heNew1);
			newhes.add(heNew2);
			newhes.add(heNew3);
			newhes.add(heNew4);
		}
		HE_MeshOp.pairHalfedges(mesh, newhes);
		return true;
	}

	/**
	 * Apply peaked extrusion.
	 *
	 * @param mesh
	 * @param faces
	 * @return mesh
	 */
	private HE_Mesh applyPeaked(final HE_Mesh mesh, final List<HE_Face> faces) {
		final int nf = faces.size();
		HE_Face f;
		WB_Coord fc;
		WB_ProgressCounter counter = new WB_ProgressCounter(nf, 10);
		tracker.setCounterStatus(this, "Creating peaked extrusions.", counter);
		for (int i = 0; i < nf; i++) {
			f = faces.get(i);
			_faceCenters.put(f.getKey(), HE_MeshOp.getFaceCenter(f));
		}
		if (heights != null) {
			if (heights.length == faces.size()) {
				for (int i = 0; i < nf; i++) {
					applyPeakToOneFace(i, faces, mesh, heights[i]);
					counter.increment();
				}
			} else {
				throw new IllegalArgumentException(
						"Length of heights array does not correspond to number of extruded faces.");
			}
		} else {
			for (int i = 0; i < nf; i++) {
				fc = HE_MeshOp.getFaceCenter(faces.get(i));
				applyPeakToOneFace(i, faces, mesh,
						d.evaluate(fc.xd(), fc.yd(), fc.zd()));
				counter.increment();
			}
		}
		return mesh;
	}

	/**
	 * Apply peaked extrusion to one face.
	 *
	 * @param id
	 * @param selFaces
	 * @param mesh
	 * @param d
	 */
	private void applyPeakToOneFace(final int id, final List<HE_Face> selFaces,
			final HE_Mesh mesh, final double d) {
		final HE_Face f = selFaces.get(id);
		final WB_Vector n = new WB_Vector(_faceNormals.get(f.getKey()));
		final WB_Point fc = new WB_Point(_faceCenters.get(f.getKey()));
		walls.add(f);
		peaks.add(f);
		f.setInternalLabel(4);
		final HE_Face[] newFaces = HEM_TriSplit
				.splitFaceTri(mesh, f, fc.addSelf(n.mulSelf(d)))
				.getFacesAsArray();
		for (final HE_Face newFace : newFaces) {
			newFace.copyProperties(f);
		}
		walls.addFaces(newFaces);
		peaks.addFaces(newFaces);
	}

	/**
	 * Apply flat extrusion.
	 *
	 * @param mesh
	 * @param faces
	 * @param fuse
	 * @return mesh
	 */
	private HE_Mesh applyFlat(final HE_Mesh mesh, final List<HE_Face> faces,
			final boolean fuse) {
		final HE_Selection sel = HE_Selection.getSelection(mesh);
		final int nf = faces.size();
		WB_ProgressCounter counter = new WB_ProgressCounter(nf, 10);
		tracker.setCounterStatus(this, "Creating flat extrusions.", counter);
		WB_Coord fc;
		if (heights != null) {
			if (heights.length == faces.size()) {
				for (int i = 0; i < nf; i++) {
					if (!applyFlatToOneFace(i, faces, mesh, sel)) {
						failedFaces.add(faces.get(i));
						failedHeights.add(heights[i]);
					}
					counter.increment();
				}
			} else {
				throw new IllegalArgumentException(
						"Length of heights array does not correspond to number of extruded faces.");
			}
		} else {
			for (int i = 0; i < nf; i++) {
				if (!applyFlatToOneFace(i, faces, mesh, sel)) {
					failedFaces.add(faces.get(i));
					fc = HE_MeshOp.getFaceCenter(faces.get(i));
					failedHeights.add(d.evaluate(fc.xd(), fc.yd(), fc.zd()));
				}
			}
			counter.increment();
		}
		if (fuse) {
			counter = new WB_ProgressCounter(sel.getNumberOfHalfedges(), 10);
			tracker.setCounterStatus(this, "Fusing original edges.", counter);
			for (int i = 0; i < sel.getNumberOfHalfedges(); i++) {
				final HE_Halfedge e = sel.getHalfedgeWithIndex(i);
				if (e.isEdge()) {
					final HE_Face f1 = e.getFace();
					final HE_Face f2 = e.getPair().getFace();
					if (f1 != null && f2 != null) {
						// System.out.println(f1.getInternalLabel() + " " +
						// f2.getInternalLabel());
						if (f1.getInternalLabel() == 2
								&& f2.getInternalLabel() == 2) {
							if (Math.abs(
									Math.PI - HE_MeshOp.getEdgeDihedralAngle(
											f1.getHalfedge(f2))) < fuseAngle) {
								final HE_Face f = mesh.deleteEdge(e);
								if (f != null) {
									f.setInternalLabel(3);
									fused.add(f);
								}
							}
						}
					}
				}
				counter.increment();
			}
		}
		return mesh;
	}

	/**
	 * Apply flat extrusion to one face.
	 *
	 * @param id
	 * @param selFaces
	 * @param mesh
	 * @param fuse
	 * @return true, if successful
	 */
	private boolean applyFlatToOneFace(final int id,
			final List<HE_Face> selFaces, final HE_Mesh mesh,
			final HE_Selection fuse) {
		final HE_Face f = selFaces.get(id);
		final WB_Coord fc = _faceCenters.get(f.getKey());
		final List<HE_Vertex> faceVertices = new FastList<HE_Vertex>();
		final List<HE_Halfedge> faceHalfedges = new FastList<HE_Halfedge>();
		final List<WB_Coord> faceHalfedgeNormals = new FastList<WB_Coord>();
		final List<WB_Coord> faceEdgeCenters = new FastList<WB_Coord>();
		final List<HE_Vertex> extFaceVertices = new FastList<HE_Vertex>();
		HE_Halfedge he = f.getHalfedge();
		do {
			faceVertices.add(he.getVertex());
			faceHalfedges.add(he);
			faceHalfedgeNormals.add(_halfedgeNormals.get(he.getKey()));
			faceEdgeCenters.add(HE_MeshOp.getHalfedgeCenter(he));
			extFaceVertices.add(he.getVertex().get());
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		boolean isPossible = true;
		final int n = faceVertices.size();
		if (relative == true) {
			double ch;
			for (int i = 0; i < n; i++) {
				final HE_Vertex v = faceVertices.get(i);
				final WB_Point diff = new WB_Point(fc).subSelf(v);
				he = faceHalfedges.get(i);
				ch = Math.max(_halfedgeEWs.get(he.getKey()),
						_halfedgeEWs.get(he.getPrevInFace().getKey()));
				diff.mulSelf(ch);
				diff.addSelf(v);
				extFaceVertices.get(i).set(diff);
			}
		} else {
			final double[] d = new double[n];
			for (int i = 0; i < n; i++) {
				d[i] = _halfedgeEWs.get(faceHalfedges.get(i).getKey());
			}
			if (HE_MeshOp.getFaceType(f) == WB_Classification.CONVEX) {
				WB_Polygon poly = HE_MeshOp.getPolygon(f);
				poly = poly.trimConvexPolygon(d);
				if (poly.getNumberOfShellPoints() > 2) {
					for (int i = 0; i < n; i++) {
						extFaceVertices.get(i)
								.set(poly.closestPoint(faceVertices.get(i)));
					}
				} else {
					isPossible = false;
				}
			} else {
				WB_Coord v1 = new WB_Point(faceVertices.get(n - 1));
				WB_Coord v2 = new WB_Point(faceVertices.get(0));
				for (int i = 0, j = n - 1; i < n; j = i, i++) {
					final WB_Coord n1 = faceHalfedgeNormals.get(j);
					final WB_Coord n2 = faceHalfedgeNormals.get(i);
					final WB_Coord v3 = faceVertices.get((i + 1) % n);
					final WB_Segment S1 = new WB_Segment(
							WB_Point.addMul(v1, d[j], n1),
							WB_Point.addMul(v2, d[j], n1));
					final WB_Segment S2 = new WB_Segment(
							WB_Point.addMul(v2, d[i], n2),
							WB_Point.addMul(v3, d[i], n2));
					final WB_IntersectionResult ir = WB_GeometryOp
							.getIntersection3D(S1, S2);
					final WB_Coord p = ir.dimension == 0 ? (WB_Point) ir.object
							: ((WB_Segment) ir.object).getCenter();
					extFaceVertices.get(i).set(p);
					v1 = v2;
					v2 = v3;
				}
			}
		}
		if (isPossible) {
			extruded.add(f);
			f.setInternalLabel(1);
			final List<HE_Halfedge> newhes = new FastList<HE_Halfedge>();
			int c = 0;
			he = f.getHalfedge();
			do {
				final HE_Face fNew = new HE_Face();
				walls.add(fNew);
				fNew.copyProperties(f);
				fNew.setInternalLabel(2);
				final HE_Halfedge heOrig1 = he;
				final HE_Halfedge heOrig2 = he.getPair();
				final HE_Halfedge heNew1 = new HE_Halfedge();
				fuse.add(heNew1);
				final HE_Halfedge heNew2 = new HE_Halfedge();
				final HE_Halfedge heNew3 = new HE_Halfedge();
				final HE_Halfedge heNew4 = new HE_Halfedge();
				final int cp = (c + 1) % faceVertices.size();
				final HE_Vertex v1 = faceVertices.get(c);
				final HE_Vertex v2 = faceVertices.get(cp);
				final HE_Vertex v4 = extFaceVertices.get(c);
				final HE_Vertex v3 = extFaceVertices.get(cp);
				mesh.setVertex(heNew1, v1);
				mesh.setHalfedge(v1, heNew1);
				mesh.setFace(heNew1, fNew);
				mesh.setHalfedge(fNew, heNew1);
				mesh.setPair(heNew1, heOrig2);
				mesh.setNext(heNew1, heNew2);
				mesh.setVertex(heNew2, v2);
				mesh.setHalfedge(v2, heNew2);
				mesh.setFace(heNew2, fNew);
				mesh.setNext(heNew2, heNew3);
				mesh.setVertex(heNew3, v3);
				mesh.setHalfedge(v3, heNew3);
				mesh.setFace(heNew3, fNew);
				mesh.remove(heOrig1);
				mesh.add(heOrig1);
				mesh.setPair(heNew3, heOrig1);
				mesh.setNext(heNew3, heNew4);
				mesh.setVertex(heNew4, v4);
				mesh.setHalfedge(v4, heNew4);
				mesh.setFace(heNew4, fNew);
				mesh.setNext(heNew4, heNew1);
				mesh.setVertex(heOrig1, v4);
				mesh.addDerivedElement(fNew, heOrig1, heOrig2);
				mesh.add(v3);
				mesh.add(heNew1);
				mesh.add(heNew2);
				mesh.add(heNew3);
				mesh.add(heNew4);
				newhes.add(heNew1);
				newhes.add(heNew2);
				newhes.add(heNew3);
				newhes.add(heNew4);
				he = he.getNextInFace();
				c++;
			} while (he != f.getHalfedge());
			HE_MeshOp.pairHalfedges(mesh, newhes);
			final List<HE_Halfedge> edgesToRemove = new FastList<HE_Halfedge>();
			for (int i = 0; i < newhes.size(); i++) {
				final HE_Halfedge e = newhes.get(i);
				// if (e.isEdge()) {
				if (WB_Epsilon.isZeroSq(WB_CoordOp.getSqDistance3D(
						e.getStartVertex(), e.getEndVertex()))) {
					edgesToRemove.add(e);
				}
				// }
			}
			for (int i = 0; i < edgesToRemove.size(); i++) {
				HE_MeshOp.collapseEdge(mesh, edgesToRemove.get(i));
			}
		}
		return isPossible;
	}
}
