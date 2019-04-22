import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
HEC_StellatedIcosahedron creator;
WB_AABB box;

int currentType;
void setup() {
  fullScreen(P3D);
  smooth(8); 
  box=new WB_AABB(-400,-400,-400,400,400,400);
  creator=new HEC_StellatedIcosahedron();
  creator.setRadius(100); 
  creator.setType(currentType+1);
  mesh=new HE_Mesh(creator); 
  mesh.fitInAABBConstrained(box);
  render=new WB_Render(this);
  textAlign(CENTER);
  textSize(16);
}

void draw() {
  background(55);
  
  translate(width/2, height/2, 0);
  fill(255);
  noStroke();
  text("Current stellated icosahedron: "+(currentType+1),0,450);
  text("Left-click for next.",0,470);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  rotateY(map(mouseX, 0, width, -PI/2, PI/2));
  rotateX(map(mouseY, 0, height, PI/2, -PI/2));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  render.drawBezierEdges(mesh);
  
}

void mousePressed(){
  currentType=(currentType+1)%59;
  creator=new HEC_StellatedIcosahedron();
  creator.setRadius(100); 
  creator.setType(currentType+1);
  mesh=new HE_Mesh(creator); 
  mesh.fitInAABBConstrained(box);
}

void keyPressed(){
  
 mesh.smooth(); 
 mesh.fitInAABBConstrained(box);
}