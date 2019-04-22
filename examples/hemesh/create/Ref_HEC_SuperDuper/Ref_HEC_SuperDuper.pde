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
  // This is bitcraft's super duper shape explorer in hemesh format.
  // http://www.openprocessing.org/visuals/?visualID=2638
  // http://www.k2g2.org/blog:bit.craft:superdupershape_explorer
  
  HEC_SuperDuper creator=new HEC_SuperDuper();
   creator.setU(512);
   creator.setV(32);
   creator.setUWrap(true); // needs to be set manually
   creator.setVWrap(false); // needs to be set manually
   creator.setRadius(50);
   
   //use any of these to set parameters
   //creator.setDonutParameters(0, 10, 10, 10, 5, 6, 12, 12,  3, 1);// parameters m1, n11, n12, n13, m2, n21, n22, n23, t, c
   //creator.setShellParameters(0, 10, 0, 0, 0, 10, 0, 0, 2, 1, 1, 5);// parameters m1, n11, n12, n13, m2, n21, n22, n23, t, d1, d2, c
  // creator.setSuperShapeParameters(4, 10, 10, 10, 4, 10,10, 10);// parameters m1, n11, n12, n13, m2, n21, n22, n23
   creator.setGeneralParameters(0, 11, 0, 0,13, 10, 15, 10, 4, 0, 0, 0, 5, 0.3, 2.2);// parameters m1, n11, n12, n13, m2, n21, n22, n23, t1, t2, d1, d2, c1, c2, c3
  // creator.setGeneralParameters(0, 10, 0, 0,6, 10, 6, 10, 3, 0, 0, 0, 4, 0.5, 0.25);
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
  noStroke();
  render.drawFaces(mesh);
}

