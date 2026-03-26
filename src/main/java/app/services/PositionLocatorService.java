package app.services;

import app.dto.SearchMatchDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionLocatorService {
    public List<SearchMatchDTO> findMatches(String text, String word) {
        List<SearchMatchDTO> matches = new ArrayList<>();
        if (text == null || text.isBlank() || word == null || word.isBlank()) {
            return matches;
        }

        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int index = matcher.start();
            int row = 1;
            int col = 1;
            for (int i = 0; i < index; i++) {
                if (text.charAt(i) == '\n') {
                    row++;
                    col = 1;
                } else {
                    col++;
                }
            }
            matches.add(new SearchMatchDTO(matcher.group(), row, col));
        }
        return matches;
    }
}
