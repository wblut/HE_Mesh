//work in progress, artifact-prone. Small and degenerate faces are not being removed yet...

import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
HE_Mesh mesh;
WB_Render render;
HE_Selection sel;
HEM_Soapfilm modifier;
double A;
double f;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  mesh.selectBoundaryVertices("boundary");
  modifier=new HEM_Soapfilm().setIterations(10).setFixed(mesh.getSelection("boundary"));
  f=0;
  A=mesh.getArea();
  render=new WB_Render(this);
}

void createMesh() {
  HEC_Creator creator=new HEC_Dodecahedron().setRadius(200);
  mesh=new HE_Mesh(creator); 
  mesh.modify(new HEM_Extrude().setDistance(100));
  mesh.getSelection("extruded").modify(new HEM_Extrude().setDistance(0).setChamfer(0.5));
  mesh.getSelection("extruded").modify(new HEM_Extrude().setDistance(100));
  mesh.getSelection("extruded").modify(new HEM_Extrude().setDistance(0).setChamfer(-0.5));
  mesh.deleteFaces(mesh.getSelection("extruded"));
  mesh.subdivide(new HES_Planar(), 2);
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
  render.drawFaces(mesh);
  if (f<0.9999) update();
}

void update() {
  mesh.modify(modifier); 
  double nA=mesh.getArea();
  f=nA/A;
  println(f);
  A=nA;
}