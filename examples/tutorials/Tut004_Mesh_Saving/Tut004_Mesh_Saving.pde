import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh box;
HE_Mesh importbox;
WB_Render render;

void setup() {
  size(600, 600, P3D);
  smooth();

  HEC_Box boxCreator=new HEC_Box().setWidth(400).setWidthSegments(10)
    .setHeight(200).setHeightSegments(2)
      .setDepth(200).setDepthSegments(2);
  boxCreator.setCenter(100, 100, 0).setZAxis(1, 1, 1);
  box=new HE_Mesh(boxCreator);
  HEM_Wireframe wireframe=new HEM_Wireframe().setStrutRadius(6).setStrutFacets(6);
  box.modify(wireframe);

//Save mesh to file

  //Simple stereolithography file format, accepted by many 3D programs and 3D printers
  HET_Export.saveToSTL(box,sketchPath(),"box");
  
  //Basic Wavefront OBJ file format, accepted by many 3D programs and 3D printers
  HET_Export.saveToOBJ(box,sketchPath(),"box"); 
  
  
  //Vertices and indexed face list, connectivity information has to be rebuild on import;
  HET_Export.saveToSimpleMesh(box,sketchPath(),"box"); 
  
  //Stores all connectivity information, larger than simple mesh
  HET_Export.saveToHemesh(box,sketchPath(),"box"); 
  
  //Binary compressed version of hemesh
  HET_Export.saveToBinaryHemesh(box,sketchPath(),"box");
  
 //Each mesh file format has its corresponding creator.
 HEC_FromSimpleMeshFile fsmf=new HEC_FromSimpleMeshFile().setPath(sketchPath("box.mesh"));
 HEC_FromHemeshFile fhf=new HEC_FromHemeshFile().setPath(sketchPath("box.hemesh"));
 HEC_FromBinaryHemeshFile fbhf=new HEC_FromBinaryHemeshFile().setPath(sketchPath("box.binhemesh"));
 
 importbox=new HE_Mesh(fbhf);
 
 
render=new WB_Render(this);
}

void draw() {
  background(120);
  lights();
  translate(300, 300, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(importbox);
  stroke(0);
  render.drawEdges(box);
}