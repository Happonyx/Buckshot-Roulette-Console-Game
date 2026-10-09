import java.util.ArrayList;
import java.util.Arrays;

public class Item_BulletproofVest extends Item{
    
    public Item_BulletproofVest(Player ownerOfBulletproofVest, Game game){
        super(
            "Bulletproof Vest", 
            "\033[38;5;69m" + "Bulletproof Vest" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("BULLET", "BULLETPROOF", "VEST", "BULLETVEST", "BULLETPROOFVEST"))),
            ownerOfBulletproofVest, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseBulletproofVest){
        if (!playerAttemptingToUseBulletproofVest.hasBulletproofVestEquipped()){
            return true;
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfBulletproofVest = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfBulletproofVest)){
            System.out.println(GeneralConversions.errorMessage("You already have a bulletproof vest on!\n"));
            return;
        }
        
        if (ownerOfBulletproofVest instanceof Bot){
            System.out.println(ownerOfBulletproofVest.name() + " put a bulletproof vest on\n");
        }
        else{
            System.out.println("You put a bulletproof vest on\n");
        }
        ownerOfBulletproofVest.setBulletproofVestEquipped(true);
        ownerOfBulletproofVest.removeFromItems(this);
    }
}
