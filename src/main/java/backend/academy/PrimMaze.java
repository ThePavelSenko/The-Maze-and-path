package backend.academy;

import java.util.ArrayList;
import java.util.List;

/**
 * PrimMaze generates a maze using Prim's algorithm. This algorithm grows the maze
 * by starting from a random cell and iteratively adding the closest adjacent cell
 * until all cells are visited.
 */
public final class PrimMaze extends AbstractGraphMaze {
    private final List<Cell> notVisitedCells;

    public PrimMaze(int width, int height) {
        super(width, height, false);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive values.");
        }
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
        if (notVisitedCells.isEmpty()) {
            throw new IllegalStateException("There are no cells to choose from.");
        }
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
