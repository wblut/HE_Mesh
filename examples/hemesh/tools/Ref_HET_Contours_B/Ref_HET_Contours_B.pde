import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;

HE_Mesh mesh;
WB_Render render;
List<WB_Segment> segments;

void setup() {
  fullScreen(P3D);
  smooth(8);
  createMesh();
  segments=HET_Contours.contours(mesh, new Sphere(), 0, 800, 10);
  render=new WB_Render(this);
}

void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(map(mouseY, 0, height, PI, -PI));
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  noFill();
  strokeWeight(4);
  stroke(255, 0, 0);
  render.drawSegment(segments);
}

void createMesh() {
  mesh=new HE_Mesh(new HEC_Beethoven().setScale(11));
}

class Sphere implements WB_ScalarParameter {
  public double evaluate(double... x) {
    return Math.sqrt((x[0])*(x[0])+x[1]*x[1]+x[2]*x[2]);
  }
}