import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory gf;
WB_Render2D render;

WB_Circle C1,C2;
List<WB_Circle> circles;
float radius;

void setup() {
  size(800, 800);
  gf=new WB_GeometryFactory();
  render=new WB_Render2D(this);
  radius=200;
  C1= gf.createCircleWithRadius(-200,0,100);
  C2= gf.createCircleWithRadius(200,0,random(250));
}

void create() {
  radius=mouseX;
  circles=gf.createCircleTangentTo2Circles(C1,C2,radius);
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
  render.drawCircle2D(C1);
  render.drawCircle2D(C2);
}


void mousePressed(){
  C1= gf.createCircleWithRadius(-200,0,100);
  C2= gf.createCircleWithRadius(200,0,random(250));
}