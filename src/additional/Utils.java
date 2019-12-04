package additional;

import java.util.Random;

public class Utils {
    public static int get_random( int min, int max )
    {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
