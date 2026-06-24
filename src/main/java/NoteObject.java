import javax.swing.*;

public class NoteObject {
  private String name;
  private String description;
  private int x;
  private int y;


  public NoteObject(String name, String description, int x, int y){
    this.name = name;
    this.description = description;
    this.x = x;
    this.y = y;
  }

  public  String name(){return name;}
  public void name(String name){this.name=name;}

  public  String description(){return description;}
  public void description(String description){this.description=description;}

  public int x(){return x;}
  public void x(int x){this.x=x;}

  public int y(){return y;}
  public void y(int y){this.y=y;}
}
