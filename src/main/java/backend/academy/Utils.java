package backend.academy;

import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public final static PrintStream OUT = System.out;
    public final static String HELLO_MESSAGE = """
                Welcome to the maze generation!
                The friendly maze (your best friend): \
                will be generated a maze which uses the Prim's algorithm. (one path, no obstacles, path always exists)
                The treacherous maze (it is going to stab you in the back): \
                will be generated a maze which uses the Kruskal's algorithm. \
                (2 and more path, have obstacles, path not always exists).
                Make your choice! (enter 'easy' for generate friendly or 'hard' for generate treacherous)
                """;

    public final static String GOODBYE_MESSAGE = "Goodbye! See you soon!";

    public final static String IF_EASY_MESSAGE = """
       Try find the path in the maze. You can't lose)
       Enter the beginning and end of a possible path.
       """;

    public final static String IF_HARD_MESSAGE = """
        Try find the path in the maze. You have 3 lives.
        If '$' takes 1 live and '~' takes away 1 live. If you don't have any lives left, you're going to lose!
        Enter the beginning and end of a possible path.
        """;

    public final static int COORDINATE_QUANTITY = 4;
    public final static String EASY = "easy";
    public final static String HARD = "hard";

    public static <T> T getRandomValue(List<T> list) {
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}
