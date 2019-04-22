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
int[] tetrahedra;
int[] triangles;
HE_Mesh mesh;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  source=new WB_RandomOnSphere();
  render=new WB_Render3D(this);
  numPoints=1200;
  points=new WB_Point[numPoints];
  for (int i=0; i<numPoints-200; i++) {
    do{
    points[i]=source.nextPoint().mulSelf(random(300, 400));
    }while(points[i].zd()>0);
  }
  
  source=new WB_RandomInSphere();
  for (int i=numPoints-200; i<numPoints; i++) {
    points[i]=source.nextPoint().mulSelf(250);
  }
  
  WB_AlphaTriangulation3D triangulation=WB_Triangulate.alphaTriangulate3D(points);
  tetrahedra=triangulation.getAlphaTetrahedra(50.0);// 1D array of indices of tetrahedra, 4 indices per tetrahedron
  triangles=triangulation.getAlphaTriangles(50.0);
  mesh=new HE_Mesh(new HEC_AlphaShape().setTriangulation(triangulation).setAlpha(56.0));
  mesh.smooth();
  mesh.stats();
}


void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  WB_Point center;
  stroke(255,0,0);
  strokeWeight(2.0);
  for (int i=0; i<triangles.length; i+=3) {
    pushMatrix();
    render.drawTriangle(points[triangles[i]], points[triangles[i+1]], points[triangles[i+2]]);
    popMatrix();
  }
  fill(255);
  noStroke();
  render.drawFaces(mesh);
}