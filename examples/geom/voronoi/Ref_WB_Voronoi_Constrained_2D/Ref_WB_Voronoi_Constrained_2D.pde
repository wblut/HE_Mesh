import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

List<WB_Point> points;
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
  for (int i=0; i<10; i++) {
    for (int j=0; j<10; j++) {
      points.add(new WB_Point(-324+i*72, -324+j*72, 0));
    }
  }
  createBoundaryPolygon();
  voronoiXY= WB_VoronoiCreator.getClippedVoronoi2D(points, boundary,2).getCells();
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
  stroke(0);
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
  voronoiXY= WB_VoronoiCreator.getClippedVoronoi2D(points, boundary,2).getCells();
}

void createBoundaryPolygon(){
  ArrayList<WB_Point> shell;
  ArrayList<WB_Point>[] holes;
    shell= new ArrayList<WB_Point>();
  for (int i=0; i<20; i++) {
    shell.add(gf.createPointFromPolar(100+100*(i%2+1), TWO_PI/20.0*i));
  }
  //holes and points cannot overlap
  holes= new ArrayList[2];
  ArrayList<WB_Point> hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    hole.add(gf.createPointFromPolar(40*(i%2+1), -TWO_PI/10.*i).addSelf(-80, 0, 0));
  } 
  holes[0]=hole;
  hole=new ArrayList<WB_Point>();
  for (int i=0; i<10; i++) {
    hole.add(gf.createPointFromPolar(40*(i%2+1), PI-TWO_PI/10.*i).addSelf(80, 0, 0));
  } 
  holes[1]=hole;
  boundary=gf.createPolygonWithHoles(shell, holes);
  
}
