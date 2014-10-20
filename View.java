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
  public void drawHex(Graphics2D g2d, int width, int height) {
    Polygon p = new Polygon();
    double x = 8.0;
    double y = 9.0;
    double scaleX = (double)width;
    double scaleY = (double)height;
    p.addPoint((int)(0.0/x*scaleX),(int)(2.0/y*scaleY));
    p.addPoint((int)(4.0/x*scaleX),(int)(0.0/y*scaleY));
    p.addPoint((int)(8.0/x*scaleX),(int)(2.0/y*scaleY));
    p.addPoint((int)(8.0/x*scaleX),(int)(7.0/y*scaleY));
    p.addPoint((int)(4.0/x*scaleX),(int)(9.0/y*scaleY));
    p.addPoint((int)(0.0/x*scaleX),(int)(7.0/y*scaleY));
    g2d.drawPolygon(p);
  }
  public void drawTile(Tile tile, Graphics2D g2d, int x, int y, int width, int height, Menu menu) {
    if(tile.visible) {
      g2d.translate(x,y);
      
      if((tile.menuID >= 0) && (tile.menuID == menu.selected)) { g2d.setColor(COLORS[2]); }
      else { g2d.setColor(tile.color); }
      
      drawHex(g2d,width,height);
      
      if(tile.menuID >= 0) {
        g2d.setFont(new Font("SansSerif", Font.PLAIN, (int)(height/9*1.5)));
        drawCenteredString(menu.get(tile.menuID), width, height, g2d);
      }
      
      g2d.translate(-x,-y);
    }
  }
  public void drawCenteredString(String s, int w, int h, Graphics2D g) {
    FontMetrics fm = g.getFontMetrics();
    int x = (w - fm.stringWidth(s)) / 2;
    int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
    g.drawString(s, x, y);
  }
  public void drawGrid(Game game, Graphics2D g2d, int[] screen) { 
    int hexWidth = screen[0]/8;
    int hexHeight = hexWidth/8*9;
    g2d.setColor(COLORS[1]);
    boolean off = false;
    g2d.translate(-hexWidth+hexWidth/2,-hexHeight/9*7);
    for(int y = 0; y < game.grid.getHeight(); y++) {
      int leftOffset = 0;
      if(off) { off = false; leftOffset = -hexWidth/2; }
      else { off = true; }
      for(int x = 0; x < game.grid.getWidth(); x++) {
        Tile tile = game.grid.getTile(x,y);
        
        //System.out.println((tile.visible)?"True":"False");
        drawTile(tile,g2d,hexWidth*x+leftOffset,hexHeight/9*7*y,hexWidth,hexHeight,game.menu);
        
      }
    }
    g2d.translate(hexWidth-hexWidth/2,hexHeight/9*7);
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
    
    if(game.state == 0) { drawGrid(game,g2d,screen); }
    else if (game.state == 1) {
      // Menu
      drawGrid(game,g2d,screen);
    }
    // Draw letterbox
    g2d.translate(-margin[0],-margin[1]);
    g2d.setColor(COLORS[0]);
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