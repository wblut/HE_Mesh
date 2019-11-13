import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.math.*;

WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render3D render;
ArrayList<WB_Point> shell;
WB_Polygon polygon;
HE_Mesh mesh;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render=new WB_Render3D(this);
  shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100*(i%2+1), TWO_PI/20.0*i));
  }
  polygon=gf.createSimplePolygon(shell);
  mesh=new HE_Mesh(gf.createPrism(polygon, 100));
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX, 0, width, -PI/2, PI/2));
  rotateX(map(mouseY, 0, height, PI/2, -PI/2));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  strokeWeight(5);
  stroke(0, 0, 255);
  render.drawPolygonEdges(polygon);
}