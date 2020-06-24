package readability;

import java.io.File;

public class Main {


    public static void main(String[] args) {
        TextAnalyzer analyzer = new TextAnalyzer();
        analyzer.analyzeText(new File(args[0]));
        analyzer.printStats();
        analyzer.printGrade();
    }
}
