
import java.awt.*;
public class Tile {
  public boolean visible = true;
  public int sprite = 0; // Who knows if this will ever be used
  public Color color = new Color(255,255,255);
  public String text = "";
  public Tile() {
  }
  public void setColor(Color color) {
    this.color = color;
  }
  public void setText(String text) {
    this.text = text;
  }
  public boolean hasText() {
    if(text.length() > 0) {
      return true;
    }
    return false;
  }
}