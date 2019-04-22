import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;


void setup(){
size(100,100,P3D);
HE_Mesh mesh = new HE_Mesh(new HEC_Cube().setEdge(100));

/*
* The basic building block of HE_Mesh is the halfedge. In a valid
* manifold, each edge is shared by two faces. An open surface will have
* some edges with only one face. Each edge connects two vertices
* Imagine the edge is split lengthwise in two half edges, each one belonging to
* one of the faces and one of the vertices. The two halfedges are paired
* to each other.
* A face can be described as a loop of halfedges. Neighboring faces are found
* by finding the faces belonging to the pairs of the halfedge loop.
*
* http://www.flipcode.com/archives/The_Half-Edge_Data_Structure.shtml
*/

/* Halfedges store the most information
*
*  he.getVertex() = vertex belonging to halfedge
*  he.getEdge() = edge belonging to halfedge
*  he.getFace() = face belonging to halfedge
*  he.getNextInFace() = next halfedge in face
*  he.getPrevInFace() = previous halfedge in face
*  he.getNextInVertex() = next halfedge in vertex
*  he.getPrevInVertex() = previous halfedge in vertex
*  he.getPair() = halfedge belonging to same edge
*
* he.getFace() can be null (e.g. in a surface with boundaries). But even if an edge has only
* one face, both halfedges have to exist. I.e. there should always be a he.getPair();
*/

//You can use halfedges to loop around a face

HE_Face face=mesh.fItr().next(); // the first face of the mesh
println("All halfedges belonging to face "+face);
HE_Halfedge he = face.getHalfedge();
do{
  //do something here with each edge, or vertex, or neighboring face, ...
  println(he);
  he=he.getNextInFace();
}
while(he!=face.getHalfedge());

/*
* One caveat, be careful when changing the halfedge loop while going through it.
* Changing the next halfedge, he.setNext(something), can easily lead to an infinite
* loop, he.nextInface() never looping back to face.getHalfedge().
*
* No really: don't modify halfedge loops while traversing. Use one pass to store all necessary halfedges in a List or array,
* and then do a second pass through that List or arry to make the necessary adjustments.
*
*/

//Likewise, you can use halfedges to loop around a vertex

HE_Vertex v=mesh.vItr().next(); // the first vertex of the mesh
println();
println("All halfedges starting in vertex "+v);
he = v.getHalfedge();
do{
  //do something here with each edge, or vertex, or neighboring face, ...
  println(he);
  he=he.getNextInVertex();
}
while(he!=v.getHalfedge());
}

//Accessing_Mesh_Elements shows some more convenient way of navigating the mesh.
//However the above methods are fundamental.