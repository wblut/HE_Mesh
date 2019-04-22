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

  HES_Planar subdividor=new HES_Planar();
  
  subdividor.setRandom(true);// Randomize center edge and center face points 
  subdividor.setRange(0.4);// Random range of center offset, from 0 (no random) to 1(fully random)
  subdividor.setSeed(1234);// Seed of random point generator
  subdividor.setKeepTriangles(true);// Subdivide triangles into 4 triangles instead of 3 quads
  mesh.subdivide(subdividor);

  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_Cube creator=new HEC_Cube(450, 1,1, 1);
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_ChamferCorners().setDistance(70));
}