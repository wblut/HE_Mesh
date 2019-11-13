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
  HEC_ChamferBox creator=new HEC_ChamferBox();
  creator.setWidth(560).setHeight(90).setDepth(300); 
  creator.setChamfer(20,20,20);
 
  creator.setWidthSegments(5).setHeightSegments(1).setDepthSegments(6);
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
  fill(255);
  render.drawFaces(mesh);
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("edges"));
  fill(0,255,0);
  render.drawFaces(mesh.getSelection("corners"));

}
