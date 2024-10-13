import backend.academy.AbstractGraphMaze;
import backend.academy.BFS;
import backend.academy.PrimMaze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class BFSTest {

    private BFS bfs;
    private AbstractGraphMaze maze;

    @BeforeEach
    public void setUp() {
        // Initialize the maze with size 5x5
        maze = new PrimMaze(5, 5); // Assuming you're using Prim's algorithm to generate the maze
        maze.generateMaze(); // Generate the maze
        bfs = new BFS(maze);
    }

    @Test
    public void testFindPath() {
        // Execute the BFS algorithm
        bfs.findPath();

        // Check that a path exists
        boolean[][] visitedCells = bfs.visited();
        assertThat(visitedCells[maze.height() - 1][maze.width() - 1]).isTrue(); // The final cell should be visited
        assertThat(visitedCells[0][0]).isTrue(); // The starting cell should be visited
    }

    @Test
    public void testPrintPath() {
        // Perform the path search
        bfs.findPath();

        // Create a visual representation of the maze
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());

        // Print the path on the maze
        bfs.printPath(outputMaze);

        List<String> mazeElements = bfs.assemblePath(outputMaze);

        // Check that the start and end points are marked with the symbol '•'
        assertThat(mazeElements.get(1).charAt(2)).isEqualTo('•'); // Start point (0,0)
        int lastRowIndex = (maze.height() - 1) * 2 + 1;
        int lastColIndex = (maze.width() - 1) * 5 + 2;
        assertThat(mazeElements.get(lastRowIndex).charAt(lastColIndex)).isEqualTo('•'); // End point (4,4)
    }
}
