import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render3D render;

//Color per halfedge = color per face per vertex

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setEdge(200); 
  mesh=new HE_Mesh(creator);
  HET_MeshOp.splitFacesCenter(mesh);
  mesh.getSelection("center").modify(new HEM_Extrude().setDistance(-40).setChamfer(0.2));
HET_MeshOp.splitFacesTri(mesh);

  HE_FaceIterator fitr=mesh.fItr();
  while (fitr.hasNext ()) {
    HE_Face f=fitr.next();
    HE_Halfedge he=f.getHalfedge();
    do {
      he.setColor(color(random(256), random(80), random(80,180)));
      he=he.getNextInFace();
    } while (he!=f.getHalfedge ());
  }


  render=new WB_Render3D(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFacesHC(mesh);
}