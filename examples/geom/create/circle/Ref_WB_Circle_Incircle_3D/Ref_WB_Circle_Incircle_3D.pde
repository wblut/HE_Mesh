import wblut.core.*;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.math.*;
import wblut.processing.*;
import java.util.List;

HE_Mesh mesh;
WB_Render3D render;
List<WB_Circle> circles;
WB_GeometryFactory gf;

void setup() {
  size(800,800, P3D);  
  smooth(8);
  render=new WB_Render3D(this);
  gf=new WB_GeometryFactory();

  mesh=new HE_Mesh(new HEC_Beethoven());
  mesh.scaleSelf(8);

  HE_FaceIterator fItr=mesh.fItr();
  HE_Face f;
  WB_Triangle T;
  circles=new ArrayList<WB_Circle>();
  while (fItr.hasNext()) {
    f=fItr.next();
    T=new WB_Triangle(f.getHalfedge().getVertex(), f.getHalfedge().getNextInFace().getVertex(), f.getHalfedge().getPrevInFace().getVertex());
    circles.add(gf.createIncircle(T));
  }
}

void draw() {
  background(55);
  translate(width/2, height/2);
  pointLight(204, 204, 204, 1000, 1000, 1000);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  noFill();
  stroke(255, 0, 0);
  for (WB_Circle C : circles) {
    render.drawCircle(C);
  }
  fill(255);
  noStroke();
  render.drawFaces(mesh);
}