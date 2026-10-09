import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class MainMenu
{
    public static void main(String[] args)
    {
        boolean hardMode = false;
        boolean singleplayer = false;
        ArrayList<Player> players = new ArrayList<>();
        Scanner userInput = new Scanner(System.in);
        ArrayList<String> playKeywords = new ArrayList<>(Arrays.asList("PLAY", "START", "BEGIN", "GO"));
        ArrayList<String> addPlayerKeywords = new ArrayList<>(Arrays.asList("PLAYER", "P", "ADDPLAYER", "NEWPLAYER", "ADDNEWPLAYER"));
        ArrayList<String> addBotKeywords = new ArrayList<>(Arrays.asList("BOT", "B", "ADDBOT", "NEWBOT", "ADDNEWBOT"));
        ArrayList<String> removeKeywords = new ArrayList<>(Arrays.asList("REMOVE", "R", "REMOVEPLAYER", "REMOVEBOT"));
        ArrayList<String> hardModeKeywords = new ArrayList<>(Arrays.asList("HARD", "HARDMODE", "H", "ACTIVATEHARDMODE", "DEACTIVATEHARDMODE"));
        
        while (true){
            promptTitleScreen(userInput);
            Console.clearScreen();
            String userChoice = "";
            
            while (true){
                userChoice = promptGameOptions(userInput, hardMode, players);
                Console.clearScreen();
                
                if (playKeywords.contains(userChoice)){
                    if (players.size() < 2){
                        System.out.println(GeneralConversions.errorMessage("Not enough players!\n"));
                        Console.wait(1000);
                    }
                    else{
                        break;
                    }
                }
                
                else{
                    // Add Player Setting
                    if (addPlayerKeywords.contains(userChoice)){
                        Player newPlayer = new Player(promptName(userInput));
                        
                        if (GeneralConversions.formatString(newPlayer.name()).equals("Y")){
                            System.out.println(GeneralConversions.errorMessage("That name is not allowed!\n"));
                            Console.wait(1000);
                        }
                        
                        boolean uniquePlayer = true;
                        for (Player player : players){
                            if (player.name().equals(newPlayer.name())){
                                Console.clearScreen();
                                System.out.println(GeneralConversions.errorMessage("Name is already taken!\n")); 
                                uniquePlayer = false;
                                Console.wait(1000);
                            }
                        }
                        if (uniquePlayer){
                            players.add(newPlayer); 
                            Console.clearScreen();
                        }
                    }
                    
                    // Add Bot Setting
                    else if (addBotKeywords.contains(userChoice)){
                        Player newBot = new Bot(promptName(userInput));
                        
                        if (GeneralConversions.formatString(newBot.name()).equals("Y")){
                            System.out.println(GeneralConversions.errorMessage("That name is not allowed!\n"));
                            Console.wait(1000);
                        }
                        
                        boolean uniqueBot = true;
                        for (Player player : players){
                            if (player.name().equals(newBot.name())){
                                Console.clearScreen();
                                System.out.println(GeneralConversions.errorMessage("Name is already taken!\n")); 
                                uniqueBot = false;
                                Console.wait(1000);
                            }
                        }
                        if (uniqueBot){
                            players.add(newBot); 
                            Console.clearScreen();
                        }
                    }
                    
                    // Remove Bot/Player setting
                    else if (removeKeywords.contains(userChoice)){
                        String nameOfPlayerToRemove = GeneralConversions.formatString(promptName(userInput));
                        Console.clearScreen();
                        
                        for (Player player : players){
                            if (player.formattedName().equals(nameOfPlayerToRemove)){
                                players.remove(player);
                                break;
                            }
                        }
                    }
                    
                    // Hard Mode Setting
                    else if (hardModeKeywords.contains(userChoice)){
                        hardMode = !hardMode;
                    }
                    
                    else{
                        System.out.println(GeneralConversions.errorMessage("Invalid Input!\n")); 
                        Console.wait(1000);
                    }
                }
            }
            
            int numberOfPlayers = 0;
            singleplayer = false;

            for (Player player : players){
                if (!(player instanceof Bot)){
                    numberOfPlayers++;
                }
            }
            
            if (numberOfPlayers == 1){
                singleplayer = true;
            }
            
            // NOTE: Singleplayer = one user & at least 1 bot
            // NOTE: Multiplayer = at least 2 users and can have bots OR only bots
    
            Game game = new Game(hardMode, singleplayer, players);
        }
    }
    
    public static String promptTitleScreen(Scanner userInput){
        System.out.println("Welcome to\n");

        System.out.println("░█▀▀█ ░█─░█ ░█▀▀█ ░█─▄▀ ░█▀▀▀█ ░█─░█ ░█▀▀▀█ ▀▀█▀▀\n" + 
                           "░█▀▀▄ ░█─░█ ░█─── ░█▀▄─ ─▀▀▀▄▄ ░█▀▀█ ░█──░█ ─░█──\n" + 
                           "░█▄▄█ ─▀▄▄▀ ░█▄▄█ ░█─░█ ░█▄▄▄█ ░█─░█ ░█▄▄▄█ ─░█──\n\n" + 
                           
                           "░█▀▀█ ░█▀▀▀█ ░█─░█ ░█─── ░█▀▀▀ ▀▀█▀▀ ▀▀█▀▀ ░█▀▀▀\n" + 
                           "░█▄▄▀ ░█──░█ ░█─░█ ░█─── ░█▀▀▀ ─░█── ─░█── ░█▀▀▀\n" + 
                           "░█─░█ ░█▄▄▄█ ─▀▄▄▀ ░█▄▄█ ░█▄▄▄ ─░█── ─░█── ░█▄▄▄");
        
        System.out.print("\n\n\nPress Enter to Start: ");
        return userInput.nextLine();
    }
    
    public static String promptGameOptions(Scanner userInput, boolean isHardMode, ArrayList<Player> players){
        System.out.println("Hard Mode: " + isHardMode + "\n");
        
        if (players.size() != 0){
            System.out.println("Players: ");
        }
        
        for (Player player : players){
            System.out.print(player.name() + " ($" + player.money() + ") - ");
            if (player instanceof Bot){
                System.out.println("Bot");
            }
            else{
                System.out.println("Player");
            }
        }
        if (players.size() != 0){
            System.out.println();
        }
        
        System.out.println("What do you do?");
        System.out.println("- Add Player (P)");
        System.out.println("- Add Bot (B)");
        System.out.println("- Remove Player/Bot (R)");
        System.out.println("- Activate/Deactivate Hard Mode (H)");
        System.out.println("- Play");
        System.out.print("Type Here: ");
        return GeneralConversions.formatString(userInput.nextLine());
    }
    
    public static String promptName(Scanner userInput){
        System.out.print("Enter Name: ");
        return userInput.nextLine();
    }
    
    public static void printTutorial(){
        System.out.println("A shotgun will be loaded with a random number of shells");
        System.out.println("A live is a shell that contains gunpowder; it will do damage");
        System.out.println("A blank is a shell without gunpowder; it does no damage");
        System.out.println("The order of the shells in the shotgun is random and unknown");
        System.out.println("Shooting someone with a live shell will take health from them");
        System.out.println("When someone is shot with any shell, the shotgun moves to the next person");
        System.out.println("You keep your turn if you shoot yourself with a blank");
        System.out.println("Items are given at the start of every round");
        System.out.println("Saw: doubles the damage of the next shot");
        System.out.println("Beer: ejects the loaded shell");
        System.out.println("Magnifying Glass: inspects the loaded shell");
        System.out.println("Lighter: gives you health");
        System.out.println("Handcuffs: skips a person's turn for two rounds");
    }
}
