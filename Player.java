import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private Scanner userInput = new Scanner(System.in);
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> newestItems = new ArrayList<>();
    private int health;
    private boolean handcuffed;
    private boolean bulletproofVestEquipped;
    private int roundsHandcuffed;
    private boolean hasTotem;
    private int numberOfWins;
    private Game game;
    private String name; // raw name
    private String formattedName; // raw name but all capitalized and no spaces
    private long money;
    private int drunkenLevel;
    private int curseLevel;
    private boolean canUseItems;
    private int turnsItemsJammed;
    
    public Player(String name){
        this.handcuffed = false;
        this.bulletproofVestEquipped = false;
        this.roundsHandcuffed = 0;
        this.hasTotem = false;
        this.numberOfWins = 0;
        this.name = name;
        this.formattedName = GeneralConversions.formatString(name);
        this.money = 0;
        this.drunkenLevel = 0;
        this.curseLevel = 0;
        this.canUseItems = true;
        this.turnsItemsJammed = 0;
    }
    
    public void assignToGame(Game game){
        this.game = game;
        this.health = game.minimumStartingHealth;
    }
    
    public void addToDrunkenLevel(){
        drunkenLevel++;
    }
    
    public void curse(){
        curseLevel++;
    }
    
    public void generateItems(int numberOfNewItems, ArrayList<String> itemsPossible){
        for (int loop = 0; loop < numberOfNewItems; loop++){
            if (items.size() + newestItems.size() < game.maxItems){
                boolean playerGetsTotem = (int)(Math.random() * (game.chanceOfGettingTotem) + 1) == 1;
                boolean playerGetsCurse = (int)(Math.random() * (game.chanceOfGettingCurse) + 1) == 1;
                
                if (playerGetsTotem && !hasTotem){
                    Item totem = new Item_Totem(this, game);
                    newestItems.add(totem);
                    hasTotem = true;
                }
                else if (playerGetsCurse){
                    Item curse = new Item_Curse(this, game);
                    newestItems.add(curse);
                }
                else{
                    int randomItemIndex = (int)(Math.random() * (itemsPossible.size()) + 0);
                    Item item = null;
                    
                    switch (itemsPossible.get(randomItemIndex)){
                        case "Saw":              item = new Item_Saw(this, game); break;
                        case "Beer":             item = new Item_Beer(this, game); break;
                        case "Magnifying Glass": item = new Item_MagnifyingGlass(this, game); break;
                        case "Lighter":          item = new Item_Lighter(this, game); break;
                        case "Handcuffs":        item = new Item_Handcuffs(this, game); break;
                        case "Bulletproof Vest": item = new Item_BulletproofVest(this, game); break;
                        case "Scissors":         item = new Item_Scissors(this, game); break;
                        case "Expired Medicine": item = new Item_ExpiredMedicine(this, game); break;
                        case "Burner Phone":     item = new Item_BurnerPhone(this, game); break;
                        case "Inverter":         item = new Item_Inverter(this, game); break;
                        case "Adrenaline":       item = new Item_Adrenaline(this, game); break;
                        case "Fogger":           item = new Item_Fogger(this, game); break;
                        case "Jammer":           item = new Item_Jammer(this, game); break;
                    }
                    newestItems.add(item);
                }
            }
        }
    }
    
    public void removeFromItems(Item itemToRemove){
        /* idk why i did this but theres probably a reason
        try{
            items.remove(items.indexOf(itemToRemove));
        } catch (Exception itemWasNotFound){}
        */
        items.remove(items.indexOf(itemToRemove));
    }
    
    public void resetRoundStats(){
        unhandcuff();
        items.clear();
        newestItems.clear();
        drunkenLevel = 0;
    }
    
    public void addItem(Item item){
        items.add(item);
    }
    
    public Item getItem(String itemName){
        for (Item item : items){
            if (item.name().equals(itemName)){
                return item;
            }
        }
        return null;
    }
    
    public boolean hasItem(String itemName){
        for (Item item : items){
            if (item.name().equals(itemName)){
                return true;
            }
        }
        return false;
    }
    
    public void giveMoney(long money){
        this.money += money;
    }
    
    public void printItemsAndHealth(int milliseconds){
        Player playerTakingTurn = game.players().get(0);
        
        if (this.equals(playerTakingTurn)){
            if (this instanceof Bot){
                System.out.println("It's " + name + "'s turn!\n");
                System.out.print(name + "'s items: ");
            }
            else{
                System.out.println("It's your turn, " + name + "!\n");
                System.out.print("Your items: ");
            }
        }
        else{
            System.out.print(name + "'s items: ");
        }
        
        if (!canUseItems){
            System.out.print("\033[38;5;196m" + "[⊘ ᴅɪꜱᴀʙʟᴇᴅ] " + "\u001b[0m");
        }
        
        // Prints the items they had before getting new ones
        for (Item item : items){
            System.out.print(item.id());
            if (items.indexOf(item) != items.size() - 1 || !newestItems.isEmpty()){
                System.out.print(", ");
            }
        }
        
        // Slowly prints the items they just recently got
        for (Item item : newestItems){
            Console.wait(milliseconds);
            System.out.print(item.id());
            items.add(item);
            
            if (newestItems.indexOf(item) != newestItems.size() - 1){
                System.out.print(", ");
            }
        }
        System.out.println();
        newestItems.clear();
        
        Console.wait(milliseconds);
        
        String healthColor = "\033[38;5;40m"; // Default health color is green
        String bulletproofVestTag = "";
        String totemTag = "";
        String cursedTag = "";
        
        if (bulletproofVestEquipped){
            bulletproofVestTag = "\033[38;5;45m[⛨ sʜɪᴇʟᴅᴇᴅ] " ;
        }
        if (hasTotem && health - game.bulletDamage() <= 0){
            totemTag = "\033[38;5;228m[𖤍 ᴘʀᴏᴛᴇᴄᴛᴇᴅ] ";
        }
        if (curseLevel > 0){
            cursedTag = "\033[38;5;201m" + "[⛧ ᴄᴜʀꜱᴇᴅ";
            if (curseLevel > 1){
                cursedTag += " x" + curseLevel;
            }
            cursedTag += "] ";
        }
        healthColor = totemTag + bulletproofVestTag + cursedTag + healthColor;
        
        if (this.equals(playerTakingTurn) && !(this instanceof Bot)){
            System.out.println("Your health: " + healthColor + health + "\u001b[0m\n");
        }
        else{
            System.out.println(name + "'s health: " + healthColor + health + "\u001b[0m\n");
        }
    }
    
    // Returns either an Item or a Player
    public Object promptOptions(){
        ArrayList<Player> otherPlayers = new ArrayList<>();
        // Remove the person prompted from the total list of players
        game.players().forEach(player -> {if (!this.equals(player)){ otherPlayers.add(player);}});

        String userChoice = "";
        
        options:
        while (true){
            System.out.println("What do you do?");
            
            if (canUseItems){
                items.forEach(item -> System.out.print("- Use " + item.id() + "\n"));
            }
            
            System.out.println("- Shoot yourself (y)");
            
            otherPlayers.forEach(otherPlayer -> System.out.println("- Shoot " + otherPlayer.name()));
            
            System.out.print("Type Here --> ");
            userChoice = GeneralConversions.formatString(userInput.nextLine());
            
            // If the prompted person's input is an item, it returns the item they entered
            for (Item item : items){
                if (item.keywords().contains(userChoice)){
                    // If the person tries to use an item while jammed, print an error message
                    if (!canUseItems){
                        Console.clearScreen();
                        System.out.println(GeneralConversions.errorMessage("You cannot use items!\n"));
                        Console.wait(1000);
                        for (Player player : game.players()){
                            player.printItemsAndHealth(0);
                        }
                        // Restarts the code to the top of the while loop
                        continue options;
                    }
                    return item;
                }
            }
            
            // If the prompted persons's input is a Player, it returns the player they entered
            for (Player otherPlayer : otherPlayers){
                if (userChoice.equals(otherPlayer.formattedName())){
                    return otherPlayer;
                }
            }
            
            // If the prompted person's input is themselves, it returns themselves
            if (userChoice.equals("Y")){
                return this;
            }
            
            else{
                Console.clearScreen();
                System.out.println(GeneralConversions.errorMessage("Invalid Input\n"));
                Console.wait(1000);
                
                for (Player player : game.players()){
                    player.printItemsAndHealth(0);
                }
            }
        }
    }
    
    // Getters and Setters
    public String name(){
        return name;
    }
    
    public String formattedName(){
        return formattedName;
    }
    
    public void addWin(){
        numberOfWins++;
    }
    
    public int curseLevel(){
        return curseLevel;
    }
    
    public void resetCurseLevel(){
        curseLevel = 0;
    }
    
    public int wins(){
        return numberOfWins;
    }
    
    public boolean hasBeenHandcuffedFor(int rounds){
        return roundsHandcuffed == rounds;
    }
    
    public void addRoundHandcuffed(){
        roundsHandcuffed++;
    }
    
    public void unhandcuff(){
        handcuffed = false;
        roundsHandcuffed = 0;
    }
    
    public void handcuff(){
        handcuffed = true;
    }
    
    public boolean isHandcuffed(){
        return handcuffed;
    }
    
    public boolean canUseItems(){
        return canUseItems;
    }
    
    public void disableItemUse(){
        canUseItems = false;
    }
    
    public void enableItemUse(){
        canUseItems = true;
        turnsItemsJammed = 0;
    }
    
    public boolean itemsHaveBeenJammedFor(int rounds){
        return turnsItemsJammed == rounds;
    }
    
    public void addRoundItemsJammed(){
        turnsItemsJammed++;
    }
    
    public void setBulletproofVestEquipped(boolean trueOrFalse){
        bulletproofVestEquipped = trueOrFalse;
    }
    
    public boolean hasBulletproofVestEquipped(){
        return bulletproofVestEquipped;
    }
    
    public int health(){
        return health;
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public ArrayList<Item> items(){
        return items;
    }
    
    public ArrayList<Item> newestItems(){
        return items;
    }
    
    public long money(){
        return money;
    }
    
    public boolean hasTotem(){
        return hasTotem;
    }
    
    public void resetDrunkenLevel(){
        drunkenLevel = 0;
    }
    
    public void removeTotem(){
        hasTotem = false;
        items.remove(items.indexOf(getItem("Totem")));
    }
    
    public Game game(){
        return game;
    }
}
