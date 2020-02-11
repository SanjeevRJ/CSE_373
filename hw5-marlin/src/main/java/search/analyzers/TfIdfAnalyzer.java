package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> docNorms;


    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.docNorms = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> vector : documentTfIdfVectors) {
            docNorms.put(vector.getKey(), norm(vector.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        if (pages == null) {
            throw new IllegalArgumentException();
        }

        IDictionary<String, Integer> numDocs = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IList<String> words = page.getWords();
            ISet<String> inDoc = new ChainedHashSet<>();
            for (String word : words) {
                if (!inDoc.contains(word)) {
                    if (numDocs.containsKey(word)) {
                        numDocs.put(word, numDocs.get(word) + 1);
                    } else {
                        numDocs.put(word, 1);
                    }
                    inDoc.add(word);
                }
            }
        }

        double numPages = pages.size();
        IDictionary<String, Double> retIdfScores = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> wordNum : numDocs) {
            int docs = numDocs.get(wordNum.getKey());
            double idfScore = numPages/docs;
            idfScore = Math.log(idfScore);
            retIdfScores.put(wordNum.getKey(), idfScore);
        }
        return retIdfScores;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        if (words == null) {
            throw new IllegalArgumentException();
        }

        IDictionary<String, Integer> numAppearances = new ChainedHashDictionary<>();
        for (String word : words) {
            if (numAppearances.containsKey(word)) {
                numAppearances.put(word, numAppearances.get(word) + 1);
            } else {
                numAppearances.put(word, 1);
            }
        }

        double numWords = words.size();
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<>();
        for (KVPair<String, Integer> word : numAppearances) {
            double tfScore = word.getValue()/numWords;
            tfScores.put(word.getKey(), tfScore);
        }

        return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        idfScores = computeIdfScores(pages);
        IDictionary<URI, IDictionary<String, Double>> uris = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfIdfs = new ChainedHashDictionary<>();
            IDictionary<String, Double> tfScores = computeTfScores(page.getWords());
            for (KVPair<String, Double> tfScore : tfScores) {
                double tfIdf = idfScores.get(tfScore.getKey()) * tfScore.getValue();
                tfIdfs.put(tfScore.getKey(), tfIdf);
            }
            uris.put(page.getUri(), tfIdfs);
        }
        return uris;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        if (query == null || pageUri== null) {
            throw new IllegalArgumentException();
        }

        IDictionary<String, Double> docVector = documentTfIdfVectors.get(pageUri);
        double numerator = 0.0;
        double queryNorm = 0.0;
        for (String word : query) {
            double docWordScore = 0.0;
            if (docVector.containsKey(word)) {
                docWordScore = docVector.get(word);
            }
            double queryIdf = 0.0;
            if (idfScores.containsKey(word)) {
                queryIdf = idfScores.get(word);
            }
            double queryWordScore = computeTfScores(query).get(word) * queryIdf;
            numerator += docWordScore * queryWordScore;
            queryNorm +=  queryWordScore * queryWordScore;
        }
        queryNorm = Math.sqrt(queryNorm);
        double docNorm = docNorms.get(pageUri);
        double denominator = docNorm * queryNorm;

        if (denominator != 0) {
            return numerator/denominator;
        }
        return 0.0;
    }

    private double norm(IDictionary<String, Double> scores) {
        double output = 0.0;
        for (KVPair<String, Double> score : scores) {
            double value = score.getValue();
            output += value*value;
        }
        return Math.sqrt(output);
    }
}
