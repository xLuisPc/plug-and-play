package app.dto;

public class SearchMatchDTO {
    private final String word;
    private final int row;
    private final int column;

    public SearchMatchDTO(String word, int row, int column) {
        this.word = word;
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return word + " -> fila " + row + ", columna " + column;
    }
}
