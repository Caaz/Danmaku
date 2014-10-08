public class Menu {
  public int selected = 0; // which menu item is selected
  public String items[] = new String[10]; // Menu items
  
  // Overloading the constructor! 
  public Menu(String menu[], int select) { setItems(menu); this.selected = select; }
  public Menu(int select) { this.selected = select; }
  public Menu() { }
  public void next() {
    selected++;
    if((selected == items.length) || (items[selected].length() == 0)) { selected = 0; }
  }
  public void previous() {
    selected--;
    if(selected == -1) { selected = getLength()-1; }
  }
  public String get(int index) {
    if((index < 0) || (index >= getLength())) {
      return "Out of bounds";
    }
    return items[index];
  }
  public int getLength() {
    int i;
    for(i = 0; i < items.length; i++) {
      try {
        if(items[i].length() == 0) { break; }
      } catch(NullPointerException e) {
        break;
      }
    }
    return i;
  }
  public void setItems(String menu[]) {
    for(int i = 0; i < items.length; i++) {
      if(i < menu.length) { items[i] = menu[i]; }
      else { items[i] = ""; }
    }
  }
}