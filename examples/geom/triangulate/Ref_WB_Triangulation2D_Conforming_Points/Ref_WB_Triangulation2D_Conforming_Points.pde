import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
WB_Triangulation2DWithPoints confTri;
List<WB_Coord> points;
WB_CoordCollection pointsAfterTriangulation;
int[] triangles;

void setup() {
  size(800, 800);
  smooth(8);
  render=new WB_Render2D(this);
  points= new ArrayList<WB_Coord>();
  int[] constraints=new int[120];
  for (int i=0; i<20; i++) {
    constraints[2*i]=i;
    constraints[2*i+1]=(i+1)%20;
  }
  for (int i=0; i<40; i++) {
    constraints[40+2*i]=20+i;
    constraints[40+2*i+1]=20+(i+1)%40;
  }

  for (int i=0; i<20; i++) {
    points.add(gf.createPointFromPolar(150*(i%2+1), TWO_PI/20.0*i));
  } 
  for (int i=0; i<40; i++) {
    WB_Point p=gf.createPointFromPolar(random(20, 100), TWO_PI/40.0*i);
    points.add(p);
  }

  confTri=WB_Triangulate.triangulateConforming2D(points, constraints,5);
  triangles=confTri.getTriangles();
  pointsAfterTriangulation=confTri.getPoints();
  background(55);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  stroke(0);
  strokeWeight(1);
  fill(255, 0, 0);
  for (int i=0; i<triangles.length; i+=3) {
    beginShape(TRIANGLES);
    render.vertex2D(pointsAfterTriangulation.get(triangles[i]));
    render.vertex2D(pointsAfterTriangulation.get(triangles[i+1]));
    render.vertex2D(pointsAfterTriangulation.get(triangles[i+2]));
    endShape();
  }

  strokeWeight(2);
  stroke(0,0,255);
  for (int i=0; i<20; i++) {
    beginShape(LINES);
    render.vertex2D(points.get(i));
    render.vertex2D(points.get((i+1)%20));
    endShape();
  }
  for (int i=0; i<40; i++) {
    beginShape(LINES);
    render.vertex2D(points.get(20+i));
    render.vertex2D(points.get(20+(i+1)%40));
    endShape();
  }
}