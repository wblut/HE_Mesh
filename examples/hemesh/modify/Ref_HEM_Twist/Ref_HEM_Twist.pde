import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;
WB_Line L;
HEM_Twist modifier;
void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  createMesh();
  
  modifier=new HEM_Twist();
  
 
 L=new WB_Line(0,0,0,1,1,0);
  modifier.setTwistAxis(L);// Twist axis
  //you can also pass the line as two points:  modifier.setTwistAxisFromPoints(new WB_Point(0,0,-200),new WB_Point(1,0,-200))
  //or as a point and a direction :  modifier.setTwistAxis(new WB_Point(0,0,-200),new WB_Vector(0,0,1))
  
  modifier.setAngleFactor(.51);// Angle per unit distance (in degrees) to the twist axis
  // points which are a distance d from the axis are rotated around it by an angle d*angleFactor°;
  
  mesh.modify(modifier);
  
  render=new WB_Render(this);
}

void draw() {
  background(120);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);

  fill(255);
  noStroke();
  render.drawFaces(mesh);
  stroke(0);
  render.drawEdges(mesh);
  stroke(255,0,0);
  render.drawLine(L,800);
  
  
  createMesh();
  modifier.setTwistAxis(L);
  modifier.setAngleFactor(mouseX*0.001);
  mesh.modify(modifier);
}


void createMesh(){
  HEC_Box creator=new HEC_Box(300,300,300,30,30,30);
  mesh=new HE_Mesh(creator);

}