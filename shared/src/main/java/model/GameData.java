package model;

import chess.ChessGame;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public GameData withWhiteUsername(String username) {
        return new GameData(gameID, username, blackUsername, gameName, game);
    }

    public GameData withBlackUsername(String username) {
        return new GameData(gameID, whiteUsername, username, gameName, game);
    }
}
