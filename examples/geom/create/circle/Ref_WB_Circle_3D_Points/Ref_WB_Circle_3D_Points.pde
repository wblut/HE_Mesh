import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

WB_Render3D render;
WB_RandomPoint generator;
WB_Circle circle;
WB_Point[] points;

void setup() {
  size(1000, 1000, P3D);
  ortho();
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 300.0);
  points=circle.getPoints(36);
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,-PI,PI));
  strokeWeight(2);
  stroke(255);
  render.drawCircle(circle);
  strokeWeight(1);
  render.drawPoint(points,4.0);
  stroke(255,0,0);
  render.drawGizmo(50);
  render.drawPlane(circle.getPlane(),340.0);
  
}

void mouseClicked(){
   circle=new WB_Circle(WB_Point.ORIGIN(),new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0)), 300.0);
   points=circle.getPoints(36);
}