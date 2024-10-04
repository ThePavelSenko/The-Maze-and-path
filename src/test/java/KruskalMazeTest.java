import backend.academy.KruskalMaze;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

public class KruskalMazeTest {
    KruskalMaze maze;

    @BeforeEach
    public void setUp() {
        maze = new KruskalMaze(10, 10);
        maze.generateMaze();
        maze.assembleMaze();
    }

    @Test
    public void testMazeNotNullAndEmpty() {
        assertThat(maze.mazeEdges()).isNotNull().isNotEmpty();
        assertThat(maze.outputMaze()).isNotNull().isNotEmpty();
    }

    @Test
    public void testCorrectQuantityOfEdges() {
        // Quantity of edges must be "quantity of cells" - 1, because we must link all cells without cycles
        Assertions.assertEquals(maze.width() * maze.height() - 1,
            maze.mazeEdges().size());
    }
}
