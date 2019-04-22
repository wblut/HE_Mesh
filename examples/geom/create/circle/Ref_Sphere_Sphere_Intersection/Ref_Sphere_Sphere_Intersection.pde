import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.nurbs.*;
import wblut.processing.*;
import java.util.List;
/*
Draw sphere-sphere intersection circles
 Demonstrates 3D circles in HE_Mesh
 */


WB_Render3D render;
WB_RandomPoint generator;
List<WB_Circle> circles;
List<WB_Sphere> spheres;
int numSpheres;
float radius;

void setup() {
  fullScreen(P3D);
  ortho();
  smooth(8);
  strokeWeight(2);
  noFill();
  render=new WB_Render3D(this);
  println(WB_Version.version());
  println(WB_Disclaimer.disclaimer());

  radius=300;
  generator=new WB_RandomOnSphere().setRadius(radius);
  numSpheres=60;
  spheres=new ArrayList<WB_Sphere>();
  spheres.add(new WB_Sphere(new WB_Point(), radius));
  for (int i=1; i<numSpheres; i++) {
    spheres.add(new WB_Sphere(generator.nextPoint(), random(20, 100)));
  }

  circles=new ArrayList<WB_Circle>();
  for (int i=0; i<numSpheres; i++) {
    for (int j=i+1; j<numSpheres; j++) {
      WB_IntersectionResult is=WB_GeometryOp.getIntersection3D(spheres.get(i), spheres.get(j));
      if (is.intersection && is.dimension==2) circles.add((WB_Circle)is.object);
    }
  }
}

void draw() {
  background(55);
  translate(width/2, height/2, 0);
  rotateY(map(mouseX, 0, width, -PI, PI));
  rotateX(map(mouseY, 0, height, PI, -PI));
  strokeWeight(2);
  stroke(255,0,0);
  for (WB_Circle C : circles) {
    render.drawCircle(C);
  }
  strokeWeight(1);
  stroke(0,120);
   for (WB_Sphere S : spheres) {
     pushMatrix();
     render.translate(S.getCenter());
    sphere((float)S.getRadius());
    popMatrix();
  }
  
}