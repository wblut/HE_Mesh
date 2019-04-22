import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
void setup(){
 size(800,800,P3D);
 source=new WB_RandomCircle().setRadius(250);
 render=new WB_Render3D(this);
  
}


void draw(){
 background(55);
 directionalLight(255, 255, 255, 1, 1, -1);
 directionalLight(127, 127, 127, -1, -1, 1);
 translate(width/2, height/2, 0);
 rotateY(mouseX*1.0f/width*TWO_PI);
 rotateX(mouseY*1.0f/height*TWO_PI);

 for(int i=0;i<500;i++){
   render.drawPoint(source.nextPoint(),5);
 }
  
}