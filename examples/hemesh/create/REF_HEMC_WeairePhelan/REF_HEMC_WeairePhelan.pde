import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_MeshCollection meshes;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEMC_WeairePhelan wp=new HEMC_WeairePhelan();
  wp.setOrigin(new WB_Point(-200, -200, -200));
  wp.setExtents(new WB_Vector(400, 400, 400));
  wp.setNumberOfUnits(2, 2, 2);
  wp.setScale(150, 150, 150);
  meshes=wp.create();
  render=new WB_Render(this);
}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  scale(1, -1, 1);
  rotateY(TWO_PI/width*mouseX-PI);
  rotateX(TWO_PI/height*mouseY-PI);
  noStroke();
  render.drawFaces(meshes);
  stroke(0);
  render.drawEdges(meshes);
}