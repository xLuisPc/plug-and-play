package app.contracts;

import app.dto.PluginRequest;
import app.dto.PluginResult;

public interface Plugin {
    String getName();

    PluginResult execute(PluginRequest request, PluginContext context);
}
