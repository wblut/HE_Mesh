//Turns a polygon with holes into a polygon without holes by connecting all contours into
//a single one.
//In principle algorithms that do not support a polygon with holes can then be used. 
//However it would need to support coincident edges and vertices to work porperluy.


import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render3D render;
WB_Polygon polygon;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render3D(this);
  createPolygon();
  polygon=gf.createSimplePolygon(polygon);
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  scale(1, -1);
  noFill();
  stroke(0, 0, 255);
  strokeWeight(3);
  render.drawPolygonEdges2D(polygon);
  stroke(0);
  fill(255,0,0);
  strokeWeight(0.4);
   render.drawPolygon2D(polygon);

 
}


void createPolygon() {
  ArrayList<WB_Point> shell;
  ArrayList<WB_Point>[] holes;
  shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100+75*(i%2+1), TWO_PI/20.0*i));
  }
  holes= new ArrayList[55];
  for (int h=0; h<55; h++) {
    ArrayList<WB_Point> hole=new ArrayList<WB_Point>();
    for (int i=0; i<10; i++) {
      hole.add(gf.createPointFromPolar(6*(i%2+1), -TWO_PI/10.*i).addSelf(-135+27*(h%11), -80+(h/11)*40, 0));
    } 
    holes[h]=hole;
  }

  polygon=gf.createPolygonWithHoles(shell, holes);

}