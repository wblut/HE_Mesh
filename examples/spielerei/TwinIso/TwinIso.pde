import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

import processing.opengl.*;

HE_Mesh mesh;
HE_Mesh invMesh;
WB_Render render;
int res;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
res=20;
  float[][][] values=new float[res+1][res+1][res+1];
  for (int i = 0; i < res+1; i++) {
    for (int j = 0; j < res+1; j++) {
      for (int k = 0; k < res+1; k++) {
        values[i][j][k]=2.1*noise(0.35*i, 0.35*j, 0.35*k);
      }
    }
  }

  HEC_IsoSurface creator=new HEC_IsoSurface();
  creator.setResolution(res,res, res);// number of cells in x,y,z direction
  creator.setSize(400.0/res, 400.0/res, 400.0/res);// cell size
  creator.setValues(values);// values corresponding to the grid points
  // values can also be double[][][]
  creator.setIsolevel(1);// isolevel to mesh
  creator.setInvert(false);// invert mesh
 creator.setBoundary(100);// value of isoFunction outside grid
  // use creator.clearBoundary() to rest boundary values to "no value".
  // A boundary value of "no value" results in an open mesh

  mesh=new HE_Mesh(creator);
 // mesh.modify(new HEM_Smooth().setIterations(10).setAutoRescale(true));
  creator.setInvert(true);

  invMesh=new HE_Mesh(creator);
   invMesh.modify(new HEM_Smooth().setIterations(10).setAutoRescale(true));
  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
   fill(255,0,0);
  render.drawFaces(invMesh);
  stroke(0);
  render.drawEdges(mesh);
  stroke(255,0,0,80);
  render.drawEdges(invMesh);
}