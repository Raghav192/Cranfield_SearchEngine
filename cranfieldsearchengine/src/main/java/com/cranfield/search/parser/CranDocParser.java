package com.cranfield.search.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cranfield.search.model.CranDocModel;

public class CranDocParser {

    private final String filePath;

    public CranDocParser(String filePath) {
        this.filePath = filePath;
    }

    public List<CranDocModel> parse() throws IOException {
        List<CranDocModel> documents = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentId = "";
            StringBuilder contentBuilder = new StringBuilder();
            boolean readingContent = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(".I")) {
                    if (readingContent) {
                        documents.add(new CranDocModel(currentId, contentBuilder.toString()));
                        contentBuilder.setLength(0);
                    }
                    currentId = line.substring(3).trim();
                    readingContent = false;
                } else if (line.startsWith(".T") || line.startsWith(".A") || line.startsWith(".B") || line.startsWith(".W")) {
                    readingContent = true;
                } else if (readingContent) {
                    contentBuilder.append(line).append(" ");
                }
            }
            documents.add(new CranDocModel(currentId, contentBuilder.toString()));
        }

        return documents;
    }
}