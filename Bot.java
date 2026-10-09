import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.*;

public class Bot extends Player{
    private ArrayList<Integer> knownShellIndexes = new ArrayList<>();
    private Player playerChosenForItem = null; // For adrenaline and handcuffs
    private Item itemChosenToSteal = null; // For adrenaline

    public Bot(String name){
        super(name);
        discardBulletKnowledge();
    }
    
    public void discardBulletKnowledge(){
        knownShellIndexes.clear();
    }
    
    public void addIndexOfKnownBullet(int index){
        if (!knownShellIndexes.contains(index)){
            knownShellIndexes.add(index);
        }
    }
    
    public Player playerChosenForItem(){
        return playerChosenForItem;
    }
    
    public Item itemChosenToSteal(){
        return itemChosenToSteal;
    }
    
    // This method is only called when bullets is updated
    public void updateKnownShellIndexes(){
        for (Integer shellIndex : knownShellIndexes){
            knownShellIndexes.set(knownShellIndexes.indexOf(shellIndex), shellIndex - 1);
        }
        knownShellIndexes.removeIf(shellIndex -> shellIndex < 0);
        
        // This logic only applies if the bot knows the total number of lives & blanks
        if (!super.game().shellSetIsFogged()){
            ArrayList<String> shells = super.game().bullets();
            // Bot will know the loaded shell if there are no blanks, no lives, or if it uses a burner phone and there's only 2 bullets in the shotgun
            if ((!shells.contains("live") || !shells.contains("blank") || (shells.size() == 2 && knownShellIndexes.contains(1)))){
                addIndexOfKnownBullet(0);
            }
        }
    }
    
    public String getKnownShellLoadedInShotgun(){
        String loadedShell = "Unknown";
        
        if (knownShellIndexes.contains(0)){
            loadedShell = super.game().bullets().get(0);
        }
        return loadedShell;
    }
    
    public Player pickTarget(){
        Game game = super.game();
        Map<Player, Integer> interestLevels = new HashMap<>();
        
        for (Player player : game.players()){
            if (!player.equals(this)){ // Ensures the bot does not pick itself to shoot
                int interestLevel = 50;
                
                if (player.hasItem("Saw")){
                    interestLevel += 10;
                }
                
                if (player.hasItem("Handcuffs")){
                    interestLevel += 10;
                }
                
                if (player.isHandcuffed()){
                    interestLevel += 10;
                }
                
                if (player.health() - game.bulletDamage() <= 0){
                    interestLevel += 10;
                }
                
                if (player.hasItem("Adrenaline")){
                    interestLevel += 5;
                }
                
                if (player.hasItem("Lighter")){
                    interestLevel -= 5;
                }
                
                if (player.hasBulletproofVestEquipped()){
                    interestLevel -= 20;
                }
                
                interestLevels.put(player, interestLevel);
            }
        }
        Player target = interestLevels.entrySet().
            stream().
            max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
        
        return target;
    }
    
    public void decideToUseAdrenaline(){
        Game game = super.game();
        boolean defenseMode = false;
        boolean attackMode = false;
        boolean neutralMode = false;
        
        // If the bot would die from a live bullet
        if (super.health() - game.baseBulletDamage <= 0){
            defenseMode = true;
        }
        /*
        // If there's no way to use the adrenaline in a defensive way, it won't be in defense mode
        if (!atLeastOnePlayerHas("Lighter") && !atLeastOnePlayerHas("Bulletproof Vest")){
            defenseMode = false;
        }
        
        // If the bot can't use adrenaline in an offensive or defensive way, it uses it neutrally
        if (!defenseMode && !attackMode){
            neutralMode = true;
        }
        
        
        // For defense mode, make sure the bot steals the lighter/bulletproof vest from the person with the lowest hp
        // For attack mode, make sure the bot steals the saw/handcuffs from people with both first (or biggest threat)
        */
    }
    
    @Override
    public Item getItem(String itemName){
        return super.getItem(itemName);
    }
    
    @Override
    public boolean hasItem(String itemName){
        return super.hasItem(itemName);
    }
    
    @Override
    public void resetRoundStats(){
        super.unhandcuff();
        discardBulletKnowledge();
        super.items().clear();
        super.newestItems().clear();
        super.resetDrunkenLevel();
    }
    
    @Override
    public Object promptOptions(){
        ArrayList<Player> otherPlayers = new ArrayList<>();
        // Remove the bot prompted from the total list of players
        super.game().players().forEach(player -> {if (!this.equals(player)){ otherPlayers.add(player);}});
        
        // return either an item or Player
        return makeSmartDecision();
    }
    
    public Object makeSmartDecision(){
        Game game = super.game();
        boolean shellSetFogged = super.game().shellSetIsFogged();
        String loadedShell = getKnownShellLoadedInShotgun(); // "Unknown" if the bot has no info on it
        int botHealth = super.health();
        Player target = pickTarget();
        playerChosenForItem = target; // Initializes the target as the person of interest for handcuffs, curse, and adnrenaline
        
        // FIRST the bot uses a lighter, bulletproof vest, or curse if it has them
        if (hasItem("Lighter") && getItem("Lighter").canBeUsedBy(this) && super.canUseItems()){
            return getItem("Lighter");
        }
        if (hasItem("Bulletproof Vest") && getItem("Bulletproof Vest").canBeUsedBy(this) && super.canUseItems()){
            return getItem("Bulletproof Vest");
        }
        if (hasItem("Curse") && getItem("Curse").canBeUsedBy(this) && super.canUseItems()){
            return getItem("Curse");
        }
        
        // SECOND the bot uses inverter if it knows its a blank
        if (loadedShell.equals("blank") && hasItem("Inverter") && getItem("Inverter").canBeUsedBy(this) && super.canUseItems()){
            return getItem("Inverter");
        }
        
        // SHOTGUN SHOOTING
        if (loadedShell.equals("blank")){
            return this;
        }
        
        if (loadedShell.equals("Unknown")){
            int numberOfBlanks = game.numberOfBulletsThatAre("blank");
            int totalBullets = game.bullets().size();
            
            boolean riskyOdds = (double) numberOfBlanks / totalBullets >= 0.4 && (double) numberOfBlanks / totalBullets <= 0.6;
            
            // If the shell set has been fogged & the bot doesn't know whats loaded, it is ALWAYS risky odds
            if (shellSetFogged){
                riskyOdds = true;
            }
            
            // Use a magnifying glass, burner phone, or beer if the odds are risky
            if (riskyOdds){
                if (hasItem("Magnifying Glass") && getItem("Magnifying Glass").canBeUsedBy(this) && super.canUseItems()){
                    return getItem("Magnifying Glass");
                }
                if (hasItem("Burner Phone") && getItem("Burner Phone").canBeUsedBy(this) && super.canUseItems()){
                    return getItem("Burner Phone");
                }
                if (hasItem("Beer") && getItem("Beer").canBeUsedBy(this) && super.canUseItems()){
                    return getItem("Beer");
                }
            }
            
            boolean shootSelf = Math.random() < (double) numberOfBlanks / totalBullets;
            
            // Bot will NEVER shoot itself if the shell set has been fogged
            if (shellSetFogged){
                shootSelf = false;
            }
            
            if (shootSelf){
                return this;
            }
        }
        // Beyond this point, the bot will ALWAYS shoot a person
        
        int numberOfLives = game.numberOfBulletsThatAre("live");
        int numberOfBlanks = game.numberOfBulletsThatAre("blank");
        int totalShells = game.bullets().size();
        double ratioOfLives = (double) numberOfLives / (totalShells);
        
        // Bot decides on using a saw if the target won't die from a regular bullet and can be shot successfully
        if (target.health() - game.bulletDamage() != 0 && !target.hasBulletproofVestEquipped() && hasItem("Saw") && getItem("Saw").canBeUsedBy(this) && super.canUseItems()){
            // Bot will always use a saw if it knows the bullet will successfully hit
            if (loadedShell.equals("live")){
                return getItem("Saw");
            }
            
            // Bot will not risk using a saw if the odds of it being a live are 50/50 or less
            if (ratioOfLives > 0.5){
                boolean allIn = Math.random() < ratioOfLives;
                
                if (allIn){
                    return getItem("Saw");
                }
            }
        }
        
        if (hasItem("Handcuffs") && getItem("Handcuffs").canBeUsedBy(this) && super.canUseItems()){
            // Only use handcuffs if the odds are favored to have 2 lives in a row
            if (numberOfLives >= 2 && ratioOfLives >= 0.6){
                return getItem("Handcuffs");
            }
        }
        
        // LASTLY the bot decides if it should use expired medicine
        if (hasItem("Expired Medicine") && getItem("Expired Medicine").canBeUsedBy(this) && super.canUseItems()){
            boolean takeTheDrugs = false;
            
            if (super.health() == 1 && !super.hasBulletproofVestEquipped() && !super.hasTotem()){
                takeTheDrugs = true;
            }
            
            // TO DO: make probability chart
            // Have a danger level counter
            // If a person has a saw and the bot has 2 health, they might die
            // If a person has handcuffs and the bot has 2 health, they might die
            // If a person has handcuffs and a saw and the bot as 3 health, they might die
            // The bot needs to see if there's even enough lives in the shotgun to kill it
            // All of these factors add to the danger level
            // The danger level determines the final % chance of it using expired medicine
            
            if (takeTheDrugs){
                return getItem("Expired Medicine");
            }
        }
        
        
        // make a random 2% chance for the bot to use something random for zero reason
        
        return target;
    }
    
}
