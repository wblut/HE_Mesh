import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.opengl.*;

HE_MeshCollection meshes;
WB_Render render;
PImage img;
PShape shape1,shape2;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
   render=new WB_Render(this);
  textureMode(NORMAL);
  create();
  img=loadImage("sky.png");
  shape1=WB_PShapeFactory.createSmoothPShape(meshes.getMesh(0),img,this);
  shape2=WB_PShapeFactory.createSmoothPShape(meshes.getMesh(1),img,this); 
}

void create() {
  HE_Mesh mesh=new HE_Mesh(new HEC_Hemisphere().setCap(false).setRadius(300).setUFacets(16).setVFacets(8));
  mesh.modify(new HEM_Shell().setThickness(10));
  meshes=new HEMC_SplitMesh().setMesh(mesh).setPlane(new WB_Plane(0,0,0,1,1,1)).setOffset(50).create();
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateX(map(mouseY,0,height,-PI,0));
  rotateZ(map(mouseX,0,width,-PI,PI));
  //shape1.disableStyle();
  shape2.disableStyle();
  noStroke();
  shape(shape1);
  shape(shape2);
}