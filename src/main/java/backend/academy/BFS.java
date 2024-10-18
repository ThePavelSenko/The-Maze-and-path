package backend.academy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import static backend.academy.Utils.OUT;

/**
 * BFS class implements the Breadth-First Search (BFS) algorithm for finding a path
 * through a maze. This class tracks visited cells and can print the maze with the
 * path highlighted.
 * <p>This algorithm is designed for non-weighted graphs.</p>
 */
public class BFS implements MazePathFinder {
    private final AbstractGraphMaze maze;
    private final int[][] parents; // To store parent cells for path reconstruction
    private final boolean[][] visited; // To track visited cells
    private final List<Cell> path;
    private static final int CELL_WIDTH = 5;  // Width of each cell in the string representation

    public BFS(AbstractGraphMaze maze) {
        this.maze = maze;
        this.parents = new int[maze.width()][maze.height()];
        this.visited = new boolean[maze.width()][maze.height()];
        this.path = new ArrayList<>();
    }

    private void initializeParents() {
        for (int i = 0; i < parents.length; i++) {
            Arrays.fill(parents[i], -1); // Initialize each row with -1
        }
    }

    /**
     * Finds a path from the start cell to the goal cell using the BFS algorithm.
     * This method tracks visited cells and stores parent relationships for path reconstruction.
     */
    @Override
    public void findPath(int startX, int startY, int endX, int endY) {
        try {
            Queue<Cell> queue = new LinkedList<>();
            queue.add(new Cell(startX, startY)); // Start from the top-left corner
            initializeParents(); // Initialize parent relationships

            while (!queue.isEmpty()) {
                Cell current = queue.poll();

                if (current.col() == endX && current.row() == endY) {
                    break;
                }

                // Check neighboring cells
                for (Edge edge : maze.mazeEdges()) {
                    Cell neighbor = null;
                    if (edge.cell1().equals(current)) {
                        neighbor = edge.cell2();
                    } else if (edge.cell2().equals(current)) {
                        neighbor = edge.cell1();
                    }

                    // Visit neighbor if it hasn't been visited yet
                    if (neighbor != null && !visited[neighbor.col()][neighbor.row()]) {
                        visited[neighbor.col()][neighbor.row()] = true;
                        queue.add(neighbor);
                        // Store the parent of the neighbor for path reconstruction
                        parents[neighbor.col()][neighbor.row()] = current.row() * maze.width() + current.col();
                    }
                }
            }
            List<Cell> startEndPath = getCells(startX, startY, endX, endY);
            Collections.reverse(startEndPath); // Reverse to get the path from start to end
        } catch (ArrayIndexOutOfBoundsException e) {
            OUT.println("Error: Attempted to access an invalid index in the maze: " + e.getMessage());
        } catch (Exception e) {
            OUT.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Assembles the output maze with the path marked.
     *
     * @param outputMaze A list of strings representing the maze to be modified.
     * @return The modified maze with the path marked.
     */
    public List<String> assemblePath(List<String> outputMaze) {
        // Display the path in the maze
        for (Cell cell : path) {
            int rowIndex = cell.row() * 2 + 1; // Index in outputMaze
            StringBuilder row = new StringBuilder(outputMaze.get(rowIndex));
            int colIndex = cell.col() * CELL_WIDTH + 2; // Position in the row (considering boundaries)
            row.setCharAt(colIndex, '•'); // Mark the path with the symbol "•"
            outputMaze.set(rowIndex, row.toString());
        }
        return outputMaze;
    }

    private List<Cell> getCells(int startX, int startY, int endX, int endY) {
        int currentRow = endX; // Start from the bottom-right corner
        int currentCol = endY;

        // Reconstruct the path from end to start
        while (currentRow != startX || currentCol != startY) {
            path.add(new Cell(currentRow, currentCol));
            int parentIndex = parents[currentCol][currentRow];
            if (parentIndex == -1) {
                break;
            } // Exit if there is no parent
            currentRow = parentIndex / maze.width();
            currentCol = parentIndex % maze.width();
        }
        path.add(new Cell(startX, startY)); // Add the starting cell
        return path;
    }

    /**
     * Retrieves a copy of the visited cells array.
     *
     * @return A boolean array representing visited cells.
     */
    public boolean[][] visited() {
        boolean[][] visitedCopy = new boolean[maze.width()][maze.height()];
        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                visitedCopy[row][col] = visited[col][row];
            }
        }
        return visitedCopy;
    }

    /**
     * Prints the maze to the console, including the path.
     * <p>Before using it, you should first use findPath() and assemblePath().</p>
     *
     * @param outputMaze A list of strings representing the maze to be printed.
     *
     */
    @Override
    public void printPath(List<String> outputMaze) {
        List<String> mazeElements = assemblePath(outputMaze);
        for (String element : mazeElements) {
            OUT.println(element);
        }
        OUT.println();
    }
}
