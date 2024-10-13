import backend.academy.KruskalMaze;
import backend.academy.Edge;
import backend.academy.Cell;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MazeAssemblerTest {
    // Test for the accuracy of printing the Maze
    @Test
    public void testAssembleMazeCorrectlyCreatesPassagesAndWalls() {
        KruskalMaze maze = new KruskalMaze(2, 2);
        List<Edge> mazeEdges = maze.mazeEdges();
        List<String> outputMaze = maze.assembleMaze(mazeEdges, maze.height(), maze.width());

        // Manually create some edges to control the presence of walls and passages
        mazeEdges.add(new Edge(new Cell(0, 0), new Cell(0, 1))); // Passage between (0,0) and (0,1)
        mazeEdges.add(new Edge(new Cell(0, 1), new Cell(1, 1))); // Passage between (0,1) and (1,1)


        assertThat(outputMaze.get(1)).contains("     "); // Absence of a wall between cells (0,0) and (0,1)
        assertThat(outputMaze.get(3)).contains("    │"); // Wall between cells (1,0) and (1,1)
    }

    @Test
    public void testAssembleMazeBordersAlwaysHaveEdges() {
        KruskalMaze maze = new KruskalMaze(5, 5);
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());


        // Check that the right borders of the maze always contain vertical walls "│"
        for (int i = 3; i < outputMaze.size() - 2; i += 2) { // Iterate through each row containing vertical boundaries
            assertThat(outputMaze.get(i)).matches("│ {4}│ {4}│ {4}│ {4}│ {4}│"); // Ensure vertical walls are present
        }

        // Check that the top and bottom borders of the maze exist and are correct
        String topBorder = outputMaze.getFirst();
        String bottomBorder = outputMaze.getLast();

        assertThat(topBorder).isEqualTo("┼────┼────┼────┼────┼────┼");
        assertThat(bottomBorder).isEqualTo("┼────┼────┼────┼────┼────┼");
    }
}
