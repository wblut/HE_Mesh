import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
HEC_Archimedes creator;
WB_AABB box;

int currentType;
void setup() {
  size(1000, 1000, P3D);
  smooth(8); 
  box=new WB_AABB(-250,-250,-250,250,250,250);
  createMesh();
  render=new WB_Render(this);
  textAlign(CENTER);
  textSize(16);
}

void createMesh(){
  creator=new HEC_Archimedes();
  creator.setEdge(100); // edge length of the polyhedron
  creator.setType(currentType+1);// type of archimedean solid, 1 to 13
  mesh=new HE_Mesh(creator); 
  mesh.fitInAABBConstrained(box);
  mesh.modify(new HEM_CenterSplitHole().setChamfer(40).setRelative(false));
  mesh.subdivide(new HES_CatmullClark(),2);
  mesh.modify(new HEM_Shell().setThickness(40));
  
  
}

void draw() {
  background(55);
  ortho();
  translate(width/2, height/2, 0);
  fill(255);
  noStroke();
  text("Current Archimedean solid: "+creator.getName()+" ("+(currentType+1)+")",0,350);
  text("Left-click for next.",0,370);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  rotateY(map(mouseX, 0, width, -PI/2, PI/2));
  rotateX(map(mouseY, 0, height, PI/2, -PI/2));
  noStroke();
  fill(255);
  render.drawFaces(mesh.getSelection("inner"));
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  
}

void mousePressed(){
  currentType=(currentType+1)%13;
  createMesh();
}