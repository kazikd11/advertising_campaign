package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.User;

public record UserSummaryDTO(Long id, String username) {
    public static UserSummaryDTO fromUser(User user) {
        return new UserSummaryDTO(
            user.getId(),
            user.getUsername()
        );
    }
}
