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
    new Color(100,100,100),    // 3  Dark Gray
    new Color(180,180,180), // 4  Light Gray
  };
  public void drawPolygon(Graphics2D g2d, int[][] points, float scale, Color lines, Color fill) {
    Polygon wPoly = new Polygon();
    for(int i = 0; i < points[0].length; i++) { wPoly.addPoint((int)(points[0][i]*scale),(int)(points[1][i]*scale)); }
    g2d.setColor(fill);
    g2d.fillPolygon(wPoly);
    //g2d.setStroke(new BasicStroke((float)1.5));
    g2d.setColor(lines);
    g2d.drawPolygon(wPoly);
    //g2d.setStroke(new BasicStroke(1));
  }
  public void drawEnemies(Graphics2D g2d, Enemy enemies[], int[] screen) {
    for(int i = 0; i < enemies.length; i++) {
      try{ 
        if(enemies[i].living) {
          enemies[i].draw(g2d, screen, this);
        }
      } catch (NullPointerException e) {
        // we don't need to do anything here, but let's break cause we can assume there's nothing else in the array.
        break;
      }
    }
  }
  public void drawBullets(Graphics2D g2d, Bullet bullets[], int[] screen) {
    for(int i = 0; i < bullets.length; i++) {
      try{ 
        if(bullets[i].living) {
          bullets[i].draw(g2d, screen, this);
          bullets[i].draw(g2d, screen, this);
        }
      } catch (NullPointerException e) {
        // we don't need to do anything here, but let's break cause we can assume there's nothing else in the array.
        break;
      }
    }
  }
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
    else if (game.state == 1) { drawGrid(game,g2d,screen); }
    else if(game.state == 4) { 
      g2d.translate(screen[0]/8,0);
      
      g2d.setColor(COLORS[3]);
      g2d.fillRect(0,0,screen[1],screen[1]);
      drawBullets(g2d,game.bullets,screen);
      drawEnemies(g2d,game.enemies,screen);
      game.player.draw(g2d,screen,this); 
      
      g2d.translate(-(screen[0]/8),0);
    
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
}