import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
ArrayList<WB_Point> shell, hole;
WB_Polygon polygon1, polygon2;
List<WB_Polygon> booleanResult;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render2D(this);
  shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100*(i%2+1), TWO_PI/20.0*i).addSelf(-100, 0, 0));
  }
  hole= new ArrayList<WB_Point>();
  // hole has to be completely in polygon, points cannot overlap
  for (int i=0; i<10; i++) {
    hole.add(gf.createPointFromPolar(40*(i%2+1), -TWO_PI/10.*i).addSelf(-100, 0, 0));
  } 
  polygon1=gf.createPolygonWithHole(shell, hole);


  for (int i=0; i<20; i++) {
    shell.get(i).addSelf(200, 100, 0);
  }
  for (int i=0; i<10; i++) {
    hole.get(i).addSelf(200, 100, 0);
  }
  polygon2=gf.createPolygonWithHole(shell, hole);

  booleanResult=gf.subtractPolygons2D(polygon1, polygon2);
  strokeWeight(2);
}

void draw() {
  background(55);
  translate(width/2, height/2);

 if(mouseX>400){
  for (WB_Polygon poly : booleanResult) {
    
    stroke(0);
    noFill();
    render.drawPolygonEdges2D(poly);
    noStroke();
    fill(255, 0, 0, 100);
    render.drawPolygon2D(poly);
  }
 }
 else{
    stroke(0);
    noFill();
    render.drawPolygonEdges2D(polygon1);
    render.drawPolygonEdges2D(polygon2);
    noStroke();
    fill(0,255, 0, 100);
    render.drawPolygon2D(polygon1);
     fill(0, 0, 255, 100);
    render.drawPolygon2D(polygon2);
 }
 
}