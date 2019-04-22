import wblut.processing.*;
import wblut.geom.*;
import wblut.math.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Point p;
WB_Point q;
WB_Circle C;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  p= factory.createPoint(width/2+180, height/2);
  q= factory.createPoint(mouseX, mouseY);
  C= factory.createCircleWithRadius( width/2, height/2, 150);
  circles=factory.createCirclePPC(p, q, C);
}

void draw() {
  background(55);
  create();
  noFill();
  stroke(0, 120);
  render.drawPoint2D(p, 5);
  render.drawPoint2D(q, 5);
  render.drawCircle2D(C);
  stroke(255,0,0, 120);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
}