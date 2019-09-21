import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;

HE_Mesh base;
HE_MeshCollection meshes;
WB_Render3D render;


void setup() {
  size(1000, 1000, P3D);  
  smooth(8);
  render=new WB_Render3D(this);
  base=new HE_Mesh(new HEC_Beethoven().setScale(10).setZAxis(0,1,0).setZAngle(PI));
  HEMC_SplitMesh sm=new HEMC_SplitMesh().setMesh(base).setOffset(40).setPlane(new WB_Plane(0,0,0,1,1,1));
  meshes=sm.create();
}

  void draw() {
  background(55);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(255, 0, 0);
  render.drawEdges(meshes);
  fill(255);
  noStroke();
  render.drawFaces(meshes);
}
