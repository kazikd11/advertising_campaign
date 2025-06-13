package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.Keyword;

public record KeywordDTO(Long id, String keyword) {
    public static KeywordDTO fromKeyword(Keyword keyword) {
        return new KeywordDTO(keyword.getId(), keyword.getKeyword());
    }
}
