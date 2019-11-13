import wblut.core.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;


void setup() {
  size(1280, 720);
  background(55);
  smooth(8);
  stroke(255, 50);
  fill(255, 50);
  render=new WB_Render2D(this);
}

void draw() {
  translate(width/2, height/2);
  float a=100;
  float sigma;
  float tau;

  WB_Point p;
  tau=frameCount*0.01;
  for (int i=0; i<100; i++) {
    sigma=TWO_PI*0.01*i;
    p=gf.createPointFromBipolar(a,sigma,tau);
    render.drawPoint2D(p);
    p=gf.createPointFromBipolar(-a,sigma,tau);
    render.drawPoint2D(p);
  }
}