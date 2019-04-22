import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh,wire;
WB_Render render;
WB_Plane P;
HEM_Slice modifier;

void setup() {
  fullScreen(P3D);
  smooth(8);
  createMesh();
  modifier=new HEM_Slice();
  P=new WB_Plane(0,0, 0, 0,0.25, 1); 
  modifier.setPlane(P);
  mesh.modify(modifier);
  render=new WB_Render(this);
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
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("caps"));
  noFill();
  strokeWeight(1);
  stroke(255, 0, 0,128);
  render.drawEdges(wire);
  
  render.drawPlane(P, 500);
}

void createMesh() {
  HEC_Torus creator=new HEC_Torus(120, 300, 24, 64).setTwist(3);
  mesh=new HE_Mesh(creator);
  
  creator=new HEC_Torus(90, 300, 24, 64).setTwist(-3);
  HE_Mesh inner=new HE_Mesh(creator);
  HET_MeshOp.flipFaces(inner);
  mesh.add(inner);
  
  creator=new HEC_Torus(60, 300, 24, 64).setTwist(3);
  inner=new HE_Mesh(creator);
  mesh.add(inner);
 
  creator=new HEC_Torus(30, 300, 24, 64).setTwist(-3);
  inner=new HE_Mesh(creator);
  HET_MeshOp.flipFaces(inner);
  mesh.add(inner);
 // mesh.smooth(2);
  wire=mesh.get();
}
