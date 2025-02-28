package results;

import model.GameData;

import java.util.List;

public record ListGamesResult(Boolean success, List<GameData> games, String message) {
}
