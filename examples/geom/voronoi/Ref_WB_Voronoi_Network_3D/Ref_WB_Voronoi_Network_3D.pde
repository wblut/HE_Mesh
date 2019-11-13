import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;

WB_Network network;

WB_Render render;

void setup() {
 fullScreen(P3D);
  smooth(16);
  render=new WB_Render(this);
  WB_RandomPoint rp=new WB_RandomBox().setSize(1000,1000,1000);

  network=WB_VoronoiFactory.getNetwork(rp.getPoints(1000));
  
}

void draw() {
  
  background(15);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(240,120);
  render.drawNetwork(network);
 
}
