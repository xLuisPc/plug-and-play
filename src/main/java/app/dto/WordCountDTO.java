package app.dto;

public class WordCountDTO {
    private final String word;
    private final int count;

    public WordCountDTO(String word, int count) {
        this.word = word;
        this.count = count;
    }

    @Override
    public String toString() {
        return word + ": " + count;
    }
}
