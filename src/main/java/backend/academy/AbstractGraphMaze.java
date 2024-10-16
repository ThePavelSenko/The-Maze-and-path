package backend.academy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import static backend.academy.Utils.OUT;

/** This class use three interfaces.
 * Should be using for algorithms that using graphs.
 * <p>Using this class, you must implement the generateMaze() method.</p>
 * You should change mazeEdges field in generateMaze() method -
 * this field should contain passages in your generated maze.
 * Use addMazeEdge() method for it.  **/
public abstract class AbstractGraphMaze implements Maze {
    private final List<Edge> edges = new ArrayList<>();
    private final List<Edge> mazeEdges = new ArrayList<>();
    private final @Getter int height;
    private final @Getter int width;

    private static final int LOW_WEIGHT = 1;
    private static final int HIGH_WEIGHT = 3;
    private static final int DEFAULT_WEIGHT = 2;
    private static final String LOW_WEIGHT_SYMBOL = " $  ";
    private static final String HIGH_WEIGHT_SYMBOL = " ~  ";
    private static final String DEFAULT_SYMBOL = "    ";

    public AbstractGraphMaze(int width, int height, boolean useWeighs) {
        this.width = width;
        this.height = height;
        if (useWeighs) {
            initializeEdgesWithWeights(edges, height, width);
        } else {
            initializeEdges(edges, height, width);
        }
    }

    // Initialize of the all possible passages
    private void initializeEdges(List<Edge> edges, int height, int width) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < width - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i + 1, j)));  // horizontal wall
                }
                if (j < height - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i, j + 1)));  // vertical wall
                }
            }
        }
        Collections.shuffle(edges);
    }

    // Initialize all possible passages with weighs
    private void initializeEdgesWithWeights(List<Edge> edges, int height, int width) {
        List<Integer> possibleWeights = List.of(LOW_WEIGHT, DEFAULT_WEIGHT, HIGH_WEIGHT);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < width - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i + 1, j),
                        Utils.getRandomValue(possibleWeights)));
                }
                if (j < height - 1) {
                    edges.add(new Edge(new Cell(i, j), new Cell(i, j + 1),
                        Utils.getRandomValue(possibleWeights)));  // vertical wall
                }
            }
        }
    }

    @Override
    public abstract void generateMaze();

    // This method change the field, use it carefully!
    protected void addMazeEdge(Edge edge) {
        mazeEdges.add(edge);
    }

    /**
     * Method for assemble of the maze.
     * The essence of the method - it takes a set of cells of the size height*width (such as in a chessboard)
     * and makes passes in them, which are contained in mazeEdges, after which it is stuffed into the outputMaze
     **/
    public List<String> assembleMaze(List<Edge> mazeEdges, int height, int width) {
        List<String> outputMaze = new ArrayList<>();
        // Creating the top row (upper horizontal border)
        StringBuilder topBorder = new StringBuilder();
        for (int j = 0; j < width; j++) {
            topBorder.append("┼────");
        }
        topBorder.append("┼");
        outputMaze.add(topBorder.toString());

        for (int i = 0; i < height; i++) {
            StringBuilder verticalWalls = new StringBuilder();

            // Vertical walls and passages between the cells
            for (int j = 0; j < width; j++) {
                if (j == 0 && i == 0) {
                    verticalWalls.append(" "); // Making the enter for the maze
                } else if (j == 0) {
                    verticalWalls.append("│"); // Left border
                }
                if (hasEdge(i, j, i, j + 1, mazeEdges, height, width) && !(j == width - 1 && i == height - 1)) {
                    String str = getCellSymbol(i, j, i, j + 1, mazeEdges) + "|";
                    verticalWalls.append(str); // Vertical wall
                } else {
                    String str = getCellSymbol(i, j, i, j + 1, mazeEdges) + " ";
                    verticalWalls.append(str); // Passage
                }
            }
            outputMaze.add(verticalWalls.toString()); // Add the string at outputMaze

            // Horizontal walls between the cells
            StringBuilder horizontalWalls = new StringBuilder();
            for (int j = 0; j < width; j++) {
                horizontalWalls.append("┼"); // The node between the cells
                if (hasEdge(i, j, i + 1, j, mazeEdges, height, width)) {
                    horizontalWalls.append("────"); // Horizontal wall
                } else {
                    horizontalWalls.append(DEFAULT_SYMBOL); // Passage
                }
            }
            horizontalWalls.append("┼"); // Right border
            outputMaze.add(horizontalWalls.toString()); // Add the String at outputMaze
        }
        return outputMaze;
    }

    private String getCellSymbol(int row1, int col1, int row2, int col2, List<Edge> mazeEdges) {
        int weight;
        String symbol = DEFAULT_SYMBOL;
        for (Edge edge: mazeEdges) {
            if (edge.cell1().row() == row1 && edge.cell1().col() == col1
                && edge.cell2().row() == row2 && edge.cell2().col() == col2
                || edge.cell1().row() == row2 && edge.cell1().col() == col2
                && edge.cell2().row() == row2 && edge.cell2().col() == col1) {
                weight = edge.weight();
                switch (weight) {
                    case LOW_WEIGHT:
                        symbol = LOW_WEIGHT_SYMBOL;
                        break;
                    case HIGH_WEIGHT:
                        symbol = HIGH_WEIGHT_SYMBOL;
                        break;
                    default:
                        return symbol;
                }
            }
        }
        return symbol;
    }

    private boolean hasEdge(int row1, int col1, int row2, int col2, List<Edge> mazeEdges, int height, int width) {
        if (row2 >= height || col2 >= width) {
            return true; // Borders of maze always with edges
        }
        return checkEdgeInMazeEdges(mazeEdges, row1, col1, row2, col2);
    }

    // Verify of existence edge between two cells
    private boolean checkEdgeInMazeEdges(List<Edge> mazeEdges, int row1, int col1, int row2, int col2) {
        for (Edge edge : mazeEdges) {
            if (edge.cell1().col() == col1 && edge.cell1().row() == row1
                && edge.cell2().col() == col2 && edge.cell2().row() == row2) {
                return false; // Edge is missing, so there is passage
            }
        }
        return true;
    }

    public List<Edge> edges() {
        return new ArrayList<>(edges);
    }

    public List<Edge> mazeEdges() {
        return new ArrayList<>(mazeEdges);
    }

    public void printMaze(List<String> outputMaze) {
        for (String mazeElement : outputMaze) {
            OUT.println(mazeElement);
        }
        OUT.println();
    }
}
