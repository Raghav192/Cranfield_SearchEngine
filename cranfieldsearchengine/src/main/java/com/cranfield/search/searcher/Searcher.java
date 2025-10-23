package com.cranfield.search.searcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.cranfield.search.model.QueryModel;

public class Searcher {

    private final String indexDirectoryPath;

    public Searcher(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public void runQueries(List<QueryModel> queries, Analyzer analyzer, Similarity similarity, String resultsFilePath) throws IOException, ParseException {
        Directory indexDir = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexReader reader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        searcher.setSimilarity(similarity);
        
        QueryParser parser = new QueryParser("content", analyzer);
        
        System.out.println("Running " + queries.size() + " queries...");

        try (PrintWriter resultsWriter = new PrintWriter(new FileWriter(resultsFilePath))) {
            for (QueryModel queryModel : queries) {
                // Escape special characters in the query text
                String queryText = QueryParser.escape(queryModel.getText());
                if (queryText.isEmpty()) {
                    continue; // Skip empty queries
                }
                Query query = parser.parse(queryText);
                
                TopDocs results = searcher.search(query, 100);
                ScoreDoc[] hits = results.scoreDocs;
                
                int rank = 1;
                for (ScoreDoc hit : hits) {
                    // *** THIS IS THE CORRECTED LINE ***
                    Document doc = searcher.storedFields().document(hit.doc);
                    String docId = doc.get("id");
                    
                    String runName = analyzer.getClass().getSimpleName() + "_" + similarity.getClass().getSimpleName();
                    String resultLine = String.format("%d Q0 %s %d %f %s",
                            queryModel.getId(),
                            docId,
                            rank,
                            hit.score,
                            runName
                    );
                    resultsWriter.println(resultLine);
                    rank++;
                }
            }
        }
        
        System.out.println("Querying complete. Results written to " + resultsFilePath);
        
        reader.close();
        indexDir.close();
    }
}