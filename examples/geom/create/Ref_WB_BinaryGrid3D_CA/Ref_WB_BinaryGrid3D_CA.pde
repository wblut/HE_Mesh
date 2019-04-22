import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

WB_BinaryGrid3D grid;
WB_Render render;
int sizeX, sizeY, sizeZ;
float dX, dY, dZ;
int currentK;
HE_Mesh mesh1,mesh2;
void setup() {
  fullScreen(P3D);
  smooth(8);
  noCursor();
  sizeX=23;
  dX=50.0;
  sizeY=23;
  dY=25.0;
  sizeZ=80;
  dZ=5.0;
  grid=WB_BinaryGrid3D.createGrid(new WB_Point(), sizeX, dX, sizeY, dY, sizeZ, dZ);
  currentK=0;
  for (int i=0; i<sizeX; i++) {
    for (int j=0; j<sizeY; j++) {
      if(random(100.0)<50)grid.set(i, j, currentK);
    }
  }
  mesh1=new HE_Mesh(new HEC_Box(3000,800,10,1,1,1).setCenter(500,0,-210));
  mesh2=new HE_Mesh(new HEC_Box(1150,575,5,1,1,1).setCenter(0,0,-202.5));
  render=new WB_Render(this);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(255,255,235, -1, -1, 1);
  directionalLight(127,127,155, -1, 1, -1);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  fill(255);
  noStroke();
  render.drawBinaryGrid3D(grid);
  render.drawFaces(mesh2);
  fill(125);
  render.drawFaces(mesh1);
  stroke(0);
  render.drawBinaryGridOutline3D(grid);
  render.drawEdges(mesh1);
  render.drawEdges(mesh2);
  update();
}

void update() {
  int newK=(currentK+1)%sizeZ;
   Rule rule=LIFE34;
  for (int i=0; i<sizeX; i++) {
    for (int j=0; j<sizeY; j++) {
      grid.clear(i, j, newK);
      int sum=-(grid.get(i, j, currentK)?1:0);
      for (int di=-1; di<=1; di++) {
        for (int dj=-1; dj<=1; dj++) {
          sum+=(grid.get(i+di, j+dj, currentK)?1:0);
          
        }
      }
     
      if(rule.newValue(grid.get(i, j, currentK),sum)) grid.set(i, j, newK);
    }
  }
  currentK=newK;
}

void keyPressed(){
   grid=WB_BinaryGrid3D.createGrid(new WB_Point(), sizeX, dX, sizeY, dY, sizeZ, dZ);
  currentK=0;
  for (int i=0; i<sizeX; i++) {
    for (int j=0; j<sizeY; j++) {
      if(random(100.0)<50)grid.set(i, j, currentK);
    }
  }
  
}