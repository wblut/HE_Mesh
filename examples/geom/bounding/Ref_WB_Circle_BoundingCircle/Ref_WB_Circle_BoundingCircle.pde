import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;
WB_Circle circle, circleInCenter;
void setup(){
 size(800,800,P3D);
 smooth(8);
 source=new WB_RandomRectangle().setSize(500,300);
 render=new WB_Render3D(this);
 numPoints=20;
 points=new WB_Point[numPoints];
  for(int i=0;i<numPoints;i++){
   points[i]=source.nextPoint();
 }
 circle=WB_GeometryOp.getBoundingCircle2D(points);
 circleInCenter=WB_GeometryOp.getBoundingCircleInCenter2D(points);
 textAlign(CENTER);
}


void draw(){
 background(55);
 fill(255);
 text("Black=minimal bounding circle",width/2, height-24);
 text("Blue=minimal bounding circle in centroid",width/2, height-10);
 noFill();
 directionalLight(255, 255, 255, 1, 1, -1);
 directionalLight(127, 127, 127, -1, -1, 1);
 translate(width/2, height/2, 0);
 
 rotateY(mouseX*1.0f/width*TWO_PI);
 rotateX(mouseY*1.0f/height*TWO_PI);
 stroke(255,0,0);
 render.drawPoint(points,5);
 stroke(0);
 render.drawCircle(circle);
 stroke(0,0,255);
 render.drawCircle(circleInCenter);

  
  
}