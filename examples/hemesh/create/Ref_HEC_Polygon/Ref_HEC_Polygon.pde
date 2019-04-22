import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
WB_ProgressReporter pr;
HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  //create base points for a single hexagon
  WB_Point[] basepoints =new WB_Point[6];
  for (int i=0;i<6;i++) {
   basepoints[i]=new WB_Point(0,200,0);
   if(i>0) basepoints[i].rotateAboutAxisSelf(Math.PI/3.0*i,0,0,0,0,0,1);
  }
  
  //create polygon from base points, HEC_Polygon assumes the polygon is planar
  WB_Polygon polygon=new WB_Polygon(basepoints);
 
  HEC_Polygon creator=new HEC_Polygon();
  
  creator.setPolygon(polygon);//alternatively polygon can be a WB_Polygon2D
  creator.setThickness(50);// thickness 0 creates a surface
  
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}