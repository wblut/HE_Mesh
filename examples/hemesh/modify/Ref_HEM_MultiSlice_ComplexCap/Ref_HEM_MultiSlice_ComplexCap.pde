import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh, slicedMesh;
WB_Render render;
WB_Plane[] planes;
int numPlanes;
HEM_MultiSlice modifier;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();

  modifier=new HEM_MultiSlice();
  numPlanes=5;
  planes=new WB_Plane[numPlanes];
  for (int i=0; i<numPlanes; i++) {
    int pol=(random(100)<50)?-1:1;
    planes[i]=new WB_Plane(-pol*random(50, 100), -pol*random(50, 100), -pol*random(50, 150), pol*random(1), pol*random(1), pol*random(1));
  } 
  modifier.setPlanes(planes);// Cut plane 
  //planes can also be any Collection<WB_Plane>
  modifier.setOffset(0);// shift cut plane along normal
  modifier.setOptimizeCap(true);
  slicedMesh.modify(modifier);
  slicedMesh.validate();
  slicedMesh.getSelection("caps").collectEdgesByFace();
    slicedMesh.modify(new HEM_HideEdges());
 for (int i=-1; i<numPlanes; i++) {
   
    slicedMesh.selectFacesWithInternalLabel("slice"+i, i);
    //Multislice internally labels all faces with the index of the corresponding cutplane, -1 for part of an original face
  }
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
  render.drawFaces(slicedMesh);
  for (int i=0; i<numPlanes; i++) {
    fill(255-(i+1)*25, 255, (i+1)*40);
    render.drawFaces(slicedMesh.getSelection("slice"+i));
  }

  noFill();
  strokeWeight(1);
  stroke(0);
  render.drawEdges(mesh);
  stroke(255, 0, 0);
  for (int i=0; i<numPlanes; i++) {
    render.drawPlane(planes[i], 600);
  }
 
  strokeWeight(1.5);
  render.drawEdges(slicedMesh.getSelection("caps"));
   
}


void createMesh() {
  HEC_Torus creator=new HEC_Torus(120, 300, 6, 16);
  mesh=new HE_Mesh(creator);
  creator=new HEC_Torus(60, 300, 6, 16);
  HE_Mesh inner=new HE_Mesh(creator);
  inner.modify(new HEM_Extrude().setDistance(48).setChamfer(0.8));
  HET_MeshOp.flipFaces(inner);
  mesh.add(inner);
  mesh.smooth(2);
  slicedMesh=mesh.get();
}