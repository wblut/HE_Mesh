
import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

WB_Point[] points;
int numpoints;
HE_Mesh container;
HE_MeshCollection cells;
int numcells;
HE_Mesh fusedcells;
HE_Selection sel;
WB_Render3D render;

void setup() {
  fullScreen(P3D);
  smooth(8);
  createContainer();
  numpoints=1000;
  createMesh();
  render=new WB_Render(this);
  noCursor();
}

void createContainer() {
  container=new HE_Mesh(new HEC_Geodesic().setB(2).setC(0).setRadius(420)); 
 // container.modify(new HEM_Extrude().setDistance(150).setChamfer(0.5));
  HE_FaceIterator fitr=container.fItr();
  while (fitr.hasNext()) {
    fitr.next().setColor(color(0, 200, 50));
  }
}

void createMesh() {  
  // generate points
  points=new WB_Point[numpoints];
  WB_RandomPoint generator=new WB_RandomInSphere().setRadius(400);
  for (int i=0; i<numpoints; i++) {
    points[i]=generator.nextPoint();
  }

  // generate voronoi cells
  HEMC_VoronoiCells multiCreator=new HEMC_VoronoiCells().setPoints(points).setContainer(container).setOffset(0);
  cells=multiCreator.create();

  //color the cells
  int counter=0;
  HE_MeshIterator mItr=cells.mItr();
  HE_Mesh m;
  while (mItr.hasNext()) {
    m= mItr.next();
    m.setFaceColorWithOtherInternalLabel(color(255-2*counter, 220, 2*counter), -1);
    counter++;
  }

  numcells=cells.size();
  boolean[] isCellOn=new boolean[numcells];
  for (int i=0; i<numcells; i++) {
    isCellOn[i]=isActive(i);
  }

  //build new mesh from active cells

  HEC_FromVoronoiCells creator=new HEC_FromVoronoiCells().setCells(cells).setActive(isCellOn);
  fusedcells=new HE_Mesh(creator);
  HET_MeshOp.fuseCoplanarFaces(fusedcells,0.1);
 sel=HET_Fixer.selectVerticesWithDegree(2,fusedcells);
  fusedcells.validate();
/*
  //clean-up mesh by joining fragmented faces back together. This does not always work
  HE_Mesh tmp=fusedcells.get();

  try {
    fusedcells.fuseCoplanarFaces(0.1);
  }
  catch(final Exception ex) {
    //oops HE_Mesh messed up, retreat!
    ex.printStackTrace();
    fusedcells=tmp;
  } 
fusedcells.triangulate(fusedcells.selectFacesWithOtherInternalLabel("inner", -1));
fusedcells.getSelection("inner").subdivide(new HES_CatmullClark(),2);
*/
  
}

boolean isActive(int i){
  WB_Coord point=points[i];
  
  float r2=(float)WB_Point.getSqLength(point);
  float zcutoff=50+0.0025*r2;
  
  return abs(point.xf())<50 || (abs(point.xf())>150 &&abs(point.xf())<250) || (abs(point.xf())>350 );
  
}

void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI+radians(0.1*frameCount));
  rotateX(mouseY*1.0f/height*TWO_PI);
  strokeWeight(1);
  stroke(255);
  render.drawEdges(fusedcells);
  noStroke();
  fill(55);
  render.drawFaces(fusedcells);
  fill(255,0,0);
  render.drawVertices(sel,5);
}