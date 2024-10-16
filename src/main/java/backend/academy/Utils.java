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
                Welcome to the maze generation! If you want to generate a maze enter easy/hard!
                easy: will be generated a maze with size 5*5 and alone path. (will be use Prim algorithm)
                hard: will be generated a maze with size 10*10 and 2 and more path and will be create a special cells.\
                 (will be use Kruskal algorithm)""";

    public final static String GOODBYE_MESSAGE = "Goodbye! See you soon!";

    public final static String IF_EASY_MESSAGE = """
        Try find the path in the maze. If you want to see this path\
         - enter "Yes" or "No" if dont want.""";

    public final static String IF_HARD_MESSAGE = """
        Try find the best path in the maze
        if '$' takes 1 point and '~' takes away 1 point. If you want to see this path\
         - enter "Yes" or "No" if dont want.""";


    public static <T> T getRandomValue(List<T> list) {
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}
