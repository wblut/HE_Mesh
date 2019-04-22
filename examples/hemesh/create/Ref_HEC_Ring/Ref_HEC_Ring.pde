import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;

WB_Render3D render;
WB_RandomPoint generator;
WB_Circle circle;
HE_Mesh mesh;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  HEC_Ring rc=new HEC_Ring().setCenter(WB_Point.ORIGIN()).setNormal(new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0))).setRadius(200,220).setThickness(80).setN(48);
  mesh=new HE_Mesh(rc);  
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  lights();
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,-PI,PI));
  noFill();
  strokeWeight(2);
  stroke(255,0,0);
  strokeWeight(1);
  stroke(100,0,0);
  render.drawEdges(mesh);

  render.drawGizmo(150);
  noStroke();
  fill(255);
  render.drawFaces(mesh);
  
}

void mouseClicked(){
  HEC_Ring rc=new HEC_Ring().setCenter(WB_Point.ORIGIN()).setNormal(new WB_Vector(random(-1.0,1.0),random(-1.0,1.0),random(-1.0,1.0))).setRadius(200,220).setThickness(80).setN(48);
  mesh=new HE_Mesh(rc);
}