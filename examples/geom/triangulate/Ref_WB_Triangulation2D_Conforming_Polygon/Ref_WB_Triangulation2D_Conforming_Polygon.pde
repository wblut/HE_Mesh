// Create a conforming Delaunay triangulation constrained by the boundaries of a polygon.
// -: New points are created to make the triangulation Delaunay, up to a certain tolerance.
// +: Robust, touching and intersecting contours are handled well.


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
int[] triangles;
ArrayList<WB_Point> points;
WB_CoordCollection pointsAfterTriangulation;
void setup() {
  size(800, 800);
  smooth(8);
  render=new WB_Render2D(this);
  createPolygon(); 
  WB_Triangulation2DWithPoints tri=WB_Triangulate.triangulateConforming2D(polygon);
  triangles=tri.getTriangles();
  pointsAfterTriangulation=tri.getPoints();
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(0);
  strokeWeight(1);
  fill(255,0,0);
  for (int i=0; i<triangles.length; i+=3) {
    beginShape(TRIANGLES);
    render.vertex2D( pointsAfterTriangulation.get(triangles[i]));
    render.vertex2D( pointsAfterTriangulation.get(triangles[i+1]));
    render.vertex2D( pointsAfterTriangulation.get(triangles[i+2]));
    endShape();
  }
  strokeWeight(2);
  stroke(0,0,255);
  noFill();
  render.drawPolygonEdges2D(polygon);
}


void createPolygon(){
  shell= new ArrayList<WB_Point>();
  points= new ArrayList<WB_Point>();
  WB_Point p;
  for (int i=0; i<20; i++) {
    p=gf.createPointFromPolar(50+75*(i%2+1), TWO_PI/20.0*i);
    shell.add(p);
    points.add(p);
  }
  //holes and points cannot overlap
  holes= new ArrayList[2];
  ArrayList<WB_Point> hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    p=gf.createPointFromPolar(50*(i%2+1), -TWO_PI/10.*i).addSelf(-20, 0, 0);
    hole.add(p);
    points.add(p);
  } 
  holes[0]=hole;
  hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    p=gf.createPointFromPolar(20*(i%2+1), PI-TWO_PI/10.*i).addSelf(80, 0, 0);
 
     hole.add(p);
    points.add(p);
  } 
  holes[1]=hole;
  polygon=gf.createPolygonWithHoles(shell, holes);
}