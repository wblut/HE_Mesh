import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;

HE_Mesh mesh;
WB_Render render;

void setup() {
  size(1000,1000,P3D);
  smooth(8);
  
  WB_Point[] points=new WB_Point[121];
  int index = 0;
  for (int j = 0; j < 11; j++) {
    for (int i = 0; i < 11; i++) {
      points[index]=new WB_Point(-200+ i * 40+(((i!=0)&&(i!=10))?random(-20, 20):0),-200+j * 40+(((j!=0)&&(j!=10))?random(-20, 20):0),sin(TWO_PI/20*i)*40+cos(TWO_PI/10*j)*40);
      index++;
    }
  }
  
  //create quads from point grid
  WB_Quad[] quads=new WB_Quad[100];

  for(int i=0;i<10;i++){
    for(int j=0;j<10;j++){
     quads[i+10*j]=new WB_Quad(points[i+11*j],points[i+1+11*j],points[i+11*j+12],points[i+11*j+11]);
    }  
  }
  
  HEC_FromQuads creator=new HEC_FromQuads();
  
  creator.setQuads(quads);
  //alternatively quads can be any Collection<WB_Quad>
  mesh=new HE_Mesh(creator); 
  HET_Diagnosis.validate(mesh);
  render=new WB_Render(this);
}

void draw() {
  background(55);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);
  translate(width/2,height/2);
  rotateY(mouseX*1.0f/width*TWO_PI);
  rotateX(mouseY*1.0f/height*TWO_PI);
  stroke(0);
  render.drawEdges(mesh);
  noStroke();
  render.drawFaces(mesh);
}

