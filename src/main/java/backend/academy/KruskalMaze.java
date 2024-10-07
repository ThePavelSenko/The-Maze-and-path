package backend.academy;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class KruskalMaze implements MazeGenerator, MazeAssemblerUsingGraphs, MazePrinter {
    private final List<Edge> edges = new ArrayList<>();
    private final List<Edge> mazeEdges;
    private final List<String> outputMaze;
    private final @Getter int height;
    private final @Getter int width;
    private final int[][] parent;
    private final int[][] rank;

    public KruskalMaze(int width, int height) {
        this.width = width;
        this.height = height;
        this.mazeEdges = new ArrayList<>();
        this.outputMaze = new ArrayList<>();
        this.parent = new int[width][height];
        this.rank = new int[width][height];
        MazeGenerator.initializeEdges(edges, height, width);
    }

    private void initializeSets() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                parent[i][j] = i * height + j; // Each cell is its own parent
                rank[i][j] = 0; // Rank equal to 0 for each cell
            }
        }
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

    @Override
    public void generateMaze() {
        initializeSets(); // Initialize of parent arrays

        for (Edge edge : edges) {
            Cell cell1 = edge.cell1();
            Cell cell2 = edge.cell2();

            if (find(cell1.row(), cell1.col()) != find(cell2.row(), cell2.col())) {
                union(cell1.row(), cell1.col(), cell2.row(), cell2.col());
                mazeEdges.add(edge); // Add an edge to the maze (remove the wall)
            }
        }
    }

    @Override
    public void printMaze() {
        this.generateMaze();
        this.assembleMaze(outputMaze, mazeEdges, height, width);
        try (PrintStream printStream = new PrintStream(System.out)) {
            for (String mazeElement : outputMaze) {
                printStream.println(mazeElement);
            }
        }
    }

    public List<Edge> mazeEdgesCopy() {
        return new ArrayList<>(mazeEdges);
    }

    public List<String> outputMazeCopy() {
        return new ArrayList<>(outputMaze);
    }
}
