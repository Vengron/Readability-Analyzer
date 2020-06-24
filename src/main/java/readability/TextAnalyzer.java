package readability;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Class for scanning the text from a file,
counting the required data for the readability analysis
and printing the results
 */
public class TextAnalyzer {

    private Scanner scanner;
    private int sentences = 0;
    private int words = 0;
    private int chars = 0;
    private int syllables = 0;
    private int polysyllables = 0;
    private final ArrayList<String> text = new ArrayList<>();


    public void analyzeText(File source) {
        try {
            scanner = new Scanner(source);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            text.add(line);

            String[] sentences = line.split("[!?.]");
            this.sentences += sentences.length;
            this.chars += sentences.length;

            for (String sentence : sentences) {
                String[] words = sentence.trim().split(" ");
                this.words += words.length;

                for(String word: words) {
                    syllables += countSyllables(word);
                    chars += word.length();
                }
            }
        }
        scanner.close();

        // check if last sentence miss dot
        String lastWord = text.get(text.size() - 1);
        char lastChar = lastWord.charAt(lastWord.length() - 1);
        if (lastChar != '.' && lastChar != '?' && lastChar != '!') {
            --chars;
        }
    }

    private int countSyllables(String word) {
        word = word.replaceAll("[,\"':;]", "");
        Pattern pattern = Pattern.compile("[aeiouyAEIOUY]");
        Matcher matcher = pattern.matcher(word);
        int vowels = 0;
        int indexOfLastVowel = -1;

        while(matcher.find()) {
            if (matcher.start() == word.length() - 1 && matcher.group().equals("e")) {
                continue;
            } else if(matcher.start() - 1 == indexOfLastVowel) {
                continue;
            }
            ++vowels;
            indexOfLastVowel = matcher.start();
        }
        if (vowels > 2) {
            ++polysyllables;
        } else if(vowels == 0) {
            return 1;
        }
        return vowels;
    }

    public void printStats() {
        System.out.printf("Words: %d\nSentences: %d\n" +
                        "Characters: %d\nSyllables: %d\n" +
                        "Polysyllables: %d\n"
                , words, sentences, chars, syllables, polysyllables);
    }

    public void printGrade() {
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String[] commands = {"ARI", "FK", "SMOG", "CL"};
        double age = 0d;
        scanner = new Scanner(System.in);
        String command = scanner.nextLine().trim();
        if ("all".equals(command)) {
            for (String comm: commands) {
                age += grade(comm);
            }
            age /= 4;
            System.out.printf("\nThis text should be understood in average by %.2f years old.\n", age);
        } else {
            grade(command);
        }
    }

    private int grade(String command) {
        switch (command) {
            case "ARI":
                AriAnalyzer ari = new AriAnalyzer();
                ari.determineScore(sentences, words, chars);
                System.out.printf("Automated Readability Index: %.2f (about %d years old).\n",
                        ari.getScore(), ari.getAge());
                return ari.getAge();
            case "FK":
                FkAnalyzer fk = new FkAnalyzer();
                fk.determineScore(sentences, words, syllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d years old).\n",
                        fk.getScore(), fk.getAge());
                return fk.getAge();
            case "SMOG":
                SmogAnalyzer smog = new SmogAnalyzer();
                smog.determineScore(sentences, polysyllables);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d years old).\n",
                        smog.getScore(), smog.getAge());
                return smog.getAge();
            case "CL":
                ClAnalyzer cl = new ClAnalyzer();
                cl.determineScore(sentences, words, chars);
                System.out.printf("Coleman–Liau index: %.2f (about %d years old).\n",
                        cl.getScore(), cl.getAge());
                return cl.getAge();
            default:
                System.out.println("Unknown command");
                return 0;
        }
    }
}
