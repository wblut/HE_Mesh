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
  render=new WB_Render(this);
  createMesh();
  mesh.modify(new HEM_Crocodile().setDistance(new Distance()).setChamfer(new XGradient()));
 
  mesh.smooth(2);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  fill(255,0,0);
  noStroke();
  render.drawFaces(mesh.getSelection("spikes"));
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_Geodesic creator=new HEC_Geodesic().setC(2).setB(2).setRadius(200);
  mesh=new HE_Mesh(creator); 
}

class XGradient implements WB_ScalarParameter {
  public double evaluate(double...x ) {
    return map((float)x[0], -200.0, 200.0, 0.0, 0.5);
  }
}

class Distance implements WB_ScalarParameter {
  public double evaluate(double...x ) {
    return map((float)x[1], -200.0, 200.0, -150.0, 300.0);
  }
}