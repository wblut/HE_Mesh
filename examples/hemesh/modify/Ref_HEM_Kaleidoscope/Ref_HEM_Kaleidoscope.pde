import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  HEM_Kaleidoscope modifier=new HEM_Kaleidoscope();
  modifier.setAngle(0);
  modifier.setOrigin(new WB_Point(0, 0, 0));
  modifier.setAxis(new WB_Vector(0, 1, 0));
  modifier.setSymmetry(5);
  modifier.setAngle(random(PI));
  mesh.modify(modifier);
  render=new WB_Render(this);
  println(mesh.getSelectionNames());
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(0.25*TWO_PI);
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("mirror0"));
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  stroke(255,0,0);
  render.drawEdges(mesh.getSelection("edges"));
  
}


void createMesh() {
  mesh=new HE_Mesh(new HEC_Beethoven().setScale(10).setCenter(50, 0, 0).setZAxis(1, 0, 1));
}