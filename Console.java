import java.util.concurrent.TimeUnit;

public class Console {
    
    public static void wait(int milliseconds){
        try{
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        }
        catch (Exception e){
        }
    }
    
    public static void buildSuspense(int numberOfDots){
        for (int loop = 0; loop < numberOfDots; loop++){
            Console.wait(500);
            System.out.print(".");
        }
        Console.wait(500);
    }
    
    public static void clearScreen(){
        System.out.print("\u001B[2J" + "\u001B[0;0f");
    }
}
