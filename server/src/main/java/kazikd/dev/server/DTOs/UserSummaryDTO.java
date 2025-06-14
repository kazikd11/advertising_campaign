package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.User;

// short dto summary for choosing user at the start
public record UserSummaryDTO(Long id, String username) {
    public static UserSummaryDTO fromUser(User user) {
        return new UserSummaryDTO(
                user.getId(),
                user.getUsername()
        );
    }
}
