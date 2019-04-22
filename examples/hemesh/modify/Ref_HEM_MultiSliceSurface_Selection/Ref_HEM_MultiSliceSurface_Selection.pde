import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Plane[] planes;
int numPlanes;
HEM_MultiSliceSurface modifier;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();
  numPlanes=15;
  modifier=new HEM_MultiSliceSurface();
  planes=new WB_Plane[numPlanes];
  for(int i=0;i<numPlanes;i++){
  planes[i]=new WB_Plane(0,0,random(-50,50),random(-1,1),random(-1,1),random(-1,1));
  } 
  modifier.setPlanes(planes);// Cut plane 
  //planes can also be any Collection<WB_Plane>
  modifier.setOffset(0);// shift cut plane along normal
  HE_Selection sel=mesh.selectRandomFaces(0.4);
  sel.modify(modifier);
 
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  
  fill(255,0,0);
  noStroke();
  render.drawFaces(mesh.getSelection("cuts"));
  noFill();
  stroke(0);
  render.drawEdges(mesh);
  strokeWeight(4);
  stroke(0,0,255);
 
  render.drawEdgesWithInternalLabel(1,mesh);// New edges by the slice operation get label 1
 
  strokeWeight(1);
  stroke(255,0,0);
  for(int i=0;i<numPlanes;i++){
  render.drawPlane(planes[i],400);
  }

}


void createMesh(){
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(64).setSteps(64).setRadius(180).setHeight(600).setCap(false,false).setCenter(0,0,0);
  mesh=new HE_Mesh(creator);
  mesh.modify(new HEM_Shell().setThickness(40));
}