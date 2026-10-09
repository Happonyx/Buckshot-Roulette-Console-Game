import java.util.ArrayList;
import java.util.Arrays;

public class Item_Inverter extends Item{
    
    public Item_Inverter(Player ownerOfInverter, Game game){
        super(
            "Inverter", 
            "\033[38;5;170m" + "Inverter" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("INV", "INVERT", "INVERTER"))),
            ownerOfInverter, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseInverter){
        return true;
    }
    
    @Override
    public void useItem(){
        Player ownerOfInverter = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfInverter)){
            System.out.println(GeneralConversions.errorMessage("Something went wrong with canBeUsed() for Inverter\n"));
            return;
        }
        
        game.invertLoadedBullet();
        
        if (ownerOfInverter instanceof Bot bot){
            System.out.println(bot.name() + " inverted the shell loaded in the shotgun\n");
        }
        else{
            System.out.println("You inverted the shell loaded in the shotgun\n");
        }
        ownerOfInverter.removeFromItems(this);
    }
}
