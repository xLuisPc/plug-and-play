package app.dto;

public class HighlightMatchDTO {
    private final int start;
    private final int end;
    private final String language;
    private final String token;

    public HighlightMatchDTO(int start, int end, String language, String token) {
        this.start = start;
        this.end = end;
        this.language = language;
        this.token = token;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getLanguage() {
        return language;
    }

    public String getToken() {
        return token;
    }
}
