import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  noCursor();
  smooth(8);
  HEC_Cylinder creator=new HEC_Cylinder();
  double r=70;
  double R=150;
  double H=400;
  creator.setRadius(R,r); // upper and lower radius. If one is 0, HEC_Cone is called. 
  creator.setHeight(H);
  creator.setFacets(32).setSteps(5);
  creator.setCap(true,true);// cap top, cap bottom?
  //Default axis of the cylinder is (0,1,0). To change this use the HEC_Creator method setZAxis(..).
  //creator.setZAxis(0,1,1);
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  
  double h=H*r/(R-r);
  double hp=R*r/h;
  double hpp=r*r/h;
  double Rtop=Math.sqrt(r*r+hpp*hpp);
  double Rbottom=Math.sqrt(R*R+hp*hp);
  
  HEC_Sphere sc=new HEC_Sphere().setRadius(Rtop).setUFacets(32).setVFacets(16);sc.setCenter(0,0,200-hpp);
  mesh.add(new HE_Mesh(sc));
  sc=new HEC_Sphere().setRadius(Rbottom).setUFacets(32).setVFacets(16);sc.setCenter(0,0,-200-hp);
  mesh.add(new HE_Mesh(sc));
  
  
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
