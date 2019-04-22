import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  HEC_Icosahedron creator=new HEC_Icosahedron();
  creator.setEdge(200); 
  //alternatively 
  //creator.setRadius(200);
  //creator.setInnerRadius(200);// radius of sphere inscribed in cube
  //creator.setOuterRadius(200);// radius of sphere circumscribing cube
  //creator.setMidRadius(200);// radius of sphere tangential to edges
  mesh=new HE_Mesh(creator); 

  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}

