package app.persistence;

import app.entities.Documento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileRepository {
    public Documento load(Path path) throws IOException {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        return new Documento(path.getFileName().toString(), content);
    }

    public void save(Path path, String content) throws IOException {
        Files.writeString(path, content == null ? "" : content, StandardCharsets.UTF_8);
    }
}
