import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Item_Scissors extends Item{
    
    public Item_Scissors(Player ownerOfScissors, Game game){
        super(
            "Scissors", 
            "\033[38;5;147m" + "Scissors" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("SCIS", "SCISS", "SCISSORS", "SCISSOR"))),
            ownerOfScissors, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseScissors){
        Game game = super.game();
        
        for (Player player : game.players()){
            if (player.hasBulletproofVestEquipped()){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfScissors = super.owner();
        Game game = super.game();
        
        if (!canBeUsedBy(ownerOfScissors)){
            System.out.println(GeneralConversions.errorMessage("Nobody has a bulletproof vest to cut!\n"));
            return;
        }
        
        ArrayList<Player> otherPlayersWithBulletproofVestEquipped = new ArrayList<>();
        // Remove the person prompted from the total list of players
        game.players().forEach(player -> {if (!ownerOfScissors.equals(player) && player.hasBulletproofVestEquipped()){ otherPlayersWithBulletproofVestEquipped.add(player);}});
        
        Player playerOrBotBeingCut = null;
        
        // If there's only 1 other player to cut, options on who to cut are not needed
        if (otherPlayersWithBulletproofVestEquipped.size() == 1){
            playerOrBotBeingCut = otherPlayersWithBulletproofVestEquipped.get(0);
        }
        
        else{
            // If a bot uses scissors, the person the bot chose is gotten from Bot class
            if (ownerOfScissors instanceof Bot bot){
                playerOrBotBeingCut = bot.playerChosenForItem();
            }
            
            else{
                Scanner userInput = new Scanner(System.in);
                System.out.println("Who's bulletproof vest do you cut?");
                otherPlayersWithBulletproofVestEquipped.forEach(player -> System.out.println("- " + player.name()));
                System.out.print("Type Here --> ");
                String userChoice = GeneralConversions.formatString(userInput.nextLine());
                
                // Convert userChoice to a player or bot
                for (Player player : otherPlayersWithBulletproofVestEquipped){
                    if (player.formattedName().equals(userChoice)){
                        if (player instanceof Bot bot){
                            playerOrBotBeingCut = bot;
                        }
                        else{
                            playerOrBotBeingCut = player;
                        }
                        break;
                    }
                }
            }
        }
        
        if (otherPlayersWithBulletproofVestEquipped.contains(playerOrBotBeingCut)){
            if (ownerOfScissors instanceof Bot){
                System.out.println(ownerOfScissors.name() + " sliced " + playerOrBotBeingCut.name() + "'s bulletproof vest\n");
            }
            // Only clear the screen when a player uses the scissors
            else{
                Console.clearScreen();
                System.out.println("You sliced " + playerOrBotBeingCut.name() + "'s bulletproof vest\n");
            }
            playerOrBotBeingCut.setBulletproofVestEquipped(false);
            ownerOfScissors.removeFromItems(this);
        }
        else{
            Console.clearScreen();
            System.out.println(GeneralConversions.errorMessage("Invalid Input\n"));
        }
    }
}
