package app.services;

import java.util.Set;

public class StopWordsService {
    private final Set<String> stopWords = Set.of(
            "a", "al", "algo", "algun", "alguna", "alguno", "algunos", "ante", "con", "contra",
            "cual", "como", "de", "del", "desde", "donde", "e", "el", "ella", "ellas", "ellos",
            "en", "entre", "era", "eramos", "es", "esa", "ese", "eso", "esta", "este", "esto",
            "fue", "fueron", "ha", "hacia", "hasta", "la", "las", "le", "les", "lo", "los", "mas",
            "mi", "mis", "muy", "ni", "no", "nos", "o", "para", "pero", "por", "que", "se", "si",
            "sin", "sobre", "su", "sus", "te", "tu", "un", "una", "uno", "unos", "y", "ya"
    );

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}
