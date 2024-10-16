package backend.academy;

import java.util.List;

/**
 * Interface representing a pathfinding algorithm for mazes.
 * This interface provides methods for generating a path through a maze
 * and for printing the maze with the path to the console.
 */
public interface MazePathFinder {
    void findPath(); // for generate maze

    void printPath(List<String> outputMaze); // for print maze in the console
}
