import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh box;
WB_Render render;

void setup() {
  size(600, 600,P3D);
  smooth();

  HEC_Box boxCreator=new HEC_Box().setWidth(400).setWidthSegments(2)
    .setHeight(200).setHeightSegments(1)
      .setDepth(200).setDepthSegments(1);
  boxCreator.setCenter(100, 100, 0).setZAxis(1, 1, 1);
  box=new HE_Mesh(boxCreator);
  
  //Subdividors work just like modifiers.
  HES_Planar planarSubdividor=new  HES_Planar();

  //Set parameters one by one ... 
  planarSubdividor.setRandom(true);

  //... or string them together
  planarSubdividor.setRange(0.8).setKeepTriangles(true);

  //Subdivide the mesh by calling a subdividor in a subdivide command
  box.subdivide(planarSubdividor);

  //Subdividors can be specified inline    
  box.subdivide(new HES_DooSabin());

  //Optionally call subdivide for multiple iterations
  box.subdivide(new HES_CatmullClark(), 2);

  render=new WB_Render(this);
}

void draw() {
  background(120);
  lights();
  translate(300, 300, 0);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(box);
  stroke(0);
  render.drawEdges(box);
}