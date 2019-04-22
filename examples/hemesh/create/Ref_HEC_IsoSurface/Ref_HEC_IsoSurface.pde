import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);

// Create an isosurface from an explicit grid of values.
// Potentially uses a lot of memory.

  float[][][] values=new float[51][51][51];
  for (int i = 0; i < 51; i++) {
    for (int j = 0; j < 51; j++) {
      for (int k = 0; k < 51; k++) {
        values[i][j][k]=noise(0.07*i, 0.07*j, 0.07*k);
      }
    }
  }

  HEC_IsoSurface creator=new HEC_IsoSurface();
  creator.setResolution(50, 50,50);// number of cells in x,y,z direction
  creator.setSize(8, 8, 8);// cell size
  creator.setValues(values);// values corresponding to the grid points
  // values can also be double[][][]
  creator.setIsolevel(.6);// isolevel to mesh
  creator.setInvert(false);// invert mesh
  creator.setBoundary(-200);// value of isoFunction outside grid
  // use creator.clearBoundary() to rest boundary values to "no value".
  // A boundary value of "no value" results in an open mesh
  
  //Gamma controls level of grid snap, 0.0-0.5. Can improve the 
  //quality of the triangles, but can give small changes in topology.
  creator.setGamma(0.3); 
  

  mesh=new HE_Mesh(creator);

  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}