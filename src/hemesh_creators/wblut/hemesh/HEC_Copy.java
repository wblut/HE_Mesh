package wblut.hemesh;

import java.util.Iterator;
import java.util.Set;

import wblut.core.WB_ProgressReporter.WB_ProgressCounter;
import wblut.geom.WB_Coord;

/**
 *
 */
public class HEC_Copy extends HEC_Creator {
	/**  */
	private HE_HalfedgeStructure source;
	/**  */
	HE_LongMap vertexCorrelation;
	/**  */
	HE_LongMap faceCorrelation;
	/**  */
	HE_LongMap halfedgeCorrelation;

	/**
	 *
	 */
	public HEC_Copy() {
		super();
		setOverride(true);
		setModelViewOverride(true);
	}

	/**
	 *
	 *
	 * @param source
	 */
	public HEC_Copy(final HE_HalfedgeStructure source) {
		this();
		setMesh(source);
	}

	/**
	 *
	 *
	 * @param source
	 * @return
	 */
	public HEC_Copy setMesh(final HE_HalfedgeStructure source) {
		this.source = source;
		return this;
	}

	/**
	 *
	 *
	 * @param source
	 * @return
	 */
	public HEC_Copy setSource(final HE_HalfedgeStructure source) {
		this.source = source;
		return this;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_LongMap getVertexCorrelation() {
		return vertexCorrelation;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_LongMap getFaceCorrelation() {
		return faceCorrelation;
	}

	/**
	 *
	 *
	 * @return
	 */
	public HE_LongMap getHalfedgeCorrelation() {
		return halfedgeCorrelation;
	}

	/**
	 *
	 *
	 * @return
	 */
	@Override
	protected HE_Mesh createBase() {
		tracker.setStartStatus(this, "Starting HEC_Copy.");
		final HE_Mesh result = new HE_Mesh();
		if (source == null) {
			tracker.setStopStatus(this, "No source mesh. Exiting HEC_Copy.");
			return result;
		}
		if (source instanceof HE_Mesh) {
			final HE_Mesh mesh = (HE_Mesh) source;
			result.copyProperties(mesh);
			vertexCorrelation = new HE_LongMap();
			faceCorrelation = new HE_LongMap();
			halfedgeCorrelation = new HE_LongMap();
			HE_Vertex rv;
			HE_Vertex v;
			WB_ProgressCounter counter = new WB_ProgressCounter(mesh.getNumberOfVertices(), 10);
			tracker.setCounterStatus(this, "Creating vertices.", counter);
			final Iterator<HE_Vertex> vItr = mesh.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				rv = new HE_Vertex(v);
				result.add(rv);
				rv.copyProperties(v);
				vertexCorrelation.put(v.getKey(), rv.getKey());
				counter.increment();
			}
			HE_Face rf;
			HE_Face f;
			counter = new WB_ProgressCounter(mesh.getNumberOfFaces(), 10);
			tracker.setCounterStatus(this, "Creating faces.", counter);
			final HE_FaceIterator fItr = mesh.fItr();
			while (fItr.hasNext()) {
				f = fItr.next();
				rf = new HE_Face();
				result.add(rf);
				rf.copyProperties(f);
				faceCorrelation.put(f.getKey(), rf.getKey());
				counter.increment();
			}
			HE_Halfedge rhe;
			HE_Halfedge he;
			counter = new WB_ProgressCounter(mesh.getNumberOfHalfedges(), 10);
			tracker.setCounterStatus(this, "Creating halfedges.", counter);
			final HE_RAS<HE_Halfedge> copyHalfedges = new HE_RAS<>();
			final Iterator<HE_Halfedge> heItr = mesh.getHalfedges().iterator();
			while (heItr.hasNext()) {
				he = heItr.next();
				rhe = new HE_Halfedge();
				copyHalfedges.add(rhe);
				rhe.copyProperties(he);
				halfedgeCorrelation.put(he.getKey(), rhe.getKey());
				counter.increment();
			}
			counter = new WB_ProgressCounter(mesh.getNumberOfVertices(), 10);
			tracker.setCounterStatus(this, "Setting vertex properties.", counter);
			HE_Vertex sv;
			HE_Vertex tv;
			final Iterator<HE_Vertex> svItr = mesh.vItr();
			final Iterator<HE_Vertex> tvItr = result.vItr();
			Long key;
			while (svItr.hasNext()) {
				sv = svItr.next();
				tv = tvItr.next();
				tv.set(sv);
				if (sv.getHalfedge() != null) {
					key = halfedgeCorrelation.get(sv.getHalfedge().getKey());
					if (key >= 0) {
						result.setHalfedge(tv, copyHalfedges.getWithKey(key));
					}
				}
				counter.increment();
			}
			counter = new WB_ProgressCounter(mesh.getNumberOfFaces(), 10);
			tracker.setCounterStatus(this, "Setting face properties.", counter);
			HE_Face sf;
			HE_Face tf;
			final HE_FaceIterator sfItr = mesh.fItr();
			final HE_FaceIterator tfItr = result.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				tf = tfItr.next();
				if (sf.getHalfedge() != null) {
					key = halfedgeCorrelation.get(sf.getHalfedge().getKey());
					if (key >= 0) {
						result.setHalfedge(tf, copyHalfedges.getWithKey(key));
					}
				}
				counter.increment();
			}
			counter = new WB_ProgressCounter(mesh.getNumberOfHalfedges(), 10);
			tracker.setCounterStatus(this, "Setting halfedge properties.", counter);
			HE_Halfedge she;
			HE_Halfedge the;
			final Iterator<HE_Halfedge> sheItr = mesh.getHalfedges().iterator();
			final Iterator<HE_Halfedge> theItr = copyHalfedges.iterator();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				the = theItr.next();
				if (she.getPair() != null) {
					key = halfedgeCorrelation.get(she.getPair().getKey());
					if (key >= 0) {
						the.setPair(copyHalfedges.getWithKey(key));
						the.getPair().setPair(the);
					}
				}
				if (she.getNextInFace() != null) {
					key = halfedgeCorrelation.get(she.getNextInFace().getKey());
					if (key >= 0) {
						the.setNext(copyHalfedges.getWithKey(key));
						the.getNextInFace().setPrev(the);
					}
				}
				if (she.getVertex() != null) {
					key = vertexCorrelation.get(she.getVertex().getKey());
					if (key >= 0) {
						result.setVertex(the, result.getVertexWithKey(key));
					}
				}
				if (she.getFace() != null) {
					key = faceCorrelation.get(she.getFace().getKey());
					if (key >= 0) {
						result.setFace(the, result.getFaceWithKey(key));
					}
				}
				result.add(the);
				counter.increment();
			}
			copySelections(mesh, result);
			copyAttributes(mesh, result);
			tracker.setStopStatus(this, "Exiting HEC_Copy.");
		} else if (source instanceof HE_Selection) {
			final HE_Selection sel = ((HE_Selection) source).get();
			result.copyProperties(sel);
			sel.completeFromFaces();
			vertexCorrelation = new HE_LongMap();
			faceCorrelation = new HE_LongMap();
			halfedgeCorrelation = new HE_LongMap();
			HE_Vertex rv;
			HE_Vertex v;
			WB_ProgressCounter counter = new WB_ProgressCounter(sel.getNumberOfVertices(), 10);
			tracker.setCounterStatus(this, "Creating vertices.", counter);
			final Iterator<HE_Vertex> vItr = sel.vItr();
			while (vItr.hasNext()) {
				v = vItr.next();
				rv = new HE_Vertex(v);
				result.add(rv);
				rv.copyProperties(v);
				vertexCorrelation.put(v.getKey(), rv.getKey());
				counter.increment();
			}
			HE_Face rf;
			HE_Face f;
			counter = new WB_ProgressCounter(sel.getNumberOfFaces(), 10);
			tracker.setCounterStatus(this, "Creating faces.", counter);
			final HE_FaceIterator fItr = sel.fItr();
			while (fItr.hasNext()) {
				f = fItr.next();
				rf = new HE_Face();
				result.add(rf);
				rf.copyProperties(f);
				faceCorrelation.put(f.getKey(), rf.getKey());
				counter.increment();
			}
			HE_Halfedge rhe;
			HE_Halfedge he;
			counter = new WB_ProgressCounter(sel.getNumberOfHalfedges(), 10);
			final HE_RAS<HE_Halfedge> copyHalfedges = new HE_RAS<>();
			tracker.setCounterStatus(this, "Creating halfedges.", counter);
			final Iterator<HE_Halfedge> heItr = sel.heItr();
			while (heItr.hasNext()) {
				he = heItr.next();
				rhe = new HE_Halfedge();
				copyHalfedges.add(rhe);
				rhe.copyProperties(he);
				halfedgeCorrelation.put(he.getKey(), rhe.getKey());
				counter.increment();
			}
			counter = new WB_ProgressCounter(sel.getNumberOfVertices(), 10);
			tracker.setCounterStatus(this, "Setting vertex properties.", counter);
			HE_Vertex sv;
			HE_Vertex tv;
			final Iterator<HE_Vertex> svItr = sel.vItr();
			final Iterator<HE_Vertex> tvItr = result.vItr();
			Long key;
			while (svItr.hasNext()) {
				sv = svItr.next();
				tv = tvItr.next();
				tv.set(sv);
				if (sv.getHalfedge() != null) {
					key = halfedgeCorrelation.get(sv.getHalfedge().getKey());
					if (key >= 0) {
						result.setHalfedge(tv, copyHalfedges.getWithKey(key));
					}
				}
				counter.increment();
			}
			counter = new WB_ProgressCounter(sel.getNumberOfFaces(), 10);
			tracker.setCounterStatus(this, "Setting face properties.", counter);
			HE_Face sf;
			HE_Face tf;
			final HE_FaceIterator sfItr = sel.fItr();
			final HE_FaceIterator tfItr = result.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				tf = tfItr.next();
				if (sf.getHalfedge() != null) {
					key = halfedgeCorrelation.get(sf.getHalfedge().getKey());
					if (key >= 0) {
						result.setHalfedge(tf, copyHalfedges.getWithKey(key));
					}
				}
				counter.increment();
			}
			counter = new WB_ProgressCounter(sel.getNumberOfHalfedges(), 10);
			tracker.setCounterStatus(this, "Setting halfedge properties.", counter);
			HE_Halfedge she;
			HE_Halfedge the;
			final Iterator<HE_Halfedge> sheItr = sel.heItr();
			final Iterator<HE_Halfedge> theItr = copyHalfedges.iterator();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				the = theItr.next();
				if (she.getPair() != null && source.contains(she.getPair())) {
					key = halfedgeCorrelation.get(she.getPair().getKey());
					if (key >= 0) {
						the.setPair(copyHalfedges.getWithKey(key));
						the.getPair().setPair(the);
					}
				}
				if (she.getNextInFace() != null) {
					key = halfedgeCorrelation.get(she.getNextInFace().getKey());
					if (key >= 0) {
						the.setNext(copyHalfedges.getWithKey(key));
						the.getNextInFace().setPrev(the);
					}
				}
				if (she.getVertex() != null) {
					key = vertexCorrelation.get(she.getVertex().getKey());
					if (key >= 0) {
						result.setVertex(the, result.getVertexWithKey(key));
					}
				}
				if (she.getFace() != null && source.contains(she.getFace())) {
					key = faceCorrelation.get(she.getFace().getKey());
					if (key >= 0) {
						result.setFace(the, result.getFaceWithKey(key));
					}
				}
				result.add(the);
				counter.increment();
			}
			HE_MeshOp.pairHalfedges(result);
			HE_MeshOp.capHalfedges(result);
			tracker.setStopStatus(this, "Exiting HEC_Copy.");
		}
		return result;
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param result
	 */
	void copySelections(final HE_Mesh mesh, final HE_Mesh result) {
		final String[] names = mesh.getSelectionNames();
		for (final String name : names) {
			final HE_Selection sourceSel = mesh.getSelection(name);
			final HE_Selection targetSel = HE_Selection.getSelection(result);
			final HE_VertexIterator svItr = sourceSel.vItr();
			HE_Vertex sv;
			long key;
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				targetSel.add(result.getVertexWithKey(key));
			}
			final HE_HalfedgeIterator sheItr = sourceSel.heItr();
			HE_Halfedge she;
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				targetSel.add(result.getHalfedgeWithKey(key));
			}
			final HE_FaceIterator sfItr = sourceSel.fItr();
			HE_Face sf;
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				targetSel.add(result.getFaceWithKey(key));
			}
			result.addSelection(name, targetSel);
		}
	}

	/**
	 *
	 *
	 * @param mesh
	 * @param result
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void copyAttributes(final HE_Mesh mesh, final HE_Mesh result) {
		Set<String> names = mesh.getAttributeNames();
		HE_Vertex sv;
		HE_Halfedge she;
		HE_Face sf;
		long key;
		for (final String name : names) {
			final HE_Attribute sourceAtt = mesh.getAttribute(name);
			final HE_Attribute targetAtt = result.addAttribute(name, sourceAtt.defaultValue, sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final Object o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final Object o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final Object o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getIntegerAttributeNames();
		for (final String name : names) {
			final HE_IntegerAttribute sourceAtt = mesh.getIntegerAttribute(name);
			final HE_IntegerAttribute targetAtt = result.addIntegerAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final int o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final int o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final int o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getFloatAttributeNames();
		for (final String name : names) {
			final HE_FloatAttribute sourceAtt = mesh.getFloatAttribute(name);
			final HE_FloatAttribute targetAtt = result.addFloatAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final float o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final float o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final float o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getDoubleAttributeNames();
		for (final String name : names) {
			final HE_DoubleAttribute sourceAtt = mesh.getDoubleAttribute(name);
			final HE_DoubleAttribute targetAtt = result.addDoubleAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final double o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final double o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final double o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getBooleanAttributeNames();
		for (final String name : names) {
			final HE_BooleanAttribute sourceAtt = mesh.getBooleanAttribute(name);
			final HE_BooleanAttribute targetAtt = result.addBooleanAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final boolean o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final boolean o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final boolean o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getStringAttributeNames();
		for (final String name : names) {
			final HE_StringAttribute sourceAtt = mesh.getStringAttribute(name);
			final HE_StringAttribute targetAtt = result.addStringAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final String o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final String o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final String o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getPointAttributeNames();
		for (final String name : names) {
			final HE_PointAttribute sourceAtt = mesh.getPointAttribute(name);
			final HE_PointAttribute targetAtt = result.addPointAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final WB_Coord o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final WB_Coord o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final WB_Coord o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
		names = mesh.getVectorAttributeNames();
		for (final String name : names) {
			final HE_VectorAttribute sourceAtt = mesh.getVectorAttribute(name);
			final HE_VectorAttribute targetAtt = result.addVectorAttribute(name, sourceAtt.defaultValue,
					sourceAtt.persistent);
			final HE_VertexIterator svItr = source.vItr();
			while (svItr.hasNext()) {
				sv = svItr.next();
				key = vertexCorrelation.get(sv.getKey());
				final WB_Coord o = sourceAtt.get(sv.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_HalfedgeIterator sheItr = source.heItr();
			while (sheItr.hasNext()) {
				she = sheItr.next();
				key = halfedgeCorrelation.get(she.getKey());
				final WB_Coord o = sourceAtt.get(she.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
			final HE_FaceIterator sfItr = source.fItr();
			while (sfItr.hasNext()) {
				sf = sfItr.next();
				key = faceCorrelation.get(sf.getKey());
				final WB_Coord o = sourceAtt.get(sf.getKey());
				if (o != sourceAtt.defaultValue) {
					targetAtt.set(key, o);
				}
			}
		}
	}
}
