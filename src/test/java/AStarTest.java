import backend.academy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

public class AStarTest {

    private AStar astar;
    private AbstractGraphMaze maze;

    @BeforeEach
    public void setUp() {
        // Инициализируем лабиринт, предположим, что он имеет размер 5x5
        maze = new KruskalMaze(10, 10);
        maze.generateMaze(); // Генерируем лабиринт
        astar = new AStar(maze);
    }

    @Test
    public void testFindPathInCyclicMaze() {
        // Выполняем алгоритм A*
        astar.findPath();

        // Получаем посещенные клетки и проверяем, что путь существует
        boolean[][] path = astar.obstacle();
        assertThat(path[9][9]).isFalse(); // Проверяем, что мы достигли конечной точки

        // Проверяем, что начальная точка тоже была посещена
        assertThat(path[0][0]).isFalse();
    }

    @Test
    public void testPathIsCorrectlyReconstructed() {
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());
        // Найти путь
        astar.findPath();

        // Получаем отображение лабиринта с проложенным путем
        List<String> mazeElements = astar.assemblePath(outputMaze);

        // Проверяем, что маршрут проложен от (0,0) до (4,4)
        assertThat(mazeElements.get(1).charAt(2)).isEqualTo('•'); // Начальная точка
        assertThat(mazeElements.get(mazeElements.size() - 2).charAt(mazeElements.get(mazeElements.size() - 2).length() - 4))
            .isEqualTo('•'); // Конечная точка
    }
}
