import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.processing.*;

HE_Mesh mesh;
HE_Mesh[] cylinders;

WB_Render3D render;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  background(55); 
  render=new WB_Render3D(this);
  create();
}

void create() {
  mesh=new HE_Mesh(new HEC_Icosahedron().setRadius(150));
  cylinders=new HE_Mesh[mesh.getNumberOfFaces()];
  for (int i=0; i<mesh.getNumberOfFaces(); i++) {
    cylinders[i]=new HE_Mesh(new HEC_Cylinder(40, 10, 150, 8, 1).setCenter(0, 0,75)); 
    HE_Face face=mesh.getFaceWithIndex(i);
    //Create a coordinate transfromation from one origin and orientation to another origin and orientation
    WB_Transform T=new WB_Transform(WB_Point.ORIGIN(), WB_Vector.Z(), face.getFaceCenter(), face.getFaceNormal());
    cylinders[i].transformSelf(T);// or .applySelf(T)
  }
}


void draw() {
  background(50);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  for (HE_Mesh cylinder : cylinders) render.drawFaces(cylinder);
  stroke(0);
  render.drawEdges(mesh);
  for (HE_Mesh cylinder : cylinders) render.drawEdges(cylinder);
}