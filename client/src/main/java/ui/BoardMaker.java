package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class BoardMaker {

    public static void makeBoard(ChessBoard board, boolean isWhiteSide, Collection<ChessPosition> highlightedPositions) {
        System.out.println(EscapeSequences.ERASE_SCREEN);

        String[] columns;
        int[] rows;

        if (isWhiteSide) {
            columns = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
            rows = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        } else {
            columns = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
            rows = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        }

        System.out.print("    ");
        for (String col : columns) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " " + col + " " + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.println();

        for (int row : rows) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " " + row + " " + EscapeSequences.RESET_TEXT_COLOR);

            for (String col : columns) {
                ChessPosition position = new ChessPosition(row, colToIndex(col));
                ChessPiece piece = board.getPiece(position);

                boolean isHighlighted = highlightedPositions != null && highlightedPositions.contains(position);
                String squareColor;
                if(!isHighlighted) {
                    squareColor = (row + colToIndex(col)) % 2 == 0
                            ? EscapeSequences.SET_BG_COLOR_BLACK
                            : EscapeSequences.SET_BG_COLOR_WHITE;
                } else {
                    squareColor = (row + colToIndex(col)) % 2 == 0
                            ? EscapeSequences.SET_BG_COLOR_DARK_GREEN
                            : EscapeSequences.SET_BG_COLOR_GREEN;
                }

                System.out.print(squareColor);
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    System.out.print(showPiece(piece));
                }
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println();
        }

        System.out.print("    ");
        for (String col : columns) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " " + col + " " + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.println();

    }

    private static int colToIndex(String col) {
        return col.charAt(0) - 'a' + 1;
    }

    private static String showPiece(ChessPiece piece) {
        switch (piece.getPieceType()) {
            case KING:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            case QUEEN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case ROOK:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case BISHOP:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case KNIGHT:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case PAWN:
                return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            default:
                return EscapeSequences.EMPTY;
        }
    }
}
