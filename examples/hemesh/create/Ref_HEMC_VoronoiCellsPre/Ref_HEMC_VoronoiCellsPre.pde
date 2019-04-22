import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

WB_Point[] points;
int numpoints;
HE_Mesh container;
HE_MeshCollection cells;

WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);

  container=new HE_Mesh(new HEC_Torus(120,320,16,64));
 HE_Mesh inner=new HE_Mesh(new HEC_Torus(60,320,16,64));

  numpoints=500;
  points=new WB_Point[numpoints];
  WB_RandomPoint rp=new WB_RandomBox().set(HE_MeshOp.getAABB(container));
  for (int i=0; i<numpoints; i++) {
    points[i]=rp.nextPoint();
  }


  HEMC_VoronoiCellsPre multiCreator=new HEMC_VoronoiCellsPre();
  multiCreator.setPoints(points);
  multiCreator.setContainer(container);
  multiCreator.setOffset(new Gradient());// offset of the bisector cutting planes, sides of the voronoi cells will be separated by twice this distance
  cells=new HE_MeshCollection();
  cells.createThreaded(multiCreator);
  cells.modifyThreaded(new HEM_HideEdges());
  render=new WB_Render(this);
  noCursor();
}

void draw() {
  cells.update();
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  drawFaces();
  drawEdges();
}

void drawEdges() {
  stroke(240);
  render.drawEdges(cells);
}

void drawFaces() {
  noStroke();
  fill(55);
  render.drawFaces(cells);
}

class Gradient implements WB_ScalarParameter {
  public double evaluate(double... x) {
    return  max(0,map((float)x[1],-400,400,0,24));
  }
}