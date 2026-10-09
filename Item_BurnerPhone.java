import java.util.ArrayList;
import java.util.Arrays;

public class Item_BurnerPhone extends Item{
    
    public Item_BurnerPhone(Player ownerOfBurnerPhone, Game game){
        super(
            "Burner Phone", 
            "\033[38;5;141m" + "Burner Phone" + "\u001b[0m",
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("BURN", "BURNER", "PHONE", "BURNERPHONE"))),
            ownerOfBurnerPhone, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseBurnerPhone){
        return true;
    }
    
    @Override
    public void useItem(){
        Player ownerOfBurnerPhone = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfBurnerPhone)){
            System.out.println(GeneralConversions.errorMessage("Something went wrong with canBeUsed() for Burner Phone\n"));
            return;
        }
        
        int randomBulletIndex = (int)(Math.random() * (game.bullets().size()) + 0);
        String numberSuffix = "";
        
        if (randomBulletIndex <= 2){
            numberSuffix = GeneralConversions.numberSuffixes.get(randomBulletIndex);
        }
        else{
            numberSuffix = "th";
        }
        
        if (ownerOfBurnerPhone instanceof Bot bot){
            System.out.println(bot.name() + " gets a mysterious call from an anonymous person");
            Console.wait(1000);
            System.out.println("They tell them something not quite intellegible\n");
            bot.addIndexOfKnownBullet(randomBulletIndex);
            
        }
        else{
            System.out.println("You get a mysterious call from an anonymous person");
            Console.wait(1000);
            System.out.println("They tell you the " + (randomBulletIndex + 1) + numberSuffix + " shell is a " + game.bullets().get(randomBulletIndex) + "\n");
        }
        ownerOfBurnerPhone.removeFromItems(this);
    }
}
