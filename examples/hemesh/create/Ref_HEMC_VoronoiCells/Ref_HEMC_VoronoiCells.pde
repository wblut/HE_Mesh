import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;

HE_Mesh container;
HE_MeshCollection cells;

WB_Render render;
boolean ASYNC;// create meshes in a separate thread?
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  // Brute force approach to 3D Voronoi inside a container mesh. Very inefficient, useful
  // for prototyping tens to hundreds of points, painfully slow
  // for more...

  //container=new HE_Mesh(new HEC_Geodesic().setB(4).setC(4).setRadius(360));

HEC_Cube creator=new HEC_Cube();
  creator.setEdge(400); 
   container = new HE_Mesh(creator);
  HE_FaceIterator fitr=container.fItr();
  while (fitr.hasNext()) {
    fitr.next().setColor(color(0,0,255));
  }
  //make a skin
//  container.modify(new HEM_Shell().setThickness(100));  

  
   int B, C;
  // make a icosahedron
  B=5;
  C=6;
  HEC_Geodesic pointMaker=new HEC_Geodesic();
  pointMaker.setRadius(20);
  pointMaker.setB(B+1);
  pointMaker.setC(C);
  
  HE_Mesh thisMesh = new HE_Mesh(pointMaker); 
WB_CoordCollection thesePoints = thisMesh.getPoints();
  // generate voronoi cells
  HEMC_VoronoiCells multiCreator=new HEMC_VoronoiCells();
  multiCreator.setPoints(thesePoints.toList());
  multiCreator.setContainer(container);// cutoff mesh for the voronoi cells, complex meshes increase the generation time
  multiCreator.setOffset(10);// offset of the bisector cutting planes, sides of the voronoi cells will be separated by twice this distance
  multiCreator.setSurface(false);// is container mesh a volume (false) or a surface (true)
  ASYNC=true;

  if (ASYNC) {
    cells=new HE_MeshCollection();
    cells.createThreaded(multiCreator);
  } else {
    cells=multiCreator.create();
  }
  render=new WB_Render(this);
}

void draw() {
  if (ASYNC) cells.update();
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
