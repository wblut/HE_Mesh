import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
import java.util.List;

WB_Render3D render;
WB_RandomPoint source;
List<WB_Point> points;
WB_Triangulation2D triangulation;
WB_IndexedAABBTree2D tree;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  points=new ArrayList<WB_Point>();
  source=new WB_RandomDisk().setRadius(400).setOffset(width/2,height/2);
  for (int i=0; i<2000; i++) {
    points.add(source.nextPoint());
  }
  triangulation=WB_TriangulationFactory.triangulate2D(points);
  tree=new WB_IndexedAABBTree2D(triangulation, WB_CoordCollection.getCollection(points),1);
}

void draw() {
  background(55);
  noFill();
  strokeWeight(1.0);
  stroke(255,0,0,20);
  render.drawAABBTree2D(tree);
  strokeWeight(1.4);
  stroke(0);
  
  render.drawTriangulation2D(triangulation,points);
 
  
  int[] closest=tree.getClosestFace(new WB_Point(mouseX,mouseY));
  fill(255,0,0);
  render.drawTriangle2D(closest,points);
  
}
