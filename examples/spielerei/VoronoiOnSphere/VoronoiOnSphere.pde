/*
Then convex hull of points on a sphere is a Delaunay triangulation.
The dual of this triangulation is the Voronoi diagram of the points on the sphere.
The cells are technically parts of the sphere and not planar so the approach below
introduces some errors. But it's close.
*/

import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render3D render;

WB_Point[] points;
int num;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  WB_RandomOnSphere rs=new WB_RandomOnSphere();
  HEC_ConvexHull creator=new HEC_ConvexHull();
  num=500;
  points =new WB_Point[num];
  for (int i=0;i<num;i++) {
   
    points[i]=rs.nextPoint().mulSelf(400.0); 
  }
  creator.setPoints(points);
  creator.setN(num);  
  mesh=new HE_Mesh(creator); 
  mesh=new HE_Mesh(new HEC_Dual(mesh).setFixNonPlanarFaces(false));
  HEM_Extrude ext=new HEM_Extrude().setDistance(0).setChamfer(5).setRelative(false);
  mesh.modify(ext);
  mesh.getSelection("extruded").modify(new HEM_Extrude().setDistance(-20));
  //mesh.smooth(2);
  render=new WB_Render3D(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  
  render.drawEdges(mesh);

 for (int i=0;i<num;i++) {
    render.drawPoint(points[i]);
  }
  noStroke();
  render.drawFaces(mesh);
}
