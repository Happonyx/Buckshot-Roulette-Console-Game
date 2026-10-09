import java.util.ArrayList;
import java.util.Arrays;

public class Item_Lighter extends Item{
    
    public Item_Lighter(Player ownerOfLighter, Game game){
        super(
            "Lighter", 
            "\033[38;5;71m" + "Lighter" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("LIGHT", "LIGHTER"))),
            ownerOfLighter, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseLighter){
        Game game = super.game();
        
        // Checks if the lighter would not put the person over the max health they can have
        if (playerAttemptingToUseLighter.health() + game.healthGainedByLighter <= game.startingHealthForRound(game.round()) - (playerAttemptingToUseLighter.curseLevel() * game.healthCurseTakesAway)){
            return true;
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfLighter = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfLighter)){
            System.out.println(GeneralConversions.errorMessage("You already have max health!\n"));
            return;
        }
        
        if (ownerOfLighter instanceof Bot){
            System.out.println(ownerOfLighter.name() + " lit a cigarette, gaining a life\n");
        }
        else{
            System.out.println("You lit a cigarette, gaining a life\n");
        }
        ownerOfLighter.setHealth(ownerOfLighter.health() + game.healthGainedByLighter);
        ownerOfLighter.removeFromItems(this);
    }
}
