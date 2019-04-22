import wblut.core.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
WB_Line L;

void setup() {
  size(1280, 720);
  background(55);
  smooth(8);
  stroke(255,50);
  render=new WB_Render2D(this);
  L=gf.createLineWithDirection2D(0,0,cos(PI/6),sin(PI/6));
 
}

void draw() {
  translate(width/2, height/2);
   if(frameCount==1){
     pushStyle();
     stroke(255,0,0);
     render.drawLine2D(L,400);
     popStyle();
   }
  WB_Point p;
  float x=(frameCount-1);
  for (int i=0; i<201; i++) {
    p=gf.createPoint(x,(i-100)*2);
    render.drawPoint2D(p);
    p=gf.createMirrorPoint2D(p,L);
    render.drawPoint2D(p);
     p=gf.createPoint(-x,(i-100)*2);
    render.drawPoint2D(p);
    p=gf.createMirrorPoint2D(p,L);
    render.drawPoint2D(p);
  }
}