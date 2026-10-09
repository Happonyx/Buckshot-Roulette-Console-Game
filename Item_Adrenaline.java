import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Item_Adrenaline extends Item{
    
    public Item_Adrenaline(Player ownerOfAdrenaline, Game game){
        super(
            "Adrenaline", 
            "\033[38;5;197m" + "Adrenaline" + "\u001b[0m", 
            GeneralConversions.addUseToKeywords(new ArrayList<>(Arrays.asList("ADREN", "ADRENALINE"))),
            ownerOfAdrenaline, 
            game
        );
    }
    
    @Override
    public boolean canBeUsedBy(Player playerAttemptingToUseAdrenaline){
        Player ownerOfAdrenaline = super.owner();
        Game game = super.game();
        
        // A player cannot attempt to steal & use another player's Adrenaline
        if (!playerAttemptingToUseAdrenaline.equals(ownerOfAdrenaline)){
            return false;
        }
        
        for (Player player : game.players()){
            if (!player.equals(ownerOfAdrenaline)){
                for (Item item : player.items()){
                    if (item.canBeUsedBy(ownerOfAdrenaline)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void useItem(){
        Player ownerOfAdrenaline = super.owner();
        Game game = super.game();
        
        Player playerGettingStolenFrom = null;
        Item itemGettingStolen = null;
        
        // Assume that the bot already knows who it's using it on & what item its stealing
        if (ownerOfAdrenaline instanceof Bot bot){
            playerGettingStolenFrom = bot.playerChosenForItem();
            itemGettingStolen = bot.itemChosenToSteal();
            
            System.out.println(bot.name() + " stole " + playerGettingStolenFrom.name() + "'s " + itemGettingStolen.id());
            itemGettingStolen.changeOwnerTo(bot);
            bot.addItem(itemGettingStolen);
            playerGettingStolenFrom.removeFromItems(itemGettingStolen);
            ownerOfAdrenaline.removeFromItems(this);
        }
        
        else{
            Scanner userInput = new Scanner(System.in);
            // Creates an arraylist with every person that has at least 1 item that can be stolen
            ArrayList<Player> otherPlayersWithStealableItems = new ArrayList<>();
            
            for (Player player : game.players()){
                if (!player.equals(ownerOfAdrenaline)){
                    for (Item item : player.items()){
                        if (item.canBeUsedBy(ownerOfAdrenaline)){
                            otherPlayersWithStealableItems.add(player);
                            break;
                        }
                    }
                }
            }
            
            // Error if there's nobody to steal from
            if (otherPlayersWithStealableItems.isEmpty()){
                System.out.println(GeneralConversions.errorMessage("No items can be currently stolen!\n"));
                return;
            }
            
            System.out.println("Who's items do you steal from?\n");
            
            for (Player otherPlayer : otherPlayersWithStealableItems){
                ArrayList<Item> stealableItems = new ArrayList<>();
                otherPlayer.items().forEach(item -> {if (item.canBeUsedBy(ownerOfAdrenaline)){ stealableItems.add(item);}});
                
                System.out.print(otherPlayer.name() + "'s items: ");
                
                for (Item item : stealableItems){
                    System.out.print(item.id());
                    if (stealableItems.indexOf(item) != stealableItems.size() - 1){
                        System.out.print(", ");
                    }
                }
                System.out.println("\n");
            }
            
            System.out.print("Type Name Here --> ");
            String userChoice = GeneralConversions.formatString(userInput.nextLine());
            boolean playerFound = false;
            
            for (Player otherPlayer : otherPlayersWithStealableItems){
                if (GeneralConversions.formatString(otherPlayer.name()).equals(userChoice)){
                    playerFound = true;
                    playerGettingStolenFrom = otherPlayer;
                    break;
                }
            }
            
            if (!playerFound){
                Console.clearScreen();
                System.out.println(GeneralConversions.errorMessage("Invalid Input!\n"));
                return;
            }
            // Past this point, a person to steal from has been identified
            
            Console.clearScreen();
            ArrayList<Item> stealableItems = new ArrayList<>();
            playerGettingStolenFrom.items().forEach(item -> {if (item.canBeUsedBy(ownerOfAdrenaline)){ stealableItems.add(item);}});
            
            // If there's only 1 item the person can steal, they do not have to type the item name
            if (stealableItems.size() == 1){
                itemGettingStolen = stealableItems.get(0);
            }
            
            else{
                System.out.println("Which item do you steal?");
                stealableItems.forEach(stealableItem -> System.out.println("- " + stealableItem.id()));
                System.out.print("Enter Item Here --> ");
                userChoice = GeneralConversions.formatString(userInput.nextLine());
                boolean itemFound = false;
                
                for (Item stealableItem : stealableItems){
                    if (stealableItem.keywords().contains(userChoice)){
                        itemFound = true;
                        itemGettingStolen = stealableItem;
                        break;
                    }
                }
                
                if (!itemFound){
                    Console.clearScreen();
                    System.out.println(GeneralConversions.errorMessage("Invalid Input!\n"));
                    return;
                }
            }
            // Adrenaline is used here
            ArrayList<String> pronouns = game.assignPronouns(playerGettingStolenFrom, ownerOfAdrenaline);
            
            Console.clearScreen();
            System.out.println(pronouns.get(0) + " stole " + itemGettingStolen.id() + " from " + pronouns.get(1) + "\n");
            playerGettingStolenFrom.removeFromItems(itemGettingStolen);
            ownerOfAdrenaline.addItem(itemGettingStolen);
            itemGettingStolen.changeOwnerTo(ownerOfAdrenaline);
            Console.wait(1000);
            
            itemGettingStolen.useItem();
            ownerOfAdrenaline.removeFromItems(this);
        }
    }
}
