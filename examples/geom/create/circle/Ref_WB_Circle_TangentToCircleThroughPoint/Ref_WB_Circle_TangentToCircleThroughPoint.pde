import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory gf;
WB_Render2D render;

WB_Circle C;
WB_Point point;
List<WB_Circle> circles;
float radius;

void setup() {
  size(800, 800);
  gf=new WB_GeometryFactory();
  render=new WB_Render2D(this);
  radius=200;
  C= gf.createCircleWithRadius(random(0, 400),random(0, 400),100);
  point= gf.createPoint(random(100, 300),random(-300, -100));
}

void create() {
  radius=mouseX;
  circles=gf.createCircleTangentToCircleThroughPoint(C,point,radius);
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
  render.drawCircle2D(C);
  render.drawPoint2D(point,5);
}


void mousePressed(){
  C= gf.createCircleWithRadius(random(0, 400),random(0, 400),100);
  point= gf.createPoint(random(100, 300),random(-300, -100));
}