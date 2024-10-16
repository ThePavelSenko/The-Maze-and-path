package backend.academy;

import java.util.List;

public interface MazePathFinder {
    void findPath(); // for generate maze

    void printPath(List<String> outputMaze); // for print maze in the console
}
