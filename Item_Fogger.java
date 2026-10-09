import java.util.ArrayList;
import java.util.Arrays;

public class Item_Fogger extends Item{
    
    public Item_Fogger(Player ownerOfFogger, Game game){
        super(
            "Fogger", 
            "\033[38;5;100m" + "Fogger" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("FOG", "FOGGER"))),
            ownerOfFogger, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseFogger){
        return !super.game().nextShellSetIsFogged();
    }
    
    @Override
    public void useItem(){
        Player ownerOfFogger = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfFogger)){
            System.out.println(GeneralConversions.errorMessage("The next shell set has already been fogged!\n"));
            return;
        }
        
        if (ownerOfFogger instanceof Bot){
            System.out.println(ownerOfFogger.name() + " clicked a fogger open, fogging the next shell set\n");
        }
        else{
            System.out.println("You clicked a fogger open, fogging the next shell set\n");
        }
        game.fogNextShellSet();
        ownerOfFogger.removeFromItems(this);
    }
}
