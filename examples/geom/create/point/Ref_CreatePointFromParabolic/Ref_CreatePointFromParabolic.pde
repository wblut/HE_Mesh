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
  render=new WB_Render2D(this);
}

void draw() {
  translate(width/2, height/2);
  float sigma;
  float tau;
  WB_Point p;
  stroke(255,0,0);
  tau=frameCount*0.1;
  for (int i=0; i<100; i++) {
    sigma=TWO_PI*0.1*i;
    p=gf.createPointFromParabolic(sigma,tau);
    render.drawPoint2D(p);
    p=gf.createPointFromParabolic(-sigma,tau);
    render.drawPoint2D(p);
 
  }
  
   stroke(0,0,255);
  sigma=frameCount*0.1;
  for (int i=0; i<100; i++) {
    tau=TWO_PI*0.1*i;
    p=gf.createPointFromParabolic(sigma,tau);
    render.drawPoint2D(p);
    p=gf.createPointFromParabolic(-sigma,tau);
    render.drawPoint2D(p);
 
  }
}