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
  render=new WB_Render(this);
}

void smooth() {
  //Laplacian modifier without adding vertices
  HEM_Smooth modifier=new HEM_Smooth();
  modifier.setIterations(1);
  modifier.setAutoRescale(true);// rescale mesh to original extents
  mesh.modify(modifier);
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
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_SuperDuper creator=new HEC_SuperDuper();
  creator.setU(16).setV(8).setRadius(50);
  creator.setDonutParameters(0, 10, 10, 10, 3, 6, 12, 12, 3, 1);
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_Extrude().setDistance(70).setChamfer(1));
  mesh.subdivide(new HES_Planar().setRandom(true).setRange(0.8));
}

void mousePressed() {
  smooth();
}
