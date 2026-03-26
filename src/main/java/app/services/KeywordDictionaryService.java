package app.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeywordDictionaryService {
    private final Set<String> javaKeywords = Set.of(
            "class", "public", "private", "protected", "static", "void", "if", "else",
            "switch", "case", "for", "while", "do", "try", "catch", "finally", "new",
            "return", "import", "package", "interface", "extends", "implements"
    );

    private final Set<String> cppKeywords = Set.of(
            "int", "float", "double", "char", "bool", "namespace", "using", "include",
            "std", "cout", "cin", "if", "else", "switch", "case", "for", "while", "class",
            "public", "private", "protected", "return", "void", "new", "delete"
    );

    private final Set<String> sqlKeywords = Set.of(
            "select", "from", "where", "insert", "into", "update", "delete", "create",
            "table", "drop", "alter", "join", "left", "right", "inner", "outer", "group",
            "order", "by", "having", "and", "or", "not", "null", "values", "set"
    );

    public String languageOf(String word) {
        String token = word.toLowerCase();
        if (javaKeywords.contains(token)) {
            return "Java";
        }
        if (cppKeywords.contains(token)) {
            return "C++";
        }
        if (sqlKeywords.contains(token)) {
            return "SQL";
        }
        return null;
    }

    public Map<String, Integer> getLanguageCounters() {
        Map<String, Integer> counters = new HashMap<>();
        counters.put("Java", 0);
        counters.put("C++", 0);
        counters.put("SQL", 0);
        return counters;
    }
}
