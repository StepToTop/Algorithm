package bridge;

import java.io.*;
import java.util.Random;

public class GenJ {
    public GenJ(String fileName, int number, int edgeNumber) {
        File file = new File(fileName+".txt");
        Random rd = new Random();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            int i = 0;
            bw.write(number+"");
            while (i < edgeNumber) {
                bw.newLine();
                bw.write(rd.nextInt(number) + " "  + rd.nextInt(number));
                i++;
            }
            bw.flush();
            bw.close();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
