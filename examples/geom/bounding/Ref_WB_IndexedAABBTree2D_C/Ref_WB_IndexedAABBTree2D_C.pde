import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
import java.util.List;

WB_Render3D render;
WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Triangulation2DWithPoints triangulation;
List<WB_Coord> points;
WB_IndexedAABBTree2D tree;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
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
    points.add(gf.createPointFromPolar(200*(i%2+1), TWO_PI/20.0*i).addSelf(width/2, height/2));
  } 
  for (int i=0; i<40; i++) {
    WB_Point p=gf.createPointFromPolar(random(40, 150), TWO_PI/40.0*i).addSelf(width/2, height/2);
    points.add(p);
  }
  triangulation=WB_TriangulationFactory.triangulateConforming2D(points, constraints, 5);
  tree=new WB_IndexedAABBTree2D(triangulation, 1);
}

void draw() {
  background(55);
  noFill();
  strokeWeight(1.0);
  stroke(255, 0, 0, 20);
  render.drawAABBTree2D(tree);
  strokeWeight(1.4);
  stroke(0);
  render.drawTriangulationEdges2D(triangulation);
  int[] closest=tree.getClosestFace(new WB_Point(mouseX, mouseY));
  fill(255, 0, 0);
  render.drawTriangle2D(closest, triangulation.getPoints().toList());
  strokeWeight(2);
  stroke(0, 0, 255);
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
