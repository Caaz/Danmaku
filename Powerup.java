import java.awt.*;
public class Powerup extends Living {
  int type = 0; // Power, Points, Bomb, Life, Switch Weapon
  double hitbox = 20;
  double size = 20;
  boolean living = false;
  final Color colors[][] = {
    // Power
    {
      new Color(75,0,0),      // Outline
      new Color(240,0,0),     // Background
    },
    {
      new Color(0,0,75),      // Outline
      new Color(0,0,240),     // Background
    },
    {
      new Color(0,75,0),      // Outline
      new Color(0,240,0),     // Background
    },
    {
      new Color(75,75,0),      // Outline
      new Color(240,240,0),     // Background
    },
    {
      new Color(0,75,75),      // Outline
      new Color(0,240,240),     // Background
    },
  };
  public Powerup(float[] position, int type) {
    for(int i = 0; i < 2; i++) { this.position[i] = position[i]; }
    this.type = type;
    living = true;
  }
  public Powerup(float[] position) {
    this.position = position;
    type = (int)(Math.random()*5);
    living = true;
  }
  public void update(long time) {
    position[1] += 1;
    if(position[1] >= 520) { die(); }
  }
  public void die() {
    living = false;
  }
  public void draw(Graphics2D g2d, int[] screen, View helper) {
    float scale = (float)(screen[1]/500.0*size/16.0);
    g2d.translate(position[0]/500*screen[1],position[1]/500*screen[1]);
    int shape[][] = {{-7,-6,6,7,7,6,-6,-7},{-6,-7,-7,-6,6,7,7,6}};
    helper.drawPolygon(g2d,shape,scale,colors[type][0], colors[type][1]);
    g2d.translate(-(position[0]/500*screen[1]),-(position[1]/500*screen[1]));
  }
}