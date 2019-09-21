import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh[] meshes;
WB_Render render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Icosahedron creator=new HEC_Icosahedron();
  // .setToModelview(this) uses the Processing transforms to set the positions
  // .setToWorldview(), the default mode, ignores Processing transfors
  creator.setEdge(40).setToModelview(this);
  meshes=new HE_Mesh[27];
  for (int i=0; i<27; i++) {
    pushMatrix();
    rotateZ(i*PI/6.0);
    translate(200, 0,-260+20*i);
    scale(i*0.06);
    meshes[i]=new HE_Mesh(creator); 
    if(i>10) meshes[i].modify(new HEM_Lattice().setWidth(8).setDepth(8));
    popMatrix();
  }

  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  for(HE_Mesh mesh:meshes){
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
  }
}
