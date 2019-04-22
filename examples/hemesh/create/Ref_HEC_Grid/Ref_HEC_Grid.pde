import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


import processing.opengl.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);

  float[][] values=new float[11][11];
  for (int j = 0; j < 11; j++) {
    for (int i = 0; i < 11; i++) {
      values[i][j]=200*noise(0.35*i, 0.35*j);
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setU(10);// number of cells in U direction
  creator.setV(10);// number of cells in V direction
  creator.setUSize(300);// size of grid in U direction
  creator.setVSize(500);// size of grid in V direction
  creator.setWValues(values);// displacement of grid points (W value)
  // alternatively this can be left out (flat grid). values can also be double[][]
  // or and implementation of the WB_Function2D<Double> interface.
  mesh=new HE_Mesh(creator);
  mesh.stats();
  render=new WB_Render(this);
}

void draw() {
  background(120);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}