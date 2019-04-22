import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;



void setup() {
  size(100, 100, P3D);
  HE_Mesh mesh = new HE_Mesh(new HEC_Cube().setEdge(100));


  /*
* A HE_Mesh objects contains 3 kinds of elements, 2 of them self-
   * explanatory:
   *   HE_Vertex
   *   HE_Face
   *
   * The 3th, the halfedge, represents both an edge and a "halfedge". This requires a bit more explanation, see Halfedge
   *
   *  HE_Halfedge
   *
   * Each element has a unique key that is used to access it. 
   */


  // Retrieve a single element; requires the key (a long value normally available from context or a HET_Selector), the key of an element never changes. An element knows its own key:
  long key=0;
  HE_Vertex v= mesh.getVertexWithKey(key);
  HE_Face f=  mesh.getFaceWithKey(key);
  HE_Halfedge e=mesh.getEdgeWithKey(key);
  HE_Halfedge he= mesh.getHalfedgeWithKey(key);

  // Or use an index; these are straightforward however the index of an element will typically change. An element does not know its own index, but the mesh it belongs to does:
  int index=0;
  v =mesh.getVertexWithIndex(index);
  index=mesh.getIndex(v);
  f= mesh.getFaceWithIndex(index);
  e =mesh.getEdgeWithIndex(index);
  he =mesh.getHalfedgeWithIndex(index);


  /*
  * Looping through all elements is done with iterators.
   */
  println();
  println("# vertices: "+mesh.getNumberOfVertices());
  HE_VertexIterator vItr= mesh.vItr();
  while (vItr.hasNext ()) {
    v=vItr.next();
    println(v);
    //do thingy
  }
  println();
  println("# faces: "+mesh.getNumberOfFaces());
  HE_FaceIterator fItr=mesh.fItr();
  while (fItr.hasNext ()) {
    f=fItr.next();
    println(f);
    //do thingy
  }
  println();
  println("# edges: "+mesh.getNumberOfEdges());
  HE_EdgeIterator eItr=mesh.eItr();
  while (eItr.hasNext ()) {
    e=eItr.next();
    println(e);
    //do thingy
  }
  println();
  println("# halfedges: "+mesh.getNumberOfHalfedges());
  HE_HalfedgeIterator heItr=mesh.heItr();
  while (heItr.hasNext ()) {
    he=heItr.next();
    println(he);
    //do thingy
  }
  println();

  /*
  * Looping around faces and vertices is done with circulators.
   */

  v=mesh.getVertexWithIndex(0);
  println("Vertex neighbors of vertex: "+v);
  HE_VertexVertexCirculator vvCrc=v.vvCrc();
  HE_Vertex vneighbor;
  while (vvCrc.hasNext ()) {
    vneighbor=vvCrc.next();
    println(vneighbor);
    //do thingy
  }
  println();

  println("Edge star of vertex: "+v);
  HE_VertexEdgeCirculator veCrc=v.veCrc();
  while (veCrc.hasNext ()) {
    e=veCrc.next();
    println(e);
    //do thingy
  }
  println();

  println("Face star of vertex: "+v);
  HE_VertexFaceCirculator vfCrc=v.vfCrc();
  while (vfCrc.hasNext ()) {
    f=vfCrc.next();
    println(f);
    //do thingy
  }
  println();

  println("Outward halfedge star of vertex: "+v);
  HE_VertexHalfedgeOutCirculator vheoCrc=v.vheoCrc();
  while (vheoCrc.hasNext ()) {
    he=vheoCrc.next();
    println(he);
    //do thingy
  }
  println();

  println("Inward halfedge star of vertex: "+v);
  HE_VertexHalfedgeInCirculator vheiCrc=v.vheiCrc();
  while (vheiCrc.hasNext ()) {
    he=vheiCrc.next();
    println(he);
    //do thingy
  }
  println();
  f=mesh.getFaceWithIndex(0);

  println("Vertices of face: "+f);
  HE_FaceVertexCirculator fvCrc=f.fvCrc();
  while (fvCrc.hasNext ()) {
    v=fvCrc.next();
    println(v);
    //do thingy
  }
  println();

  println("Edges of face: "+f);
  HE_FaceEdgeCirculator feCrc=f.feCrc();
  while (feCrc.hasNext ()) {
    e=feCrc.next();
    println(e);
    //do thingy
  }
  println();

  HE_Face fadjacent;
  println("Neighboring faces of face: "+f);
  HE_FaceFaceCirculator ffCrc=f.ffCrc();
  while (ffCrc.hasNext ()) {
    fadjacent=ffCrc.next();
    println(fadjacent);
    //do thingy
  }
  println();

  println("Inner halfedges of face: "+f);
  HE_FaceHalfedgeInnerCirculator fheiCrc=f.fheiCrc();
  while (fheiCrc.hasNext ()) {
    he=fheiCrc.next();
    println(he);
    //do thingy
  }
  println();

  println("Outer halfedges of face: "+f);
  HE_FaceHalfedgeOuterCirculator fheoCrc=f.fheoCrc();
  while (fheoCrc.hasNext ()) {
    he=fheoCrc.next();
    println(he);
    //do thingy
  }
  println();
}