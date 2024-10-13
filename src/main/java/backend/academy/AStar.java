package backend.academy;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

// for kruskal maze
public class AStar implements MazePathFinder {
    private final @Getter boolean[][] obstacle;
    private final int[][] gScore;  // Реальные стоимости пути
    private final int[][] fScore;  // Оценочная функция (g + эвристика)
    private final Cell[][] cameFrom;  // Для восстановления пути
    private final AbstractGraphMaze maze;

    public AStar(AbstractGraphMaze maze) {
        this.maze = maze;
        this.obstacle = new boolean[maze.width()][maze.height()];
        this.gScore = new int[maze.width()][maze.height()];
        this.fScore = new int[maze.width()][maze.height()];
        this.cameFrom = new Cell[maze.width()][maze.height()];
        initializeObstacles();
    }

    private void initializeObstacles() {
        for (int i = 0; i < maze.width(); i++) {
            for (int j = 0; j < maze.height(); j++) {
                obstacle[i][j] = true; // Все клетки по умолчанию являются препятствиями
                gScore[i][j] = Integer.MAX_VALUE;
                fScore[i][j] = Integer.MAX_VALUE;
                cameFrom[i][j] = null;
            }
        }
    }

    // Эвристическая функция: Манхэттенское расстояние
    private int heuristic(Cell current, Cell goal) {
        return Math.abs(current.row() - goal.row()) + Math.abs(current.col() - goal.col());
    }

    @Override
    public void findPath() {
        Cell start = new Cell(0, 0);
        Cell goal = new Cell(maze.width() - 1, maze.height() - 1);

        // Используем очередь с приоритетом для выбора клетки с минимальной f-стоимостью
        PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(c -> fScore[c.row()][c.col()]));
        openSet.add(start);

        gScore[start.row()][start.col()] = 0;
        fScore[start.row()][start.col()] = heuristic(start, goal);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();

            if (current.equals(goal)) {
                reconstructPath(goal);
                return;
            }

            // Получаем соседей из лабиринта (по граням)
            List<Cell> neighbors = getNeighbors(current);
            for (Cell neighbor : neighbors) {
                int tentativeGScore = gScore[current.row()][current.col()] + 1; // Стоимость прохода к соседу

                if (tentativeGScore < gScore[neighbor.row()][neighbor.col()]) {
                    cameFrom[neighbor.row()][neighbor.col()] = current;
                    gScore[neighbor.row()][neighbor.col()] = tentativeGScore;
                    fScore[neighbor.row()][neighbor.col()] = gScore[neighbor.row()][neighbor.col()] + heuristic(neighbor, goal);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
    }

    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        List<Edge> edges = maze.mazeEdges();
        for (Edge edge : edges) {
            if (edge.cell1().equals(cell)) {
                neighbors.add(edge.cell2());
            } else if (edge.cell2().equals(cell)) {
                neighbors.add(edge.cell1());
            }
        }
        return neighbors;
    }

    // Восстанавливаем путь
    private void reconstructPath(Cell goal) {
        Cell current = goal;
        while (current != null) {
            obstacle[current.row()][current.col()] = false; // Отмечаем путь
            current = cameFrom[current.row()][current.col()];
        }
    }

    public List<String> assemblePath(List<String> outputMaze) {
        for (int i = 0; i < maze.width(); i++) {
            for (int j = 0; j < maze.height(); j++) {
                if (!obstacle[i][j]) {
                    int rowIndex = i * 2 + 1; // Индекс в outputMaze
                    StringBuilder row = new StringBuilder(outputMaze.get(rowIndex));
                    int colIndex = j * 5 + 2; // Позиция в строке (с учетом границ)
                    row.setCharAt(colIndex, '•'); // Обозначаем путь символом "•"
                    outputMaze.set(rowIndex, row.toString());
                }
            }
        }
        return outputMaze;
    }

    @Override
    public void printPath(List<String> outputMaze) {
        List<String> mazeElements = assemblePath(outputMaze);
        for (String element: mazeElements) {
            System.out.println(element);
        }
        System.out.println();
    }
}
