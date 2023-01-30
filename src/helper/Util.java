package helper;

import java.util.Random;

public final class Util {
    private static final Random random = new Random();

    public static int nextInt() {
        return random.nextInt();
    }
}
