import wblut.nurbs.*;
import wblut.hemesh.*;
import wblut.geom.*;
import wblut.core.*;
import wblut.processing.*;
import wblut.math.*;
import java.util.List;

double theta,phi,r;

List<WB_Point> points;
WB_Render3D render;
WB_GeometryFactory gf;

void setup() {
  fullScreen(P3D);
  smooth(8);
  points=new ArrayList<WB_Point>();
  render=new WB_Render3D(this);
  gf=new WB_GeometryFactory();
  //Get spherical theta and phi angles evenly distributed over the sphere
  //Randomly selecting theta [0,PI) and phi [0,TWO_PI) gives higher density at the poles
  WB_PointGenerator rp=new WB_RandomSpherical();
  for (int i=0; i<10000; i++) {
    WB_Coord p=rp.nextPoint();
    theta =p.xd();
    phi=p.yd();
    int l=2;
    for (int m=-l; m<=l; m++) {
      r=WB_SphericalHarmonics.Ylm(l, m, theta, phi);
      r*=r;//most commonly real spherical harmonics are used squared
      r*=1200.0;
      points.add(gf.createPointFromSpherical(r, theta, phi));
      
    }
  }
}

void draw() {
  background(25); 
  stroke(255,150);
  translate(width/2, height/2);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(map(mouseY, 0, height, -PI, PI));
  for (WB_Point p : points) {
    render.drawPoint(p);
  }
}
