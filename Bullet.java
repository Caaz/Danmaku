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
  public boolean friendly = false;
  public Bullet() { } // This probably won't do anything.
  public Bullet(boolean friendly, long birth, long life, float origin[], float offset[], float pattern[]) {
    this.friendly = friendly;
    this.birth = birth;
    this.life = life;
    //this.origin = origin;
    //this.offset = offset;
    // Have to convert these floats to ints.
    for(int i = 0; i < 2; i++) { 
      this.origin[i] = origin[i];
      this.offset[i] = offset[i];
      this.pattern[i] = (int)(pattern[i]); 
    }
    // Bring it to life!
    living = true;
  }
  public void update(long sysTime) {
    // Every in game tick, the bullet gets updated here
    // Let's calculate how long the bullet has been alive here (We can later use this in calculations)
    long lifeTime = sysTime - birth;
    if(life > lifeTime) {
      // Here we calculate the x and y coordinates of the bullet by getting the angle and distance and doing magic.
      position[0] = (float)(origin[0] + getDistance(lifeTime) * Math.cos(getAngle(lifeTime)));
      position[1] = (float)(origin[1] + getDistance(lifeTime) * Math.sin(getAngle(lifeTime)));
    }
    // If we've exceeded the lifetime, then die.
    else { die(); }
    for(int i = 0; i<2; i++) {
      // Check if we're off screen!
      if((position[i] < 0) || position[i] > 500) { die(); break; } 
    }
  }
  public float getAngle(long lifeTime) {
    // North
    if(pattern[0] == 0) { return (float)(Math.toRadians(offset[0]+270)); }
    // Spiral, three times.
    else if(pattern[0] == 1) { return (float)Math.toRadians(offset[0]+(float)(lifeTime)/(float)(life)*360*3); }
    
    // This last one is essentially the else.
    return (float)0;
  }
  public float getDistance(long lifeTime) {
    // Linear, speed depends on the lifetime of the bullet.
    if(pattern[1] == 0) { return offset[1]+(float)((float)(lifeTime)/(float)(life)*500.0); }
    
    // Else, static.
    return (float)0;
  }
  public void die() {
    // Very simplistic.
    living = false;
  }
}