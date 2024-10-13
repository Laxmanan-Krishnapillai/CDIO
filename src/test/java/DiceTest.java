import Game.Dice;
import java.util.HashMap;
import java.util.Map;

public class DiceTest {

    public static void main(String[] args) {
        Dice dice = new Dice();
        int numberOfThrows = 1000;
        Map<Integer, Integer> distribution = new HashMap<>();

        // Theoretical probabilities for each sum from 2 to 12
        double[] theoreticalProbabilities = {0.0, 0.0, 1.0/36, 2.0/36, 3.0/36, 4.0/36, 5.0/36, 6.0/36, 5.0/36, 4.0/36, 3.0/36, 2.0/36, 1.0/36};

        // Initialize the distribution map to hold counts for sums from 2 to 12
        for (int i = 2; i <= 12; i++) {
            distribution.put(i, 0);
        }

        // Roll the dice 1000 times and collect the results
        for (int i = 0; i < numberOfThrows; i++) {
            int[] roll = dice.roll();
            int sum = roll[0] + roll[1];
            distribution.put(sum, distribution.get(sum) + 1);
        }

        // Print the distribution of sums
        System.out.println("Dice Roll Distribution after " + numberOfThrows + " throws:");
        for (int sum = 2; sum <= 12; sum++) {
            System.out.println("Sum " + sum + ": " + distribution.get(sum) + " times");
        }

        // Perform the chi-square test
        double chiSquare = calculateChiSquare(distribution, theoreticalProbabilities, numberOfThrows);
        System.out.println("\nChi-Square value: " + chiSquare);

        // Define the critical value for the chi-square test with df = 11 at significance level 0.05
        // Lookup the critical value: https://www.scribbr.com/wp-content/uploads/2022/05/Chi-square-table.pdf
        double criticalValue = 19.675;

        // Verify the result by comparing the chi-square value to the critical value
        if (chiSquare < criticalValue) {
            System.out.println("The dice roll distribution has been verified to match the expected normal distribution.");
        } else {
            System.out.println("The dice roll distribution does NOT match the expected normal distribution.");
        }
    }

    // Method to calculate the Chi-Square statistic
    private static double calculateChiSquare(Map<Integer, Integer> observedDistribution, double[] theoreticalProbabilities, int totalRolls) {
        double chiSquare = 0.0;
        for (int sum = 2; sum <= 12; sum++) {
            int observed = observedDistribution.get(sum);
            double expected = theoreticalProbabilities[sum] * totalRolls;
            chiSquare += Math.pow(observed - expected, 2) / expected;
        }
        return chiSquare;
    }
}
