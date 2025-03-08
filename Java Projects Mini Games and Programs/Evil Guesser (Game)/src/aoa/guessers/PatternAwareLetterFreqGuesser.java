package aoa.guessers;

import aoa.utils.FileUtils;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;

public class PatternAwareLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PatternAwareLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    @Override
    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN. */
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> lst = new ArrayList<>(this.words);
        PatternMatcher(pattern, lst);
        Map<Character, Integer> map = getFrequencyMap(lst);
        TreeMap<Character, Integer> sortedmap = new TreeMap(map);
        return getGuessHelper(guesses, sortedmap);
    }

    public void PatternMatcher(String pattern, List<String> lst) {

        Iterator itr = lst.iterator();
        int index = 0;
        for (Character c : pattern.toCharArray()) {
            if (c == '-') {
                continue;
            }
            index = Math.floorMod(pattern.indexOf(c), pattern.length());
            while (itr.hasNext()) {
                String elem = (String) itr.next();
                char[] characters = elem.toCharArray();
                if (elem.length() != pattern.length() || characters[index] != c) {
                    itr.remove();
                }
            }
            itr = lst.iterator();
        }

    }
    public Map<Character, Integer> getFrequencyMap(List<String> lst) {
        Map<Character, Integer> map = new HashMap<>();
        for (String elem : lst) {
            for (char c : elem.toCharArray()) {
                if (map.containsKey(c)) {
                    int val = map.get(c);
                    map.put(c, val+1);
                } else {
                    map.put(c,1);
                }
            }
        }
        return map;
    }

    public char getGuessHelper(List<Character> guesses, Map<Character, Integer> map) {
        for (Character c : guesses) {
            map.remove(c);
        }
        int maxValue = 0;
        Character maxKey = null;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxKey;
    }

    public static void main(String[] args) {
        PatternAwareLetterFreqGuesser palfg = new PatternAwareLetterFreqGuesser("data/example.txt");
        System.out.println(palfg.getGuess("-e--", List.of('e')));
    }
}