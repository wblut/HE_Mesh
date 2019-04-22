import wblut.geom.*;
import wblut.processing.*;

WB_Point q1;
WB_Point q2;
WB_Line L1;
WB_Line L2;
WB_IntersectionResult i;
WB_Render3D render;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  background(55); 
  create(); 
  render=new WB_Render3D(this);
  strokeWeight(2);
}

void create() {
  L1=new WB_Line(new WB_Point(random(-200, 200), random(-200, 200), random(-200, 200)), new WB_Vector(random(-200, 200), random(-200, 200), random(-200, 200)));
  L2=new WB_Line(new WB_Point(random(-200, 200), random(-200, 200), random(-200, 200)), new WB_Vector(random(-200, 200), random(-200, 200), random(-200, 200)));
  i=WB_GeometryOp.getClosestPoint3D(L1, L2);
}

void draw() {
  background(55); 
  translate(width/2,height/2);
  rotateX(mouseY*1.0/height*TWO_PI-PI);
  rotateY(mouseX*1.0/width*TWO_PI-PI);
  stroke(0);
  
  render.drawLine(L1, 4000);
  render.drawLine(L2, 4000);
   stroke(255,0,0);
  if (i.dimension==1) {
    WB_Coord i1=((WB_Segment)i.object).getOrigin();
    WB_Coord i2=((WB_Segment)i.object).getEndpoint();
    
    render.drawPoint(i1, 10);
    render.drawPoint(i2, 10);
    render.drawSegment(i1, i2);
  } else {
    WB_Point i1=(WB_Point)i.object;
    render.drawPoint(i1, 10);
  }
}

void mousePressed() {
 create();
}