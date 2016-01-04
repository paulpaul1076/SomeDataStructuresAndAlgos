import java.io.BufferedInputStream;
import java.io.IOException;

public class BinaryStdIn {

    private static BufferedInputStream in = new BufferedInputStream(System.in);

    private static int buffer;
    private static int n;

    static{
        fillBuffer();
    }

    private static void fillBuffer() {
        if (n != 0) return;
        try {
            buffer = in.read();
            n = 8;
        } catch (IOException e) {
            buffer = -1;
            n = 0;
        }
    }

    public static void close() {
        buffer = -1;
        n = 0;
    }

    public static double readDouble() {
        long longBits = readBits(64);
        double result = Double.longBitsToDouble(longBits);
        return result;
    }

    public static float readFloat() {
        int intBits = (int) (readBits(32) & 0xff);
        float result = Float.intBitsToFloat(intBits);
        return result;
    }

    public static long readLong() {
        return readBits(64);
    }

    public static int readInt() {
        int result = (int) (readBits(32) & 0xffffffff);
        return result;
    }

    public static char readChar() {
        char result = (char) (readBits(8) & 0xff);
        return result;
    }

    public static short readShort() {
        short result = (short) (readBits(16) & 0xffff);
        return result;
    }

    public static byte readByte() {
        byte result = (byte) (readBits(8) & 0xff);
        return result;
    }

    private static long readBits(int howMany) {
        assert howMany > 0 && howMany < 65;
        long result = 0;
        for (int i = 0; i < howMany && !isEmpty(); i++) {
            result <<= 1;
            result |= readBoolean() ? 1 : 0;
        }
        return result;
    }

    public static boolean isEmpty() {
        return buffer == -1;
    }

    public static boolean readBoolean() {
        --n;
        boolean result = ((buffer >>> n) & 1) == 1;
        fillBuffer();
        return result;
    }

    public static String readString() {
        StringBuilder result = new StringBuilder();
        while(!isEmpty()) {
            char ch = readChar();
            result.append(ch);
            System.err.println(ch);
        }
        return result.toString();
    }
}