package Game;
import java.util.Scanner;

enum PlayerColor {
    GREEN("\u001B[32m"),  
    BLUE("\u001B[34m"),   
    RESET("\u001B[0m");   
    private final String code;
    PlayerColor(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}

class Player {
    final private String name;
    private int score;
    final private PlayerColor color;
    public Player(String name, PlayerColor color) {
        this.name = name;
        this.score = 0;
        this.color = color;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public void addScore(int points) {
        this.score += points;
    }
    public void resetScore() {
        this.score = 0;
    }
    public PlayerColor getColor() {
        return color;
    }
}

public class Game {
    final private Player player1;
    final private Player player2;
    final private Dice dice;
    final private int targetScore;
    private boolean gameOver;
    final private Scanner scanner;
    private Player lastDoubleSixPlayer;

    public Game(String player1Name, String player2Name, int targetScore) {
        player1 = new Player(player1Name, PlayerColor.GREEN);
        player2 = new Player(player2Name, PlayerColor.BLUE);
        dice = new Dice();
        this.targetScore = targetScore;
        gameOver = false;
        lastDoubleSixPlayer = null;
        scanner = new Scanner(System.in);
    }
    private void printWithColor(Player player, String message) {
        System.out.print(player.getColor().getCode()); 
        System.out.println(message);
        System.out.print(PlayerColor.RESET.getCode()); 
    }
    private void displayRules() {
        System.out.println("\n--- Game Rules ---");
        System.out.println("1. Players roll two dice and add the sum to their score.");
        System.out.println("2. If you roll two 1s, you lose all your points and your turn.");
        System.out.println("3. If you roll doubles (except two 1s), you get a bonus turn.");
        System.out.println("4. After reaching 40 points, you must roll doubles to win.");
        System.out.println("5. If you roll two 6s twice in a row, you win instantly.\n");
    }
    public void start() {
        displayRules();
        System.out.println("Starting the Dice Game. First to reach " + targetScore + " points wins!");
        while (!gameOver) {
            playTurn(player1);
            if (gameOver) break; 
            playTurn(player2);
        }
    }
    private void playTurn(Player player) {
        printWithColor(player, "\n" + player.getName() + "'s turn. Press Enter to roll the dice, or type 'r' to restart the game, or 'help' for rules:");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("r")) {
            reset(); 
            return;
        } else if (input.equalsIgnoreCase("help")) {
            displayRules();  
            return;          
        }
        if (input.isEmpty()) {
            int[] rollResult = dice.roll();
            int die1 = rollResult[0];
            int die2 = rollResult[1];
            printWithColor(player, player.getName() + " rolled: " + die1 + " and " + die2);
            if (die1 == 1 && die2 == 1) {
                player.resetScore();
                printWithColor(player, "Rolled two 1s! You lose all points and your turn.");
                return; 
            }
            if (die1 == 6 && die2 == 6) {
                if (lastDoubleSixPlayer == player) {
                    printWithColor(player, player.getName() + " rolled double sixes twice in a row and wins instantly!");
                    gameOver = true;
                    return;
                } else {
                    lastDoubleSixPlayer = player;
                }
            } else {
                lastDoubleSixPlayer = null; 
            }
            player.addScore(die1 + die2);
            printWithColor(player, player.getName() + "'s total score: " + player.getScore());
            if (player.getScore() >= targetScore) {
                if (die1 == die2) {
                    printWithColor(player, player.getName() + " wins the game by rolling doubles after reaching " + targetScore + " points!");
                    gameOver = true;
                } else {
                    printWithColor(player, player.getName() + " has reached 40 points but must roll doubles to win.");
                }
            } else {
                if (die1 == die2) {
                    printWithColor(player, player.getName() + " rolled doubles and gets an extra turn!");
                    playTurn(player); 
                }
            }
        } else {
            printWithColor(player, "Invalid input. Press Enter to roll, or type 'r' to restart, or 'help' to see rules.");
        }
    }
    public void reset() {
        player1.resetScore();
        player2.resetScore();
        gameOver = false;
        lastDoubleSixPlayer = null;
        System.out.println("Game has been reset. Scores are back to zero.");
        start();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Dice Game!");
        System.out.print("Enter name for Player 1: ");
        String fl = scanner.nextLine();
        String player1Name = fl.isBlank() ? "Player 1" : fl;
        System.out.print("Enter name for Player 2: ");
        String nl = scanner.nextLine();
        String player2Name = nl.isBlank() ? "Player 2" : nl;
        int targetScore = 40;
        Game game = new Game(player1Name, player2Name, targetScore);
        game.start();
        scanner.close();
    }
}
