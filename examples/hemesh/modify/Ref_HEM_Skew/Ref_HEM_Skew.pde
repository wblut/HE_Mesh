import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane P;
WB_Vector V;
HEM_Skew modifier;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();
  
  modifier=new HEM_Skew();
  
  P=new WB_Plane(0,0,-200,0,0,1); 
  modifier.setGroundPlane(P);// Ground plane of skew modifier 
  //you can also pass directly as origin and normal:  modifier.setGroundPlane(0,0,-200,0,0,1)
 
 V=new WB_Vector(1,1,0);
  modifier.setSkewDirection(V);// Direction of skew shift
  //you can also pass the vector directly:  modifier.setSkewDirection(1,0,0)
  
  modifier.setSkewFactor(100.0/400);// Skew distance per unit distance to the ground plane
  // points which are a distance d from the ground plane are move over a distance
  // d*skewFactor in the skew direction
 
  modifier.setPosOnly(false);// apply modifier only on positive side of the ground plane?
  
  mesh.modify(modifier);
  
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(400, 350, 0);
  rotateY(0.125*TWO_PI);
  rotateX(HALF_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
  render.drawPlane(P,500);
  //recreate mesh and modifier
  createMesh();
   V=new WB_Vector(cos(mouseY*TWO_PI/height),sin(mouseY*TWO_PI/height),0);
  modifier.setSkewDirection(V);
  modifier.setSkewFactor(0.001*mouseX);
  mesh.modify(modifier);
}


void createMesh(){
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(32).setSteps(16).setRadius(50).setHeight(400);
  mesh=new HE_Mesh(creator);
  
}

