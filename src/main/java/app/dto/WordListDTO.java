package app.dto;

public class WordListDTO {
    private final String word;

    public WordListDTO(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }
}
