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

  float[][] values=new float[11][21];
  for (int j = 0; j < 21; j++) {
    for (int i = 0; i < 11; i++) {
      values[i][j]=200*noise(0.35*i, 0.35*j);
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setU(10);// number of cells in U direction
  creator.setV(20);// number of cells in V direction
  creator.setUSize(300);// size of grid in U direction
  creator.setVSize(600);// size of grid in V direction
  creator.setValues(values);// displacement of grid points (W value)
  // alternatively this can be left out (flat grid). values can also be double[][]
  // or and implementation of the WB_ScalarParameter interface.
  creator.setBase(true);
  creator.setBaseValue(0);
  mesh=new HE_Mesh(creator);

  mesh.moveToSelf(0,0,0);
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
  fill(255);
  render.drawFaces(mesh);
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("grid"));
  stroke(0);
  render.drawEdges(mesh);
}
