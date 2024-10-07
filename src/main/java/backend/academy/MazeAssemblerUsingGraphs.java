package backend.academy;

import java.util.List;

/** This interface is intended for classes describing algorithms that use graphs. **/
public interface MazeAssemblerUsingGraphs {
    /** Method for assemble of the maze.
     * The essence of the method - it takes a set of cells of the size height*width (such as in a chessboard)
     * and makes passes in them, which are contained in mazeEdges, after which it is stuffed into the outputMaze **/
    default void assembleMaze(List<String> outputMaze, List<Edge> mazeEdges, int height, int width) {
        // Creating the top row (upper horizontal border)
        StringBuilder topBorder = new StringBuilder();
        for (int j = 0; j < width; j++) {
            topBorder.append("N++++");
        }
        topBorder.append("N");
        outputMaze.add(topBorder.toString());

        for (int i = 0; i < height; i++) {
            StringBuilder verticalWalls = new StringBuilder();

            // Vertical walls and passages between the cells
            for (int j = 0; j < width; j++) {
                if (j == 0 && i == 0) {
                    verticalWalls.append(" "); // Making the enter for the maze
                } else if (j == 0) {
                    verticalWalls.append("|"); // Left border
                }
                if (hasEdge(i, j, i, j + 1, mazeEdges, height, width) && !(j == width - 1 && i == height - 1)) {
                    verticalWalls.append("    |"); // Vertical wall
                } else {
                    verticalWalls.append("     "); // Passage
                }
            }
            outputMaze.add(verticalWalls.toString()); // Add the string at outputMaze

            // Horizontal walls between the cells
            StringBuilder horizontalWalls = new StringBuilder();
            for (int j = 0; j < width; j++) {
                horizontalWalls.append("N"); // The node between the cells
                if (hasEdge(i, j, i + 1, j, mazeEdges, height, width)) {
                    horizontalWalls.append("++++"); // Horizontal wall
                } else {
                    horizontalWalls.append("    "); // Passage
                }
            }
            horizontalWalls.append("N"); // Right border
            outputMaze.add(horizontalWalls.toString()); // Add the String at outputMaze
        }
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
}
