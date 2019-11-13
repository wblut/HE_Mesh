import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;
HE_Mesh mesh, mesh2;
List<WB_Point> points;
List<WB_Point> inside;
List<WB_Point> outside;
WB_Render3D render;
WB_BSPTree3D tree;
void setup() {
  fullScreen(P3D);
  smooth(8);
  render=new WB_Render3D(this);
  mesh=new HE_Mesh(new HEC_Icosahedron().setRadius(200)); 
  mesh.modify(new HEM_Crocodile().setDistance(200));
  tree=new WB_BSPTree3D();
  tree.build(mesh);
  points=new ArrayList<WB_Point>();
  WB_PointFactory rp=new WB_RandomInSphere().setRadius(350);
  WB_Point trial;
  for (int i=0; i<20000; i++) {
    do{
      trial=rp.nextPoint();
    }while(trial.zd()>0);
    points.add(trial);
  }
  inside=new ArrayList<WB_Point>();
  outside=new ArrayList<WB_Point>();
  for (WB_Point p : points) {
    if (tree.pointLocation(p)<0) {
      inside.add(p);
    } else {
      outside.add(p);
    }
  }
}

void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(255, 0, 0,100);
  render.drawPoint(outside);
  stroke(0, 255, 0);
  render.drawPoint(inside);
}
