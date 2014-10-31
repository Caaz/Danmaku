public class Player extends Living {
  public int controls[] = {0,0,0,0,0,0,0,0}; // Up down left right shoot bomb pause
  public float velocity[] = {0,0};
  public float maxVelocity = 5;
  public int weapon = 1;
  public int bullet = 0;
  public Player() {
  }
  public void resetControls() {
    for(int k = 0; k < controls.length; k++){
      controls[k] = 0;
    }
  }
  public void update() {
    // Every tick this is alled.
    // Decrement the velocity. Because fuck nuetan's laws.
    // System.out.println(velocity[0]+" x "+ velocity[1]);
    for(int i = 0; i < 2; i++) { 
      //if(velocity[i] > 0) { velocity[i] -= .02; }
      //else if(velocity[i] < 0) { velocity[i] += .02; }
      
      velocity[i] = (float)(velocity[i]/1.2);
      position[i] += velocity[i];
      if(position[i] < 0) { position[i] = 0; }
      else if(position[i] > 500) { position[i] = 500; }
    }
    
    
  }
}