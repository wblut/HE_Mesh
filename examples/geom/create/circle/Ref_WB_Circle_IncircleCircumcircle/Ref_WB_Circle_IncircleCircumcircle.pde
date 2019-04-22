import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

WB_GeometryFactory gf;
WB_Render2D render;

WB_Triangle T;
WB_Circle incircle;
WB_Circle circumcircle;
WB_RandomPoint generator;

void setup() {
  size(800, 800);
  gf=new WB_GeometryFactory();
  render=new WB_Render2D(this);
  generator=new WB_RandomRectangle().setSize(800, 800);
  create();
}

void create() {
  T=gf.createTriangle(generator.nextPoint(), generator.nextPoint(), generator.nextPoint());
  incircle=gf.createIncircle(T);
  circumcircle=gf.createCircumcircle2D(T);
}

void draw() {
  background(55);
  translate(400,400);
  noFill();
  strokeWeight(2);
  stroke(255, 0, 0);
  render.drawCircle2D(incircle);
  render.drawCircle2D(circumcircle);
  stroke(0);
  render.drawTriangle2D(T);
}


void mousePressed() {
  create();
}