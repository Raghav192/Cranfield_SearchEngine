package com.cranfield.search.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import com.cranfield.search.model.QueryModel;

public class CranQueryParser {

    private final String filePath;

    public CranQueryParser(String filePath) {
        this.filePath = filePath;
    }
    public List<QueryModel> parse() throws IOException {
        List<QueryModel> queries = new ArrayList<>();
        int sequentialIdCounter = 1;

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder queryTextBuilder = new StringBuilder();
            boolean readingQueryText = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".I")){

                    if(readingQueryText){
                        queries.add(new QueryModel(sequentialIdCounter - 1, queryTextBuilder.toString().trim()));
                        queryTextBuilder.setLength(0);
                    }
                    readingQueryText = false;
                    sequentialIdCounter++;
                    } 
                    else if (line.startsWith(".W")){
                        readingQueryText = true;
                    }
                    else if (readingQueryText){
                        queryTextBuilder.append(line).append(" ");
                    }
                }
                if (queryTextBuilder.length() > 0){
                    queries.add(new QueryModel(sequentialIdCounter - 1, queryTextBuilder.toString().trim()));
                }
            }    
        return queries;
    }
}
