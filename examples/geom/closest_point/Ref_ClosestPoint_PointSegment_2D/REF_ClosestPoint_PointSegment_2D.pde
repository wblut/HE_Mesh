import wblut.geom.*;
WB_Point p1;
WB_Point p2 ;
WB_Point p3;
WB_Point q;
WB_Segment S;


void setup() {
  size(800, 800);
  background(55); 
  p1 = new WB_Point(0, 0, 0);
  p2 = new WB_Point(100, 200, 0);
  p3 = new WB_Point(600, 500, 0);
  S=new WB_Segment(p2, p3);
}

void draw() {
  background(55); 
  stroke(0);
  line(p2.xf(), p2.yf(), p3.xf(), p3.yf());
  p1.set(mouseX, mouseY, 0);
  q=WB_GeometryOp.getClosestPoint2D(p1, S);
  stroke(255,0,0);
  ellipse(q.xf(), q.yf(), 10, 10);
  line(mouseX, mouseY, q.xf(), q.yf());
}