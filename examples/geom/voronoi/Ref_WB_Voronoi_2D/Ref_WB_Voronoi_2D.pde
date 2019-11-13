import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

List<WB_Point> points;
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
      points.add(new WB_Point(-270+i*60, -270+j*60, 0));
    }
  }
 

  voronoiXY= WB_VoronoiCreator.getVoronoi2D(points,2).getCells();
  textAlign(CENTER);
}

void draw() {
  background(55);
  translate(width/2, height/2);
 
  noFill();
  stroke(240,0,0);
  strokeWeight(2);
  render.drawPoint(points, 1); 
  strokeWeight(1);

  for (WB_VoronoiCell2D vor : voronoiXY) {

    render.drawPolygonEdges(vor.getPolygon());
  }
  fill(0);
  text("click",0,350);
}


void mousePressed() {
  for (WB_Point p : points) {
    p.addSelf(random(-5, 5), random(-5, 5), 0);
  } 
  voronoiXY= WB_VoronoiCreator.getVoronoi2D(points,2).getCells();
}
