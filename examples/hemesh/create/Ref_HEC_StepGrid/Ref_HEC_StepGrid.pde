import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.opengl.*;

HE_Mesh mesh, mesh2, mesh3;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);

  float[][] values=new float[40][20];
  for (int j = 0; j < 20; j++) {
    for (int i = 0; i <40; i++) {
      values[i][j]=400*noise(0.12*i, 0.12*j);
    }
  }

  HEC_Grid creator=new HEC_Grid();
  creator.setStepped(true);
  creator.setU(40);// number of cells in U direction
  creator.setV(20);// number of cells in V direction
  creator.setUSize(1200);// size of grid in U direction
  creator.setVSize(600);// size of grid in V direction
  creator.setValues(values);// displacement of grid cells 
  creator.setBase(true);
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_HideEdges());

  mesh.stats();
  mesh.moveSelf(-600, -300, -150);

  values=new float[41][21];
  for (int j = 0; j < 21; j++) {
    for (int i = 0; i <41; i++) {
      values[i][j]=400*noise(0.12*(i-0.5), 0.12*(j-0.5));
    }
  }

  creator=new HEC_Grid();

  creator.setU(40);// number of cells in U direction
  creator.setV(20);// number of cells in V direction
  creator.setUSize(1200);// size of grid in U direction
  creator.setVSize(600);// size of grid in V direction
  creator.setValues(values);// displacement of grid cells  
  creator.setBase(true);
  mesh2=new HE_Mesh(creator);
  mesh2.moveSelf(-600, -300, -150);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  scale(0.75);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  fill(255);
  render.drawFaces(mesh);

  stroke(0);
  strokeWeight(1);
  render.drawEdges(mesh);
  stroke(255, 0, 0);
  strokeWeight(1.4);
  render.drawEdges(mesh2);
}
