package app.entities;

public class Documento {
    private final String name;
    private final String content;

    public Documento(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
