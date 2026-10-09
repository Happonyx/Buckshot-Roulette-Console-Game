import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Item_Handcuffs extends Item{
    
    public Item_Handcuffs(Player ownerOfHandcuffs, Game game){
        super(
            "Handcuffs", 
            "\033[38;5;250m" + "Handcuffs" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("HAND", "CUFF", "CUFFS", "HANDCUFF", "HANDCUFFS"))),
            ownerOfHandcuffs, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseHandcuffs){
        for (Player player : super.game().players()){
            if (!player.equals(playerAttemptingToUseHandcuffs) && !player.isHandcuffed()){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfHandcuffs = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfHandcuffs)){
            System.out.println(GeneralConversions.errorMessage("Nobody can be handcuffed!\n"));
            return;
        }
        
        ArrayList<Player> otherUnhandcuffedPlayers = new ArrayList<>();
        // Remove the person prompted from the total list of players
        game.players().forEach(player -> {if (!ownerOfHandcuffs.equals(player) && !player.isHandcuffed()){ otherUnhandcuffedPlayers.add(player);}});
        
        Player playerOrBotBeingHandcuffed = null;
        
        // If there's only 1 other player to handcuff, options on who to handcuff are not needed
        if (otherUnhandcuffedPlayers.size() == 1){
            playerOrBotBeingHandcuffed = otherUnhandcuffedPlayers.get(0);
        }
        
        else{
            // If a bot uses handcuffs, the person being handcuffed is gotten from Bot class
            if (ownerOfHandcuffs instanceof Bot bot){
                playerOrBotBeingHandcuffed = bot.playerChosenForItem();
            }
            
            else{
                Scanner userInput = new Scanner(System.in);
                System.out.println("Who do you handcuff?");
                otherUnhandcuffedPlayers.forEach(player -> System.out.println("- " + player.name()));
                System.out.print("Type Here --> ");
                String userChoice = GeneralConversions.formatString(userInput.nextLine());
                
                // Convert userChoice to a player or bot
                for (Player player : otherUnhandcuffedPlayers){
                    if (player.formattedName().equals(userChoice)){
                        if (player instanceof Bot bot){
                            playerOrBotBeingHandcuffed = bot;
                        }
                        else{
                            playerOrBotBeingHandcuffed = player;
                        }
                        break;
                    }
                }
            }
        }
        
        if (otherUnhandcuffedPlayers.contains(playerOrBotBeingHandcuffed)){
            if (ownerOfHandcuffs instanceof Bot){
                System.out.println(ownerOfHandcuffs.name() + " handcuffed " + playerOrBotBeingHandcuffed.name() + "\n");
            }
            // Only clear the screen when a player uses the handcuffs
            else{
                Console.clearScreen();
                System.out.println("You handcuffed " + playerOrBotBeingHandcuffed.name() + "\n");
            }
            playerOrBotBeingHandcuffed.handcuff();
            ownerOfHandcuffs.removeFromItems(this);
        }
        else{
            Console.clearScreen();
            System.out.println(GeneralConversions.errorMessage("Invalid Input\n"));
        }
    }
}
