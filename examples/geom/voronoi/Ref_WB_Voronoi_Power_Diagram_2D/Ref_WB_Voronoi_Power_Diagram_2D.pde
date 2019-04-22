import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

List<WB_Point> points;
double[] w;
WB_Polygon boundary;
List<WB_VoronoiCell2D> voronoiXY;

WB_Render3D render;
WB_GeometryFactory gf=new WB_GeometryFactory();

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render= new WB_Render3D(this);

  points=new ArrayList<WB_Point>();
  // add points to collection
  w=new double[100];
  int id=0;
  for (int i=0; i<10; i++) {
    for (int j=0; j<10; j++) {
      points.add(new WB_Point(-324+i*72, -324+j*72, 0));
      w[id++]=random(0.1,10.0);
    }
  }
  createBoundaryPolygon();
  voronoiXY= WB_VoronoiCreator.getPowerDiagram2D(WB_CoordCollection.getCollection(points),w, boundary).getCells();
  textAlign(CENTER);
  textSize(24);
}

void draw() {
  background(55);
  translate(width/2, height/2);
  noFill();
  stroke(0);
  strokeWeight(2);
  render.drawPoint(points, 1); 
  stroke(255,0,0);
  render.drawPolygonEdges(boundary);
  stroke(255,0,0);
  strokeWeight(1);
  for (WB_VoronoiCell2D vor : voronoiXY) {
    render.drawPolygonEdges(vor.getPolygon());
  }
  fill(255,0,0);
  text("click",0,450);
}


void mousePressed() {
  for (WB_Point p : points) {
    p.addSelf(random(-5, 5), random(-5, 5), 0);
  } 
  voronoiXY= WB_VoronoiCreator.getPowerDiagram2D(WB_CoordCollection.getCollection(points),w, boundary).getCells();
}

void createBoundaryPolygon(){
  ArrayList<WB_Point> shell;
  ArrayList<WB_Point>[] holes;
    shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(200, TWO_PI/20.0*i));
  }
  boundary=gf.createSimplePolygon(shell);
  
}
