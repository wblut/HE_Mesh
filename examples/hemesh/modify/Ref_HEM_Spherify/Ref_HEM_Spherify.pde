import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();

  HEM_Spherify modifier=new HEM_Spherify();
  modifier.setRadius(200);
  modifier.setCenter(100, 0, 0);
  mesh.modify(modifier);

  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  createMesh();

  HEM_Spherify modifier=new HEM_Spherify();
  modifier.setRadius(250);
  modifier.setCenter(0.5*(mouseX-400), 0, 0);
  modifier.setFactor(mouseY/800.0);
  mesh.modify(modifier);

  
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_Cube creator=new HEC_Cube(300, 5, 5, 5);
  mesh=new HE_Mesh(creator);
}

