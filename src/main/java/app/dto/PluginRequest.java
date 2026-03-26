package app.dto;

public class PluginRequest {
    private final String text;
    private final String searchTerm;

    public PluginRequest(String text, String searchTerm) {
        this.text = text == null ? "" : text;
        this.searchTerm = searchTerm;
    }

    public String getText() {
        return text;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
