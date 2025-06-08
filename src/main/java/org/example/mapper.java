package org.example;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
public class mapper extends Mapper<Object, Text, Text, Text> {
    private Text word = new Text();
    private Text positionInfo = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        if (!line.contains(" ")) return;
        String docID = line.substring(0, line.indexOf(" "));
        String content = line.substring(line.indexOf(" ") + 1);

        String[] words = content.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            word.set(words[i].toLowerCase());
            positionInfo.set(docID + ":" + (i + 1));
            context.write(word, positionInfo);
        }
    }
}
