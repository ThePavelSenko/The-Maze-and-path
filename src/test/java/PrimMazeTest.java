import backend.academy.PrimMaze;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PrimMazeTest {
    PrimMaze maze;

    @BeforeEach
    public void setUp() {
        maze = new PrimMaze(10, 10);
        maze.generateMaze();
    }

    @Test
    public void testMazeNotNullAndEmpty() {
        assertThat(maze.mazeEdges()).isNotNull().isNotEmpty();
    }

    @Test
    public void testCorrectQuantityOfEdges() {
        // Quantity of edges must be "quantity of cells" - 1, because we must link all cells without cycles
        Assertions.assertEquals(maze.width() * maze.height() - 1,
            maze.mazeEdges().size());
    }
}
