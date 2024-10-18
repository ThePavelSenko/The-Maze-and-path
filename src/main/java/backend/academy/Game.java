package backend.academy;

import java.util.List;
import java.util.Scanner;
import lombok.experimental.UtilityClass;
import static backend.academy.Utils.COORDINATE_QUANTITY;
import static backend.academy.Utils.EASY;
import static backend.academy.Utils.GOODBYE_MESSAGE;
import static backend.academy.Utils.HARD;
import static backend.academy.Utils.HELLO_MESSAGE;
import static backend.academy.Utils.IF_EASY_MESSAGE;
import static backend.academy.Utils.IF_HARD_MESSAGE;
import static backend.academy.Utils.OUT;

@UtilityClass
public class Game {
    public void startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            OUT.println(HELLO_MESSAGE);
            AbstractGraphMaze maze;
            MazePathFinder pathFinder;
            int width;
            int height;
            String[] size;
            int startX;
            int startY;
            int endX;
            int endY;
            String[] coordinates;

            // Loop until valid difficulty level is entered
            String difficulty;
            while (true) {
                OUT.println("Please choose difficulty (easy/hard):");
                difficulty = scanner.nextLine().toLowerCase();
                if (EASY.equals(difficulty) || HARD.equals(difficulty)) {
                    break;
                } else {
                    OUT.println("Invalid difficulty level. Please try again.");
                }
            }

            // Loop until valid maze size is entered
            while (true) {
                OUT.println("Ok! Enter size of maze with format n*m! (two int separated by space): ");
                size = scanner.nextLine().split(" ");
                if (size.length == 2) {
                    try {
                        width = Integer.parseInt(size[0]);
                        height = Integer.parseInt(size[1]);
                        break;
                    } catch (NumberFormatException e) {
                        OUT.println("Invalid input. Please enter integer values for size.");
                    }
                } else {
                    OUT.println("Please enter exactly two integers separated by space.");
                }
            }

            // Setup maze and pathfinder based on difficulty
            if ("easy".equals(difficulty)) {
                maze = new PrimMaze(width, height);
                pathFinder = new BFS(maze);
                OUT.println(IF_EASY_MESSAGE);
            } else {
                maze = new KruskalMaze(width, height);
                pathFinder = new AStar(maze);
                OUT.println(IF_HARD_MESSAGE);
            }

            maze.generateMaze();
            List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
            maze.printMaze(outputMaze);

            // Loop until valid start and end coordinates are entered
            while (true) {
                OUT.println("Ok! Enter the coordinate of start and end of your path! (four int separated by space): ");
                coordinates = scanner.nextLine().split(" ");
                if (coordinates.length == COORDINATE_QUANTITY) {
                    try {
                        int i = 0;
                        startX = Integer.parseInt(coordinates[i]);
                        i++;
                        startY = Integer.parseInt(coordinates[i]);
                        i++;
                        endX = Integer.parseInt(coordinates[i]);
                        i++;
                        endY = Integer.parseInt(coordinates[i]);

                        // Check if the coordinates are within the bounds of the maze
                        if (startX < 0 || startX >= width || startY < 0 || startY >= height ||
                            endX < 0 || endX >= width || endY < 0 || endY >= height) {
                            OUT.println("Coordinates out of maze bounds. Please enter valid coordinates within the maze.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        OUT.println("Invalid input. Please enter integer values for coordinates.");
                    }
                } else {
                    OUT.println("Please enter exactly four integers separated by space.");
                }
            }

            // Find and print the path
            pathFinder.findPath(startX, startY, endX, endY);
            pathFinder.printPath(outputMaze);
            OUT.println(GOODBYE_MESSAGE);
        } catch (IllegalStateException e) {
            OUT.println("An unexpected error occurred. Please try again.");
        }
    }
}
