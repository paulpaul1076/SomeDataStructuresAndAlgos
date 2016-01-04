import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Created by paulp on 02.01.2016.
 */
public class BinaryStdOut {
    private static BufferedOutputStream out = new BufferedOutputStream(System.out);
    private static int buffer;
    private static int n;

    private BinaryStdOut() {
    }

    public static void flush() {
        clearBuffer();
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        flush();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBit(boolean b) {
        if (n == 8) clearBuffer();
        buffer <<= 1;
        if (b) buffer |= 1;
        n++;
    }

    private static void clearBuffer() {
        if (n == 0) return;
        if (n > 0) buffer <<= (8 - n);
        try {
            out.write(buffer);
            n = 0;
            buffer = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(boolean b) {
        writeBit(b);
    }

    public static void write(char c) {
        for (int i = 0; i < 8; ++i) {
            writeBit(((c >>> (8 - i - 1)) & 1) == 1);
        }
    }

    public static void write(byte b) {
        for (int i = 0; i < 8; ++i) {
            writeBit(((b >>> (8 - i - 1)) & 1) == 1);
        }
    }

    public static void write(short sh) {
        for (int i = 0; i < 16; ++i) {
            writeBit(((sh >>> (16 - i - 1)) & 1) == 1);
        }
    }

    public static void write(int x) {
        for (int i = 0; i < 32; i++) {
            writeBit(((x >>> (32 - i - 1)) & 1) == 1);
        }
    }

    public static void write(long l) {
        for (int i = 0; i < 64; i++) {
            writeBit(((l >>> (64 - i - 1)) & 1) == 1);
        }
    }

    public static void write(float f) {
        int bits = Float.floatToRawIntBits(f);
        write(bits);
    }

    public static void write(double d) {
        long bits = Double.doubleToRawLongBits(d);
        write(bits);
    }
}
