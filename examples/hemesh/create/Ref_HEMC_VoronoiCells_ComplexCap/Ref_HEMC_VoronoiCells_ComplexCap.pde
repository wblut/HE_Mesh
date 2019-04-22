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
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Torus creator=new HEC_Torus(120, 300, 6, 16);
  container=new HE_Mesh(creator);
  creator=new HEC_Torus(60, 300, 6, 16);
  HE_Mesh inner=new HE_Mesh(creator);
  inner.modify(new HEM_Extrude().setDistance(32).setChamfer(0.8));
  HET_MeshOp.flipFaces(inner);
  container.add(inner);
  container.smooth();
  HE_FaceIterator fitr=container.fItr();
  while (fitr.hasNext()) {
    fitr.next().setColor(color(0, 200, 50));
  }

  numpoints=100;
  points=new float[numpoints][3];
  for (int i=0; i<numpoints; i++) {
    points[i][0]=random(-250, 250);
    points[i][1]=random(-250, 250);
    points[i][2]=random(-100, 100);
  }

  HEMC_VoronoiCells multiCreator=new HEMC_VoronoiCells();
  multiCreator.setPoints(points);
  multiCreator.setContainer(container);
  multiCreator.setOffset(10);

  cells=multiCreator.create();

  HE_MeshIterator mItr=cells.mItr();
  HE_Mesh mesh;
  while (mItr.hasNext()) {
    mesh=mItr.next();
    mesh.modify(new HEM_HideEdges());
  }


  render=new WB_Render(this);
}

void draw() {
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
  HE_MeshIterator mItr=cells.mItr();
  HE_Mesh mesh;
  HE_FaceIterator fItr;
  HE_Face f;
  while (mItr.hasNext()) {
    mesh=mItr.next();
    fItr=mesh.fItr();
    while (fItr.hasNext()) {
      f=fItr.next();
      if (f.getInternalLabel()==-1) {
        fill(WB_Color.spectralColorZucconi6(mesh.getInternalLabel()*2.6+420));
      } else {
        fill(WB_Color.spectralColorZucconi6(f.getInternalLabel()*2.6+420));
      }
      render.drawFace(f);
    }
  }
}