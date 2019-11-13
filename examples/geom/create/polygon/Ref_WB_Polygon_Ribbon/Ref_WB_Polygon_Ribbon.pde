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
List<WB_Polygon> ribbon;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  render=new WB_Render2D(this);
  shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(50+75*(i%2+1), TWO_PI/20.0*i));
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
 
  ribbon=gf.createRibbonPolygons(polygon,20);
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  scale(1, -1);
  fill(255, 0, 0);
  noStroke();
  for(WB_Polygon poly:ribbon){
  render.drawPolygon2D(poly);
  }
  noFill();
  stroke(0);
  render.drawPolygonEdges2D(polygon);
  for(WB_Polygon poly:ribbon){
  render.drawPolygonEdges2D(poly);
  }
 
}