import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
float N;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  N=10;
  HEC_FibonacciSphere creator=new HEC_FibonacciSphere();
  creator.setRadius(350); 
  creator.setN(round(N));
  mesh=new HE_Mesh(creator); 
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  text("N="+round(N), 50, 950);
  text("click to increment N.", 50, 975);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX, 0, width, -PI/2, PI/2));
  rotateX(map(mouseY, 0, height, PI/2, -PI/2));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  strokeWeight(4);
  scale(1.01);
  stroke(255, 0, 0);
  for (int i=0; i<mesh.getNumberOfVertices(); i++) {
    render.drawPoint(mesh.getVertex(i));
  }
}

void mousePressed() {
  N*=sqrt(2.0);
  HEC_FibonacciSphere creator=new HEC_FibonacciSphere();
  creator.setRadius(350); 
  creator.setN(round(N));
  mesh=new HE_Mesh(creator);
}