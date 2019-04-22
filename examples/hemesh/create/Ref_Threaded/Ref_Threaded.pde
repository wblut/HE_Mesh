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

  float[][] values=new float[31][31];
  for (int j = 0; j < 31; j++) {
    for (int i = 0; i < 31; i++) {
      values[i][j]=200*noise(0.35*i, 0.35*j);
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setU(30);// number of cells in U direction
  creator.setV(30);// number of cells in V direction
  creator.setUSize(800);// size of grid in U direction
  creator.setVSize(800);// size of grid in V direction
 
  mesh=new HE_Mesh();
  mesh.createThreaded(creator);
  mesh.modifyThreaded(new HEM_Lattice().setWidth(5).setDepth(5));
  mesh.subdivideThreaded(new HES_CatmullClark());
  mesh.subdivideThreaded(new HES_CatmullClark());
  render=new WB_Render(this);
}

void draw() {
 mesh.update();
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