import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


float[][] points;
int numpoints;
HE_Mesh container;
HE_MeshCollection cells;

WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);
  
  //generate points
  numpoints=500;
  points=new float[numpoints][3];
  for(int i=0;i<numpoints;i++) {
    points[i][0]=random(-300,300);
    points[i][1]=random(-300,300);
    points[i][2]=random(-300,300);
  }
  
  // generate voronoi cells
  HEMC_VoronoiSpheres multiCreator=new HEMC_VoronoiSpheres();
  multiCreator.setPoints(points);
  multiCreator.setLevel(3);// subdivision level for cell spheres
  multiCreator.setCutoff(150);// maximum radius of cell
  multiCreator.setApprox(true);// approximate cells by point expansion or precise cells by sphere slicing
  multiCreator.setNumTracers(1000);// random points per cell in approcimate mode
  multiCreator.setTraceStep(1);// step size for random points expansion
  multiCreator.setOffset(10);
  cells=new HE_MeshCollection();
  cells.createThreaded(multiCreator);
  
  render=new WB_Render(this);
}

void draw() {
  cells.update();
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  drawFaces();
  drawEdges();
}

void drawEdges(){
  stroke(0,50);
  render.drawEdges(cells);
}

void drawFaces(){
  
  noStroke();
  fill(255);
  render.drawFaces(cells);

}