import wblut.processing.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Point p;
WB_Circle C1;
WB_Circle C2;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  p=factory.createPoint(mouseX, mouseY);
  C1= factory.createCircleWithRadius(400,400, 120);
  C2= factory.createCircleWithRadius(400, 400, 250);
  circles=factory.createCirclePCC(p, C1, C2);
}

void draw() {
  background(55);
  create();
  noFill();
  stroke(0, 120);
  render.drawPoint2D(p, 5);
  render.drawCircle2D(C1);
  render.drawCircle2D(C2);
  stroke(255,0,0, 120);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
}