package com.cranfield.search.indexer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.cranfield.search.model.CranDocModel;

public class Indexer {
    private final String indexDirectoryPath;

    public Indexer(String indexDirectoryPath) {
        this.indexDirectoryPath = indexDirectoryPath;
    }

    public void createIndex(List<CranDocModel> documents, Analyzer analyzer) throws IOException {
        Directory indexDir = FSDirectory.open(Paths.get(indexDirectoryPath));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (IndexWriter writer = new IndexWriter(indexDir, config)) {
        System.out.println("Starting to index " + documents.size() + " documents...");

        for (CranDocModel docModel : documents){
            Document doc = new Document();
            doc.add(new StringField("id", docModel.getId(), Field.Store.YES));
            doc.add(new TextField("content", docModel.getContent(), Field.Store.NO));
            writer.addDocument(doc);
        }
        System.out.println("All documents have been indexed.");

        writer.close();
        indexDir.close();
        }
    }
}
