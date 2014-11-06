public class Level {
  public Bullet bullets[] = new Bullet[256];
  public Enemy enemies[] = new Enemy[50];
  
  
  public int length = 60;   // Length of level (in seconds)
  public long margin = 5;   // Length of space between start and level, level and end. (nothing happens here!) (in seconds)
  
  public long start = 0;    // Start time (in milliseconds)
  
  public int groupSize = 4; // Number of enemies to spawn in a group
  public long groupSpace = 1000;  // Length of time btween groups (in milliseconds) (this doesn't include the enemy buffer that happens regardless)
  public long enemySpace = 500;  // Length of time between enemies in a group (in milliseconds)
  public long enemyLife = 10000; // Time enemies should stay alive. 
  
  private int patterns[][] = {
    {0,2},
  };
  private float offsets[][] = {
    {250,0},
  };
  
  private long buffer = 0;  // Don't touch
  private int iterator = 0; // Don't touch
  
  public Level() { }
  public Level(long time) {
    start = time;
  }
  public void update(long time, Game game) {
    if(((time-start) < margin*1000) || ((time-start) > margin*1000+length*1000)) {
      // Margins
    }
    else {
      // Actual level time
      if(time > buffer) {
        //
        iterator++;
        if(iterator <= groupSize) {
          createEnemy(time,enemyLife,patterns[0],offsets[0],0);
          buffer = time + enemySpace;
        }
        else {
          iterator = 0;
          buffer = time + groupSpace;
        }
      }
    }
    updateBullets(time);
    updateEnemies(time);
    checkCollisions();
  }
  public void createEnemy(long birth, long life, int pattern[], float offset[], int design) {
    for(int i = 0; i < enemies.length; i++) {
      try {
        if(enemies[i].living == false) {
          enemies[i] = new Enemy(birth,life,pattern,offset,design);
          break;
        }
      }
      catch(NullPointerException e) {
        enemies[i] = new Enemy(birth,life,pattern,offset,design);
        break;
      }
    }
  }
  public void createBullet(boolean friendly, long birth, long life, float origin[], float offset[], float pattern[]) {
    for(int i = 0; i < bullets.length; i++) {
      // Try for safety!
      try {
        // Check if the bullet is living...
        if(bullets[i].living == false) {
          // And if it's not, then that means we can place a new bullet here, because it wasn't being used anyway.
          bullets[i] = new Bullet(friendly,birth,life,origin,offset,pattern);
          // And then lets break out of this loop because we no longer need to look through the array.
          break;
        }
      }
      // However there's this problem of if no bullets existed! Or the specific one we were checking didn't exist at all.
      catch(NullPointerException e) {
        // In that case, just make a new bullet in it's place!
        bullets[i] = new Bullet(friendly,birth,life,origin,offset,pattern);
        // And then break out of the loop because we don't want to keep making new bullets in the array.
        break;
      }
    }
  }
  public void updateEnemies(long time) {
    // Loop through all the available enemies
    for(int i = 0; i < enemies.length; i++) {
      // Try block for safety.
      try {
        // If the enemy is alive, then let's update it.
        if(enemies[i].living == true) { enemies[i].update(time); }
      }
      // If the enemy was never initialized, it'd throw a null pointer exception.
      catch(NullPointerException e) {
        break; // We won't check any more bullets after this one.
      }
    }
  }
  // This updates every individual bullet.
  public void updateBullets(long time) {
    // Loop through all the available bullets
    for(int i = 0; i < bullets.length; i++) {
      // Try block for safety.
      try {
        // If the bullet is alive, then let's update it.
        if(bullets[i].living == true) { bullets[i].update(time); }
      }
      // If the bullet was never initialized, it'd throw a null pointer exception.
      catch(NullPointerException e) {
        // Right here I had copied the code from the spawnBullet method, and it was creating a new bullet every time the game started!
        // Anyway... If we throw this error, this means we've essentially hit the end of the list of bullets that exist, so we can break the loop here for efficiency.
        break;
        // Which means we won't check any more bullets after this one.
      }
    }
  }
  public void checkCollisions() {
    // Loop through the bullets...
    for(int b = 0; b < bullets.length; b++) {
      try {
        if(bullets[b].living) {
          if(bullets[b].friendly) {
            // If it's a friendly bullet. (Shot by the player)
            // Loop through enemies
            for(int e = 0; e < enemies.length; e++) {
              try {
                if(enemies[e].living) {
                  double dist = Math.sqrt(Math.pow(enemies[e].position[0]-bullets[b].position[0],2) + Math.pow(enemies[e].position[1]-bullets[b].position[1],2));
                  //System.out.println("Got distance " + dist);
                  dist -= enemies[e].hitbox + bullets[b].hitbox;
                  if(dist <= 0) {
                    enemies[e].hit(bullets[b]);
                  }
                }
              }
              catch(NullPointerException error) { break; }
            }
          }
          else {
            // This means it's not a friendly bullet, so check it against the player
          }
        }
      }
      catch(NullPointerException e) { break; }
    }
  }
}