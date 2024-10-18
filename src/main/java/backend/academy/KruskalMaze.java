package backend.academy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * KruskalMaze implements the CreateGraphShortCycle interface to generate mazes
 * based on Kruskal's algorithm. This class ensures that it avoids creating
 * short cycles, thereby generating multiple paths while maintaining the integrity
 * of the maze structure.
 * <p>This algorithm is designed for weighted graphs.</p>
 */
public final class KruskalMaze extends AbstractGraphMaze implements CreateGraphShortCycle {
    private final int[] parent;  // Flattened parent array for union-find
    private final int[] rank;    // Flattened rank array for union-find

    public KruskalMaze(int width, int height) {
        super(width, height, true);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive values.");
        }
        this.parent = new int[width * height];
        this.rank = new int[width * height];
    }

    private void initializeSets() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i; // Every cell is the parent of itself
            rank[i] = 0;   // Initial rank is 0
        }
    }

    private int find(int index) {
        if (parent[index] != index) {
            parent[index] = find(parent[index]);  // Path compression
        }
        return parent[index];
    }

    private void union(int index1, int index2) {
        int root1 = find(index1);
        int root2 = find(index2);

        if (root1 != root2) {
            // Union by rank
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else {
                parent[root2] = root1;
                rank[root1]++;
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

        // Main path generation
        for (Edge edge : edges) {
            Cell cell1 = edge.cell1();
            Cell cell2 = edge.cell2();

            int index1 = cell1.row() * super.width() + cell1.col();
            int index2 = cell2.row() * super.width() + cell2.col();

            if (find(index1) != find(index2)) {
                union(index1, index2);
                super.addMazeEdge(edge);  // Add edge to the maze (remove the wall)
            } else {
                skippedEdges.add(edge);
            }
        }

        // Adding extra edges to avoid short cycles
        Collections.shuffle(skippedEdges);
        int addedEdges = 0;
        for (Edge edge : skippedEdges) {
            if (addedEdges >= MIN_CYCLE_LENGTH) {
                break;
            }
            if (!createsShortCycle(edge, mazeEdges())) {
                super.addMazeEdge(edge);
                addedEdges++;
            }
        }
    }
}
