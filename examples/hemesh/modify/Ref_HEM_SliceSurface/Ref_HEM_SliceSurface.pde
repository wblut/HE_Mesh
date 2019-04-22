import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane P;
HEM_SliceSurface modifier;
void setup() {
  size(1000, 1000, P3D);
  createMesh();

  modifier=new HEM_SliceSurface();

  P=new WB_Plane(0, 0, 20, 1, 1, 1); 
  modifier.setPlane(P);// Cut plane 
  //you can also pass directly as origin and normal:  modifier.setPlane(0,0,-200,0,0,1)
  modifier.setOffset(0);// shift cut plane along normal
  mesh.modify(modifier);


  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(0.25*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  fill(255, 0, 0);
  noStroke();
  render.drawFaces(mesh.getSelection("cuts"));
  noFill();
  stroke(0);
  render.drawEdges(mesh);
  strokeWeight(4);
  stroke(0, 0, 255);
  render.drawEdges(mesh.getSelection("edges"));
  strokeWeight(1);
  stroke(255, 0, 0);
  render.drawPlane(P, 300);
}


void createMesh() {
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(32).setSteps(16).setRadius(50).setHeight(400).setCenter(0, 0, 0);
  mesh=new HE_Mesh(creator);
}