import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory gf;
WB_Render2D render;

WB_Line L1,L2;
List<WB_Circle> circles;
float radius;

void setup() {
  size(800, 800);
  gf=new WB_GeometryFactory();
  render=new WB_Render2D(this);
  radius=200;
  L1= gf.createLineThroughPoints(0,0,random(0, 400),random(0, 400));
  L2= gf.createLineThroughPoints(0,0,random(0, 400),random(-400, 0));
}

void create() {
  radius=mouseX;
  circles=gf.createCircleTangentTo2Lines(L1,L2,radius);
}

void draw() {
  background(55);
  translate(400,400);
  create();
  noFill();
  strokeWeight(2);
  stroke(255,0,0);
  for(WB_Circle C:circles){
  render.drawCircle2D(C);
  }
  stroke(0, 120);
  render.drawLine2D(L1,2000);
  render.drawLine2D(L2,2000);
}


void mousePressed(){
  L1= gf.createLineThroughPoints(0,0,random(0, 400),random(0, 400));
  L2= gf.createLineThroughPoints(0,0,random(0, 400),random(-400, 0));
}