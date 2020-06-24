package readability;

/*
Classes for determining readability of a text
and recommended age of a reader
based on four different formulas
 */
public abstract class ReadabilityAnalyzer {

    protected double score = 0;
    protected int age = 0;

    protected void determineAge() {
        int roundedScore = (int) Math.ceil(score);

        switch ( roundedScore) {
            case 1:
            case 2:
                age = 5 + roundedScore;
                break;
            case 3:
                age = 9;
                break;
            case 13:
                age = 24;
                break;
            case 14:
                age = 25;
                break;
            default:
                age = 6 + roundedScore;
                break;
        }
    }

    public int getAge() {
        return age;
    }

    public double getScore() {
        return score;
    }
}

class AriAnalyzer extends ReadabilityAnalyzer {

    public void determineScore(int sentences, int words, int chars) {
        score = 4.71 * chars / words + 0.5 * words / sentences - 21.43;
        determineAge();
    }

}

class FkAnalyzer extends ReadabilityAnalyzer {

    public void determineScore(int sentences, int words, int syllables) {
        score = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        determineAge();
    }

}

class SmogAnalyzer extends ReadabilityAnalyzer {

    public void determineScore(int sentences, int polysyllables) {
        score = 1.043 * Math.sqrt(polysyllables * 30.0 / sentences) + 3.1291;
        determineAge();
    }

}

class ClAnalyzer extends ReadabilityAnalyzer {

    public void determineScore(int sentences, int words, int chars) {
        score = 0.0588 * chars / words * 100 - 0.296 * sentences / words * 100 - 15.8;
        determineAge();
    }

}



