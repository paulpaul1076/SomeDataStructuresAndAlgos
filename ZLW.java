import java.util.HashMap;

/**
 * Created by Paul on 1/4/2016.
 */
public class ZLW {
    private static final int RADIX = 256;
    private static final int MAX_CODE_NUM = 4096;
    private static final int CODE_BIT_LENGTH = 12;
    private static final int EOF = RADIX;

    public static void compress() {
        PrefixTree<Integer> stringToCode = new PrefixTree<>();
        for (int i = 0; i < RADIX; ++i)
            stringToCode.put("" + (char) i, i);
        int code = RADIX + 1;
        String input = BinaryStdIn.readString();
        int count = 0;
        while (!input.equals("")) {
            String longestPrefix = stringToCode.longestPrefixOf(input);
            int curCode = stringToCode.get(longestPrefix);
            BinaryStdOut.write(curCode, CODE_BIT_LENGTH);
            int length = longestPrefix.length();
            System.err.println("longest prefix: " + longestPrefix);
            if (code < MAX_CODE_NUM && length < input.length()) {
                String newKey = longestPrefix + input.charAt(length);
                stringToCode.put(newKey, code++);
                System.err.println("new key: " + newKey);
                System.err.println("code: " + (code - 1));
            }
            input = input.substring(length);
            count++;
        }
        System.err.println("count = " + count);
        BinaryStdOut.write(RADIX, CODE_BIT_LENGTH);
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void decompress() {
        HashMap<Integer, String> codeToString = new HashMap<>();
        for (int i = 0; i < RADIX; ++i)
            codeToString.put(i, "" + (char) i);
        int code = RADIX + 1;
        int curCode = BinaryStdIn.readInt(12);
        String oldString = codeToString.get(curCode);
        if (curCode != EOF) {
            BinaryStdOut.write(oldString);
        }
        while (true) {
            System.err.println("code: " + curCode);
            if (curCode == EOF) break;
            curCode = BinaryStdIn.readInt(12);
            if (curCode == code) {
                System.err.println("oldString: " + oldString);
                codeToString.put(code++, oldString + oldString.charAt(0));
                BinaryStdOut.write(codeToString.get(curCode));
                oldString = codeToString.get(curCode);
                System.err.println("YEP: " + curCode);
            } else if (curCode != EOF) {
                String newString = codeToString.get(curCode);
                BinaryStdOut.write(newString);
                codeToString.put(code++, oldString + newString.charAt(0));
                oldString = newString;
            } else break;
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
                break;
            default:
                System.err.println("Wrong option");
        }
    }
}
