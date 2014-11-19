
import java.awt.*;
public class Player extends Living {
  public double size = 60;
  public double hitbox = 12;
  public int controls[] = {0,0,0,0,0,0,0,0}; // Up down left right shoot bomb pause
  public float velocity[] = {0,0};
  public float velIncrement = (float).5;
  public float velDecrement = (float)1.2;
  public boolean shield = false;
  
  // HONESTLY A FIVE DIMENSIONAL ARRAY SEEMED LIKE A GOOD IDEA AT THE TIME.
  public int[][][][][] weapons = {
    {
      // Weapon Type A
      {
        // Level 0!
        {{-7,-14},{0,0},{0,0}},
        {{7,-14},{0,0},{0,0}},
      },
      {
        // Level 1
        {{-7,-14},{-5,0},{0,0}},
        {{0,-15},{0,0},{0,0}},
        {{7,-14},{5,0},{0,0}},
      },
      {
        // Level 2
        {{-7,-14},{-10,0},{0,0}},
        {{-7,-14},{-5,0},{0,0}},
        {{0,-15},{0,0},{0,0}},
        {{7,-14},{5,0},{0,0}},
        {{7,-14},{10,0},{0,0}},
      }
    },
  };
  // Type of weapon
  public int type = 0;
  // Level of weapon
  public int level = 0;
  public long shotDelay = 25; // Delay between shots
  public long shotWait = 0; // Leave this alone!
  public boolean shooting = false;
  private long shieldTime = 0;
  public int bombs = 3;
  
  private Color[] colors = {
    new Color(70,70,75), // 0  Outline
    new Color(200,200,210), // 1  Body
    new Color(170,170,185), // 2  Body (Darkened)
    new Color(200,200,255), // 3  Window
    new Color(150,150,160), // 4  Window (Darkened)
    new Color(100,100,255), // 5  Body (Shield)
  };
  
  public Player() { 
      position[0] = 250;
      position[1] = 400;
      living = true;
  }
  public void resetControls() { for(int k = 0; k < controls.length; k++) { controls[k] = 0; } }
  public void update(long sysTime, Game game) {
    // {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"}
    if(living) {
      if(game.keys[controls[0]]) { velocity[1] -= velIncrement; }
      if(game.keys[controls[1]]) { velocity[1] += velIncrement; }
      if(game.keys[controls[2]]) { velocity[0] -= velIncrement; }
      if(game.keys[controls[3]]) { velocity[0] += velIncrement; }
      if(game.keys[controls[6]]) { for(int i = 0; i < 2; i++) { velocity[i] = (float)(velocity[i]/velDecrement); } }
      
      // When you shoot, it sets a variable to true! So you can shoot more than one thing at once.
      if(game.keys[controls[4]]) { shooting = true; }
      if(game.keys[controls[5]]) {
        if(bombs > 0) {
          // You have one bomb!
          if(shieldTime < sysTime) {
            // You're not currently using it...
            shield = true;
            shieldTime = sysTime+5000;
            bombs--;
            
          }
        }
      }
      if((shield) && (shieldTime < sysTime)) { shield = false; }
      
      // So if we're shooting...
      if(shooting) {
        // First make sure this timer thing is okay.
        if(sysTime >= shotWait) {
          // Make a bullet! 
          for(int b = 0; b<weapons[type][level].length; b++) {
            // Here we're iterating through each bullet in the bullet list, to create them at the same time.
            // While creating them seperately makes a nice stream, when combined with a lot of streams, it looks offbalanced, and ugly.
            
            // Here we're making an offset for the origin point of the bullet.
            float offset[] = {position[0]+weapons[type][level][b][0][0],position[1]+weapons[type][level][b][0][1]};
            float ad[] = {(float)Math.toRadians(weapons[type][level][b][1][0]),weapons[type][level][b][1][1]};
            
            // Then we're creating the bullet.
            game.level.createBullet(true,sysTime,300,offset,ad,weapons[type][level][b][2]);
            
            // And then we set the delay, so that we're not making too many bullets.
            shotWait = sysTime+shotDelay; 
            shooting = false; 
          }
        }
      }
      // Every tick this is alled.
      // Decrement the velocity. Because fuck nuetan's laws.
      for(int i = 0; i < 2; i++) {                        // x and y...
        velocity[i] = (float)(velocity[i]/1.1);           // this cuts the velocity speed
        position[i] += velocity[i];                       // This updates the actual ship's position.
        if(position[i] < 0) { position[i] = 0; }          // If we're hitting the 0 boundary, we clip to 0.
        else if(position[i] > 500) { position[i] = 500; } // Or, if we get over 500, we clip there.
      }
    }
    
  }
  public void hit(Bullet bullet, Game game) {
    bullet.die();
    die(game);
  }
  public void die(Game game) {
    //try { Thread.sleep(500); } catch (InterruptedException e) { }
    if(shield == false) {
      health--;
      position[0] = 250;
      position[1] = 400;
      for(int b = 0; b < game.level.bullets.length; b++) {
        try { game.level.bullets[b].die(); }
        catch(NullPointerException y) { }
      }
      /*
      for(int e = 0; e < game.level.enemies.length; e++) {
        try { game.level.enemies[e].die(); }
        catch(NullPointerException x) { }
      }
      */
      if(health <= 0) {
        living = false;
        game.state = 1;
      }
    }
  }
  public void get(Powerup pu, Game game) {
    if(pu.type == 0) {
      level++;
      if(level >= weapons[type].length) { level--; }
      pu.die();
    }
  }
  // This draws the player
  // I decided to throw in the drawing code here because it /kind of/ makes sense.
  public void draw(Graphics2D g2d, int[] screen, View helper) {
    if(living) {
      float scale = (float)(screen[1]/500.0*size/32.0);                                 // Get the scale for the player's size.
      g2d.translate(position[0]/500*screen[1],position[1]/500*screen[1]);               // Translate to the player's position.
      
      int turret[][] = {{2,2,3,3,4,4,5,5},{1,-3,-4,-6,-6,-4,-3,1}};                     // Turret shape
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[(shield)?5:1]);                         // Draw the shape.
      for(int f = 0; f < turret[0].length; f++) {                                       // Iterate through the X values of the shape
        turret[0][f] = turret[0][f]-turret[0][f]*2;                                     // Let's flip the turret to the other side with some math
      }                                                                                 //
      helper.drawPolygon(g2d,turret,scale,colors[0],colors[(shield)?5:1]);                         // And draw it again!
      int wings[][] = {{0,5,9,9,2,2,-2,-2,-9,-9,-5,0},{-8,1,5,7,7,6,6,7,7,5,1,-8}};     // Now let's try wings. (Both are drawn together so we don't need to flip it.)
      helper.drawPolygon(g2d,wings,scale,colors[0],colors[(shield)?5:1]);                          // Draw them
      int body[][] = {{0,2,2,-2,-2,0},{-8,-6,6,6,-6,-8}};                               // The pencil shape in the center
      helper.drawPolygon(g2d,body,scale,colors[0],colors[(shield)?5:1]);                           // Draw it
      int jets[][] = {{2,3,5,6,6,5,3,2,2},{4,3,3,4,8,9,9,8,4}};                         // The jet thing at the back of the wings
      helper.drawPolygon(g2d,jets,scale,colors[2],colors[(shield)?5:1]);                           // Draw it
      for(int f = 0; f < jets[0].length; f++) { jets[0][f] = jets[0][f]-jets[0][f]*2; } // Flip it
      helper.drawPolygon(g2d,jets,scale,colors[2],colors[(shield)?5:1]);                           // Draw it again
      int fin[][] = {{0,1,1,0,-1,-1,0},{0,1,6,8,6,1,0}};                                // The center fin
      helper.drawPolygon(g2d,fin,scale,colors[2],colors[(shield)?5:1]);                            // Draw it
      int pit[][] = {{0,1,1,-1,-1,0},{-7,-6,-4,-4,-6,-7}};                              // The cockpit window
      helper.drawPolygon(g2d,pit,scale,colors[4],colors[3]);                            // Draw it
      // Think of the flames here
      
      // Translate back to wherever we started, so that we don't fuck up drawing after this.
      g2d.translate(-(position[0]/500*screen[1]),-(position[1]/500*screen[1]));
    }
  }
}