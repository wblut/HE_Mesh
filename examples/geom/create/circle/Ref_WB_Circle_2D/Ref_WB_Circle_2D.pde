import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
/*
Draw circles inside circle
Demonstrates 2D circles in HE_Mesh
*/


WB_Render2D render;
WB_RandomPoint generator;
WB_Circle[] circles=new WB_Circle[20];
int numCircles;
float radius;

void setup() {
  size(800, 800, P2D);
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render2D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
 
  radius=250;
  //random points distributed uniformily inside disk with radius 250
  generator=new WB_RandomDisk().setRadius(radius);
  numCircles=20;
  circles=new WB_Circle[numCircles];
 
  for (int i=0; i<numCircles; i++) {
    WB_Point p=generator.nextPoint();
    //calculate radius: radius of disk - distance of center circle to center disk)
    float rad=radius-(float)WB_GeometryOp.getDistance2D(p, WB_Point.ORIGIN());
    //create circle (center, radius)
    circles[i]=new WB_Circle(p,  rad);
  }
}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(255,0,0);
  ellipse(0,0,2*radius,2*radius);
  stroke(0);
  for (WB_Circle C : circles) {
    render.drawCircle2D(C);
  }
  
  //Replace one circle every frame
  WB_Point p=generator.nextPoint();
  float rad=radius-(float)WB_GeometryOp.getDistance2D(p, WB_Point.ORIGIN());
  circles[frameCount%numCircles]=new WB_Circle(p,  rad);
}