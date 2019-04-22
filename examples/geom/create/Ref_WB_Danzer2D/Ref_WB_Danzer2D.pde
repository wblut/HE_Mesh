import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;


WB_Render3D render;
WB_Danzer2D danzerA, danzerB, danzerC;

float scale;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  scale=550;
  danzerA=new WB_Danzer2D(scale, WB_Danzer2D.Type.A,new WB_Point(-250,0,0));
  danzerA.inflate(3);
  danzerB=new WB_Danzer2D(scale, WB_Danzer2D.Type.B);
  danzerB.inflate(3);
  danzerC=new WB_Danzer2D(scale, WB_Danzer2D.Type.C,new WB_Point(250,0,0));
  danzerC.inflate(3);

}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(0);
  render.drawTriangle2D(danzerA);
  render.drawTriangle2D(danzerB);
  render.drawTriangle2D(danzerC);
   
}