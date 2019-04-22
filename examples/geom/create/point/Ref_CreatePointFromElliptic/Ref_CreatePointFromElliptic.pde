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
  float a=200;//pole
  float sigma;// >=0
  float tau;// (-1,1);

  WB_Point p;
  tau=(frameCount-1)*0.004;
  for (int i=0; i<201; i++) {
    sigma=(0.05*i);
    p=gf.createPointFromElliptic(a,sigma,tau);
    render.drawPoint2D(p);
    point(p.xf(),-p.yf());
   
   p=gf.createPointFromElliptic(a,sigma,-tau);
    render.drawPoint2D(p);
    point(p.xf(),-p.yf());

  }
}