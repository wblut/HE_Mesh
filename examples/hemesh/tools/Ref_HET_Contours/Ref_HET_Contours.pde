import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;

HE_Mesh mesh;
WB_Render render;
List<WB_Segment> segments;

void setup() {
  fullScreen(P3D);
  smooth(8);
  createMesh();
  segments=HET_Contours.contours(mesh, new WB_Plane(0,0,0,1,0,0),-800,800,40);
  
  //This variations returns the contours as a List of connected curves (List of WB_Point), slower because
  //it has to sort through all segments and connect them.
  //List<List<WB_Coord>> contours=HET_Contours.contoursAsPaths(mesh, new WB_Plane(0,0,0,1,0,0),-400,400,20);
  
  render=new WB_Render(this);
}

void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(map(mouseX,0,width,-PI,PI));
  rotateX(map(mouseY,0,height,PI,-PI));

  stroke(255,0,0);
  render.drawSegment(segments);

}

void createMesh(){
  HEC_Torus creator=new HEC_Torus();
  creator.setRadius(60,300); 
  creator.setTubeFacets(16);
  creator.setTorusFacets(64);
  creator.setTwist(5);
  mesh=new HE_Mesh(creator); 
  mesh.modify(new HEM_Crocodile().setDistance(240));
  mesh.smooth();
}