package backend.academy;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class PrimMaze implements MazeGenerator, MazeAssemblerUsingGraphs, MazePrinter {
    private final @Getter int height;
    private final @Getter int width;
    private final List<Edge> edges;
    private final List<Edge> mazeEdges;
    private final List<String> outputMaze;
    private final List<Cell> notVisitedCells;

    public PrimMaze(int width, int height) {
        this.height = height;
        this.width = width;
        this.edges = new ArrayList<>();
        this.mazeEdges = new ArrayList<>();
        this.outputMaze = new ArrayList<>();
        this.notVisitedCells = new ArrayList<>();
        MazeGenerator.initializeEdges(edges, height, width);
    }

    private void initializeNotVisitedCells() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                notVisitedCells.add(new Cell(i, j));
            }
        }
    }

    private Cell chooseStartCell() {
        initializeNotVisitedCells();
        return Utils.getRandomValue(notVisitedCells);
    }

    private void addAdjacentEdges(Cell cell, List<Edge> currentEdges) {
        for (Edge edge : edges) {
            if ((edge.cell1().equals(cell) || edge.cell2().equals(cell))
                && (notVisitedCells.contains(edge.cell1()) || notVisitedCells.contains(edge.cell2()))) {
                currentEdges.add(edge);
            }
        }
    }

    @Override
    public void generateMaze() {
        Cell currentCell = chooseStartCell();
        notVisitedCells.remove(currentCell);

        // List of edges that lead with current cell
        List<Edge> currentEdges = new ArrayList<>();

        addAdjacentEdges(currentCell, currentEdges);

        while (!notVisitedCells.isEmpty() && !currentEdges.isEmpty()) {
            Edge randomEdge = Utils.getRandomValue(currentEdges);
            Cell visitedCell;

            if (!mazeEdges.contains(randomEdge)) {
                if (notVisitedCells.contains(randomEdge.cell1())) {
                    visitedCell = randomEdge.cell1();
                } else if (notVisitedCells.contains(randomEdge.cell2())) {
                    visitedCell = randomEdge.cell2();
                } else {
                    currentEdges.remove(randomEdge);
                    continue;
                }
                notVisitedCells.remove(visitedCell);
                mazeEdges.add(randomEdge);
                addAdjacentEdges(visitedCell, currentEdges);
            }
            currentEdges.remove(randomEdge);
        }
    }

    @Override
    public void printMaze() {
        this.generateMaze();
        this.assembleMaze(outputMaze, mazeEdges, height, width);
        try (PrintStream printStream = new PrintStream(System.out)) {
            for (String mazeElement : outputMaze) {
                printStream.println(mazeElement);
            }
        }
    }

    public List<Edge> mazeEdgesCopy() {
        return new ArrayList<>(mazeEdges);
    }

    public List<String> outputMazeCopy() {
        return new ArrayList<>(outputMaze);
    }
}
