import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;


WB_Render3D render;
WB_Danzer2D danzerA, danzerB, danzerC;
ArrayList<WB_Polygon> polys;
float scale;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  scale=400;
  danzerA=new WB_Danzer2D(scale, WB_Danzer2D.Type.A,new WB_Point(-250,0,0));
  danzerA.inflate(3);
  danzerB=new WB_Danzer2D(scale, WB_Danzer2D.Type.B);
  danzerB.inflate(3);
  danzerC=new WB_Danzer2D(scale, WB_Danzer2D.Type.C,new WB_Point(250,0,0));
  danzerC.inflate(3);
  polys=new ArrayList<WB_Polygon>();
  polys.addAll(danzerA.getTiles());
  polys.addAll(danzerB.getTiles());
  polys.addAll(danzerC.getTiles());
}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(0);
  for (WB_Polygon poly : polys) {
    render.drawPolygon(poly);
  }  
}
