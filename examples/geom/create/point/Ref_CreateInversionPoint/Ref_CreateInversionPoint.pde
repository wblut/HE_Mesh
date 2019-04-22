import wblut.core.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
WB_Circle C;

void setup() {
  size(1280, 720);
  background(55);
  smooth(8);
  stroke(255, 50);
noFill();
  render=new WB_Render2D(this);
  C=gf.createCircleWithRadius(0, 0, 200);
}

void draw() {
  translate(width/2, height/2);
  if (frameCount==1) {
    pushStyle();
    stroke(255, 0, 0);
    render.drawCircle2D(C);
    popStyle();
  }
  WB_Point p;
  float x=(frameCount-1);
  for (int i=0; i<201; i++) {

    p=gf.createPoint(x, (i-100)*20);
    render.drawPoint2D(p);
    p=gf.createInversionPoint2D(p, C);
    if (p!=null)render.drawPoint2D(p);
    p=gf.createPoint(-x, (i-100)*20);
    render.drawPoint2D(p);
    p=gf.createInversionPoint2D(p, C);
    if (p!=null)render.drawPoint2D(p);
  }
}