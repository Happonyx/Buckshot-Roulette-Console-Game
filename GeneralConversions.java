import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class GeneralConversions {
    public static final ArrayList<String> numberSuffixes = new ArrayList<>(Arrays.asList("st", "nd", "rd"));
    
    // Returns string version of a number in Roman Numerals
    public static String romanNumeralValueOf(int numberToConvert){
        String romanNumeral = "";
        
        int[] numbers = {10000, 9000, 5000, 4000, 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romanNumerals = {"CCIↃↃ", "MCCIↃↃ", "IↃↃ", "MIↃↃ", "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        for (int index = 0; index < numbers.length; index++){
            while (numberToConvert >= numbers[index]){
                romanNumeral += romanNumerals[index];
                numberToConvert -= numbers[index];
            }
        }
        return romanNumeral;
    }
    
    // Returns a list of keywords that includes them with "USE" added (for items specifically)
    public static ArrayList<String> addUseToKeywords(ArrayList<String> keywords){
        ArrayList<String> newKeywords = new ArrayList<>(keywords);
        for (String keyword : keywords){
            newKeywords.add("USE" + keyword);
        }
        return newKeywords;
    }
    
    // Returns the string without spaces and fully uppercase
    public static String formatString(String string){
        return string.replaceAll("\\s", "").toUpperCase();
    }
    
    public static String errorMessage(String error){
        return "\u001b[01m" + "\033[38;5;160m⚠︎  " + error + "\u001b[0m";
    }
    
    public static ArrayList<String> bigBoldMoneyNumbersOf(long number){
        String numberAsString = String.format("%,d", number);
        ArrayList<String> allNumbers = new ArrayList<>(Arrays.asList("   █    ",
                                                                     " █████  ",
                                                                     "█  █    ",  
                                                                     " ████   ",
                                                                     "   █ █  ",
                                                                     "█████   ",
                                                                     "   █    "));
        
        for (char num : numberAsString.toCharArray()){
            ArrayList<String> characterArray = new ArrayList<>();
            switch (num){
                case ',':
                    characterArray = comma; break;
                case '0':
                    characterArray = zero; break;
                case '1':
                    characterArray = one; break;
                case '2':
                    characterArray = two; break;
                case '3':
                    characterArray = three; break;
                case '4':
                    characterArray = four; break;
                case '5':
                    characterArray = five; break;
                case '6':
                    characterArray = six; break;
                case '7':
                    characterArray = seven; break;
                case '8':
                    characterArray = eight; break;
                case '9':
                    characterArray = nine; break;
            }
            for (int row = 0; row < zero.size(); row++){
                allNumbers.set(row, allNumbers.get(row) + characterArray.get(row));
            }
        }
        
        for (int row = 0; row < allNumbers.size(); row++){
            allNumbers.set(row, allNumbers.get(row) + "\n");
        }
        return allNumbers;
    }
    
    public static final ArrayList<String> leaseOfLiability = new ArrayList<>(Arrays.asList("---------------------------------------__ ", 
                                                                                           "|        _________________________       \\",
                                                                                           "|       | Acknowledgment of Risk: |       |",
                                                                                           "|       L-------------------------⅃       |", 
                                                                                           "|                                         |", 
                                                                                           "|   Player understands the physical and   |", 
                                                                                           "|   psychological risks of participating  |", 
                                                                                           "|   in the game and will not use outside  |", 
                                                                                           "|   celestial, ethereal, or transcendent  |", 
                                                                                           "|   powers to gain an unfair advantage.   |", 
                                                                                           "|                             \033[48;5;124m \u001b[0m           |", 
                                                                                           "|   By participating, the player waives   |", 
                                                                                           "|     any and all liability, including    |", 
                                                                                           "| \033[48;5;160m  \u001b[0m    injury, losses, and \033[38;5;196mdeath\u001b[0m.        |", 
                                                                                           "|                        \033[48;5;160m  \u001b[0m               |", 
                                                                                           "|   By signing this document, the player  |", 
                                                                                           "|      willingly agrees to the terms      |", 
                                                                                           "| \033[48;5;160m  \u001b[0m              provided.   \033[48;5;160m  \u001b[0m          |", 
                                                                                           "|   \033[48;5;124m \u001b[0m    \033[48;5;160m  \u001b[0m                               |", 
                                                                                           "|   \033[48;5;160m \u001b[0m       \033[48;5;88m \u001b[0m     \033[48;5;88m   \u001b[0m      SIGNED BY:     |", 
                                                                                           "|              _____   _____   ____       |",
                                                                                           "|  \033[48;5;124m   \u001b[0m   \033[48;5;160m \u001b[0m    | ____| | ___ | | __ \\      |", 
                                                                                           "|     \033[48;5;160m   \u001b[0m     | | ___ | | | | | | \\ |     |", 
                                                                                           "|   \033[48;5;88m \u001b[0m   \033[48;5;124m   \u001b[0m   | |_⅃ | | |_| | | |_/ |     |", 
                                                                                           "| \033[48;5;88m  \u001b[0m   \033[48;5;160m  \u001b[0m     |_____| |_____| |____/      |", 
                                                                                           "| \033[48;5;124m \u001b[0m     --------------------------------  |", 
                                                                                           "|    \033[48;5;124m \u001b[0m          \033[48;5;88m  \u001b[0m       \033[48;5;160m \u001b[0m                |", 
                                                                                           "L_________________________________________⅃"));
    
    public static void printLeaseOfLiability(){
        for (String row : leaseOfLiability){
            Console.wait(1000);
            System.out.println(row);
        }
        Console.wait(1000);
        System.out.println();
        Scanner input = new Scanner(System.in);
        System.out.print("Type anything to continue: ");
        String userChoice = input.nextLine();
    }
    
    // Numbers & Characters
    
    private static final ArrayList<String> zero = new ArrayList<>(Arrays.asList("       ",
                                                                                "█████  ",
                                                                                "██ ██  ", 
                                                                                "██ ██  ", 
                                                                                "██ ██  ", 
                                                                                "█████  ",
                                                                                "       "));
    
    private static final ArrayList<String> one = new ArrayList<>(Arrays.asList("       ",
                                                                               "  ██   ",
                                                                               "████   ", 
                                                                               "  ██   ", 
                                                                               "  ██   ", 
                                                                               "█████  ",
                                                                               "       "));
    
    private static final ArrayList<String> two = new ArrayList<>(Arrays.asList("       ",
                                                                               "█████  ",
                                                                               "   ██  ", 
                                                                               "█████  ", 
                                                                               "██     ", 
                                                                               "█████  ",
                                                                               "       "));
    
    private static final ArrayList<String> three = new ArrayList<>(Arrays.asList("       ",
                                                                                 "█████  ",
                                                                                 "   ██  ", 
                                                                                 "█████  ", 
                                                                                 "   ██  ", 
                                                                                 "█████  ",
                                                                                 "       "));
    
    private static final ArrayList<String> four = new ArrayList<>(Arrays.asList("       ",
                                                                                "██ ██  ",
                                                                                "██ ██  ", 
                                                                                "█████  ", 
                                                                                "   ██  ", 
                                                                                "   ██  ",
                                                                                "       "));
    
    private static final ArrayList<String> five = new ArrayList<>(Arrays.asList("       ",
                                                                                "█████  ",
                                                                                "██     ", 
                                                                                "█████  ", 
                                                                                "   ██  ", 
                                                                                "█████  ",
                                                                                "       "));
    
    private static final ArrayList<String> six = new ArrayList<>(Arrays.asList("       ",
                                                                               "█████  ",
                                                                               "██     ", 
                                                                               "█████  ", 
                                                                               "██ ██  ", 
                                                                               "█████  ",
                                                                               "       "));
    
    private static final ArrayList<String> seven = new ArrayList<>(Arrays.asList("       ",
                                                                                 "█████  ",
                                                                                 "   ██  ", 
                                                                                 "  ██   ", 
                                                                                 " ██    ", 
                                                                                 "██     ",
                                                                                 "       "));
    
    private static final ArrayList<String> eight = new ArrayList<>(Arrays.asList("       ",
                                                                                 "█████  ",
                                                                                 "██ ██  ", 
                                                                                 "█████  ", 
                                                                                 "██ ██  ", 
                                                                                 "█████  ",
                                                                                 "       "));
    
    private static final ArrayList<String> nine = new ArrayList<>(Arrays.asList("       ",
                                                                                "█████  ",
                                                                                "██ ██  ", 
                                                                                "█████  ", 
                                                                                "   ██  ", 
                                                                                "█████  ",
                                                                                "       "));
    
    private static final ArrayList<String> comma = new ArrayList<>(Arrays.asList("    ",
                                                                                 "    ",
                                                                                 "    ", 
                                                                                 "    ", 
                                                                                 "    ", 
                                                                                 " █  ",
                                                                                 "██  "));
}
