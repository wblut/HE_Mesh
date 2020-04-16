package wblut.hemesh;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.core.WB_ProgressReporter.WB_ProgressTracker;
import wblut.geom.WB_AABB;
import wblut.geom.WB_Classification;
import wblut.geom.WB_Coord;
import wblut.geom.WB_CoordCollection;
import wblut.geom.WB_CoordList;
import wblut.geom.WB_GeometryFactory3D;
import wblut.geom.WB_GeometryOp3D;
import wblut.geom.WB_KDTree3D;
import wblut.geom.WB_Network;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Polygon;
import wblut.geom.WB_Segment;
import wblut.geom.WB_SimpleMesh;
import wblut.geom.WB_SimpleMeshCreator;
import wblut.geom.WB_Transform2D;
import wblut.geom.WB_Transform3D;
import wblut.geom.WB_Transformable3D;
import wblut.geom.WB_TriangleFactory;
import wblut.geom.WB_Vector;
import wblut.math.WB_Epsilon;
import wblut.math.WB_MTRandom;

public class HE_Mesh extends HE_MeshElement implements WB_TriangleFactory, HE_HalfedgeStructure, WB_Transformable3D {
	protected final static WB_ProgressTracker tracker = WB_ProgressTracker.instance();
	protected WB_GeometryFactory3D gf = new WB_GeometryFactory3D();

	class CreatorThread implements Callable<HE_Mesh> {
		HEC_Creator creator;

		CreatorThread(final HEC_Creator creator) {
			this.creator = creator;
		}

		@Override
		public HE_Mesh call() {
			final HE_Mesh result = creator.create();
			return result;
		}
	}

	class ModifierThread implements Callable<HE_Mesh> {
		HEM_Modifier machine;
		HE_Mesh mesh;

		ModifierThread(final HEM_Modifier machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (final Exception e) {
				return mesh;
			}
		}
	}

	class SimplifierThread implements Callable<HE_Mesh> {
		HES_Simplifier machine;
		HE_Mesh mesh;

		SimplifierThread(final HES_Simplifier machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (final Exception e) {
				return mesh;
			}
		}
	}

	class SubdividorThread implements Callable<HE_Mesh> {
		HES_Subdividor machine;
		HE_Mesh mesh;

		SubdividorThread(final HES_Subdividor machine, final HE_Mesh mesh) {
			this.machine = machine;
			this.mesh = mesh;
		}

		@Override
		public HE_Mesh call() {
			try {
				return machine.applySelf(mesh.get());
			} catch (final Exception e) {
				return mesh;
			}
		}
	}

	String name;
	private HE_RAS<HE_Vertex> vertices;
	private HE_RAS<HE_Face> faces;
	private HE_RAS<HE_Halfedge> edges;
	private HE_RAS<HE_Halfedge> halfedges;
	private HE_RAS<HE_Halfedge> unpairedHalfedges;
	ExecutorService executor;
	LinkedList<Callable<HE_Mesh>> tasks;
	boolean finished;
	Future<HE_Mesh> future;
	Map<String, HE_Selection> selections;
	@SuppressWarnings("rawtypes")
	Map<String, HE_Attribute> attributes;
	Map<String, HE_IntegerAttribute> integerAttributes;
	Map<String, HE_FloatAttribute> floatAttributes;
	Map<String, HE_DoubleAttribute> doubleAttributes;
	Map<String, HE_BooleanAttribute> booleanAttributes;
	Map<String, HE_StringAttribute> stringAttributes;
	int[] triangles;
	double uRepeat;
	double vRepeat;

	public HE_Mesh() {
		super();
		vertices = new HE_RAS<>();
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
		unpairedHalfedges = new HE_RAS<>();
		faces = new HE_RAS<>();
		selections = new HashMap<>();
		integerAttributes = new HashMap<>();
		floatAttributes = new HashMap<>();
		doubleAttributes = new HashMap<>();
		booleanAttributes = new HashMap<>();
		stringAttributes = new HashMap<>();
		attributes = new HashMap<>();
		tasks = new LinkedList<>();
		future = null;
		executor = null;
		finished = true;
		triangles = null;
		uRepeat = 1.0;
		vRepeat = 1.0;
	}

	public HE_Mesh(final HE_Mesh mesh) {
		this();
		set(mesh);
		triangles = null;
	}

	public HE_Mesh(final HEC_Creator creator) {
		this();
		setNoCopy(creator.create());
		triangles = null;
	}

	public HE_Mesh(final WB_SimpleMesh mesh) {
		this(new HEC_FromSimpleMesh(mesh));
		triangles = null;
	}

	public HE_Mesh(final WB_SimpleMeshCreator mesh) {
		this(new HEC_FromSimpleMesh(mesh.create()));
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

	@Override
	public void add(final HE_Face f) {
		faces.add(f);
	}

	public void addDerivedElement(final HE_Face f, final HE_Element... el) {
		add(f);
		for (final HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (final HE_Element element : el) {
				contains |= sel.contains(element);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(f);
			}
		}
		addDerivedElementToAttributes(f, el);
	}

	@SuppressWarnings("unchecked")
	private void addDerivedElementToAttributes(final HE_Element derived, final HE_Element... el) {
		for (@SuppressWarnings("rawtypes")
		final HE_Attribute attribute : attributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				Object o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
		for (final HE_IntegerAttribute attribute : integerAttributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				int o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
		for (final HE_FloatAttribute attribute : floatAttributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				float o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
		for (final HE_DoubleAttribute attribute : doubleAttributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				double o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
		for (final HE_BooleanAttribute attribute : booleanAttributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				boolean o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
		for (final HE_StringAttribute attribute : stringAttributes.values()) {
			if (attribute.isPersistent()) {
				boolean contains = false;
				String o = attribute.defaultValue;
				for (final HE_Element element : el) {
					contains |= attribute.attributeList.containsKey(element.getKey());
					if (contains) {
						o = attribute.get(element);
						break;
					}
				}
				if (contains) {
					attribute.set(derived, o);
				}
			}
		}
	}

	@Override
	public void addFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			add(f);
		}
	}

	@Override
	public void addFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			add(face);
		}
	}

	@Override
	public void addFaces(final HE_HalfedgeStructure source) {
		faces.addAll(source.getFaces());
	}

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

	public void addDerivedElement(final HE_Halfedge he, final HE_Element... el) {
		add(he);
		for (final HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (final HE_Element element : el) {
				contains |= sel.contains(element);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(he);
			}
		}
		addDerivedElementToAttributes(he, el);
	}

	@Override
	public void addHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			add(he);
		}
	}

	@Override
	public void addHalfedges(final HE_Halfedge[] halfedges) {
		for (final HE_Halfedge halfedge : halfedges) {
			add(halfedge);
		}
	}

	@Override
	public void addHalfedges(final HE_HalfedgeStructure source) {
		for (final HE_Halfedge he : source.getHalfedges()) {
			add(he);
		}
	}

	@Override
	public void add(final HE_Vertex v) {
		vertices.add(v);
	}

	public void addDerivedElement(final HE_Vertex v, final HE_Element... el) {
		add(v);
		for (final HE_Selection sel : selections.values()) {
			boolean contains = false;
			for (final HE_Element element : el) {
				contains |= sel.contains(element);
				if (contains) {
					break;
				}
			}
			if (contains) {
				sel.add(v);
			}
		}
		addDerivedElementToAttributes(v, el);
	}

	@Override
	public void addVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			add(v);
		}
	}

	@Override
	public void addVertices(final HE_HalfedgeStructure source) {
		vertices.addAll(source.getVertices());
	}

	@Override
	public void addVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertex : vertices) {
			add(vertex);
		}
	}

	@Override
	public void add(final HE_Mesh mesh) {
		addVertices(mesh.vertices);
		addFaces(mesh.faces);
		addHalfedges(mesh.edges);
		addHalfedges(mesh.halfedges);
		addHalfedges(mesh.unpairedHalfedges);
		final String[] selections = mesh.getSelectionNames();
		for (final String name : selections) {
			final HE_Selection sourceSel = mesh.getSelection(name);
			final HE_Selection currentSel = getSelection(name);
			final HE_Selection sel = sourceSel.get();
			currentSel.add(sel);
		}
	}

	void addSelection(final String name, final HE_Machine machine, final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			sel.createdBy = machine.getName();
			final HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding to selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				prevsel.add(sel);
			}
		} else {
			tracker.setDuringStatus(this,
					"Selection " + name + " not added: selection is null or parent mesh is not the same.");
		}
	}

	public void addSelection(final String name, final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			final HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding to selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				prevsel.add(sel);
			}
		} else {
			tracker.setDuringStatus(this,
					"Selection " + name + " not added: selection is null or parent mesh is not the same.");
		}
	}

	@Override
	public HE_Mesh apply(final WB_Transform3D T) {
		return new HEC_Transform3D(this, T).create();
	}

	@Override
	public HE_Mesh applySelf(final WB_Transform3D T) {
		return modify(new HEM_Transform3D(T));
	}

	@Override
	public HE_Mesh apply2D(final WB_Transform2D T) {
		return new HEC_Transform2D(this, T).create();
	}

	@Override
	public HE_Mesh apply2DSelf(final WB_Transform2D T) {
		return modify(new HEM_Transform2D(T));
	}

	public void clean() {
		modify(new HEM_Clean());
	}

	public void cleanSelections() {
		final List<String> selToDel = new FastList<>();
		for (final Entry<String, HE_Selection> entry : selections.entrySet()) {
			final HE_Selection sel = entry.getValue();
			sel.cleanSelection();
			if (sel.getNumberOfFaces() == 0 && sel.getNumberOfVertices() == 0 && sel.getNumberOfHalfedges() == 0) {
				selToDel.add(entry.getKey());
			}
		}
		for (final String name : selToDel) {
			selections.remove(name);
		}
	}

	@Deprecated
	public HE_Mesh cleanUnusedElementsByFace() {
		return removeUnconnectedElements();
	}

	public HE_Mesh removeUnconnectedElements() {
		return HE_MeshOp.removeUnconnectedElements(this);
	}

	@Override
	public void clear() {
		selections = new HashMap<>();
		clearVertices();
		clearHalfedges();
		clearFaces();
	}

	@Override
	public void clearEdges() {
		edges = new HE_RAS<>();
		for (final HE_Selection sel : selections.values()) {
			sel.clearEdges();
		}
	}

	public void clearFace(final HE_Halfedge he) {
		he.clearFace();
		if (he.getPair() != null) {
			setPair(he, he.getPair());
		}
	}

	@Override
	public void clearFaces() {
		faces = new HE_RAS<>();
		for (final HE_Selection sel : selections.values()) {
			sel.clearFaces();
		}
	}

	void clearFacesNoSelectionCheck() {
		faces = new HE_RAS<>();
	}

	public void clearHalfedge(final HE_Face f) {
		f.clearHalfedge();
	}

	public void clearHalfedge(final HE_Vertex v) {
		v.clearHalfedge();
	}

	@Override
	public void clearHalfedges() {
		halfedges = new HE_RAS<>();
		edges = new HE_RAS<>();
		unpairedHalfedges = new HE_RAS<>();
		for (final HE_Selection sel : selections.values()) {
			sel.clearHalfedges();
		}
	}

	public void clearNext(final HE_Halfedge he) {
		if (he.getNextInFace() != null) {
			he.getNextInFace().clearPrev();
		}
		he.clearNext();
	}

	public void clearPair(final HE_Halfedge he) {
		if (he.getPair() == null) {
			return;
		}
		final HE_Halfedge hep = he.getPair();
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
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().clearPrecomputed();
		}
	}

	public void clearPrecomputedHalfedges() {
		final HE_HalfedgeIterator heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().clearPrecomputed();
		}
	}

	public void clearPrecomputedVertices() {
		final HE_VertexIterator vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().clearPrecomputed();
		}
	}

	public void clearPrev(final HE_Halfedge he) {
		if (he.getPrevInFace() != null) {
			he.getPrevInFace().clearNext();
		}
		he.clearPrev();
	}

	public void clearSelections() {
		selections = new HashMap<>();
	}

	public void clearVertex(final HE_Halfedge he) {
		he.clearVertex();
	}

	@Override
	public void clearVertices() {
		vertices = new HE_RAS<>();
		for (final HE_Selection sel : selections.values()) {
			sel.clearVertices();
		}
	}

	void clearVerticesNoSelectionCheck() {
		vertices = new HE_RAS<>();
	}

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

	@Override
	public boolean contains(final HE_Face f) {
		return faces.contains(f);
	}

	@Override
	public boolean contains(final HE_Halfedge he) {
		return edges.contains(he) || halfedges.contains(he) || unpairedHalfedges.contains(he);
	}

	@Override
	public boolean contains(final HE_Vertex v) {
		return vertices.contains(v);
	}

	@Override
	public boolean containsEdge(final long key) {
		return edges.containsKey(key);
	}

	@Override
	public boolean containsFace(final long key) {
		return faces.containsKey(key);
	}

	@Override
	public boolean containsHalfedge(final long key) {
		return halfedges.containsKey(key) || edges.containsKey(key) || unpairedHalfedges.containsKey(key);
	}

	@Override
	public boolean containsVertex(final long key) {
		return vertices.containsKey(key);
	}

	public HE_Mesh copy() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	public HE_Selection copySelection(final String from, final String to) {
		final HE_Selection sel = selections.get(from);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + from + " not found.");
		}
		final HE_Selection copy = sel.get();
		addSelection(to, copy);
		return copy;
	}

	public void createThreaded(final HEC_Creator creator) {
		tasks.add(new CreatorThread(creator));
	}

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
			remove(he2.getFace());
			remove(he1.getFace());
			addDerivedElement(f, e.getPair().getFace());
			setHalfedge(f, he1p);
			HE_Halfedge he = he1p;
			do {
				setFace(he, f);
				he = he.getNextInFace();
			} while (he != he1p);
		}
		remove(he1);
		remove(he2);
		return f;
	}

	public void deleteFace(final HE_Face f) {
		HE_Halfedge he = f.getHalfedge();
		do {
			clearFace(he);
			he = he.getNextInFace();
		} while (he != f.getHalfedge());
		remove(f);
	}

	public void deleteFaces(final HE_Selection faces) {
		HE_Face f;
		final HE_FaceIterator fItr = faces.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			remove(f);
		}
		removeUnconnectedElements();
		HE_MeshOp.capHalfedges(this);
	}

	@Override
	public HE_EdgeIterator eItr() {
		return new HE_EdgeIterator(edges);
	}

	public void fitInAABB(final WB_AABB AABB) {
		final WB_AABB self = HE_MeshOp.getAABB(this);
		moveSelf(new WB_Vector(self.getMin(), AABB.getMin()));
		scaleSelf(AABB.getWidth() / self.getWidth(), AABB.getHeight() / self.getHeight(),
				AABB.getDepth() / self.getDepth(), new WB_Point(AABB.getMin()));
	}

	public void fitInAABB(final WB_AABB from, final WB_AABB to) {
		moveSelf(new WB_Vector(from.getMin(), to.getMin()));
		scaleSelf(to.getWidth() / from.getWidth(), to.getHeight() / from.getHeight(), to.getDepth() / from.getDepth(),
				new WB_Point(to.getMin()));
	}

	public double fitInAABBConstrained(final WB_AABB AABB) {
		final WB_AABB self = HE_MeshOp.getAABB(this);
		moveSelf(new WB_Vector(self.getCenter(), AABB.getCenter()));
		double f = Math.min(AABB.getWidth() / self.getWidth(), AABB.getHeight() / self.getHeight());
		f = Math.min(f, AABB.getDepth() / self.getDepth());
		scaleSelf(f, new WB_Point(AABB.getCenter()));
		return f;
	}

	public double fitInAABBConstrained(final WB_AABB from, final WB_AABB to) {
		moveSelf(new WB_Vector(from.getCenter(), to.getCenter()));
		double f = Math.min(to.getWidth() / from.getWidth(), to.getHeight() / from.getHeight());
		f = Math.min(f, to.getDepth() / from.getDepth());
		scaleSelf(f, new WB_Point(to.getCenter()));
		return f;
	}

	@Override
	public HE_FaceIterator fItr() {
		final List<HE_Face> fs = new HE_FaceList(getFaces());
		return new HE_FaceIterator(fs);
	}

	public void fuse(final HE_Mesh mesh) {
		addVertices(mesh.getVerticesAsArray());
		addFaces(mesh.getFacesAsArray());
		addHalfedges(mesh.getHalfedgesAsArray());
		setNoCopy(new HE_Mesh(new HEC_FromPolygons().setPolygons(this.getPolygonList())));
	}

	public HE_Mesh get() {
		return new HE_Mesh(new HEC_Copy(this));
	}

	public WB_Point getCenter() {
		return HE_MeshOp.getCenter(this);
	}

	public List<HE_Halfedge> getAllBoundaryHalfedges() {
		final List<HE_Halfedge> boundaryHalfedges = new HE_HalfedgeList();
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

	public List<HE_Vertex> getAllBoundaryVertices() {
		final List<HE_Vertex> boundaryVertices = new HE_VertexList();
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

	public WB_Point[] getEdgeCenters() {
		final WB_Point[] result = new WB_Point[getNumberOfEdges()];
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

	public WB_Vector[] getEdgeNormals() {
		final WB_Vector[] result = new WB_Vector[getNumberOfEdges()];
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

	@Override
	public List<HE_Halfedge> getEdges() {
		return new HE_HalfedgeList(edges.getObjects());
	}

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
		final HE_IntMap vertexKeys = new HE_IntMap();
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

	@Override
	public HE_Halfedge getEdgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size()) {
			throw new IndexOutOfBoundsException("Requested edge index " + i + "not in range.");
		}
		return edges.getWithIndex(i);
	}

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

	public int getEulerCharacteristic() {
		return getNumberOfVertices() - getNumberOfEdges() + getNumberOfFaces();
	}

	public WB_Point getFaceCenter(final int id) {
		return HE_MeshOp.getFaceCenter(getFaceWithIndex(id));
	}

	public WB_Point[] getFaceCenters() {
		final WB_Point[] result = new WB_Point[getNumberOfFaces()];
		int i = 0;
		HE_Face f;
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getFaceCenter(f);
			i++;
		}
		return result;
	}

	public int[] getFaceColors() {
		final int[] result = new int[getNumberOfFaces()];
		final HE_FaceIterator fItr = fItr();
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
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getInternalLabel();
			i++;
		}
		return result;
	}

	public WB_Vector getFaceNormal(final int id) {
		return HE_MeshOp.getFaceNormal(getFaceWithIndex(id));
	}

	public WB_Vector[] getFaceNormals() {
		final WB_Vector[] result = new WB_Vector[getNumberOfFaces()];
		int i = 0;
		HE_Face f;
		final HE_FaceIterator fItr = fItr();
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
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getPlane(f);
			i++;
		}
		return result;
	}

	@Override
	public List<HE_Face> getFaces() {
		return new HE_FaceList(faces.getObjects());
	}

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

	public int[][] getFacesAsInt() {
		final int[][] result = new int[getNumberOfFaces()][];
		final HE_IntMap vertexKeys = getVertexKeyToIndexMap();
		final HE_FaceIterator fItr = fItr();
		HE_Halfedge he;
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = new int[f.getFaceDegree()];
			he = f.getHalfedge();
			int j = 0;
			do {
				result[i][j] = vertexKeys.get(he.getVertex());
				he = he.getNextInFace();
				j++;
			} while (he != f.getHalfedge());
			i++;
		}
		return result;
	}

	public int[] getFaceTextureIds() {
		final int[] result = new int[getNumberOfFaces()];
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getTextureId();
			i++;
		}
		return result;
	}

	public WB_KDTree3D<WB_Point, Long> getFaceTree() {
		final WB_KDTree3D<WB_Point, Long> tree = new WB_KDTree3D<>();
		HE_Face f;
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			tree.add(HE_MeshOp.getFaceCenter(f), f.getKey());
		}
		return tree;
	}

	public int[] getFaceLabels() {
		final int[] result = new int[getNumberOfFaces()];
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.getLabel();
			i++;
		}
		return result;
	}

	public boolean[] getFaceVisibility() {
		final boolean[] result = new boolean[getNumberOfFaces()];
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = f.isVisible();
			i++;
		}
		return result;
	}

	@Override
	public HE_Face getFaceWithIndex(final int i) {
		if (i < 0 || i >= faces.size()) {
			throw new IndexOutOfBoundsException("Requested face index " + i + "not in range.");
		}
		return faces.getWithIndex(i);
	}

	@Override
	public HE_Face getFaceWithKey(final long key) {
		return faces.getWithKey(key);
	}

	public HE_Halfedge getHalfedgeFromTo(final HE_Vertex v0, final HE_Vertex v1) {
		final List<HE_Halfedge> hes = v0.getHalfedgeStar();
		for (final HE_Halfedge he : hes) {
			if (he.getEndVertex() == v1) {
				return he;
			}
		}
		return null;
	}

	@Override
	public List<HE_Halfedge> getHalfedges() {
		final List<HE_Halfedge> halfedges = new HE_HalfedgeList();
		halfedges.addAll(this.halfedges);
		halfedges.addAll(this.edges);
		halfedges.addAll(this.unpairedHalfedges);
		return halfedges;
	}

	@Override
	public HE_Halfedge[] getHalfedgesAsArray() {
		final List<HE_Halfedge> hes = getHalfedges();
		final HE_Halfedge[] halfedges = new HE_Halfedge[hes.size()];
		int i = 0;
		for (final HE_Halfedge he : hes) {
			halfedges[i] = he;
			i++;
		}
		return halfedges;
	}

	@Override
	public HE_Halfedge getHalfedgeWithIndex(final int i) {
		if (i < 0 || i >= edges.size() + halfedges.size() + unpairedHalfedges.size()) {
			throw new IndexOutOfBoundsException("Requested halfedge index " + i + "not in range.");
		}
		if (i >= edges.size() + halfedges.size()) {
			return unpairedHalfedges.getWithIndex(i - edges.size() - halfedges.size());
		} else if (i >= edges.size()) {
			return halfedges.getWithIndex(i - edges.size());
		}
		return edges.getWithIndex(i);
	}

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

	@Override
	public int getIndex(final HE_Face f) {
		return faces.indexOf(f);
	}

	@Override
	public int getIndex(final HE_Halfedge edge) {
		return edges.indexOf(edge);
	}

	@Override
	public int getIndex(final HE_Vertex v) {
		return vertices.indexOf(v);
	}

	public HE_ObjectMap<WB_Point> getKeyedEdgeCenters() {
		final HE_ObjectMap<WB_Point> result = new HE_ObjectMap<>();
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result.put(e.getKey(), HE_MeshOp.getHalfedgeCenter(e));
		}
		return result;
	}

	public HE_ObjectMap<WB_Vector> getKeyedEdgeNormals() {
		final HE_ObjectMap<WB_Vector> result = new HE_ObjectMap<>();
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			result.put(e.getKey(), HE_MeshOp.getEdgeNormal(e));
		}
		return result;
	}

	public HE_ObjectMap<WB_Point> getKeyedFaceCenters() {
		final HE_ObjectMap<WB_Point> result = new HE_ObjectMap<>();
		HE_Face f;
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result.put(f.getKey(), HE_MeshOp.getFaceCenter(f));
		}
		return result;
	}

	public HE_ObjectMap<WB_Vector> getKeyedFaceNormals() {
		final HE_ObjectMap<WB_Vector> result = new HE_ObjectMap<>();
		HE_Face f;
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			result.put(f.getKey(), HE_MeshOp.getFaceNormal(f));
		}
		return result;
	}

	public HE_ObjectMap<WB_Vector> getKeyedVertexNormals() {
		final HE_ObjectMap<WB_Vector> result = new HE_ObjectMap<>();
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
		final HE_EdgeIterator eItr = this.eItr();
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
		final HE_Selection sel = HE_Selection.getSelection(this);
		replaceSelection(name, sel);
		return sel;
	}

	public WB_Network getNetwork() {
		final WB_Network frame = new WB_Network(getVerticesAsCoord());
		final HE_IntMap map = getVertexKeyToIndexMap();
		final Iterator<HE_Halfedge> eItr = eItr();
		HE_Halfedge e;
		while (eItr.hasNext()) {
			e = eItr.next();
			frame.addConnection(map.get(e.getVertex().getKey()), map.get(e.getEndVertex().getKey()));
		}
		return frame;
	}

	@Override
	public int getNumberOfEdges() {
		return edges.size();
	}

	@Override
	public int getNumberOfFaces() {
		return faces.size();
	}

	@Override
	public int getNumberOfHalfedges() {
		return halfedges.size() + edges.size() + unpairedHalfedges.size();
	}

	@Override
	public int getNumberOfVertices() {
		return vertices.size();
	}

	@Override
	public WB_CoordCollection getPoints() {
		final List<WB_Coord> result = new WB_CoordList();
		result.addAll(vertices.getObjects());
		return WB_CoordCollection.getCollection(result);
	}

	public List<WB_Polygon> getPolygonList() {
		final List<WB_Polygon> result = new FastList<>();
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			result.add(HE_MeshOp.getPolygon(f));
		}
		return result;
	}

	public WB_Polygon[] getPolygons() {
		final WB_Polygon[] result = new WB_Polygon[getNumberOfFaces()];
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		int i = 0;
		while (fItr.hasNext()) {
			f = fItr.next();
			result[i] = HE_MeshOp.getPolygon(f);
			i++;
		}
		return result;
	}

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

	public HE_Selection getSelection(final String name) {
		final HE_Selection sel = selections.get(name);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + name + " not found.");
			return getNewSelection(name);
		}
		return sel;
	}

	public String[] getSelectionNames() {
		final String[] names = new String[selections.size()];
		selections.keySet().toArray(names);
		Arrays.sort(names);
		return names;
	}

	public void selectionCheck() {
		for (final Map.Entry<String, HE_Selection> entry : selections.entrySet()) {
			final HE_Selection sel = entry.getValue();
			final String name = entry.getKey();
			final HE_VertexIterator vItr = sel.vItr();
			while (vItr.hasNext()) {
				if (!contains(vItr.next())) {
					System.out.println("External vertex in selection " + name + ".");
				}
			}
			final HE_HalfedgeIterator heItr = sel.heItr();
			while (heItr.hasNext()) {
				if (!contains(heItr.next())) {
					System.out.println("External halfedge in selection " + name + ".");
				}
			}
			final HE_FaceIterator fItr = sel.fItr();
			while (fItr.hasNext()) {
				if (!contains(fItr.next())) {
					System.out.println("External face in selection " + name + ".");
				}
			}
		}
	}

	public List<HE_Face> getSharedFaces(final HE_Vertex v1, final HE_Vertex v2) {
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

	@Override
	public int[] getTriangles() {
		if (triangles == null) {
			final HE_Mesh trimesh = this.copy();
			HE_MeshOp.triangulate(trimesh);
			triangles = new int[3 * trimesh.getNumberOfFaces()];
			final HE_FaceIterator fItr = trimesh.fItr();
			HE_Face f;
			int id = 0;
			while (fItr.hasNext()) {
				f = fItr.next();
				triangles[id++] = trimesh.getIndex(f.getHalfedge().getVertex());
				triangles[id++] = trimesh.getIndex(f.getHalfedge().getNextInFace().getVertex());
				triangles[id++] = trimesh.getIndex(f.getHalfedge().getNextInFace().getNextInFace().getVertex());
			}
		}
		return triangles;
	}

	public List<HE_Halfedge> getUnpairedHalfedges() {
		final List<HE_Halfedge> halfedges = new HE_HalfedgeList();
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

	public HE_IntMap getVertexKeyToIndexMap() {
		final HE_IntMap map = new HE_IntMap();
		int i = 0;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			map.put(vItr.next().getKey(), i);
			i++;
		}
		return map;
	}

	public WB_Vector getVertexNormal(final int i) {
		return HE_MeshOp.getVertexNormal(getVertexWithIndex(i));
	}

	public WB_Vector[] getVertexNormals() {
		final WB_Vector[] result = new WB_Vector[getNumberOfVertices()];
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

	public WB_KDTree3D<WB_Coord, Long> getVertexTree() {
		final WB_KDTree3D<WB_Coord, Long> tree = new WB_KDTree3D<>();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			tree.add(v, v.getKey());
		}
		return tree;
	}

	public int[] getVertexLabels() {
		final int[] result = new int[getNumberOfVertices()];
		final Iterator<HE_Vertex> vItr = vItr();
		HE_Vertex v;
		int i = 0;
		while (vItr.hasNext()) {
			v = vItr.next();
			result[i] = v.getLabel();
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

	@Override
	public HE_Vertex getVertexWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException("Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i);
	}

	public WB_Point getPositionWithIndex(final int i) {
		if (i < 0 || i >= vertices.size()) {
			throw new IndexOutOfBoundsException("Requested vertex index " + i + "not in range.");
		}
		return vertices.getWithIndex(i).getPosition();
	}

	@Override
	public HE_Vertex getVertexWithKey(final long key) {
		return vertices.getWithKey(key);
	}

	@Override
	public List<HE_Vertex> getVertices() {
		return new HE_VertexList(vertices.getObjects());
	}

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

	public List<WB_Coord> getVerticesAsCoord() {
		final WB_CoordList result = new WB_CoordList();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			result.add(v);
		}
		return result.asUnmodifiable();
	}

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

	@Override
	public HE_HalfedgeIterator heItr() {
		return HE_HalfedgeIterator.getIterator(edges, halfedges, unpairedHalfedges);
	}

	public boolean isFinished() {
		return finished;
	}

	boolean isNeighbor(final HE_RAS<HE_Face> partition1, final HE_RAS<HE_Face> partition2) {
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
							if (he2.getPair() == null && he2.getNextInFace().getVertex() == v1) {
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

	public HE_Mesh move(final double x, final double y, final double z) {
		final HE_Mesh result = copy();
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x, y, z);
		}
		result.clearPrecomputed();
		return result;
	}

	public HE_Mesh move(final WB_Coord v) {
		return move(v.xd(), v.yd(), v.zd());
	}

	public HE_Mesh moveSelf(final double x, final double y, final double z) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x, y, z);
		}
		clearPrecomputed();
		return this;
	}

	public HE_Mesh moveSelf(final WB_Coord v) {
		return moveSelf(v.xd(), v.yd(), v.zd());
	}

	public HE_Mesh moveTo(final double x, final double y, final double z) {
		final HE_Mesh result = copy();
		final WB_Point center = HE_MeshOp.getCenter(result);
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x - center.xd(), y - center.yd(), z - center.zd());
		}
		result.clearPrecomputed();
		return result;
	}

	public HE_Mesh moveTo(final WB_Coord v) {
		return moveTo(v.xd(), v.yd(), v.zd());
	}

	public HE_Mesh moveToSelf(final double x, final double y, final double z) {
		final WB_Point center = HE_MeshOp.getCenter(this);
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().getPosition().addSelf(x - center.xd(), y - center.yd(), z - center.zd());
		}
		clearPrecomputed();
		return this;
	}

	public HE_Mesh moveToSelf(final WB_Coord v) {
		return moveToSelf(v.xd(), v.yd(), v.zd());
	}

	@Override
	public void remove(final HE_Face f) {
		faces.remove(f);
		for (final HE_Selection sel : selections.values()) {
			sel.remove(f);
		}
		removeElementFromAttributes(f);
	}

	@Override
	public void remove(final HE_Halfedge he) {
		edges.remove(he);
		halfedges.remove(he);
		unpairedHalfedges.remove(he);
		for (final HE_Selection sel : selections.values()) {
			sel.remove(he);
		}
		removeElementFromAttributes(he);
	}

	@Override
	public void remove(final HE_Vertex v) {
		vertices.remove(v);
		for (final HE_Selection sel : selections.values()) {
			sel.remove(v);
		}
		removeElementFromAttributes(v);
	}

	@SuppressWarnings("rawtypes")
	void removeElementFromAttributes(final HE_Element el) {
		for (final HE_Attribute attribute : attributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
		for (final HE_IntegerAttribute attribute : integerAttributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
		for (final HE_FloatAttribute attribute : floatAttributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
		for (final HE_DoubleAttribute attribute : doubleAttributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
		for (final HE_BooleanAttribute attribute : booleanAttributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
		for (final HE_StringAttribute attribute : stringAttributes.values()) {
			attribute.attributeList.remove(el.getKey());
		}
	}

	@Override
	public void removeEdges(final Collection<? extends HE_Halfedge> edges) {
		for (final HE_Halfedge e : edges) {
			remove(e);
		}
	}

	@Override
	public void removeEdges(final HE_Halfedge[] edges) {
		for (final HE_Halfedge edge : edges) {
			remove(edge);
		}
	}

	@Override
	public void removeFaces(final Collection<? extends HE_Face> faces) {
		for (final HE_Face f : faces) {
			remove(f);
		}
	}

	@Override
	public void removeFaces(final HE_Face[] faces) {
		for (final HE_Face face : faces) {
			remove(face);
		}
	}

	@Override
	public void removeHalfedges(final Collection<? extends HE_Halfedge> halfedges) {
		for (final HE_Halfedge he : halfedges) {
			remove(he);
		}
	}

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

	public HE_Selection removeSelection(final String name) {
		final HE_Selection prevsel = selections.remove(name);
		if (prevsel == null) {
			tracker.setDuringStatus(this, "Selection " + name + " not found.");
		} else {
			tracker.setDuringStatus(this, "Removed selection " + name + ".");
		}
		return prevsel;
	}

	@Override
	public void removeVertices(final Collection<? extends HE_Vertex> vertices) {
		for (final HE_Vertex v : vertices) {
			remove(v);
		}
	}

	@Override
	public void removeVertices(final HE_Vertex[] vertices) {
		for (final HE_Vertex vertice : vertices) {
			remove(vertice);
		}
	}

	public boolean renameSelection(final String from, final String to) {
		final HE_Selection sel = removeSelection(from);
		if (sel == null) {
			tracker.setDuringStatus(this, "Selection " + from + " not found.");
			return false;
		}
		replaceSelection(to, sel);
		tracker.setDuringStatus(this, "Renamed selection " + from + " to " + to + ".");
		return true;
	}

	private void replaceFaces(final HE_Mesh mesh) {
		clearFaces();
		addFaces(mesh.faces);
	}

	private void replaceHalfedges(final HE_Mesh mesh) {
		clearHalfedges();
		final HE_HalfedgeIterator heItr = mesh.heItr();
		while (heItr.hasNext()) {
			add(heItr.next());
		}
	}

	HE_Selection replaceSelection(final String name, final HE_Machine machine, final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			sel.createdBy = machine.getName();
			final HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Replacing selection " + name + ".");
				removeSelection(name);
				selections.put(name, sel);
			}
			return prevsel;
		} else {
			tracker.setDuringStatus(this,
					"Selection " + name + " not added: selection is null or parent mesh is not the same.");
		}
		return null;
	}

	public HE_Selection replaceSelection(final String name, final HE_Selection sel) {
		if (sel.getParent() == this && sel != null) {
			final HE_Selection prevsel = selections.get(name);
			if (prevsel == null) {
				tracker.setDuringStatus(this, "Adding selection " + name + ".");
				selections.put(name, sel);
			} else {
				tracker.setDuringStatus(this, "Replacing selection " + name + ".");
				removeSelection(name);
				selections.put(name, sel);
			}
			return prevsel;
		} else {
			tracker.setDuringStatus(this,
					"Selection " + name + " not added: selection is null or parent mesh is not the same.");
		}
		return null;
	}

	private void replaceVertices(final HE_Mesh mesh) {
		clearVertices();
		addVertices(mesh.vertices);
	}

	protected void resetEdgeInternalLabels() {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setInternalLabel(-1);
		}
	}

	public void resetEdgeLabels() {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setLabel(-1);
		}
	}

	protected void resetFaceInternalLabels() {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(-1);
		}
	}

	public void resetFaceLabels() {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setLabel(-1);
		}
	}

	protected void resetHalfedgeInternalLabels() {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setInternalLabel(-1);
		}
	}

	public void resetHalfedgeLabels() {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setLabel(-1);
		}
	}

	protected void resetInternalLabels() {
		resetVertexInternalLabels();
		resetFaceInternalLabels();
		resetHalfedgeInternalLabels();
	}

	public void resetLabels() {
		resetVertexLabels();
		resetFaceLabels();
		resetHalfedgeLabels();
	}

	protected void resetVertexInternalLabels() {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setInternalLabel(-1);
		}
	}

	public void resetVertexLabels() {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setLabel(-1);
		}
	}

	public HE_Mesh rotateAboutAxis(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		final HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(px, py, pz), new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	public HE_Mesh rotateAboutAxis(final double angle, final WB_Coord p, final WB_Coord a) {
		final HE_Mesh result = copy();
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

	public HE_Mesh rotateAboutAxis2P(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		final HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		result.clearPrecomputed();
		return result;
	}

	public HE_Mesh rotateAboutAxis2P(final double angle, final WB_Coord p1, final WB_Coord p2) {
		final HE_Mesh result = copy();
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

	public HE_Mesh rotateAboutAxis2PSelf(final double angle, final double p1x, final double p1y, final double p1z,
			final double p2x, final double p2y, final double p2z) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(p1x, p1y, p1z), new WB_Vector(p2x - p1x, p2y - p1y, p2z - p1z));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	public HE_Mesh rotateAboutAxis2PSelf(final double angle, final WB_Coord p1, final WB_Coord p2) {
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

	public HE_Mesh rotateAboutAxisSelf(final double angle, final double px, final double py, final double pz,
			final double ax, final double ay, final double az) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		final WB_Transform3D raa = new WB_Transform3D();
		raa.addRotateAboutAxis(angle, new WB_Point(px, py, pz), new WB_Vector(ax, ay, az));
		while (vItr.hasNext()) {
			v = vItr.next();
			raa.applyAsPointSelf(v);
		}
		clearPrecomputed();
		return this;
	}

	public HE_Mesh rotateAboutAxisSelf(final double angle, final WB_Coord p, final WB_Coord a) {
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

	public HE_Mesh rotateAboutCenter(final double angle, final double ax, final double ay, final double az) {
		return rotateAboutAxis(angle, HE_MeshOp.getCenter(this), new WB_Vector(ax, ay, az));
	}

	public HE_Mesh rotateAboutCenter(final double angle, final WB_Coord a) {
		return rotateAboutAxis(angle, HE_MeshOp.getCenter(this), a);
	}

	public HE_Mesh rotateAboutCenterSelf(final double angle, final double ax, final double ay, final double az) {
		return rotateAboutAxisSelf(angle, HE_MeshOp.getCenter(this), new WB_Vector(ax, ay, az));
	}

	public HE_Mesh rotateAboutCenterSelf(final double angle, final WB_Coord a) {
		return rotateAboutAxisSelf(angle, HE_MeshOp.getCenter(this), a);
	}

	public HE_Mesh rotateAboutOrigin(final double angle, final double ax, final double ay, final double az) {
		final HE_Mesh result = copy();
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

	public HE_Mesh rotateAboutOrigin(final double angle, final WB_Coord a) {
		final HE_Mesh result = copy();
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

	public HE_Mesh rotateAboutOriginSelf(final double angle, final double ax, final double ay, final double az) {
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

	public HE_Mesh scale(final double scaleFactor) {
		return scale(scaleFactor, scaleFactor, scaleFactor);
	}

	public HE_Mesh scale(final double scaleFactorx, final double scaleFactory, final double scaleFactorz) {
		final HE_Mesh result = copy();
		final WB_Point center = HE_MeshOp.getCenter(result);
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

	public HE_Mesh scale(final double scaleFactorx, final double scaleFactory, final double scaleFactorz,
			final WB_Coord c) {
		final HE_Mesh result = copy();
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = result.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(c.xd() + scaleFactorx * (v.xd() - c.xd()), c.yd() + scaleFactory * (v.yd() - c.yd()),
					c.zd() + scaleFactorz * (v.zd() - c.zd()));
		}
		result.clearPrecomputed();
		return result;
	}

	public HE_Mesh scale(final double scaleFactor, final WB_Coord c) {
		return scale(scaleFactor, scaleFactor, scaleFactor, c);
	}

	public HE_Mesh scaleSelf(final double scaleFactor) {
		return scaleSelf(scaleFactor, scaleFactor, scaleFactor);
	}

	public HE_Mesh scaleSelf(final double scaleFactorx, final double scaleFactory, final double scaleFactorz) {
		final WB_Point center = HE_MeshOp.getCenter(this);
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

	public HE_Mesh scaleSelf(final double scaleFactorx, final double scaleFactory, final double scaleFactorz,
			final WB_Coord c) {
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			v.set(c.xd() + scaleFactorx * (v.xd() - c.xd()), c.yd() + scaleFactory * (v.yd() - c.yd()),
					c.zd() + scaleFactorz * (v.zd() - c.zd()));
		}
		clearPrecomputed();
		return this;
	}

	public HE_Mesh scaleSelf(final double scaleFactor, final WB_Coord c) {
		return scaleSelf(scaleFactor, scaleFactor, scaleFactor, c);
	}

	public HE_Selection selectAll() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		sel.addHalfedges(this.halfedges);
		sel.addVertices(this.vertices);
		return sel;
	}

	public HE_Selection selectAll(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		sel.addHalfedges(this.halfedges);
		sel.addVertices(this.vertices);
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectAllEdges() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addEdges(this.edges);
		return sel;
	}

	public HE_Selection selectAllEdges(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addEdges(this.edges);
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectAllFaces() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		return sel;
	}

	public HE_Selection selectAllFaces(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addFaces(this.faces);
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFaces(final int start, final int stop) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		for (int i = start; i < stop; i++) {
			sel.add(getFaceWithIndex(i));
		}
		return sel;
	}

	public HE_Selection selectFaces(final String name, final int start, final int stop) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		for (int i = start; i < stop; i++) {
			sel.add(getFaceWithIndex(i));
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectAllHalfedges() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addHalfedges(this.edges);
		sel.addHalfedges(this.halfedges);
		sel.addHalfedges(this.unpairedHalfedges);
		return sel;
	}

	public HE_Selection selectAllHalfedges(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addHalfedges(this.edges);
		sel.addHalfedges(this.halfedges);
		sel.addHalfedges(this.unpairedHalfedges);
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectAllVertices() {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addVertices(this.vertices);
		return sel;
	}

	public HE_Selection selectAllVertices(final String name) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		sel.addVertices(this.vertices);
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectBackEdges(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.BACK) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectBackEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.BACK) {
				sel.add(e);
			}
		}
		return sel;
	}

	public HE_Selection selectBackFaces(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.BACK) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectBackFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.BACK) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectBackVertices(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.BACK) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectBackVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.BACK) {
				sel.add(v);
			}
		}
		return sel;
	}

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

	public HE_Selection selectCrossingEdges(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.CROSSING) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectCrossingEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.CROSSING) {
				sel.add(e);
			}
		}
		return sel;
	}

	public HE_Selection selectCrossingFaces(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.CROSSING) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectCrossingFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.CROSSING) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectEdgesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getLabel() == label) {
				sel.add(e);
			}
		}
		return sel;
	}

	public HE_Selection selectEdgesWithLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getLabel() == label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectEdgesWithOtherInternalLabel(final String name, final int label) {
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

	public HE_Selection selectEdgesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getLabel() != label) {
				sel.add(e);
			}
		}
		return sel;
	}

	public HE_Selection selectEdgesWithOtherLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge e;
		final Iterator<HE_Halfedge> eItr = this.eItr();
		while (eItr.hasNext()) {
			e = eItr.next();
			if (e.getLabel() != label) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectEdgesWithInternalLabel(final String name, final int label) {
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

	public HE_Selection selectFacesWithInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() == label) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithInternalLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() == label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFacesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getLabel() == label) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getLabel() == label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFacesWithNormal(final String name, final WB_Coord v) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector w = new WB_Vector(v);
		w.normalizeSelf();
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (WB_Vector.dot(HE_MeshOp.getFaceNormal(f), v) > 1.0 - WB_Epsilon.EPSILON) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFacesWithNormal(final String name, final WB_Coord n, final double ta) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector nn = new WB_Vector(n);
		nn.normalizeSelf();
		final double cta = Math.cos(ta);
		final HE_FaceIterator fItr = sel.getParent().fItr();
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

	public HE_Selection selectFacesWithNormal(final WB_Coord v) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector w = new WB_Vector(v);
		w.normalizeSelf();
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (WB_Vector.dot(HE_MeshOp.getFaceNormal(f), v) > 1.0 - WB_Epsilon.EPSILON) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithNormal(final WB_Coord n, final double ta) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final WB_Vector nn = new WB_Vector(n);
		nn.normalizeSelf();
		final double cta = Math.cos(ta);
		final HE_FaceIterator fItr = sel.getParent().fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (nn.dot(HE_MeshOp.getFaceNormal(f)) > cta) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithOtherInternalLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() != label) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithOtherInternalLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getInternalLabel() != label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFacesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getLabel() != label) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFacesWithOtherLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Face f;
		final HE_FaceIterator fItr = this.fItr();
		while (fItr.hasNext()) {
			f = fItr.next();
			if (f.getLabel() != label) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFrontEdges(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.FRONT) {
				sel.add(e);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFrontEdges(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eitr = this.eItr();
		HE_Halfedge e;
		while (eitr.hasNext()) {
			e = eitr.next();
			if (HE_MeshOp.classifyEdgeToPlane3D(e, P) == WB_Classification.FRONT) {
				sel.add(e);
			}
		}
		return sel;
	}

	public HE_Selection selectFrontFaces(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.FRONT) {
				sel.add(f);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFrontFaces(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fitr = this.fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (HE_MeshOp.classifyFaceToPlane3D(f, P) == WB_Classification.FRONT) {
				sel.add(f);
			}
		}
		return sel;
	}

	public HE_Selection selectFrontVertices(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.FRONT) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectFrontVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.FRONT) {
				sel.add(v);
			}
		}
		return sel;
	}

	public HE_Selection selectHalfedgesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getLabel() == label) {
				sel.add(he);
			}
		}
		return sel;
	}

	public HE_Selection selectHalfedgesWithLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getLabel() == label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectHalfedgesWithOtherInternalLabel(final String name, final int label) {
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

	public HE_Selection selectHalfedgesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getLabel() != label) {
				sel.add(he);
			}
		}
		return sel;
	}

	public HE_Selection selectHalfedgesWithOtherLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Halfedge he;
		final Iterator<HE_Halfedge> heItr = this.heItr();
		while (heItr.hasNext()) {
			he = heItr.next();
			if (he.getLabel() != label) {
				sel.add(he);
			}
		}
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectHalfedgeWithInternalLabel(final String name, final int label) {
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

	public HE_Selection selectOnVertices(final String name, final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.ON) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public HE_Selection selectOnVertices(final WB_Plane P) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vitr = this.vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (WB_GeometryOp3D.classifyPointToPlane3D(v, P) == WB_Classification.ON) {
				sel.add(v);
			}
		}
		return sel;
	}

	public HE_Selection selectRandomEdges(final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
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

	public HE_Selection selectRandomEdges(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
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

	public HE_Selection selectRandomEdges(final String name, final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
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

	public HE_Selection selectRandomEdges(final String name, final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_EdgeIterator eItr = this.eItr();
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

	public HE_Selection selectRandomFaces(final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fItr = this.fItr();
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

	public HE_Selection selectRandomFaces(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fItr = this.fItr();
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

	public HE_Selection selectRandomFaces(final String name, final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fItr = this.fItr();
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

	public HE_Selection selectRandomFaces(final String name, final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_FaceIterator fItr = this.fItr();
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

	public HE_Selection selectRandomVertices(final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vItr = this.vItr();
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

	public HE_Selection selectRandomVertices(final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vItr = this.vItr();
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

	public HE_Selection selectRandomVertices(final String name, final double r) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vItr = this.vItr();
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

	public HE_Selection selectRandomVertices(final String name, final double r, final long seed) {
		final WB_MTRandom random = new WB_MTRandom(seed);
		final HE_Selection sel = HE_Selection.getSelection(this);
		final HE_VertexIterator vItr = this.vItr();
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

	public HE_Selection selectVerticesWithInternalLabel(final String name, final int label) {
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

	public HE_Selection selectVerticesWithLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getLabel() == label) {
				sel.add(v);
			}
		}
		return sel;
	}

	public HE_Selection selectVerticesWithLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getLabel() == label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

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

	public HE_Selection selectVerticesWithOtherInternalLabel(final String name, final int label) {
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

	public HE_Selection selectVerticesWithOtherLabel(final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getLabel() != label) {
				sel.add(v);
			}
		}
		return sel;
	}

	public HE_Selection selectVerticesWithOtherLabel(final String name, final int label) {
		final HE_Selection sel = HE_Selection.getSelection(this);
		HE_Vertex v;
		final Iterator<HE_Vertex> vItr = this.vItr();
		while (vItr.hasNext()) {
			v = vItr.next();
			if (v.getLabel() != label) {
				sel.add(v);
			}
		}
		selections.put(name, sel);
		return sel;
	}

	public void set(final HE_Mesh target) {
		final HE_Mesh result = target.copy();
		replaceVertices(result);
		replaceFaces(result);
		replaceHalfedges(result);
		selections = target.selections;
		integerAttributes = target.integerAttributes;
		floatAttributes = target.floatAttributes;
		doubleAttributes = target.doubleAttributes;
		booleanAttributes = target.booleanAttributes;
		stringAttributes = target.stringAttributes;
		attributes = target.attributes;
	}

	protected void setEdgeInternalLabels(final int label) {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setInternalLabel(label);
		}
	}

	public void setEdgeLabels(final int label) {
		final Iterator<HE_Halfedge> eItr = eItr();
		while (eItr.hasNext()) {
			eItr.next().setLabel(label);
		}
	}

	public void setFace(final HE_Halfedge he, final HE_Face f) {
		he.setFace(f);
		if (he.getPair() != null) {
			setPair(he, he.getPair());
		}
	}

	public void setFaceColor(final int color) {
		final HE_FaceIterator fitr = fItr();
		while (fitr.hasNext()) {
			fitr.next().setColor(color);
		}
	}

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

	public void setFaceColorWithOtherInternalLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() != i) {
				f.setColor(color);
			}
		}
	}

	public void setFaceColorWithOtherLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getLabel() != i) {
				f.setColor(color);
			}
		}
	}

	public void setFaceColorWithLabel(final int color, final int i) {
		final HE_FaceIterator fitr = fItr();
		HE_Face f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getLabel() == i) {
				f.setColor(color);
			}
		}
	}

	protected void setFaceInternalLabels(final int label) {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setInternalLabel(label);
		}
	}

	public void setFaceLabels(final int label) {
		final HE_FaceIterator fItr = fItr();
		while (fItr.hasNext()) {
			fItr.next().setLabel(label);
		}
	}

	public void setHalfedge(final HE_Face f, final HE_Halfedge he) {
		f.setHalfedge(he);
	}

	public void setHalfedge(final HE_Vertex v, final HE_Halfedge he) {
		v.setHalfedge(he);
	}

	public void setHalfedgeColor(final int color) {
		final HE_HalfedgeIterator heitr = heItr();
		while (heitr.hasNext()) {
			heitr.next().setColor(color);
		}
	}

	public void setHalfedgeColorWithInternalLabel(final int color, final int i) {
		final HE_HalfedgeIterator fitr = heItr();
		HE_Halfedge f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getInternalLabel() == i) {
				f.setColor(color);
			}
		}
	}

	public void setHalfedgeColorWithOtherInternalLabel(final int color, final int i) {
		final HE_HalfedgeIterator heitr = heItr();
		HE_Halfedge f;
		while (heitr.hasNext()) {
			f = heitr.next();
			if (f.getInternalLabel() != i) {
				f.setColor(color);
			}
		}
	}

	public void setHalfedgeColorWithOtherLabel(final int color, final int i) {
		final HE_HalfedgeIterator heitr = heItr();
		HE_Halfedge he;
		while (heitr.hasNext()) {
			he = heitr.next();
			if (he.getLabel() != i) {
				he.setColor(color);
			}
		}
	}

	public void setHalfedgeColorWithLabel(final int color, final int i) {
		final HE_HalfedgeIterator fitr = heItr();
		HE_Halfedge f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getLabel() == i) {
				f.setColor(color);
			}
		}
	}

	protected void setHalfedgeInternalLabels(final int label) {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setInternalLabel(label);
		}
	}

	public void setHalfedgeLabels(final int label) {
		final Iterator<HE_Halfedge> heItr = heItr();
		while (heItr.hasNext()) {
			heItr.next().setLabel(label);
		}
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setNext(final HE_Halfedge he, final HE_Halfedge hen) {
		he.setNext(hen);
		hen.setPrev(he);
	}

	void setNoCopy(final HE_Mesh target) {
		synchronized (this) {
			replaceVertices(target);
			replaceFaces(target);
			replaceHalfedges(target);
			selections = new HashMap<>();
			for (final String name : target.getSelectionNames()) {
				final HE_Selection sel = target.getSelection(name);
				final HE_Selection copy = new HE_Selection(this);
				copy.add(sel);
				addSelection(name, copy);
			}
			selections = target.selections;
			integerAttributes = target.integerAttributes;
			floatAttributes = target.floatAttributes;
			doubleAttributes = target.doubleAttributes;
			booleanAttributes = target.booleanAttributes;
			stringAttributes = target.stringAttributes;
			attributes = target.attributes;
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

	public void setPairNoSelectionCheck(final HE_Halfedge he1, final HE_Halfedge he2) {
		removeNoSelectionCheck(he1);
		removeNoSelectionCheck(he2);
		he1.setPair(he2);
		he2.setPair(he1);
	}

	public void setVertex(final HE_Halfedge he, final HE_Vertex v) {
		he.setVertex(v);
	}

	public void setVertex(final HE_Vertex v, final double x, final double y) {
		v.set(x, y);
	}

	public void setVertex(final HE_Vertex v, final double x, final double y, final double z) {
		v.set(x, y, z);
	}

	public void setVertex(final HE_Vertex v, final WB_Coord c) {
		v.set(c);
	}

	public void setVertexColor(final int color) {
		final HE_VertexIterator vitr = vItr();
		while (vitr.hasNext()) {
			vitr.next().setColor(color);
		}
	}

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

	public void setVertexColorWithOtherInternalLabel(final int color, final int i) {
		final HE_VertexIterator vitr = vItr();
		HE_Vertex v;
		while (vitr.hasNext()) {
			v = vitr.next();
			if (v.getInternalLabel() != i) {
				v.setColor(color);
			}
		}
	}

	public void setVertexColorWithOtherLabel(final int color, final int i) {
		final HE_VertexIterator fitr = vItr();
		HE_Vertex f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getLabel() != i) {
				f.setColor(color);
			}
		}
	}

	public void setVertexColorWithLabel(final int color, final int i) {
		final HE_VertexIterator fitr = vItr();
		HE_Vertex f;
		while (fitr.hasNext()) {
			f = fitr.next();
			if (f.getLabel() == i) {
				f.setColor(color);
			}
		}
	}

	protected void setVertexInternalLabels(final int label) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setInternalLabel(label);
		}
	}

	public void setVertexLabels(final int label) {
		final Iterator<HE_Vertex> vItr = vItr();
		while (vItr.hasNext()) {
			vItr.next().setLabel(label);
		}
	}

	public void setVertexWithIndex(final int index, final double x, final double y) {
		final HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(x, y);
	}

	public void setVertexWithIndex(final int index, final double x, final double y, final double z) {
		final HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(x, y, z);
	}

	public void setVertexWithIndex(final int index, final WB_Coord c) {
		final HE_Vertex v = getVertexWithIndex(index);
		if (v == null) {
			return;
		}
		v.set(c);
	}

	public void setVertexWithKey(final long key, final double x, final double y) {
		final HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(x, y);
	}

	public void setVertexWithKey(final long key, final double x, final double y, final double z) {
		final HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(x, y, z);
	}

	public void setVertexWithKey(final long key, final WB_Coord c) {
		final HE_Vertex v = getVertexWithKey(key);
		if (v == null) {
			return;
		}
		v.set(c);
	}

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

	public void smooth() {
		if (finished) {
			subdivide(new HES_CatmullClark());
		} else {
			subdivideThreaded(new HES_CatmullClark());
		}
	}

	public void smooth(final int rep) {
		if (finished) {
			subdivide(new HES_CatmullClark(), rep);
		} else {
			for (int i = 0; i < rep; i++) {
				subdivideThreaded(new HES_CatmullClark());
			}
		}
	}

	public void sort() {
		final List<HE_Face> sortedFaces = new HE_FaceList();
		sortedFaces.addAll(getFaces());
		Collections.sort(sortedFaces);
		clearFacesNoSelectionCheck();
		addFaces(sortedFaces);
		final List<HE_Vertex> sortedVertices = new HE_VertexList();
		sortedVertices.addAll(getVertices());
		Collections.sort(sortedVertices);
		clearVerticesNoSelectionCheck();
		addVertices(sortedVertices);
	}

	public void sort(final HE_FaceSort faceSort, final HE_VertexSort vertexSort) {
		final List<HE_Face> sortedFaces = new HE_FaceList();
		sortedFaces.addAll(getFaces());
		Collections.sort(sortedFaces);
		clearFacesNoSelectionCheck();
		addFaces(sortedFaces);
		final List<HE_Vertex> sortedVertices = new HE_VertexList();
		sortedVertices.addAll(getVertices());
		Collections.sort(sortedVertices);
		clearVerticesNoSelectionCheck();
		addVertices(sortedVertices);
	}

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

	public void subdivideThreaded(final HES_Subdividor subdividor, final int rep) {
		for (int i = 0; i < rep; i++) {
			tasks.add(new SubdividorThread(subdividor, this));
		}
	}

	public WB_SimpleMesh toSimpleMesh() {
		return gf.createMesh(getVerticesAsCoord(), getFacesAsInt());
	}

	@Override
	public String toString() {
		final String s = "HE_Mesh key: " + getKey() + ". (" + getNumberOfVertices() + ", " + getNumberOfFaces() + ")";
		return s;
	}

	public HE_Mesh transform(final WB_Transform3D T) {
		return copy().modify(new HEM_Transform3D(T));
	}

	public HE_Mesh transformSelf(final WB_Transform3D T) {
		return modify(new HEM_Transform3D(T));
	}

	public void uncapBoundaryHalfedges() {
		tracker.setStartStatus(this, "Uncapping boundary halfedges.");
		final WB_ProgressCounter counter = new WB_ProgressCounter(getNumberOfHalfedges(), 10);
		final List<HE_Halfedge> halfedges = getHalfedges();
		final HE_RAS<HE_Halfedge> remove = new HE_RAS<>();
		for (final HE_Halfedge he : halfedges) {
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
				final HE_Mesh result = future.get();
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

	public boolean validate() {
		return HET_Diagnosis.validate(this);
	}

	@Override
	public HE_VertexIterator vItr() {
		return new HE_VertexIterator(vertices);
	}

	@Override
	public void stats() {
		System.out.println("HE_Mesh: " + getKey());
		System.out.println("Number of vertices: " + this.getNumberOfVertices());
		System.out.println("Number of faces: " + this.getNumberOfFaces());
		System.out.println("Number of halfedges: " + this.edges.size() + "/" + this.halfedges.size() + "/"
				+ this.unpairedHalfedges.size());
		System.out.println("Number of boundary halfedges: " + this.getAllBoundaryHalfedges().size());
	}

	public WB_AABB getAABB() {
		return HE_MeshOp.getAABB(this);
	}

	public void triangulate() {
		HE_MeshOp.triangulate(this);
	}

	public HE_IntegerAttribute addIntegerAttribute(final String name, final int defaultValue,
			final boolean persistent) {
		final HE_IntegerAttribute att = new HE_IntegerAttribute(name, defaultValue, persistent);
		integerAttributes.put(name, att);
		return att;
	}

	public HE_FloatAttribute addFloatAttribute(final String name, final float defaultValue, final boolean persistent) {
		final HE_FloatAttribute att = new HE_FloatAttribute(name, defaultValue, persistent);
		floatAttributes.put(name, att);
		return att;
	}

	public HE_DoubleAttribute addDoubleAttribute(final String name, final double defaultValue,
			final boolean persistent) {
		final HE_DoubleAttribute att = new HE_DoubleAttribute(name, defaultValue, persistent);
		doubleAttributes.put(name, att);
		return att;
	}

	public HE_BooleanAttribute addBooleanAttribute(final String name, final boolean defaultValue,
			final boolean persistent) {
		final HE_BooleanAttribute att = new HE_BooleanAttribute(name, defaultValue, persistent);
		booleanAttributes.put(name, att);
		return att;
	}

	public HE_StringAttribute addStringAttribute(final String name, final String defaultValue,
			final boolean persistent) {
		final HE_StringAttribute att = new HE_StringAttribute(name, defaultValue, persistent);
		stringAttributes.put(name, att);
		return att;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HE_Attribute addAttribute(final String name, final Object defaultValue, final boolean persistent) {
		final HE_Attribute att = new HE_Attribute(name, defaultValue, persistent);
		attributes.put(name, att);
		return att;
	}

	public void setIntegerAttribute(final HE_Element element, final String name, final int value) {
		final HE_IntegerAttribute attribute = integerAttributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	public void setFloatAttribute(final HE_Element element, final String name, final float value) {
		final HE_FloatAttribute attribute = floatAttributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	public void setDoubleAttribute(final HE_Element element, final String name, final double value) {
		final HE_DoubleAttribute attribute = doubleAttributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	public void setBooleanAttribute(final HE_Element element, final String name, final boolean value) {
		final HE_BooleanAttribute attribute = booleanAttributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	public void setStringAttribute(final HE_Element element, final String name, final String value) {
		final HE_StringAttribute attribute = stringAttributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAttribute(final HE_Element element, final String name, final Object value) {
		final HE_Attribute attribute = attributes.get(name);
		if (attribute == null) {
			return;
		}
		attribute.set(element.getKey(), value);
	}

	public int getIntegerAttributeForElement(final HE_Element element, final String name) {
		final HE_IntegerAttribute attribute = integerAttributes.get(name);
		return attribute.get(element.getKey());
	}

	public float getFloatAttributeForElement(final HE_Element element, final String name) {
		final HE_FloatAttribute attribute = floatAttributes.get(name);
		return attribute.get(element.getKey());
	}

	public double getDoubleAttributeForElement(final HE_Element element, final String name) {
		final HE_DoubleAttribute attribute = doubleAttributes.get(name);
		return attribute.get(element.getKey());
	}

	public boolean getBooleanAttributeForElement(final HE_Element element, final String name) {
		final HE_BooleanAttribute attribute = booleanAttributes.get(name);
		return attribute.get(element.getKey());
	}

	public String getStringAttributeForElement(final HE_Element element, final String name) {
		final HE_StringAttribute attribute = stringAttributes.get(name);
		return attribute.get(element.getKey());
	}

	@SuppressWarnings("rawtypes")
	public Object getAttributeForElement(final HE_Element element, final String name) {
		final HE_Attribute attribute = attributes.get(name);
		return attribute.get(element.getKey());
	}

	public HE_IntegerAttribute getIntegerAttribute(final String name) {
		return integerAttributes.get(name);
	}

	public HE_FloatAttribute getFloatAttribute(final String name) {
		return floatAttributes.get(name);
	}

	public HE_DoubleAttribute getDoubleAttribute(final String name) {
		return doubleAttributes.get(name);
	}

	public HE_BooleanAttribute getBooleanAttribute(final String name) {
		return booleanAttributes.get(name);
	}

	public HE_StringAttribute getStringAttribute(final String name) {
		return stringAttributes.get(name);
	}

	@SuppressWarnings("rawtypes")
	public HE_Attribute getAttribute(final String name) {
		return attributes.get(name);
	}

	public void clearAttributes() {
		integerAttributes = new HashMap<>();
		floatAttributes = new HashMap<>();
		doubleAttributes = new HashMap<>();
		booleanAttributes = new HashMap<>();
		stringAttributes = new HashMap<>();
		attributes = new HashMap<>();
	}

	public void clearAttribute(final String name) {
		integerAttributes.remove(name);
		floatAttributes.remove(name);
		doubleAttributes.remove(name);
		booleanAttributes.remove(name);
		stringAttributes.remove(name);
		attributes.remove(name);
	}

	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	public Set<String> getIntegerAttributeNames() {
		return integerAttributes.keySet();
	}

	public Set<String> getFloatAttributeNames() {
		return floatAttributes.keySet();
	}

	public Set<String> getDoubleAttributeNames() {
		return doubleAttributes.keySet();
	}

	public Set<String> getBooleanAttributeNames() {
		return booleanAttributes.keySet();
	}

	public Set<String> getStringAttributeNames() {
		return stringAttributes.keySet();
	}

	public HE_Selection selectAttributeRange(final String attribute, final double min, final double max) {
		final boolean dblAtt = getDoubleAttribute(attribute) != null;
		final boolean fltAtt = getFloatAttribute(attribute) != null;
		final boolean intAtt = getIntegerAttribute(attribute) != null;
		final HE_Selection selection = getNewSelection();
		if (!dblAtt && !fltAtt && !intAtt) {
			return selection;
		}
		final HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		double value;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(v, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(v, attribute);
			} else {
				value = getIntegerAttributeForElement(v, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(v);
			}
		}
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(f, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(f, attribute);
			} else {
				value = getIntegerAttributeForElement(f, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(f);
			}
		}
		final HE_HalfedgeIterator heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(he, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(he, attribute);
			} else {
				value = getIntegerAttributeForElement(he, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(he);
			}
		}
		return selection;
	}

	public HE_Selection selectAttributeRange(final String name, final String attribute, final double min,
			final double max) {
		final boolean dblAtt = getDoubleAttribute(attribute) != null;
		final boolean fltAtt = getFloatAttribute(attribute) != null;
		final boolean intAtt = getIntegerAttribute(attribute) != null;
		final HE_Selection selection = getNewSelection("name");
		if (!dblAtt && !fltAtt && !intAtt) {
			return selection;
		}
		final HE_VertexIterator vItr = vItr();
		HE_Vertex v;
		double value;
		while (vItr.hasNext()) {
			v = vItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(v, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(v, attribute);
			} else {
				value = getIntegerAttributeForElement(v, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(v);
			}
		}
		final HE_FaceIterator fItr = fItr();
		HE_Face f;
		while (fItr.hasNext()) {
			f = fItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(f, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(f, attribute);
			} else {
				value = getIntegerAttributeForElement(f, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(f);
			}
		}
		final HE_HalfedgeIterator heItr = heItr();
		HE_Halfedge he;
		while (heItr.hasNext()) {
			he = heItr.next();
			if (dblAtt) {
				value = getDoubleAttributeForElement(he, attribute);
			} else if (fltAtt) {
				value = getFloatAttributeForElement(he, attribute);
			} else {
				value = getIntegerAttributeForElement(he, attribute);
			}
			if (value >= min && value <= max) {
				selection.add(he);
			}
		}
		return selection;
	}
}