import backend.academy.Cell;
import backend.academy.Edge;
import backend.academy.KruskalMaze;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MazeAssemblerTest {
    @Test
    public void testAssembleMazeCorrectlyCreatesPassagesAndWalls() {
        KruskalMaze maze = new KruskalMaze(2, 2);
        List<Edge> mazeEdges = maze.mazeEdges();

        // Manually adding edges to control the creation of passages
        mazeEdges.add(new Edge(new Cell(0, 0), new Cell(0, 1), 0)); // Passage (0,0) <-> (0,1)
        mazeEdges.add(new Edge(new Cell(0, 1), new Cell(1, 1), 0)); // Passage (0,1) <-> (1,1)

        // Assemble the maze
        List<String> outputMaze = maze.assembleMaze(mazeEdges, maze.height(), maze.width());

        // Verifying walls and passages
        assertThat(outputMaze.get(1)).contains("     "); // No wall between (0,0) and (0,1)
        assertThat(outputMaze.get(3)).contains("│    |    "); // Wall between (1,0) and (1,1)
    }

    @Test
    public void testAssembleMazeBordersAlwaysHaveEdges() {
        KruskalMaze maze = new KruskalMaze(10, 10);

        // Assemble the maze
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());

        // Check top and bottom borders for correctness
        String topBorder = outputMaze.getFirst();
        String bottomBorder = outputMaze.getLast();

        assertThat(topBorder).isEqualTo("┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼");
        assertThat(bottomBorder).isEqualTo("┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼");
    }
}
