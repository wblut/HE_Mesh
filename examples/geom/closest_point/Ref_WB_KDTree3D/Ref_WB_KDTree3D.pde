import wblut.geom.*;
import wblut.processing.*;
import java.util.*;

WB_RandomPoint source;
WB_Render3D render;
WB_KDTreeInteger3D<WB_Point> tree;
List<WB_AABB> leafs;
List<WB_Point> points;
void setup() {
  size(800, 800, P3D);
  source=new WB_RandomOnSphere().setRadius(250);
  render=new WB_Render3D(this);
  tree= new WB_KDTreeInteger3D<WB_Point>(8);
  points=new ArrayList<WB_Point>();
  for (int i=0; i<1000; i++) {
    points.add(source.nextPoint());
    tree.add(points.get(i), i);
  }
  leafs=tree.getLeafRegions();
}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  for(WB_AABB aabb: leafs){
     render.drawAABB(aabb);
  }
  fill(255,0,0);
  render.drawPoint(points,5);
}
