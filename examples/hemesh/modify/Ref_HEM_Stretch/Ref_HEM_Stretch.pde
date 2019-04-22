import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane P;
HEM_Stretch modifier;

void setup() {
  size(1000, 1000, P3D);
  createMesh();
  
  modifier=new HEM_Stretch();
  
  P=new WB_Plane(0,0,0,1,1,0); 
  modifier.setGroundPlane(P);// Ground plane of stretch modifier 
  //you can also pass directly as origin and normal:  modifier.setGroundPlane(0,0,-200,0,0,1)
  
  modifier.setStretchFactor(2);// Amount to stretch mesh along ground plane normal
 
 //Optionally:
 //modifier.setCompressionFactor(1.2);//  Amount to compress mesh perpendicular ground plane normal.
 //If not explicitely set this defaults to sqrt(stretchFactor);
 
  modifier.setPosOnly(false);// apply modifier only on positive side of the ground plane?
  
  mesh.modify(modifier);
  
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(.125*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
  render.drawPlane(P,300);
  //reconstruct mesh and modifier
  createMesh();
  modifier.setStretchFactor(mouseX*0.005);
   mesh.modify(modifier);
  
}


void createMesh(){
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(32).setSteps(16).setRadius(100).setHeight(400);
  mesh=new HE_Mesh(creator); 
}