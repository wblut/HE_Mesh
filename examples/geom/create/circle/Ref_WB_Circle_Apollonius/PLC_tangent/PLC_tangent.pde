import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Point p;
WB_Line L;
WB_Circle C;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  p= factory.createPoint(mouseX, mouseY);
  L= factory.createLineWithDirection2D(width/2, height/2+100, 1, 0);
  C= factory.createCircleWithRadius(width/2, height/2, 100);
  circles=factory.createCirclePLC(p, L, C);
}

void draw() {
  background(255);
  create();
  noFill();
  stroke(0, 120);
  render.drawPoint2D(p, 5);
  render.drawLine2D(L, 2*width);
  render.drawCircle2D(C);
  stroke(255,0,0, 120);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
}