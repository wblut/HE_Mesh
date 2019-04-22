import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;
int[] tetrahedra;
int[] triangles;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  source=new WB_RandomOnSphere();
  render=new WB_Render3D(this);
  numPoints=400;
  points=new WB_Point[numPoints+100];
  for (int i=0; i<numPoints; i++) {
    points[i]=source.nextPoint().mulSelf(random(260,340));
  }
  source=new WB_RandomOnCylinder().setRadius(50).setHeight(600);
  for (int i=numPoints; i<numPoints+100; i++) {
    points[i]=source.nextPoint();
  }
  WB_AlphaTriangulation3D triangulation=WB_Triangulate.alphaTriangulate3D(points);
  tetrahedra=triangulation.getAlphaTetrahedra(10000.0);// 1D array of indices of tetrahedra, 4 indices per tetrahedron
  triangles=triangulation.getAlphaTriangles(65.0);
  println("First tetrahedron: ["+tetrahedra[0]+", "+tetrahedra[1]+", "+tetrahedra[2]+", "+tetrahedra[3]+"]");
}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  WB_Point center;
   stroke(0);
   strokeWeight(1.0);
  for (int i=0; i<tetrahedra.length; i+=4) {
    pushMatrix();
    // center=new WB_Point(points[tetrahedra[i]]).addSelf(points[tetrahedra[i+1]]).addSelf(points[tetrahedra[i+2]]).addSelf(points[tetrahedra[i+3]]).mulSelf(0.25+0.25*sin(0.005*frameCount));
    //render.translate(center);
    render.drawTetrahedron(points[tetrahedra[i]], points[tetrahedra[i+1]], points[tetrahedra[i+2]], points[tetrahedra[i+3]]);
    popMatrix();
  }
  fill(255);
  stroke(255,0,0);
  strokeWeight(3.0);
  for (int i=0; i<triangles.length; i+=3) {
    render.drawTriangle(points[triangles[i]], points[triangles[i+1]], points[triangles[i+2]]);
  }
  
}