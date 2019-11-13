import wblut.geom.*;
import wblut.processing.*;
import java.util.*;

WB_RandomPoint source;
WB_Render3D render;
WB_KDTreeInteger3D<WB_Point> tree;

List<WB_Point> points;
WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Point>[] inRange;
WB_Point center;
double range;
void setup() {
  size(800, 800, P3D);
  source=new WB_RandomInSphere().setRadius(250);
  render=new WB_Render3D(this);
  tree= new WB_KDTreeInteger3D<WB_Point>();
  points=new ArrayList<WB_Point>();
  for (int i=0; i<50000; i++) {
    points.add(source.nextPoint());
    tree.add(points.get(i), i);
  }
center=new WB_Point(0,0,0);
range=50.0;
}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(0);
  render.drawPoint(points);
  inRange=tree.getRange(center,range);
  
  stroke(255,0,0);
  for(WB_KDTreeInteger3D.WB_KDEntryInteger<WB_Point> entry:inRange){
   render.drawPoint(points.get(entry.value),2); 
  }
  
  tree= new WB_KDTreeInteger3D<WB_Point>();
  //Dynamic data requires rebuilding the tree...
  for (int i=0; i<50000; i++) {
    points.get(i).addSelf(random(-10.0,10.0),random(-10.0,10.0),random(-10.0,10.0));
    tree.add(points.get(i), i);
  }
println(frameRate);
}
