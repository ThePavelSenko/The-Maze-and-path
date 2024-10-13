import backend.academy.KruskalMaze;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

public class KruskalMazeTest {
    KruskalMaze maze;

    @BeforeEach
    public void setUp() {
        maze = new KruskalMaze(10, 10);
    }

    @Test
    public void testMazeNotNullAndEmpty() {
        maze.generateMaze();

        assertThat(maze.mazeEdges()).isNotNull().isNotEmpty();
    }

    @Test
    public void testExtraEdgesInMaze() {
        maze.generateMaze();

        // Verify, that quantity of edges according to size of minimal frame + quantity of extra edges
        int expectedEdges = (maze.width() * maze.height() - 1);

        assertThat(maze.mazeEdges().size())
            .as("Quantity of edges in maze must equal to quantity of cells - 1 + additional path")
            .isGreaterThanOrEqualTo(expectedEdges);
    }
}
