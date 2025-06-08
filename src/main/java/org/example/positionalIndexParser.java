package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class positionalIndexParser {

    public static Map<String, Map<Integer, List<Integer>>> parseFile(String filePath) throws IOException {
        Map<String, Map<Integer, List<Integer>>> positionalIndex = new TreeMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] newLine = line.split("<");
            String term = newLine[0].trim();
            String docs = newLine[1].replace(">", "").trim();

            Map<Integer, List<Integer>> termDocs = new TreeMap<>();
            String[] docEntries = docs.split(";");

            for (String docEntry : docEntries) {
                String[] docParts = docEntry.split(":");
                Integer docID = Integer.parseInt(docParts[0].trim());

                ArrayList<Integer> positions = new ArrayList<>();

                String[] termPositions = docParts[1].split(",");
                for(String termPosition : termPositions){
                    positions.add(Integer.parseInt(termPosition.trim()));
                }

                termDocs.put(docID, positions);
            }

            positionalIndex.put(term, termDocs);
        }

        reader.close();
        return positionalIndex;
    }
}
