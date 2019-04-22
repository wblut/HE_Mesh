import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
float[][] fakeData;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  HEC_DataCylinder creator=new HEC_DataCylinder();
  creator.setRadius(150, 50);  
  creator.setHeight(400);
  readData();

  creator.setDataFromFloat(fakeData);
  creator.setCap(true, true);
  //creator.setSpiky(true);
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}

void readData() {
  fakeData=new float[10][20]; //10 categories, 20 data points each
  for (int cat=0; cat<10; cat++) {
    for (int point=0; point<20; point++) {
      fakeData[cat][point]=random(10, 150);
    }
  }
}