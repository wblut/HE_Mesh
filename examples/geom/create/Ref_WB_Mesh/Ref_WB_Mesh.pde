import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;


WB_Render3D render;
WB_Mesh mesh;

float scale;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  WB_MeshCreator geo=new WB_Geodesic(200,3,2);
  mesh=geo.create();  
}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(0);
  render.drawMesh(mesh);
}