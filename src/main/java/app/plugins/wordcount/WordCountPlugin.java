package app.plugins.wordcount;

import app.contracts.Plugin;
import app.contracts.PluginContext;
import app.dto.PluginMessage;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.dto.WordCountDTO;
import app.services.TextTokenizerService;
import app.services.WordNormalizationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordCountPlugin implements Plugin {
    @Override
    public String getName() {
        return "ContadorPalabras";
    }

    @Override
    public PluginResult execute(PluginRequest request, PluginContext context) {
        TextTokenizerService tokenizer = context.getService(TextTokenizerService.class);
        WordNormalizationService normalizer = context.getService(WordNormalizationService.class);

        Map<String, Integer> countMap = new HashMap<>();
        for (String raw : tokenizer.tokenize(request.getText())) {
            String token = normalizer.normalize(raw);
            if (!token.isBlank()) {
                countMap.merge(token, 1, Integer::sum);
            }
        }

        List<WordCountDTO> data = countMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(e -> new WordCountDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        List<PluginMessage> messages = new ArrayList<>();
        messages.add(new PluginMessage(PluginMessage.Type.INFO, getName(),
                "Conteo generado para " + data.size() + " palabras."));
        return new PluginResult(getName(), "Frecuencia de palabras", data, messages);
    }
}
