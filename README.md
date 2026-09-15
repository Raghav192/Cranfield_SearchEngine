# Cranfield Search Engine

## Overview
A Java based information retrieval system built with Apache Lucene to index and query the Cranfield dataset. The system parses 1,400 aerospace engineering documents and 225 queries. It evaluates different combinations of text analyzers and scoring models to find the optimal search configuration.

## Project Structure
* `cranfieldsearchengine/`: Java source code containing the parsing, indexing, and searching logic.
* `cranfield/`: The standard Cranfield collection containing documents (`cran.all.1400`) and queries (`cran.qry`).
* `trec_eval-9.0.7/`: The TREC evaluation tool used to calculate performance metrics.

## Configuration Variations
The search engine tests 15 distinct configurations by combining different analyzers and similarities:

### Analyzers
* **StandardAnalyzer**: General purpose text processing.
* **EnglishAnalyzer**: Tailored for English with stemming and stop words.
* **SimpleAnalyzer**: Divides text at non letter characters and lowercases.
* **WhitespaceAnalyzer**: Divides text strictly at whitespace.
* **CustomAnalyzer**: Custom pipeline using a StandardTokenizer, LowerCaseFilter, StopFilter (English stop words), and PorterStemFilter.

### Scoring Models
* **BM25Similarity**: Probabilistic ranking model.
* **ClassicSimilarity**: TF IDF based ranking model.
* **BooleanSimilarity**: Simple boolean matching.

## Setup and Execution
1. Ensure Java 21 and Maven are installed.
2. Navigate to the `cranfieldsearchengine` directory.
3. Build the project:
   `mvn clean install`
4. Run the main `SearchEngine` class. The system will iterate through all analyzer and similarity combinations and output the prediction files to a `results` directory.

## Evaluation Results
The system output is evaluated using `trec_eval` to calculate Mean Average Precision (MAP) and recall. 

The top performing configuration was the `EnglishAnalyzer` paired with `ClassicSimilarity` (TF-IDF):
* **MAP**: 0.4148
* **Relevant Documents Retrieved**: 1327 / 1837

Other notable results:
* CustomAnalyzer with ClassicSimilarity: MAP 0.4146
* EnglishAnalyzer with BM25Similarity: MAP 0.4125
* CustomAnalyzer with BM25Similarity: MAP 0.4110
