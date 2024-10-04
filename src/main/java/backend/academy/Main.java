package backend.academy;

import java.io.PrintStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        final int SIZE = 10;
        KruskalMaze maze = new KruskalMaze(SIZE, SIZE);
        maze.generateMaze();
        maze.assembleMaze();
        try (PrintStream out = new PrintStream(System.out)) {
            for (String str : maze.outputMaze()) {
                out.println(str);
            }
        }
    }
}
