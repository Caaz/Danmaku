public class Grid {
  Tile array[][] = new Tile[9][7];
  public Grid() {
    // 
  }
  public int getWidth() {
    return 9;
  }
  public int getHeight() {
    return 7;
  }
  public Tile getTile(int x, int y) {
    Tile tile;
    try {
      tile = array[x][y];
    }
    catch(ArrayIndexOutOfBoundsException e) {
      tile = new Tile();
    }
    return tile;
  }
}