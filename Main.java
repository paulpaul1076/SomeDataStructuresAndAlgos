import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by paulp on 02.01.2016.
 */
public class Main {
    static void compress() throws Exception{
        System.setIn(new FileInputStream("C:\\Users\\paulp\\Desktop\\in.txt"));
        System.setOut(new PrintStream(new FileOutputStream("C:\\Users\\paulp\\Desktop\\out.bin")));
        RunLength.main(new String[]{"+"});
    }
    static void decompress()throws Exception{
        System.setIn(new FileInputStream("C:\\Users\\paulp\\Desktop\\out.bin"));
        System.setOut(new PrintStream(new FileOutputStream("C:\\Users\\paulp\\Desktop\\out.txt")));
        RunLength.main(new String[]{"-"});
    }
    public static void main(String[] args) throws Exception {
        decompress();
    }
}
