Rule REPLICATOR=new Rule("1357", "1357");
Rule SEEDS=new Rule("2", "");
Rule B25S4=new Rule("25", "4");
Rule LIFEWITHOUTDEATH=new Rule("3", "012345678");
Rule LIFE=new Rule("3", "23");
Rule LIFE34=new Rule("34", "34");
Rule DIAMOEBA=new Rule("35678", "5678");
Rule TWOBYWTO=new Rule("36", "125");
Rule HIGHLIFE=new Rule("36", "23");
Rule DAYANDNIGHT=new Rule("3678", "34678");
Rule MORLEY=new Rule("368", "245");
Rule ANNEAL=new Rule("4678", "35678");

class Rule {
  /* 
   https://en.wikipedia.org/wiki/Life-like_cellular_automaton
   
   Standard rules
   Any live cell with fewer than two live neighbours dies (referred to as underpopulation or exposure).
   Any live cell with more than three live neighbours dies (referred to as overpopulation or overcrowding).
   Any live cell with two or three live neighbours lives, unchanged, to the next generation.
   Any dead cell with exactly three live neighbours will come to life.
   
   Standard reformulation 
   if cell = 0 (dead), then cell becomes alive if 3 live neighbors: BIRTH, otherwise nothing happens
   if cell = 1 (alive), then cell remains alive if 2 or 3 neighbors: SURVIVE, otherwise nothing happens
   Notation:B3S23
   */

  boolean[] BIRTH;
  boolean[] SURVIVE;

  Rule() {
    this("B3","S23");
  }


  Rule(String _BIRTH, String _SURVIVE) {
   BIRTH=new boolean[9];
    for (int i=0; i<9; i++) {
      BIRTH[i]= (match(_BIRTH, str(i))!=null);
    }
    SURVIVE=new boolean[9];
    for (int i=0; i<9; i++) {
      SURVIVE[i]= (match(_SURVIVE, str(i))!=null);
    }
  }

  boolean newValue(boolean currentValue, int activeNeighbors) {
    if (currentValue) {
      if (SURVIVE[activeNeighbors]) {
        return true;
      } else {
        return false;
      }
    } else {
      if (BIRTH[activeNeighbors]) {
        return true;
      } else {
        return false;
      }
    }
  }
}