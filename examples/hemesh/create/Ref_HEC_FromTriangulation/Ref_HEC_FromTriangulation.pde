import wblut.nurbs.*;
import wblut.hemesh.*;
import wblut.core.*;
import wblut.geom.*;
import wblut.processing.*;
import wblut.math.*;



WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;
int[] triangles;
HE_Mesh mesh;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  source=new WB_RandomRectangle().setSize(1200, 1200);
  render=new WB_Render3D(this);
  numPoints=250;
  points=new WB_Point[numPoints];
  for (int i=0; i<numPoints; i++) {
    points[i]=source.nextPoint();
  }
  WB_Triangulation2D triangulation=WB_Triangulate.triangulate2D(points);
  triangles=triangulation.getTriangles();// 1D array of indices of triangles, 3 indices per triangle
  mesh=new HE_Mesh(new HEC_FromTriangulation().setTriangulation(triangulation).setPoints(points));
  println(WB_Version.version());
}


void draw() {
  background(255);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
}