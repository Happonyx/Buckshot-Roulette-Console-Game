import java.util.ArrayList;
import java.util.Arrays;

public class Item_ExpiredMedicine extends Item{
    
    public Item_ExpiredMedicine(Player ownerOfExpiredMedicine, Game game){
        super(
            "Expired Medicine", 
            "\033[38;5;223m" + "Expired Medicine" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("EXPIRED", "MEDS", "MEDICINE", "EXPIREDMEDS", "EXPIREDMEDICINE", "DRUGS"))),
            ownerOfExpiredMedicine, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseExpiredMedicine){
        Game game = super.game();
        
        // Checks if the expired medicine would not put the person over the max health they can have
        if (playerAttemptingToUseExpiredMedicine.health() + game.healthExpiredMedicineGives <= game.startingHealthForRound(game.round()) - (playerAttemptingToUseExpiredMedicine.curseLevel() * game.healthCurseTakesAway)){
            return true;
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfExpiredMedicine = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfExpiredMedicine)){
            System.out.println(GeneralConversions.errorMessage("Expired Medicine may put you over max health!\n"));
            return;
        }
        
        int healthTaken = game.healthExpiredMedicineTakes;
        int healthGiven = game.healthExpiredMedicineGives;
        int theirHealth = ownerOfExpiredMedicine.health();

        boolean giveHealth = (int)(Math.random() * (2) + 1) == 1;
        
        if (ownerOfExpiredMedicine instanceof Bot){
            System.out.println(ownerOfExpiredMedicine.name() + " popped expired pills into their mouth");
            Console.wait(1500);
            System.out.print("The pills"); Console.buildSuspense(3);
            
            if (giveHealth){
                System.out.println(" gave them " + healthGiven + " health!\n");
                ownerOfExpiredMedicine.setHealth(theirHealth + healthGiven);
            }
            else{
                System.out.println(" took " + healthTaken + " health!\n");
                ownerOfExpiredMedicine.setHealth(theirHealth - healthTaken);
            }
        }
        else{
            System.out.println("You popped expired pills into your mouth");
            Console.wait(1500);
            System.out.print("The pills"); Console.buildSuspense(3);
            
            if (giveHealth){
                System.out.println(" gave you " + healthGiven + " health!\n");
                ownerOfExpiredMedicine.setHealth(theirHealth + healthGiven);
            }
            else{
                System.out.println(" took " + healthTaken + " health!\n");
                ownerOfExpiredMedicine.setHealth(theirHealth - healthTaken);
            }
        }
        // Ensure their health does not go below 0
        if (ownerOfExpiredMedicine.health() < 0){
            ownerOfExpiredMedicine.setHealth(0);
        }
        ownerOfExpiredMedicine.removeFromItems(this);
    }
}
