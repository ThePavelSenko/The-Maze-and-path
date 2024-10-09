package backend.academy;


public final class KruskalMaze extends AbstractGraphMaze {
    // Need generate the start and the end points of the maze
    // Might need to create a few ways to exit in the maze
    private final int[][] parent;
    private final int[][] rank;

    public KruskalMaze(int width, int height) {
        super(width, height);
        this.parent = new int[width][height];
        this.rank = new int[width][height];
    }

    private void initializeSets() {
        for (int i = 0; i < super.width(); i++) {
            for (int j = 0; j < super.height(); j++) {
                parent[i][j] = i * super.height() + j; // Each cell is its own parent
                rank[i][j] = 0; // Rank equal to 0 for each cell
            }
        }
    }

    private int find(int row, int col) {
        // Find the root and perform path compression
        if (parent[row][col] != row * super.height() + col) {
            parent[row][col] = find(parent[row][col] / super.height(), parent[row][col] % super.height());
        }
        return parent[row][col];
    }

    private void union(int row1, int col1, int row2, int col2) {
        int root1 = find(row1, col1);
        int root2 = find(row2, col2);

        if (root1 != root2) {
            // Join two sets
            if (rank[root1 / super.height()][root1 % super.height()] > rank[root2 / super.height()][root2 % super.height()]) {
                parent[root2 / super.height()][root2 % super.height()] = root1;
            } else if (rank[root1 / super.height()][root1 % super.height()] < rank[root2 / super.height()][root2 % super.height()]) {
                parent[root1 / super.height()][root1 % super.height()] = root2;
            } else {
                parent[root2 / super.height()][root2 % super.height()] = root1;
                rank[root1 / super.height()][root1 % super.height()]++;
            }
        }
    }

    @Override
    public void generateMaze() {
        initializeSets(); // Initialize of parent arrays

        for (Edge edge : super.edges()) {
            Cell cell1 = edge.cell1();
            Cell cell2 = edge.cell2();

            if (find(cell1.row(), cell1.col()) != find(cell2.row(), cell2.col())) {
                union(cell1.row(), cell1.col(), cell2.row(), cell2.col());
                super.addMazeEdge(edge); // Add an edge to the maze (remove the wall)
            }
        }
    }
}
