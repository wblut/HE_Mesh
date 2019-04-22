import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
HEC_Johnson creator;
WB_AABB box;

int currentType;
void setup() {
  size(1000, 1000, P3D);
  smooth(8); 
  box=new WB_AABB(-250,-250,-250,250,250,250);
  creator=new HEC_Johnson();
  creator.setEdge(100); // edge length of the polyhedron
  creator.setType(currentType+1);// type of archimedean solid, 1 to 92
  mesh=new HE_Mesh(creator); 
  mesh.fitInAABBConstrained(box);
  render=new WB_Render(this);
  textAlign(CENTER);
  textSize(16);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  fill(255);
  noStroke();
  text("Current Johnson solid: "+creator.getName()+" ("+(currentType+1)+")",0,350);
  text("Left-click for next.",0,370);
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

void mousePressed(){
  currentType=(currentType+1)%92;
  creator=new HEC_Johnson();
  creator.setEdge(100); 
  creator.setType(currentType+1);
  mesh=new HE_Mesh(creator); 
  mesh.fitInAABBConstrained(box);
}