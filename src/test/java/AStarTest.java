import backend.academy.AbstractGraphMaze;
import backend.academy.AStar;
import backend.academy.KruskalMaze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class AStarTest {

    private AStar astar;
    private AbstractGraphMaze maze;

    @BeforeEach
    public void setUp() {
        // Initialize the maze, assuming it's 10x10
        maze = new KruskalMaze(10, 10);
        maze.generateMaze(); // Generate the maze
        astar = new AStar(maze); // Initialize A* algorithm
    }

    @Test
    public void testFindPathInCyclicMaze() {
        // Execute the A* algorithm to find a path
        astar.findPath();

        // Retrieve visited cells and check that the path reaches the destination
        boolean[][] path = astar.obstacle();
        assertThat(path[9][9]).isFalse(); // Check if the final destination is reached

        // Verify that the starting point was also visited
        assertThat(path[0][0]).isFalse();
    }

    @Test
    public void testPathIsCorrectlyReconstructed() {
        // Create an initial maze layout without a path
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());

        // Find the path using A*
        astar.findPath();

        // Retrieve the maze layout with the path displayed
        List<String> mazeElements = astar.assemblePath(outputMaze);

        // Check if the path starts at (0,0) and ends at (9,9)
        assertThat(mazeElements.get(1).charAt(2)).isEqualTo('•'); // Start point
        assertThat(mazeElements.get(mazeElements.size() - 2).charAt(mazeElements.get(mazeElements.size() - 2).length() - 4))
            .isEqualTo('•'); // End point
    }
}
