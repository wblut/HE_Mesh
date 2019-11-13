import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.List;
HE_Mesh mesh, mesh2;
List<WB_Point> points;
WB_Render render;
WB_BSPTree3D tree;
void setup() {
  fullScreen(P3D);
  smooth(8);
  render=new WB_Render(this);
  mesh=new HE_Mesh(new HEC_Icosahedron().setRadius(200)); 
  mesh.modify(new HEM_Crocodile().setDistance(200));
  tree=new WB_BSPTree3D();
  tree.build(mesh);
  points=new ArrayList<WB_Point>();
  WB_PointFactory rp=new WB_RandomInSphere().setRadius(350);
  for (int i=0; i<25000; i++) {
    points.add(rp.nextPoint());
  }
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2, height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  for (WB_Point p : points) {
    if (tree.pointLocation(p)>0) {
      stroke(255, 0, 0);
      render.drawPoint(p);
    } else {
      stroke(0, 255, 0);
      render.drawPoint(p);
    }
  }
}
