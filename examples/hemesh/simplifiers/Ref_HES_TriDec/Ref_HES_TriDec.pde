import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh, oldmesh;

WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  render=new WB_Render(this);
  textAlign(CENTER);
  textSize(16);
}

void draw() {
  background(120);

  translate(width/2, height/2);
  fill(255);
  noStroke();
  text("Click to reduce number of faces by 10%.", 0, 470);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(255, 0, 0, 100);
  render.drawEdges(oldmesh);
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_Creator creator=new HEC_Beethoven().setScale(10).setZAxis(0,-1,0);
  mesh=new HE_Mesh(creator);
  oldmesh=mesh.get();
}

void mousePressed() {
  mesh.simplify(new HES_TriDec().setGoal(0.9));
}