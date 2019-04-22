import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane P, P2, P3;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  HEM_Mirror modifier=new HEM_Mirror();
  P=new WB_Plane(0, 0, 0, 0, 1, 1); 
  modifier.setPlane(P); 
  modifier.setOffset(0);
  modifier.setReverse(false);
  mesh.modify(modifier);
  P2=new WB_Plane(0, 0, 0, 1, -1, 1); 
  modifier.setPlane(P2);// mirror plane 
  mesh.modify(modifier);
  P3=new WB_Plane(-80, 0, 0, 1, 0, 0); 
  modifier.setPlane(P3);// mirror plane 
  mesh.modify(modifier);
 
 
  mesh.validate();
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(0.25*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh.getSelection("mirror0"));
  fill(255,0,0);
  noStroke();
  render.drawFaces(mesh.getSelection("mirror1"));
  noFill();
  stroke(0);
  render.drawEdges(mesh);
  stroke(255, 0, 0);
  render.drawEdges(mesh.getSelection("edges"));
  render.drawPlane(P, 300);
  render.drawPlane(P2, 300);
  render.drawPlane(P3, 300);
}


void createMesh() {

  mesh=new HE_Mesh(new HEC_Beethoven().setScale(10));
}