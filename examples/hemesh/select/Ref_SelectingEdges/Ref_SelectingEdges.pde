import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_SelectRender3D selrender;
WB_Render3D render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setEdge(200); 
  mesh=new HE_Mesh(creator); 
  HET_MeshOp.splitFacesCenter(mesh);
  selrender=new WB_SelectRender3D(this);
  render=new WB_Render3D(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  pushMatrix();
  translate(width/2, height/2);
  stroke(0);
  strokeWeight(1);
  render.drawEdges(mesh);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  selrender.drawEdges(mesh,5);
  stroke(255, 0, 0);
  strokeWeight(2);
  render.drawEdge(selrender.getKey(), mesh);
  println(selrender.getKey());
  popMatrix();
}