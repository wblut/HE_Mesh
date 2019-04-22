import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;
import java.util.List;

HE_Mesh mesh;
WB_Render3D render;
int counter;


void setup() {
  size(1000, 1000, P3D);  
  smooth(8);
  textAlign(CENTER);
  render=new WB_Render3D(this);
  counter=0;
  mesh=new HE_Mesh(new HEC_Beethoven());
  mesh.scaleSelf(10);
}

void draw() {
  background(0);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  text("Click for Taubin smooth ("+counter+").", 0, 450);
   rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,-PI,PI));
  noFill();
  stroke(255, 0, 0);
  render.drawEdges(mesh);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
}

void mousePressed() {
  HEM_TaubinSmooth smooth= new HEM_TaubinSmooth().setIterations(2);
  mesh.modify(smooth);
  counter+=2;
}