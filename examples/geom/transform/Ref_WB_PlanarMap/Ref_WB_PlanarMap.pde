import wblut.geom.*;
import wblut.processing.*;

WB_RandomPoint source;
WB_Render3D render;
WB_Point[] points;
int numPoints;

WB_PlanarMap mapToXY;
WB_PlanarMap mapToYZ;
WB_PlanarMap mapToXZ;
WB_PlanarMap mapToP;

WB_Triangulation2D triangulationOnXY;
WB_Triangulation2D triangulationOnYZ;
WB_Triangulation2D triangulationOnXZ;
WB_Triangulation2D triangulationOnP;

void setup() {
  size(1000, 1000, P3D);
  source=new WB_RandomOnSphere().setRadius(250);
  render=new WB_Render3D(this);
  numPoints=500;
  points=new WB_Point[numPoints];
  for (int i=0; i<numPoints; i++) {
    points[i]=source.nextPoint();
  }

  mapToXY=new WB_PlanarMap(WB_PlanarMap.XY, 270);
  mapToYZ=new WB_PlanarMap(WB_PlanarMap.YZ, 270);
  mapToXZ=new WB_PlanarMap(WB_PlanarMap.XZ, 270);
  WB_Plane plane=new WB_Plane(0, 0, 0, 1, 1, 1);
  mapToP=new WB_PlanarMap(plane);
  
  // WB_PlanarMap is a  coordinate system transform, it converts 3D world coordinates to local 3D coordinates in the system of a plane, and vice versa. 
  // This is for example useful to 2D triangulate a polygon with an arbitrary orientation in space. The origin of 3D space is mapped to the origin of the plane.
  // WB_PlanarMap is not the same as projection. WB_PlanarMap recalculates the point's coordinates (x,y,z) in the coordinate system of the plane, (u,v,w). The distance of
  // of the point to the plane is given by w. However projection moves the point to the plane discarding w (x,y,z)->(u,v,0).
  // Therefor, WB_PlanarMap is reversible while a projection is not.
  
  triangulationOnXY=WB_Triangulate.triangulate2D(points, mapToXY);
  triangulationOnYZ=WB_Triangulate.triangulate2D(points, mapToYZ);
  triangulationOnXZ=WB_Triangulate.triangulate2D(points, mapToXZ);
  triangulationOnP=WB_Triangulate.triangulate2D(points, mapToP);
}


void draw() {
  background(25);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2-50);
  rotateY(mouseX*1.0f/width*PI+QUARTER_PI);

  noFill();
  stroke(255);
  render.drawPoint(points, 2);
  stroke(255,0,255);

  //WB_PlanarMap is a example of a WB_Map, more specifically a WB_Map2D, a mapping intended to transform between
  //3D and 2D space.
  
  //Render functions labelled with "mapped" apply a WB_Map before rendering, by definition this will render
  //any WB_Map2D (x,y,z)->(u,v,0) to the XY-plane.
  //Render functions labelled "unmapped" do the reverse, unmapping a point before rendering it.
  //To show the results of a WB_Map2D embedded in 3D space, use render functions labelled with "embedded2D". This is a special case of
  // the "unmapped" functions that ignore the local z-coordinate (for example the elevation in a WB_Planar map).
  
  
  render.drawPointEmbedded2D(points, 2, mapToXY);
  render.drawTriangulationEmbedded2D(triangulationOnXY, points, mapToXY);
  stroke(255, 255, 0);
  render.drawPointEmbedded2D(points, 2, mapToYZ);
  render.drawTriangulationEmbedded2D(triangulationOnYZ, points, mapToYZ);
  stroke(0, 255, 255);
  render.drawPointEmbedded2D(points, 2, mapToXZ);
  render.drawTriangulationEmbedded2D(triangulationOnXZ, points, mapToXZ);
  stroke(255);
  render.drawPointEmbedded2D(points, 2, mapToP);
  render.drawTriangulationEmbedded2D(triangulationOnP, points, mapToP);
}