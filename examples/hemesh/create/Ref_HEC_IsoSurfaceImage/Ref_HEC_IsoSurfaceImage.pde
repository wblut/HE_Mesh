import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
String[] images;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
 images=new String[360];
  for(int i=0;i<360;i++){
   images[i]=sketchPath("/data/square-"+index(i+1)+".png"); 
  }
  HEC_IsoSurface creator=new HEC_IsoSurface();
  creator.setSize(8, 8,8);
  creator.setValues(images,this,64,64,64);
  creator.setIsolevel(128);
  creator.setInvert(false);
  creator.setBoundary(-20000);// value outside grid
  // use creator.clearBoundary() to rest boundary values to "no value".
  // A boundary value of "no value" results in an open mesh
  
  //Gamma controls level of grid snap, 0.0-0.5. Can improve the 
  //quality of the triangles, but can give small changes in topology.
  creator.setGamma(0.3); 
 
  mesh=new HE_Mesh(creator);

  render=new WB_Render(this);
}

String index(int i){
  if(i<10) return "00"+ str(i);
  else if (i<100) return "0"+str(i);
  else return str(i);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
}