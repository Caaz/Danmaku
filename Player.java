public class Player extends Living {
  public int controls[] = {0,0,0,0,0,0,0,0}; // Up down left right shoot bomb pause
  public Player() {
  }
  public void resetControls() {
    for(int k = 0; k < controls.length; k++){
      controls[k] = 0;
    }
  }
}