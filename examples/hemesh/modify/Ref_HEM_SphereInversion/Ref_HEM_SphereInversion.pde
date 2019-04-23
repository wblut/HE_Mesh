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

  HEM_SphericalInversion modifier=new HEM_SphericalInversion();
  modifier.setRadius(200);
  modifier.setCenter(50, 0, 0);
  //also accepts a WB_Point
  modifier.setCutoff(1000);// maximum distance outside the inversion sphere
  modifier.setLinear(false);// if true, mirrors a point across the sphere surface instead of a true spherical inversion
  mesh.modify(modifier);

  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}


void createMesh() {
  HEC_Cube creator=new HEC_Cube(200, 1,1, 1);
  mesh=new HE_Mesh(creator);
  HEM_Extrude ext=new HEM_Extrude().setDistance(200);
  mesh.modify(ext);
  HEM_Extrude ext2=new HEM_Extrude().setDistance(0).setChamfer(0.5);
  ext.extruded.modify(ext2);
  ext=new HEM_Extrude().setDistance(-200);
  ext2.extruded.modify(ext);
  mesh=new HE_Mesh(new HEC_FromFrame().setFrame(mesh).setMaximumStrutLength(20));
  mesh.smooth();
}