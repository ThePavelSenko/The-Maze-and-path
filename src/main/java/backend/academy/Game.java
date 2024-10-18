package backend.academy;

import java.util.List;
import java.util.Scanner;
import lombok.experimental.UtilityClass;
import static backend.academy.Utils.COORDINATES_FORMAT_ERROR;
import static backend.academy.Utils.COORDINATES_PROMPT;
import static backend.academy.Utils.COORDINATE_QUANTITY;
import static backend.academy.Utils.DIFFICULTY_PROMPT;
import static backend.academy.Utils.EASY;
import static backend.academy.Utils.GOODBYE_MESSAGE;
import static backend.academy.Utils.HARD;
import static backend.academy.Utils.HELLO_MESSAGE;
import static backend.academy.Utils.IF_EASY_MESSAGE;
import static backend.academy.Utils.IF_HARD_MESSAGE;
import static backend.academy.Utils.INVALID_COORDINATES_MESSAGE;
import static backend.academy.Utils.INVALID_DIFFICULTY_MESSAGE;
import static backend.academy.Utils.INVALID_SIZE_MESSAGE;
import static backend.academy.Utils.OUT;
import static backend.academy.Utils.OUT_OF_BOUNDS_MESSAGE;
import static backend.academy.Utils.SIZE_POSITIVE_MESSAGE;
import static backend.academy.Utils.SIZE_PROMPT;

@UtilityClass
public class Game {
    public void startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            OUT.println(HELLO_MESSAGE);
            AbstractGraphMaze maze;
            MazePathFinder pathFinder;
            int size;
            int startX;
            int startY;
            int endX;
            int endY;

            String difficulty = getDifficulty(scanner);
            size = getMazeSize(scanner);

            if (EASY.equals(difficulty)) {
                maze = new PrimMaze(size, size);
                pathFinder = new BFS(maze);
                OUT.println(IF_EASY_MESSAGE);
            } else {
                maze = new KruskalMaze(size, size);
                pathFinder = new AStar(maze);
                OUT.println(IF_HARD_MESSAGE);
            }

            maze.generateMaze();
            List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
            maze.printMaze(outputMaze);

            int[] coordinates = getCoordinates(scanner, size);
            int i = 0;
            startX = coordinates[i];
            i++;
            startY = coordinates[i];
            i++;
            endX = coordinates[i];
            i++;
            endY = coordinates[i];

            pathFinder.findPath(startX, startY, endX, endY);
            pathFinder.printPath(outputMaze);
            OUT.println(GOODBYE_MESSAGE);
        } catch (IllegalStateException e) {
            OUT.println("An unexpected error occurred. Please try again.");
        }
    }

    private String getDifficulty(Scanner scanner) {
        String difficulty;
        while (true) {
            OUT.println(DIFFICULTY_PROMPT);
            difficulty = scanner.nextLine().toLowerCase();
            if (EASY.equals(difficulty) || HARD.equals(difficulty)) {
                break;
            }
            OUT.println(INVALID_DIFFICULTY_MESSAGE);
        }
        return difficulty;
    }

    private int getMazeSize(Scanner scanner) {
        int size;
        while (true) {
            OUT.println(SIZE_PROMPT);
            try {
                size = Integer.parseInt(scanner.nextLine());
                if (size > 0) {
                    break;
                }
                OUT.println(SIZE_POSITIVE_MESSAGE);
            } catch (NumberFormatException e) {
                OUT.println(INVALID_SIZE_MESSAGE);
            }
        }
        return size;
    }

    private int[] getCoordinates(Scanner scanner, int size) {
        int[] coordinates = new int[COORDINATE_QUANTITY];
        while (true) {
            OUT.println(COORDINATES_PROMPT);
            String[] input = scanner.nextLine().split(" ");
            if (input.length == COORDINATE_QUANTITY) {
                try {
                    for (int i = 0; i < COORDINATE_QUANTITY; i++) {
                        coordinates[i] = Integer.parseInt(input[i]);
                    }
                    if (isWithinBounds(coordinates, size)) {
                        break;
                    }
                    OUT.println(OUT_OF_BOUNDS_MESSAGE);
                } catch (NumberFormatException e) {
                    OUT.println(INVALID_COORDINATES_MESSAGE);
                }
            } else {
                OUT.println(COORDINATES_FORMAT_ERROR);
            }
        }
        return coordinates;
    }

    private boolean isWithinBounds(int[] coordinates, int size) {
        for (int i = 0; i < COORDINATE_QUANTITY; i++) {
            if (coordinates[i] < 0 || coordinates[i] >= size) {
                return false;
            }
        }
        return true;
    }
}
