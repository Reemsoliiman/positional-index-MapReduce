package org.example;

import java.util.*;

public class QueryOutputs {

    private static List<String> removeKeyWords (List<String> query){
        List<String> queryResult = new ArrayList<>();
        for(String term : query){
            if(!term.equals("AND") && !term.equals("OR") && !term.equals("AND NOT") && !term.equals("OR NOT")){
                if(term.trim().contains(" ")){
                    queryResult.addAll(List.of(term.split(" ")));
                }else{
                    queryResult.add(term);
                }
            }
        }
        return queryResult;
    }

    public static Map<String, Integer> computeQueryTF (List<String> query){
        Map<String, Integer> queryTF = new TreeMap<>();
        List<String> resultQuery = removeKeyWords(query);

        for(String term : resultQuery){
            int count = 0;

            for(int i = 0 ; i < resultQuery.size() ; i++){
                if(resultQuery.get(i).equals(term)){
                    count++;
                }
            }

            queryTF.put(term , count);
        }
        return queryTF;
    }

    public static Map<String, Number> computeQueryWeightTF (Map<String, Integer> queryTF){
        Map<String, Number> queryWeightTF = new TreeMap<>();

        for(String term : queryTF.keySet()){
            double weightTFValue = 1 + Math.log10(queryTF.get(term));
            double roundedValue = Math.round(weightTFValue * 100000.0) / 100000.0;

            if (roundedValue == Math.floor(roundedValue)) {
                queryWeightTF.put(term, (int) roundedValue);
            } else {
                queryWeightTF.put(term, roundedValue);
            }
        }
        return queryWeightTF;
    }

    public static Map<String, Number> retrieveQueryIDF(List<String> query, Map<String, Number> idfMap) {
        Map<String, Number> queryIDF = new TreeMap<>();
        List<String> resultQuery = removeKeyWords(query);

        for (String term : resultQuery) {
            if (idfMap.containsKey(term)) {
                queryIDF.put(term, idfMap.get(term));
            } else {
                queryIDF.put(term, 0);
            }
        }
        return queryIDF;
    }

    public static Map<String, Number> computeQueryTF_IDF(Map<String, Number> queryWeightTF, Map<String, Number> queryIDF) {
        Map<String, Number> queryTF_IDF = new TreeMap<>();

        for (String term : queryWeightTF.keySet()) {
            double tf = queryWeightTF.get(term).doubleValue();
            double idf = queryIDF.getOrDefault(term, 0).doubleValue();
            double tf_idf = tf * idf;
            double roundedValue = Math.round(tf_idf * 100000.0) / 100000.0;

            if (roundedValue == Math.floor(roundedValue)) {
                queryTF_IDF.put(term, (int) roundedValue);
            } else {
                queryTF_IDF.put(term, roundedValue);
            }
        }
        return queryTF_IDF;
    }

    public static double computeQueryLength(Map<String, Number> queryTF_IDF) {
        double sum = 0.0;
        for (Map.Entry<String, Number> entry : queryTF_IDF.entrySet()) {
            double weight = entry.getValue().doubleValue();
            sum += weight * weight;
        }
        double length = Math.sqrt(sum);
        return Math.round(length * 100000.0) / 100000.0;
    }

    public static Map<String, Number> computeNormalizedQueryTF_IDF(Map<String, Number> queryTF_IDF) {
        Map<String, Number> normalizedQueryTF_IDF = new TreeMap<>();
        double queryLength = computeQueryLength(queryTF_IDF);

        for (String term : queryTF_IDF.keySet()) {
            double tf_idf = queryTF_IDF.get(term).doubleValue();
            double normalizedValue = queryLength > 0 ? tf_idf / queryLength : 0;
            normalizedValue = Math.round(normalizedValue * 100000.0) / 100000.0;
            normalizedQueryTF_IDF.put(term, normalizedValue);
        }

        return normalizedQueryTF_IDF;
    }

    public static Map<String, Map<Integer, Number>> computeNormalizedDocTF_IDF(Map<String, Map<Integer, Number>> TF_IdfMap, List<String> query, List<Integer> matchedDocs) {
        Map<String, Map<Integer, Number>> normalizedDocTF_IDF = new TreeMap<>();
        List<String> finalQuery = removeKeyWords(query);

        for (String term : finalQuery) {
            Map<Integer, Number> docTF_IDF = new TreeMap<>();

            if (TF_IdfMap.containsKey(term)) {
                for (Integer docId : matchedDocs) {
                    if (TF_IdfMap.get(term).containsKey(docId)) {
                        double tfidf = TF_IdfMap.get(term).get(docId).doubleValue();

                        docTF_IDF.put(docId, tfidf);
                    }
                }

                normalizedDocTF_IDF.put(term, docTF_IDF);
            }
        }
        return normalizedDocTF_IDF;
    }

    public static Map<String, Map<Integer, Number>> productQueryMatchedDocs (Map<String, Map<Integer, Number>> normalizedDocTF_IDF , Map<String, Number> normalizedQueryTF_IDF){
        Map<String, Map<Integer, Number>> product = new TreeMap<>();

        for(String term : normalizedDocTF_IDF.keySet()){
            Map<Integer, Number> termDocs = new HashMap<>();

            for(Integer docID : normalizedDocTF_IDF.get(term).keySet()){
                double productValue = normalizedDocTF_IDF.get(term).get(docID).doubleValue() * normalizedQueryTF_IDF.get(term).doubleValue();
                double roundedValue = Math.round(productValue * 100000.0) / 100000.0;

                if (roundedValue == Math.floor(roundedValue)) {
                    termDocs.put(docID, (int) roundedValue);
                } else {
                    termDocs.put(docID, roundedValue);
                }
            }

            product.put(term ,termDocs);
        }

        return product;
    }

    public static Map<Integer, Double> computeSimilarity(Map<String, Map<Integer, Number>> productQueryMatchedDocs) {
        Map<Integer, Double> similarityScores = new TreeMap<>();

        for (Map<Integer, Number> docValues : productQueryMatchedDocs.values()) {
            for (Map.Entry<Integer, Number> entry : docValues.entrySet()) {
                int docId = entry.getKey();
                double value = entry.getValue().doubleValue();

                similarityScores.put(docId, similarityScores.getOrDefault(docId, 0.0) + value);
            }
        }
        similarityScores.replaceAll((docId, score) -> Math.round(score * 10000.0) / 10000.0);

        return similarityScores;
    }

    public static List<Map.Entry<Integer, Double>> rankDocuments(Map<Integer, Double> similarityScores) {
        List<Map.Entry<Integer, Double>> rankedDocuments = new ArrayList<>(similarityScores.entrySet());
        rankedDocuments.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));
        return rankedDocuments;
    }

}

