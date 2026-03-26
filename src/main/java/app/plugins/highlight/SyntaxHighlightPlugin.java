package app.plugins.highlight;

import app.contracts.Plugin;
import app.contracts.PluginContext;
import app.dto.HighlightMatchDTO;
import app.dto.PluginMessage;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.services.KeywordDictionaryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlightPlugin implements Plugin {
    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}[\\p{L}\\p{Nd}_]*");

    @Override
    public String getName() {
        return "ResaltarTexto";
    }

    @Override
    public PluginResult execute(PluginRequest request, PluginContext context) {
        KeywordDictionaryService dictionary = context.getService(KeywordDictionaryService.class);
        List<HighlightMatchDTO> matches = new ArrayList<>();
        Map<String, Integer> counters = dictionary.getLanguageCounters();

        Matcher matcher = WORD_PATTERN.matcher(request.getText());
        while (matcher.find()) {
            String word = matcher.group();
            String language = dictionary.languageOf(word);
            if (language != null) {
                matches.add(new HighlightMatchDTO(matcher.start(), matcher.end(), language, word));
                counters.put(language, counters.get(language) + 1);
            }
        }

        List<PluginMessage> messages = new ArrayList<>();
        messages.add(new PluginMessage(PluginMessage.Type.INFO, getName(),
                "Tokens resaltados: " + matches.size() + " (Java=" + counters.get("Java")
                        + ", C++=" + counters.get("C++") + ", SQL=" + counters.get("SQL") + ")"));
        return new PluginResult(getName(), "Palabras reservadas encontradas", matches, messages);
    }
}
