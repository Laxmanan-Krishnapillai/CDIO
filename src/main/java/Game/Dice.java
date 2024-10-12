package Game;

import java.util.Random;

public class Dice {
    final private Random random;
    public Dice() {
        random = new Random();
    }
    public int[] roll() {
        int die1 = random.nextInt(6) + 1; 
        int die2 = random.nextInt(6) + 1; 
        return new int[] { die1, die2 };
    }
}
