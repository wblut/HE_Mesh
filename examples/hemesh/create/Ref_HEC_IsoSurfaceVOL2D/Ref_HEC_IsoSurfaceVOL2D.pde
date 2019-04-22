import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

WB_Render render;
HE_Mesh mesh;

void setup() {
  fullScreen(P3D);
  smooth(8);
  float[][] values=new float[81][51];
  for (int i = 0; i < 81; i++) {
    for (int j = 0; j < 51; j++) {
      values[i][j]=2.5*cos(4.0*TWO_PI*0.0125*i)*cos(2.0*TWO_PI*0.02*j);
    }
  }
  HEC_IsoSurfaceVOL2D creator=new HEC_IsoSurfaceVOL2D();
  creator.setResolution(80, 50);
  creator.setSize(12, 12);
  creator.setValues(values);
  creator.setIsolevel(-0.2, 0.2);
  creator.setZFactor(25.0); 
  mesh=creator.create();
  mesh.modify(new HEM_Shell().setThickness(12.5));
  mesh.stats();
  mesh.modify(new HEM_HideEdges().setThreshold(0.7));

  render=new WB_Render(this);
}

void draw() {
  background(25);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  stroke(0);
  strokeWeight(2.0);
  render.drawEdges(mesh);
}