import java.awt.*;
public class Bullet {
  public boolean rotates = false;
  public float roation = 0;
  // Pattern pattern = new Pattern();
  int pattern = 0;
  Color color = new Color(255,255,255);
  public long life = 0;
  public long birth = 0;
  public float[] position = {0,0}; // Angle, Distance
  public float[] origin = {0,0}; // X, Y
  public boolean living = false;
  public boolean dangerous = false;
  public Bullet() {
    // create
  }
  public void update(long sysTime) {
    long lifeTime = sysTime - birth;
    if(life < lifeTime) {
      position[0] = getAngle(lifeTime);
      position[1] = getDistance(lifeTime);
    }
    else {
      die();
    }
  }
  public float getAngle(long lifeTime) {
    return (float)0;
  }
  public float getDistance(long lifeTime) {
    return (float)0;
  }
  public void die() {
    living = false;
  }
}