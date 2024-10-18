package backend.academy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import lombok.Getter;
import static backend.academy.Utils.OUT;

/**
 * AStar class implements the A* pathfinding algorithm for finding the shortest path
 * through a maze. It utilizes a heuristic (Manhattan distance) to optimize the pathfinding
 * process. This class marks the path within the maze and can print the maze with the
 * path highlighted.
 * <p>This algorithm is designed for weighted graphs.</p>
 */
public class AStar implements MazePathFinder {
    private final @Getter boolean[][] obstacle;
    private final int[][] gScore;  // Real path costs
    private final int[][] fScore;  // Heuristic function (g + heuristic)
    private final Cell[][] cameFrom;  // To reconstruct the path
    private final AbstractGraphMaze maze;
    private static final int CELL_WIDTH = 5;  // Width of each cell in the string representation
    private static final int MAX_LIVES = 3; // Maximum number of lives
    private static final int BAD_WEIGHT = 3;

    public AStar(AbstractGraphMaze maze) {
        this.maze = maze;
        this.obstacle = new boolean[maze.height()][maze.width()];
        this.gScore = new int[maze.height()][maze.width()];
        this.fScore = new int[maze.height()][maze.width()];
        this.cameFrom = new Cell[maze.height()][maze.width()];
        initializeObstacles();
    }

    private void initializeObstacles() {
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                obstacle[i][j] = true; // By default, all cells are obstacles
                gScore[i][j] = Integer.MAX_VALUE;
                fScore[i][j] = Integer.MAX_VALUE;
                cameFrom[i][j] = null;
            }
        }
    }

    // Heuristic function: Manhattan distance
    private int heuristic(Cell current, Cell goal) {
        return Math.abs(current.row() - goal.row()) + Math.abs(current.col() - goal.col());
    }

    /**
     * Finds the path from the start cell to the goal cell using the A* algorithm
     * with the condition that the player has a limited number of lives.
     */
    @Override
    public void findPath(int startX, int startY, int endX, int endY) {
        try {
            Cell start = new Cell(startX, startY);
            Cell goal = new Cell(endX, endY);
            boolean reachedGoal = false;

            // Priority queue to store the cells to be explored
            PriorityQueue<PathState> openSet =
                new PriorityQueue<>(Comparator.comparingInt(s -> fScore[s.cell().row()][s.cell().col()]));
            openSet.add(new PathState(start, MAX_LIVES));

            gScore[start.row()][start.col()] = 0;
            fScore[start.row()][start.col()] = heuristic(start, goal);

            while (!openSet.isEmpty()) {
                PathState currentState = openSet.poll();
                Cell current = currentState.cell();
                int currentLives = currentState.lives();

                // If the goal is reached and the condition of lives is satisfied
                if (current.equals(goal)) {
                    if (currentLives > 0) {
                        reconstructPath(goal);
                        reachedGoal = true;
                    } else {
                        OUT.println("No valid path found with enough lives.");
                        reachedGoal = true;
                    }
                }
                if (reachedGoal) {
                    return;
                }

                List<Edge> neighbors = getNeighbors(current);
                for (Edge edge : neighbors) {
                    Cell neighbor = (edge.cell1().equals(current)) ? edge.cell2() : edge.cell1();

                    int tentativeGScore = gScore[current.row()][current.col()] + edge.weight();
                    int tentativeLives = currentLives;

                    // Adjust lives based on the edge weight
                    if (edge.weight() == 1) {
                        tentativeLives = Math.max(MAX_LIVES, tentativeLives + 1);
                    } else if (edge.weight() == BAD_WEIGHT) {
                        tentativeLives--;  // Lose one life
                        // If no lives remain, skip this path
                        if (tentativeLives <= 0) {
                            continue;
                        }
                    }

                    if (tentativeGScore < gScore[neighbor.row()][neighbor.col()]) {
                        cameFrom[neighbor.row()][neighbor.col()] = current;
                        gScore[neighbor.row()][neighbor.col()] = tentativeGScore;
                        fScore[neighbor.row()][neighbor.col()] =
                            gScore[neighbor.row()][neighbor.col()] + heuristic(neighbor, goal);

                        openSet.add(new PathState(neighbor, tentativeLives));
                    }
                }
            }

            // If the open set is empty and we haven't found a valid path
            OUT.println("No valid path found.");

        } catch (ArrayIndexOutOfBoundsException e) {
            OUT.println("Error: Attempted to access an invalid index in the maze: " + e.getMessage());
        } catch (Exception e) {
            OUT.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private List<Edge> getNeighbors(Cell cell) {
        List<Edge> neighbors = new ArrayList<>();
        List<Edge> edges = maze.mazeEdges();  // Assumes that the maze edges contain weight information
        for (Edge edge : edges) {
            if (edge.cell1().equals(cell) || edge.cell2().equals(cell)) {
                neighbors.add(edge);
            }
        }
        return neighbors;
    }

    private void reconstructPath(Cell goal) {
        Cell current = goal;
        while (current != null) {
            obstacle[current.row()][current.col()] = false; // Mark the path
            current = cameFrom[current.row()][current.col()];
        }
    }

    /**
     * Assembles the output maze with the path marked.
     *
     * @param outputMaze A list of strings representing the maze to be modified.
     * @return The modified maze with the path marked.
     */
    public List<String> assemblePath(List<String> outputMaze) {
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                if (!obstacle[i][j]) {
                    int rowIndex = i * 2 + 1; // Row index in outputMaze
                    StringBuilder row = new StringBuilder(outputMaze.get(rowIndex));
                    int colIndex = j * CELL_WIDTH + 2; // Position in the string (considering borders)
                    row.setCharAt(colIndex, '•'); // Mark the path with '•'
                    outputMaze.set(rowIndex, row.toString());
                }
            }
        }
        return outputMaze;
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

    // Helper class to track the state of a path (cell and lives left)
    private record PathState(Cell cell, int lives) {
    }
}
