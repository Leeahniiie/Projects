package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.*;

/*
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    //private TimeSeries WH;

    private TreeMap<String, TimeSeries> wordMap;
    private TreeMap<Integer, Double> countMap;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordMap = new TreeMap<>();
        TimeSeries ts = new TimeSeries();
        In file = new In(wordsFilename);
        In countsfile = new In(countsFilename);
        countMap = new TreeMap<>();

        while (file.hasNextLine() && !file.isEmpty()) {
            String word = (file.readString());
            if (!wordMap.containsKey(word)) {
                ts = new TimeSeries();
                ts.put(file.readInt(), file.readDouble());
                wordMap.put(word, ts);
                file.readInt();
            } else {
                ts.put(file.readInt(), file.readDouble());
                wordMap.replace(word, ts);
                file.readInt();
            }
        }
        while (countsfile.hasNextLine() && !countsfile.isEmpty()) {
            String[] linesp = countsfile.readString().split(",");
            countMap.put(Integer.parseInt(linesp[0]), Double.parseDouble(linesp[1]));
        }
    }


    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries(this.wordMap.get(word), startYear, endYear);
        if (wordMap.containsKey(word)) {
            return ts;
        }
        return new TimeSeries();
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        if (wordMap.containsKey(word)) {
            TimeSeries ts;
            ts = wordMap.get(word);
            return ts;
        }
        return new TimeSeries();
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries numwords = new TimeSeries();
        Collection keys = countMap.keySet();
        Iterator itr = keys.iterator();
        Collection values = countMap.values();
        Iterator itrs = values.iterator();
        while (itr.hasNext()) {
            numwords.put((Integer) itr.next(), (Double) itrs.next());
        }
        return numwords;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {

        return countHistory(word, startYear, endYear).dividedBy(totalCountHistory());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return countHistory(word).dividedBy(totalCountHistory());
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        Iterator itr = words.iterator();
        TimeSeries curr = wordMap.get(itr.next());
        while (itr.hasNext()) {
            curr = curr.plus(wordMap.get(itr.next()));
        }
        TimeSeries div = curr.dividedBy(totalCountHistory());
        TimeSeries rv = new TimeSeries(div, startYear, endYear);
        return rv;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        Iterator itr = words.iterator();
        TimeSeries curr = wordMap.get(itr.next());
        while (itr.hasNext()) {
            curr = curr.plus(wordMap.get(itr.next()));
        }
        TimeSeries rv = curr.dividedBy(totalCountHistory());
        return rv;
    }

}
