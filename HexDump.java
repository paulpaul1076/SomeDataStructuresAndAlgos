/**
 * Created by paulp on 02.01.2016.
 */
public class HexDump {
    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        assert width > 0 && width < 16;
        int count = 0;
        label : while (true) {
            for (int i = 0; i < width; i++) {
                if(BinaryStdIn.isEmpty()) break label;
                if(i != 0) {
                    System.out.print(" ");
                }
                System.out.printf("%x", BinaryStdIn.readByte());
                count++;
            }
            System.out.println();
        }
        System.out.println(8 * count + " bits read");
    }
}
