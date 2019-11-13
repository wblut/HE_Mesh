import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;


HE_Mesh mesh;
WB_Render3D render;
WB_AABBTree3D tree;


void setup() {
  size(1000, 1000, P3D);  
  smooth(8);
  render=new WB_Render3D(this);
  mesh=new HE_Mesh(new HEC_Beethoven().setScale(10).setZAxis(0,1,0).setZAngle(PI));
  tree=new WB_AABBTree3D(mesh, 1);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(255, 0, 0);
  render.drawAABBTree(tree);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
}
