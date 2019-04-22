import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh container;
HE_MeshCollection cells;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);
 
  container=new HE_Mesh(new HEC_Torus(120,320,8,32));


  HEMC_VoronoiCells multiCreator=new HEMC_VoronoiCells();
  multiCreator.setMesh(container,true);
 
  multiCreator.setOffset(8);
  cells=new HE_MeshCollection();
  cells.createThreaded(multiCreator);
  cells.modifyThreaded(new HEM_HideEdges());
  render=new WB_Render(this);
  noCursor();
}

void draw() {
  cells.update();
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  drawFaces();
  drawEdges();
}

void drawEdges() {
  stroke(0);
  render.drawEdges(cells);
}

void drawFaces() {
  noStroke();
  fill(255);
  render.drawFaces(cells);
}