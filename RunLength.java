
public class RunLength {
    public static void compress() {
        int count = 0;
        boolean oldBit = false;
        while (!BinaryStdIn.isEmpty()) {
            System.err.println("ITER");
            boolean bit = BinaryStdIn.readBoolean();
            if (bit != oldBit) {
                BinaryStdOut.write((byte) count);
                oldBit = !oldBit;
                count = 0;
            }
            if (count == 255) {
                BinaryStdOut.write((byte) count);
                BinaryStdOut.write((byte) 0);
                count = 0;
            }
            count++;
        }
        BinaryStdOut.write((byte) count);
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void decompress() {
        boolean bit = false;
        while(!BinaryStdIn.isEmpty()) {
            int count = BinaryStdIn.readByte() & 0xff;
            for (int i = 0; i < count; i++) {
                BinaryStdOut.write(bit);
            }
            bit = !bit;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        String option = args[0];
        switch (option) {
            case "+":
                compress();
                break;
            case "-":
                decompress();
        }
    }
}