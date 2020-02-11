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
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;
    private IDictionary<URI, Double> numUniqueLinks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        numUniqueLinks = new ChainedHashDictionary<>();
        for (Webpage page : webpages) {
            ISet<URI> uniqueLinks = new ChainedHashSet<>();
            for (URI link : page.getLinks()) {
                uniqueLinks.add(link);
            }
            numUniqueLinks.put(page.getUri(), (double) uniqueLinks.size());
        }
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        if (webpages == null) {
            throw new IllegalArgumentException();
        }

        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        for (Webpage page : webpages) {
            URI curLink = page.getUri();
            ISet<URI> edges = new ChainedHashSet<>();
            IList<URI> links = page.getLinks();
            for (URI link : links) {
                if (curLink != link && numUniqueLinks.containsKey(link)) {
                    edges.add(link);
                }
            }
            graph.put(curLink, edges);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> oldPageRanks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> vertex : graph) {
            double numPages = graph.size();
            oldPageRanks.put(vertex.getKey(), 1/numPages);
        }

        IDictionary<URI, Double> newPageRanks = new ChainedHashDictionary<>();
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            for (KVPair<URI, ISet<URI>> vertex : graph) {
                newPageRanks.put(vertex.getKey(), 0.0);
            }

            for (KVPair<URI, ISet<URI>> vertex : graph) {
                if (vertex.getValue().size() > 0) {
                    double uniqueLinks = numUniqueLinks.get(vertex.getKey());
                    double curNewPageRank = decay * (oldPageRanks.get(vertex.getKey())/uniqueLinks);
                    for (URI link : vertex.getValue()) {
                        newPageRanks.put(link, newPageRanks.get(link) + curNewPageRank);
                    }
                } else {
                    double curNewPageRank = decay * (oldPageRanks.get(vertex.getKey())/graph.size());
                    for (KVPair<URI, ISet<URI>> curVertex : graph) {
                        newPageRanks.put(curVertex.getKey(), newPageRanks.get(curVertex.getKey()) + curNewPageRank);
                    }
                }
                double distribution = (1-decay)/graph.size();
                newPageRanks.put(vertex.getKey(), newPageRanks.get(vertex.getKey()) + distribution);
            }

            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean converged = true;
            for (KVPair<URI, Double> rank : newPageRanks) {
                double difference = Math.abs(rank.getValue() - oldPageRanks.get(rank.getKey()));
                if (difference > epsilon) {
                    converged = false;
                    break;
                }
            }
            if (converged) {
                break;
            } else {
                for (KVPair<URI, Double> rank : newPageRanks) {
                    oldPageRanks.put(rank.getKey(), rank.getValue());
                }
            }
        }
        return newPageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
