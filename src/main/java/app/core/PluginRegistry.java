package app.core;

import app.contracts.Plugin;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class PluginRegistry {
    private final Map<String, Plugin> plugins = new LinkedHashMap<>();

    public void register(Plugin plugin) {
        plugins.put(plugin.getName(), plugin);
    }

    public Plugin get(String name) {
        return plugins.get(name);
    }

    public Collection<Plugin> getAll() {
        return plugins.values();
    }
}
