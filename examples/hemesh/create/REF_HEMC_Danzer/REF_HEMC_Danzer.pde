import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;


HE_MeshCollection meshes;
WB_Render render;

void setup() {
 fullScreen(P3D);
  smooth(8);
  HEMC_Danzer danzer=new HEMC_Danzer();
  danzer.setOrigin(new WB_Point(0, 0, 0));
  danzer.setExtents(new WB_Vector(800,500, 300));
  danzer.setLevel(6);
 danzer.setScale(800);
 danzer.setSpacing(0.1);
  //danzer.setCrop(true);
  meshes=danzer.create();
  render=new WB_Render(this);
}


void draw() {
  background(55);

  translate(width/2,height/2);
  scale(1, -1, 1);
  rotateY(TWO_PI/width*mouseX-PI);
  rotateX(TWO_PI/height*mouseY-PI);
  noStroke();
  for(int i=0;i<meshes.size();i++){
     switch(meshes.getMesh(i).getInternalLabel()) {
    case 0:
      fill(0, 255, 0);
      break;
    case 1:
      fill(255, 255, 0);
      break;
    case 2:
      fill(0, 0, 255);
      break;
    case 3:
      fill(255, 0, 0);
      break;
    default:
      fill(255);
      break;
    }
  render.drawFaces(meshes.getMesh(i));
  }
  stroke(0);
  render.drawEdges(meshes);
}
