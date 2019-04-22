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
  HEC_UVParametric creator=new  HEC_UVParametric();
  creator.setUVSteps(40, 40);
  creator.setScale(100); //scaling factor
  creator.setEvaluator(new UVFunction());// expects an implementation of the WB_Function2D<WB_Point3d> interface, taking u and v from 0 to 1

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
  stroke(0);
  render.drawEdges(mesh);
}

class UVFunction implements WB_VectorParameter {
  WB_Point evaluate(double... u) {
    double pi23=2*Math.PI/3;
    double ua=Math.PI*2*u[0];
    double va=Math.PI*2*u[1];
    double sqrt2=Math.sqrt(2.0d);
    double px = Math.sin(ua) / Math.abs(sqrt2+ Math.cos(va));
    double py = Math.sin(ua+pi23) / Math.abs(sqrt2 +Math.cos(va + pi23));
    double pz = Math.cos(ua-pi23) / Math.abs(sqrt2 +Math.cos(va - pi23));
    return new WB_Point(px, py, pz);
  }
}