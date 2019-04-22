import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render2D render;
ArrayList<WB_Point> shell;
WB_Polygon polygon;
boolean flip;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render2D(this);
  shell= new ArrayList<WB_Point>();

  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100*(i%2+1), TWO_PI/20.0*i));
  } 
  polygon=gf.createSimplePolygon(shell);

  background(55);
  textAlign(CENTER);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  if (flip) scale(1, -1);
  fill(255, 0, 0);
  render.drawPolygon2D(polygon);
  WB_Point p;
  for (int i=0; i<20; i+=2) {
    p= polygon.getPoint(i); 
    line(p.xf()*1.4, p.yf()*1.4, p.xf()*1.1, p.yf()*1.1);
    textWithFlip(str(i), p.xf()*1.5, p.yf()*1.5, flip);
  }
  for (int i=1; i<20; i+=2) {
    p= polygon.getPoint(i); 
    line(p.xf()*1.16, p.yf()*1.16, p.xf()*1.04, p.yf()*1.04);
    textWithFlip(str(i), p.xf()*1.2, p.yf()*1.2, flip);
  }

  line(-390, -390, -290, -390);
  textWithFlip("x", -280, -385, flip);
  line(-390, -390, -390, -290);
  textWithFlip("y", -390, -280, flip);
  if (flip) {
    textWithFlip("Right-handed coordinate system (click to flip)", 0, -390, flip);
  } else {
    textWithFlip("Processing/Left-handed coordinate system (click to flip)", 0, 390, flip);
  }

  drawOrientation();
}



void textWithFlip(String text, float x, float y, boolean flip) {
  pushMatrix();
  translate(x, y);
  if (flip) scale(1, -1);
  text(text, 0, 0);
  popMatrix();
}

void drawOrientation() {
  noFill();
  arc(0, 0, 600, 600, 0, TWO_PI/4.0*3.0);
  line(0, -280, 0, -320);
  line(0, -280, 40, -300);
  line(0, -320, 40, -300);
  
}

void mousePressed() {
  flip=!flip;
}