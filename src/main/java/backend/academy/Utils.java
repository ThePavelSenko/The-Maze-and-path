package backend.academy;

import java.security.SecureRandom;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static <T> T getRandomValue(List<T> list) {
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}
