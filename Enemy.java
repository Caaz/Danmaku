import java.awt.*;
public class Enemy extends Living {
  public double size = 80;       // Drawn size
  public double hitbox = 12;     // Hitbox size (This doesn't scale correctly at all)
  public int pattern[] = {0,0};  // X, Y
  //public int shot[] = {0,0};     // Pattern, Weapon 
  public float offset[] = {0,0}; // X, Y
  public float rotation = 0;     // Radians? Don't touch this.
  public int health = 15;        // HP is always nice.
  public int design = 0;         // Design the drawing should use.
  public long birth = 0;         // When the enemy is born. Don't touch this.
  public long life = 0;          // How long the enemy will live. Don't touch this.
  public double angle = 0;
  public boolean living = false;
  private boolean shooting = false;
  private long shotWait = 0;
  private int weapon = 0;
  private int weapons[][][][] = {
    // Weapon
    {
      // Bullet
      {
        {0,0},  // Position offset
        {0,0},  // Angle offset
        {0,0},  // Pattern
        {3000,2000}, // Speed, delay
      },
    },
    {
      // Bullet
      {{0,0}, {0,0}, {0,0},   {2000,1000}},
      {{0,0}, {3,0}, {0,0},  {2000,1000}},
      {{0,0}, {-3,0}, {0,0}, {2000,1000}},
      {{0,0}, {0,0}, {0,0},   {4000,1000}},
      {{0,0}, {3,0}, {0,0},  {4000,1000}},
      {{0,0}, {-3,0}, {0,0}, {4000,1000}},
      //{{0,0}, {0,0}, {0,0},   {4000,2000}},
      //{{0,0}, {90,0}, {0,0},  {4000,2000}},
      //{{0,0}, {180,0}, {0,0}, {4000,2000}},
    },
    {
      // Bullet
      {{0,0}, {0,0}, {0,0},   {2000,1000}},
      {{0,0}, {90,0}, {0,0},  {2000,1000}},
      {{0,0}, {270,0}, {0,0}, {2000,1000}},
      {{0,0}, {0,0}, {0,0},   {4000,1000}},
      {{0,0}, {90,0}, {0,0},  {4000,1000}},
      {{0,0}, {270,0}, {0,0}, {4000,1000}},
    },
  };
  private Color colors[] = {
    new Color(20,20,20),  // 0  Outline
    new Color(70,70,70),  // 1  Body
    new Color(60,50,50),  // 2  Body (Darker)
    new Color(100,50,50), // 3  Window
    new Color(100,20,20), // 4  Window (Darker)
  };
  public Enemy() { }
  public Enemy(long birth, long life, int pattern[], float offset[], int weapon, int design) {
    this.birth = birth;
    this.life = life;
    this.pattern = pattern;
    this.design = design;
    this.offset = offset;
    this.weapon = weapon;
    // Bring it to life
    living = true;
  }
  public void update(long sysTime, Level level) {
    // Every in game tick, the bullet gets updated here
    // Let's calculate how long the bullet has been alive here (We can later use this in calculations)
    long lifeTime = sysTime - birth;
    if(life > lifeTime) {
      float percent = (float)(lifeTime)/(float)(life);
      float newpos[] = {getX(percent),getY(percent)};
      if((newpos[0] != position[0]) || (newpos[1] != position[1])) {
        angle = (float)Math.atan2(-(position[0] - newpos[0]), position[1] - newpos[1]);
      }
      position[0] = newpos[0];
      position[1] = newpos[1];
      
      shooting = getShot(sysTime);
      
      if(shooting) {
        // First make sure this timer thing is okay.
        if(sysTime >= shotWait) {
          // Make a bullet!
          for(int b = 0; b<weapons[weapon].length; b++) {
            // Here we're iterating through each bullet in the bullet list, to create them at the same time.
            // While creating them seperately makes a nice stream, when combined with a lot of streams, it looks offbalanced, and ugly.
            
            // Here we're making an offset for the origin point of the bullet.
            float offset[] = {position[0]+weapons[weapon][b][0][0],position[1]+weapons[weapon][b][0][1]};
            float ad[] = {(float)angle+(float)Math.toRadians(weapons[weapon][b][1][0]),weapons[weapon][b][1][1]};
            
            // Then we're creating the bullet.
            // public void createBullet(boolean friendly, long birth, long life, float origin[], float offset[], float pattern[]) {
            level.createBullet(false,sysTime,weapons[weapon][b][3][0],offset,ad,weapons[weapon][b][2]);
            
            // And then we set the delay, so that we're not making too many bullets.
            shotWait = sysTime+weapons[weapon][b][3][1];
          }
        }
      }
    }
    // If we've exceeded the lifetime, then die.
    else { die(); }
    // If we're out of health, die.
  }
  public boolean getShot(float percent) {
    // Similar to patterns, this will return true or false if the enemy is shooting
    //if(pattern[])
    return true;
  }
  public float getX(float percent) {
    // No movement
    if(pattern[0] == 0) { return (float)(0+offset[0]); }
    // left to right
    if(pattern[0] == 1) { return (float)(percent*600.0-50.0+offset[0]); }
    // Right to left
    if(pattern[0] == 2) { return (float)(percent*-600.0+50.0+offset[0]); }
    // donno
    // This last one is essentially the else.
    return (float)0;
  }
  public float getY(float percent) {
    // No movement
    if(pattern[1] == 0) { return (float)(0+offset[1]); }
    // Top to bottom
    if(pattern[1] == 1) { return (float)(percent*600.0-50.0+offset[1]); }
    // Moves downward, stops for a while, then continues on downward
    if(pattern[1] == 2) { 
      if(percent < .3) {
        return (float)(-(Math.pow(percent*100.0-30.0,2)/5.0)+150+offset[1]);
      }
      else if(percent < .6) {
        return (float)(150+offset[1]);
      }
      else {
        return (float)(Math.pow(percent*100.0-60.0,2)/2.0+150+offset[1]);
      }
    }
    // This last one is essentially the else.
    return (float)0;
  }
  public void die() { 
    living = false; 
  }
  public void die(Level level) {
    level.createPowerup(new Powerup(position));
    living = false;
  }
  public void hit(Bullet bullet, Level level) {
    health--;
    bullet.die();
    if(health <= 0) { die(level); }
  }
  
  // Draw code, may or may not have been ripped from player.
  public void draw(Graphics2D g2d, int[] screen, View helper) {
    float scale = (float)(screen[1]/500.0*size/32.0);
    // Translate to the position
    g2d.translate(position[0]/500*screen[1],position[1]/500*screen[1]);
    g2d.rotate(angle);
    if(design == 0) {
      
      int turret[][] = {{4,5,5,6,6,7,7,5,4},{-2,-3,-5,-5,-3,-2,4,6,6}};                            // Turret shape
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[1]);                                 // Draw the shape.
      for(int f = 0; f < turret[0].length; f++) { turret[0][f] = turret[0][f]-turret[0][f]*2; } // Flip it!
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[1]);                                 // And draw it again!
      int jets[][] = {{-2,-2,-1,1,2,2},{0,4,5,5,4,0}};                                      // The jet thing at the back of the ship
      helper.drawPolygon(g2d,jets,scale,colors[2],colors[1]);                                   // Draw it
      int sTur[][] = {
        { 0, 4, 5, 7, 7, 4, 4, 3, 3, 2, 2, 1, 1,-1,-1,-2,-2,-3,-3,-4,-4,-7,-7,-5,-4, 0},        // I hate this shape.
        {2,6,6,4,1, -2, -5, -6, -7, -7, -6, -5, -2, -2, -5, -6, -7, -7, -6, -5, -2,1,4,6,6,2}         // It's the second set of turrets on the enemy ship
      };
      helper.drawPolygon(g2d,sTur,scale,colors[0],colors[1]);                                   // Draw them
      
      int body[][] = {{0,4,4,2,2,0,-2,-2,-4,-4,0},{2,6,1,-1,-3,-5,-3,-1,1,6,2}};               // Weird triangle hull!
      helper.drawPolygon(g2d,body,scale,colors[0],colors[1]);                                   // Draw it
      
      int pit[][] = {{1,1,0,-1,-1},{-1,-3,-4,-3,-1}};                                                // The cockpit window
      helper.drawPolygon(g2d,pit,scale,colors[4],colors[3]);                                    // Draw it
      
      // Maybe work on flame?
    }
    else if(design == 1) {
      scale = (float)(screen[1]/500.0*size/64.0);
      int back[][] = {{-5,5,12,12,5,-5,-12,-12},{-12,-12,-5,5,12,12,5,-5}};
      helper.drawPolygon(g2d,back,scale,colors[0],colors[1]);
      int pit[][] = {{0,2,8,2,0,-2,-8,-2},{-8,-2,0,2,8,2,0,-2}};
      helper.drawPolygon(g2d,pit,scale,colors[4],colors[3]);
    }
    g2d.rotate(-angle);
    g2d.translate(-(position[0]/500*screen[1]),-(position[1]/500*screen[1]));
  }
}