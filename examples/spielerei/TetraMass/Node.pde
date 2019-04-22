class Node {
  WB_Point pos; 
  float radiusfactor;
  Node parent;
  boolean isParent;
  int id;
  boolean active;
  int generation;

  Node(WB_Point pos,  float radiusfactor, Node parent, int id, int gen) { 
    this.pos = pos;
    this.parent = parent;
    this.radiusfactor=radiusfactor;
    this.id=id;
    active=true;
    generation=gen;
    isParent=false;
  }

  void draw(float r) {
    pushMatrix();
    translate(pos.xf(), pos.yf(), pos.zf());
    rotateX(-ax);
    rotateY(-ay);
    ellipse(0, 0, 2*radiusfactor*r, 2*radiusfactor*r);
    popMatrix();
  }
}

