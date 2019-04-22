import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;
WB_Sphere sphere, sphereInCenter;
void setup(){
 size(800,800,P3D);
 source=new WB_RandomBox().setSize(500,300,200);
 render=new WB_Render3D(this);
 numPoints=20;
 points=new WB_Point[numPoints];
  for(int i=0;i<numPoints;i++){
   points[i]=source.nextPoint();
 }
 sphere=WB_GeometryOp.getBoundingSphere(points);
  sphereInCenter=WB_GeometryOp.getBoundingSphereInCenter(points);
  textAlign(CENTER);
}


void draw(){
 background(55);
 fill(255);
 text("Black=minimal bounding sphere",width/2, height-24);
 text("Blue=minimal bounding sphere in centroid",width/2, height-10);
 noFill();
 directionalLight(255, 255, 255, 1, 1, -1);
 directionalLight(127, 127, 127, -1, -1, 1);
 translate(width/2, height/2, 0);
 rotateY(mouseX*1.0f/width*TWO_PI);
 rotateX(mouseY*1.0f/height*TWO_PI);
 stroke(255,0,0);
 render.drawPoint(points,5);
 stroke(0,100);
 pushMatrix();
 render.translate(sphere.getCenter());
 sphere((float)sphere.getRadius());
 popMatrix();
 stroke(0,0,255,100);
 render.translate(sphereInCenter.getCenter());
 sphere((float)sphereInCenter.getRadius());
  
}