import wblut.nurbs.*;
import wblut.hemesh.*;
import wblut.core.*;
import wblut.geom.*;
import wblut.processing.*;


WB_RBSpline P;
WB_BSpline T;
WB_Point[] points;
double[] weights;
HE_Mesh surface;
HE_Mesh aabb;
WB_Render render;
void setup() {
  size(1000, 1000, OPENGL);
smooth(8);
  points=new WB_Point[17];
  weights=new double[17];
  for (int i=0;i<17;i++) {
    points[i]=new WB_Point(random(50, 250), 0, -400+i*50); 
    weights[i]=random(1.0);
  }
  P=new WB_RBSpline(points, 4, weights);
  points=new WB_Point[11];
  weights=new double[11];
  for (int i=0;i<11;i++) {
    float a=i*0.080*PI;
    float r=1+random(-.5, .510);
    points[i]=new WB_Point(r*cos(a), r*sin(a), 0); 
    weights[i]=random(1.0);
  }
  T=new WB_RBSpline(points, 4, weights);

  WB_RBSplineSurface bsps=WB_NurbsFactory.getSwungSurface(P, T, 1);
  println(bsps.getPointOnSurface(0.5,0.5));
  surface=new HE_Mesh(new HEC_FromSurface().setSurface(bsps).setU(20).setV(10));


 surface=new HE_Mesh(new HEC_FromNetwork().setNetwork(surface).setConnectionRadius(6).setAngleOffset(0.5).setConnectionFacets(6));
  surface.subdivide(new HES_CatmullClark(),2);
  aabb=new HE_Mesh(new HEC_Box().setFromAABB(HE_MeshOp.getAABB(surface), 0));
  render=new WB_Render(this);
}

void draw() {
  background(55);
  translate(500, 500, 0);
  lights();
  rotateX(mouseY*TWO_PI/height-PI);
  rotateY(mouseX*TWO_PI/width-PI);
  stroke(0);
  //render.drawEdges(surface);
  render.drawEdges(aabb);
  noStroke();
  render.drawFaces(surface);
}
