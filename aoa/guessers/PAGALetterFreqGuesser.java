package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.*;

public class PAGALetterFreqGuesser implements Guesser{
    private final List<String> words;

    public PAGALetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    @Override
    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN and the GUESSES that have been made. */
    public char getGuess(String pattern, List<Character> guesses) {
       this.words.removeIf(n -> n.length() != pattern.length());
        List<String> lst = new ArrayList<>(this.words);
        List<Character> guess = new ArrayList<>(guesses);
        PatternMatcher(pattern, lst, guess);
        Iterator<String> iter = lst.iterator();
        while (iter.hasNext()) {
            String elem = iter.next();
            for (Character c: guess) {
                if (elem.contains(String.valueOf(c))) {
                    iter.remove();
                    break;  // exit the inner loop once an element has been removed
                }
            }
        }
        Map<Character, Integer> map = getFrequencyMap(lst);
        TreeMap<Character, Integer> sortedmap = new TreeMap(map);
        return getGuessHelper(guesses, sortedmap);
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
    public void PatternMatcher(String pattern, List<String> lst, List<Character> guesses) {
        int tracker = 0;
        Character prevChar = '-';
        for (Character c : pattern.toCharArray()) {
            Iterator<String> itr = lst.iterator();
            if (c == '-') {
                Iterator<String> second = lst.iterator();
                while (second.hasNext()) {
                    String element = second.next();
                    if (element.charAt(tracker) == prevChar) {
                        second.remove();
                    }
                }
                tracker++;
                continue;
            }
            prevChar = c;
            guesses.remove(c);
            while (itr.hasNext()) {
                String elem = itr.next();
                char[] characters = elem.toCharArray();
                if (elem.length() != pattern.length() || characters[tracker] != c) {
                    itr.remove();
                }
            }
            tracker++;
            itr = lst.iterator();
        }
    }
    public static void main(String[] args) {
        PAGALetterFreqGuesser pagalfg = new PAGALetterFreqGuesser("data/sorted_scrabble.txt");
        Character c = pagalfg.getGuess("-ee", List.of('e'));
        System.out.println(c);
    }
}
