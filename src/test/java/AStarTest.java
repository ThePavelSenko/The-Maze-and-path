import backend.academy.AbstractGraphMaze;
import backend.academy.AStar;
import backend.academy.KruskalMaze;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class AStarTest {
    private AbstractGraphMaze maze;
    private AStar aStar;

    @BeforeEach
    void setUp() {
        maze = new KruskalMaze(5, 5);
    }

    @Test
    void testFindPath_WhenPathExists() {
        maze.generateMaze();
        aStar = new AStar(maze);
        aStar.findPath(0, 0, 4, 4);

        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
        aStar.assemblePath(outputMaze);
        aStar.printPath(outputMaze);

        assertThat(aStar.obstacle()[4][4]).isFalse();  // Ensure the goal cell is part of the path
    }

    @Test
    void testFindPath_WhenNoValidPath() {
        aStar = new AStar(maze);  // Initialize the AStar algorithm with the maze
        aStar.findPath(0, 0, 4, 4);

        // Assert
        assertThat(aStar.obstacle()[4][4]).isTrue();  // Path should not reach the goal
    }
}

