import java.awt.*;
public class Enemy extends Living {
  public double size = 80;       // Drawn size
  public double hitbox = 12;     // Hitbox size (This doesn't scale correctly at all)
  public int pattern[] = {0,0};  // X, Y
  public float offset[] = {0,0}; // X, Y
  public float rotation = 0;     // Radians? Don't touch this.
  public int health = 30;        // HP is always nice.
  public int design = 0;         // Design the drawing should use.
  public long birth = 0;         // When the enemy is born. Don't touch this.
  public long life = 0;          // How long the enemy will live. Don't touch this.
  public boolean living = false;
  public Color colors[] = {
    new Color(20,20,20),  // 0  Outline
    new Color(70,70,70),  // 1  Body
    new Color(60,50,50),  // 2  Body (Darker)
    new Color(100,50,50), // 3  Window
    new Color(100,20,20), // 4  Window (Darker)
  };
  public Enemy() { }
  public Enemy(long birth, long life, int pattern[], float offset[], int design) {
    this.birth = birth;
    this.life = life;
    this.pattern = pattern;
    this.design = design;
    this.offset = offset;
    // Bring it to life
    living = true;
  }
  public void update(long sysTime) {
    // Every in game tick, the bullet gets updated here
    // Let's calculate how long the bullet has been alive here (We can later use this in calculations)
    long lifeTime = sysTime - birth;
    if(life > lifeTime) {
      // Here we calculate the x and y coordinates of the bullet by getting the angle and distance and doing magic.
      position[0] = getX(lifeTime);
      position[1] = getY(lifeTime);
    }
    // If we've exceeded the lifetime, then die.
    else { die(); }
    // If we're out of health, die.
    if(health <= 0) { die(); }
    /*
    for(int i = 0; i<2; i++) {
      // Check if we're off screen!
      if((position[i] < 0) || position[i] > 500) { die(); break; } 
    }
    */
  }
  public float getX(long lifeTime) {
    // No movement
    if(pattern[0] == 0) { return (float)(0+offset[0]); }
    // left to right
    if(pattern[0] == 1) { return (float)((float)(lifeTime)/(float)(life)*600.0-50.0+offset[0]); }
    // Sine! This oesn't even work right. someone else handle patterns I'm terrible.
    if(pattern[0] == 2) { return (float)Math.sin(Math.toRadians(((float)(lifeTime)/(float)(life)*360)))*500; }
    // This last one is essentially the else.
    return (float)0;
  }
  public float getY(long lifeTime) {
    // No movement
    if(pattern[1] == 0) { return (float)(0+offset[1]); }
    // Top to bottom
    if(pattern[1] == 1) { return (float)((float)(lifeTime)/(float)(life)*600.0-50.0+offset[1]); }
    // This last one is essentially the else.
    return (float)0;
  }
  public void die() { living = false; }
  public void hit(Bullet bullet) {
    health--;
    bullet.die();
  }
  
  // Draw code, may or may not have been ripped from player.
  public void draw(Graphics2D g2d, int[] screen, DrawHelper helper) {
    if(design == 0) {
      float scale = (float)(screen[1]/500.0*size/32.0);
      // Translate to the position
      g2d.translate(position[0]/500*screen[1],position[1]/500*screen[1]);
      
      int turret[][] = {{4,5,5,6,6,7,7,5,4},{2,3,5,5,3,2,-4,-6,-6}};                            // Turret shape
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[1]);                                 // Draw the shape.
      for(int f = 0; f < turret[0].length; f++) { turret[0][f] = turret[0][f]-turret[0][f]*2; } // Flip it!
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[1]);                                 // And draw it again!
      int jets[][] = {{-2,-2,-1,1,2,2},{0,-4,-5,-5,-4,0}};                                      // The jet thing at the back of the ship
      helper.drawPolygon(g2d,jets,scale,colors[2],colors[1]);                                   // Draw it
      int sTur[][] = {
        { 0, 4, 5, 7, 7, 4, 4, 3, 3, 2, 2, 1, 1,-1,-1,-2,-2,-3,-3,-4,-4,-7,-7,-5,-4, 0},        // I hate this shape.
        {-2,-6,-6,-4,-1, 2, 5, 6, 7, 7, 6, 5, 2, 2, 5, 6, 7, 7, 6, 5, 2,-1,-4,-6,-6,-2}         // It's the second set of turrets on the enemy ship
      };
      helper.drawPolygon(g2d,sTur,scale,colors[0],colors[1]);                                   // Draw them
      
      int body[][] = {{0,4,4,2,2,0,-2,-2,-4,-4,0},{-2,-6,-1,1,3,5,3,1,-1,-6,-2}};               // Weird triangle hull!
      helper.drawPolygon(g2d,body,scale,colors[0],colors[1]);                                   // Draw it
      
      int pit[][] = {{1,1,0,-1,-1},{1,3,4,3,1}};                                                // The cockpit window
      helper.drawPolygon(g2d,pit,scale,colors[4],colors[3]);                                    // Draw it
      
      // Maybe work on flame?
      
      // Translate back to wherever we started, so that we don't fuck up drawing after this.
      g2d.translate(-(position[0]/500*screen[1]),-(position[1]/500*screen[1]));
    }
  }
}