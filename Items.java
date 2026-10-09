import java.util.ArrayList;
import java.util.Scanner;

public abstract class Item{
    private String name;
    private String id;
    private ArrayList<String> keywords = new ArrayList<String>();
    private Player owner;
    private Game game;

    public Item(String name, String id, ArrayList<String> keywords, Player owner, Game game){
        this.name = name;
        this.id = id;
        this.keywords = keywords;
        this.owner = owner;
        this.game = game;
    }
    
    public void changeOwnerTo(Player newOwner){
        this.owner = newOwner;
    }
    
    public abstract boolean canBeUsedBy(Player player);
    public abstract void useItem();
    
    public String name(){
        return name;
    }
    
    public String id(){
        return id;
    }
    
    public ArrayList<String> keywords(){
        return keywords;
    }
    
    public Player owner(){
        return owner;
    }
    
    public Game game(){
        return game;
    }
}
