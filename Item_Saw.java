import java.util.ArrayList;
import java.util.Arrays;

public class Item_Saw extends Item{
    
    public Item_Saw(Player ownerOfSaw, Game game){
        super(
            "Saw", 
            "\033[38;5;160m" + "Saw" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("SAW"))),
            ownerOfSaw, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseSaw){
        Game game = super.game();
        return game.bulletDamage() == game.baseBulletDamage;
    }
    
    @Override
    public void useItem(){
        Player ownerOfSaw = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfSaw)){
            System.out.println(GeneralConversions.errorMessage("Shotgun is already sawed off!\n"));
            return;
        }
        
        if (ownerOfSaw instanceof Bot){
            System.out.println(ownerOfSaw.name() + " sawed off the shotgun\n");
        }
        else{
            System.out.println("You sawed off the shotgun\n");
        }
        game.setBulletDamage(game.baseBulletDamage + game.damageIncreasedBySaw);
        ownerOfSaw.removeFromItems(this);
    }
}
