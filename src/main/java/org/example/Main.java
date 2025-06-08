package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        String filePath = "mapReduceOutput.txt";
        Map<String, Map<Integer, List<Integer>>> outPut = positionalIndexParser.parseFile(filePath);
        printTable("Positional Index", outPut);

        int totalDocuments = 10;

        // Calculate Term Frequency (TF)
        Map<String, Map<Integer, Integer>> TF = TermFrequency.calculateTF(outPut, totalDocuments);
        printTable("Term Frequency (TF)", TF);

        // Calculate Weight TF
        Map<String, Map<Integer, Number>> weightTF = TermFrequency.calculateWeightTF(TF, totalDocuments);
        printTable("Weighted Term Frequency (TF Weight)", weightTF);

        // Calculate Document Frequency (DF)
        Map<String, Integer> DF = documentFrequency.calculateDF(outPut);
        printTable("Document Frequency (DF)", DF);

        // Calculate Inverse Document Frequency (IDF)
        Map<String, Number> IDF = documentFrequency.calculateIDF(DF, totalDocuments);
        printTable("Inverse Document Frequency (IDF)", IDF);

        // Calculate TF-IDF
        Map<String, Map<Integer, Number>> TFIDF = TF_IDF.calcTF_IDF(weightTF, IDF);
        printTable("TF-IDF", TFIDF);

        // Calculate Document Lengths
        Map<Integer, Number> docLengths = TF_IDF.calculateDocLengths(TFIDF);
        printTable("Document Lengths", docLengths);

        // Normalize TF-IDF
        Map<String, Map<Integer, Number>> normalizedTFIDF = TF_IDF.normalizeTFIDF(TFIDF, docLengths);
        printTable("Normalized TF-IDF", normalizedTFIDF);

//      -----------------------------------------Query----------------------------------------------
        int choice = 0;
        do{
            System.out.println("-------------------------Query Menu--------------------------");
            System.out.println("1- Write Query");
            System.out.println("2- Exit");
            System.out.print("Enter your choice: ");
            if (input.hasNextInt()) {
                choice = input.nextInt();
                input.nextLine();

                if (choice == 1) {
                    StringBuilder query = new StringBuilder();

                    System.out.print("Enter query: ");
                    String query1 = input.nextLine();
                    query.append(query1).append(" ");

                    while (true) {
                        System.out.print("Do you want to add operator and query? (y/n): ");
                        String continueChoice = input.nextLine();

                        if (continueChoice.equalsIgnoreCase("n")) {
                            break;
                        } else if (continueChoice.equalsIgnoreCase("y")) {
                            System.out.print("Enter operator: ");
                            String op = input.nextLine();
                            query.append(" ").append(op);

                            System.out.print("Enter query: ");
                            String query2 = input.nextLine();
                            query.append(" ").append(query2);
                        } else {
                            System.out.println("Invalid choice. Please type 'y' or 'n'.");
                        }
                    }

                    String finalQuery = query.toString();

                    List<String> parsedQuery = QueryProcessor.queryParser(finalQuery);
                    System.out.println("Query => " + parsedQuery);
                    List<Integer> queryResult = new ArrayList<>();

                    if(parsedQuery.size() > 1){
                        queryResult = QueryProcessor.showQueryResult(parsedQuery);
                    }else{
                        Map<String, List<Integer>> queryPositionalIndex = QueryProcessor.getQueryPositionalIndex(outPut , finalQuery);

                        for (List<Integer> list : queryPositionalIndex.values()) {
                            queryResult.addAll(list);
                        }
                    }
                    System.out.println("Query Result (Doc ids) => " + queryResult);

                    Map<String, Integer> queryTF = QueryOutputs.computeQueryTF(parsedQuery);
                    printTable("Query TF" , queryTF);

                    Map<String, Number> queryWeightTF = QueryOutputs.computeQueryWeightTF(queryTF);
                    printTable("Query WeightTF" , queryWeightTF);

                    Map<String, Number> queryIDF = QueryOutputs.retrieveQueryIDF(parsedQuery, IDF);
                    printTable("Query IDF" , queryIDF);

                    Map<String, Number> queryTF_IDF = QueryOutputs.computeQueryTF_IDF(queryWeightTF, queryIDF);
                    printTable("Query TF * IDF" , queryTF_IDF);

                    double queryLength = QueryOutputs.computeQueryLength(queryTF_IDF);
                    System.out.println("Query Length: " + queryLength);

                    Map<String, Number> normalizedQueryTF_IDF = QueryOutputs.computeNormalizedQueryTF_IDF(queryTF_IDF);
                    printTable("Query Normalized TF * IDF" , normalizedQueryTF_IDF);

                    Map<String, Map<Integer, Number>> normalizedDocTF_IDF = QueryOutputs.computeNormalizedDocTF_IDF(normalizedTFIDF , parsedQuery ,queryResult);
                    printTable("Doc Normalized TF * IDF" , normalizedDocTF_IDF);

                    Map<String, Map<Integer, Number>> productQueryMatchedDocs =QueryOutputs.productQueryMatchedDocs(normalizedDocTF_IDF , normalizedQueryTF_IDF);
                    printTable("product (Query * MatchedDocs) " , productQueryMatchedDocs);

                    Map<Integer, Double> similarityScores = QueryOutputs.computeSimilarity(productQueryMatchedDocs);
                    printTable("Similarity Scores (sum)", similarityScores);

                    List<Map.Entry<Integer, Double>> rankedDocuments = QueryOutputs.rankDocuments(similarityScores);
                    System.out.println("\n" + "═".repeat(50));
                    System.out.println(" Ranked Documents by Similarity ");
                    System.out.println("═".repeat(50));
                    System.out.printf("%-20s | %-20s\n", "Document", "Similarity");
                    System.out.println("─".repeat(50));

                    for (Map.Entry<Integer, Double> entry : rankedDocuments) {
                        System.out.printf("Doc%-18d | %-20.4f\n", entry.getKey(), entry.getValue());
                    }
                    System.out.println("═".repeat(50));


                } else if (choice == 2) {
                    System.out.println("Exiting...");
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    choice = 0;
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                input.next();
            }

            System.out.println("============================================================================");
        }while (choice != 2);
    }


    // Enhanced table printer
    private static <K, V> void printTable(String title, Map<K, V> data) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(" " + title + " ");
        System.out.println("═".repeat(50));
        System.out.printf("%-20s | %-50s\n", "Key", "Value");
        System.out.println("─".repeat(80));

        for (Map.Entry<K, V> entry : data.entrySet()) {
            System.out.printf("%-20s | %-50s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("═".repeat(80));
    }
}
