import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane P;
WB_Line L;
HEM_Bend modifier;
WB_GeometryFactory gf=new WB_GeometryFactory();
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();
  
  modifier=new HEM_Bend();
  
  P=new WB_Plane(0,0,-200,0,0,1); 
  modifier.setGroundPlane(P);// Ground plane of bend modifier 
  //you can also pass directly as origin and normal:  modifier.setGroundPlane(0,0,-200,0,0,1)
 
  L=new WB_Line(0,0,-200,-1,0,-200);
  modifier.setBendAxis(L);// Bending axis
  //you can also pass the line as two points:  modifier.setBendAxis(0,0,-200,1,0,-200)
  
  modifier.setAngleFactor(30.0/400);// Angle per unit distance (in degrees) to the ground plane
  // points which are a distance d from the ground plane are rotated around the
  // bend axis by an angle d*angleFactor;
 
  modifier.setPosOnly(false);// apply modifier only on positive side of the ground plane?
  
  mesh.modify(modifier);
  
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(0.125*TWO_PI);
  rotateX(HALF_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
  render.drawPlane(P,500);
  render.drawLine(L,800);
  //recreate mesh and modifier
  createMesh();
  L=gf.createLineThroughPoints(0,0,500-mouseY,-1,0,500-mouseY);
  modifier.setAngleFactor(0.00030*mouseX);
  modifier.setBendAxis(L);
  mesh.modify(modifier);
}


void createMesh(){
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(32).setSteps(16).setRadius(50).setHeight(400).setCenter(0,0,0);
  mesh=new HE_Mesh(creator);
  
}