/**
 * Created by paulp on 02.01.2016.
 */

/**
 * Reads bits from file and outputs them to the screen
 */
public class BinaryDump {
    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        assert width > 0 && width < 16;
        int count = 0;
        label:
        while (true) {
            for (int i = 0; i < width; ++i) {
                if (BinaryStdIn.isEmpty()) break label;
                if (i != 0) {
                    System.out.print(" ");
                }
                System.out.print(BinaryStdIn.readBoolean() ? 1 : 0);
                count++;
            }
            System.out.println();
        }
        System.out.println(count + " bits read");
    }
}
