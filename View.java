/*
  All drawing should be handled here.
  Hopefully this'll help in organization.
*/
import java.awt.*;
public class View {
  private final Color COLORS[] = {
    new Color(0,0,0),       // 0  Black
    new Color(255,255,255), // 1  White
    new Color(0,0,255),     // 2  Blue
  };
  public Polygon getHex(int size) {
    Polygon p = new Polygon();
    p.addPoint(0*size,2*size);
    p.addPoint(4*size,0*size);
    p.addPoint(8*size,2*size);
    p.addPoint(8*size,7*size);
    p.addPoint(4*size,9*size);
    p.addPoint(0*size,7*size);
    return p;
  }
  public void draw(Game game, Graphics2D g2d) {
    Dimension res = game.getSize();
    g2d.setBackground(COLORS[0]);
    g2d.setColor(COLORS[0]);
    g2d.fillRect(0,0,res.width,res.height);    
    if(game.state == 0) { drawKeyConfig(game,g2d); }
    else if (game.state == 1) {
      // Menu
      g2d.setColor(COLORS[2]);
      Polygon hex = getHex(10);
      size = hex.getBounds();
      width = size.width();
      for(int i = 0; i < 20; i++) {
        translate(width,0);
        g2d.drawPolygon(hex);
      }
      translate(-width,0);
    }
    // Draw background
    // Draw Bullets
    // Draw Enemies
    // Draw Player
    // Draw Overlay
  }
  public void drawKeyConfig(Game game, Graphics2D g2d) {
    int items = game.menu.getLength();
    // Figure out sizes
    Dimension res = game.getSize();
    float line = res.height/(items*2+1);
    g2d.setFont(new Font("Monospaced", Font.PLAIN, (int)line));
    
    // This is all kinds of fucking annoying
    g2d.translate(100,line*2);
    for(int i = 0; i < items; i++) {
      if(i == game.menu.selected) { g2d.setColor(COLORS[2]); }
      else { g2d.setColor(COLORS[1]); }
      g2d.drawString(game.menu.get(i),0,0);
      g2d.translate(0,line*2);
    }
    g2d.translate(-100,-(line*((items+1)*2)));
  }
}