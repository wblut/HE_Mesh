import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

WB_Render render;
List<WB_Triangle> triangles;
void setup() {
  fullScreen(P3D);
  smooth(8);
  float[][] values=new float[81][51];
  for (int i = 0; i < 81; i++) {
    for (int j = 0; j < 51; j++) {
      values[i][j]=2.5*cos(4.0*TWO_PI*0.0125*i)*cos(2.0*TWO_PI*0.02*j)+4.0*(0.5-noise(i*0.025,j*0.025));
    }
  }
  WB_IsoSurfaceVOL2D creator=new WB_IsoSurfaceVOL2D();
  creator.setSize(12, 12);
  creator.setValues(values);
  creator.setIsolevel(-0.5, 0.5);
  creator.setZFactor(25.0);
  
  triangles=creator.getTriangles();
  render=new WB_Render(this);
}

void draw() {
  background(25);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  fill(255);
  render.drawTriangle(triangles);
}