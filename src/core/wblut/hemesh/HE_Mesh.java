/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.core.WB_ProgressReporter.WB_ProgressTracker;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_Frame;
import wblut.geom.WB_Geometry3D;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_KDTree;
import wblut.geom.WB_Mesh;
import wblut.geom.WB_MeshCreator;
import wblut.geom.WB_Network;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_Transform3D;
import wblut.geom.WB_Transform2D;
import wblut.geom.WB_TriangleGenerator;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_MTRandom;

/**
 * Half-edge mesh data structure.
 *
 * @author Frederik Vanhoutte (W:Blut)
 *
 */
public class HE_Mesh extends HE_MeshElement
		implements WB_TriangleGenerator, HE_HalfedgeStructure, WB_Geometry3D {
	protected final static WB_ProgressTracker	tracker	= WB_ProgressTracker
			.instance();
	protected WB_GeometryFactory				gf		= new WB_GeometryFactory();

	/**
	 *
	 */
	class CreatorThread implements Callable<HE_Mesh> {
		HEC_Creator creator;

		CreatorThread(final HEC_Creator creator) {
			this.creator = creator;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_Mesh call() {
			HE_Mesh result = creator.create();
			return result;
		}
	}

	/**
	 *
	 */
	class ModifierThread implements Callable<HE_Mesh> {
		HEM_Modifier	machine;
		HE_Mesh			mesh;

		ModifierThread(final HEM_Modifier machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (Exception e) {
				return mesh;
			}
		}
	}

	/**
	 *
	 */
	class SimplifierThread implements Callable<HE_Mesh> {
		HES_Simplifier	machine;
		HE_Mesh			mesh;

		SimplifierThread(final HES_Simplifier machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (Exception e) {
				return mesh;
			}
		}
	}

	/**
	 *
	 */
	class SubdividorThread implements Callable<HE_Mesh> {
		HES_Subdividor	machine;
		HE_Mesh			mesh;

		SubdividorThread(final HES_Subdividor machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (Exception e) {
				return mesh;
			}
		}
	}

	String							name;
	private HE_RAS<HE_Vertex>		vertices;
	private HE_RAS<HE_Face>			faces;
	private HE_RAS<HE_Halfedge>		edges;
	private HE_RAS<HE_Halfedge>		halfedges;
	private HE_RAS<HE_Halfedge>		unpairedHalfedges;
	ExecutorService					executor;
	LinkedList<Callable<HE_Mesh>>	tasks;
	boolean							finished;
	Future<HE_Mesh>					future;
	Map<String, HE_Selection>		selections;
	int[]							triangles;

	/**
	 * Instantiates a new HE_Mesh.
	 *
	 */
	public HE_Mesh() {
		super();
		vertices = new HE_RAS<HE_Vertex>();
		halfedges = new HE_RAS<HE_Halfedge>();
		edges = new HE_RAS<HE_Halfedge>();
		unpairedHalfedges = new HE_RAS<HE_Halfedge>();
		faces = new HE_RAS<HE_Face>();
		selections = new UnifiedMap<String, HE_Selection>();
		tasks = new LinkedList<Callable<HE_Mesh>>();
		future = null;
		executor = null;
		finished = true;
		triangles = null;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HE_Mesh(final HE_Mesh mesh) {
		this();
		set(mesh);
		triangles = null;
	}

	/**
	 * Constructor.
	 *
	 * @param creator
	 *            HE_Creator that generates this mesh
	 */
	public HE_Mesh(final HEC_Creator creator) {
		this();
		setNoCopy(creator.create());
		triangles = null;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HE_Mesh(final WB_Mesh mesh) {
		this(new HEC_FromWBMesh(mesh));
		triangles = null;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	public HE_Mesh(final WB_MeshCreator mesh) {
		this(new HEC_FromWBMesh(mesh.create()));
		triangles = null;
	}

	@Override
	public void add(final HE_Element el) {
		if (el instanceof HE_Face) {
			add((HE_Face) el);
		} else if (el instanceof HE_Vertex) {
			add((HE_Vertex) el);
		} else if (el instanceof HE_Halfedge) {
			add((HE_Halfedge) el);
		}
	}

	/**
	 * Add face.
	 *
	 * @param f
	 *            face to add
	 */
	@Override
	public void add(final HE_Face f) {
		faces.add(f);
	}

	/**
	 * Adds a face to the mesh. The face is also added to any selection that
	 * contains one of the elements it derives from.
	 *
	 * @param f
	 *            new face
	 * @param el
	 *            elements the face derives from
	 */
	public void addDerivedElement(final HE_Face f, final HE_Element... el) {
		add(f);
		for (HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (int i = 0; i < el.length; i++) {
				contains |= sel.contains(el[i]);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(f);
			}
		}
	}

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as Collection<? extends HE_Face>
	 */
	@Override
	public void addFaces(final Collection<? extends HE_Face> faces) {
		for (HE_Face f : faces) {
			add(f);
		}
	}

	/**
	 * Adds faces.
	 *
	 * @param faces
	 *            faces to add as HE_Face[]
	 */
	@Override
	public void addFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			add(face);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public void addFaces(final HE_HalfedgeStructure source) {
		faces.addAll(source.getFaces());
	}

	/**
	 * Adds halfedge.
	 *
	 * @param he
	 *            halfedge to add
	 */
	@Override
	public void add(final HE_Halfedge he) {
		if (he.getPair() == null) {
			unpairedHalfedges.add(he);
		} else if (he.isEdge()) {
			edges.add(he);
		} else {
			halfedges.add(he);
		}
	}

	/**
	 * Adds a halfedge to the mesh. The halfedge is also added to any selection
	 * that contains one of the elements it derives from.
	 *
	 * @param he
	 *            new halfedge
	 * @param el
	 *            elements the halfedge derives from
	 */
	public void addDerivedElement(final HE_Halfedge he,
			final HE_Element... el) {
		add(he);
		for (HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (int i = 0; i < el.length; i++) {
				contains |= sel.contains(el[i]);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(he);
			}
		}
	}

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as Collection<? extends HE_Halfedge>
	 */
	@Override
	public void addHalfedges(
			final Collection<? extends HE_Halfedge> halfedges) {
		for (HE_Halfedge he : halfedges) {
			add(he);
		}
	}

	/**
	 * Adds halfedges.
	 *
	 * @param halfedges
	 *            halfedges to add as HE_Halfedge[]
	 */
	@Override
	public void addHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			add(halfedge);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public void addHalfedges(final HE_HalfedgeStructure source) {
		for (HE_Halfedge he : source.getHalfedges()) {
			add(he);
		}
	}

	/**
	 * Add vertex.
	 *
	 * @param v
	 *            vertex to add
	 */
	@Override
	public void add(final HE_Vertex v) {
		vertices.add(v);
	}

	/**
	 * Adds a vertex to the mesh. The vertex is also added to any selection that
	 * contains one of the elements it derives from.
	 *
	 * @param v
	 *            new vertex
	 * @param el
	 *            elements the vertex derives from
	 */
	public void addDerivedElement(final HE_Vertex v, final HE_Element... el) {
		add(v);
		for (HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (int i = 0; i < el.length; i++) {
				contains |= sel.contains(el[i]);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(v);
			}
		}
	}

	/**
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as Collection<? extends HE_Vertex>
	 */
	@Override
	public void addVertices(final Collection<? extends HE_Vertex> vertices) {
		for (HE_Vertex v : vertices) {
			add(v);
		}
	}

	/**
	 *
	 *
	 * @param source
	 */
	@Override
	public void addVertices(final HE_HalfedgeStructure source) {
		vertices.addAll(source.getVertices());
	}

	/**
	 * Adds vertices.
	 *
	 * @param vertices
	 *            vertices to add as HE_Vertex[]
	 */
	@Override
	public void addVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertex : vertices) {
			add(vertex);
		}
	}

	/**
	 * Add all mesh elements to this mesh. No copies are made.
	 *
	 * @param mesh
	 *            mesh to add
	 */
	@Override
	public void add(final HE_Mesh mesh) {
		addVertices(mesh.vertices);
		addFaces(mesh.faces);
		addHalfedges(mesh.edges);
		addHalfedges(mesh.halfedges);
		addHalfedges(mesh.unpairedHalfedges);
		Set<String> selections = mesh.getSelectionNames();
		for (String name : selections) {
			HE_Selection sourceSel = mesh.getSelection(name);
			HE_Selection currentSel = getSelection(name);
			HE_Selection sel = sourceSel.get();
			if (currentSel == null) {
				currentSel = new HE_Selection(this);
				currentSel.add(sel);
				addSelection(name, currentSel);
			} else {
				currentSel.add(sel);
			}
		}
	}

	void addSelection(final String name, final HE_Machine machine,
			final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			sel.createdBy = machine.getName();
			HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this,
						"Adding to selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				prevsel.add(sel);
			}
		} else {
			tracker.setDuringStatus(this, "Selection " + name
					+ " not added: selection is null or parent mesh is not the same.");
		}
	}

	/**
	 *
	 * @param name
	 * @param sel
	 */
	public void addSelection(final String name, final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this,
						"Adding to selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				prevsel.add(sel);
			}
		} else {
			tracker.setDuringStatus(this, "Selection " + name
					+ " not added: selection is null or parent mesh is not the same.");
		}
	}

	@Override
	public HE_Mesh apply(final WB_Transform3D T) {
		return new HEC_Transform(this, T).create();
	}

	/**
	 *
	 * @param T
	 * @return
	 */
	@Override
	public HE_Mesh applySelf(final WB_Transform3D T) {
		return modify(new HEM_Transform(T));
	}

	@Override
	public HE_Mesh apply2D(final WB_Transform2D T) {
		return new HEC_Transform2D(this, T).create();
	}

	/**
	 *
	 * @param T
	 * @return
	 */
	@Override
	public HE_Mesh apply2DSelf(final WB_Transform2D T) {
		return modify(new HEM_Transform2D(T));
	}

	/**
	 * Clean.
	 */
	public void clean() {
		modify(new HEM_Clean());
	}

	/**
	 *
	 */
	public void cleanSelections() {
		for (HE_Selection sel : selections.values()) {
			sel.cleanSelection();
		}
	}

	/**
	 * Clean all mesh elements not used by any faces.
	 *
	 * @return self
	 */
	public HE_Mesh cleanUnusedElementsByFace() {
		return HE_MeshOp.cleanUnusedElementsByFace(this);
	}

	/**
	 * Clear entire structure.
	 */
	@Override
	public void clear() {
		selections = new UnifiedMap<String, HE_Selection>();
		clearVertices();
		clearHalfedges();
		clearFaces();
	}

	/**
	 * Clear edges.
	 */
	@Override
	public void clearEdges() {
		edges = new HE_RAS<HE_Halfedge>();
		for (HE_Selection sel : selections.values()) {
			sel.clearEdges();
		}
	}

	/**
	 * Unlink face from halfedge
	 *
	 * @param he
	 */
	public void clearFace(final HE_Halfedge he) {
		he.clearFace();
		if (he.getPair() != null) {
			setPair(he, he.getPair());
		}
	}

	/**
	 * Clear faces.
	 */
	@Override
	public void clearFaces() {
		faces = new HE_RAS<HE_Face>();
		for (HE_Selection sel : selections.values()) {
			sel.clearFaces();
		}
	}

	/**
	 * Clear faces.
	 */
	void clearFacesNoSelectionCheck() {
		faces = new HE_RAS<HE_Face>();
	}

	/**
	 * Unlink halfedge from face
	 *
	 * @param f
	 */
	public void clearHalfedge(final HE_Face f) {
		f.clearHalfedge();
	}

	/**
	 * Unlink halfedge from vertex
	 *
	 * @param v
	 */
	public void clearHalfedge(final HE_Vertex v) {
		v.clearHalfedge();
	}

	/**
	 * Clear halfedges.
	 */
	@Override
	public void clearHalfedges() {
		halfedges = new HE_RAS<HE_Halfedge>();
		edges = new HE_RAS<HE_Halfedge>();
		unpairedHalfedges = new HE_RAS<HE_Halfedge>();
		for (HE_Selection sel : selections.values()) {
			sel.clearHalfedges();
		}
	}

	/**
	 * Unlink next halfedge from halfedge, unlinks the corresponding "previous"
	 * relationship.
	 *
	 * @param he
	 */
	public void clearNext(final HE_Halfedge he) {
		if (he.getNextInFace() != null) {
			he.getNextInFace().clearPrev();
		}
		he.clearNext();
	}

	/**
	 * Unpair halfedge. If the halfedge was paired, its pair is unpaired as
	 * well.
	 *
	 * @param he
	 */
	public void clearPair(final HE_Halfedge he) {
		if (he.getPair() == null) {
			return;
		}
		HE_Halfedge hep = he.getPair();
		removeNoSelectionCheck(he);
		removeNoSelectionCheck(hep);
		he.clearPair();
		hep.clearPair();
		add(he);
		add(hep);
	}

	@Override
	public void clearPrecomputed() {
		triangles = null;
		clearPrecomputedFaces();
		clearPrecomputedVertices();
		clearPrecomputedHalfedges();
	}

	public void clearPrecomputedFaces() {
		HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().clearPrecomputed();
		}
	}

	public void clearPrecomputedHalfedges() {
		HE_HalfedgeIterator heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().clearPrecomputed();
		}
	}

	public void clearPrecomputedVertices() {
		HE_VertexIterator vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().clearPrecomputed();
		}
	}

	/**
	 * Unlink previous halfedge from halfedge, unlinks the corresponding "next"
	 * relationship.
	 *
	 * @param he
	 */
	public void clearPrev(final HE_Halfedge he) {
		if (he.getPrevInFace() != null) {
			he.getPrevInFace().clearNext();
		}
		he.clearPrev();
	}

	/**
	 *
	 */
	public void clearSelections() {
	}

	/**
	 * Unlink vertex from halfedge
	 *
	 * @param he
	 */
	public void clearVertex(final HE_Halfedge he) {
		he.clearVertex();
	}

	/**
	 * Clear vertices.
	 */
	@Override
	public void clearVertices() {
		vertices = new HE_RAS<HE_Vertex>();
		for (HE_Selection sel : selections.values()) {
			sel.clearVertices();
		}
	}

	/**
	 * Clear vertices.
	 */
	void clearVerticesNoSelectionCheck() {
		vertices = new HE_RAS<HE_Vertex>();
	}

	/**
		 *
		 */
	public void clearVisitedElements() {
		final HE_FaceIterator fitr = fItr();
		while (fitr.hasNext()) {
			fitr.next().clearVisited();
		}
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			vitr.next().clearVisited();
		}
		final HE_HalfedgeIterator heitr = heItr();
		while (heitr.hasNext()) {
			heitr.next().clearVisited();
		}
	}

	@Override
	public boolean contains(final HE_Element el) {
		if (el instanceof HE_Face) {
			return contains((HE_Face) el);
		} else if (el instanceof HE_Vertex) {
			return contains((HE_Vertex) el);
		} else if (el instanceof HE_Halfedge) {
			return contains((HE_Halfedge) el);
		}
		return false;
	}

	/**
	 * Check if structure contains face.
	 *
	 * @param f
	 *            face
	 * @return true, if successful
	 */
	@Override
	public boolean contains(final HE_Face f) {
		return faces.contains(f);
	}

	/**
	 * Check if structure contains halfedge.
	 *
	 * @param he
	 *            halfedge
	 * @return true, if successful
	 */
	@Override
	public boolean contains(final HE_Halfedge he) {
		return edges.contains(he) || halfedges.contains(he)
				|| unpairedHalfedges.contains(he);
	}

	/**
	 * Check if structure contains vertex.
	 *
	 * @param v
	 *            vertex
	 * @return true, if successful
	 */
	@Override
	public boolean contains(final HE_Vertex v) {
		return vertices.contains(v);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsEdge(final long key) {
		return edges.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsFace(final long key) {
		return faces.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsHalfedge(final long key) {
		return halfedges.containsKey(key) || edges.containsKey(key)
				|| unpairedHalfedges.containsKey(key);
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsVertex(final long key) {
		return vertices.containsKey(key);
	}

	/**
	 * Deep copy of mesh.
	 *
	 * @return copy as new HE_Mesh
	 */
	public HE_Mesh copy() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	public HE_Selection copySelection(final String from, final String to) {
		HE_Selection sel = selections.get(from);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + from + " not found.");
		}
		HE_Selection copy = sel.get();
		addSelection(to, copy);
		return copy;
	}

	public void createThreaded(final HEC_Creator creator) {
		tasks.add(new CreatorThread(creator));
	}

	/**
	 * Delete face and remove all references. Its halfedges are removed, the
	 * boundary loop is unpaired.
	 *
	 * @param f
	 */
	public void cutFace(final HE_Face f) {
		HE_Halfedge he = f.getHalfedge();
		do {
			setHalfedge(he.getVertex(), he.getNextInVertex());
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		do {
			clearFace(he);
			clearPair(he);
			remove(he);
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		remove(f);
	}

	/**
	 * Delete edge. Adjacent faces are fused.
	 *
	 * @param e
	 *            edge to delete
	 * @return fused face (or null)
	 */
	public HE_Face deleteEdge(final HE_Halfedge e) {
		HE_Face f = null;
		final HE_Halfedge he1 = e.isEdge() ? e : e.getPair();
		final HE_Halfedge he2 = he1.getPair();
		final HE_Halfedge he1n = he1.getNextInFace();
		final HE_Halfedge he2n = he2.getNextInFace();
		final HE_Halfedge he1p = he1.getPrevInFace();
		final HE_Halfedge he2p = he2.getPrevInFace();
		HE_Vertex v = he1.getVertex();
		if (v.getHalfedge() == he1) {
			setHalfedge(v, he1.getNextInVertex());
		}
		v = he2.getVertex();
		if (v.getHalfedge() == he2) {
			setHalfedge(v, he2.getNextInVertex());
		}
		setNext(he1p, he2n);
		setNext(he2p, he1n);
		if (he1.getFace() != null && he2.getFace() != null) {
			f = new HE_Face();
			f.copyProperties(e.getPair().getFace());
			addDerivedElement(f, e.getPair().getFace());
			setHalfedge(f, he1p);
			HE_Halfedge he = he1p;
			do {
				setFace(he, f);
				he = he.getNextInFace();
			} while (he != he1p);
		}
		if (he1.getFace() != null) {
			remove(he1.getFace());
		}
		if (he2.getFace() != null) {
			remove(he2.getFace());
		}
		remove(he1);
		remove(he2);
		return f;
	}

	/**
	 * Delete face and remove all references. Its halfedges remain and form a
	 * valid boundary loop.
	 *
	 * @param f
	 *            face to delete
	 */
	public void deleteFace(final HE_Face f) {
		HE_Halfedge he = f.getHalfedge();
		do {
			clearFace(he);
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		remove(f);
	}

	/**
	 * Delete face and remove all references.
	 *
	 * @param faces
	 *            faces to delete
	 */
	public void deleteFaces(final HE_Selection faces) {
		HE_Face f;
		final Iterator<HE_Face> fItr = faces.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			remove(f);
		}
		cleanUnusedElementsByFace();
		HE_MeshOp.capHalfedges(this);
	}

	/**
	 * Edge iterator.
	 *
	 * @return edge iterator
	 */
	@Override
	public HE_EdgeIterator eItr() {
		return new HE_EdgeIterator(edges);
	}

	/**
	 * Fit in aabb.
	 *
	 * @param AABB
	 *
	 */
	public void fitInAABB(final WB_AABB AABB) {
		final WB_AABB self = HE_MeshOp.getAABB(this);
		moveSelf(new WB_Vector(self.getMin(), AABB.getMin()));
		scaleSelf(AABB.getWidth() / self.getWidth(),
				AABB.getHeight() / self.getHeight(),
				AABB.getDepth() / self.getDepth(), new WB_Point(AABB.getMin()));
	}

	public void fitInAABB(final WB_AABB from, final WB_AABB to) {
		moveSelf(new WB_Vector(from.getMin(), to.getMin()));
		scaleSelf(to.getWidth() / from.getWidth(),
				to.getHeight() / from.getHeight(),
				to.getDepth() / from.getDepth(), new WB_Point(to.getMin()));
	}

	/**
	 * Fit in aabb constrained.
	 *
	 * @param AABB
	 *
	 * @return
	 */
	public double fitInAABBConstrained(final WB_AABB AABB) {
		final WB_AABB self = HE_MeshOp.getAABB(this);
		moveSelf(new WB_Vector(self.getCenter(), AABB.getCenter()));
		double f = Math.min(AABB.getWidth() / self.getWidth(),
				AABB.getHeight() / self.getHeight());
		f = Math.min(f, AABB.getDepth() / self.getDepth());
		scaleSelf(f, new WB_Point(AABB.getCenter()));
		return f;
	}

	public double fitInAABBConstrained(final WB_AABB from, final WB_AABB to) {
		moveSelf(new WB_Vector(from.getCenter(), to.getCenter()));
		double f = Math.min(to.getWidth() / from.getWidth(),
				to.getHeight() / from.getHeight());
		f = Math.min(f, to.getDepth() / from.getDepth());
		scaleSelf(f, new WB_Point(to.getCenter()));
		return f;
	}

	/**
	 * Face iterator.
	 *
	 * @return face iterator
	 */
	@Override
	public HE_FaceIterator fItr() {
		List<HE_Face> fs = new FastList<HE_Face>(getFaces());
		return new HE_FaceIterator(fs);
	}

	/**
	 * Add all mesh elements to this mesh. No copies are made. Tries to join
	 * geometry.
	 *
	 * @param mesh
	 *            mesh to add
	 */
	public void fuse(final HE_Mesh mesh) {
		addVertices(mesh.getVerticesAsArray());
		addFaces(mesh.getFacesAsArray());
		addHalfedges(mesh.getHalfedgesAsArray());
		setNoCopy(new HE_Mesh(
				new HEC_FromPolygons().setPolygons(this.getPolygonList())));
	}

	/**
	 * Deep copy of mesh.
	 *
	 * @return copy as new HE_Mesh
	 */
	public HE_Mesh get() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	/**
	 * Collect all boundary halfedges.
	 *
	 * @return boundary halfedges
	 */
	public List<HE_Halfedge> getAllBoundaryHalfedges() {
		final List<HE_Halfedge> boundaryHalfedges = new FastList<HE_Halfedge>();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				boundaryHalfedges.add(he);
			}
		}
		return boundaryHalfedges;
	}

	/**
	 * Collect all boundary vertices.
	 *
	 * @return boundary vertices
	 */
	public List<HE_Vertex> getAllBoundaryVertices() {
		final List<HE_Vertex> boundaryVertices = new FastList<HE_Vertex>();
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				boundaryVertices.add(he.getVertex());
			}
		}
		return boundaryVertices;
	}

	/**
	 * Return all edge centers.
	 *
	 * @return array of WB_Coordinate.
	 */
	public WB_Coord[] getEdgeCenters() {
		final WB_Coord[] result = new WB_Coord[getNumberOfEdges()];
		int i = 0;
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result[i] = HE_MeshOp.getHalfedgeCenter(e);
			i++;
		}
		return result;
	}

	/**
	 * Return all edge normals.
	 *
	 * @return array of WB_Coordinate.
	 */
	public WB_Coord[] getEdgeNormals() {
		final WB_Coord[] result = new WB_Coord[getNumberOfEdges()];
		int i = 0;
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result[i] = HE_MeshOp.getEdgeNormal(e);
			i++;
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public List<HE_Halfedge> getEdges() {
		return new FastList<HE_Halfedge>(edges.getObjects());
	}

	/**
	 * Edges as array.
	 *
	 * @return all edges as HE_Halfedge[]
	 */
	@Override
	public HE_Halfedge[] getEdgesAsArray() {
		final HE_Halfedge[] edges = new HE_Halfedge[getNumberOfEdges()];
		final Iterator<HE_Halfedge> eItr = eItr();
		int i = 0;
		while (eItr.hasNext()) {
			edges[i] = eItr.next();
			i++;
		}
		return edges;
	}

	public int[][] getEdgesAsInt() {
		final int[][] result = new int[getNumberOfEdges()][2];
		final LongIntHashMap vertexKeys = new LongIntHashMap();
		final Iterator<HE_Vertex> vItr = vItr();
		int i = 0;
		while (vItr.hasNext()) {
			vertexKeys.put(vItr.next().getKey(), i);
			i++;
		}
		final Iterator<HE_Halfedge> eItr = eItr();
		HE_Halfedge he;
		i = 0;
		while (eItr.hasNext()) {
			he = eItr.next();
			result[i][0] = vertexKeys.get(he.getVertex().getKey());
			he = he.getPair();
			result[i][1] = vertexKeys.get(he.getVertex().getKey());
			i++;
		}
		return result;
	}

	/**
	 * Get edge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            edge index
	 * @return
	 */
	@Override
	public HE_Halfedge getEdgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size()) {
			throw new IndexOutOfBoundsException(
					"Requested edge index " + i + "not in range.");
		}
		return edges.getWithIndex(i);
	}

	/**
	 * Get edge with key. The key of a mesh element is unique and never changes.
	 *
	 * @param key
	 *            halfedge key
	 * @return halfedge
	 */
	@Override
	public HE_Halfedge getEdgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		if (he != null) {
			return he;
		}
		return unpairedHalfedges.getWithKey(key);
	}

	/**
	 *
	 *
	 * @return
	 */
	public int getEulerCharacteristic() {
		return getNumberOfVertices() - getNumberOfEdges() + getNumberOfFaces();
	}

	public WB_Coord getFaceCenter(final int id) {
		return HE_MeshOp.getFaceCenter(getFaceWithIndex(id));
	}

	/**
	 * Return all face centers.
	 *
	 * @return array of WB_Coordinate.
	 */
	public WB_Coord[] getFaceCenters() {
		final WB_Coord[] result = new WB_Coord[getNumberOfFaces()];
		int i = 0;
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getFaceCenter(f);
			i++;
		}
		return result;
	}

	public int[] getFaceColors() {
		final int[] result = new int[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getColor();
			i++;
		}
		return result;
	}

	public int[] getFaceInternalLabels() {
		final int[] result = new int[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getInternalLabel();
			i++;
		}
		return result;
	}

	public WB_Coord getFaceNormal(final int id) {
		return HE_MeshOp.getFaceNormal(getFaceWithIndex(id));
	}

	/**
	 * Return all face normals.
	 *
	 * @return array of WB_Coordinate.
	 */
	public WB_Coord[] getFaceNormals() {
		final WB_Coord[] result = new WB_Coord[getNumberOfFaces()];
		int i = 0;
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getFaceNormal(f);
			i++;
		}
		return result;
	}

	public WB_Plane[] getFacePlanes() {
		final WB_Plane[] result = new WB_Plane[getNumberOfFaces()];
		int i = 0;
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getPlane(f);
			i++;
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public List<HE_Face> getFaces() {
		return new FastList<HE_Face>(faces.getObjects());
	}

	/**
	 * Faces as array.
	 *
	 * @return all faces as HE_Face[]
	 */
	@Override
	public HE_Face[] getFacesAsArray() {
		final HE_Face[] faces = new HE_Face[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = this.faces.iterator();
		int i = 0;
		while (fItr.hasNext()) {
			faces[i] = fItr.next();
			i++;
		}
		return faces;
	}

	/**
	 * Return the faces as array of vertex indices.
	 *
	 * @return 2D array of int. First index gives face. Second index gives
	 *         vertices.
	 */
	public int[][] getFacesAsInt() {
		final int[][] result = new int[getNumberOfFaces()][];
		Map<Long, Integer> vertexKeys = getVertexKeyToIndexMap();
		final Iterator<HE_Face> fItr = fItr();
		HE_Halfedge he;
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = new int[f.getFaceDegree()];
			he = f.getHalfedge();
			int j = 0;
			do {
				result[i][j] = vertexKeys.get(he.getVertex().getKey());
				he = he.getNextInFace();
				j++;
			} while (he != f.getHalfedge());
			i++;
		}
		return result;
	}

	public int[] getFaceTextureIds() {
		final int[] result = new int[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getTextureId();
			i++;
		}
		return result;
	}

	/**
	 * Return a KD-tree containing all face centers.
	 *
	 * @return WB_KDTree
	 */
	public WB_KDTree<WB_Coord, Long> getFaceTree() {
		final WB_KDTree<WB_Coord, Long> tree = new WB_KDTree<WB_Coord, Long>();
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			tree.add(HE_MeshOp.getFaceCenter(f), f.getKey());
		}
		return tree;
	}

	public int[] getFaceUserLabels() {
		final int[] result = new int[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getUserLabel();
			i++;
		}
		return result;
	}

	public boolean[] getFaceVisibility() {
		final boolean[] result = new boolean[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.isVisible();
			i++;
		}
		return result;
	}

	/**
	 * Get face with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            face index
	 * @return
	 */
	@Override
	public HE_Face getFaceWithIndex(final int i) {
		if (i < 0 || i >= faces.size()) {
			throw new IndexOutOfBoundsException(
					"Requested face index " + i + "not in range.");
		}
		return faces.getWithIndex(i);
	}

	/**
	 * Get face with key. The key of a mesh element is unique and never changes.
	 *
	 * @param key
	 *            face key
	 * @return face
	 */
	@Override
	public HE_Face getFaceWithKey(final long key) {
		return faces.getWithKey(key);
	}

	/**
	 * Return a halfedge from vertex v0 to vertex v1. If no such halfedge exists
	 * return null.
	 *
	 * @param v0
	 * @param v1
	 * @return
	 */
	public HE_Halfedge getHalfedgeFromTo(final HE_Vertex v0,
			final HE_Vertex v1) {
		final List<HE_Halfedge> hes = v0.getHalfedgeStar();
		for (final HE_Halfedge he : hes) {
			if (he.getEndVertex() == v1) {
				return he;
			}
		}
		return null;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public List<HE_Halfedge> getHalfedges() {
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		halfedges.addAll(this.halfedges);
		halfedges.addAll(this.edges);
		halfedges.addAll(this.unpairedHalfedges);
		return halfedges;
	}

	/**
	 * Halfedges as array.
	 *
	 * @return all halfedges as HE_Halfedge[]
	 */
	@Override
	public HE_Halfedge[] getHalfedgesAsArray() {
		List<HE_Halfedge> hes = getHalfedges();
		final HE_Halfedge[] halfedges = new HE_Halfedge[hes.size()];
		int i = 0;
		for (HE_Halfedge he : hes) {
			halfedges[i] = he;
			i++;
		}
		return halfedges;
	}

	/**
	 * Get halfedge with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            halfedge index
	 * @return
	 */
	@Override
	public HE_Halfedge getHalfedgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size() + halfedges.size()
				+ unpairedHalfedges.size()) {
			throw new IndexOutOfBoundsException(
					"Requested halfedge index " + i + "not in range.");
		}
		if (i >= edges.size() + halfedges.size()) {
			return unpairedHalfedges
					.getWithIndex(i - edges.size() - halfedges.size());
		} else if (i >= edges.size()) {
			return halfedges.getWithIndex(i - edges.size());
		}
		return edges.getWithIndex(i);
	}

	/**
	 * Get halfedge with key. The key of a mesh element is unique and never
	 * changes.
	 *
	 * @param key
	 *            halfedge key
	 * @return halfedge
	 */
	@Override
	public HE_Halfedge getHalfedgeWithKey(final long key) {
		HE_Halfedge he = edges.getWithKey(key);
		if (he != null) {
			return he;
		}
		he = halfedges.getWithKey(key);
		if (he != null) {
			return he;
		}
		return unpairedHalfedges.getWithKey(key);
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	@Override
	public int getIndex(final HE_Face f) {
		return faces.indexOf(f);
	}

	/**
	 *
	 *
	 * @param edge
	 * @return
	 */
	@Override
	public int getIndex(final HE_Halfedge edge) {
		return edges.indexOf(edge);
	}

	/**
	 *
	 *
	 * @param v
	 * @return
	 */
	@Override
	public int getIndex(final HE_Vertex v) {
		return vertices.indexOf(v);
	}

	/**
	 * Return all edge centers.
	 *
	 * @return UnifiedMap of WB_Coordinate.
	 */
	public Map<Long, WB_Coord> getKeyedEdgeCenters() {
		final Map<Long, WB_Coord> result = new UnifiedMap<Long, WB_Coord>();
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result.put(e.getKey(), HE_MeshOp.getHalfedgeCenter(e));
		}
		return result;
	}

	/**
	 * Return all edge normals.
	 *
	 * @return UnifiedMap of WB_Coordinate.
	 */
	public Map<Long, WB_Coord> getKeyedEdgeNormals() {
		final Map<Long, WB_Coord> result = new UnifiedMap<Long, WB_Coord>();
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result.put(e.getKey(), HE_MeshOp.getEdgeNormal(e));
		}
		return result;
	}

	/**
	 * Return all face centers.
	 *
	 * @return UnifiedMap of WB_Coordinate.
	 */
	public Map<Long, WB_Coord> getKeyedFaceCenters() {
		final Map<Long, WB_Coord> result = new UnifiedMap<Long, WB_Coord>();
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result.put(f.getKey(), HE_MeshOp.getFaceCenter(f));
		}
		return result;
	}

	/**
	 * Return all face normals.
	 *
	 * @return UnifiedMap of WB_Coordinate.
	 */
	public Map<Long, WB_Coord> getKeyedFaceNormals() {
		final Map<Long, WB_Coord> result = new UnifiedMap<Long, WB_Coord>();
		HE_Face f;
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result.put(f.getKey(), HE_MeshOp.getFaceNormal(f));
		}
		return result;
	}

	/**
	 * Get vertex normals mapped on vertex key
	 *
	 *
	 * @return
	 */
	public Map<Long, WB_Coord> getKeyedVertexNormals() {
		final Map<Long, WB_Coord> result = new UnifiedMap<Long, WB_Coord>();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result.put(v.getKey(), HE_MeshOp.getVertexNormal(v));
		}
		return result;
	}

	public double getMeanEdgeLength() {
		double sum = 0;
		HE_EdgeIterator eItr = this.eItr();
		while (eItr.hasNext()) {
			sum += HE_MeshOp.getLength(eItr.next());
		}
		return sum / this.getNumberOfEdges();
	}

	@Override
	public String getName() {
		return name;
	}

	public HE_Selection getNewSelection() {
		return HE_Selection.getSelection(this);
	}

	public HE_Selection getNewSelection(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		replaceSelection(name, sel);
		return sel;
	}

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public WB_Network getNetwork() {
		final WB_Network frame = new WB_Network(getVerticesAsCoord());
		final LongIntHashMap map = new LongIntHashMap();
		Map<Long, Integer> indexMap = getVertexKeyToIndexMap();
		for (Entry<Long, Integer> entry : indexMap.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		final Iterator<HE_Halfedge> eItr = eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			frame.addConnection(map.get(e.getVertex().getKey()),
					map.get(e.getEndVertex().getKey()));
		}
		return frame;
	}
	
	public WB_Frame getFrame() {
		final WB_Frame frame = new WB_Frame(getVerticesAsCoord());
		final LongIntHashMap map = new LongIntHashMap();
		Map<Long, Integer> indexMap = getVertexKeyToIndexMap();
		for (Entry<Long, Integer> entry : indexMap.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		final Iterator<HE_Halfedge> eItr = eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			frame.addStrut(map.get(e.getVertex().getKey()),
					map.get(e.getEndVertex().getKey()));
		}
		return frame;
	}

	/**
	 * Number of edges.
	 *
	 * @return the number of edges
	 */
	@Override
	public int getNumberOfEdges() {
		return edges.size();
	}

	/**
	 * Number of faces.
	 *
	 * @return the number of faces
	 */
	@Override
	public int getNumberOfFaces() {
		return faces.size();
	}

	/**
	 * Number of halfedges.
	 *
	 * @return the number of halfedges
	 */
	@Override
	public int getNumberOfHalfedges() {
		return halfedges.size() + edges.size() + unpairedHalfedges.size();
	}

	/**
	 * Number of vertices.
	 *
	 * @return the number of vertices
	 */
	@Override
	public int getNumberOfVertices() {
		return vertices.size();
	}

	@Override
	public WB_CoordCollection getPoints() {
		final List<WB_Coord> result = new FastList<WB_Coord>();
		result.addAll(vertices.getObjects());
		return WB_CoordCollection.getCollection(result);
	}

	/**
	 * Gets the polygon list.
	 *
	 * @return the polygon list
	 */
	public List<WB_Polygon> getPolygonList() {
		final List<WB_Polygon> result = new FastList<WB_Polygon>();
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			result.add(HE_MeshOp.getPolygon(f));
		}
		return result;
	}

	/**
	 * Return the mesh as polygon soup.
	 *
	 * @return array of WB_polygon
	 *
	 */
	public WB_Polygon[] getPolygons() {
		final WB_Polygon[] result = new WB_Polygon[getNumberOfFaces()];
		final Iterator<HE_Face> fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getPolygon(f);
			i++;
		}
		return result;
	}

	/**
	 * Gets the segments.
	 *
	 * @return the segments
	 */
	public WB_Segment[] getSegments() {
		final WB_Segment[] result = new WB_Segment[getNumberOfEdges()];
		final Iterator<HE_Halfedge> eItr = eItr();
		HE_Halfedge e;
		int i = 0;
		while (eItr.hasNext()) {
			e = eItr.next();
			result[i] = new WB_Segment(e.getVertex(), e.getEndVertex());
			i++;
		}
		return result;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public HE_Selection getSelection(final String name) {
		HE_Selection sel = selections.get(name);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + name + " not found.");
			return new HE_Selection(this);
		}
		return sel;
	}

	/**
	 *
	 * @return
	 */
	public Set<String> getSelectionNames() {
		return selections.keySet();
	}

	/**
	 *
	 *
	 * @param v1
	 * @param v2
	 * @return
	 */
	public List<HE_Face> getSharedFaces(final HE_Vertex v1,
			final HE_Vertex v2) {
		final List<HE_Face> result = v1.getFaceStar();
		final List<HE_Face> compare = v2.getFaceStar();
		final Iterator<HE_Face> it = result.iterator();
		while (it.hasNext()) {
			if (!compare.contains(it.next())) {
				it.remove();
			}
		}
		return result;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public int[] getTriangles() {
		if (triangles == null) {
			final HE_Mesh trimesh = this.copy();
			HE_MeshOp.triangulate(this);
			triangles = new int[trimesh.getNumberOfFaces()];
			final Iterator<HE_Face> fItr = trimesh.fItr();
			HE_Face f;
			int id = 0;
			while (fItr.hasNext()) {
				f = fItr.next();
				triangles[id++] = trimesh.getIndex(f.getHalfedge().getVertex());
			}
		}
		return triangles;
	}

	/**
	 * Collect all unpaired halfedges.
	 *
	 * @return the unpaired halfedges
	 */
	public List<HE_Halfedge> getUnpairedHalfedges() {
		final List<HE_Halfedge> halfedges = new FastList<HE_Halfedge>();
		halfedges.addAll(this.unpairedHalfedges);
		return halfedges;
	}

	public WB_Coord getVertex(final int i) {
		return getVertexWithIndex(i);
	}

	public int[] getVertexColors() {
		final int[] result = new int[getNumberOfVertices()];
		final Iterator<HE_Vertex> vItr = vItr();
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = v.getColor();
			i++;
		}
		return result;
	}

	public int[] getVertexInternalLabels() {
		final int[] result = new int[getNumberOfVertices()];
		final Iterator<HE_Vertex> vItr = vItr();
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = v.getInternalLabel();
			i++;
		}
		return result;
	}

	/**
	 * Vertex key to index.
	 *
	 * @return the map
	 */
	public Map<Long, Integer> getVertexKeyToIndexMap() {
		final Map<Long, Integer> map = new UnifiedMap<Long, Integer>();
		int i = 0;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			map.put(vItr.next().getKey(), i);
			i++;
		}
		return map;
	}

	public WB_Coord getVertexNormal(final int i) {
		return HE_MeshOp.getVertexNormal(getVertexWithIndex(i));
	}

	/**
	 * Return all vertex normals.
	 *
	 * @return array of WB_Coordinate.
	 */
	public WB_Coord[] getVertexNormals() {
		final WB_Coord[] result = new WB_Coord[getNumberOfVertices()];
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = HE_MeshOp.getVertexNormal(v);
			i++;
		}
		return result;
	}

	/**
	 * Return a KD-tree containing all vertices.
	 *
	 * @return WB_KDTree
	 */
	public WB_KDTree<WB_Coord, Long> getVertexTree() {
		final WB_KDTree<WB_Coord, Long> tree = new WB_KDTree<WB_Coord, Long>();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			tree.add(v, v.getKey());
		}
		return tree;
	}

	public int[] getVertexUserLabels() {
		final int[] result = new int[getNumberOfVertices()];
		final Iterator<HE_Vertex> vItr = vItr();
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = v.getUserLabel();
			i++;
		}
		return result;
	}

	public boolean[] getVertexVisibility() {
		final boolean[] result = new boolean[getNumberOfVertices()];
		final Iterator<HE_Vertex> vItr = vItr();
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = v.isVisible();
			i++;
		}
		return result;
	}

	/**
	 * Get vertex with index. Indices of mesh elements are not fixed and will
	 * change when the mesh is modified.
	 *
	 * @param i
	 *            vertex index
	 * @return
	 */
	@Override
	public HE_Vertex getVertexWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException(
					"Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i);
	}

	public WB_Point getPositionWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException(
					"Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i).getPosition();
	}

	/**
	 * Get vertex with key. The key of a mesh element is unique and never
	 * changes.
	 *
	 * @param key
	 *            vertex key
	 * @return vertex
	 */
	@Override
	public HE_Vertex getVertexWithKey(final long key) {
		return vertices.getWithKey(key);
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	public List<HE_Vertex> getVertices() {
		return new FastList<HE_Vertex>(vertices.getObjects());
	}

	/**
	 * Vertices as array.
	 *
	 * @return all vertices as HE_Vertex[]
	 */
	@Override
	public HE_Vertex[] getVerticesAsArray() {
		final HE_Vertex[] vertices = new HE_Vertex[getNumberOfVertices()];
		final Collection<HE_Vertex> _vertices = this.vertices;
		final Iterator<HE_Vertex> vitr = _vertices.iterator();
		int i = 0;
		while (vitr.hasNext()) {
			vertices[i] = vitr.next();
			i++;
		}
		return vertices;
	}

	/**
	 * Return all vertex positions as an immutable List of immutable WB_Coord.
	 *
	 * @return array of WB_Coord.
	 */
	public List<WB_Coord> getVerticesAsCoord() {
		final FastList<WB_Coord> result = new FastList<WB_Coord>();
		;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result.add(v);
		}
		return result.asUnmodifiable();
	}

	/**
	 * Return all vertex positions as an array .
	 *
	 * @return 2D array of double. First index gives vertex. Second index gives
	 *         x-,y- or z-coordinate.
	 */
	public double[][] getVerticesAsDouble() {
		final double[][] result = new double[getNumberOfVertices()][3];
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i][0] = v.xd();
			result[i][1] = v.yd();
			result[i][2] = v.zd();
			i++;
		}
		return result;
	}

	/**
	 * Return all vertex positions as an array .
	 *
	 * @return 2D array of float. First index gives vertex. Second index gives
	 *         x-,y- or z-coordinate.
	 */
	public float[][] getVerticesAsFloat() {
		final float[][] result = new float[getNumberOfVertices()][3];
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i][0] = v.xf();
			result[i][1] = v.yf();
			result[i][2] = v.zf();
			i++;
		}
		return result;
	}

	/**
	 * Halfedge iterator.
	 *
	 * @return halfedge iterator
	 */
	@Override
	public HE_HalfedgeIterator heItr() {
		return HE_HalfedgeIterator.getIterator(edges, halfedges,
				unpairedHalfedges);
	}

	public boolean isFinished() {
		return finished;
	}

	/**
	 *
	 *
	 * @param partition1
	 * @param partition2
	 * @return
	 */
	boolean isNeighbor(final HE_RAS<HE_Face> partition1,
			final HE_RAS<HE_Face> partition2) {
		HE_Halfedge he1, he2;
		HE_Vertex v1;
		HE_FaceHalfedgeInnerCirculator heitr1, heitr2;
		for (final HE_Face f1 : partition1) {
			heitr1 = new HE_FaceHalfedgeInnerCirculator(f1);
			while (heitr1.hasNext()) {
				he1 = heitr1.next();
				if (he1.getPair() == null) {
					v1 = he1.getNextInFace().getVertex();
					for (final HE_Face f2 : partition2) {
						heitr2 = new HE_FaceHalfedgeInnerCirculator(f2);
						while (heitr2.hasNext()) {
							he2 = heitr2.next();
							if (he2.getPair() == null
									&& he2.getNextInFace().getVertex() == v1) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isSurface() {
		return this.getAllBoundaryHalfedges().size() > 0;
	}

	/**
	 * Modify the mesh.
	 *
	 * @param modifier
	 *            HE_Modifier to apply
	 * @return self
	 */
	@Override
	public HE_Mesh modify(final HEM_Modifier modifier) {
		if (finished) {
			modifier.apply(this);
			clearPrecomputed();
		} else {
			modifyThreaded(modifier);
		}
		return this;
	}

	public void modifyThreaded(final HEM_Modifier modifier) {
		tasks.add(new ModifierThread(modifier, this));
	}

	/**
	 * Create translated copy of mesh.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return copy
	 */
	public HE_Mesh move(final double x, final double y, final double z) {
		HE_Mesh result = copy();
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x, y, z);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Created translated copy of mesh.
	 *
	 * @param v
	 *
	 * @return copy
	 */
	public HE_Mesh move(final WB_Coord v) {
		return move(v.xd(), v.yd(), v.zd());
	}

	/**
	 * Translate entire mesh.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return self
	 */
	public HE_Mesh moveSelf(final double x, final double y, final double z) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x, y, z);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Translate entire mesh.
	 *
	 * @param v
	 *            the v
	 * @return self
	 */
	public HE_Mesh moveSelf(final WB_Coord v) {
		return moveSelf(v.xd(), v.yd(), v.zd());
	}

	/**
	 * Create copy of mesh at given position.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return copy
	 */
	public HE_Mesh moveTo(final double x, final double y, final double z) {
		HE_Mesh result = copy();
		WB_Point center = HE_MeshOp.getCenter(result);
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x - center.xd(), y - center.yd(),
					z - center.zd());
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * create copy of mesh at given position.
	 *
	 * @param v
	 *
	 * @return copy
	 */
	public HE_Mesh moveTo(final WB_Coord v) {
		return moveTo(v.xd(), v.yd(), v.zd());
	}

	/**
	 * Translate entire mesh to given position.
	 *
	 * @param x
	 *
	 * @param y
	 *
	 * @param z
	 *
	 * @return self
	 */
	public HE_Mesh moveToSelf(final double x, final double y, final double z) {
		WB_Point center = HE_MeshOp.getCenter(this);
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x - center.xd(), y - center.yd(),
					z - center.zd());
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Translate entire mesh to given position.
	 *
	 * @param v
	 *
	 * @return self
	 */
	public HE_Mesh moveToSelf(final WB_Coord v) {
		return moveToSelf(v.xd(), v.yd(), v.zd());
	}

	/**
	 * Removes face.
	 *
	 * @param f
	 *            face to remove
	 */
	@Override
	public void remove(final HE_Face f) {
		faces.remove(f);
		for (HE_Selection sel : selections.values()) {
			sel.remove(f);
		}
	}

	/**
	 * Removes halfedge.
	 *
	 * @param he
	 *            halfedge to remove
	 */
	@Override
	public void remove(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
		unpairedHalfedges.remove(he);
		for (HE_Selection sel : selections.values()) {
			sel.remove(he);
		}
	}

	/**
	 * Removes vertex.
	 *
	 * @param v
	 *            vertex to remove
	 */
	@Override
	public void remove(final HE_Vertex v) {
		vertices.remove(v);
		for (HE_Selection sel : selections.values()) {
			sel.remove(v);
		}
	}

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as Collection<? extends HE_Halfedge>
	 */
	@Override
	public void removeEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			remove(e);
		}
	}

	/**
	 * Removes edges.
	 *
	 * @param edges
	 *            edges to remove as HE_Halfedge[]
	 */
	@Override
	public void removeEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			remove(edge);
		}
	}

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as Collection<? extends HE_Face>
	 */
	@Override
	public void removeFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			remove(f);
		}
	}

	/**
	 * Removes faces.
	 *
	 * @param faces
	 *            faces to remove as HE_Face[]
	 */
	@Override
	public void removeFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			remove(face);
		}
	}

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as Collection<? extends HE_Halfedge>
	 */
	@Override
	public void removeHalfedges(
			final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			remove(he);
		}
	}

	/**
	 * Removes halfedges.
	 *
	 * @param halfedges
	 *            halfedges to remove as HE_Halfedge[]
	 */
	@Override
	public void removeHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			remove(halfedge);
		}
	}

	void removeNoSelectionCheck(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
		unpairedHalfedges.remove(he);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public HE_Selection removeSelection(final String name) {
		HE_Selection prevsel = selections.remove(name);
		if (prevsel == null) {
			tracker.setDuringStatus(this, "Selection " + name + " not found.");
		} else {
			tracker.setDuringStatus(this, "Removed selection " + name + ".");
		}
		return prevsel;
	}

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as Collection<? extends HE_Vertex>
	 */
	@Override
	public void removeVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			remove(v);
		}
	}

	/**
	 * Removes vertices.
	 *
	 * @param vertices
	 *            vertices to remove as HE_Vertex[]
	 */
	@Override
	public void removeVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertice : vertices) {
			remove(vertice);
		}
	}

	/**
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean renameSelection(final String from, final String to) {
		HE_Selection sel = removeSelection(from);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + from + " not found.");
			return false;
		}
		replaceSelection(to, sel);
		tracker.setDuringStatus(this,
				"Renamed selection " + from + " to " + to + ".");
		return true;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	private void replaceFaces(final HE_Mesh mesh) {
		clearFaces();
		addFaces(mesh.faces);
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	private void replaceHalfedges(final HE_Mesh mesh) {
		clearHalfedges();
		HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			add(heItr.next());
		}
	}

	HE_Selection replaceSelection(final String name, final HE_Machine machine,
			final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			sel.createdBy = machine.getName();
			HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this,
						"Replacing selection " + name + ".");
				removeSelection(name);
				selections.put(name, sel);
			}
			return prevsel;
		} else {
			tracker.setDuringStatus(this, "Selection " + name
					+ " not added: selection is null or parent mesh is not the same.");
		}
		return null;
	}

	/**
	 *
	 * @param name
	 * @param sel
	 * @return
	 */
	public HE_Selection replaceSelection(final String name,
			final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this,
						"Replacing selection " + name + ".");
				removeSelection(name);
				selections.put(name, sel);
			}
			return prevsel;
		} else {
			tracker.setDuringStatus(this, "Selection " + name
					+ " not added: selection is null or parent mesh is not the same.");
		}
		return null;
	}

	/**
	 *
	 *
	 * @param mesh
	 */
	private void replaceVertices(final HE_Mesh mesh) {
		clearVertices();
		addVertices(mesh.vertices);
	}

	/**
	 * Reset all edge labels to -1.
	 *
	 */
	protected void resetEdgeInternalLabels() {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setInternalLabel(-1);
		}
	}

	/**
	 * Reset all edge labels to -1.
	 */
	public void resetEdgeUserLabels() {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setUserLabel(-1);
		}
	}

	/**
	 * Reset face labels to -1.
	 *
	 *
	 */
	protected void resetFaceInternalLabels() {
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(-1);
		}
	}

	/**
	 * Reset all face labels to -1.
	 */
	public void resetFaceUserLabels() {
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setUserLabel(-1);
		}
	}

	/**
	 * Reset all halfedge labels to -1.
	 *
	 */
	protected void resetHalfedgeInternalLabels() {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setInternalLabel(-1);
		}
	}

	/**
	 * Reset all halfedge labels to -1.
	 */
	public void resetHalfedgeUserLabels() {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setUserLabel(-1);
		}
	}

	/**
	 * Reset all internal labels to -1.
	 *
	 */
	protected void resetInternalLabels() {
		resetVertexInternalLabels();
		resetFaceInternalLabels();
		resetHalfedgeInternalLabels();
	}

	/**
	 * Reset all labels.
	 */
	public void resetUserLabels() {
		resetVertexUserLabels();
		resetFaceUserLabels();
		resetHalfedgeUserLabels();
	}

	/**
	 * Reset all vertex labels to -1.
	 *
	 *
	 */
	protected void resetVertexInternalLabels() {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setInternalLabel(-1);
		}
	}

	/**
	 * Reset all vertex labels to -1.
	 */
	public void resetVertexUserLabels() {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setUserLabel(-1);
		}
	}

	/**
	 * Rotate entire mesh around an arbitrary axis defined by a point and a
	 * direction.
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return copy
	 */
	public HE_Mesh rotateAboutAxis(final double angle, final double px,
			final double py, final double pz, final double ax, final double ay,
			final double az) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(px, py, pz),
				new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create copy of mesh rotated around an arbitrary axis defined by a point
	 * and a direction.
	 *
	 * @param angle
	 *            angle
	 * @param p
	 *            rotation point
	 * @param a
	 *            axis
	 * @return copy
	 */
	public HE_Mesh rotateAboutAxis(final double angle, final WB_Coord p,
			final WB_Coord a) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create rotated copy of mesh around an arbitrary axis defined by 2 points.
	 *
	 * @param angle
	 *            angle
	 * @param p1x
	 *            x-coordinate of first point on axis
	 * @param p1y
	 *            y-coordinate of first point on axis
	 * @param p1z
	 *            z-coordinate of first point on axis
	 * @param p2x
	 *            x-coordinate of second point on axis
	 * @param p2y
	 *            y-coordinate of second point on axis
	 * @param p2z
	 *            z-coordinate of second point on axis
	 * @return copy
	 */
	public HE_Mesh rotateAboutAxis2P(final double angle, final double p1x,
			final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(p1x, p1y, p1z),
				new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create copy of mesh rotated around an arbitrary axis defined by 2 points.
	 *
	 * @param angle
	 *            angle
	 * @param p1
	 *            first point on axis
	 * @param p2
	 *            second point on axis
	 * @return copy
	 */
	public HE_Mesh rotateAboutAxis2P(final double angle, final WB_Coord p1,
			final WB_Coord p2) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis defined by 2 points.
	 *
	 * @param angle
	 *            angle
	 * @param p1x
	 *            x-coordinate of first point on axis
	 * @param p1y
	 *            y-coordinate of first point on axis
	 * @param p1z
	 *            z-coordinate of first point on axis
	 * @param p2x
	 *            x-coordinate of second point on axis
	 * @param p2y
	 *            y-coordinate of second point on axis
	 * @param p2z
	 *            z-coordinate of second point on axis
	 * @return self
	 */
	public HE_Mesh rotateAboutAxis2PSelf(final double angle, final double p1x,
			final double p1y, final double p1z, final double p2x,
			final double p2y, final double p2z) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(p1x, p1y, p1z),
				new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis defined by 2 points..
	 *
	 * @param angle
	 *            angle
	 * @param p1
	 *            first point on axis
	 * @param p2
	 *            second point on axis
	 * @return self
	 */
	public HE_Mesh rotateAboutAxis2PSelf(final double angle, final WB_Coord p1,
			final WB_Coord p2) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p1, new WB_Vector(p1, p2));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis defined by a point and a
	 * direction.
	 *
	 * @param angle
	 * @param px
	 * @param py
	 * @param pz
	 * @param ax
	 * @param ay
	 * @param az
	 * @return self
	 */
	public HE_Mesh rotateAboutAxisSelf(final double angle, final double px,
			final double py, final double pz, final double ax, final double ay,
			final double az) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(px, py, pz),
				new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis defined by a point and a
	 * direction.
	 *
	 * @param angle
	 *            angle
	 * @param p
	 *            rotation point
	 * @param a
	 *            axis
	 * @return self
	 */
	public HE_Mesh rotateAboutAxisSelf(final double angle, final WB_Coord p,
			final WB_Coord a) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, p, a);
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Create copy of mesh rotated around an arbitrary axis in center.
	 *
	 * @param angle
	 * @param ax
	 * @param ay
	 * @param az
	 * @return copy
	 */
	public HE_Mesh rotateAboutCenter(final double angle, final double ax,
			final double ay, final double az) {
		return rotateAboutAxis(angle, HE_MeshOp.getCenter(this),
				new WB_Vector(ax, ay, az));
	}

	/**
	 * Create copy of mesh rotated around an arbitrary axis in center.
	 *
	 * @param angle
	 *            angle
	 * @param a
	 *            axis
	 * @return self
	 */
	public HE_Mesh rotateAboutCenter(final double angle, final WB_Coord a) {
		return rotateAboutAxis(angle, HE_MeshOp.getCenter(this), a);
	}

	/**
	 * Rotate entire mesh around an arbitrary axis in center.
	 *
	 * @param angle
	 * @param ax
	 * @param ay
	 * @param az
	 * @return
	 */
	public HE_Mesh rotateAboutCenterSelf(final double angle, final double ax,
			final double ay, final double az) {
		return rotateAboutAxisSelf(angle, HE_MeshOp.getCenter(this),
				new WB_Vector(ax, ay, az));
	}

	/**
	 * Rotate entire mesh around an arbitrary axis in center.
	 *
	 * @param angle
	 *            angle
	 * @param a
	 *            axis
	 * @return self
	 */
	public HE_Mesh rotateAboutCenterSelf(final double angle, final WB_Coord a) {
		return rotateAboutAxisSelf(angle, HE_MeshOp.getCenter(this), a);
	}

	/**
	 * Create copy of mesh rotated around an arbitrary axis in origin.
	 *
	 *
	 * @param angle
	 * @param ax
	 * @param ay
	 * @param az
	 * @return copy
	 */
	public HE_Mesh rotateAboutOrigin(final double angle, final double ax,
			final double ay, final double az) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create copy of mesh rotate around an arbitrary axis in origin.
	 *
	 * @param angle
	 *            angle
	 * @param a
	 *            axis
	 * @return copy
	 */
	public HE_Mesh rotateAboutOrigin(final double angle, final WB_Coord a) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis in origin.
	 *
	 *
	 * @param angle
	 * @param ax
	 * @param ay
	 * @param az
	 * @return
	 */
	public HE_Mesh rotateAboutOriginSelf(final double angle, final double ax,
			final double ay, final double az) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Rotate entire mesh around an arbitrary axis in origin.
	 *
	 * @param angle
	 *            angle
	 * @param a
	 *            axis
	 * @return self
	 */
	public HE_Mesh rotateAboutOriginSelf(final double angle, final WB_Coord a) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutOrigin(angle, a);
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Create copy of mesh scaled around bodycenter.
	 *
	 * @param scaleFactor
	 *            scale
	 * @return copy
	 */
	public HE_Mesh scale(final double scaleFactor) {
		return scale(scaleFactor, scaleFactor, scaleFactor);
	}

	/**
	 * Create copy of mesh scaled around bodycenter.
	 *
	 * @param scaleFactorx
	 *            x-coordinate of scale factor
	 * @param scaleFactory
	 *            y-coordinate of scale factor
	 * @param scaleFactorz
	 *            z-coordinate of scale factor
	 * @return copy
	 */
	public HE_Mesh scale(final double scaleFactorx, final double scaleFactory,
			final double scaleFactorz) {
		HE_Mesh result = copy();
		WB_Point center = HE_MeshOp.getCenter(result);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(center.xd() + scaleFactorx * (v.xd() - center.xd()),
					center.yd() + scaleFactory * (v.yd() - center.yd()),
					center.zd() + scaleFactorz * (v.zd() - center.zd()));
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create copy of mesh scaled around center point.
	 *
	 * @param scaleFactorx
	 *            x-coordinate of scale factor
	 * @param scaleFactory
	 *            y-coordinate of scale factor
	 * @param scaleFactorz
	 *            z-coordinate of scale factor
	 * @param c
	 *            center
	 * @return copy
	 */
	public HE_Mesh scale(final double scaleFactorx, final double scaleFactory,
			final double scaleFactorz, final WB_Coord c) {
		HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(c.xd() + scaleFactorx * (v.xd() - c.xd()),
					c.yd() + scaleFactory * (v.yd() - c.yd()),
					c.zd() + scaleFactorz * (v.zd() - c.zd()));
		}
		result.clearPrecomputed();
		return result;
	}

	/**
	 * Create copy of mesh scaled around center point.
	 *
	 * @param scaleFactor
	 *            scale
	 * @param c
	 *            center
	 * @return copy
	 */
	public HE_Mesh scale(final double scaleFactor, final WB_Coord c) {
		return scale(scaleFactor, scaleFactor, scaleFactor, c);
	}

	/**
	 * Scale entire mesh around bodycenter.
	 *
	 * @param scaleFactor
	 *            scale
	 * @return self
	 */
	public HE_Mesh scaleSelf(final double scaleFactor) {
		return scaleSelf(scaleFactor, scaleFactor, scaleFactor);
	}

	/**
	 * Scale entire mesh around bodycenter.
	 *
	 * @param scaleFactorx
	 *            x-coordinate of scale factor
	 * @param scaleFactory
	 *            y-coordinate of scale factor
	 * @param scaleFactorz
	 *            z-coordinate of scale factor
	 * @return self
	 */
	public HE_Mesh scaleSelf(final double scaleFactorx,
			final double scaleFactory, final double scaleFactorz) {
		WB_Point center = HE_MeshOp.getCenter(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(center.xd() + scaleFactorx * (v.xd() - center.xd()),
					center.yd() + scaleFactory * (v.yd() - center.yd()),
					center.zd() + scaleFactorz * (v.zd() - center.zd()));
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Scale entire mesh around center point.
	 *
	 * @param scaleFactorx
	 *            x-coordinate of scale factor
	 * @param scaleFactory
	 *            y-coordinate of scale factor
	 * @param scaleFactorz
	 *            z-coordinate of scale factor
	 * @param c
	 *            center
	 * @return self
	 */
	public HE_Mesh scaleSelf(final double scaleFactorx,
			final double scaleFactory, final double scaleFactorz,
			final WB_Coord c) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(c.xd() + scaleFactorx * (v.xd() - c.xd()),
					c.yd() + scaleFactory * (v.yd() - c.yd()),
					c.zd() + scaleFactorz * (v.zd() - c.zd()));
		}
		clearPrecomputed();
		return this;
	}

	/**
	 * Scale entire mesh around center point.
	 *
	 * @param scaleFactor
	 *            scale
	 * @param c
	 *            center
	 * @return self
	 */
	public HE_Mesh scaleSelf(final double scaleFactor, final WB_Coord c) {
		return scaleSelf(scaleFactor, scaleFactor, scaleFactor, c);
	}

	/**
	 * Select all mesh elements. Unnamed selections are not stored in the mesh
	 * and are not updated.
	 *
	 * @return current selection
	 */
	public HE_Selection selectAll() {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		sel.addHalfedges(this.halfedges);
		sel.addVertices(this.vertices);
		return sel;
	}

	/**
	 * Select all mesh elements.
	 *
	 * @return current selection
	 */
	public HE_Selection selectAll(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		sel.addHalfedges(this.halfedges);
		sel.addVertices(this.vertices);
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllEdges() {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addEdges(this.edges);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllEdges(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addEdges(this.edges);
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllFaces() {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllFaces(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllHalfedges() {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addHalfedges(this.edges);
		sel.addHalfedges(this.halfedges);
		sel.addHalfedges(this.unpairedHalfedges);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllHalfedges(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addHalfedges(this.edges);
		sel.addHalfedges(this.halfedges);
		sel.addHalfedges(this.unpairedHalfedges);
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllInnerBoundaryHalfedges() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getPair().getFace() == null) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllInnerBoundaryHalfedges(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getPair().getFace() == null) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllOuterBoundaryHalfedges() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllOuterBoundaryHalfedges(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllVertices() {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addVertices(this.vertices);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectAllVertices(final String name) {
		HE_Selection sel = HE_Selection.getSelection(this);
		sel.addVertices(this.vertices);
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackEdges(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.BACK) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.BACK) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackFaces(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.BACK) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.BACK) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackVertices(final String name,
			final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.BACK) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectBackVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.BACK) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryEdges() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.isInnerBoundary()) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryEdges(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.isInnerBoundary()) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryFaces() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he.getPair().getFace());
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryFaces(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he.getPair().getFace());
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryVertices() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he.getVertex());
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 */
	public HE_Selection selectBoundaryVertices(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final Iterator<HE_Halfedge> heItr = this.heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getFace() == null) {
				sel.add(he.getVertex());
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectCrossingEdges(final String name,
			final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.CROSSING) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectCrossingEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.CROSSING) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectCrossingFaces(final String name,
			final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.CROSSING) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectCrossingFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.CROSSING) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getUserLabel() == label) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getUserLabel() == label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithOtherInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getInternalLabel() != label) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithOtherInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getInternalLabel() != label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getUserLabel() != label) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithOtherLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getUserLabel() != label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getInternalLabel() == label) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectEdgesWithInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getInternalLabel() == label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() == label) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() == label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getUserLabel() == label) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getUserLabel() == label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param v
	 */
	public HE_Selection selectFacesWithNormal(final String name,
			final WB_Coord v) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector w = new WB_Vector(v);
		w.normalizeSelf();
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (WB_Vector.dot(HE_MeshOp.getFaceNormal(f), v) > 1.0
					- WB_Epsilon.EPSILON) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param n
	 * @param ta
	 */
	public HE_Selection selectFacesWithNormal(final String name,
			final WB_Coord n, final double ta) {
		HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector nn = new WB_Vector(n);
		nn.normalizeSelf();
		final double cta = Math.cos(ta);
		HE_FaceIterator fItr = sel.getParent().fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (nn.dot(HE_MeshOp.getFaceNormal(f)) > cta) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param v
	 */
	public HE_Selection selectFacesWithNormal(final WB_Coord v) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector w = new WB_Vector(v);
		w.normalizeSelf();
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (WB_Vector.dot(HE_MeshOp.getFaceNormal(f), v) > 1.0
					- WB_Epsilon.EPSILON) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param n
	 * @param ta
	 */
	public HE_Selection selectFacesWithNormal(final WB_Coord n,
			final double ta) {
		HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector nn = new WB_Vector(n);
		nn.normalizeSelf();
		final double cta = Math.cos(ta);
		HE_FaceIterator fItr = sel.getParent().fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (nn.dot(HE_MeshOp.getFaceNormal(f)) > cta) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithOtherInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() != label) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithOtherInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() != label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getUserLabel() != label) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectFacesWithOtherLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final Iterator<HE_Face> fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getUserLabel() != label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontEdges(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.FRONT) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e,
					P) == WB_Classification.FRONT) {
				sel.add(e);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontFaces(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.FRONT) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f,
					P) == WB_Classification.FRONT) {
				sel.add(f);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontVertices(final String name,
			final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.FRONT) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectFrontVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.FRONT) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getUserLabel() == label) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getUserLabel() == label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithOtherInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getInternalLabel() != label) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithOtherInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getInternalLabel() != label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getUserLabel() != label) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgesWithOtherLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getUserLabel() != label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgeWithInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getInternalLabel() == label) {
				sel.add(he);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectHalfedgeWithInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getInternalLabel() == label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectOnVertices(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.ON) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param P
	 */
	public HE_Selection selectOnVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v,
					P) == WB_Classification.ON) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomEdges(final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e != null) {
				if (Math.random() < r) {
					sel.add(e);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomEdges(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e != null) {
				if (random.nextFloat() < r) {
					sel.add(e);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomEdges(final String name, final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e != null) {
				if (Math.random() < r) {
					sel.add(e);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomEdges(final String name, final double r,
			final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_EdgeIterator eItr = this.eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e != null) {
				if (random.nextFloat() < r) {
					sel.add(e);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomFaces(final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_FaceIterator fItr = this.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f != null) {
				if (Math.random() < r) {
					sel.add(f);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomFaces(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_FaceIterator fItr = this.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f != null) {
				if (random.nextFloat() < r) {
					sel.add(f);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomFaces(final String name, final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_FaceIterator fItr = this.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f != null) {
				if (Math.random() < r) {
					sel.add(f);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomFaces(final String name, final double r,
			final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_FaceIterator fItr = this.fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f != null) {
				if (random.nextFloat() < r) {
					sel.add(f);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomVertices(final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_VertexIterator vItr = this.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v != null) {
				if (Math.random() < r) {
					sel.add(v);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomVertices(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_VertexIterator vItr = this.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v != null) {
				if (random.nextFloat() < r) {
					sel.add(v);
				}
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 */
	public HE_Selection selectRandomVertices(final String name,
			final double r) {
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_VertexIterator vItr = this.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v != null) {
				if (Math.random() < r) {
					sel.add(v);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param r
	 * @param seed
	 */
	public HE_Selection selectRandomVertices(final String name, final double r,
			final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		HE_Selection sel = HE_Selection.getSelection(this);
		HE_VertexIterator vItr = this.vItr();
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v != null) {
				if (random.nextFloat() < r) {
					sel.add(v);
				}
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getInternalLabel() == label) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getInternalLabel() == label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getUserLabel() == label) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getUserLabel() == label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithOtherInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getInternalLabel() != label) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithOtherInternalLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getInternalLabel() != label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getUserLabel() != label) {
				sel.add(v);
			}
		}
		return sel;
	}

	/**
	 *
	 * @param name
	 * @param label
	 */
	public HE_Selection selectVerticesWithOtherLabel(final String name,
			final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getUserLabel() != label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	/**
	 * Replace mesh with deep copy of target.
	 *
	 * @param target
	 *            HE_Mesh to be duplicated
	 */
	public void set(final HE_Mesh target) {
		final HE_Mesh result = target.copy();
		replaceVertices(result);
		replaceFaces(result);
		replaceHalfedges(result);
		selections = target.selections;
	}

	/**
	 * Set edge labels to value.
	 *
	 * @param label
	 *
	 */
	protected void setEdgeInternalLabels(final int label) {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setInternalLabel(label);
		}
	}

	/**
	 * Reset all edge labels to value.
	 *
	 * @param label
	 */
	public void setEdgeUserLabels(final int label) {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setUserLabel(label);
		}
	}

	/**
	 * Link face to halfedge
	 *
	 * @param he
	 * @param f
	 */
	public void setFace(final HE_Halfedge he, final HE_Face f) {
		he.setFace(f);
		if (he.getPair() != null) {
			setPair(he, he.getPair());
		}
	}

	/**
	 *
	 *
	 * @param color
	 */
	public void setFaceColor(final int color) {
		final HE_FaceIterator fitr = fItr();
		while (fitr.hasNext()) {
			fitr.next().setColor(color);
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setFaceColorWithInternalLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setFaceColorWithOtherInternalLabel(final int color,
			final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() != i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setFaceColorWithOtherUserLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getUserLabel() != i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setFaceColorWithUserLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getUserLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 * Set face labels to value.
	 *
	 * @param label
	 */
	protected void setFaceInternalLabels(final int label) {
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(label);
		}
	}

	/**
	 * Reset all face labels to value.
	 *
	 * @param label
	 */
	public void setFaceUserLabels(final int label) {
		final Iterator<HE_Face> fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setUserLabel(label);
		}
	}

	/**
	 * Link halfedge to face
	 *
	 * @param f
	 * @param he
	 */
	public void setHalfedge(final HE_Face f, final HE_Halfedge he) {
		f.setHalfedge(he);
	}

	/**
	 * Link halfedge to vertex
	 *
	 * @param v
	 * @param he
	 */
	public void setHalfedge(final HE_Vertex v, final HE_Halfedge he) {
		v.setHalfedge(he);
	}

	/**
	 *
	 *
	 * @param color
	 */
	public void setHalfedgeColor(final int color) {
		final HE_HalfedgeIterator heitr = heItr();
		while (heitr.hasNext()) {
			heitr.next().setColor(color);
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setHalfedgeColorWithInternalLabel(final int color,
			final int i) {
		final HE_HalfedgeIterator fitr = heItr();
		HE_Halfedge f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setHalfedgeColorWithOtherInternalLabel(final int color,
			final int i) {
		final HE_HalfedgeIterator heitr = heItr();
		HE_Halfedge f;
		while (heitr.hasNext()) {
			f = heitr.next();
			if (f.getInternalLabel() != i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setHalfedgeColorWithOtherUserLabel(final int color,
			final int i) {
		final HE_HalfedgeIterator heitr = heItr();
		HE_Halfedge he;
		while (heitr.hasNext()) {
			he = heitr.next();
			if (he.getUserLabel() != i) {
				he.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setHalfedgeColorWithUserLabel(final int color, final int i) {
		final HE_HalfedgeIterator fitr = heItr();
		HE_Halfedge f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getUserLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 * Det edge labels to value.
	 *
	 * @param label
	 *
	 */
	protected void setHalfedgeInternalLabels(final int label) {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setInternalLabel(label);
		}
	}

	/**
	 * Reset all halfedge labels to value.
	 *
	 * @param label
	 */
	public void setHalfedgeUserLabels(final int label) {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setUserLabel(label);
		}
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Link hen to he as next halfedge, he is linked as previous halfedge to he.
	 *
	 * @param he
	 * @param hen
	 */
	public void setNext(final HE_Halfedge he, final HE_Halfedge hen) {
		he.setNext(hen);
		hen.setPrev(he);
	}

	/**
	 * Replace mesh with shallow copy of target.
	 *
	 * @param target
	 *            HE_Mesh to be duplicated
	 */
	void setNoCopy(final HE_Mesh target) {
		synchronized (this) {
			replaceVertices(target);
			replaceFaces(target);
			replaceHalfedges(target);
			selections = new UnifiedMap<String, HE_Selection>();
			for (String name : target.getSelectionNames()) {
				HE_Selection sel = target.getSelection(name);
				HE_Selection copy = new HE_Selection(this);
				copy.add(sel);
				addSelection(name, copy);
			}
		}
	}

	public void setPair(final HE_Halfedge he1, final HE_Halfedge he2) {
		removeNoSelectionCheck(he1);
		removeNoSelectionCheck(he2);
		he1.setPair(he2);
		he2.setPair(he1);
		addDerivedElement(he1, he2);
		addDerivedElement(he2, he1);
	}

	public void setPairNoSelectionCheck(final HE_Halfedge he1,
			final HE_Halfedge he2) {
		removeNoSelectionCheck(he1);
		removeNoSelectionCheck(he2);
		he1.setPair(he2);
		he2.setPair(he1);
	}

	/**
	 * Link vertex to halfedge
	 *
	 * @param he
	 * @param v
	 */
	public void setVertex(final HE_Halfedge he, final HE_Vertex v) {
		he.setVertex(v);
	}

	public void setVertex(final HE_Vertex v, final double x, final double y) {
		v.set(x, y);
	}

	public void setVertex(final HE_Vertex v, final double x, final double y,
			final double z) {
		v.set(x, y, z);
	}

	public void setVertex(final HE_Vertex v, final WB_Coord c) {
		v.set(c);
	}

	/**
	 *
	 *
	 * @param color
	 */
	public void setVertexColor(final int color) {
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			vitr.next().setColor(color);
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setVertexColorWithInternalLabel(final int color, final int i) {
		final HE_VertexIterator fitr = vItr();
		HE_Vertex f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setVertexColorWithOtherInternalLabel(final int color,
			final int i) {
		final HE_VertexIterator vitr = vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (v.getInternalLabel() != i) {
				v.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setVertexColorWithOtherUserLabel(final int color, final int i) {
		final HE_VertexIterator fitr = vItr();
		HE_Vertex f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getUserLabel() != i) {
				f.setColor(color);
			}
		}
	}

	/**
	 *
	 *
	 * @param color
	 * @param i
	 */
	public void setVertexColorWithUserLabel(final int color, final int i) {
		final HE_VertexIterator fitr = vItr();
		HE_Vertex f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getUserLabel() == i) {
				f.setColor(color);
			}
		}
	}

	/**
	 * Set all vertex labels to value.
	 *
	 * @param label
	 */
	protected void setVertexInternalLabels(final int label) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setInternalLabel(label);
		}
	}

	/**
	 * Set all vertex labels to value.
	 *
	 * @param label
	 */
	public void setVertexUserLabels(final int label) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setUserLabel(label);
		}
	}

	public void setVertexWithIndex(final int index, final double x,
			final double y) {
		HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(x, y);
	}

	public void setVertexWithIndex(final int index, final double x,
			final double y, final double z) {
		HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(x, y, z);
	}

	public void setVertexWithIndex(final int index, final WB_Coord c) {
		HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(c);
	}

	public void setVertexWithKey(final long key, final double x,
			final double y) {
		HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(x, y);
	}

	public void setVertexWithKey(final long key, final double x, final double y,
			final double z) {
		HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(x, y, z);
	}

	public void setVertexWithKey(final long key, final WB_Coord c) {
		HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(c);
	}

	/**
	 * Set vertex positions to values in a 2D array. If length of array is not
	 * the same as number of vertices, nothing happens.
	 *
	 * @param values
	 *            2D array of double. First index is number of vertices, second
	 *            index is 3 (x-,y- and z-coordinate)
	 */
	public void setVerticesFromDouble(final double[][] values) {
		if (values.length != getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i][0], values[i][1], values[i][2]);
			i++;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to values in a 1D array. If length of array is not
	 * 3* number of vertices, nothing happens.
	 *
	 * @param values
	 *            1D array of float. 3 values, x,y, and z, per point
	 */
	public void setVerticesFromFloat(final double[] values) {
		if (values.length != 3 * getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i], values[i + 1], values[i + 2]);
			i += 3;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to values in a 1D array. If length of array is not
	 * 3* number of vertices, nothing happens.
	 *
	 * @param values
	 *            1D array of float. 3 values, x,y, and z, per point
	 */
	public void setVerticesFromFloat(final float[] values) {
		if (values.length != 3 * getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i], values[i + 1], values[i + 2]);
			i += 3;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to values in a 2D array. If length of array is not
	 * the same as number of vertices, nothing happens.
	 *
	 * @param values
	 *            2D array of float. First index is number of vertices, second
	 *            index is 3 (x-,y- and z-coordinate)
	 */
	public void setVerticesFromFloat(final float[][] values) {
		if (values.length != getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i][0], values[i][1], values[i][2]);
			i++;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to values in a 1D array. If length of array is not
	 * 3* number of vertices, nothing happens.
	 *
	 * @param values
	 *            1D array of float. 3 values, x,y, and z, per point
	 */
	public void setVerticesFromFloat(final int[] values) {
		if (values.length != 3 * getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i], values[i + 1], values[i + 2]);
			i += 3;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to values in a 2D array. If length of array is not
	 * the same as number of vertices, nothing happens.
	 *
	 * @param values
	 *            2D array of int. First index is number of vertices, second
	 *            index is 3 (x-,y- and z-coordinate)
	 */
	public void setVerticesFromInt(final int[][] values) {
		if (values.length != getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i][0], values[i][1], values[i][2]);
			i++;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to List of WB_Coord. If the size of the List is not
	 * the same as number of vertices, nothing happens.
	 *
	 * @param values
	 *            array of WB_Coord.
	 */
	public void setVerticesFromPoint(final List<? extends WB_Coord> values) {
		if (values.size() != getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values.get(i));
			i++;
		}
		clearPrecomputed();
	}

	/**
	 * Set vertex positions to array of WB_Coord. If length of array is not the
	 * same as number of vertices, nothing happens.
	 *
	 * @param values
	 *            array of WB_Coord.
	 */
	public void setVerticesFromPoint(final WB_Coord[] values) {
		if (values.length != getNumberOfVertices()) {
			return;
		}
		int i = 0;
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(values[i]);
			i++;
		}
		clearPrecomputed();
	}

	/**
	 * Simplify.
	 *
	 * @param simplifier
	 *            the simplifier
	 * @return the h e_ mesh
	 */
	@Override
	public HE_Mesh simplify(final HES_Simplifier simplifier) {
		if (finished) {
			simplifier.apply(this);
			clearPrecomputed();
		} else {
			simplifyThreaded(simplifier);
		}
		return this;
	}

	public void simplifyThreaded(final HES_Simplifier simplifier) {
		tasks.add(new SimplifierThread(simplifier, this));
	}

	/**
	 * Smooth.
	 */
	public void smooth() {
		if (finished) {
			subdivide(new HES_CatmullClark());
		} else {
			subdivideThreaded(new HES_CatmullClark());
		}
	}

	/**
	 *
	 *
	 * @param rep
	 */
	public void smooth(final int rep) {
		if (finished) {
			subdivide(new HES_CatmullClark(), rep);
		} else {
			for (int i = 0; i < rep; i++) {
				subdivideThreaded(new HES_CatmullClark());
			}
		}
	}

	/**
	 * Sort all faces and vertices in lexographical order
	 */
	public void sort() {
		List<HE_Face> sortedFaces = new FastList<HE_Face>();
		sortedFaces.addAll(getFaces());
		Collections.sort(sortedFaces);
		clearFacesNoSelectionCheck();
		addFaces(sortedFaces);
		List<HE_Vertex> sortedVertices = new FastList<HE_Vertex>();
		sortedVertices.addAll(getVertices());
		Collections.sort(sortedVertices);
		clearVerticesNoSelectionCheck();
		addVertices(sortedVertices);
	}

	public void sort(final HE_FaceSort faceSort,
			final HE_VertexSort vertexSort) {
		List<HE_Face> sortedFaces = new FastList<HE_Face>();
		sortedFaces.addAll(getFaces());
		Collections.sort(sortedFaces);
		clearFacesNoSelectionCheck();
		addFaces(sortedFaces);
		List<HE_Vertex> sortedVertices = new FastList<HE_Vertex>();
		sortedVertices.addAll(getVertices());
		Collections.sort(sortedVertices);
		clearVerticesNoSelectionCheck();
		addVertices(sortedVertices);
	}

	/**
	 * Subdivide the mesh.
	 *
	 * @param subdividor
	 *            HE_Subdividor to apply
	 * @return self
	 */
	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor) {
		if (finished) {
			subdividor.apply(this);
			clearPrecomputed();
		} else {
			subdivideThreaded(subdividor);
		}
		return this;
	}

	/**
	 * Subdivide the mesh a number of times.
	 *
	 * @param subdividor
	 *            HE_Subdividor to apply
	 * @param rep
	 *            subdivision iterations. WARNING: higher values will lead to
	 *            unmanageable number of faces.
	 * @return self
	 */
	@Override
	public HE_Mesh subdivide(final HES_Subdividor subdividor, final int rep) {
		if (finished) {
			for (int i = 0; i < rep; i++) {
				subdividor.apply(this);
				clearPrecomputed();
			}
		} else {
			for (int i = 0; i < rep; i++) {
				subdivideThreaded(subdividor);
			}
		}
		return this;
	}

	public void subdivideThreaded(final HES_Subdividor subdividor) {
		tasks.add(new SubdividorThread(subdividor, this));
	}

	public void subdivideThreaded(final HES_Subdividor subdividor,
			final int rep) {
		for (int i = 0; i < rep; i++) {
			tasks.add(new SubdividorThread(subdividor, this));
		}
	}

	/**
	 *
	 *
	 * @return
	 */
	public WB_Mesh toFacelistMesh() {
		return gf.createMesh(getVerticesAsCoord(), getFacesAsInt());
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.geom.Point3D#toString()
	 */
	@Override
	public String toString() {
		String s = "HE_Mesh key: " + getKey() + ". (" + getNumberOfVertices()
				+ ", " + getNumberOfFaces() + ")";
		return s;
	}

	/**
	 * Create transformed copy of mesh.
	 *
	 * @param T
	 *            WB_Transform to apply
	 *
	 * @return copy
	 */
	public HE_Mesh transform(final WB_Transform3D T) {
		return copy().modify(new HEM_Transform(T));
	}

	/**
	 * Apply transform to entire mesh.
	 *
	 * @param T
	 *            WB_Transform to apply
	 *
	 * @return self
	 */
	public HE_Mesh transformSelf(final WB_Transform3D T) {
		return modify(new HEM_Transform(T));
	}

	/**
	 * Uncap halfedges.
	 */
	public void uncapBoundaryHalfedges() {
		tracker.setStartStatus(this, "Uncapping boundary halfedges.");
		WB_ProgressCounter counter = new WB_ProgressCounter(
				getNumberOfHalfedges(), 10);
		List<HE_Halfedge> halfedges = getHalfedges();
		final HE_RAS<HE_Halfedge> remove = new HE_RAS<HE_Halfedge>();
		for (HE_Halfedge he : halfedges) {
			if (he.getFace() == null) {
				setHalfedge(he.getVertex(), he.getNextInVertex());
				clearPair(he);
				remove.add(he);
			} else {
			}
			counter.increment();
		}
		removeHalfedges(remove);
		tracker.setStopStatus(this, "Removing outer boundary halfedges.");
	}

	public void update() {
		if (future == null) {
			if (tasks.size() > 0) {
				if (executor == null) {
					executor = Executors.newFixedThreadPool(1);
				}
				future = executor.submit(tasks.removeFirst());
				finished = false;
			} else {
				if (executor != null) {
					executor.shutdown();
				}
				executor = null;
			}
		} else if (future.isDone()) {
			try {
				HE_Mesh result = future.get();
				if (result != this) {// HEM_Modify returns this if modification
										// of copy failed.
					setNoCopy(result);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			future = null;
			finished = true;
		} else if (future.isCancelled()) {
			future = null;
			finished = true;
		}
	}

	/**
	 * Check consistency of datastructure.
	 *
	 * @return true or false
	 */
	public boolean validate() {
		return HET_Diagnosis.validate(this);
	}

	/**
	 * Vertex iterator.
	 *
	 * @return vertex iterator
	 */
	@Override
	public HE_VertexIterator vItr() {
		return new HE_VertexIterator(vertices);
	}

	@Override
	public void stats() {
		System.out.println("HE_Mesh: " + getKey());
		System.out.println("Number of vertices: " + this.getNumberOfVertices());
		System.out.println("Number of faces: " + this.getNumberOfFaces());
		System.out.println("Number of halfedges: " + this.edges.size() + "/"
				+ this.halfedges.size() + "/" + this.unpairedHalfedges.size());
		System.out.println("Number of boundary halfedges: "
				+ this.getAllBoundaryHalfedges().size());
	}
	
	public WB_AABB getAABB() {
		return HE_MeshOp.getAABB(this);
		
	}
	
	
	public void triangulate() {
	HE_MeshOp.triangulate(this);	

	}
}