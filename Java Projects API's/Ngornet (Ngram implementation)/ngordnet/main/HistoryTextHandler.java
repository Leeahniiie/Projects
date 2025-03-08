package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.browser.NgordnetQueryHandler;

import java.util.Iterator;
import java.util.List;


public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }


    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        Iterator itr = words.iterator();
        String word = itr.next().toString();
        TimeSeries history = map.weightHistory(word, q.startYear(), q.endYear());
        String response = word + ": " + history.toString() + "\n";
        while (itr.hasNext()) {
            word = itr.next().toString();
            history = map.weightHistory(word, q.startYear(), q.endYear());
            response += word + ": " + history.toString() + "\n";
        }
        return response;
    }
}

