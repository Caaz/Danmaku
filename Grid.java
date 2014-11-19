public class Grid {
  public Tile array[][] = new Tile[10][8];
  public Grid() {
    // 
    for(int y = 0; y < getHeight(); y++) {
      for(int x = 0; x < getWidth(); x++) {
        array[x][y] = new Tile();
      }
    }
  }
  public int getWidth() { return 9; }
  public int getHeight() { return 7; }
  public Tile getTile(int x, int y) {
    Tile mytile = new Tile();
    try {
      //System.out.println("Actual tile returned");
      mytile = array[x][y];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      //System.out.println("Out of bounds tile");
    }
    finally {
      //System.out.println("Returning false tile");
      return mytile;
    }
  }
  public void makeHole() {
    for(int y = 0; y < getHeight(); y++) {
      for(int x = 0; x < getWidth(); x++) {
        if(((y == 1) || (y == 5)) && (x>=3) && (x<=6)) {
          array[x][y].visible = false; 
        }
        if(((y == 2) || (y == 4)) && (x>=2) && (x<=6)) { 
          array[x][y].visible = false; 
        }
        if((y == 3) && (x>=2) && (x<=7)) { 
          array[x][y].visible = false; 
        }
      }
    }
  }
  public void makeHolierHole() {
    for(int y = 0; y < getHeight(); y++) {
      for(int x = 0; x < getWidth(); x++) {
        if((x >= 1+y%2) && (x <= 5+y%2)) {
          array[x][y].visible = false; 
        }
      }
    }
  }
}