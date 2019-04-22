import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh, copymesh;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  
  HEM_Noise modifier=new HEM_Noise();
  modifier.setDistance(20);
  copymesh.modify(modifier);

  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  HEM_Noise modifier=new HEM_Noise();
  copymesh=mesh.get();
  modifier.setDistance(mouseY/20);
  //modifier.setSeed(125);
  copymesh.modify(modifier);
  
  fill(255);
  noStroke();
  render.drawFaces(copymesh);
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh(){
  HEC_Cube creator=new HEC_Cube(300,5,5,5);
  mesh=new HE_Mesh(creator); 
  copymesh=mesh.get();
}