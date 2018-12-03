package bridge;

import com.google.gson.*;

import java.io.*;

public class GenJ {
    private JsonObject json;
    public GenJ(String fileName) {
        File file = new File(fileName+".txt");
        json = new JsonObject();
        int Count = 0;
        JsonObject edge = new JsonObject();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str;
            String []strArr;
            while((str = br.readLine()) != null) {
                strArr = str.split(" ");
                if (!edge.has(strArr[0])) {
                    edge.add(strArr[0], new JsonArray());
                    Count ++;
                }
                edge.getAsJsonArray(strArr[0]).add(Integer.parseInt(strArr[1]));
            }
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        json.addProperty("Number", Count);
        json.add("Edge", edge);
        File jsonFile = new File(fileName + ".json");
        try {
            (new FileWriter(jsonFile)).write((new Gson()).toJson(json));
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
