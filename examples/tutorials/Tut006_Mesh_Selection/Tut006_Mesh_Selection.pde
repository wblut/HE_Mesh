import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_Mesh box;
WB_Render render;
void setup() {
  size(800, 800, P3D);
  smooth(8);

  HEC_Box boxCreator=new HEC_Box().setWidth(400).setWidthSegments(10)
    .setHeight(200).setHeightSegments(4)
      .setDepth(200).setDepthSegments(4);
  boxCreator.setCenter(100, 100, 0).setZAxis(1, 1, 1);
  box=new HE_Mesh(boxCreator);

  //define a selection
  HE_Selection selection=new HE_Selection(box);  

  //add faces to selection
  HE_FaceIterator fItr=box.fItr();
  HE_Face f;
  while (fItr.hasNext ()) {
    f=fItr.next();
    if (random(100)<10) selection.add(f);
  }


  HEM_Extrude extrude=new HEM_Extrude().setDistance(100);

  //only modify selection (if applicable)
  selection.modify(extrude);
  
 
  HE_Selection newSelection=box.getSelection("walls");
  extrude.setDistance(-10).setChamfer(0.4);
  newSelection.modify(extrude);
  
  render=new WB_Render(this);
}

void draw() {
  background(25);
  lights();
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(box);
  stroke(0);
  render.drawEdges(box);
}