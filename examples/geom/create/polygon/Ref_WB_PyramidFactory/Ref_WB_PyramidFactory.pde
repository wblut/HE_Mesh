import wblut.geom.*;
import wblut.hemesh.*;
import wblut.core.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
WB_PyramidFactory pf;
WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render3D render;
ArrayList<WB_Point> shell;
double[] offsets;
WB_Polygon polygon;
HE_Mesh pyramid;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render3D(this);
  create(250);
  background(55);
  noCursor();
}

void draw() {


  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(pyramid);
  noFill();
  stroke(0);
  render.drawEdges(pyramid);
 
}

void mousePressed() {
  create(250);
}

void create(float h) {
  shell= new ArrayList<WB_Point>();
  offsets=new double[40];
  for (int i=0; i<40; i++) {
    shell.add(gf.createPointFromPolar(200*(i%2+1)*random(0.5, 1.2), TWO_PI/40.0*i));
    offsets[i]=40;
  } 
  polygon=gf.createSimplePolygon(shell);
  pf= new WB_PyramidFactory();
  pf.setPoints(polygon);
  pyramid=new HE_Mesh(pf.createOffset(h, offsets));
  frameCount=0;
}