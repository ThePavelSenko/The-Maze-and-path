package backend.academy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import lombok.Getter;
import static backend.academy.Utils.OUT;

// for Kruskal Maze
public class AStar implements MazePathFinder {
    private final @Getter boolean[][] obstacle;
    private final int[][] gScore;  // Real path costs
    private final int[][] fScore;  // Heuristic function (g + heuristic)
    private final Cell[][] cameFrom;  // To reconstruct the path
    private final AbstractGraphMaze maze;
    private static final int CELL_WIDTH = 5;  // Width of each cell in the string representation

    public AStar(AbstractGraphMaze maze) {
        this.maze = maze;
        this.obstacle = new boolean[maze.width()][maze.height()];
        this.gScore = new int[maze.width()][maze.height()];
        this.fScore = new int[maze.width()][maze.height()];
        this.cameFrom = new Cell[maze.width()][maze.height()];
        initializeObstacles();
    }

    private void initializeObstacles() {
        for (int i = 0; i < maze.width(); i++) {
            for (int j = 0; j < maze.height(); j++) {
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

    @Override
    public void findPath() {
        Cell start = new Cell(0, 0);
        Cell goal = new Cell(maze.width() - 1, maze.height() - 1);

        // Priority queue for cells with the minimum f-cost
        PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(c -> fScore[c.row()][c.col()]));
        openSet.add(start);

        gScore[start.row()][start.col()] = 0;
        fScore[start.row()][start.col()] = heuristic(start, goal);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();

            // If the goal is reached, reconstruct the path
            if (current.equals(goal)) {
                reconstructPath(goal);
                return;
            }

            // Get neighbors considering edges and their weights
            List<Edge> neighbors = getNeighbors(current);
            for (Edge edge : neighbors) {
                Cell neighbor = (edge.cell1().equals(current)) ? edge.cell2() : edge.cell1();
                int tentativeGScore = gScore[current.row()][current.col()] + edge.weight();  // Consider the edge weight

                if (tentativeGScore < gScore[neighbor.row()][neighbor.col()]) {
                    cameFrom[neighbor.row()][neighbor.col()] = current;
                    gScore[neighbor.row()][neighbor.col()] = tentativeGScore;
                    fScore[neighbor.row()][neighbor.col()] =
                        gScore[neighbor.row()][neighbor.col()] + heuristic(neighbor, goal);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
    }

    // Retrieve neighbors along with edges
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

    // Reconstruct the path
    private void reconstructPath(Cell goal) {
        Cell current = goal;
        while (current != null) {
            obstacle[current.row()][current.col()] = false; // Mark the path
            current = cameFrom[current.row()][current.col()];
        }
    }

    public List<String> assemblePath(List<String> outputMaze) {
        for (int i = 0; i < maze.width(); i++) {
            for (int j = 0; j < maze.height(); j++) {
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

    @Override
    public void printPath(List<String> outputMaze) {
        List<String> mazeElements = assemblePath(outputMaze);
        for (String element : mazeElements) {
            OUT.println(element);
        }
        OUT.println();
    }
}
