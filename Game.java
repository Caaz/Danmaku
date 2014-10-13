//

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel  {
  private long keyWait; // How long to wait before repeating keys
  private long delay = 0; // When it'll stop waiting. Don't change this.
  public boolean[] keys = new boolean[256];
  private static int SLEEP = 10;
  public Player player = new Player();
  public Enemy enemies[] = new Enemy[30];
  public Bullet bullets[] = new Bullet[255];
  public View view = new View();
  public Menu menu = new Menu();
  //public Game game = new Game;
  public Grid grid = new Grid();
  // In game shit
  public int state = 0; // State
  /*
    0 - Set Key controls
    1 - Main Menu
    2 - In Game
    3 - Game Over
  */
  public int level = 0; // Difficulty, sprites being used
  public long wave = 60000; // wave length in milliseconds
  private long calm = 5000; // Space between waves in milliseconds
  private long start = 0;
  private long spawn = 500; // Space between making new enemies
  private int group = 4; // How many enemies should be spawned with the same pattern
  public Game() {
		KeyListener listener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
        //System.out.println("Pressed : "+e.getKeyCode()+" - "+KeyEvent.getKeyText(e.getKeyCode()));
        keys[e.getKeyCode()] = true;
			}
			@Override
			public void keyReleased(KeyEvent e) {
        //System.out.println("Released: "+e.getKeyCode()+" - "+KeyEvent.getKeyText(e.getKeyCode()));
        keys[e.getKeyCode()] = false;
			}
		};
		addKeyListener(listener);
		setFocusable(true);
    setPreferredSize(new Dimension(640,480));
  }
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("Danmaku");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Game game = new Game();
    frame.setContentPane(game);
    //frame.setResizable(false);
    frame.pack();
    frame.setVisible(true);
    //System.out.println("Doing stuff");
    //game.setLevel(1);
		while (true) {
      long time = System.currentTimeMillis();
      game.update(time);
      game.repaint();
			Thread.sleep(SLEEP);
		}
  }
  public void update(long sysTime) {
    if(state == 0) {
      // Set controls
      if(sysTime > delay) {
        // Set
        int myKey = -1;
        for(int k = 0; k<keys.length; k++) { if(keys[k]){ myKey = k; } }
        if(myKey > -1) {
          // A key was caught!
          //System.out.println("Got "+myKey);
          // Check if the key code is in the controls already.
          boolean good = true;
          for(int j = 0; j < player.controls.length; j++) { if(myKey == player.controls[j]) { good = false; } }
          if(good) {
            // Set control
            player.controls[menu.selected] = myKey;
            // Go to next key.
            String controls[] = {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"};
            for(int l = 0; l < controls.length; l++) {
              if(player.controls[l] != 0) { controls[l] = "Got "+controls[l]; }
              else { controls[l] = "Press "+controls[l]; }
            }
            menu.setItems(controls);
            menu.next();
            // If we're at the end of the list, go to the menu
            if(menu.selected == 0) { state = 1; }
          }
        }
        delay = sysTime+keyWait;
      }
      if(menu.getLength() < 1) {
        String controls[] = {"Press UP", "Press DOWN", "Press LEFT", "Press RIGHT", "Press SHOOT", "Press BOMB", "Press SLOW", "Press PAUSE"};
        menu = new Menu(controls,0);
      }
    }
    else if(state == 1) {
      // Menu
    }
    else if(state == 2) {
      // In Game
      long lifeTime = sysTime - start;
      if(lifeTime < calm) {
        // Waiting.
      }
      else if((lifeTime - calm) < wave) {
        if(lifeTime%spawn == 0) {
          // spawn enemies
        }
      }
    }
  }
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    view.draw(this,g2d);
  }
}