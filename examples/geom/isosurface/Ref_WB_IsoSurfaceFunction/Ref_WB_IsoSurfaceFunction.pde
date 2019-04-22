import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;

List<WB_Triangle> triangles;
WB_Render render;

void setup() {
  fullScreen(P3D);
  smooth(8);


  WB_IsoSurface creator=new WB_IsoSurface();
  creator.setSize(8, 8,8);
  //3D grids of values can take up a lot of memory, using a function
  //can decrease the memory use and allow larger grids. The resulting number
  //of triangles can still be a limiting factor for Processing though.
  creator.setValues(new ScalarField(),0.0,0.0,0.0,.1,.1,.1,128, 64,64);

  creator.setIsolevel(.6);
  creator.setInvert(false);
  creator.setBoundary(200);// value outside grid
  // use creator.clearBoundary() to rest boundary values to "no value".
  // A boundary value of "no value" results in an open mesh
  
  //Gamma controls level of grid snap, 0.0-0.5. Can improve the 
  //quality of the triangles, but can give small changes in topology.
  //For 3D, gamma=0.3 is a good value.
  creator.setGamma(0.3); 
  
  triangles=creator.getTriangles();
  render=new WB_Render(this);
}

void draw() {
  background(55);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  stroke(0);
  render.drawTriangle(triangles);
}

class ScalarField implements WB_ScalarParameter{
  double evaluate(double... x){
   return 2.5*sin((float)x[0])*cos((float)x[1])*sin((float)x[2])+4*noise(0.2*(float)x[0],0.4*(float)x[1],0.2*(float)x[2]); 
  }
}