import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

WB_Render render;
List<WB_Tetrahedron> tetra;
void setup() {
  fullScreen(P3D);
  smooth(8);

  float[][][] values=new float[21][21][21];
  for (int i = 0; i < 21; i++) {
    for (int j = 0; j < 21; j++) {
      for (int k = 0; k < 21; k++) {
        values[i][j][k]=2.5*noise(0.07*i, 0.07*j, 0.07*k);
      }
    }
  }

  WB_IsoSurfaceVOL creator=new WB_IsoSurfaceVOL();
  creator.setSize(30,30,30);
  creator.setValues(values);
  creator.setIsolevel(0.9,1.1);
  tetra=creator.getTetrahedra();
  render=new WB_Render(this);
}

void draw() {
  background(25);
  lights();
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(255,0,0);
  for(int i=0;i<tetra.size();i++){
 pushMatrix();
 render.translate(tetra.get(i).getCenter().mul(0.2));
  render.drawTetrahedron(tetra.get(i));
  popMatrix();
  }

}