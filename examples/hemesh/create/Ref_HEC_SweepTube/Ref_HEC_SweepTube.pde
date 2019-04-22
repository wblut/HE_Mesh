import wblut.nurbs.*;
import wblut.hemesh.*;
import wblut.core.*;
import wblut.geom.*;
import wblut.processing.*;
import wblut.math.*;



WB_Render render;
WB_BSpline C;
WB_Point[] points;
HE_Mesh mesh;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  // Several WB_Curve classes are in development. HEC_SweepTube provides
  // a way of generating meshes from them.

  //Generate a BSpline
  points=new WB_Point[11];
  for (int i=0;i<11;i++) {
    points[i]=new WB_Point(5*(i-5)*(i-5), -200+40*i, random(100));
  }
  C=new WB_BSpline(points, 4);

  HEC_SweepTube creator=new HEC_SweepTube();
  creator.setCurve(C);//curve should be a WB_BSpline
  creator.setRadius(20);
  creator.setSteps(40);
  creator.setFacets(8);
  creator.setCap(true, true); // Cap start, cap end?

  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}