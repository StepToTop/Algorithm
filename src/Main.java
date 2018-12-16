import bridge.*;

import java.io.*;


public class Main {
    private static StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
    private static int getInt() {
        try {
            in.nextToken();
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return (int)in.nval;
    }
    public static void main(String[] args) {
        int A = getInt();
        int B = getInt();
        System.out.println(A^B + (A&B)<<1);
    }

}