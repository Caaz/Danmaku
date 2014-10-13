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
    // Gets Actual screen dimensions.
    Dimension res = game.getSize();
    // Fill it black.
    g2d.setBackground(COLORS[0]);
    g2d.setColor(COLORS[0]);
    g2d.fillRect(0,0,res.width,res.height); 
    
    // Virtual Screen dimensions. (This will always be 16:9 ratio)
    int screen[] = new int[2];
    screen[0] = res.width; screen[1] = (screen[0]/16)*9;
    if(screen[1] > res.height) { screen[1] = res.height; screen[0] = (screen[1]/9)*16; }
    
    // Get the size of the padding on the edge of the screen after 16:9 ratio has been achieved.
    // Used for letterboxing.
    int margin[] = new int[2];
    margin[0] = (res.width-screen[0])/2;
    margin[1] = (res.height-screen[1])/2;
    
    // Draw screen.
    // g2d.setColor(COLORS[2]);
    // g2d.fillRect(margin[0],margin[1],screen[0],screen[1]);
    
    // Translate to the screen's position
    g2d.translate(margin[0],margin[1]);
    
    if(game.state == 0) { drawKeyConfig(game,g2d,screen); }
    else if (game.state == 1) {
      // Menu
      int size = 1*(screen[1]/100);
      g2d.setColor(COLORS[1]);
      Polygon hex = getHex(size);
      Rectangle bounds = hex.getBounds();
      float hexes[] = new float[2];
      hexes[0] = (float)bounds.getWidth();
      hexes[1] = (float)bounds.getHeight();
      int hexX = (screen[0]/(int)hexes[0])+1;
      int hexY = (screen[1]/(int)(hexes[1]-(hexes[1]/9*2)))+1;
      boolean offset = false;
      int origin[] = new int[2];
      for(int i = 0; i < hexY; i++) {
        for(int j = 0; j < hexX; j++) {
          g2d.drawPolygon(hex);
          g2d.translate(hexes[0],0);
          origin[0] += hexes[0];
        }
        origin[0] -= hexes[0] * hexX;
        origin[1] += hexes[1]-(hexes[1]/9*2);
        g2d.translate(-hexes[0]*hexX,hexes[1]-(hexes[1]/9*2));
        if(offset) { g2d.translate(hexes[0]/2,0); offset = false; }
        else { g2d.translate(-hexes[0]/2,0); offset = true; }
      }
      if(offset) { g2d.translate(hexes[0]/2,0); }
      g2d.translate(-origin[0],-origin[1]);
    }
    // Draw letterbox
    g2d.translate(-margin[0],-margin[1]);
    g2d.setColor(COLORS[2]);
    g2d.fillRect(0,0,margin[0],screen[1]);
    g2d.fillRect(0,0,screen[0],margin[1]);
    g2d.translate(res.width,res.height);
    g2d.fillRect(0,0,-margin[0],-screen[1]);
    g2d.fillRect(0,0,-screen[0],-margin[1]);
    // Draw Bullets
    // Draw Enemies
    // Draw Player
    // Draw Overlay
  }
  public void drawKeyConfig(Game game, Graphics2D g2d, int[] screen) {
    int items = game.menu.getLength();
    // Figure out sizes
    float line = screen[1]/(items*2+1);
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