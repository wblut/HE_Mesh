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
  HEC_Torus creator=new HEC_Torus();
  creator.setRadius(40,200); 
  creator.setTubeFacets(8);
  creator.setTorusFacets(64);
  creator.setTwist(3);//twist the torus a given number of facets
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,PI,-PI));
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}