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
  public void drawPlayer(Graphics2D g2d, Player player, int[] screen) {
    float scale = screen[1]/200;
    //Translate
    //g2d.translate(player.position[0]/500*screen[1],player.position[1]/500*screen[1]);
    //System.out.println("Translating to "+player.position[0]/500.0*screen[1]+ "x"+player.position[1]/500.0*screen[1]);
    //System.out.println("This is "+player.position[0]+ "x"+player.position[1]);
    
      g2d.translate(player.position[0]/500*screen[1],player.position[1]/500*screen[1]);
    
    
    g2d.setColor(COLORS[1]);
    
    // Working polygon
    Polygon wPoly = new Polygon();
    // draw hitbox
    
    // Sets up the Turret shape
    int turret[][] = {{2,2,3,3,4,4,5,5},{1,-3,-4,-6,-6,-4,-3,1}};
    // Draws it
    drawShapeWithLines(g2d,turret,scale,COLORS[0],COLORS[4]);
    // Flips the turret to the left side
    for(int f = 0; f < turret[0].length; f++) { turret[0][f] = turret[0][f]-turret[0][f]*2; }
    // Draws it again
    drawShapeWithLines(g2d,turret,scale,COLORS[0],COLORS[4]);    
    // Sets up wings, and draws it
    int wings[][] = {{0,5,9,9,2,2,-2,-2,-9,-9,-5,0},{-8,1,5,7,7,6,6,7,7,5,1,-8}};
    drawShapeWithLines(g2d,wings,scale,COLORS[0],COLORS[4]);    
    // Sets up body and draws.
    int body[][] = {{0,2,2,-2,-2,0},{-8,-6,6,6,-6,-8}};
    drawShapeWithLines(g2d,body,scale,COLORS[0],COLORS[4]);   
    int jets[][] = {{2,3,5,6,6,5,3,2,2},{4,3,3,4,8,9,9,8,4}};
    drawShapeWithLines(g2d,jets,scale,new Color(130,130,130),new Color(200,200,200));
    for(int f = 0; f < jets[0].length; f++) { jets[0][f] = jets[0][f]-jets[0][f]*2; }
    drawShapeWithLines(g2d,jets,scale,new Color(130,130,130),new Color(200,200,200));
    int fin[][] = {{0,1,1,0,-1,-1,0},{0,1,6,8,6,1,0}};
    drawShapeWithLines(g2d,fin,scale,new Color(130,130,130),new Color(200,200,200));
    int pit[][] = {{0,1,1,-1,-1,0},{-7,-6,-4,-4,-6,-7}};
    drawShapeWithLines(g2d,pit,scale,new Color(100,100,130),new Color(200,200,255));
    
    //Back
      g2d.translate(-(player.position[0]/500*screen[1]),-(player.position[1]/500*screen[1]));
    //g2d.translate(-(player.position[0]/500*screen[1]),-(player.position[1]/500*screen[1]));
  }
  public void drawShapeWithLines(Graphics2D g2d, int[][] points, float scale, Color lines, Color fill) {
    Polygon wPoly = new Polygon();
    for(int i = 0; i < points[0].length; i++) { wPoly.addPoint((int)(points[0][i]*scale),(int)(points[1][i]*scale)); }
    g2d.setColor(fill);
    g2d.fillPolygon(wPoly);
    g2d.setColor(lines);
    g2d.drawPolygon(wPoly);
    
  }
  public void drawBullets(Graphics2D g2d, Bullet bullets[], int[] screen) {
    for(int i = 0; i < bullets.length; i++) {
      try{ 
        if(bullets[i].living) {
          // Translate to the position of the bullet
          g2d.translate(bullets[i].position[0]/500*screen[1],bullets[i].position[1]/500*screen[1]);
          //System.out.println("Drawing bullet at "+bullets[i].position[0]/500*screen[1]+"x"+bullets[i].position[1]/500*screen[1]);
          float scale = screen[1]/300;
          
          int bullet[][] = {{0,2,0,-2,0},{-2,0,2,0,-2}};
          
          Color bColor = new Color(200,200,255);
          if(bullets[i].friendly == false) {
            bColor = new Color(255,100,100);
          }
          
          drawShapeWithLines(g2d,bullet,scale,new Color(0,0,0),bColor);
          
          // Translate back to where we started.
          g2d.translate(-(bullets[i].position[0]/500*screen[1]),-(bullets[i].position[1]/500*screen[1]));
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
      drawPlayer(g2d,game.player,screen); 
      
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