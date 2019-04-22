import wblut.geom.*;
import wblut.processing.*;
import java.util.*;

WB_RandomPoint source;
WB_Render3D render;
WB_KDTreeInteger2D<WB_Point> tree;
List<WB_AABB2D> regions;
List<WB_AABB2D> bounds;
List<WB_Point> points;
WB_KDTreeInteger2D.WB_KDEntryInteger[] pointsInRange;

void setup() {
  size(800, 800, P3D);
  source=new WB_RandomDisk().setRadius(300);
  render=new WB_Render3D(this);
  tree= new WB_KDTreeInteger2D<WB_Point>(16);
  points=new ArrayList<WB_Point>();
  for (int i=0; i<1000; i++) {
    points.add(source.nextPoint());
    tree.add(points.get(i), i);
  }
  regions=tree.getLeafRegions();
  bounds=tree.getLeafBounds();
}


void draw() {
  background(55);
  translate(width/2, height/2, 0);
  stroke(0);
  strokeWeight(1.0);
  noFill();
  for(WB_AABB2D aabb: regions){
     render.drawAABB2D(aabb);
  }
  stroke(255,0,0);
  for(WB_AABB2D aabb: bounds){
     render.drawAABB2D(aabb);
  }
  fill(255,0,0);
  render.drawPoint(points,3);
  
  WB_Point c=new WB_Point(mouseX-width/2,mouseY-height/2);
  float radius=80.0;
  pointsInRange=tree.getRange(c,50.0);
  noFill();
  stroke(0,0,255);
  strokeWeight(1.0);
  ellipse(c.xf(),c.yf(),100.0,100.0);
  for(int i=0;i<pointsInRange.length;i++){
    render.drawPoint(pointsInRange[i].coord,4);
  }
}