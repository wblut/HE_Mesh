import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.opengl.*;

HE_Mesh mesh;
WB_Render render;
HEC_IsoSurface creator;
void setup() {
  size(1000,1000,P3D);
  smooth(8);


  creator=new HEC_IsoSurface();
  creator.setSize(8, 8,8);
  //3D grids of values can take up a lot of memory, using a function
  //can decrease the memory use, allow larger grids. The resulting number
  //of triangles can still be a limiting factor for Processing though.
  creator.setValues(new ScalarField(),0.0,0.0,0.0,.1,.1,.1,64, 64,64);//scalar field function, origin, stepsize, resolution

  creator.setIsolevel(.36);
  creator.setInvert(false);
  creator.setBoundary(100000);
  //Gamma controls level of grid snap, 0.0-0.5. Can improve the 
  //quality of the triangles, but can give small changes in topology.
  creator.setGamma(0.3); 
  

  mesh=new HE_Mesh(creator);
 mesh.modify(new HEM_HideEdges().setThresholdAngle(radians(.1)));
  render=new WB_Render(this);
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

class ScalarField implements WB_ScalarParameter{
  double evaluate(double... x){
   return noise(0.35*(float)(x[0]-3.2),0.35*(float)(x[1]-3.2),0.35*(float)(x[2]-3.2)); 
  }
}
