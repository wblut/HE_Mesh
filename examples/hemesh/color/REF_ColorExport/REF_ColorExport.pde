import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render3D render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
   HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setEdge(200); 
  mesh=new HE_Mesh(creator); 
  HEM_ChamferCorners cc=new HEM_ChamferCorners().setDistance(40);
  mesh.modify(cc);
  mesh.getSelection("chamfer").modify(new HEM_Crocodile().setDistance(150));
  mesh.smooth();
  mesh.triangulate();
  HE_FaceIterator fitr=mesh.fItr();
  colorMode(HSB);
  while (fitr.hasNext ()) {
    fitr.next().setColor(color((int)random(4.0)*10, 255, 255));
  }

  HE_VertexIterator vitr=mesh.vItr();
  colorMode(HSB);
  while (vitr.hasNext ()) {
    vitr.next().setColor(color((int)random(4.0)*10, 255, 255));
  }
  colorMode(RGB);
  HET_Export.saveToWRL(mesh, sketchPath("WRL"), "testnc");
  HET_Export.saveToWRLWithFaceColor(mesh, sketchPath("WRL"), "testfc");
  HET_Export.saveToWRLWithVertexColor(mesh, sketchPath("WRL"), "testvc");

  HET_Export.saveToPLY(mesh, sketchPath("PLY"), "testnc");
  HET_Export.saveToPLYWithVertexColor(mesh, sketchPath("PLY"), "testvc");
  HET_Export.saveToPLYWithFaceColor(mesh, sketchPath("PLY"), "testfc");

  HET_Export.saveToOBJ(mesh, sketchPath("OBJ/nocolor"), "testnc");
  HET_Export.saveToOBJWithVertexColor(mesh, sketchPath("OBJ/vertexcolor"), "testvc");
  HET_Export.saveToOBJWithFaceColor(mesh, sketchPath("OBJ/facecolor"), "testfc");

  HET_Export.saveToSTL(mesh, sketchPath("STL"), "testnc");

  render=new WB_Render3D(this);
}

void draw() {
  background(60);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFacesFC(mesh);
}