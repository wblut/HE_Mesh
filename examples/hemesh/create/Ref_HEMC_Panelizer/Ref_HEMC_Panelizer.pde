import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh hull;
HE_MeshCollection panels;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);
  
  //create a hull mesh
  hull=new HE_Mesh(new HEC_Beethoven().setScale(11));
  //panelize the hull
  HEMC_Panelizer multiCreator=new HEMC_Panelizer();
  multiCreator.setMesh(hull);
  multiCreator.setThickness(5);
  
  panels=new HE_MeshCollection();
  panels.createThreaded(multiCreator);

  render=new WB_Render(this);
}

void draw() 
{
  panels.update();
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255,0,0);
  noStroke();
  render.drawFaces(hull);
  drawFaces();
  drawEdges();
}

void drawEdges(){
  stroke(0);
  render.drawEdges(panels);
}

void drawFaces(){
  noStroke();
  fill(255);
  render.drawFaces(panels);
}
/*
class Gradient implements WB_ScalarParameter {
  public double evaluate(double... x) {
    return  max(0,map((float)x[2],-300,300,0,50));
  }
}*/