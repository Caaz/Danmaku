/*
  Mindmap thing : https://coggle.it/diagram/542f2d3b5467340e16003f60/0e32f3c089192d2795fa4eba7e5a640b08d8700187f7e204ce3eb58646b11f83
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel  {
  // -keyWait:long
  private long keyWait = 10; // How long to wait before repeating keys
  private long delay = 0; // When it'll stop waiting. Don't change this.
  public boolean[] keys = new boolean[256]; // This is for keypresses. Is the array too big? Maybe. Good news is boolean is literally a bit.
  private static int SLEEP = 10; // This is how long the thread sleeps between frames or whatever. Probably could be larger without any noticable impact.
  public Player player = new Player(); // This just sets up the player.
  public Level level = new Level(); // Level
  public View view = new View(); // This is a class used for drawing on screen. 
  public Menu menu = new Menu(); // This handles the menu!
  public Grid grid = new Grid(); // This is the grid! Also part of the menu. Probably important.
  // In game shit
  public int state = 0; // State, see below. //
  /*
    0 - Set Key controls
    1 - Main Menu
    2 - In Game
    3 - Game Over
    4 - Debug
  */
  public Game() {
		KeyListener listener = new KeyListener() {
      // This is an anonymous class. Or something. Essentially what it does is sets up key events, it changes the key element for whichever is true.
      // That's a terrible description.
      // It's so we know what buttons are being pressed.
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { keys[e.getKeyCode()] = true; }
			@Override
			public void keyReleased(KeyEvent e) { keys[e.getKeyCode()] = false; }
		};
		addKeyListener(listener); // This throws it onto the view.
		setFocusable(true); // This makes it so that you can type in it, kind of important.
    setPreferredSize(new Dimension(800,450)); // This sets the screen's width and height. Should probably actually make it a 16:9 ratio.
  }
  
  // This is where the program starts.
  public static void main(String[] args) throws InterruptedException {
    JFrame frame = new JFrame("Danmaku"); // This is the Window itself, with the title name as an argument.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // This makes the program terminate when the window is closed/
    Game game = new Game(); // Sets up the game object, which handles a few important variables and stuff. this may be a bad way to do it. It may be too late to change this.
    frame.setContentPane(game); // This throws the game into the window.
    frame.pack(); // this packs the window to the size of its elements. it's so that we don't need to explicitly set a size for the window, it's defined by its element.
    // Think of it as shrink wrap.
    frame.setVisible(true); // Sets the window to visible!
		while (true) {
      // This is the main game loop, here we set a time variable.
      long time = System.currentTimeMillis();
      
      // We make the whole game update with this time value to make things smooth.
      game.update(time);
      // Then we draw the screen, because we can assume something probably changed.
      game.repaint();
      // And now we sleep for a while to make everything smooth.
			Thread.sleep(SLEEP);
		}
  }
  
  // This is the game's update tick.
  public void update(long sysTime) {
    if(state == 0) {
      // Set controls
      if(menu.getLength() != 8) {
        System.out.println("Setting Key Config Menu");
        player.resetControls(); // Sets player controls all to 0.
        int menuPos[][] = {{3,2},{3,4},{3,3},{4,3},{4,2},{5,3},{4,4},{6,3}}; // This is an array of points on the hexagon grid, it's for placing the button names.
        String labels[] = {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"}; // This is an array of names of the player controls.
        menu = new Menu(labels,0); // This sets the new menu with the labels right here ^ and makes "UP" the selected element
        for(int menuItem = 0; menuItem < menuPos.length; menuItem++) {
          // This loops through each element of the menu array
          // menuItem is a number from 0 to the end of the list.
          Tile tile; // Here we're getting this tile thing temporarily
          // This uses the elements in menuPos to get points on the grid and assigns the tile variable to the tile at that grid position
          tile = grid.getTile(menuPos[menuItem][0],menuPos[menuItem][1]); 
          // Here we set that tile ID to menuItem, which is related to the labels.
          tile.setID(menuItem);
        }
      }
      if(sysTime > delay) {
        // This is where we check for a key.
        int myKey = -1; // This variable is initialized to -1 for a reason!
        // Here we go through the array of keys, and as soon as we find one that's pressed, we set myKey to it's element ID.
        for(int k = 0; k<keys.length; k++) { if(keys[k]){ myKey = k; } } 
        if(myKey > -1) {
          // if the myKey value is greater than -1, that means a key was pressed.
          boolean good = true; // Again this  variable is initialized to true for a reason.
          // Here we go through the player's controls, if the new key pressed is the same as another key, we set good to false. (not good)
          for(int j = 0; j < player.controls.length; j++) { if(myKey == player.controls[j]) { good = false; } } // Prevents duplicate key assignments.
          if(good) {
            // So we're good, this is a new key pressed.
            player.controls[menu.selected] = myKey; // Here we set the player control that's selected to the value of myKey. (our new key pressed)
            // we initialize this array to a list of the keys. 
            String controls[] = {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"};
            for(int l = 0; l < controls.length; l++) {
              // Here we're going through that array...
              
              // If the player control is set, we'll set the string to the name of the key "Shift, Enter, Up, Down, A, D" Etc.
              if(player.controls[l] != 0) { controls[l] = KeyEvent.getKeyText(player.controls[l]); }
              // If the control is the one that's (going to be) selected, we throw on "Press", so that the user knows what button they need to press.
              else if(l-1 == menu.selected) { controls[l] = "> "+controls[l]+" <"; }
            }
            // Here we update the menu's labels.
            menu.setItems(controls);
            // And we go on to the next menu element.
            menu.next();
            // If we're at the end of the list, lets go (back) to the main menu.
            if(menu.selected == 0) {
              // Here we're simply setting the menu to an empty array.
              String nully[] = {};
              menu = new Menu(nully,0);
              // And we reset the grid.
              grid = new Grid(); 
              // Now change the state to the menu!
              state = 1;
            }
          }
        }
        // This just handles the delay so we're not doing too much on the computer.
        delay = sysTime+keyWait;
      }
    }
    else if(state == 1) {
      // This state is the menu, so here's what happens.
      if(menu.getLength() != 4) {
        // This only happens if the menu hasn't been defined yet
        System.out.println("Setting Normal Menu");
        grid.makeHole(); // Here we're setting up this hole in the grid, so that you can see behind it.
        int menuPos[][] = {{6,2},{7,3},{6,4},{6,5}}; // Another list of points on the grid
        String labels[] = {"Controls","Start","Credits","Testing"}; // Labels of the menu.
        menu = new Menu(labels,1); // Setting up the menu, this time selecting start as the default selected value
        for(int menuItem = 0; menuItem < menuPos.length; menuItem++) {
          // Now here's where we set up the menu on the grid. See the key config part if you want to explain this again. Same exact stuff.
          Tile tile;
          tile = grid.getTile(menuPos[menuItem][0],menuPos[menuItem][1]);
          tile.setID(menuItem);
        }
      }
      if(sysTime > delay) {
        // Here's where actual menu happenings happen.
        int myKey = -1; // Again, a value set to -1
        for(int k = 0; k<keys.length; k++) { if(keys[k]){ myKey = k; } } // Here's where we look for a key being pressed and then update myKey as it's found.
        if(myKey > -1) {
          // A key was pressed if it's greater than -1.
          // Here's the player control list, for reference.
          // {"UP", "DOWN", "LEFT", "RIGHT", "SHOOT", "BOMB", "SLOW", "PAUSE"}
          //   0     1       2       3        4        5       6       7
          // So now that we know the positions of these buttons...
          if(myKey == player.controls[0]) { // So if myKey equals the 0th element, (Up) we go to the previous element
            delay = sysTime+keyWait*10; // Here we set some keybuffer, the reason for this is because the menu is insane to control if buttons are being spammed.
            menu.previous(); // And then we move the menu.
          }
          else if(myKey == player.controls[1]) { // Down!
            delay = sysTime+keyWait*10; // Again some framebuffer
            menu.next(); // Now we move next!
          }
          else if(myKey == player.controls[4]) { // Shoot! I asume this will be the button used for selecting or something.
          
            // Now lets figure out which menu item is actually selected
            // As of today, here's what the array looks like
            // {"Controls","Start","Credits","Testing"};
            //   0          1       2         3
            // The testing part will probably be removed, but we can keep the code there anyway.
            if(menu.selected == 0) {
              // 0 is Controls 
              grid = new Grid(); // Make a brand new grid! (to close the hole we created)
              state = 0; // set the state back to 0 again for making a new key config.
            }
            else if(menu.selected == 1) {
              // 1 is start!
              level = new Level(sysTime);
              state = 2;
            }
            
            // You'll notice there's no 2 here. SOON.
            
            else if(menu.selected == 3) {
              // 3 is the testing part
              state = 4;
            }
          }
          else {
            // uh.
            delay = sysTime+keyWait*10;
          }  // This
        }     // is
        else { // redundant.
          delay = sysTime+keyWait*10;
        }
      }
    }
    else if(state == 2) {
      // In Game which never happens.
      // We'll do something here later.
      player.update(sysTime, this);
      level.update(sysTime, this);
    }
    else if(state == 4) {
      // Testing grounds.
      // Actual update shit
      player.update(sysTime, this);
      level.update(sysTime, this);
    }
  }
  
  
  // Here's where drawing happens!
  @Override
  public void paintComponent(Graphics g) {
    // Or is it...
    // We make a graphics 2d object from our boring ol graphics object
    Graphics2D g2d = (Graphics2D)g; // I don't know why it looks this way... It could probably be different...
    // Now lets just pass this to the view class
    view.draw(this,g2d);
    // Why? Because drawing code is fucking HUGE.
  }
}