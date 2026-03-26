package app.services;

public class WordNormalizationService {
    public String normalize(String input) {
        if (input == null) {
            return "";
        }
        return input.toLowerCase().replaceAll("[^\\p{L}\\p{Nd}_]", "").trim();
    }
}
