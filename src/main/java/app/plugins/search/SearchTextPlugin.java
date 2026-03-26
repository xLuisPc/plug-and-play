package app.plugins.search;

import app.contracts.Plugin;
import app.contracts.PluginContext;
import app.dto.PluginMessage;
import app.dto.PluginRequest;
import app.dto.PluginResult;
import app.dto.SearchMatchDTO;
import app.services.PositionLocatorService;

import java.util.ArrayList;
import java.util.List;

public class SearchTextPlugin implements Plugin {
    @Override
    public String getName() {
        return "BuscarTexto";
    }

    @Override
    public PluginResult execute(PluginRequest request, PluginContext context) {
        List<PluginMessage> messages = new ArrayList<>();
        String term = request.getSearchTerm();
        if (term == null || term.isBlank()) {
            messages.add(new PluginMessage(PluginMessage.Type.ERROR, getName(),
                    "Debe ingresar una palabra a buscar."));
            return new PluginResult(getName(), "Busqueda", List.of(), messages);
        }

        PositionLocatorService locator = context.getService(PositionLocatorService.class);
        List<SearchMatchDTO> matches = locator.findMatches(request.getText(), term.trim());
        messages.add(new PluginMessage(PluginMessage.Type.INFO, getName(),
                "Coincidencias encontradas: " + matches.size()));
        return new PluginResult(getName(), "Resultados de busqueda", matches, messages);
    }
}
