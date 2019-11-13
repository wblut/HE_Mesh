import wblut.geom.*;
import wblut.processing.*;

WB_Point[] points; 
WB_AABB AABB;
int numPoints;
WB_Render3D render;
void setup() {
  size(1000, 1000, P3D);
  background(55);
  render=new WB_Render3D(this);
  noFill();
  create();
}

void draw() {
  background(55); 
  translate(width/2, height/2);
  rotateY(mouseX*1.0/width*TWO_PI-PI);
  rotateX(mouseY*1.0/height*TWO_PI-PI);
  stroke(255, 0, 0, 200);
  render.drawAABB(AABB);
  for (int i=0; i< numPoints; i++) {
    stroke(0, 125);
    render.drawPoint(points[i], 5);
    WB_Point q=WB_GeometryOp.getClosestPoint3D(points[i], AABB);
    beginShape(LINES);
    stroke(0, 50);
    render.vertex(points[i]);
    stroke(255, 0, 0, 50);
    render.vertex(q);
    endShape();
    stroke(255, 0, 0, 125);
    render.drawPoint(q, 2);
  }
}

void mousePressed() {
  create();
}

void create() {
  WB_RandomPoint generator=new WB_RandomBox().setSize(500, 500, 500);
  AABB=new WB_AABB(generator.nextPoint(), generator.nextPoint());
  numPoints=1000;
  points=new WB_Point[numPoints];

  for (int i=0; i< numPoints; i++) {
    points[i]= generator.nextPoint();
  }
}
