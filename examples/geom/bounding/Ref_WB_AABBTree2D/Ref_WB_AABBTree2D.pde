import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

WB_Render3D render;
int[] tris;
WB_CoordCollection points;
WB_AABBTree2D tree;
WB_Danzer2D danzerC;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  danzerC=new WB_Danzer2D(800.0, WB_Danzer2D.Type.C, new WB_Point(width/2, height/2));
  danzerC.inflate(3);
  points=danzerC.getPoints();
  tris=danzerC.getTriangles();
  tree=new WB_AABBTree2D(tris, points, 1);
}

void draw() {
  background(55);
  noFill();
  strokeWeight(1.0);
  stroke(255, 0, 0, 40);
  render.drawAABBTree2D(tree);
  strokeWeight(1.4);
  stroke(0);
  render.drawTriangle2D(danzerC);
  WB_Triangle closest=tree.getClosestFace(new WB_Point(mouseX, mouseY));
  fill(255, 0, 0);
  render.drawTriangle2D(closest);
}
