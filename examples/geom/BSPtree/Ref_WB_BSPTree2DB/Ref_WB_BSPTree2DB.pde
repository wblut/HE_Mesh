import wblut.processing.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.core.*;
import wblut.math.*;
import wblut.nurbs.*;
import java.util.List;


WB_Point[] points;
int numP;
WB_Polygon poly,star;
WB_BSPTree2D tree;
List<WB_Polygon> pos= new ArrayList<WB_Polygon>();
List<WB_Polygon> neg= new ArrayList<WB_Polygon>();
WB_GeometryFactory gf=new WB_GeometryFactory();
WB_Render3D render;
void setup() {

  size(800, 800, P3D);
  smooth(8);
  render=new WB_Render3D(this);
  background(0);
  createPolygon();
  tree=new WB_BSPTree2D();
  tree.build(poly);
  
}

void draw() {
  background(25);
  translate(400, 400, 0);
  pos= new ArrayList<WB_Polygon>();
  neg= new ArrayList<WB_Polygon>();
 starOnMouse();
 
 
  tree.partitionPolygon(star, pos,neg);
  strokeWeight(1.0);
  stroke(255);
  fill(255,50);
  render.drawPolygonEdges(poly);
  strokeWeight(4.0);
  stroke(0, 255, 0);
  fill(0,255,0,50);
  render.drawPolygonEdges(pos);
  stroke(255, 0, 0);
  fill(255,0,0,50);
  render.drawPolygonEdges(neg);

}


void starOnMouse(){
  List<WB_Point> shell;
  shell= new ArrayList<WB_Point>();

  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100*(i%2+1), TWO_PI/20.0*i).addSelf(mouseX-400,mouseY-400));
  } 
 star=gf.createSimplePolygon(shell); 
  
}

void createPolygon(){
  numP=38;
  points =new WB_Point[numP];
  points[0]=new WB_Point(-350, 200);
  for (int i=1; i<numP-7; i++) {
    points[i]=new WB_Point(-350+700.0/(numP-6)*i, -200+80*(int)random(5));
  }
  points[numP-7]=new WB_Point(350, 200);
  points[numP-6]=new WB_Point(200, 200);
  points[numP-5]=new WB_Point(200, 140);
  points[numP-4]=new WB_Point(0, 140);
  points[numP-3]=new WB_Point(0, 160);
  points[numP-2]=new WB_Point(100, 160);
  points[numP-1]=new WB_Point(100, 200);
  poly=new WB_Polygon(points);
}