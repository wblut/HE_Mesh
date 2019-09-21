import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.opengl.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);

  float[][] values=new float[40][40];
  for (int j = 0; j < 40; j++) {
    for (int i = 0; i <40; i++) {
      values[i][j]=(20*noise(0.035*i, 0.035*j))*40;
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setStepped(true);
  creator.setU(40);// number of cells in U direction
  creator.setV(40);// number of cells in V direction
  creator.setUSize(1000);// size of grid in U direction
  creator.setVSize(1000);// size of grid in V direction
  creator.setValues(values);// displacement of grid cells  
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_HideEdges());
  mesh.stats();
  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
  stroke(255, 0, 0);
  render.drawBoundaryEdges(mesh);
  noFill();
  // render.drawVertices(mesh, 2);
}
