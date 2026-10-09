import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game{
    // Item Stats
    public final int turnsHandcuffsLast = 1;
    public final int healthExpiredMedicineGives = 2;
    public final int healthExpiredMedicineTakes = 1;
    public final int healthCurseTakesAway = 1;
    public final int baseBulletDamage = 1;
    public final int damageIncreasedBySaw = 1;
    public final int healthGainedByLighter = 1;
    public final int healthTotemGives = 2;
    public final int chanceOfGettingTotem = /* 1 in */ 40;
    public final int chanceOfGettingCurse = /* 1 in */ 25;
    public final int chanceOfLeaseAppearing = /* 1 in */ 7;
    public int turnsJammerLasts = 2; // total number of players + the int
    
    // Game Stats
    private final ArrayList<String> regularItems = new ArrayList<>(Arrays.asList("Saw", "Beer", "Magnifying Glass", "Lighter", "Handcuffs", "Bulletproof Vest", "Scissors"));
    //private final ArrayList<String> regularItems = new ArrayList<String>(Arrays.asList("Adrenaline"));
    private final ArrayList<String> hardModeItems = new ArrayList<>(Arrays.asList("Expired Medicine", "Burner Phone", "Inverter", "Adrenaline", "Fogger", "Jammer"));
    public final int maxBulletsPerReload = 8;
    public final int maxItems = 8;
    public final int roundsWonRequiredToWin = 3;
    public final int minimumStartingHealth = 3;
    public final int maximumStartingHealth = startingHealthForRound(roundsWonRequiredToWin);
    public final int healthGainedPerRound = 1;
    public final boolean singleplayer;
    private boolean hardMode;
    private Player user = null; // if it's not singleplayer, user will be null
    private long moneyPool;
    private int round = 1;
    private int bulletDamage = baseBulletDamage;
    private boolean firstItemAndHealthPrint = false;
    private boolean nextShellSetFogged = false;
    private boolean currentShellSetFogged = false;
    private boolean printedLeaseOfLiability = false;
    private ArrayList<String> bullets = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>(); // the turn will always be the first element
    private ArrayList<Player> deadPlayers = new ArrayList<>();
    private ArrayList<Player> originalPlayerOrder = new ArrayList<>();
    
    public Game(boolean hardMode, boolean singleplayer, ArrayList<Player> allPlayers){
        this.hardMode = hardMode;
        this.singleplayer = singleplayer;
        this.players.addAll(allPlayers);
        this.originalPlayerOrder.addAll(players);
        this.turnsJammerLasts = players.size() + turnsJammerLasts;

        // Gives every player access to this class and its methods
        players.forEach(player -> player.assignToGame(this));
        
        // If there is only one player in the game, it finds & saves the Player
        if (singleplayer){
            for (Player player : players){
                if (!(player instanceof Bot)){
                    this.user = player; break;
                }
            }
        }
        
        this.moneyPool = generateMoneyPool();
        
        while (true){
            startRound();
            
            // If the game is singleplayer, the game will run until the player reaches roundsWonRequiredToWin
            // The game will end if the player dies
            if (singleplayer){
                while (true){
                    // First, the game checks if a bot died
                    Player deadBot = checkIfABotDied();
                    if (deadBot != null){
                        System.out.print(deadBot.name() + " died");
                        
                        if (deadBot.hasTotem()){
                            Console.buildSuspense(3);
                            System.out.println("but their totem saves them!\n");
                            deadBot.setHealth(healthTotemGives);
                            deadBot.removeTotem();
                        }
                        else{
                            System.out.println("!\n");
                            deadPlayers.add(players.remove(players.indexOf(deadBot)));
                        }
                        Console.wait(2000);
                    }
                    
                    // Secondly, the game checks if the player won the round
                    if (players.size() == 1){
                        System.out.println("You won the round!");
                        Console.wait(1500);
                        
                        if (round == roundsWonRequiredToWin){
                            System.out.println("\nYou win!");
                            Console.wait(2000);
                            Console.clearScreen();
                            displayMoneyWon();
                            user.giveMoney(moneyPool);
                            endGame();
                            Console.wait(2000);
                            Console.clearScreen();
                            return;
                        }
                        round++;
                        resetDeadPlayerStats();
                        resetPlayerOrder();
                        resetPlayersHealth();
                        user.unhandcuff();
                        bullets.clear();
                        Console.clearScreen();
                        startRound();
                    }
                    
                    if (user.health() == 0){
                        System.out.print("You died");
                        if (user.hasTotem()){
                            Console.buildSuspense(3);
                            System.out.println("but your totem saves you!\n");
                            user.setHealth(healthTotemGives);
                            user.removeTotem();
                        }
                        else{
                            System.out.println("!\n");
                            endGame();
                            Console.wait(1000);
                            Console.clearScreen();
                            return;
                        }
                    }
                    
                    if (bullets.isEmpty()){
                        reloadShotgun();
                    }
                    takeTurn(players.get(0));
                    Console.wait(1000);
                }
            }
            
            // If the game is multiplayer, the game will run until one player or bot reaches roundsWonRequiredToWin
            else{
                while (true){
                    for (Player player : players){
                        if (player.health() == 0){
                            System.out.print(player.name() + " died");
                            
                            if (player.hasTotem()){
                                Console.buildSuspense(3);
                                System.out.println("but their totem saves them!\n");
                                player.setHealth(healthTotemGives);
                                player.removeTotem();
                            }
                            else{
                                System.out.println("!\n");
                                deadPlayers.add(players.remove(players.indexOf(player)));
                            }
                            Console.wait(2000);
                            break;
                        }
                    }
                    
                    if (players.size() == 1){
                        Player roundWinner = players.get(0);
                        System.out.println(roundWinner.name() + " won this round!\n");
                        roundWinner.addWin();
                        Console.wait(1500);
                        
                        if (roundWinner.wins() == roundsWonRequiredToWin){
                            System.out.println(roundWinner.name() + " won the game!");
                            Console.wait(2000);
                            Console.clearScreen();
                            displayMoneyWon();
                            roundWinner.giveMoney(moneyPool);
                            endGame();
                            Console.wait(2000);
                            Console.clearScreen();
                            return;
                        }
                        round++;
                        resetDeadPlayerStats();
                        resetPlayerOrder();
                        resetPlayersHealth();
                        bullets.clear();
                        Console.clearScreen();
                        startRound();
                    }
                    
                    if (bullets.isEmpty()){
                        reloadShotgun();
                    }
                    takeTurn(players.get(0));
                    Console.wait(1000);
                }
            }
        }
    }
    
    public void loadShotgunWithRandomBullets(){
        // Unfog the shell set if it was already fogged before being reloaded
        if (currentShellSetFogged){
            currentShellSetFogged = false;
        }
        
        // Fog the shell set if a fogger was used
        else if (nextShellSetFogged){
            currentShellSetFogged = true;
            nextShellSetFogged = false;
        }
        
        for (Player player : players){
            if (player instanceof Bot bot){
                bot.discardBulletKnowledge();
            }
        }
        
        int numberOfTotalBullets = (int)(Math.random() * (maxBulletsPerReload - 1) + 2);
        int numberOfLiveBullets = (int)(Math.random() * (numberOfTotalBullets - 1) + 1);
        int numberOfBlankBullets = numberOfTotalBullets - numberOfLiveBullets;

        printBulletInfo(numberOfTotalBullets, numberOfBlankBullets, numberOfLiveBullets);
        
        for (int loop = 0; loop < numberOfLiveBullets; loop++){
            bullets.add("live");
        }
        
        for (int loop = 0; loop < numberOfBlankBullets; loop++){
            bullets.add("blank");
        }
        Collections.shuffle(bullets);
    }
    
    public void generateItems(){
        firstItemAndHealthPrint = true;
        
        int numberOfNewItems = (int)(Math.random() * (round + 1) + 1);
        ArrayList<String> itemsPossible = new ArrayList<>();
        itemsPossible.addAll(regularItems);
        
        if (hardMode){
            itemsPossible.addAll(hardModeItems);
        }
        
        for (Player player : players){
            player.generateItems(numberOfNewItems, itemsPossible);
        }
    }
    
    public void takeTurn(Player playerTakingTurn){
        // Items will only print when it's a bot's turn if its the first item print of the round
        if (playerTakingTurn instanceof Bot && firstItemAndHealthPrint){
            for (Player player : players){
                player.printItemsAndHealth(1000);
                Console.wait(1000);
            }
            firstItemAndHealthPrint = false;
        }
        
        // If it's a player's turn, items will always be printed
        else if (!(playerTakingTurn instanceof Bot)){
            for (Player player : players){
                // Only print the items & health slowly when any new items are given
                if (firstItemAndHealthPrint){
                    player.printItemsAndHealth(1000);
                    Console.wait(1000);
                }
                else{
                    player.printItemsAndHealth(0);
                }
            }
            firstItemAndHealthPrint = false;
        }
        
        // Make a Player to unwrap what promptOptions returns if its a player
        Player playerOrBotBeingShot = null;
        Object thingChosen = playerTakingTurn.promptOptions();
        
        if (thingChosen instanceof Bot bot){
            playerOrBotBeingShot = bot;
        }
        else if (!(thingChosen instanceof Bot) && thingChosen instanceof Player player){
            playerOrBotBeingShot = player;
        }
        
        // Only clear the screen when a player uses an item
        if (!(playerTakingTurn instanceof Bot)){
            Console.clearScreen();
        }
        // Use item if the prompted options returned an Item
        if (thingChosen instanceof Item item){
            item.useItem();
        }
        
        // If the prompted options did not return an item, that means a person is going to be shot
        else{ 
            ArrayList<String> pronouns = assignPronouns(playerOrBotBeingShot, playerTakingTurn);
            System.out.print(pronouns.get(0) + " shot " + pronouns.get(1) + " with a");
            
            // If the person getting shot has a bulletproof vest on and is about to get shot with a live
            if (playerOrBotBeingShot.hasBulletproofVestEquipped() && bullets.get(0).equals("live")){
                System.out.print(" live"); Console.buildSuspense(2);
                System.out.println(" but " + pronouns.get(2) + " bulletproof vest absorbed it!\n");
                Console.wait(1000);
                System.out.println(pronouns.get(2).toUpperCase().substring(0, 1) + pronouns.get(2).substring(1) + " bulletproof vest tore from the bullet\n");
                playerOrBotBeingShot.setBulletproofVestEquipped(false);
            }
            // If the person getting shot can die from the bullet
            else if (playerOrBotBeingShot.health() - bulletDamage <= 0){
                Console.buildSuspense(3); System.out.println(" " + bullets.get(0) + "!\n");
                
                if (bullets.get(0).equals("live")){
                    playerOrBotBeingShot.setHealth(0);
                }
            }
            else{
                if (bullets.get(0).equals("live")){
                    playerOrBotBeingShot.setHealth(playerOrBotBeingShot.health() - bulletDamage);
                }
                System.out.println(" " + bullets.get(0) + "!\n");
            }
            
            // Update handcuffs & jammers for all players
            for (Player player : players){
                if (player.itemsHaveBeenJammedFor(turnsJammerLasts)){
                    player.enableItemUse();
                    Console.wait(1000);
                    System.out.println(player.name() + "'s items unjammed\n");
                }
                else if (!player.canUseItems()){
                    player.addRoundItemsJammed();
                }
                if (player.hasBeenHandcuffedFor(turnsHandcuffsLast)){
                    player.unhandcuff();
                    Console.wait(1000);
                    System.out.println(player.name() + "'s handcuffs snap off\n");
                }
                else if (player.isHandcuffed()){
                    player.addRoundHandcuffed();
                }
            }
            
            // Switch turn if the person taking the turn did not shoot themselves with a blank
            if (!(playerOrBotBeingShot.equals(playerTakingTurn) && bullets.get(0).equals("blank"))){
                switchTurn();
            }
            
            ejectBullet();
            bulletDamage = baseBulletDamage;
        }
    }
    
    public Player checkIfABotDied(){
        for (Player player : players){
            if (player instanceof Bot && player.health() == 0){
                return player;
            }
        }
        return null;
    }
    
    // Sets every player's health to what it would be for the current round
    // Method should be called AFTER the round is increased by 1
    public void resetPlayersHealth(){
        for (Player player : players){
            if (round >= roundsWonRequiredToWin){
                player.setHealth(maximumStartingHealth);
            }
            else{
                player.setHealth(startingHealthForRound(round));
            }
            player.setHealth(player.health() - (player.curseLevel() * healthCurseTakesAway));
        }
    }
    
    public ArrayList<String> assignPronouns(Player personGettingShot, Player playerTakingTurn){
        ArrayList<String> pronouns = new ArrayList<>();
        
        if (playerTakingTurn instanceof Bot){
            pronouns.add(0, playerTakingTurn.name());
        }
        else{
            pronouns.add(0, "You");
        }
        
        if (personGettingShot.equals(playerTakingTurn)){
            if (playerTakingTurn instanceof Bot){
                pronouns.add(1, "themself");
                pronouns.add(2, "their");
            }
            else{
                pronouns.add(1, "yourself");
                pronouns.add(2, "your");
            }
            return pronouns;
        }
        
        // If a player shoots a bot
        if (!(playerTakingTurn instanceof Bot) && personGettingShot instanceof Bot bot){
            pronouns.add(1, bot.name()); pronouns.add(2, "their");
        }
        // If a player shoots another player
        if (!(playerTakingTurn instanceof Bot) && !(personGettingShot instanceof Bot) && personGettingShot instanceof Player otherPlayer){
            pronouns.add(1, otherPlayer.name()); pronouns.add(2, "their");
        }
        // If a bot shoots a player in singleplayer
        if (singleplayer && playerTakingTurn instanceof Bot && !(personGettingShot instanceof Bot)){
            pronouns.add(1, "you"); pronouns.add(2, "your");
        }
        // If a bot shoots a bot in singleplayer
        if (singleplayer && playerTakingTurn instanceof Bot && personGettingShot instanceof Bot){
            pronouns.add(1, personGettingShot.name()); pronouns.add(2, "their");
        }
        // If a bot shoots a player or bot in multiplayer
        if (!singleplayer && playerTakingTurn instanceof Bot && personGettingShot instanceof Player otherBotOrPlayer){
            pronouns.add(1, otherBotOrPlayer.name()); pronouns.add(2, "their");
        }
        return pronouns;
    }
    
    public void printBulletInfo(int numberOfTotalBullets, int numberOfBlankBullets, int numberOfLiveBullets){
        System.out.println("\u001b[01m" + numberOfTotalBullets + "\u001b[0m shells are inserted into the shotgun in a random order.");
        Console.wait(2000);
        
        // Fog the shell set if a fogger was used previously
        if (currentShellSetFogged){
            System.out.println("Fog covers the shell set; it contains an unknown number of lives and blanks");
        }
        
        else{
            System.out.print("The shell set contains \033[38;5;248m\u001b[01m" + numberOfBlankBullets);
            
            switch (numberOfBlankBullets){
                case 1: System.out.print(" blank "); break;
                default: System.out.print(" blanks ");
            }
            System.out.print("\u001b[0m" + "and \033[38;5;196m\u001b[01m" + numberOfLiveBullets);
            switch (numberOfLiveBullets){
                case 1: System.out.println(" live"); break;
                default: System.out.println(" lives");
            }
        }
        System.out.println("\u001b[0m");
    }
    
    public void reloadShotgun(){
        System.out.print("Reloading shotgun"); 
        Console.buildSuspense(3);
        Console.clearScreen();
        loadShotgunWithRandomBullets();
        Console.wait(2000);
    }
    
    public void switchTurn(){
        Player firstPlayerInLine = players.get(0);
        Player nextPlayerInLine = players.get(1);

        while (true){
            players.remove(0);
            players.add(firstPlayerInLine);
            firstPlayerInLine = players.get(0);
            nextPlayerInLine = players.get(1);
            
            if (!firstPlayerInLine.isHandcuffed()){
                break;
            }
        }
    }
    
    public void startRound(){
        if (singleplayer && !printedLeaseOfLiability && round > 1){
            boolean showLeaseOfLiability = (int)(Math.random() * (chanceOfLeaseAppearing) + 1) == 1;
            
            if (showLeaseOfLiability){
                Console.clearScreen();
                GeneralConversions.printLeaseOfLiability();
                Console.clearScreen();
                printedLeaseOfLiability = true;
            }
        }
        
        System.out.println("Round " + GeneralConversions.romanNumeralValueOf(round) + "\n");
        Console.wait(2000);
        loadShotgunWithRandomBullets(); // Prints the shell set
        Console.wait(2000);
        generateItems(); // Prints nothing
    }
    
    public void displayMoneyWon(){
        double timeOfAnimation = 5.00;
        double timeElapsed = 0.00;
        double money = 0;
        
        while (timeElapsed < timeOfAnimation){
            Console.clearScreen();
            System.out.println("You won: \n");
            for (String row : GeneralConversions.bigBoldMoneyNumbersOf((int)money)){
                System.out.print(row);
            }
            Console.wait(10);
            timeElapsed += 0.01;
            money = moneyPool * (1 - Math.pow(1 - timeElapsed / timeOfAnimation, 3));
        }
    }
    
    public void resetPlayerOrder(){
        players.clear(); 
        deadPlayers.clear();
        players.addAll(originalPlayerOrder);
    }
    
    public void resetDeadPlayerStats(){
        deadPlayers.forEach(deadPlayer -> deadPlayer.resetRoundStats());
    }
    
    public void endGame(){
        players.addAll(deadPlayers);
        players.forEach(player -> {player.resetRoundStats(); player.resetCurseLevel();});
    }
    
    public int bulletDamage(){
        return bulletDamage;
    }
    
    public ArrayList<String> bullets(){
        return bullets;
    }
    
    public void invertLoadedBullet(){
        switch (bullets.get(0)){
            case "live": bullets.set(0, "blank"); break;
            case "blank": bullets.set(0, "live"); break;
        }
    }
    
    public long generateMoneyPool(){
        if (singleplayer){
            if (user.money() == 0){
                return (int)(Math.random() * (15000) + 5000);
            }
            return user.money(); // Double or Nothing
        }
        else{
            long lowestMoney = 0;
            
            for (Player player : players){
                if (player.money() < lowestMoney){
                    lowestMoney = player.money();
                }
            }
            return (int)(Math.random() * (lowestMoney) + 5000);
        }
    }
    
    public String ejectBullet(){
        String ejectedBullet = bullets.remove(0);
        
        for (Player player : players){
            if (player instanceof Bot bot){
                bot.updateKnownShellIndexes();

            }
        }
        return ejectedBullet;
    }
    
    public void setBulletDamage(int newBulletDamage){
        bulletDamage = newBulletDamage;
    }
    
    public ArrayList<Player> players(){
        return players;
    }
    
    public int round(){
        return round;
    }
    
    public void fogNextShellSet(){
        nextShellSetFogged = true;
    }
    
    public boolean shellSetIsFogged(){
        return currentShellSetFogged;
    }
    
    public boolean nextShellSetIsFogged(){
        return nextShellSetFogged;
    }
    
    public int numberOfBulletsThatAre(String bulletType){
        // Return -1 if the shell set is fogged
        if (currentShellSetFogged){
            return -1;
        }
        
        int number = 0;
        
        for (String bullet : bullets){
            if (bullet.equals(bulletType)){
                number++;
            }
        }
        return number;
    }
    
    // Returns the health that a player would normally start with on a round
    public int startingHealthForRound(int round){
        return minimumStartingHealth + healthGainedPerRound * (round - 1); 
    }
}
