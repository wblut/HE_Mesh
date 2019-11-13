import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory factory;
WB_Render2D render;

WB_Line L1;
WB_Line L2;
WB_Line L3;
List<WB_Circle> circles;

void setup() {
  size(800, 800);
  factory=new WB_GeometryFactory();
  render=new WB_Render2D(this); 
}

void create() {
  L1= factory.createLineWithDirection2D( width/2,height/2, 0.2,1);
   L2= factory.createLineWithDirection2D( width/2,height/2, 1,0.2);
   L3= factory.createLineWithDirection2D( mouseX,mouseY, 1,1);
   circles=factory.createCircleLLL(L1,L2,L3);
}

void draw() {
  background(255);
  create();
  noFill();
  stroke(0, 120);
  render.drawLine2D(L1,width);
  render.drawLine2D(L2,width);
  render.drawLine2D(L3,2*width);
  stroke(255,0,0, 120);
  for(WB_Circle C:circles){
   render.drawCircle2D(C); 
  }
}