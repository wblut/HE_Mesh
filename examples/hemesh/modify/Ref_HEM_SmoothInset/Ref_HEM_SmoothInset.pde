import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();
  
  HEM_SmoothInset modifier=new HEM_SmoothInset();
  modifier.setLevel(2);// level of recursive division
  modifier.setOffset(10);// distance between inset face and original faces (should be > 0)
  mesh.modify(modifier);
  mesh.getSelection("inset").modify(new HEM_Extrude().setDistance(-10));
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
   fill(255,0,0);
  noStroke();
  render.drawFaces(mesh.getSelection("inset"));
  fill(255,150);
 render.drawFaces(mesh.getSelection("walls"));
  
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh(){
  HEC_Cube creator=new HEC_Cube(300,5,5,5);
  mesh=new HE_Mesh(creator); 
}