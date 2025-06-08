package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class documentFrequency {

    public static Map<String, Integer> calculateDF(Map<String, Map<Integer, List<Integer>>> positionalIndex) {
        Map<String, Integer> documentFrequencies = new TreeMap<>();

        for (Map.Entry<String, Map<Integer, List<Integer>>> entry : positionalIndex.entrySet()) {
            String term = entry.getKey();
            Map<Integer, List<Integer>> docMap = entry.getValue();

            documentFrequencies.put(term, docMap.size());
        }

        return documentFrequencies;
    }

    public static Map<String, Number> calculateIDF(Map<String, Integer> documentFrequencies, int totalDocuments) {
        Map<String, Number> idfScores = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : documentFrequencies.entrySet()) {
            String term = entry.getKey();
            int df = entry.getValue();

            double idf = Math.log10((double) totalDocuments / df);
            double roundedValue = Math.round(idf *100000.0)/100000.0;

            if(roundedValue == Math.floor(roundedValue)){
                idfScores.put(term, (int)roundedValue);
            }else{
                idfScores.put(term , roundedValue);
            }

        }

        return idfScores;
    }
}


