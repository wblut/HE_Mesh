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
WB_Triangulation3D triangulation;
WB_IndexedAABBTree3D tree;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  points=new ArrayList<WB_Point>();
  source=new WB_RandomInShell().setRadius(200,300);
  for (int i=0; i<100; i++) {
    points.add(source.nextPoint());
  }
  triangulation=WB_TriangulationFactory.triangulate3D(points);
  tree=new WB_IndexedAABBTree3D(triangulation, WB_CoordCollection.getCollection(points),1);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  lights();
  rotateX(map(mouseY,0,height,PI,-PI));
  rotateZ(map(mouseX,0,width,-PI,PI));
  noFill();
  strokeWeight(1.0);
  stroke(255,0,0,20);
  render.drawAABBTree(tree);
  strokeWeight(1.4);
  stroke(0);
  render.drawTriangulation(triangulation,points);
  int[] closest=tree.getClosestFace(new WB_Point(mouseX-width/2,mouseY-height/2));
  fill(255,0,0);
  render.drawTetrahedron(closest,points);
  
}
