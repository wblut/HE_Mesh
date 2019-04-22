import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;
int[] tetrahedra;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  source=new WB_RandomBox().setSize(500,500,500);
  render=new WB_Render3D(this);
  numPoints=100;
  points=new WB_Point[numPoints];
  for (int i=0; i<numPoints; i++) {
    points[i]=source.nextPoint();
  }
  WB_Triangulation3D triangulation=WB_Triangulate.triangulate3D(points);
  tetrahedra=triangulation.getTetrahedra();// 1D array of indices of tetrahedra, 4 indices per tetrahedron
  println("First tetrahedron: ["+tetrahedra[0]+", "+tetrahedra[1]+", "+tetrahedra[2]+", "+tetrahedra[3]+"]");
 

}


void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  WB_Point center;
  for(int i=0;i<tetrahedra.length;i+=4){
    pushMatrix();
    center=new WB_Point(points[tetrahedra[i]]).addSelf(points[tetrahedra[i+1]]).addSelf(points[tetrahedra[i+2]]).addSelf(points[tetrahedra[i+3]]).mulSelf(0.25+0.25*sin(0.005*frameCount));
  render.translate(center);
  render.drawTetrahedron(points[tetrahedra[i]],points[tetrahedra[i+1]],points[tetrahedra[i+2]],points[tetrahedra[i+3]]);
  popMatrix();
}
}