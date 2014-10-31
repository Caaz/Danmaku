import java.awt.*;
public class Bullet {
  public boolean rotates = false;
  public float roation = 0;
  // Pattern pattern = new Pattern();
  int pattern[] = {0,0};
  Color color = new Color(255,255,255);
  public long life = 0;
  public long birth = 0;
  public float[] position = {0,0}; // Angle, Distance
  public float[] offset = {0,0}; // Angle, Distance
  public float[] origin = {0,0}; // X, Y
  public boolean living = false;
  public boolean enemy = false;
  public Bullet() {
    // create
  }
  public Bullet(Player player, long birth) {
    // create with player
    //System.out.println("Creating new bullet from player");
    for(int i = 0; i<2; i++) {
      origin[i] = player.position[i];
    }
    this.birth = birth-1;
    life = 1000;
    living = true;
    
    if(player.weapon == 0) {
      pattern[0] = player.bullet;
      player.bullet++;
      if(player.bullet == 3) { player.bullet = 0; }
    }
    else if(player.weapon == 1) {
      pattern[0] = 3;
      life = 3000;
      player.bullet++;
      offset[0] = (float)Math.toRadians(player.bullet/3.0*360.0);
      if(player.bullet == 3) { player.bullet = 0; }
    }
  }
  public void update(long sysTime) {
    long lifeTime = sysTime - birth;
    if(life > lifeTime) {
      position[0] = (float)(origin[0] + getDistance(lifeTime) * Math.cos(getAngle(lifeTime)));
      position[1] = (float)(origin[1] + getDistance(lifeTime) * Math.sin(getAngle(lifeTime)));
    }
    else {
      die();
    }
  }
  public float getAngle(long lifeTime) {
    if(pattern[0] == 0) {
      // Straight up
      return offset[0]+(float)(Math.toRadians(270));
    }
    else if(pattern[0] == 1) {
      // Angled left
      return offset[0]+(float)(Math.toRadians(250));
    }
    else if(pattern[0] == 2) {
      // Angled right
      return offset[0]+(float)(Math.toRadians(290));
    }
    else if(pattern[0] == 3) {
      // Spiral
      return offset[0]+(float)Math.toRadians((float)(lifeTime)/(float)(life)*360*3);
    }
    return (float)0;
  }
  public float getDistance(long lifeTime) {
    if(pattern[1] == 0) {
      //  Linear speed, depends on lifetime for speed
      return offset[1]+(float)((float)(lifeTime)/(float)(life)*500.0);
      // Right?
    }
    return (float)0;
  }
  public void die() {
    living = false;
    //System.out.println("A bullet has died");
  }
}