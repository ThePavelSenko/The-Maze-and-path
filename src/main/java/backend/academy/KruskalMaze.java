package backend.academy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class KruskalMaze extends AbstractGraphMaze implements CreateGraphShortCycle {
    private final int[][] parent;
    private final int[][] rank;

    public KruskalMaze(int width, int height) {
        super(width, height, true);
        this.parent = new int[width][height];
        this.rank = new int[width][height];
    }

    private void initializeSets() {
        for (int i = 0; i < super.width(); i++) {
            for (int j = 0; j < super.height(); j++) {
                parent[i][j] = i * super.height() + j; // Every cell — parent of yourself
                rank[i][j] = 0; // Начальный ранг равен 0
            }
        }
    }

    private int find(int row, int col) {
        if (parent[row][col] != row * super.height() + col) {
            parent[row][col] = find(parent[row][col] / super.height(), parent[row][col] % super.height());
        }
        return parent[row][col];
    }

    private void union(int row1, int col1, int row2, int col2) {
        int root1 = find(row1, col1);
        int root2 = find(row2, col2);

        if (root1 != root2) {
            // Link two sets
            if (rank[root1 / super.height()][root1 % super.height()]
                > rank[root2 / super.height()][root2 % super.height()]) {
                parent[root2 / super.height()][root2 % super.height()] = root1;
            } else if (rank[root1 / super.height()][root1 % super.height()]
                < rank[root2 / super.height()][root2 % super.height()]) {
                parent[root1 / super.height()][root1 % super.height()] = root2;
            } else {
                parent[root2 / super.height()][root2 % super.height()] = root1;
                rank[root1 / super.height()][root1 % super.height()]++;
            }
        }
    }

    @Override
    public void generateMaze() {
        mazeEdges().clear();
        initializeSets();

        List<Edge> edges = super.edges();
        List<Edge> skippedEdges = new ArrayList<>();
        Collections.shuffle(edges);

        // The main path
        for (Edge edge : edges) {
            Cell cell1 = edge.cell1();
            Cell cell2 = edge.cell2();

            if (find(cell1.row(), cell1.col()) != find(cell2.row(), cell2.col())) {
                union(cell1.row(), cell1.col(), cell2.row(), cell2.col());
                super.addMazeEdge(edge); // Add the edge of the maze (erase the wall)
            } else {
                skippedEdges.add(edge);
            }
        }

        Collections.shuffle(skippedEdges);
        int extraEdges = 10;
        int addedEdges = 0;

        for (Edge edge : skippedEdges) {
            if (addedEdges >= extraEdges) {
                break;
            }
            if (!createsShortCycle(edge, mazeEdges())) {
                super.addMazeEdge(edge);
                addedEdges++;
            }
        }
    }
}



