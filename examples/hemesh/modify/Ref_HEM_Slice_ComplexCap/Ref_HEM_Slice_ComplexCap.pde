import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh,wire;
WB_Render render;
WB_Plane P;
HEM_Slice modifier;
boolean renderWire;
void setup() {
  fullScreen(P3D);
  smooth(8);
  createMesh();
  modifier=new HEM_Slice();
  P=new WB_Plane(0,0, 0, 0,0.25, 1); 
  modifier.setPlane(P);
  mesh.modify(modifier);
  mesh.validate();
  render=new WB_Render(this);
  renderWire=true;
}

void draw() {
  background(0);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(map(mouseY, 0, height, PI, -PI));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  stroke(0);
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("caps"));
  noFill();
  strokeWeight(1);
  stroke(255, 0, 0,150);
  if(renderWire) render.drawEdges(wire);
  render.drawPlaneHatch(P, 600,80);
}

void createMesh() {
  HEC_Torus creator=new HEC_Torus(120, 300, 24, 64).setTwist(3);
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_Crocodile().setDistance(100));
  creator=new HEC_Torus(100, 300, 24, 64).setTwist(-3);
  HE_Mesh inner=new HE_Mesh(creator);
  HET_MeshOp.flipFaces(inner);
  mesh.add(inner);
  
  creator=new HEC_Torus(80, 300, 24, 64).setTwist(3);
  inner=new HE_Mesh(creator);
  mesh.add(inner);
 
  creator=new HEC_Torus(60, 300, 24, 64).setTwist(-3);
  inner=new HE_Mesh(creator);
  HET_MeshOp.flipFaces(inner);
  
  mesh.add(inner);
  
   creator=new HEC_Torus(40, 300, 24, 64).setTwist(3);
  inner=new HE_Mesh(creator);
  mesh.add(inner);
  
  mesh.smooth();
  wire=mesh.get();
}

void mousePressed(){
 renderWire=!renderWire; 
}
