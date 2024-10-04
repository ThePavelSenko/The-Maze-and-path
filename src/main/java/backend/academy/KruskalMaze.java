package backend.academy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;


public class KruskalMaze implements Maze {
    private final List<Edge> edges = new ArrayList<>();
    private final @Getter List<Edge> mazeEdges = new ArrayList<>();
    private final @Getter int height;
    private final @Getter int width;
    private final @Getter int[][] parent;
    private final @Getter int[][] rank;
    private final @Getter List<String> outputMaze;

    public KruskalMaze(int width, int height) {
        this.width = width;
        this.height = height;
        this.parent = new int[width][height];
        this.rank = new int[width][height];
        this.outputMaze = new ArrayList<>();
        initializeSets(); // Initialize of parent arrays
    }

    private void initializeSets() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                parent[i][j] = i * height + j; // Each cell is its own parent
                rank[i][j] = 0; // Rank equal to 0 for each cell
            }
        }
    }

    private void initializeEdge() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < width - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i + 1, j)));  // horizontal wall
                }
                if (j < height - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i, j + 1)));  // vertical wall
                }
            }
        }
        Collections.shuffle(edges);
    }

    private int find(int row, int col) {
        // Find the root and perform path compression
        if (parent[row][col] != row * height + col) {
            parent[row][col] = find(parent[row][col] / height, parent[row][col] % height);
        }
        return parent[row][col];
    }

    private void union(int row1, int col1, int row2, int col2) {
        int root1 = find(row1, col1);
        int root2 = find(row2, col2);

        if (root1 != root2) {
            // Join two sets
            if (rank[root1 / height][root1 % height] > rank[root2 / height][root2 % height]) {
                parent[root2 / height][root2 % height] = root1;
            } else if (rank[root1 / height][root1 % height] < rank[root2 / height][root2 % height]) {
                parent[root1 / height][root1 % height] = root2;
            } else {
                parent[root2 / height][root2 % height] = root1;
                rank[root1 / height][root1 % height]++;
            }
        }
    }

    // Method for verify existence of edge between two cells
    private boolean hasEdge(int row1, int col1, int row2, int col2) {
        if (row2 >= height || col2 >= width) {
            return true;
        } // Out borders always with walls
        for (Edge edge : mazeEdges) {
            if (edge.cell1.col() == col1 && edge.cell1.row() == row1
                && edge.cell2.col() == col2 && edge.cell2.row() == row2) {
                return false; // No wall (the edge is missing)
            }
        }
        return true; // There is the edge
    }

    @Override
    public void generateMaze() {
        initializeEdge();

        for (Edge edge : edges) {
            Cell cell1 = edge.cell1;
            Cell cell2 = edge.cell2;

            if (find(cell1.row(), cell1.col()) != find(cell2.row(), cell2.col())) {
                union(cell1.row(), cell1.col(), cell2.row(), cell2.col());
                mazeEdges.add(edge); // Add an edge to the maze (remove the wall)
            }
        }
    }

    @Override
    public void assembleMaze() {
        // Creating the top row (upper horizontal border)
        StringBuilder topBorder = new StringBuilder();
        for (int j = 0; j < width; j++) {
            topBorder.append("N++++");
        }
        topBorder.append("N");
        outputMaze.add(topBorder.toString());

        for (int i = 0; i < height; i++) {
            StringBuilder verticalWalls = new StringBuilder();

            // Vertical walls and passages between the cells
            for (int j = 0; j < width; j++) {
                if (j == 0 && i == 0) {
                    verticalWalls.append(" "); // Making the enter for the maze
                } else if (j == 0) {
                    verticalWalls.append("|"); // Left border
                }
                if (hasEdge(i, j, i, j + 1) && !(j == width - 1 && i == height - 1)) {
                    verticalWalls.append("    |"); // Vertical wall
                } else {
                    verticalWalls.append("     "); // Passage
                }
            }
            outputMaze.add(verticalWalls.toString()); // Add the string at outputMaze

            // Горизонтальные стены между клетками
            StringBuilder horizontalWalls = new StringBuilder();
            for (int j = 0; j < width; j++) {
                horizontalWalls.append("N"); // The node between the cells
                if (hasEdge(i, j, i + 1, j)) {
                    horizontalWalls.append("++++"); // Horizontal wall
                } else {
                    horizontalWalls.append("    "); // Passage
                }
            }
            horizontalWalls.append("N"); // Right border
            outputMaze.add(horizontalWalls.toString()); // Add the String at outputMaze
        }
    }
}
