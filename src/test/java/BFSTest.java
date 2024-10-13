import backend.academy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class BFSTest {

    private BFS bfs;
    private AbstractGraphMaze maze;

    @BeforeEach
    public void setUp() {
        // Инициализируем лабиринт с размером 5x5
        maze = new PrimMaze(5, 5); // Предполагается, что вы используете алгоритм Прима для генерации лабиринта
        maze.generateMaze(); // Генерируем лабиринт
        bfs = new BFS(maze);
    }

    @Test
    public void testFindPath() {
        // Выполняем алгоритм BFS
        bfs.findPath();

        // Проверяем, что путь существует
        boolean[][] visitedCells = bfs.visited();
        assertThat(visitedCells[maze.height() - 1][maze.width() - 1]).isTrue(); // Конечная клетка должна быть посещена
        assertThat(visitedCells[0][0]).isTrue(); // Начальная клетка должна быть посещена
    }

    @Test
    public void testPrintPath() {
        // Выполняем поиск пути
        bfs.findPath();

        // Создаем визуальное представление лабиринта
        List<String> outputMaze = maze.assembleMaze(maze.mazeEdges(), maze.height(), maze.width());

        // Печатаем путь
        bfs.printPath(outputMaze);

        List<String> mazeElements = bfs.assemblePath(outputMaze);

        // Проверяем, что начальная и конечная точки отмечены символом '•'
        assertThat(mazeElements.get(1).charAt(2)).isEqualTo('•'); // Начальная точка (0,0)
        int lastRowIndex = (maze.height() - 1) * 2 + 1;
        int lastColIndex = (maze.width() - 1) * 5 + 2;
        assertThat(mazeElements.get(lastRowIndex).charAt(lastColIndex)).isEqualTo('•'); // Конечная точка (4,4)
    }
}
