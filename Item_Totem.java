import java.util.ArrayList;
import java.util.Arrays;

public class Item_Totem extends Item{
    
    public Item_Totem(Player ownerOfTotem, Game game){
        super(
            "Totem", 
            "\033[38;5;226m" + "T" + "\033[38;5;220m" + "o" + "\033[38;5;214m" + "t" + "\033[38;5;208m" + "e" + "\033[38;5;202m" + "m" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("TOTEM"))),
            ownerOfTotem, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseTotem){
        return false;
    }
    
    @Override
    public void useItem(){
        System.out.println(GeneralConversions.errorMessage("Totem is not usable!\n"));
    }
}
