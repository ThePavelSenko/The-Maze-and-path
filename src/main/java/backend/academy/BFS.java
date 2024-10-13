package backend.academy;

import java.util.*;

// for prim maze
public class BFS implements MazePathFinder {
    private final AbstractGraphMaze maze;
    private final int[][] parents; // To store parent cells for path reconstruction
    private final boolean[][] visited; // To track visited cells
    private boolean pathExists; // Flag to check if path exists

    public BFS(AbstractGraphMaze maze) {
        this.maze = maze;
        this.parents = new int[maze.width()][maze.height()];
        this.visited = new boolean[maze.width()][maze.height()];
        this.pathExists = false; // Initially assume no path exists
    }

    // Initialize the parent array with -1 to indicate no parent
    private void initializeParents() {
        for (int i = 0; i < parents.length; i++) {
            Arrays.fill(parents[i], -1); // Initialize each row with -1
        }
    }

    @Override
    public void findPath() {
        Queue<Cell> queue = new LinkedList<>();
        queue.add(new Cell(0, 0)); // Start from the top-left corner
        visited[0][0] = true;
        initializeParents(); // Initialize parent relationships

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            // If we reach the bottom-right corner, stop searching
            if (current.row() == maze.height() - 1 && current.col() == maze.width() - 1) {
                pathExists = true; // Path exists
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
    }

    public List<String> assemblePath(List<String> outputMaze) {
        List<Cell> path = getCells();
        Collections.reverse(path); // Reverse to get the path from start to end

        // Display the path in the maze
        for (Cell cell : path) {
            int rowIndex = cell.row() * 2 + 1; // Index in outputMaze
            StringBuilder row = new StringBuilder(outputMaze.get(rowIndex));
            int colIndex = cell.col() * 5 + 2; // Position in the row (considering boundaries)
            row.setCharAt(colIndex, '•'); // Mark the path with the symbol "•"
            outputMaze.set(rowIndex, row.toString());
        }
        return outputMaze;
    }

    @Override
    public void printPath(List<String> outputMaze) {
        List<String> mazeElements = assemblePath(outputMaze);

        // Print the maze with the path
        for (String element : mazeElements) {
            System.out.println(element);
        }
        System.out.println();
    }

    // Helper method to reconstruct the path
    private List<Cell> getCells() {
        List<Cell> path = new ArrayList<>();
        int currentRow = maze.height() - 1; // Start from the bottom-right corner
        int currentCol = maze.width() - 1;

        // Reconstruct the path from end to start
        while (currentRow != 0 || currentCol != 0) {
            path.add(new Cell(currentRow, currentCol));
            int parentIndex = parents[currentCol][currentRow];
            if (parentIndex == -1) break; // Exit if there is no parent
            currentRow = parentIndex / maze.width();
            currentCol = parentIndex % maze.width();
        }
        path.add(new Cell(0, 0)); // Add the starting cell
        return path;
    }

    public boolean[][] visited() {
        boolean[][] visitedCopy = new boolean[maze.width()][maze.height()];
        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                visitedCopy[row][col] = visited[col][row];
            }
        }
        return visitedCopy;
    }
}
