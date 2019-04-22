import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

WB_BinaryGrid2D grid;
WB_Render render;
int sizeX, sizeY;
float dX,dY;
void setup() {
  fullScreen(P3D);
  smooth(8);
  sizeX=100;
  dX=10.0;
  sizeY=50;
  dY=10.0;
  create(0.0);
  render=new WB_Render(this);
}

void create(float z){
  grid=WB_BinaryGrid2D.createGrid(new WB_Point(),sizeX,dX,sizeY,dY);
  for(int i=0;i<sizeX;i++){
     for(int j=0;j<sizeY;j++){
          if(noise(0.05*(i-sizeX*0.5+0.5),0.05*(j-sizeY*0.5+0.5),z)>0.5) grid.set(i,j);
     }
  }
  
}

void draw() {
  background(25);
  translate(width/2, height/2);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  noStroke();
  fill(55);
  render.drawBinaryGrid2D(grid);
  stroke(240);
  render.drawBinaryGridOutline2D(grid);
  create(frameCount*0.01);
}

void mousePressed(){
  noiseSeed((long)random(1000000));
 create(0.0); 
}