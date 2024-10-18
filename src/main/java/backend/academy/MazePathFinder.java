package backend.academy;

import java.util.List;

/**
 * Interface representing a pathfinding algorithm for mazes.
 * This interface provides methods for generating a path through a maze
 * and for printing the maze with the path to the console.
 */
public interface MazePathFinder {
    void findPath(int row1, int col1, int row2, int col2); // for generate maze

    void printPath(List<String> outputMaze); // for print maze in the console
}
