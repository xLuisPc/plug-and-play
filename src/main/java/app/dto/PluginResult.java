package app.dto;

import java.util.ArrayList;
import java.util.List;

public class PluginResult {
    private final String pluginName;
    private final String title;
    private final Object data;
    private final List<PluginMessage> messages;

    public PluginResult(String pluginName, String title, Object data, List<PluginMessage> messages) {
        this.pluginName = pluginName;
        this.title = title;
        this.data = data;
        this.messages = messages == null ? new ArrayList<>() : messages;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getTitle() {
        return title;
    }

    public Object getData() {
        return data;
    }

    public List<PluginMessage> getMessages() {
        return messages;
    }
}
