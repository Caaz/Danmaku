public class Level {
  public Bullet bullets[] = new Bullet[256];
  public Enemy enemies[] = new Enemy[50];
  public Powerup powerups[] = new Powerup[50];
  
  
  public int length = 10;   // Length of level (in seconds)
  public long margin = 1;   // Length of space between start and level, level and end. (nothing happens here!) (in seconds)
  
  public long start = 0;    // Start time (in milliseconds)
  
  public long groupSpace = 2000;  // Length of time btween groups (in milliseconds) (this doesn't include the enemy buffer that happens regardless)
  public long enemySpace = 1000;  // Length of time between enemies in a group (in milliseconds)
  public long enemyLife = 5000; // Time enemies should stay alive. 
  private boolean boss;
  private int groups[][][][] = {
    //Group
    {
      //Enemy
      {{0,2},{100,0},{0,1}},
      {{0,2},{250,0},{0,1}},
      {{0,2},{400,0},{0,1}}
    },
    {
      //Enemy
      {{0,2},{400,0},{0,1}},
      {{0,2},{250,0},{0,1}},
      {{0,2},{100,0},{0,1}}
    },
    {
      //Enemy
      {{1,2},{0,0},{0,1}},
      {{1,2},{0,50},{0,1}},
      {{1,2},{0,150},{0,1}}
    },
    {
      //Enemy
      {{2,2},{500,0},{0,1}},
      {{2,2},{500,50},{0,1}},
      {{2,2},{500,150},{0,1}}
    },
    {
      //Enemy
      {{0,2},{250,0},{1,2}},
    },
    {
      //Enemy
      {{1,0},{0,100},{1,2}},
      {{2,0},{500,100},{1,2}},
      {{1,0},{0,100},{1,2}},
    },
    {
      //Enemy
      {{2,0},{500,100},{1,2}},
      {{1,0},{0,100},{1,2}},
      {{2,0},{500,100},{1,2}},
    },
  };
  
  private long buffer = 0;  // Don't touch
  private int iterator[] = {0,0}; // Don't touch
  
  public Level() { }
  public Level(long time) {
    start = time;
    System.out.println("New Level created");
  }
  public void update(long time, Game game) {
    if((time-start) < margin*1000) {
      // Opening Margin
      // Maybe find a way to display shit here
    }
    if((time-start) > margin*1000+length*1000) {
      // Ending Margin
      if(boss == false) {
        int pattern[] = {0,3};
        float offset[] = {250,-500};
        createEnemy(time,120000,pattern,offset,3,0);
        boss = true;
      }
    }
    else {
      // Actual level time
      if(time > buffer) {
        //
        if(iterator[1] <= groups[iterator[0]].length - 1) {
          float offset[] = {groups[iterator[0]][iterator[1]][1][0],groups[iterator[0]][iterator[1]][1][1]};
          createEnemy(time,enemyLife,groups[iterator[0]][iterator[1]][0],offset,groups[iterator[0]][iterator[1]][2][1],groups[iterator[0]][iterator[1]][2][0]);
          buffer = time + enemySpace;
          iterator[1]++;
        }
        else {
          iterator[0] = (int)(Math.random()*groups.length);
          iterator[1] = 0;
          buffer = time + groupSpace;
        }
      }
    }
    updateBullets(time);
    updateEnemies(time);
    updatePowerups(time);
    checkCollisions(game);
  }
  public void createEnemy(long birth, long life, int pattern[], float offset[], int weapon, int design) {
    for(int i = 0; i < enemies.length; i++) {
      try {
        if(enemies[i].living == false) {
          enemies[i] = new Enemy(birth,life,pattern,offset,weapon,design);
          break;
        }
      }
      catch(NullPointerException e) {
        enemies[i] = new Enemy(birth,life,pattern,offset,weapon,design);
        break;
      }
    }
  }
  public void createPowerup(Powerup pu) {
    for(int i = 0; i < powerups.length; i++) {
      try {
        if(powerups[i].living == false) {
          powerups[i] = pu;
          break;
        }
      }
      catch(NullPointerException e) {
        powerups[i] = pu;
        break;
      }
    }
  }
  public void createBullet(boolean friendly, long birth, long life, float origin[], float offset[], int pattern[]) {
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
        if(enemies[i].living == true) { enemies[i].update(time,this); }
      }
      // If the enemy was never initialized, it'd throw a null pointer exception.
      catch(NullPointerException e) {
        break; // We won't check any more bullets after this one.
      }
    }
  }
  public void updatePowerups(long time) {
    // Loop through all the available enemies
    for(int i = 0; i < powerups.length; i++) {
      // Try block for safety.
      try {
        // If the enemy is alive, then let's update it.
        if(powerups[i].living == true) { powerups[i].update(time); }
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
  public void checkCollisions(Game game) {
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
                    enemies[e].hit(bullets[b],this);
                  }
                }
              }
              catch(NullPointerException error) { break; }
            }
          }
          else {
            // This means it's not a friendly bullet, so check it against the player
            double dist = Math.sqrt(Math.pow(game.player.position[0]-bullets[b].position[0],2) + Math.pow(game.player.position[1]-bullets[b].position[1],2));
            //System.out.println("Got distance " + dist);
            dist -= game.player.hitbox + bullets[b].hitbox;
            if(dist <= 0) {
              game.player.hit(bullets[b], game);
            }
          }
        }
      }
      catch(NullPointerException e) { break; }
    }
    for(int p = 0; p < powerups.length; p++) {
      try {
        if(powerups[p].living) {
          // This means it's not a friendly bullet, so check it against the player
          double dist = Math.sqrt(Math.pow(game.player.position[0]-powerups[p].position[0],2) + Math.pow(game.player.position[1]-powerups[p].position[1],2));
          //System.out.println("Got distance " + dist);
          dist -= game.player.hitbox + powerups[p].hitbox;
          if(dist <= 0) {
            game.player.get(powerups[p], game);
          }
        }
      }
      catch(NullPointerException er) { break; }
    }
  }
  //public void draw(Game game, int[] screen, Graphics2D g2d) {}
}