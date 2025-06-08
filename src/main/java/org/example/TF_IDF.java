package org.example;

import java.util.Map;
import java.util.TreeMap;

public class TF_IDF {

    public static Map<String, Map<Integer, Number>> calcTF_IDF(
            Map<String, Map<Integer, Number>> weightTF,
            Map<String, Number> IDF) {

        Map<String, Map<Integer, Number>> TF_IDF = new TreeMap<>();

        for (Map.Entry<String, Number> entry : IDF.entrySet()) {
            String term = entry.getKey();
            double idfValue = entry.getValue().doubleValue();

            Map<Integer, Number> termWeights = new TreeMap<>();
            TF_IDF.put(term, termWeights);

            for (Map.Entry<Integer, Number> tfEntry : weightTF.getOrDefault(term, new TreeMap<>()).entrySet()) {
                int docID = tfEntry.getKey();
                double tfValue = tfEntry.getValue().doubleValue();

                if(tfValue > 0){
                    double roundedValue = Math.round((tfValue * idfValue) * 100000.0) / 100000.0;
                    if (roundedValue == Math.floor(roundedValue)) {
                        termWeights.put(docID, (int) roundedValue);
                    } else {
                        termWeights.put(docID, roundedValue);
                    }
                }else{
                    termWeights.put(docID, 0);
                }
            }
        }

        return TF_IDF;
    }


    public static Map<Integer, Number> calculateDocLengths(Map<String, Map<Integer, Number>> TF_IDF) {
        Map<Integer, Number> docLengths = new TreeMap<>();

        for (Map<Integer, Number> termWeights : TF_IDF.values()) {
            for (Map.Entry<Integer, Number> entry : termWeights.entrySet()) {
                int docID = entry.getKey();
                double value = entry.getValue().doubleValue();
                docLengths.put(docID, docLengths.getOrDefault(docID, 0.0).doubleValue() + value * value);
            }
        }

        docLengths.replaceAll((docID, length) -> {
            double sqrtValue = Math.sqrt(length.doubleValue());
            return Math.round(sqrtValue * 100000.0) / 100000.0;
        });

        return docLengths;
    }


    public static Map<String, Map<Integer, Number>> normalizeTFIDF(
            Map<String, Map<Integer, Number>> TF_IDF,
            Map<Integer, Number> docLengths) {

        Map<String, Map<Integer, Number>> normalizedTFIDF = new TreeMap<>();

        for (Map.Entry<String, Map<Integer, Number>> entry : TF_IDF.entrySet()) {
            String term = entry.getKey();
            Map<Integer, Number> termTFIDF = entry.getValue();
            Map<Integer, Number> termNormalized = new TreeMap<>();

            for (Map.Entry<Integer, Number> tfidfEntry : termTFIDF.entrySet()) {
                int docID = tfidfEntry.getKey();
                double tfidfValue = tfidfEntry.getValue().doubleValue();
                double docLength = docLengths.getOrDefault(docID, 1.0).doubleValue(); // Avoid division by zero

                if(tfidfValue > 0){
                    double roundedValue = Math.round(tfidfValue / docLength * 100000.0) / 100000.0;
                    if (roundedValue == Math.floor(roundedValue)) {
                        termNormalized.put(docID, (int) roundedValue);
                    } else {
                        termNormalized.put(docID, roundedValue);
                    }
                }else{
                    termNormalized.put(docID, 0);
                }
            }

            normalizedTFIDF.put(term, termNormalized);
        }

        return normalizedTFIDF;
    }
}
