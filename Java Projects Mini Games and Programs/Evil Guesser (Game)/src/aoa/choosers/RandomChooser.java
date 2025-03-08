package aoa.choosers;

import edu.princeton.cs.algs4.StdRandom;
import aoa.utils.FileUtils;
import java.util.List;

public class RandomChooser implements Chooser {
    private final String chosenWord;
    private String pattern = "-";

    int wordLength;

    public RandomChooser(int wordLength, String dictionaryFile) {
        // TODO: Fill in/change this constructor.
        this.wordLength = wordLength;
        if (wordLength < 1) {
            throw new IllegalArgumentException("No Word");
        }
        List<String> words = FileUtils.readWordsOfLength(dictionaryFile, wordLength);
        if (words.isEmpty()) {
            throw new IllegalStateException("No words!");
        }
        int numWords = words.size();
        int randomlyChosenWordNumber = StdRandom.uniform(numWords);
        chosenWord = words.get(randomlyChosenWordNumber);
        for (int i = 0; i < wordLength - 1; i++) {
            pattern += '-';
        }
    }

    @Override
    public int makeGuess(char letter) {
        int num = 0;
        int i = 0;
        char[] charArray = pattern.toCharArray();
        for (Character c: chosenWord.toCharArray()) {
            if (c == letter) {
                charArray[i] = c;
                num += 1;
            }
            i++;
        }
        String pattern = new String(charArray);
        this.pattern = pattern;
        return num;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getWord() {
        return chosenWord;
    }
}
