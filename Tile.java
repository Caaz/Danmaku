
import java.awt.*;
public class Tile {
  public boolean visible = true;
  public int sprite = 0; // Who knows if this will ever be used
  public Color color = new Color(255,255,255);
  //public String text = "";
  public int menuID = -1;
  public Tile() {
  }
  public void setColor(Color color) {
    this.color = color;
  }
  public void setID(int i) {
    menuID = i;
  }
}