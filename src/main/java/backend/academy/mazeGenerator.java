package backend.academy;

import java.util.List;
import java.util.Collections;

@FunctionalInterface
public interface mazeGenerator {
    void generateMaze();

    /** Helper method.
    Creating all of possible passages of the maze. **/
    static void initializeEdges(List<Edge> edges, int height, int width) {
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
}
