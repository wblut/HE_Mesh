import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

import processing.opengl.*;

HE_Mesh mesh;
WB_Render render;
PImage img;
int MODE=0;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  textureMode(NORMAL);
  create();
  img=loadImage("sky.png");//texture.jpg");
  render=new WB_Render(this);
}

void create() {
  switch(MODE) {
  case 0:
    float[][] values=new float[21][21];
    for (int j = 0; j < 21; j++) {
      for (int i = 0; i < 21; i++) {
        values[i][j]=200*noise(0.35*i, 0.35*j);
      }
    }
    HEC_Grid creator0=new HEC_Grid();
    creator0.setU(20);
    creator0.setV(20);
    creator0.setUSize(600);
    creator0.setVSize(600);
    creator0.setWValues(values);
    mesh=new HE_Mesh(creator0);
    break;
  case 1:
    HEC_Cylinder creator1=new HEC_Cylinder();
    creator1.setRadius(150, 150); 
    creator1.setHeight(400);
    creator1.setFacets(14).setSteps(1);
    creator1.setCap(true, true);
    mesh=new HE_Mesh(creator1); 
    HET_MeshOp.flipFaces(mesh);
    break;
  case 2:
    HEC_Cone creator2=new HEC_Cone();
    creator2.setRadius(150); 
    creator2.setHeight(400);
    creator2.setFacets(14).setSteps(5);
    creator2.setCap(true);
    mesh=new HE_Mesh(creator2); 
    break;
  case 3:
    mesh=new HE_Mesh(new HEC_Torus(80, 200, 6, 12).setTwist(4)); 
    break;
  case 4:
    mesh=new HE_Mesh(new HEC_Hemisphere().setCap(false).setRadius(200).setUFacets(16).setVFacets(8));
    mesh.modify(new HEM_Shell().setThickness(10));
    break;
  case 5:
    mesh=new HE_Mesh(new HEC_Sphere().setRadius(2000).setUFacets(64).setVFacets(32));
    break;
  case 6:
    HEC_SuperDuper creator6=new HEC_SuperDuper();
    creator6.setU(64);
    creator6.setV(8);
    creator6.setUWrap(true);
    creator6.setVWrap(false); 
    creator6.setRadius(50);
    creator6.setDonutParameters(0, 10, 10, 10, 5, 6, 12, 12, 3, 1);
    mesh=new HE_Mesh(creator6); 
    break;
  }
}

void draw() {
  background(55);
  translate(width/2, height/2);
  rotateX(map(mouseY, 0, height, -PI, 0));
  rotateZ(map(mouseX, 0, width, -PI, PI));
  noStroke();
  render.drawFaces(mesh, img);
  stroke(0);
  render.drawEdges(mesh);
}

void mousePressed() {
  MODE=(MODE+1)%7;
  create();
}