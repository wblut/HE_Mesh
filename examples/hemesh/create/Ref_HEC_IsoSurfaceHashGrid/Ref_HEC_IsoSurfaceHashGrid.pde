import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.opengl.*;

HE_Mesh mesh;
WB_HashGridDouble grid;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  int R=100;
  float dR=600.0/R;
  grid = new WB_HashGridDouble(R, R, R, 0);

  // Fill grid with 100 metaballs
  for (int i = 0; i < 100; i++) {
    int k=(int)random(R);
    int l=(int)random(R);
    int m=(int)random(R);
    int r=(int)random(10, 20);//radius of metaball
    double r2=r*r;
    for (int dk=-r; dk<=r; dk++) {
      for (int dl=-r; dl<=r; dl++) {
        for (int dm=-r; dm<=r; dm++) {
          double d2=(dk*dk+dl*dl+dm*dm)/r2;
          if (d2<1.0) {
            double v=1.0-d2;
            grid.addValue(v*v, k+dk, l+dl, m+dm);
          }
        }
      }
    }
  }

  HEC_IsoSurface creator=new HEC_IsoSurface();
  creator.setSize(dR, dR, dR);
  creator.setValues(grid);
  creator.setIsolevel(.6);
  creator.setInvert(false);
  creator.setBoundary(-100);
  
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