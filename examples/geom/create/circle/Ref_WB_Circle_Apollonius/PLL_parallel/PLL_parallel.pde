import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Line L1;
WB_Line L2;
WB_Point p;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  L1= factory.createLineWithDirection2D( width/2, height/2+100, 1,0);
  L2= factory.createLineWithDirection2D( width/2, height/2-100, 1, 0);
  p= factory.createPoint( mouseX, mouseY);
  circles=factory.createCirclePLL(p, L1, L2);
}

void draw() {
  background(255);
  create();
  noFill();
  stroke(0, 120);
  render.drawLine2D(L1, width);
  render.drawLine2D(L2, width);
  render.drawPoint2D(p, 5);
  stroke(255,0,0, 120);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
}