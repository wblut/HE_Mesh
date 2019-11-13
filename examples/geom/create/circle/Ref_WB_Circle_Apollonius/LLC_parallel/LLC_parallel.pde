import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Line L1;
WB_Line L2;
WB_Circle C;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  L1= factory.createLineWithDirection2D( width/2, height/2+100,1,0);
  L2= factory.createLineWithDirection2D( width/2, height/2-100,1,0);
  C= factory.createCircleWithRadius( mouseX, mouseY,80);
}

void draw() {
  background(255);
  create();
  noFill();
  stroke(0, 120);
  render.drawLine2D(L1, width);
  render.drawLine2D(L2, width);
  render.drawCircle2D(C);
  stroke(255,0,0, 120);
  List<WB_Circle> circles=factory.createCircleLLC(L1, L2,C);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
}