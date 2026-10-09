import java.util.ArrayList;
import java.util.Arrays;

public class Item_Beer extends Item{
    
    public Item_Beer(Player ownerOfBeer, Game game){
        super(
            "Beer", 
            "\033[38;5;130m" + "Beer" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("BEER"))),
            ownerOfBeer, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseBeer){
        return true;
    }
    
    @Override
    public void useItem(){
        Player ownerOfBeer = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfBeer)){
            System.out.println(GeneralConversions.errorMessage("Something went wrong with canBeUsed() for Beer\n"));
            return;
        }
        
        String bulletEjected = game.ejectBullet();
        
        if (ownerOfBeer instanceof Bot){
            System.out.println(ownerOfBeer.name() + " drank a beer, ejecting a " + bulletEjected + "\n");
        }
        else{
            System.out.println("You drank a beer, ejecting a " + bulletEjected + "\n");
        }
        ownerOfBeer.removeFromItems(this);
        ownerOfBeer.addToDrunkenLevel();
    }
}
