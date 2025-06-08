package org.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class reducer extends Reducer<Text, Text, Text, Text> {


    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        Map<String, StringBuilder> docPositions = new HashMap<>();

        for (Text value : values) {
            String[] parts = value.toString().split(":");
            String docID = parts[0];
            String position = parts[1];

            if (!docPositions.containsKey(docID)) {
                docPositions.put(docID, new StringBuilder());
            } else {
                docPositions.get(docID).append(",");
            }
            docPositions.get(docID).append(position);
        }


        StringBuilder result = new StringBuilder("<");
        for (Map.Entry<String, StringBuilder> entry : docPositions.entrySet()) {
            String docID = entry.getKey();
            String positions = entry.getValue().toString();

            result.append(docID).append(":").append(positions).append("; ");
        }


        if (result.length() > 1) {
            result.setLength(result.length() - 2);
        }
        result.append(">");

        context.write(key, new Text(result.toString()));
    }
}
