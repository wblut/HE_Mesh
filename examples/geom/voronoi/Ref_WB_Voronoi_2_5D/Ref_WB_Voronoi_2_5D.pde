import wblut.processing.*;
import wblut.geom.*;
import java.util.List;

List<WB_Point> points;
WB_Polygon boundary;
List<WB_VoronoiCell2D> voronoiXY;
List<WB_Mesh> columns;

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
  createBoundaryPolygon();
  voronoiXY= WB_Voronoi.getClippedVoronoi2D(points, boundary,4);
  columns=new ArrayList<WB_Mesh>();
  for (WB_VoronoiCell2D vor : voronoiXY) {
    columns.add(gf.createPrism(vor.getPolygon(),random(50,500)));
  }
  
  
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
  stroke(0);
  strokeWeight(2);
  render.drawPoint(points, 1); 
  stroke(255,0,0);
  render.drawPolygonEdges(boundary);
  stroke(0);
  strokeWeight(1);
  
  for (WB_Mesh mesh : columns) {
    render.drawMeshEdges(mesh);
  }
  noStroke();
  fill(255);
  for (WB_Mesh mesh : columns) {
    render.drawMeshFaces(mesh);
  }

}


void mousePressed() {
  for (WB_Point p : points) {
    p.addSelf(random(-5, 5), random(-5, 5), 0);
  } 
  voronoiXY= WB_Voronoi.getClippedVoronoi2D(points, boundary,4);
  columns=new ArrayList<WB_Mesh>();
  for (WB_VoronoiCell2D vor : voronoiXY) {
    columns.add(gf.createPrism(vor.getPolygon(),random(50,500)));
  }
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