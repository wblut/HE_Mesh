import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

WB_Render3D render;
WB_RandomPoint generator;
WB_Circle circle;
HE_Mesh mesh1,mesh2,mesh3;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 300.0);
  HEC_Disk dc=new HEC_Disk().setCircle(circle).setThickness(15.0);
  mesh1=new HE_Mesh(dc);
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 200.0);
  dc=new HEC_Disk().setCircle(circle).setThickness(30.0);
  mesh2=new HE_Mesh(dc);
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 100.0);
  dc=new HEC_Disk().setCircle(circle).setThickness(50.0);
  mesh3=new HE_Mesh(dc);
  
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  lights();
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,-PI,PI));
  noFill();
  strokeWeight(2);
  stroke(255,0,0);
  strokeWeight(1);
  stroke(100,0,0);
  render.drawEdges(mesh1);
  render.drawEdges(mesh2);
  render.drawEdges(mesh3);
  stroke(255,0,0);
  render.drawPlane(circle.getPlane(),340.0);
  noStroke();
  fill(255);
  render.drawFaces(mesh1);
  render.drawFaces(mesh2);
  render.drawFaces(mesh3);
  
}

void mouseClicked(){
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 300.0);
  HEC_Disk dc=new HEC_Disk().setCircle(circle).setThickness(15.0);
  mesh1=new HE_Mesh(dc);
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 200.0);
  dc=new HEC_Disk().setCircle(circle).setThickness(30.0);
  mesh2=new HE_Mesh(dc);
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 100.0);
  dc=new HEC_Disk().setCircle(circle).setThickness(50.0);
  mesh3=new HE_Mesh(dc);
}