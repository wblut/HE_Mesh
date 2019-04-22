import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;

WB_Peak[] peaks;
void setup() {
  size(1000, 280, P3D);
  smooth(8);
  peaks=new WB_Peak[5];
  peaks[0]=WB_Peak.getPeakAbs(0.5);
  peaks[1]=WB_Peak.getPeakCos(0.5);
  peaks[2]=WB_Peak.getPeakSin(0.5);
  peaks[3]=WB_Peak.getPeakMin(0.5);
  peaks[4]=WB_Peak.getPeakMax(0.5);
  textAlign(CENTER);
  frameRate(30);
}

void draw() {
  background(55);
  strokeWeight(2);
  stroke(255, 0, 0);
  for (int i=0; i<5; i++) {
    peaks[i].setPower(2.5-2.0*cos(radians(frameCount)));
  }
  text("power="+String.format("%.2f", 2.5-2.0*cos(radians(frameCount))), 500, 260);
  translate(30, 120);
  scale(1, -1);
  for (int i=0; i<5; i++) {
    strokeWeight(1);
    stroke(0);
    line(0, -80, 180, -80);
    line(90, -90, 90, 90);
    strokeWeight(1);
    stroke(255, 0, 0);
    pushMatrix();
    translate(90, -80);
    for (int j=-80; j<80; j++) {
      line(j, (float)peaks[i].getValue(j*0.0125)*160, j+1, (float)peaks[i].getValue((j+1)*0.0125)*160);
    }
    popMatrix();
    translate(188, 0);
  }
}
