package results;

import model.GameData;
import model.GameListData;

import java.util.List;

public record ListGamesResult(Boolean success, List<GameListData> games, String message) {
}
