import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Line L;
WB_Circle C1;
WB_Circle C2;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this);
}

void create() {
  L= factory.createLineWithDirection2D(width/2, height/2-130, 1, 0);
  C1= factory.createCircleWithRadius( width/2, height/2, 100);
  C2= factory.createCircleWithRadius( mouseX, mouseY,80);
  circles=factory.createCircleLCC(L, C1, C2);
  
}

void draw() {
  background(255);
  create();
  noFill();
  stroke(0, 120);
  render.drawLine2D(L, 2*width);
  render.drawCircle2D(C1);
  render.drawCircle2D(C2);
  stroke(255,0,00, 120);
  for (WB_Circle C:circles) {
    render.drawCircle2D(C);
  }
 }