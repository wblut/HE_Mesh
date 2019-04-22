import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
/*
Draw circles on surface of sphere
Demonstrates 3D circles in HE_Mesh
*/


WB_Render3D render;
WB_RandomPoint generator;
WB_Circle[] circles=new WB_Circle[20];
int numCircles;
float radius;

void setup() {
  size(1000, 1000, P3D);
  ortho();
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
 
  radius=250;
  //random points distributed uniformily inside sphere with radius 250
  generator=new WB_RandomInSphere().setRadius(radius);
  numCircles=100;
  circles=new WB_Circle[numCircles];
 
  for (int i=0; i<numCircles; i++) {
    WB_Point p=generator.nextPoint();
    //calculate radius: sq(distance of center circle to center sphere)+ sq(radius of circle)=sq(radius of sphere)
    float rad=sqrt(radius*radius-(float)WB_GeometryOp.getSqDistance3D(p, WB_Point.ORIGIN()));
    //create circle (center, normal of plane of circle, radius)
    circles[i]=new WB_Circle(p, p, rad);
  }
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  strokeWeight(4);
  stroke(255,0,0);
  ellipse(0,0,2*radius,2*radius);
  strokeWeight(2);
  stroke(0);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,-PI,PI));
  for (WB_Circle C : circles) {
    render.drawCircle(C);
  }
  
  //Replace one circle every frame
  WB_Point p=generator.nextPoint();
  float rad=sqrt(radius*radius-(float)WB_GeometryOp.getSqDistance3D(p, WB_Point.ORIGIN()));
  circles[frameCount%numCircles]=new WB_Circle(p, p, rad);
}