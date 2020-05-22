/**
 *
 */
package wblut.hemesh;

/**
 *
 */
public class HEC_FromFaces extends HEC_Creator {
	private HE_Selection source;

	public HEC_FromFaces setFaces(final HE_Selection faces) {
		this.source = faces.get();
		return this;
	}

	@Override
	protected HE_Mesh createBase() {
		if (source == null) {
			return new HE_Mesh();
		}
		if (source.getNumberOfFaces() == 0) {
			return new HE_Mesh();
		}
		source.collectVertices();
		final HE_IntMap vertexIndices = new HE_IntMap();
		final HE_VertexIterator vItr = source.vItr();
		final double[][] vertices = new double[source.getNumberOfVertices()][3];
		int id = 0;
		HE_Vertex v;
		while (vItr.hasNext()) {
			v = vItr.next();
			vertices[id] = new double[] { v.xd(), v.yd(), v.zd() };
			vertexIndices.put(v, id++);
		}
		final HE_FaceIterator fItr = source.fItr();
		final int[][] faces = new int[source.getNumberOfFaces()][];
		HE_Face f;
		HE_FaceHalfedgeInnerCirculator fheiCrc;
		HE_Halfedge he;
		id = 0;
		int index, idj;
		while (fItr.hasNext()) {
			f = fItr.next();
			fheiCrc = f.fheiCrc();
			faces[id] = new int[f.getFaceDegree()];
			idj = 0;
			while (fheiCrc.hasNext()) {
				he = fheiCrc.next();
				index = vertexIndices.getIfAbsent(he.getVertex(), -1);
				faces[id][idj++] = index;
			}
			id++;
		}
		return new HEC_FromFacelist().setVertices(vertices).setFaces(faces).setCheckManifold(false)
				.setCheckNormals(false).createBase();
	}
}
