import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;


HE_Mesh mesh, diamesh;
WB_Render3D render;


void setup() {
  size(1000, 1000, P3D);  
  smooth(8);
  render=new WB_Render3D(this);
  mesh=new HE_Mesh(new HEC_Beethoven().setScale(10));
  diamesh=mesh.get();
  diamesh.modify(new HEM_Diagrid());
  diamesh.validate();
}

  void draw() {
  background(55);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(255, 0, 0);
  render.drawEdges(diamesh);
  fill(255);
  noStroke();
  render.drawFaces(diamesh);
}