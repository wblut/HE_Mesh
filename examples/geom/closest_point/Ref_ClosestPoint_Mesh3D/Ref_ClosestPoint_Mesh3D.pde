import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;
import java.util.List;

HE_Mesh mesh;
WB_Render3D render;

WB_AABBTree3D tree;
List<WB_Point> points;

void setup() {
  size(1000, 1000, P3D);  
  smooth(8);
  render=new WB_Render3D(this);
  mesh=new HE_Mesh(new HEC_Beethoven().setScale(10).setZAxis(0, 1, 0).setZAngle(PI));
  mesh.smooth();
  tree=new WB_AABBTree3D(mesh, 1);
  WB_PointFactory rp=new WB_RandomInCylinder().setHeight(700).setRadius(200);
  points=new ArrayList<WB_Point>();
  for (int i=0; i<500; i++) {
    points.add(rp.nextPoint().rotateAboutAxisSelf(HALF_PI, 0, 0, 0, 1, 0, 0));
  }
}

void draw() {
  background(55);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  rotateY(2*PI/3);//mouseX*1.0f/width*TWO_PI);
  rotateX(PI);//mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(0);
  render.drawEdges(mesh);
  fill(200);
  render.drawFaces(mesh);
  stroke(255, 0, 0);
  WB_Coord q;
  for (WB_Point p : points) {
    stroke(0);
    render.drawPoint(p, 2);
    q=tree.getClosestPoint(p);
    beginShape(LINES);
    render.vertex(p);
    stroke(255, 0, 0, 2);
    render.vertex(q);
    endShape();
    stroke(255, 0, 0,80);
    render.drawPoint(q, 4);
    p.rotateAboutAxisSelf(0.01, 0, 0, 0, 0, 1, 0);
  }
}
