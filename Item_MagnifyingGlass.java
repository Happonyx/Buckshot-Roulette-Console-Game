import java.util.ArrayList;
import java.util.Arrays;

public class Item_MagnifyingGlass extends Item{
    
    public Item_MagnifyingGlass(Player ownerOfMagnifyingGlass, Game game){
        super(
            "Magnifying Glass", 
            "\033[38;5;221m" + "Magnifying Glass" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("MAG", "MAGNIFYING", "GLASS", "MAGNIFYINGGLASS"))),
            ownerOfMagnifyingGlass, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseMagnifyingGlass){
        return true;
    }
    
    @Override
    public void useItem(){
        Player ownerOfMagnifyingGlass = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfMagnifyingGlass)){
            System.out.println(GeneralConversions.errorMessage("Something went wrong with canBeUsed() for Magnifying Glass\n"));
            return;
        }
        
        if (ownerOfMagnifyingGlass instanceof Bot bot){
            System.out.println(bot.name() + " inspected the loaded shell\n");
            bot.addIndexOfKnownBullet(0);
        }
        else{
            System.out.println("You inspected the loaded shell; it's a " + game.bullets().get(0) + "\n");
        }
        ownerOfMagnifyingGlass.removeFromItems(this);
    }
}
