import wblut.geom.WB_GeometryFactory;
import wblut.geom.*;
import wblut.hemesh.*;
import wblut.processing.*;
import wblut.math.*;

WB_Segment[] S;
WB_Vector[] V;
int n;
WB_Render3D render;

void setup() {
  size(800,800,P3D);
  smooth(8);
  render=new WB_Render3D(this);
  background(55); 
  n=15;
  S=new WB_Segment[n];
  V=new WB_Vector[2*n];
  create();
  noFill();
  strokeWeight(2);
}

void create() {
  WB_RandomPoint pointGenerator=new WB_RandomBox().setSize(800,800,800);
  WB_RandomVector vectorGenerator=new WB_RandomOnSphere();
  for (int i=0; i<n; i++) {
    S[i]=new WB_Segment(pointGenerator.nextPoint(),pointGenerator.nextPoint());
    V[2*i]=vectorGenerator.nextVector();
    V[2*i+1]=vectorGenerator.nextVector();
  }
}


void draw() {
  background(55); 
  translate(width/2, height/2);
 rotateY(mouseX*1.0/width*TWO_PI-PI);
  rotateX(mouseY*1.0/height*TWO_PI-PI);

  WB_IntersectionResult is;
  for (int i=0; i<n; i++) {

    stroke(0);
    render.drawSegment(S[i]);
    stroke(255, 0, 0,100);
    for (int j=i+1; j<n; j++) {
      is=WB_GeometryOp.getClosestPoint3D(S[i], S[j]);   
      if(is.dimension==1){
      WB_Segment Sij=(WB_Segment)is.object;
      render.drawSegment(Sij);
      }
      if(is.dimension==0){
      WB_Point p=(WB_Point)is.object;
      render.drawPoint(p,5);
      }
    }
  }

  update();
  constrain();
}

void mousePressed() {
  create();
}



void update() {

  for (int i=0; i<n; i++) {
    S[i]=new WB_Segment(WB_Point.add(S[i].getOrigin(),V[2*i]), WB_Point.add(S[i].getEndpoint(),V[2*i+1]));
  }
}

void constrain() {
  WB_Point a;
  WB_Point b;
  for (int i=0; i<n; i++) {
    a=new WB_Point(S[i].getOrigin()); 
    b=new WB_Point(S[i].getEndpoint());
    if (a.xf()<-400) { 
      a.setX(-800-a.xf());
      V[2*i].setX(V[2*i].xf()*-1);
    }
    if (a.yf()<-400) { 
      a.setY(-800-a.yf());
      V[2*i].setY(V[2*i].yf()*-1);
    }
    if (a.zf()<-400) { 
      a.setZ(-800-a.zf());
      V[2*i].setZ(V[2*i].zf()*-1);
    }
    if (b.xf()<-400) { 
      b.setX(-800-b.xf());
      V[2*i+1].setX(V[2*i+1].xf()*-1);
    }
    if (b.yf()<-400) { 
      b.setY(-800-b.yf());
      V[2*i+1].setY(V[2*i+1].yf()*-1);
    }
    if (b.zf()<-400) { 
      b.setZ(-800-b.zf());
      V[2*i+1].setZ(V[2*i+1].zf()*-1);
    }
    if (a.xf()>400) { 
      a.setX(800-a.xf());
      V[2*i].setX(V[2*i].xf()*-1);
    }
    if (a.yf()>400) { 
      a.setY(800-a.yf());
      V[2*i].setY(V[2*i].yf()*-1);
    }
    if (a.zf()>400) { 
      a.setZ(800-a.zf());
      V[2*i].setZ(V[2*i].zf()*-1);
    }
    if (b.xf()>400) { 
      b.setX(800-b.xf());
      V[2*i+1].setX(V[2*i+1].xf()*-1);
    }
    if (b.yf()>400) { 
      b.setY(800-b.yf());
      V[2*i+1].setY(V[2*i+1].yf()*-1);
    }
    if (b.zf()>400) {
      b.setZ(800-b.zf());
      V[2*i+1].setZ(V[2*i+1].zf()*-1);
    }
    S[i]=new WB_Segment(a,b);
  }
}
