package app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTokenizerService {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}[\\p{L}\\p{Nd}_]*");

    public List<String> tokenize(String text) {
        List<String> words = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return words;
        }
        Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }
}
