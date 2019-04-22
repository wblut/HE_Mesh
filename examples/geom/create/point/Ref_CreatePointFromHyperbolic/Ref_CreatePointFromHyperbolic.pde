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
  stroke(255,200);
  render=new WB_Render2D(this);
}

void draw() {
  float u;
  float v;
  WB_Point p;
  
  u=(frameCount-1)*0.01;
  for (int i=0; i<100; i++) {
    v=TWO_PI*i;
    p=gf.createPointFromHyperbolic(u,v);
    render.drawPoint2D(p); 
    p=gf.createPointFromHyperbolic(-u,v);
    render.drawPoint2D(p);   
  }   
}