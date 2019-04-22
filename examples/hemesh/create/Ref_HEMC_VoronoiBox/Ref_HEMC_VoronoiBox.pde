import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

WB_Point[] points;
int numpoints;
WB_AABB container;
HE_MeshCollection cells;
HE_Mesh mesh;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);

  container=new WB_AABB(-300, -300, -300, 300, 300, 300);
  mesh=new HE_Mesh(new HEC_Box().setFromAABB(container));
  numpoints=1000;
  points=new WB_Point[numpoints];
  WB_RandomPoint rp=new WB_RandomOnSphere();
  for (int i=0; i<numpoints; i++) {
    points[i]=rp.nextPoint().mulSelf(random(299.9, 300));
  }

  HEMC_VoronoiBox multiCreator=new HEMC_VoronoiBox();
  multiCreator.setPoints(points);
  multiCreator.setContainer(container);
  multiCreator.setOffset(new Gradient());
  cells=multiCreator.create();
  /*
  HE_MeshIterator mItr=cells.mItr();
   HE_Mesh m;
   while(mItr.hasNext()){
   m=mItr.next();
   m.modify(new HEM_Slice().setPlane(50,50,50,-1,-1,-1));
   m.modify(new HEM_Slice().setPlane(-50,-50,-50,1,1,1));
   }
   float d=4.0/sqrt(3.0);
   mesh.modify(new HEM_Slice().setPlane(50+d,50+d,50+d,-1,-1,-1));
   mesh.modify(new HEM_Slice().setPlane(-50-d,-50-d,-50-d,1,1,1));
   */
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  //rotateX(mouseY*1.0f/height*TWO_PI);
  drawFaces();
  drawEdges();
  stroke(255, 0, 0);
  render.drawEdges(mesh);
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

class Gradient implements WB_ScalarParameter {
  public double evaluate(double... x) {
    return  map((float)x[1], 200, -250, 0, 6);
  }
}