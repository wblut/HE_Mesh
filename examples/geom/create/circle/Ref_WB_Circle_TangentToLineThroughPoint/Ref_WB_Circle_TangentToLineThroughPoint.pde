import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory gf;
WB_Render2D render;

WB_Line L;
WB_Point point;
List<WB_Circle> circles;
float radius;

void setup() {
  size(800, 800);
  gf=new WB_GeometryFactory();
  render=new WB_Render2D(this);
  radius=200;
  L= gf.createLineThroughPoints(0,0,random(0, 400),random(0, 400));
  point= gf.createPoint(random(100, 300),random(-300, -100));
}

void create() {
  radius=mouseX;
  circles=gf.createCircleTangentToLineThroughPoint(L,point,radius);
}

void draw() {
  background(55);
  translate(400,400);
  create();
  noFill();
  strokeWeight(2);
  stroke(255,0,0);
  for(WB_Circle circle:circles){
  render.drawCircle2D(circle);
  }
  stroke(0, 120);
  render.drawLine2D(L,2000);
  render.drawPoint2D(point,5);
}


void mousePressed(){
  L= gf.createLineThroughPoints(0,0,random(0, 400),random(0, 400));
  point= gf.createPoint(random(100, 300),random(-300, -100));
}