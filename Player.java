public class Player extends Living {
  public int controls[] = {0,0,0,0,0,0,0,0}; // Up down left right shoot bomb pause
  public float velocity[] = {0,0};
  public float maxVelocity = 5;
  
  // HONESTLY A FIVE DIMENSIONAL ARRAY SEEMED LIKE A GOOD IDEA AT THE TIME.
  public float[][][][][] weapons = {
    {
      // Weapon Type A
      {
        // Level 1
        {{0,0},{0,0}},
        {{10,0},{0,0}},
        {{-10,0},{0,0}},
      },
      {
        // Level 2
        {{0,0},{0,0}},
        {{10,0},{0,0}},
        {{-10,0},{0,0}},
        {{20,0},{0,0}},
        {{-20,0},{0,0}},
      }
    },
  };
  // Type of weapon
  public int type = 0;
  // Level of weapon
  public int level = 1;
  public int bIteration = 0; // Bullet iteration.
  public long shotDelay = 50; // Delay between shots
  public long shotWait = 0; // Leave this alone!
  public boolean shooting = false;
  
  public Player() { }
  public void resetControls() { for(int k = 0; k < controls.length; k++) { controls[k] = 0; } }
  public void update(long sysTime, Game game) {
    // {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"}
    if(game.keys[controls[0]]) { velocity[1] -= 1; }
    if(game.keys[controls[1]]) { velocity[1] += 1; }
    if(game.keys[controls[2]]) { velocity[0] -= 1; }
    if(game.keys[controls[3]]) { velocity[0] += 1; }
    if(game.keys[controls[6]]) { for(int i = 0; i < 2; i++) { velocity[i] = (float)(velocity[i]/1.5); } }
    
    // When you shoot, it sets a variable to true! So you can shoot more than one thing at once.
    if(game.keys[controls[4]]) { shooting = true; }
    
    // So if we're shooting...
    if(shooting) {
      // First make sure this timer thing is okay.
      if(sysTime >= shotWait) {
        // Make a bullet!
        game.createBullet(true,sysTime,1000,position,weapons[type][level][bIteration][0],weapons[type][level][bIteration][1]);
        // Move to the next bullet iteration.
        bIteration++;
        // if we've exceeded the weapon, go back to 0.
        if(bIteration >= weapons[type][level].length) { 
          bIteration = 0; 
          shotWait = sysTime+shotDelay; 
          shooting = false; 
        }
      }
    }
    // Every tick this is alled.
    // Decrement the velocity. Because fuck nuetan's laws.
    for(int i = 0; i < 2; i++) { 
      velocity[i] = (float)(velocity[i]/1.25);
      position[i] += velocity[i];
      if(position[i] < 0) { position[i] = 0; }
      else if(position[i] > 500) { position[i] = 500; }
    }
    
    
  }
}