/*
 * HE_Mesh Frederik Vanhoutte - www.wblut.com
 * https://github.com/wblut/HE_Mesh
 * A Processing/Java library for for creating and manipulating polygonal meshes.
 * Public Domain: http://creativecommons.org/publicdomain/zero/1.0/
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Frame;
import wblut.geom.WB_Frame.Strut;
import wblut.geom.WB_Frame.Node;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point;
import wblut.geom.WB_Vector;
import wblut.math.WB_ConstantScalarParameter;
import wblut.math.WB_ScalarParameter;

/**
 *
 */
public class HEC_FromFrame extends HEC_Creator {
	/**
	 *
	 */
	private static WB_GeometryFactory	gf	= new WB_GeometryFactory();
	/**
	 *
	 */
	private WB_Frame					frame;
	/**
	 *
	 */
	private int							numberOfNodes, numberOfStruts;
	/**
	 *
	 */
	private NodeType[]					nodeTypes;
	/**
	 *
	 */
	private NodeStrut[]			strutNodeConnections;
	/**
	 *
	 */
	private WB_ScalarParameter			strutRadius;
	/**
	 *
	 */
	private int							strutFacets;
	/**
	 *
	 */
	private double						fidget;
	/**
	 *
	 */
	private double						fillfactor;
	/**
	 *
	 */
	private HE_Mesh						mesh;
	/**
	 *
	 */
	private double						maximumStrutLength;
	/**
	 *
	 */
	private double						minimumBalljointAngle;
	/**
	 *
	 */
	private WB_ScalarParameter			maximumStrutOffset;
	/**
	 *
	 */
	private boolean						taper;
	/**
	 *
	 */
	private boolean						cap;
	/**
	 *
	 */
	private boolean						useNodeValues;
	/**
	 *
	 */
	private boolean						createIsolatedNodes;
	/**
	 *
	 */
	private WB_ScalarParameter			angleFactor;
	/**
	 *
	 */
	private boolean						suppressBalljoint;

	/**
	 *
	 */
	enum NodeType {
		/**
		 *
		 */
		ISOLATED,
		/**
		 *
		 */
		ENDPOINT,
		/**
		 *
		 */
		STRAIGHT,
		/**
		 *
		 */
		BEND,
		/**
		 *
		 */
		TURN,
		/**
		 *
		 */
		STAR
	}

	/**
	 *
	 */
	class NodeStrut {
		/**
		 *
		 */
		double			maxoffset;
		/**
		 *
		 */
		double			offset;
		/**
		 *
		 */
		double			radius;
		/**
		 *
		 */
		List<HE_Vertex>	vertices;
		/**
		 *
		 */
		WB_Vector		dir;
		/**
		 *
		 */
		Node			node;
		/**
		 *
		 */
		Strut		strut;

		/**
		 *
		 *
		 * @param node
		 * @param strut
		 * @param mo
		 * @param o
		 * @param r
		 */
		NodeStrut(final Node node, final Strut strut,
				final double mo, final double o, final double r) {
			maxoffset = mo;
			offset = o;
			radius = r;
			vertices = new ArrayList<HE_Vertex>();
			this.node = node;
			this.strut = strut;
		}
	}

	/**
	 *
	 */
	public HEC_FromFrame() {
		strutRadius = new WB_ConstantScalarParameter(5.0);
		strutFacets = 6;
		maximumStrutLength = 100.0;
		override = true;
		fidget = 1.0001;
		fillfactor = 0.99;
		minimumBalljointAngle = 0.55 * Math.PI;
		maximumStrutOffset = new WB_ConstantScalarParameter(
				Double.MAX_VALUE);
		angleFactor = new WB_ConstantScalarParameter(0.0);
		cap = true;
		useNodeValues = true;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEC_FromFrame setStrutRadius(final double r) {
		strutRadius = new WB_ConstantScalarParameter(r);
		return this;
	}

	/**
	 *
	 *
	 * @param r
	 * @return
	 */
	public HEC_FromFrame setStrutRadius(final WB_ScalarParameter r) {
		strutRadius = r;
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEC_FromFrame setMaximumStrutOffset(
			final WB_ScalarParameter o) {
		maximumStrutOffset = o;
		return this;
	}

	/**
	 *
	 *
	 * @param o
	 * @return
	 */
	public HEC_FromFrame setMaximumStrutOffset(final double o) {
		maximumStrutOffset = new WB_ConstantScalarParameter(o);
		return this;
	}

	/**
	 *
	 *
	 * @param a
	 * @return
	 */
	public HEC_FromFrame setMinimumBalljointAngle(final double a) {
		minimumBalljointAngle = a;
		return this;
	}

	/**
	 *
	 *
	 * @param d
	 * @return
	 */
	public HEC_FromFrame setMaximumStrutLength(final double d) {
		maximumStrutLength = d;
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEC_FromFrame setStrutFacets(final int f) {
		strutFacets = f;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromFrame setTaper(final boolean b) {
		taper = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromFrame setCap(final boolean b) {
		cap = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromFrame setSuppressBalljoint(final boolean b) {
		suppressBalljoint = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromFrame setUseNodeValues(final boolean b) {
		useNodeValues = b;
		return this;
	}

	/**
	 *
	 *
	 * @param b
	 * @return
	 */
	public HEC_FromFrame setCreateIsolatedNodes(final boolean b) {
		createIsolatedNodes = b;
		return this;
	}

	/**
	 *
	 *
	 * @param f
	 * @return
	 */
	public HEC_FromFrame setFidget(final double f) {
		fidget = f;
		return this;
	}

	/**
	 *
	 *
	 * @param ff
	 * @return
	 */
	public HEC_FromFrame setFillFactor(final double ff) {
		fillfactor = 0.99;
		return this;
	}

	/**
	 *
	 *
	 * @param af
	 * @return
	 */
	public HEC_FromFrame setAngleOffset(final double af) {
		angleFactor = new WB_ConstantScalarParameter(af);
		return this;
	}

	/**
	 *
	 *
	 * @param af
	 * @return
	 */
	public HEC_FromFrame setAngleOffset(final WB_ScalarParameter af) {
		angleFactor = af;
		return this;
	}

	/**
	 *
	 */
	private void getNodeTypes() {
		int i = 0;
		double minSpan;
		for (final Node node : frame.getNodes()) {
			if (node.getOrder() == 0) {
				nodeTypes[i] = NodeType.ISOLATED;
			} else if (node.getOrder() == 1) {
				nodeTypes[i] = NodeType.ENDPOINT;
			} else if (node.getOrder() == 2) {
				minSpan = node.findSmallestSpan();
				if (minSpan == Math.PI) {
					nodeTypes[i] = NodeType.STRAIGHT;
				} else if (minSpan > minimumBalljointAngle) {
					nodeTypes[i] = NodeType.BEND;
				} else {
					nodeTypes[i] = NodeType.TURN;
				}
			} else {
				nodeTypes[i] = NodeType.STAR;
			}
			i++;
		}
	}

	/**
	 *
	 */
	private void getNodeStruts() {
		int i = 0;
		for (final Node node : frame.getNodes()) {
			if (nodeTypes[i] == NodeType.ENDPOINT) {
				double r = strutRadius.evaluate(node.xd(), node.yd(),
						node.zd());
				if (useNodeValues) {
					r *= node.getValue();
				}
				createNodeStrut(node, 0, 0.0, 0.0, r);
			} else if (nodeTypes[i] == NodeType.STRAIGHT) {
				double r = strutRadius.evaluate(node.xd(), node.yd(),
						node.zd());
				if (useNodeValues) {
					r *= node.getValue();
				}
				double o = strutRadius.evaluate(node.xd(), node.yd(),
						node.zd());
				if (useNodeValues) {
					o *= node.getValue();
				}
				createNodeStrut(node, 0, o, o, r);
				createNodeStrut(node, 1, o, o, r);
			} else if (nodeTypes[i] == NodeType.TURN
					|| nodeTypes[i] == NodeType.BEND) {
				final double minSpan = node.findSmallestSpan();
				double r = strutRadius.evaluate(node.xd(), node.yd(),
						node.zd());
				double o = fidget * r / Math.min(1.0, Math.tan(0.5 * minSpan));
				if (useNodeValues) {
					r *= node.getValue();
				}
				if (useNodeValues) {
					o *= node.getValue();
				}
				createNodeStrut(node, 0, o, o, r);
				createNodeStrut(node, 1, o, o, r);
			} else if (nodeTypes[i] == NodeType.STAR) {
				final double minSpan = node.findSmallestSpan();
				double r = strutRadius.evaluate(node.xd(), node.yd(),
						node.zd());
				double mo = fidget * r / Math.min(1.0, Math.tan(0.5 * minSpan));
				if (useNodeValues) {
					r *= node.getValue();
				}
				if (useNodeValues) {
					mo *= node.getValue();
				}
				for (int j = 0; j < node.getOrder(); j++) {
					final double minLocSpan = node
							.findSmallestSpanAroundStrut(j);
					double o = fidget * r
							/ Math.min(1.0, Math.tan(0.5 * minLocSpan));
					if (useNodeValues) {
						o *= node.getValue();
					}
					double mso = maximumStrutOffset.evaluate(node.xd(),
							node.yd(), node.zd());
					if (useNodeValues) {
						mso *= node.getValue();
					}
					if (o > mso) {
						createNodeStrut(node, j, mo, mso, mso / fidget
								* Math.min(1.0, Math.tan(0.5 * minLocSpan)));
					} else {
						createNodeStrut(node, j, mo, o, r);
					}
				}
			}
			i++;
		}
		if (!taper) {
			for (i = 0; i < frame.getNumberOfStruts(); i++) {
				final double r = Math.min(
						strutNodeConnections[2 * i].radius,
						strutNodeConnections[2 * i + 1].radius);
				strutNodeConnections[2 * i].radius = r;
				strutNodeConnections[2 * i + 1].radius = r;
			}
		}
	}

	/**
	 *
	 *
	 * @param node
	 * @param i
	 * @param maxoff
	 * @param off
	 * @param rad
	 */
	private void createNodeStrut(final Node node, final int i,
			final double maxoff, final double off, final double rad) {
		final Strut strut = node.getStrut(i);
		final int id = getStrutIndex(node, strut);
		strutNodeConnections[id] = new NodeStrut(node, strut,
				maxoff, off, rad);
	}

	/**
	 *
	 */
	private void createVertices() {
		final double da = 2 * Math.PI / strutFacets;
		for (int id = 0; id < frame.getNumberOfStruts() * 2; id++) {
			final double sr = strutNodeConnections[id].radius;
			final double sgn = strutNodeConnections[id].node == strutNodeConnections[id].strut
					.start() ? 1 : -1;
			final double so = strutNodeConnections[id].maxoffset;
			final WB_Vector v = strutNodeConnections[id].strut
					.toVector();
			v.normalizeSelf();
			v.mulSelf(sgn);
			strutNodeConnections[id].dir = v.copy();
			final WB_Plane P = strutNodeConnections[id].strut
					.toPlane();
			final WB_Vector u = P.getU();
			for (int j = 0; j < strutFacets; j++) {
				final WB_Point p = new WB_Point(
						strutNodeConnections[id].node);
				final double af = angleFactor.evaluate(
						strutNodeConnections[id].node.xd(),
						strutNodeConnections[id].node.yd(),
						strutNodeConnections[id].node.zd());
				p.addMulSelf(so, v);
				final WB_Vector localu = u.mul(sr);
				localu.rotateAboutAxisSelf((j + af) * da, new WB_Point(),
						P.getNormal());
				p.addSelf(localu);
				final HE_Vertex vrtx = new HE_Vertex(p);
				vrtx.setInternalLabel(
						strutNodeConnections[id].node.getIndex());
				strutNodeConnections[id].vertices.add(vrtx);
				mesh.add(vrtx);
			}
		}
	}

	/**
	 *
	 *
	 * @param node
	 * @param strut
	 * @return
	 */
	private int getStrutIndex(final Node node,
			final Strut strut) {
		if (node == strut.start()) {
			return 2 * strut.getIndex();
		} else {
			return 2 * strut.getIndex() + 1;
		}
	}

	/**
	 *
	 */
	private void createStruts() {
		int i = 0;
		for (final Strut strut : frame.getStruts()) {
			/*
			 * System.out.println("HEC_FromFrame: Creating strut " + (i +
			 * 1) + " of " + frame.getNumberOfStruts() + ".");
			 */
			final int offsets = i * 2;
			final int offsete = i * 2 + 1;
			int ns = (int) Math
					.round(strut.getLength() / maximumStrutLength);
			ns = Math.max(ns, 1);
			final ArrayList<HE_Halfedge> hes = new ArrayList<HE_Halfedge>();
			final HE_Vertex[][] extraVertices = new HE_Vertex[strutFacets][ns
					- 1];
			for (int j = 0; j < strutFacets; j++) {
				for (int k = 0; k < ns - 1; k++) {
					extraVertices[j][k] = new HE_Vertex(
							gf.createInterpolatedPoint(
									strutNodeConnections[offsets].vertices
											.get(j),
									strutNodeConnections[offsete].vertices
											.get(j),
									(k + 1) / (double) ns));
					mesh.add(extraVertices[j][k]);
				}
			}
			for (int j = 0; j < strutFacets; j++) {
				final int jp = (j + 1) % strutFacets;
				final HE_Vertex s0 = strutNodeConnections[offsets].vertices
						.get(j);
				final HE_Vertex s1 = strutNodeConnections[offsets].vertices
						.get(jp);
				final HE_Vertex e2 = strutNodeConnections[offsete].vertices
						.get(jp);
				final HE_Vertex e3 = strutNodeConnections[offsete].vertices
						.get(j);
				for (int k = 0; k < ns; k++) {
					final HE_Face f = new HE_Face();
					final HE_Halfedge he0 = new HE_Halfedge();
					final HE_Halfedge he1 = new HE_Halfedge();
					final HE_Halfedge he2 = new HE_Halfedge();
					final HE_Halfedge he3 = new HE_Halfedge();
					mesh.setVertex(he0, k == 0 ? s0 : extraVertices[j][k - 1]);
					mesh.setVertex(he1, k == 0 ? s1 : extraVertices[jp][k - 1]);
					mesh.setVertex(he2,
							k == ns - 1 ? e2 : extraVertices[jp][k]);
					mesh.setVertex(he3, k == ns - 1 ? e3 : extraVertices[j][k]);
					mesh.setNext(he0, he1);
					mesh.setNext(he1, he2);
					mesh.setNext(he2, he3);
					mesh.setNext(he3, he0);
					mesh.setFace(he0, f);
					mesh.setHalfedge(f, he0);
					f.setInternalLabel(1);
					mesh.setFace(he1, f);
					mesh.setFace(he2, f);
					mesh.setFace(he3, f);
					mesh.setHalfedge(he0.getVertex(), he0);
					mesh.setHalfedge(he1.getVertex(), he1);
					mesh.setHalfedge(he2.getVertex(), he2);
					mesh.setHalfedge(he3.getVertex(), he3);
					mesh.add(f);
					mesh.add(he0);
					mesh.add(he1);
					mesh.add(he2);
					mesh.add(he3);
					hes.add(he1);
					hes.add(he3);
					if (k < ns - 1 && k > 0) {
						hes.add(he0);
						hes.add(he2);
					}
				}
			}
			i++;
		}
	}

	/**
	 *
	 */
	private void createNodes() {
		int i = 0;
		for (Node node : frame.getNodes()) {
			/*
			 * System.out.println("HEC_FromFrame: Creating node " + (i + 1) +
			 * " of " + frame.getNumberOfNodes() + ".");
			 */
			node = frame.getNode(i);
			final List<Strut> struts = node.getStruts();
			final List<HE_Vertex> hullPoints = new ArrayList<HE_Vertex>();
			if (nodeTypes[i] == NodeType.ENDPOINT) {
				if (cap) {
					int offset;
					if (node == struts.get(0).start()) {
						offset = struts.get(0).getIndex() * 2;
					} else {
						offset = struts.get(0).getIndex() * 2 + 1;
					}
					final ArrayList<HE_Halfedge> hes = new ArrayList<HE_Halfedge>(
							strutFacets);
					final HE_Face f = new HE_Face();
					mesh.add(f);
					for (int k = 0; k < strutFacets; k++) {
						final HE_Halfedge he = new HE_Halfedge();
						mesh.setVertex(he,
								strutNodeConnections[offset].vertices
										.get(k));
						mesh.setFace(he, f);
						hes.add(he);
						mesh.add(he);
					}
					mesh.setHalfedge(f, hes.get(0));
					f.setInternalLabel(3);
					if (node == struts.get(0).start()) {
						for (int k = 0, j = strutFacets
								- 1; k < strutFacets; j = k, k++) {
							mesh.setNext(hes.get(k), hes.get(j));
						}
					} else {
						for (int k = 0, j = strutFacets
								- 1; k < strutFacets; j = k, k++) {
							mesh.setNext(hes.get(j), hes.get(k));
						}
					}
				}
			} else {
				if (nodeTypes[i] != NodeType.ISOLATED || createIsolatedNodes) {
					double br = strutRadius.evaluate(node.xd(), node.yd(),
							node.zd());
					for (int j = 0; j < struts.size(); j++) {
						int offset;
						if (node == struts.get(j).start()) {
							offset = struts.get(j).getIndex() * 2;
						} else {
							offset = struts.get(j).getIndex() * 2 + 1;
						}
						for (int k = 0; k < strutFacets; k++) {
							hullPoints.add(
									strutNodeConnections[offset].vertices
											.get(k));
							br = Math.min(br,
									strutNodeConnections[offset].radius);
						}
					}
					br *= fillfactor;
					final int n = hullPoints.size();
					if (nodeTypes[i] != NodeType.STRAIGHT
							&& nodeTypes[i] != NodeType.BEND
							&& !suppressBalljoint) {
						final HE_Mesh ball = new HE_Mesh(new HEC_Sphere()
								.setRadius(br).setUFacets(strutFacets)
								.setVFacets(strutFacets).setCenter(node));
						hullPoints.addAll(ball.getVertices());
						mesh.addVertices(ball);
					}
					final HEC_ConvexHull ch = new HEC_ConvexHull()
							.setPointsFromVertices(hullPoints);
					try {
						final HE_Mesh tmp = new HE_Mesh(ch);
						final Map<Long, Integer> vertexToPointIndex = ch.vertexToPointIndex;
						final Iterator<HE_Face> tmpfItr = tmp.fItr();
						HE_Face f;
						HE_Halfedge tmphe;
						final ArrayList<HE_Face> facesToRemove = new ArrayList<HE_Face>();
						while (tmpfItr.hasNext()) {
							f = tmpfItr.next();
							f.setInternalLabel(2);
							tmphe = f.getHalfedge();
							int initid = vertexToPointIndex
									.get(tmphe.getVertex().getKey());
							boolean endface = initid < n;
							initid = initid / strutFacets;
							do {
								final int id = vertexToPointIndex
										.get(tmphe.getVertex().getKey());
								endface = id / strutFacets == initid
										&& id < n;
								if (!endface) {
									break;
								}
								tmphe = tmphe.getNextInFace();
							} while (tmphe != f.getHalfedge());
							if (endface) {
								facesToRemove.add(f);
							}
						}
						for (int j = 0; j < facesToRemove.size(); j++) {
							tmp.deleteFace(facesToRemove.get(j));
						}
						tmp.cleanUnusedElementsByFace();
						tmp.uncapBoundaryHalfedges();
						for (int j = 0; j < struts.size(); j++) {
							int offset;
							if (node == struts.get(j).start()) {
								offset = struts.get(j).getIndex() * 2;
							} else {
								offset = struts.get(j).getIndex() * 2 + 1;
							}
							final WB_Vector v = strutNodeConnections[offset].dir;
							v.mulSelf(strutNodeConnections[offset].offset
									- strutNodeConnections[offset].maxoffset);
							for (int k = 0; k < strutFacets; k++) {
								strutNodeConnections[offset].vertices
										.get(k).getPosition().addSelf(v);
							}
						}
						final Iterator<HE_Halfedge> tmpheItr = tmp.heItr();
						HE_Vertex tmpv;
						while (tmpheItr.hasNext()) {
							tmphe = tmpheItr.next();
							tmpv = tmphe.getVertex();
							final int j = vertexToPointIndex.get(tmpv.getKey());
							mesh.setVertex(tmphe, hullPoints.get(j));
							if (j >= n) {
								mesh.setHalfedge(hullPoints.get(j), tmphe);
							}
						}
						tmp.cleanUnusedElementsByFace();
						mesh.add(tmp);
					} catch (final Exception e) {
					}
				}
			}
			i++;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		mesh = new HE_Mesh();
		numberOfNodes = frame.getNumberOfNodes();
		// System.out.println(numberOfNodes + " " +
		// frame.getNumberOfStruts());
		nodeTypes = new NodeType[numberOfNodes];
		getNodeTypes();
		numberOfStruts = frame.getNumberOfStruts();
		strutNodeConnections = new NodeStrut[2 * numberOfStruts];
		getNodeStruts();
		createVertices();
		createNodes();
		createStruts();
		HE_MeshOp.pairHalfedges(mesh);
		if (!cap) {
			HE_MeshOp.capHalfedges(mesh);
		}
		return mesh;
	}

	/**
	 *
	 *
	 * @param frame
	 * @return
	 */
	public HEC_FromFrame setFrame(final WB_Frame frame) {
		this.frame = frame;
		return this;
	}


	/**
	 *
	 *
	 * @param mesh
	 * @return
	 */
	public HEC_FromFrame setFrame(final HE_Mesh mesh) {
		frame = mesh.getFrame();
		return this;
	}
	
}
