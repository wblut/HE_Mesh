import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh source,expanded;
WB_Render render;
float d;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setEdge(100); 
  source=new HE_Mesh(creator); 
  source.modify(new HEM_Crocodile().setDistance(250));
  source.modify(new HEM_ChamferCorners().setDistance(50));
  source.subdivide(new HES_Planar());
  expanded=source.get();
  d=2;
  expanded.modify(new HEM_VertexExpand().setDistance(d));
  render=new WB_Render(this);
   textAlign(CENTER);
  textSize(16);
}

void draw() {
  background(55);
  translate(width/2,height/2);
  fill(255);
  noStroke();
  text("Left-click to expand.",0,450);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  strokeWeight(1.0);
  stroke(0);
  render.drawEdges(source);
  strokeWeight(2.0);
  stroke(255,0,0);
  render.drawEdges(expanded);
  noStroke();
  render.drawFaces(source);
}

void mousePressed(){
  expanded=source.get();
  d+=2;
  expanded.modify(new HEM_VertexExpand().setDistance(d));
}

void keyPressed(){
 expanded.smooth(); 
}