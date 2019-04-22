import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render2D render;
WB_Point[] points;
int numPoints;
int[] triangles;
int[] edges;
void setup() {
  size(1000, 1000, P3D);
  source=new WB_RandomDisk().setRadius(350);
  render=new WB_Render2D(this);
  numPoints=5000;
  points=new WB_Point[numPoints];
  for (int i=0; i<numPoints; i++) {
    points[i]=source.nextPoint();
  }
  WB_Triangulation2D triangulation=WB_Triangulate.triangulate2D(points);
  triangles=triangulation.getTriangles();// 1D array of indices of triangles, 3 indices per triangle
  println("First triangle: ["+triangles[0]+", "+triangles[1]+", "+triangles[2]+"]");
  edges=triangulation.getEdges();// 1D array of indices of triangulation edges, 2 indices per edge
   println("First edge: ["+edges[0]+", "+edges[1]+"]");

}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  render.drawTriangle2D(triangles, points);
}