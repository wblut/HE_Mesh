//Convex decomposition of a polygon, holes are not supported.

import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
ArrayList<WB_Point> shell;
ArrayList<WB_Point>[] holes;
WB_Polygon polygon;
List<WB_Polygon> convexDecomposition;

void setup() {
  size(800, 800);
  smooth(8);
  render=new WB_Render2D(this);
  shell= new ArrayList<WB_Point>();
   for (int i=0; i<40; i++) {
    shell.add(gf.createPointFromPolar(50+75*(i%2+1+random(2.0)), TWO_PI/40.0*i));
  }
  holes= new ArrayList[2];
  ArrayList<WB_Point> hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    hole.add(gf.createPointFromPolar(20*(i%2+1), -TWO_PI/10.*i).addSelf(-50, 0, 0));
  } 
  holes[0]=hole;
  hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    hole.add(gf.createPointFromPolar(20*(i%2+1), PI-TWO_PI/10.*i).addSelf(50, 0, 0));
  } 
  holes[1]=hole;
  polygon=gf.createPolygonWithHoles(shell, holes);
  convexDecomposition=gf.createConvexPolygonDecomposition2D(polygon);
  background(55);
}

void draw() {
  background(200);
  translate(width/2, height/2);
  scale(1, -1);
  noFill();
  strokeWeight(2);
  stroke(0,0,255);
  render.drawPolygonEdges2D(polygon);
  
  noStroke();
  fill(255);
  for (WB_Polygon convexPoly : convexDecomposition){
   render.drawPolygon2D(convexPoly);
  }
 strokeWeight(1.4);
  stroke(0);
  for (WB_Polygon convexPoly : convexDecomposition){
   render.drawPolygonEdges2D(convexPoly);
  }
}