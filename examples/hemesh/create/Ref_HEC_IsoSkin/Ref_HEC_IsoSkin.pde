import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
HE_Mesh substrate;
WB_Render render;

PShape scaffold;
void setup() {
  fullScreen(P3D);
  smooth(8);
  substrate=new HE_Mesh(new HEC_Geodesic().setRadius(200));
 // substrate.modify(new HEM_Extrude().setDistance(200).setChamfer(0.5));
  substrate.smooth();
  int n=substrate.getNumberOfVertices();
  //substrate.smooth(3);
  HEC_IsoSkin isoskin=new HEC_IsoSkin();
  int numberOfLayers=10;
  isoskin.setSubstrate(substrate, numberOfLayers, 10, 10.0);

  double[][] values=new double[numberOfLayers+1][substrate.getNumberOfVertices()];

  for (int i=1; i<numberOfLayers; i++) {
    WB_Coord p;
    HE_Vertex v;
    for (int j=0; j< substrate.getNumberOfVertices(); j++) {
      v=substrate.getVertexWithIndex(j);
      p=v.addMul(i*20.0, v.getVertexNormal());
      values[i][j]=(i>=numberOfLayers-2)?1.0:noise(100+0.035*p.xf(), 100+0.035*p.yf(), 100+0.035*p.zf());
    }
  }
  isoskin.setValues(values);
  isoskin.setIsolevel(0.5);
  mesh=new HE_Mesh(isoskin);
  mesh.smooth();
  mesh.modify(new HEM_Slice().setPlane(0, 0, 0, 1, 0, 0));
  scaffold=WB_PShapeFactory.createSubstratePShape(isoskin, this) ;
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  scale(1.4);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(map(mouseY, 0, height, PI, -PI));
  noStroke();
  fill(255);
  // render.drawFaces(substrate);
  render.drawFaces(mesh);
  fill(255,0,0);
  render.drawFaces(mesh.getSelection("caps"));
  noFill();
  strokeWeight(1);
  stroke(0,0,120);
  render.drawEdges(substrate);
   stroke(255,0,0,50);
  //shape(scaffold);
}