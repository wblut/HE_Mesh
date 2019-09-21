import wblut.math.*;
import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_AABBTree tree;
WB_Render3D render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_Dodecahedron creator=new HEC_Dodecahedron();
  creator.setCenter(50, 0, 10);
  creator.setEdge(200); 
  mesh=new HE_Mesh(creator); 

  HET_MeshOp.splitFacesCenter(mesh);
  HET_MeshOp.splitFacesTri(mesh, 40);
  mesh.smooth();
  mesh.getNewSelection("picked");
  tree=new WB_AABBTree(mesh, 4);
  render=new WB_Render3D(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(frameCount*0.005);
  stroke(0);
  render.drawEdges(mesh);
  fill(255);
  noStroke();
  render.drawFaces(mesh);

noLights();
  fill(255, 0, 0);
  render.drawFaces(mesh.getSelection("picked"));
  fill(0, 255, 0);
  HE_Face f=render.pickClosestFace(tree, mouseX, mouseY);
  if (f!=null) {
    render.drawFace(f);
  }
}


void mousePressed() {
  HE_Face f=render.pickClosestFace(tree, mouseX, mouseY);
  if (f!=null) {
    HE_Selection sel=mesh.getSelection("picked");
    if (sel.contains(f)) {
      sel.remove(f);
    } else {
      sel.add(f);
    }
  }
}

void keyPressed() {
  HE_Selection sel=mesh.getSelection("picked");
  sel.surround();
}
