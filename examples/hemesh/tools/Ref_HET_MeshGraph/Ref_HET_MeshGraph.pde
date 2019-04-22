import processing.opengl.*;

import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  fullScreen(OPENGL);
  smooth(8);
  createMesh();

  HEM_Wireframe modifier=new HEM_Wireframe();
  modifier.setConnectionRadius(6);// Connection radius
  modifier.setConnectionFacets(6);// number of faces in the Connections, min 3, max whatever blows up the CPU
  // modifier.setMaximumConnectionOffset(20);// limit the joint radius by decreasing the Connection radius where necessary. Joint offset is added after this limitation.
  modifier.setAngleOffset(0.5);// rotate the Connections by a fraction of a facet. 0 is no rotation, 1 is a rotation over a full facet. More noticeable for low number of facets.
  modifier.setTaper(false);// allow Connections to have different radii at each end?
  mesh.modify(modifier);

  mesh.modify(new HEM_Slice().setPlane(0, 0, -50, 0, 0, 1));
  HE_Selection sel=mesh.copySelection("caps", "sel");
  sel.invertSelection();
  sel.subdivide(new HES_CatmullClark());
  render=new WB_Render(this);
}

void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawFaces(mesh);
  fill(255, 0, 0);
  render.drawFaces(mesh.getSelection("caps"));
  stroke(0);
  render.drawEdges(mesh);
}

void createMesh() {
  HEC_SuperDuper sd=new HEC_SuperDuper();
  sd.setU(48).setV(8).setRadius(100);
  sd.setDonutParameters(0, 10, 10, 10, 3, 6, 12, 12, 3, 5);
  mesh=new HE_Mesh(sd);
}