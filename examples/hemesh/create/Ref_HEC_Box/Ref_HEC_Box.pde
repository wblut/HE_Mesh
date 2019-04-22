import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8); 
  HEC_Box creator=new HEC_Box();
  creator.setWidth(560).setHeight(90).setDepth(300); 
  //alternatively a box can be created from any WB_AABB, axis-aligned box
  //creator.setFromAABB(new WB_AABB(-200,-300,-150,100,200,50));
  creator.setWidthSegments(14).setHeightSegments(3).setDepthSegments(20);
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2,0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}