package kazikd.dev.server.DTOs;

import kazikd.dev.server.Model.Town;

public record TownDTO(Long id, String name) {
    public static TownDTO fromTown(Town town) {
        return new TownDTO(town.getId(), town.getName());
    }
}
