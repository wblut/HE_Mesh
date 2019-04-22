import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;

WB_Noise n1, n2, n3;
void setup() {
  size(800, 800);
 
  //Perlin Noise, implementation from Processing core
  n1=new WB_PNoise().setDetail(8,0.5);
  n1.setScale(0.01);
  
  //Simplex noise
  n2=new WB_SNoise();
  n2.setScale(0.01);
  
  //Open simplex noise
  n3=new WB_OSNoise();
  n3.setScale(0.01);
}

void draw() {
  background(55);
  strokeWeight(2);
  stroke(255,0,0);
  for (int i=0; i<width; i++) {
    point(i, 200-100*(2.0*(float)n1.value1D(i+frameCount)-1.0));
    point(i, 400-100*(float)n2.value1D(i+frameCount));
    point(i, 600-100*(float)n3.value1D(i+frameCount));
  }
  strokeWeight(1);
  stroke(0);
  line(0, 200, width, 200);
  line(0, 400, width, 400);
  line(0, 600, width, 600);
}
