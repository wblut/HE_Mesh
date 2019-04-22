import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

List<WB_Point> points;
List<WB_VoronoiCell3D> voronoi;

WB_Render3D render;
WB_GeometryFactory gf=new WB_GeometryFactory();
WB_AABB box;

void setup() {
  size(1000, 1000, P3D);
  smooth(8);
  render= new WB_Render3D(this);

  points=new ArrayList<WB_Point>();
  // add points to collection
  for (int i=0; i<6; i++) {
    for (int j=0; j<6; j++) {
      for (int k=0; k<6; k++) {
      points.add(new WB_Point(-200+i*80, -200+j*80 , -200+k*80));
      }
    }
  }
  box=new WB_AABB(-250,-250,-250,250,250,250);
  voronoi= WB_VoronoiCreator.getVoronoi3D(points,box,4).getCells();

  textAlign(CENTER);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  fill(0);
  text("click",0,350);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(255,0,0);
  strokeWeight(2);
  render.drawPoint(points, 1); 
  stroke(255,0,0,150);
  strokeWeight(1);
  noFill();
  for (WB_VoronoiCell3D vor : voronoi) {
    render.drawMesh(vor.getMesh());
  }
 
}


void mousePressed() {
  for (WB_Point p : points) {
    p.addSelf(random(-5, 5), random(-5, 5), random(-5, 5));
  } 
 voronoi= WB_VoronoiCreator.getVoronoi3D(points,box,4).getCells();
}
