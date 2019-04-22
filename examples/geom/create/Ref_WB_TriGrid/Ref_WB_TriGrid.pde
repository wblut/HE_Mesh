import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

WB_Render3D render;
HE_Mesh trimesh, hexmesh;

float scale;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  WB_TriGrid trigrid=new WB_TriGrid();
  trigrid.setScale(40*2*cos(PI/6));
  trimesh=new HE_Mesh(trigrid.getHex(5));
  hexmesh=new HE_Mesh(new HEC_Dual().setSource(trimesh).setKeepBoundary(true));

}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  stroke(200,0,0);
  render.drawEdges(trimesh);
  stroke(0);
  render.drawEdges(hexmesh);

}