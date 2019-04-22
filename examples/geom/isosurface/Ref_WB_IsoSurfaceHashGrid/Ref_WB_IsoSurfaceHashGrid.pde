import wblut.nurbs.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.core.*;
import wblut.processing.*;
import wblut.math.*;
import java.util.List;


WB_Render3D render;
WB_HashGridDouble grid;
List<WB_Triangle> triangles;

void setup(){
  fullScreen(P3D);  
  render=new WB_Render3D(this);
  int R=100;
  float dR=600.0/R;
  grid = new WB_HashGridDouble(2*R, R, R,0);
 
  // Fill grid with 100 metaballs
  for (int i = 0; i < 100; i++) {
    int k=(int)random(2*R);
    int l=(int)random(R);
    int m=(int)random(R);
    int r=(int)random(10,20);//radius of metaball
    double r2=r*r;
    for (int dk=-r;dk<=r;dk++) {
      for (int dl=-r;dl<=r;dl++) {
        for (int dm=-r;dm<=r;dm++) {
          double d2=(dk*dk+dl*dl+dm*dm)/r2;
          if(d2<1.0){
          double v=1.0-d2;
          grid.addValue(v*v, k+dk, l+dl, m+dm);
          }
        }
      }
    }
  }
  
  WB_IsoSurface iso = new  WB_IsoSurface().setIsolevel(0.5).setSize(dR, dR, dR).setValues(grid);
  iso.setBoundary(-100);
  triangles=iso.getTriangles();
}


void draw(){
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,PI,-PI));
  noStroke();
  render.drawTriangle(triangles);
}