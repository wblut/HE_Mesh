import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
int B, C;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  B=0;
  C=0;
  HEC_Geodesic creator=new HEC_Geodesic();
  creator.setRadius(200); 
  // http://stackoverflow.com/questions/3031875/math-for-a-geodesic-sphere
  // N=B+C=number of divisions
  // B=N and C=0 or B=0 and C=N: class I
  // B=C=N/2: class II
  // Other: class III 
  creator.setB(B+1);
  creator.setC(C);

  // class I, II and III: TETRAHEDRON,OCTAHEDRON,ICOSAHEDRON
  // class II only: CUBE, DODECAHEDRON
  creator.setType(WB_Geodesic.Type.ICOSAHEDRON);
  mesh=new HE_Mesh(creator); 
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  text("B="+(B+1)+" C="+C,50,950);
    text("click left half to increment B, click right half to increment C.",50,975);
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

}

void mousePressed() {
  if (mouseX<width/2) B=(B+1)%10;
  if (mouseX>width/2) C=(C+1)%11;

  HEC_Geodesic creator=new HEC_Geodesic();
  creator.setRadius(200); 
  creator.setB(B+1);
  creator.setC(C);
  creator.setType(WB_Geodesic.Type.ICOSAHEDRON);
  mesh=new HE_Mesh(creator);
}