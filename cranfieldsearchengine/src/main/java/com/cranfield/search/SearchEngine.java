package com.cranfield.search;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;

import com.cranfield.search.analyzer.CustomAnalyzer;
import com.cranfield.search.indexer.Indexer;
import com.cranfield.search.model.CranDocModel;
import com.cranfield.search.model.QueryModel;
import com.cranfield.search.parser.CranDocParser;
import com.cranfield.search.parser.CranQueryParser;
import com.cranfield.search.searcher.Searcher;

public class SearchEngine {
    private static final String INDEX_DIRECTORY = "./index";
    private static final String RESULTS_DIRECTORY = "./results";
    private static final String CRANFIELD_DOCS_PATH ="cranfield\\cran.all.1400";
    private static final String CRANFIELD_QUERIES_PATH = "cranfield\\cran.qry";

    public static void main(String[] args) {
        System.out.println("Cranfield Search Engine Starting...");

        try{
            Files.createDirectories(Paths.get(RESULTS_DIRECTORY));
        }
        catch(IOException e) {
            System.err.println("Error creating results directory: " + e.getMessage());
            return;
        }

        Analyzer[] analyzers = {
            new StandardAnalyzer(),
            new EnglishAnalyzer(),
            new SimpleAnalyzer(),
            new WhitespaceAnalyzer(),
            new CustomAnalyzer()
        };

        Similarity[] similarities = {
            new BM25Similarity(),       
            new ClassicSimilarity(),
            new BooleanSimilarity()    
        };

        for (Analyzer analyzer : analyzers){
            for (Similarity similarity : similarities){
                String runName = analyzer.getClass().getSimpleName() + "_" + similarity.getClass().getSimpleName();
                System.out.println("\n");
                System.out.println("--- STARTING RUN: " + runName + " ---");
                String resultsFilePath = Paths.get(RESULTS_DIRECTORY,"results_" + runName + ".txt").toString();

                try {
                    CranDocParser docParser = new CranDocParser(CRANFIELD_DOCS_PATH);
                    CranQueryParser queryParser = new CranQueryParser(CRANFIELD_QUERIES_PATH);
                    Indexer indexer = new Indexer(INDEX_DIRECTORY);
                    Searcher searcher = new Searcher(INDEX_DIRECTORY);

                    List<CranDocModel> documents = docParser.parse();
                    List<QueryModel> queries = queryParser.parse();

                    System.out.println("Creating index with " + analyzer.getClass().getSimpleName() + "...");
                    indexer.createIndex(documents, analyzer);

                    System.out.println("Running queries with " + similarity.getClass().getSimpleName() + "...");
                    searcher.runQueries(queries, analyzer, similarity, resultsFilePath);

                } catch (Exception e) {
                    System.err.println("An error occurred during run " + runName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            analyzer.close();
        }
    }
}