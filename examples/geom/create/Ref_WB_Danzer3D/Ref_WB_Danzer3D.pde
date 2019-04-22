import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
import wblut.geom.WB_Danzer3D.WB_DanzerTile3D;

WB_Render3D render;
WB_Danzer3D danzerA, danzerB, danzerC, danzerK;

float scale;

void setup() {
  fullScreen(P3D);
  smooth(8);
  strokeWeight(1.4);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());
  scale=400;
  danzerA=new WB_Danzer3D(WB_Danzer3D.Type.A, scale, new WB_Point(-800, 220, 0));
  danzerA.inflate(3);
  danzerB=new WB_Danzer3D(WB_Danzer3D.Type.B, scale, new WB_Point(-325, 220, 0));
  danzerB.inflate(3);
  danzerC=new WB_Danzer3D(WB_Danzer3D.Type.C, scale, new WB_Point(150, 220, 0));
  danzerC.inflate(3);
  danzerK=new WB_Danzer3D(WB_Danzer3D.Type.K, scale, new WB_Point(475, 220, 0));
  danzerK.inflate(3);
}

void draw() {
  background(55);
  
  lights();
  translate(width/2, height/2, 0);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,PI,-PI));
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  drawDanzer(danzerA);
  drawDanzer(danzerB);
  drawDanzer(danzerC);
  drawDanzer(danzerK);
}


void drawDanzer(WB_Danzer3D danzer) {
  for (WB_DanzerTile3D tile : danzer.getTiles()) {
    switch(tile.getType()) {
    case A:
      fill(0, 255, 0);
      break;
    case B:
      fill(255, 255, 0);
      break;
    case C:
      fill(0, 0, 255);
      break;
    case K:
      fill(255, 0, 0);
      break;
    default:
      fill(255);
      break;
    }
    render.drawTetrahedron(tile);
  }
}