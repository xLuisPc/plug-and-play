package app.plugins.wordlist;

import app.contracts.Plugin;
import app.contracts.PluginContext;
import app.dto.PluginMessage;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.dto.WordListDTO;
import app.services.StopWordsService;
import app.services.TextTokenizerService;
import app.services.WordNormalizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class UniqueWordsPlugin implements Plugin {
    @Override
    public String getName() {
        return "ListarPalabras";
    }

    @Override
    public PluginResult execute(PluginRequest request, PluginContext context) {
        TextTokenizerService tokenizer = context.getService(TextTokenizerService.class);
        WordNormalizationService normalizer = context.getService(WordNormalizationService.class);
        StopWordsService stopWords = context.getService(StopWordsService.class);

        Set<String> unique = new TreeSet<>();
        for (String raw : tokenizer.tokenize(request.getText())) {
            String token = normalizer.normalize(raw);
            if (!token.isBlank() && !stopWords.isStopWord(token)) {
                unique.add(token);
            }
        }

        List<PluginMessage> messages = new ArrayList<>();
        messages.add(new PluginMessage(PluginMessage.Type.INFO, getName(),
                "Se encontraron " + unique.size() + " palabras unicas."));
        List<WordListDTO> words = unique.stream().map(WordListDTO::new).collect(Collectors.toList());
        return new PluginResult(getName(), "Palabras encontradas (sin repetidas)", words, messages);
    }
}
