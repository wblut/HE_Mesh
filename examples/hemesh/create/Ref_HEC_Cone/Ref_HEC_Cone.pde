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
  HEC_Cone creator=new HEC_Cone();
  creator.setRadius(200).setHeight(400); 
  creator.setFacets(7).setSteps(5);
  creator.setCap(true);
  creator.setReverse(false);// flip cone?
  //Default axis of the cone is (0,1,0). To change this use the HEC_Creator method setZAxis(..).
  creator.setZAxis(0,0,1);
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
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

