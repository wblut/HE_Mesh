import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

WB_Render render;
List<WB_Segment> segments;
void setup() {
  fullScreen(P3D);
  smooth(8);
  
  WB_IsoSurface2D creator=new WB_IsoSurface2D();
  creator.setSize(2.5, 2.5);
  PImage image=loadImage("flower.png");
  creator.setValues(image,this,256,256);
  creator.setIsolevel(50);
  creator.setBoundary(0);// value outside grid
  // use creator.clearBoundary() to rest boundary values to "no value".
  
  //Gamma controls level of grid snap, 0.0-0.5. Can improve the 
  //quality of the segments, but can give small changes in topology and less smooth contours.
  //For 2D, no snap, gamma=0.0 is a good value
  creator.setGamma(0.0); 
  segments=creator.getSegments();
  render=new WB_Render(this);
}

void draw() {
  background(25);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(255);
  render.drawSegment(segments);
}