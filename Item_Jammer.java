import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Item_Jammer extends Item{
    
    public Item_Jammer(Player ownerOfJammer, Game game){
        super(
            "Jammer", 
            "\033[38;5;202m" + "Jammer" + "\u001b[0m",
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("JAM", "JAMM", "JAMMER"))),
            ownerOfJammer, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseJammer){
        for (Player player : super.game().players()){
            if (!player.equals(playerAttemptingToUseJammer) && player.canUseItems()){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfJammer = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfJammer)){
            System.out.println(GeneralConversions.errorMessage("Nobody's items can be jammed!\n"));
            return;
        }
        
        ArrayList<Player> otherUnjammedPlayers = new ArrayList<>();
        // Remove the person prompted from the total list of players
        game.players().forEach(player -> {if (!ownerOfJammer.equals(player) && player.canUseItems()){ otherUnjammedPlayers.add(player);}});
        
        Player playerOrBotBeingJammed = null;
        
        // If there's only 1 other player to jam, options on who to jam are not needed
        if (otherUnjammedPlayers.size() == 1){
            playerOrBotBeingJammed = otherUnjammedPlayers.get(0);
        }
        
        else{
            // If a bot uses Jammer, the person being jammed is gotten from Bot class
            if (ownerOfJammer instanceof Bot bot){
                playerOrBotBeingJammed = bot.playerChosenForItem();
            }
            
            else{
                Scanner userInput = new Scanner(System.in);
                System.out.println("Who's items do you jam?");
                otherUnjammedPlayers.forEach(player -> System.out.println("- " + player.name()));
                System.out.print("Type Here --> ");
                String userChoice = GeneralConversions.formatString(userInput.nextLine());
                
                // Convert userChoice to a player or bot
                for (Player player : otherUnjammedPlayers){
                    if (player.formattedName().equals(userChoice)){
                        if (player instanceof Bot bot){
                            playerOrBotBeingJammed = bot;
                        }
                        else{
                            playerOrBotBeingJammed = player;
                        }
                        break;
                    }
                }
            }
        }
        
        if (otherUnjammedPlayers.contains(playerOrBotBeingJammed)){
            if (ownerOfJammer instanceof Bot){
                System.out.println(ownerOfJammer.name() + " jammed " + playerOrBotBeingJammed.name() + "'s items\n");
            }
            // Only clear the screen when a player uses the Jammer
            else{
                Console.clearScreen();
                System.out.println("You jammed " + playerOrBotBeingJammed.name() + "'s items\n");
            }
            playerOrBotBeingJammed.disableItemUse();
            ownerOfJammer.removeFromItems(this);
        }
        else{
            Console.clearScreen();
            System.out.println(GeneralConversions.errorMessage("Invalid Input\n"));
        }
    }
}
