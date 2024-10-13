package backend.academy;

import java.util.ArrayList;
import java.util.List;

public final class PrimMaze extends AbstractGraphMaze {
    private final List<Cell> notVisitedCells;

    public PrimMaze(int width, int height) {
        super(width, height);
        this.notVisitedCells = new ArrayList<>();
    }

    private void initializeNotVisitedCells() {
        for (int i = 0; i < super.width(); i++) {
            for (int j = 0; j < super.height(); j++) {
                notVisitedCells.add(new Cell(i, j));
            }
        }
    }

    private Cell chooseStartCell() {
        initializeNotVisitedCells();
        return Utils.getRandomValue(notVisitedCells);
    }

    private void addAdjacentEdges(Cell cell, List<Edge> currentEdges) {
        for (Edge edge : super.edges()) {
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

        // List of edges that lead to the current cell
        List<Edge> currentEdges = new ArrayList<>();

        addAdjacentEdges(currentCell, currentEdges);

        while (!notVisitedCells.isEmpty() && !currentEdges.isEmpty()) {
            Edge randomEdge = Utils.getRandomValue(currentEdges);
            Cell visitedCell;

            // If the edge does not already exist in the maze
            if (!super.mazeEdges().contains(randomEdge)) {
                if (notVisitedCells.contains(randomEdge.cell1())) {
                    visitedCell = randomEdge.cell1();
                } else if (notVisitedCells.contains(randomEdge.cell2())) {
                    visitedCell = randomEdge.cell2();
                } else {
                    currentEdges.remove(randomEdge);
                    continue;
                }

                // Remove the visited cell from the list of not visited cells
                notVisitedCells.remove(visitedCell);
                super.addMazeEdge(randomEdge);
                addAdjacentEdges(visitedCell, currentEdges);
            }
            currentEdges.remove(randomEdge);
        }
    }
}
