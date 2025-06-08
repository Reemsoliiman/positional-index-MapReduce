package org.example;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TermFrequency {

    public static Map<String, Map<Integer, Integer>> calculateTF(Map<String, Map<Integer, List<Integer>>> positionalIndex , int totalDocuments) {
        Map<String, Map<Integer, Integer>> termsTF = new TreeMap<>();

        for (String term : positionalIndex.keySet()) {
            Map<Integer, List<Integer>> docs = positionalIndex.get(term);
            Map<Integer, Integer> TF = new TreeMap<>();

            for (int i = 1; i <= totalDocuments; i++) {
                if (docs.containsKey(i)) {
                    TF.put(i, docs.get(i).size());
                } else {
                    TF.put(i, 0);
                }
            }

            termsTF.put(term, TF);
        }

        return termsTF;
    }

    public static Map<String, Map<Integer, Number>> calculateWeightTF(Map<String, Map<Integer, Integer>> TF , int totalDocuments) {
        Map<String, Map<Integer, Number>> weightTF = new TreeMap<>();

        for (String term : TF.keySet()) {
            Map<Integer, Integer> docs = TF.get(term);

            Map<Integer, Number> weight = new TreeMap<>();

            for (int i = 1; i <= totalDocuments; i++) {
                if (docs.get(i) != null && docs.get(i) > 0) {
                    double weightTFValue = 1 + Math.log10(docs.get(i));
                    double roundedValue = Math.round(weightTFValue * 100000.0) / 100000.0;

                    if (roundedValue == Math.floor(roundedValue)) {
                        weight.put(i, (int) roundedValue);
                    } else {
                        weight.put(i, roundedValue);
                    }
                } else {
                    weight.put(i, 0);
                }
            }

            weightTF.put(term, weight);
        }

        return weightTF;
    }

}

