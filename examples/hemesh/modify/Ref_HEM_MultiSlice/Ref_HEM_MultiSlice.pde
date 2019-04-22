import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh, slicedMesh;
WB_Render render;
WB_Plane[] planes;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  createMesh();
  
  HEM_MultiSlice modifier=new HEM_MultiSlice();
  planes=new WB_Plane[5];
  for(int i=0;i<5;i++){
    int pol=(random(100)<50)?-1:1;
  planes[i]=new WB_Plane(0,0,-pol*random(50,150),pol*random(1),pol*random(1),pol*random(1));
  } 
  modifier.setPlanes(planes);// Cut plane 
  //planes can also be any Collection<WB_Plane>
  modifier.setOffset(0);// shift cut plane along normal
  
  slicedMesh.modify(modifier);
  
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(0.25*TWO_PI);
  fill(255);
  noStroke();
  for(int i=-1;i<5;i++){
 fill(255-(i+1)*40,255,(i+1)*40);
    render.drawFacesWithInternalLabel(i,slicedMesh);//Multislice labels all faces with the index of the corresponding cutplane, -1 for part of an original face
  }
  noFill();
  stroke(0);
  render.drawEdges(mesh);
  stroke(255,0,0);
  for(int i=0;i<5;i++){
  render.drawPlane(planes[i],400);
  }

}


void createMesh(){
  HEC_Cylinder creator=new HEC_Cylinder();
  creator.setFacets(32).setSteps(1).setRadius(200).setHeight(400);
  mesh=new HE_Mesh(creator);
  slicedMesh=mesh.get();
  
}