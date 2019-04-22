import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import java.util.*;

int numseg;
RotatingSegment[] S;
float scale;
WB_Render3D render;
HE_Mesh mesh;
void setup() {
  size(1000,1000,P3D);
  smooth(8);
  render=new WB_Render3D(this); 

  S=new RotatingSegment[numseg];
  create();
  noFill();
}


void draw() {
  background(40); 
  translate(width/2,height/2);
  scale(scale);
  rotateX(mouseY*TWO_PI/height-PI);
  rotateY(mouseX*TWO_PI/width-PI);
  strokeWeight(2);
  for (int i=0;i<numseg;i++) {
    WB_Point c=S[i].S.getCenter();
    stroke(0);
    line(S[i].S.getOrigin().xf(), S[i].S.getOrigin().yf(), S[i].S.getOrigin().zf(), S[i].S.getEndpoint().xf(), S[i].S.getEndpoint().yf(), S[i].S.getEndpoint().zf()); 
  }  
  strokeWeight(2);
  ArrayList<WB_Segment> S2=new ArrayList<WB_Segment>();
  for (int i=0;i<numseg;i++) {
    for (int j=i+1;j<numseg;j++) {       
      WB_IntersectionResult is=WB_GeometryOp.getIntersection3D(S[i].S, S[j].S);
      if ((is.dimension==1)&&(is.sqDist<40000)&&(is.sqDist>4000)) {
        stroke(255, 0, 0, 255-0.005*(float)is.sqDist);
        WB_Segment seg=(WB_Segment)is.object;
        S2.add(seg);
        line(seg.getOrigin().xf(), seg.getOrigin().yf(), seg.getOrigin().zf(), seg.getEndpoint().xf(), seg.getEndpoint().yf(), seg.getEndpoint().zf());
      }
    }
  }
  strokeWeight(1);
  int nums2=S2.size();
  for (int i=0;i<nums2;i++) {
    for (int j=i+1;j<nums2;j++) {       
      WB_IntersectionResult is=WB_GeometryOp.getIntersection3D(S2.get(i), S2.get(j));
      if ((is.dimension==1)&&(is.sqDist<40000)&&(is.sqDist>4000)&&(is.t1>0.01)&&(is.t1<0.99)&&(is.t2>0.01)&&(is.t2<.99)) {
        stroke(255, 255-0.0075*(float)is.sqDist);
        WB_Segment seg=(WB_Segment)is.object;
        line(seg.getOrigin().xf(), seg.getOrigin().yf(), seg.getOrigin().zf(), seg.getEndpoint().xf(), seg.getEndpoint().yf(), seg.getEndpoint().zf());
      }
    }
  }
  for (int i=0;i<numseg;i++) {
    S[i].update();
  }
}

void mousePressed() {
  create();
}

void create() {
  
  int t=(int)random(1.0, 109.99999);
  if (t<=5) { 
    HEC_Plato pc=new HEC_Plato().setEdge(200).setType(t);
    mesh=new HE_Mesh(pc);
  }
  else if (t<=18) {
    HEC_Archimedes ac=new HEC_Archimedes().setEdge(200).setType(t-5);
    mesh=new HE_Mesh(ac);
  } 
  else {
    HEC_Johnson jc=new HEC_Johnson().setEdge(200).setType(t-18);
    mesh=new HE_Mesh(jc);
  }

  numseg=mesh.getNumberOfEdges();
  WB_Segment[] segments=mesh.getSegments();
  S=new RotatingSegment[numseg];
  WB_RandomOnSphere rs=new WB_RandomOnSphere();
  Iterator<HE_Halfedge> eItr=mesh.eItr();
  HE_Halfedge e;
  for (int i=0;i<numseg;i++) {
    e=eItr.next();
    S[i]=new RotatingSegment(segments[i], HE_MeshOp.getEdgeNormal(e), 0.01, 1);//random(-0.01, 0.01), 1);
  }
  WB_Coord v=segments[0].getOrigin();
  
  scale=250.0/(float)WB_Point.getLength3D(v);
}




class RotatingSegment {
  WB_Segment S;
  WB_Coord axis;
  WB_Point c;
  float rotVel;
  int count;
  int first;
  int num;
  WB_Coord[] history;
  WB_Coord start;
  WB_Coord end;

  RotatingSegment(WB_Segment S, WB_Coord axis, float rotVel, int num) {
    this.S=S;
    c=S.getCenter();
    this.axis=axis; 
    this.rotVel=rotVel;
    this.num=max(1, num);
    this.count=0;
    this.first=0;
    history=new WB_Point[2*num];
    history[0]=S.getOrigin();
    history[1]=S.getEndpoint();
    start=S.getOrigin();
    end=S.getEndpoint();
  }

  void update() {

    WB_Point o=new WB_Point(S.getOrigin());
    o.rotateAboutAxisSelf(rotVel, c, axis);
    WB_Point e=new WB_Point(S.getEndpoint());
    e.rotateAboutAxisSelf(rotVel, c, axis);
    S=new WB_Segment(o, e);
    count++;
    if (count>=num) {
      history[2*first]=o;
      history[2*first+1]=e;
      first=(first+1)%num;
    }
    else {
      history[2*count]=o;
      history[2*count+1]=e;
    }
  }

  void drawHistory() {
    float da=255.0/min(num, count);
    float dsw=2.0/min(num, count);

    beginShape();
    for (int i=0;i<min(num,count);i++) {
      int ci=(first+i)%num;
      strokeWeight(dsw*(i+1));
      stroke(0, da*(i+1));
      vertex(history[2*ci].xf(), history[2*ci].yf(), history[2*ci].zf());
    }
    endShape(OPEN);
    beginShape();
    for (int i=0;i<min(num,count);i++) {
      int ci=(first+i)%num;
      strokeWeight(dsw*(i+1));
      stroke(0, da*(i+1));
      vertex(history[2*ci+1].xf(), history[2*ci+1].yf(), history[2*ci+1].zf());
    }
    endShape(OPEN);
  }
}
