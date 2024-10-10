package backend.academy;

import java.util.*;

public class BFS {
    private final AbstractGraphMaze maze;
    private final int[][] parents; // Для восстановления пути
    private final boolean[][] visited;

    public BFS(AbstractGraphMaze maze) {
        this.maze = maze;
        this.parents = new int[maze.width()][maze.height()];
        this.visited = new boolean[maze.width()][maze.height()];
    }

    private void initializeParents() {
        for (int i = 0; i < parents.length; i++) {
            Arrays.fill(parents[i], -1); // Инициализируем каждую строку значением -1
        }
    }

    public void findPath() {
        Queue<Cell> queue = new LinkedList<>();
        queue.add(new Cell(0, 0)); // Начинаем с верхнего левого угла
        visited[0][0] = true;
        initializeParents(); // Инициализация родителей

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            // Если достигли правого нижнего угла, завершаем поиск
            if (current.row() == maze.height() - 1 && current.col() == maze.width() - 1) {
                break;
            }

            // Проверяем соседние клетки
            for (Edge edge : maze.mazeEdges()) {
                Cell neighbor = null;
                if (edge.cell1().equals(current)) {
                    neighbor = edge.cell2();
                } else if (edge.cell2().equals(current)) {
                    neighbor = edge.cell1();
                }

                if (neighbor != null && !visited[neighbor.col()][neighbor.row()]) {
                    visited[neighbor.col()][neighbor.row()] = true;
                    queue.add(neighbor);
                    parents[neighbor.col()][neighbor.row()] = current.row() * maze.width() + current.col(); // Сохраняем родителя
                }
            }
        }
    }


    // Метод для восстановления и отображения пути
    public void printPath(List<String> outputMaze) {
        List<Cell> path = new ArrayList<>();
        int currentRow = maze.height() - 1; // Конечная точка
        int currentCol = maze.width() - 1;

        // Восстанавливаем путь от конца до начала
        while (currentRow != 0 || currentCol != 0) {
            path.add(new Cell(currentRow, currentCol));
            int parentIndex = parents[currentCol][currentRow];
            if (parentIndex == -1) break; // Если нет родителя, выходим из цикла
            currentRow = parentIndex / maze.width();
            currentCol = parentIndex % maze.width();
        }
        path.add(new Cell(0, 0)); // Добавляем стартовую клетку
        Collections.reverse(path); // Переворачиваем, чтобы получить путь от начала до конца

        // Отображаем путь в лабиринте
        for (Cell cell : path) {
            int rowIndex = cell.row() * 2 + 1; // Индекс в outputMaze
            StringBuilder row = new StringBuilder(outputMaze.get(rowIndex));
            int colIndex = cell.col() * 5 + 2; // Позиция в строке (с учетом границ)
            row.setCharAt(colIndex, '•'); // Обозначаем путь символом "•"
            outputMaze.set(rowIndex, row.toString());
        }
        for (String element: outputMaze) {
            System.out.println(element);
        }
        System.out.println();
    }
}
