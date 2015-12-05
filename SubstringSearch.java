import java.util.Arrays;

public class SubstringSearch {
    public static void main(String[] args) {
        RabinKarpLasVegas.client();
    }
}

interface SearchAPI {
    int search(String text);
}

class RabinKarpLasVegas implements SearchAPI {
    private long Q;
    private long patHash;
    private long RM;
    private int patternSize;
    private String pattern;

    private long longPrime() {
        return 11997;
    }

    public RabinKarpLasVegas(String pattern) {
        Q = longPrime();
        RM = 1;
        for (int i = 0; i < pattern.length() - 1; i++) {
            RM = (RM * 256) % Q;
        }
        for (int i = 0; i < pattern.length(); i++) {
            patHash = (patHash * 256 + pattern.charAt(i)) % Q;
        }
        this.pattern = pattern;
        patternSize = pattern.length();
    }

    public int search(String text) {
        long textHash = 0;
        for (int i = 0; i < patternSize; i++) {
            textHash = (textHash * 256 + text.charAt(i)) % Q;
        }
        if (textHash == patHash) {
            int j = 0;
            for (; j < patternSize; ++j) {
                if (text.charAt(j) != pattern.charAt(j)) break;
            }
            if (j == patternSize)
                return 0;
        }
        for (int i = 0; i < text.length() - patternSize; i++) {
            textHash = ((((((textHash - ((RM * text.charAt(i)) % Q)) + Q) % Q) * 256) % Q) + text.charAt(i + patternSize)) % Q;
            if (textHash == patHash) {
                int j = 0;
                for (; j < patternSize; ++j) {
                    if (text.charAt(i + j + 1) != pattern.charAt(j)) break;
                }
                if (j == patternSize)
                    return i + 1;
            }
        }
        return text.length();
    }

    public static void client() {
        String text = "she sells seashells by the seashore, the shells, she sells, are surely, seashells";
        String pattern = "surely";
        SearchAPI search = new RabinKarpLasVegas(pattern);
        System.out.println(search.search(text));
    }
}

class BruteForce implements SearchAPI {
    private String pattern;

    public BruteForce(String pattern) {
        this.pattern = pattern;
    }

    public int search(String text) {
        int j = 0;
        for (int i = 0; i <= text.length() - pattern.length(); ++i) {
            while (j < pattern.length() && text.charAt(i + j) == pattern.charAt(j)) ++j;
            if (j == pattern.length()) return i;
            else j = 0;
        }
        return text.length();
    }

    public static void client() {
        String text = "she sells seashells by the seashore, the shells, she sells, are surely, seashells";
        String pattern = "sells";
        SearchAPI search = new BruteForce(pattern);
        System.out.println(search.search(text));
    }
}

class KMP implements SearchAPI {
    private int dfa[][];
    private int patternSize;

    public KMP(String pattern) {
        patternSize = pattern.length();
        dfa = new int[256][patternSize];
        for (int j = 0, i = 0; i < patternSize; ++i) {
            for (int c = 0; c < 256; ++c) {
                dfa[c][i] = dfa[c][j];
            }
            char ch = pattern.charAt(i);
            j = dfa[ch][j];
            dfa[ch][i] = i + 1;
        }
    }

    public int search(String text) {
        for (int j = 0, i = 0; i < text.length(); ++i) {
            j = dfa[text.charAt(i)][j];
            if (j == patternSize) return i - j + 1;
        }
        return text.length();
    }


    public static void client() {
        String text = "she sells seashells by the seashore, the shells, she sells, are surely, seashells";
        String pattern = "sells";
        SearchAPI search = new KMP(pattern);
        System.out.println(search.search(text));
    }
}

class BoyerMoore implements SearchAPI {
    private int[] rightPos;
    private int patternSize;
    private String pattern;

    public BoyerMoore(String pattern) {
        patternSize = pattern.length();
        this.pattern = pattern;
        rightPos = new int[256];
        Arrays.fill(rightPos, -1);
        for (int i = 0; i < patternSize; ++i) {
            rightPos[pattern.charAt(i)] = i;
        }
    }

    public int search(String text) {
        int textPtr = 0;
        while (textPtr <= text.length() - patternSize) {
            int i;
            for (i = patternSize - 1; i >= 0; --i) {
                if (text.charAt(textPtr + i) != pattern.charAt(i)) break;
            }
            if (i == -1) return textPtr;
            else if (i == 0) textPtr++;
            else if (rightPos[text.charAt(textPtr + i)] != -1) textPtr += i - rightPos[text.charAt(textPtr + i)];
            else textPtr += i;
        }
        return text.length();
    }

    public static void client() {
        String text = "she sells seashells by the seashore, the shells, she sells, are surely, seashells";
        String pattern = "she";
        SearchAPI search = new BoyerMoore(pattern);
        System.out.println(search.search(text));
    }
}

class RabinKarpMonteCarlo implements SearchAPI {
    private long RM;
    private long Q;
    private long patternHash;
    private int patternSize;

    private long longPrime() {
        return 11997;
    }

    public RabinKarpMonteCarlo(String pattern) {
        patternSize = pattern.length();
        Q = longPrime();
        RM = 1;
        for (int i = 0; i < patternSize - 1; ++i) {
            RM = (RM * 256) % Q;
        }
        //System.out.println("RM = " + RM);
        patternHash = 0;
        for (int i = 0; i < pattern.length(); i++) {
            patternHash = (patternHash * 256 + pattern.charAt(i)) % Q;
        }
        //System.out.println("patternHash = " + patternHash);
        //System.out.println("t = " + ((int) 't'));
    }

    public int search(String text) {
        if (text.length() < patternSize) return text.length();
        long textHash = 0;
        for (int i = 0; i < patternSize; ++i) {
            textHash = (textHash * 256 + text.charAt(i)) % Q;
        }
        //System.out.println("0) textHash = " + textHash);
        if (textHash == patternHash) return 0;
        for (int i = 0; i < text.length() - patternSize; ++i) {
            textHash = (((((textHash - ((text.charAt(i) * RM) % Q)) + Q) % Q) * 256) % Q + text.charAt(i + patternSize)) % Q;
            if (textHash == patternHash) return i + 1;
            //System.out.println((i + 1) + ") TextHash = " + textHash + ", patternHash = " + patternHash);
        }
        return text.length();
    }

    public static void client() {
        String text = "she sells seashells by the seashore, the shells, she sells, are surely, seashells";
        String pattern = "surely";
        SearchAPI search = new RabinKarpMonteCarlo(pattern);
        System.out.println(search.search(text));
    }
}