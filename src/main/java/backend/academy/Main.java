package backend.academy;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        final int SIZE = 10;
        KruskalMaze maze = new KruskalMaze(SIZE, SIZE);
        maze.printMaze();
    }
}
