package app.dto;

public class PluginMessage {
    public enum Type { INFO, WARN, ERROR }

    private final Type type;
    private final String plugin;
    private final String message;

    public PluginMessage(Type type, String plugin, String message) {
        this.type = type;
        this.plugin = plugin;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getPlugin() {
        return plugin;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + type + "] " + plugin + ": " + message;
    }
}
