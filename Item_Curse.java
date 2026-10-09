import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Item_Curse extends Item{
    
    public Item_Curse(Player ownerOfCurse, Game game){
        super(
            "Curse", 
            "\033[38;5;201m" + "C" + "\033[38;5;201m" + "u" + "\033[38;5;165m" + "r" + "\033[38;5;165m" + "s" + "\033[38;5;129m" + "e" + "\u001b[0m",
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("CURSE"))),
            ownerOfCurse, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseCurse){
        return true;
    }
    
    @Override
    public void useItem(){
        Player ownerOfCurse = super.owner();
        Player personGettingCursed = null;
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfCurse)){
            System.out.println(GeneralConversions.errorMessage("Something went wrong with canBeUsed() for Curse\n"));
            return;
        }
        
        if (ownerOfCurse instanceof Bot bot){
            ArrayList<String> pronouns = game.assignPronouns(bot.playerChosenForItem(), bot);
            System.out.println(pronouns.get(0) + " placed a curse on " + pronouns.get(1) + "\n");
            personGettingCursed = bot.playerChosenForItem();
        }
        else{
            ArrayList<Player> otherPlayers = new ArrayList<>();
            // Remove the person prompted from the total list of players
            game.players().forEach(player -> {if (!player.equals(ownerOfCurse)){otherPlayers.add(player);}});
            
            if (otherPlayers.size() == 1){
                personGettingCursed = otherPlayers.get(0);
            }
            
            else{
                Scanner input = new Scanner(System.in);
                System.out.println("Who do you curse?");
                
                for (Player otherPlayer : otherPlayers){
                    System.out.println("- " + otherPlayer.name());
                }
                System.out.print("Type Name Here --> ");
                String userChoice = GeneralConversions.formatString(input.nextLine());
                
                for (Player otherPlayer : otherPlayers){
                    if (otherPlayer.formattedName().equals(userChoice)){
                        personGettingCursed = otherPlayer;
                        break;
                    }
                }
                
                // If a player was never found
                if (personGettingCursed == null){
                    Console.clearScreen();
                    System.out.println(GeneralConversions.errorMessage("Invalid Input!\n"));
                    return;
                }
            }
            // Past this point, a player to be cursed has been identified
            
            Console.clearScreen();
            ArrayList<String> pronouns = game.assignPronouns(personGettingCursed, ownerOfCurse);
            System.out.println(pronouns.get(0) + " placed a curse on " + pronouns.get(1) + "\n");
        }
        
        personGettingCursed.curse();
        ownerOfCurse.removeFromItems(this);
    }
}
