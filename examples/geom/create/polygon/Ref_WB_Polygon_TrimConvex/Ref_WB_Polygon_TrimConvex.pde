import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;
WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
ArrayList<WB_Point> shell;
WB_Polygon polygon;
List<WB_Polygon> polys;
List<WB_Polygon> trimmedPolys;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render2D(this);
  shell= new ArrayList<WB_Point>();

  for (int i=0; i<40; i++) {
    shell.add(gf.createPointFromPolar(100*(i%2+random(1, 3)), TWO_PI/40.0*i));
  } 
  polygon=gf.createSimplePolygon(shell);
  polys=gf.createConvexPolygonDecomposition2D(polygon);
  trimmedPolys= new ArrayList<WB_Polygon>();
  for (WB_Polygon poly : polys){
   trimmedPolys.add(poly.trimConvexPolygon(5));
  }
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  scale(1, -1);
  noFill();
  strokeWeight(2);
  stroke(0,0,255);
  render.drawPolygonEdges2D(polygon);
  strokeWeight(1.4);
  stroke(0);
  for (WB_Polygon poly : trimmedPolys){
    render.drawPolygonEdges2D(poly);
  }
 
}