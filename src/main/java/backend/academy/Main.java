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
            String response = scanner.nextLine();
            switch (response) {
                case "easy":
                    maze = new PrimMaze(ifEasySize, ifEasySize);
                    pathFinder = new BFS(maze);
                    OUT.println(IF_EASY_MESSAGE);
                    break;
                case "hard":
                    maze = new KruskalMaze(ifHardSize, ifHardSize);
                    pathFinder = new AStar(maze);
                    OUT.println(IF_HARD_MESSAGE);
                    break;
                default:
                    throw new IllegalStateException(response);
            }
            maze.generateMaze();
            List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
            maze.printMaze(outputMaze);
            String showPath = scanner.nextLine();
            switch (showPath) {
                case "Yes":
                    pathFinder.findPath();
                    pathFinder.printPath(outputMaze);
                    OUT.println(GOODBYE_MESSAGE);
                    break;
                case "No":
                    OUT.println(GOODBYE_MESSAGE);
                    break;
                default:
                    throw new IllegalStateException(showPath);
            }
        } catch (IllegalStateException e) {
            OUT.println("You must enter correct value! Please try again!");
        }
    }
}


