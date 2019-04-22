import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
PImage img;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  textureMode(NORMAL);
  mesh=new HE_Mesh(new HEC_Grid(50,50, 500,500)); 
  img=loadImage("texture.jpg");
  render=new WB_Render(this);
  render.setVertexColorFromTexture(mesh,img);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFacesVC(mesh);
  stroke(0);
  render.drawEdges(mesh);
}