import wblut.processing.*;
import wblut.geom.*;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Point o;
WB_Point p;
WB_Point q;
WB_Circle C;
void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  o= factory.createPoint(320, 400);
  p=factory.createPoint(480, 400);
  q= factory.createPoint( mouseX, mouseY);
  C=factory.createCirclePPP(o, p, q);
}

void draw() {
  background(55);
  create();
  noFill();
  stroke(0, 120);
  render.drawPoint2D(o, 10);
  render.drawPoint2D(p, 10);
  render.drawPoint2D(q, 10);
  stroke(255, 0, 0, 120);
  render.drawCircle2D(C);
}