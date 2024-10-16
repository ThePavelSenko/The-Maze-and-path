package backend.academy;

import java.util.List;
import java.util.Scanner;
import lombok.experimental.UtilityClass;
import static backend.academy.Utils.GOODBYE_MESSAGE;
import static backend.academy.Utils.HELLO_MESSAGE;
import static backend.academy.Utils.IF_EASY_MESSAGE;
import static backend.academy.Utils.IF_HARD_MESSAGE;
import static backend.academy.Utils.OUT;

@UtilityClass

public class Main {
    public static void main(String[] args) {
        final int ifEasySize = 5;
        final int ifHardSize = 10;
        AbstractGraphMaze maze;
        MazePathFinder pathFinder;

        try (Scanner scanner = new Scanner(System.in)) {
            OUT.println(HELLO_MESSAGE);
            String response;

            // Loop until valid difficulty level is entered
            while (true) {
                OUT.println("Please enter difficulty level ('easy' or 'hard'):");
                response = scanner.nextLine().toLowerCase();
                if ("easy".equals(response)) {
                    maze = new PrimMaze(ifEasySize, ifEasySize);
                    pathFinder = new BFS(maze);
                    OUT.println(IF_EASY_MESSAGE);
                    break;
                } else if ("hard".equals(response)) {
                    maze = new KruskalMaze(ifHardSize, ifHardSize);
                    pathFinder = new AStar(maze);
                    OUT.println(IF_HARD_MESSAGE);
                    break;
                } else {
                    OUT.println("Invalid difficulty level. Please try again.");
                }
            }

            maze.generateMaze();
            List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
            maze.printMaze(outputMaze);

            // Loop until valid answer about path visualization is entered
            while (true) {
                OUT.println("Would you like to see the path? ('Yes' or 'No'):");
                String showPath = scanner.nextLine();
                if ("Yes".equalsIgnoreCase(showPath)) {
                    pathFinder.findPath();
                    pathFinder.printPath(outputMaze);
                    OUT.println(GOODBYE_MESSAGE);
                    break;
                } else if ("No".equalsIgnoreCase(showPath)) {
                    OUT.println(GOODBYE_MESSAGE);
                    break;
                } else {
                    OUT.println("Invalid input. Please enter 'Yes' or 'No'.");
                }
            }
        } catch (IllegalStateException e) {
            OUT.println("You must enter correct value! Please try again!");
        }
    }
}


