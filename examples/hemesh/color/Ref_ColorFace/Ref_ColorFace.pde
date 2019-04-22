import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.core.*;

HE_Mesh mesh;
WB_Render3D render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  HE_FaceIterator fitr=mesh.fItr();
  while (fitr.hasNext()) {
    fitr.next().setColor(color(random(255), random(80), random(80,180)));
  }
  render=new WB_Render3D(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFacesFC(mesh);
}

void createMesh() {
  HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setEdge(200); 
  mesh=new HE_Mesh(creator); 
  HEM_ChamferCorners cc=new HEM_ChamferCorners().setDistance(40);
  mesh.modify(cc);
 
 mesh.getSelection("chamfer").modify(new HEM_Crocodile().setDistance(50));
}